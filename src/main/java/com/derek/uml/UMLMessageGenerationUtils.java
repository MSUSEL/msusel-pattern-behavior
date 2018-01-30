package com.derek.uml;

import com.derek.uml.srcML.*;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.ArrayList;
import java.util.List;

public class UMLMessageGenerationUtils {

    //need message builders here for anything that can eventually parse down to <expr>.
    //these are: <expr>:(JavaLambda|name|decltype|call|name|initBlock|operator|literal|JavaAnonymousClass|expr|cast|ternary)+;
    /**
     * I will be able to get all messages, but the order will be mewssed up. How do I enforce order?
     * @param block
     * @return
     */
    public static List<UMLMessage> getUMLMessages(SrcMLBlock block){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLNode node : block.getChildNodeOrder()){
            switch(node.getElement().getNodeName()){
                case "block":
                    messages.addAll(getUMLMessages((SrcMLBlock) node));
                    break;
            }
//                //basic expression cases
//                case "argument":
//                case "condition":
//                case "else":
//                case "incr":
//                case "range":
//                case "return":
//                case "then":
//                    messages.add(getUMLMessage(((SrcMLSingleExpression)node).getExpression()));
//                    break;
//                case "argument_list":
//                    messages.addAll(getUMLMessages((SrcMLArgumentList)node));
//                case "control":
//                    messages.addAll(getUMLMessages((SrcMLControl)node));
//                case "if":
//                    //messages.add(getUMLMessage((SrcMLIf)node));
//            }
        }
        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLArgumentList argumentList){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLArgument argument : argumentList.getArguments()){
            UMLMessage message = getUMLMessage(argument.getExpression());
            messages.add(message);
        }
        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLDeclStmt declStmt){
        List<UMLMessage> messages = new ArrayList<>();
        for (SrcMLDecl decl : declStmt.getDecls()){
            UMLMessage message = getUMLMessage(decl.getInit().getExpressions());
            messages.add(message);
        }
        return messages;
    }
    public static List<UMLMessage> getUMLMessages(SrcMLWhile srcMLWhile){
        List<UMLMessage> messages = new ArrayList<>();
        messages.add(getUMLMessage(srcMLWhile.getCondition()));
        messages.addAll(getUMLMessages(srcMLWhile.getBlock()));
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
    public static List<UMLMessage> getUMLMessages(SrcMLControl srcMLControl){
        List<UMLMessage> messages = new ArrayList<>();
        messages.add(getUMLMessage(srcMLControl.getInit()));
        messages.add(getUMLMessage(srcMLControl.getCondition()));
        messages.add(getUMLMessage(srcMLControl.getIncr()));
        return messages;
    }
    public static UMLMessage getUMLMessage(SrcMLIncr srcMLIncr){
        return getUMLMessage(srcMLIncr.getExpression());
    }
    public static UMLMessage getUMLMessage(SrcMLCondition srcMLCondition){
        return getUMLMessage(srcMLCondition.getExpression());
    }
    public static UMLMessage getUMLMessage(SrcMLInit srcMLInit){
        return getUMLMessage(srcMLInit.getExpressions());
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
                String nextParent = parent + "." + call.getName();
                callForest.putEdge(parent, nextParent);
                parent = nextParent;
            }
            //regardless, fill the rest of the forest with edges for each argument.
            for (SrcMLArgument argument : call.getArgumentList().getArguments()) {
                fillCallDepthLayer(callForest, parent, argument.getExpression());
            }
        }
    }
}
