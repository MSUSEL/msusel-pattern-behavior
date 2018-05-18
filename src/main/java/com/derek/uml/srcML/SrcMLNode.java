/**
 * MIT License
 *
 * Copyright (c) 2017 Montana State University, Gianforte School of Computing
 * Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.derek.uml.srcML;

import com.derek.uml.CallTreeNode;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SrcMLNode {
    protected Element element;
    protected List<SrcMLNode> childNodeOrder;

    public SrcMLNode(Element element){
        if (element != null) {
            //will be null for anonymous classes, and I really don't care about them.
            this.element = element;
            collectOrderOfChildNodes();
            parse();
        }
    }

    private void collectOrderOfChildNodes(){
        childNodeOrder = new ArrayList<>();
        List<Element> childElements = XmlUtils.getChildElements(element);
        for (Element e : childElements){
            SrcMLNode nextNode = null;
            switch(e.getNodeName()){
                case "annotation":
                    nextNode = new SrcMLAnnotation(e);
                    break;
                case "annotation_defn":
                    nextNode = new SrcMLAnnotationDefn(e);
                    break;
                case "argument":
                    nextNode = new SrcMLArgument(e);
                    break;
                case "argument_list":
                    nextNode = new SrcMLArgumentList(e);
                    break;
                case "block":
                    nextNode = new SrcMLBlock(e);
                    break;
                case "call":
                    nextNode = new SrcMLCall(e);
                    break;
                case "case":
                    nextNode = new SrcMLCase(e);
                    break;
                case "catch":
                    nextNode = new SrcMLCatch(e);
                    break;
                case "class":
                    nextNode = new SrcMLClass(e);
                    break;
                case "condition":
                    nextNode = new SrcMLCondition(e);
                    break;
                case "constructor":
                case "constructor_decl":
                    nextNode = new SrcMLConstructor(e);
                    break;
                case "continue":
                    nextNode = new SrcMLContinue(e);
                    break;
                case "control":
                    nextNode = new SrcMLControl(e);
                    break;
                case "type":
                    nextNode = new SrcMLDataType(e);
                    break;
                case "decl":
                    nextNode = new SrcMLDecl(e);
                    break;
                case "decl_stmt":
                    nextNode = new SrcMLDeclStmt(e);
                    break;
                case "default":
                    nextNode = new SrcMLDefault(e);
                    break;
                case "else":
                    nextNode = new SrcMLElse(e);
                    break;
                case "enum":
                    nextNode = new SrcMLEnum(e);
                    break;
                case "expr":
                    nextNode = new SrcMLExpression(e);
                    break;
                case "expr_stmt":
                    nextNode = new SrcMLExprStmt(e);
                    break;
                case "extends":
                    nextNode = new SrcMLExtends(e);
                    break;
                case "finally":
                    nextNode = new SrcMLFinally(e);
                    break;
                case "for":
                    nextNode = new SrcMLFor(e);
                    break;
                case "function":
                case "function_decl":
                    nextNode = new SrcMLFunction(e);
                    break;
                case "if":
                case "elseif":
                    nextNode = new SrcMLIf(e);
                    break;
                case "implements":
                    nextNode = new SrcMLImplements(e);
                    break;
                case "import":
                    nextNode = new SrcMLImport(e);
                    break;
                case "incr":
                    nextNode = new SrcMLIncr(e);
                    break;
                case "init":
                    nextNode = new SrcMLInit(e);
                    break;
                case "interface":
                    nextNode = new SrcMLInterface(e);
                    break;
                case "lambda":
                    nextNode = new SrcMLJavaLambda(e);
                    break;
                case "name":
                    nextNode = new SrcMLName(e);
                    break;
                case "package":
                    nextNode = new SrcMLPackage(e);
                    break;
                case "parameter":
                    nextNode = new SrcMLParameter(e);
                    break;
                case "parameter_list":
                    nextNode = new SrcMLParameterList(e);
                    break;
                case "range":
                    nextNode = new SrcMLRange(e);
                    break;
                case "return":
                    nextNode = new SrcMLReturn(e);
                    break;
                case "static":
                    nextNode = new SrcMLStaticBlock(e);
                    break;
                case "super":
                    nextNode = new SrcMLSuper(e);
                    break;
                case "switch":
                    nextNode = new SrcMLSwitch(e);
                    break;
                case "synchronized":
                    nextNode = new SrcMLSynchronized(e);
                    break;
                case "ternary":
                    nextNode = new SrcMLTernary(e);
                    break;
                case "then":
                    nextNode = new SrcMLThen(e);
                    break;
                case "throws":
                case "throw":
                    nextNode = new SrcMLThrows(e);
                    break;
                case "try":
                    nextNode = new SrcMLTry(e);
                    break;
                case "do":
                case "while":
                    nextNode = new SrcMLWhile(e);
                    break;
                case "specifier":
                case "modifier":
                case "operator":
                case "literal":
                case "comment":
                case "index":
                case "break":
                case "empty_stmt":
                    //label is like a goto statement.... I don't think I need to worry.
                case "label":
                    //do nothing; im leaving these in here because they are in teh srcml spec but I may want to do something with them in the future
                    break;
                default:
                    System.out.println("did not find element " + e.getNodeName() + "  this should have been found.");
                    System.exit(0);
                    break;
            }
            if (nextNode != null) {
                childNodeOrder.add(nextNode);
            }
        }
    }
    protected List<SrcMLAnnotation> parseAnnotations(){
        List<SrcMLAnnotation> annotations = new ArrayList<>();
        for (SrcMLNode annotation : getSameNameElements("annotation")){
            annotations.add((SrcMLAnnotation)annotation);
        }
        return annotations;
    }
    protected List<SrcMLAnnotationDefn> parseAnnotationDefns(){
        List<SrcMLAnnotationDefn> annotationDefns = new ArrayList<>();
        for (SrcMLNode annotationDefn : getSameNameElements("annotation_defn")){
            annotationDefns.add((SrcMLAnnotationDefn)annotationDefn);
        }
        return annotationDefns;
    }
    protected SrcMLName parseName(){
        return parseNames().isEmpty() ? null : parseNames().get(0);
    }
    protected SrcMLSuper parseSuper(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("super");
        return srcMLNodes.isEmpty() ? null : (SrcMLSuper)srcMLNodes.get(0);
    }
    protected SrcMLInit parseInit(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("init");
        return srcMLNodes.isEmpty() ? null : (SrcMLInit)srcMLNodes.get(0);
    }
    protected SrcMLThen parseThen(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("then");
        return srcMLNodes.isEmpty() ? null : (SrcMLThen)srcMLNodes.get(0);
    }
    protected SrcMLElse parseElse(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("else");
        return srcMLNodes.isEmpty() ? null : (SrcMLElse)srcMLNodes.get(0);
    }
    protected SrcMLCondition parseCondition(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("condition");
        return srcMLNodes.isEmpty() ? null : (SrcMLCondition)srcMLNodes.get(0);
    }
    protected SrcMLIncr parseIncr(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("incr");
        return srcMLNodes.isEmpty() ? null : (SrcMLIncr)srcMLNodes.get(0);
    }
    protected SrcMLRange parseRange(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("range");
        return srcMLNodes.isEmpty() ? null : (SrcMLRange)srcMLNodes.get(0);
    }

    protected SrcMLDataType parseDataType(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("type");
        return srcMLNodes.isEmpty() ? null : (SrcMLDataType) srcMLNodes.get(0);
    }
    protected SrcMLControl parseControl(){
        List<SrcMLNode> srcMLNodes = getSameNameElements("control");
        return srcMLNodes.isEmpty() ? null : (SrcMLControl) srcMLNodes.get(0);
    }
    protected SrcMLBlock parseBlock(){
        return parseBlocks().isEmpty() ? null : parseBlocks().get(0);
    }
    protected List<SrcMLBlock> parseBlocks(){
        List<SrcMLBlock> blocks = new ArrayList<>();
        for (SrcMLNode block : getSameNameElements("block")){
            blocks.add((SrcMLBlock)block);
        }
        return blocks;
    }
    protected List<SrcMLElse> parseElses(){
        List<SrcMLElse> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("else")){
            nodes.add((SrcMLElse)node);
        }
        return nodes;
    }
    protected List<SrcMLDecl> parseDecls(){
        List<SrcMLDecl> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("decl")){
            nodes.add((SrcMLDecl) node);
        }
        return nodes;
    }
    protected List<SrcMLIf> parseElseIfs(){
        List<SrcMLIf> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("elseif")){
            nodes.add((SrcMLIf)node);
        }
        return nodes;
    }
    protected SrcMLDefault parseDefault(){
        return parseDefaults().isEmpty() ? null : parseDefaults().get(0);
    }
    protected List<SrcMLDefault> parseDefaults(){
        List<SrcMLDefault> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("default")){
            nodes.add((SrcMLDefault)node);
        }
        return nodes;
    }
    protected List<SrcMLFinally> parseFinallies(){
        List<SrcMLFinally> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("finally")){
            nodes.add((SrcMLFinally)node);
        }
        return nodes;
    }
    protected List<SrcMLClass> parseClasses(){
        List<SrcMLClass> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("class")){
            nodes.add((SrcMLClass)node);
        }
        return nodes;
    }
    protected SrcMLIf parseElseIf(){
        return parseElseIfs().isEmpty() ? null : parseElseIfs().get(0);
    }
    protected List<SrcMLThrows> parseThrows(){
        List<SrcMLThrows> throws1 = new ArrayList<>();
        for (SrcMLNode throw1 : getSameNameElements("throws")){
            throws1.add((SrcMLThrows)throw1);
        }
        return throws1;
    }
    protected List<SrcMLName> parseNames(){
        List<SrcMLName> names = new ArrayList<>();
        for (SrcMLNode name : getSameNameElements("name")){
            names.add((SrcMLName)name);
        }
        return names;
    }
    protected SrcMLArgumentList parseArgumentList(){
        return parseArgumentLists().isEmpty() ? null : parseArgumentLists().get(0);
    }
    protected List<SrcMLArgumentList> parseArgumentLists(){
        List<SrcMLArgumentList> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("argument_list")){
            nodes.add((SrcMLArgumentList)node);
        }
        return nodes;
    }

    protected List<SrcMLExpression> parseExpressions(){
        List<SrcMLExpression> expressions = new ArrayList<>();
            for (SrcMLNode expression : getSameNameElements("expr")){
                expressions.add((SrcMLExpression)expression);
            }
        return expressions;
    }
    protected SrcMLParameterList parseParameterList(){
        return parseParameterLists().isEmpty() ? null : parseParameterLists().get(0);
    }
    protected List<SrcMLArgument> parseArguments(){
        List<SrcMLArgument> arguments = new ArrayList<>();
        for (SrcMLNode argument : getSameNameElements("argument")){
            arguments.add((SrcMLArgument)argument);
        }
        return arguments;
    }
    protected List<SrcMLParameterList> parseParameterLists(){
        List<SrcMLParameterList> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("parameter_list")){
            nodes.add((SrcMLParameterList)node);
        }
        return nodes;
    }
    protected List<SrcMLNode> getSameNameElements(String searchString){
        List<SrcMLNode> nodes = new ArrayList<>();
        if (!childNodeOrder.isEmpty()){
            for (SrcMLNode srcMLNode : childNodeOrder){
                if (searchString.equals(srcMLNode.getElement().getNodeName())){
                    nodes.add(srcMLNode);
                }
            }
        }
        return nodes;
    }
    protected List<String> parseModifiers(){
        List<String> modifiers = new ArrayList<>();
        List<Node> modifierNodes = XmlUtils.getImmediateChildren(element, "modifier");
        for (Node modifierNode : modifierNodes){
            modifiers.add(XmlUtils.elementify(modifierNode).getTextContent());
        }
        return modifiers;
    }
    protected String parseModifier(){
        return parseModifiers().isEmpty() ? "" : parseModifiers().get(0);
    }
    protected List<String> parseSpecifiers(){
        List<String> specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(element, "specifier");
        for (Node specifierNode : specifierNodes){
            specifiers.add(XmlUtils.elementify(specifierNode).getTextContent());
        }
        return specifiers;
    }
    protected List<String> parseOperators(){
        List<String> operators = new ArrayList<>();
        List<Node> operatorNodes = XmlUtils.getImmediateChildren(element, "operator");
        for (Node operatorNode : operatorNodes){
            operators.add(XmlUtils.elementify(operatorNode).getTextContent());
        }
        return operators;
    }

    protected abstract void parse();


    //tree is really just the root of the tree underneath.
    protected CallTreeNode<SrcMLNode> buildCallTree(CallTreeNode<SrcMLNode> root, SrcMLExpression expression){
        fillCallTree(root, root.getName(), expression);
        return root;
    }

    private void fillCallTree(CallTreeNode<SrcMLNode> callTree, SrcMLNode parent, SrcMLExpression childNode) {
        if (childNode != null) {
            List<SrcMLCall> calls = childNode.getCalls();
            for (SrcMLCall call : calls) {
                CallTreeNode<SrcMLNode> nextChild = new CallTreeNode<>(call, "call");
                callTree.addChild(nextChild);
                //regardless, fill the rest of the forest with edges for each argument.
                for (SrcMLArgument argument : call.getArgumentList().getArguments()) {
                    fillCallTree(nextChild, call, argument.getExpression());
                }
            }
            List<SrcMLJavaLambda> lambdas = childNode.getJavaLambdas();
            for (SrcMLJavaLambda lambda : lambdas){
                CallTreeNode<SrcMLNode> nextChild = lambda.getCallTree();
                callTree.addChild(nextChild);
            }
        }
    }
}
