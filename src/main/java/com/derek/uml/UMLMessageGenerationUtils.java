package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {

    //need message builders here for anything that can eventually parse down to <expr>.

    /**
     * I will be able to get all messages, but the order will be mewssed up. How do I enforce order?
     * @param block
     * @return
     */
    public static List<UMLMessage> getUMLMessages(SrcMLBlock block){
        List<UMLMessage> messages = new ArrayList<>();
        List<UMLMessage> expressions = new ArrayList<>();
        List<UMLMessage> ifs = new ArrayList<>();
        List<UMLMessage> fors = new ArrayList<>();
        for (SrcMLDeclStmt declStmt : block.getDeclStmts()){
            for (SrcMLDecl decl : declStmt.getDecls()){
                UMLMessage message = getUMLMessage(decl.getInit().getExpressions());
                messages.add(message);
            }
        }
        for (SrcMLBlock.SrcMLExprStmt exprStmt : block.getExpr_stmts()){
            for (SrcMLExpression expression : exprStmt.getExpressions()){
                expressions.add(getUMLMessage(expression));
            }
        }
        for (SrcMLIf srcMLIf : block.getIfs()){
            ifs = getUMLMessages(srcMLIf);
        }
        for (SrcMLFor srcMLFor : block.getFors()){
            fors = getUMLMessages(srcMLFor);
        }

        //order();

        messages.addAll(ifs);

        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLFor srcMLFor){
        List<UMLMessage> messages = new ArrayList<>();
        List<UMLMessage> controlLoops = getUMLMessages(srcMLFor.getControl());
        List<UMLMessage> blockMessages = getUMLMessages(srcMLFor.getBlock());
        messages.addAll(controlLoops);
        messages.addAll(blockMessages);
        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLFor.SrcMLControl srcMLControl){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLExpression expression : srcMLControl.getInit().getExpressions()){
            //each init can have multiple expressions, and therefore calls - this code is an example: for (int i = 0, j = 9; i < 5 && j > 8; i++, j++)
            //although if this is true then its going to be very hard to track control characters (how do I match i to the other i's in the above example)
            messages.add(getUMLMessage(expression));
        }
        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLInit srcMLInit){
        return null;
        // /return getUMLMessages(srcMLInit.getExpressions());
    }

    public static List<UMLMessage> getUMLMessages(SrcMLIf srcMLIf){
        List<UMLMessage> messages = new ArrayList<>();
        UMLMessage ifMessage = getUMLMessage(srcMLIf.getCondition().getExpression());
        List<UMLMessage> thenMessages = getUMLMessages(srcMLIf.getThen());
        List<UMLMessage> elseMessages = new ArrayList<>();
        if (srcMLIf.getElse1() != null) {
            elseMessages = getUMLMessages(srcMLIf.getElse1());
        }
        List<UMLMessage> ifElseMessages = new ArrayList<>();
        if (srcMLIf.getElseIf() != null) {
            ifElseMessages = getUMLMessages(srcMLIf.getElseIf());
        }

        messages.add(ifMessage);
        messages.addAll(thenMessages);
        messages.addAll(elseMessages);
        messages.addAll(ifElseMessages);
        return messages;
    }

    public static List<UMLMessage> getUMLMessages(SrcMLThen srcMLThen){
        List<UMLMessage> messages = new ArrayList<>();
        List<UMLMessage> blocks = new ArrayList<>();
        List<UMLMessage> expressions = new ArrayList<>();
        if (srcMLThen.getBlock() != null){
            blocks = getUMLMessages(srcMLThen.getBlock());
        }
        if (srcMLThen.getExpression() != null){
            expressions.add(getUMLMessage(srcMLThen.getExpression()));
        }
        messages.addAll(blocks);
        messages.addAll(expressions);
        return messages;
    }

    public static List<UMLMessage> getUMLMessages(SrcMLElse srcMLElse){
        List<UMLMessage> messages = new ArrayList<>();
        List<UMLMessage> blocks = new ArrayList<>();
        List<UMLMessage> expressions = new ArrayList<>();
        if (srcMLElse.getBlock() != null){
            blocks = getUMLMessages(srcMLElse.getBlock());
        }
        if (srcMLElse.getExpression() != null){
            expressions.add(getUMLMessage(srcMLElse.getExpression()));
        }
        messages.addAll(blocks);
        messages.addAll(expressions);
        return messages;
    }

    public static UMLMessage getUMLMessage(List<SrcMLExpression> expressions){
        MutableGraph<String> callForest = GraphBuilder.directed().allowsSelfLoops(false).build();
        for (SrcMLExpression expression : expressions){
            //should generally only be 1 expression
            fillCallDepthLayer(callForest, "", expression);
        }
        return new UMLMessage(callForest);
    }

    /**
     * similar to the one above (getUMLExpressionMessage()) but only uses 1 expression as input
     * @param expression
     * @return
     */
    public static UMLMessage getUMLMessage(SrcMLExpression expression){
        MutableGraph<String> callForest = GraphBuilder.directed().allowsSelfLoops(false).build();
        fillCallDepthLayer(callForest, "", expression);
        return new UMLMessage(callForest);
    }

    private static void fillCallDepthLayer(MutableGraph<String> callForest, String parent, SrcMLExpression expression){
        List<SrcMLCall> calls = expression.getCalls();
        for (SrcMLCall call : calls) {
            if (parent.equals("")){
                //first time through forest, add a new root as a caller..
                parent = call.getName();
                callForest.addNode(parent);
            }else{
                //we already have a parent
                callForest.putEdge(parent, call.getName());
                parent = call.getName();
            }
            //regardless, fill the rest of the forest with edges for each argument.
            for (SrcMLArgument argument : call.getArgumentList().getArguments()) {
                fillCallDepthLayer(callForest, parent, argument.getExpression());
            }
        }
    }
}
