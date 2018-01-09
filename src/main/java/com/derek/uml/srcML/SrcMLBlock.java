package com.derek.uml.srcML;

import com.derek.uml.UMLAttribute;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SrcMLBlock {
    // topLevelElements:(comment|assertStmt|block|break|case|classInterfaceOrAnnotation|constructor|constructorDecl|continue|
    // declStmt|defaultCase|do|if|else|elseif|exprStmt|JavaEnum|finally|for|JavaFunction|JavaFunctionDecl|
    // import|package|specifier|return|staticBlock|switch|throw|try|while;

    //some of the documented elements here arent' needed:
    //catch|multiTypeCatch|emptyStmt|goto|label|
    private Element blockEle;
    private List<SrcMLBlock> blocks;
    private List<SrcMLCase> cases;
    private List<SrcMLClass> classes;
    private List<SrcMLInterface> interfaces;
    private List<SrcMLAnnotationDefn> annotations;
    private List<SrcMLConstructor> constructors;
    private List<SrcMLConstructor> constructors_decl;
    private List<SrcMLContinue> continues;
    private List<SrcMLDeclStmt> declStmts;
    private List<SrcMLDefault> defaults;
    private List<SrcMLWhile> dos;
    private List<SrcMLIf> ifs;
    private List<SrcMLElse> elses;
    private List<SrcMLIf> elseIfs;
    private List<SrcMLExprStmt> expr_stmts;
    private List<SrcMLEnum> enums;
    private List<SrcMLFinally> finallies;
    private List<SrcMLFor> fors;
    private List<SrcMLFunction> functions;
    private List<SrcMLFunction> functionDecls;
    private List<SrcMLImport> imports;
    private List<SrcMLPackage> packages;
    private List<String> specifiers;
    private List<SrcMLReturn> returns;
    private List<SrcMLStaticBlock> statics;
    private List<SrcMLSwitch> switches;
    private List<SrcMLThrows> throws1;
    private List<SrcMLTry> tries;
    private List<SrcMLWhile> whiles;
    private List<SrcMLDecl> decls;

    public SrcMLBlock(Element blockEle){
        this.blockEle = blockEle;
        parse();
    }
    private void parse(){
        parseComments();
        parseAssert();
        parseBlock();
        parseCase();
        parseClass();
        parseInterface();
        parseAnnotation();
        parseConstructor();
        parseConstructorDecl();
        parseContinue();
        parseDeclStmt();
        parseDefault();
        parseDo();
        parseIf();
        parseElse();
        parseElseIf();
        parseExprStmt();
        parseJavaEnum();
        parseFinally();
        parseFor();
        parseJavaFunction();
        parseJavaFunctionDecl();
        parseImport();
        parsePackage();
        parseSpecifier();
        parseReturn();
        parseStaticBlock();
        parseSwitch();
        parseThrow();
        parseTry();
        parseWhile();
        //decl isn't explicitly included in the documentation, but it exists for enum blocks (which are blocks)
        parseDecl();
    }
    private void parseComments(){
        //dont think I care about comments
    }
    private void parseAssert(){
        //dont care
    }
    private void parseBlock(){
        blocks = new ArrayList<>();
        List<Node> blockNodes = XmlUtils.getImmediateChildren(blockEle, "block");
        for (Node blockNode : blockNodes){
            blocks.add(new SrcMLBlock(XmlUtils.elementify(blockNode)));
        }
    }
    private void parseCase(){
        cases = new ArrayList<>();
        List<Node> caseNodes = XmlUtils.getImmediateChildren(blockEle, "case");
        for (Node caseNode : caseNodes){
            cases.add(new SrcMLCase(XmlUtils.elementify(caseNode)));
        }
    }
    private void parseClass(){
        classes = new ArrayList<>();
        List<Node> classNodes = XmlUtils.getImmediateChildren(blockEle, "class");
        for (Node classNode : classNodes){
            classes.add(new SrcMLClass(XmlUtils.elementify(classNode)));
        }
    }
    private void parseInterface(){
        interfaces = new ArrayList<>();
        List<Node> interfaceNodes = XmlUtils.getImmediateChildren(blockEle, "interface");
        for (Node interfaceNode : interfaceNodes){
            interfaces.add(new SrcMLInterface(XmlUtils.elementify(interfaceNode)));
        }
    }
    private void parseAnnotation(){
        annotations = new ArrayList<>();
        List<Node> annotation_defnNodes = XmlUtils.getImmediateChildren(blockEle, "annotation_defn");
        for (Node annotation_defnNode : annotation_defnNodes){
            annotations.add(new SrcMLAnnotationDefn(XmlUtils.elementify(annotation_defnNode)));
        }
    }
    private void parseConstructor(){
        constructors = new ArrayList<>();
        List<Node> constructorNodes = XmlUtils.getImmediateChildren(blockEle, "constructor");
        for (Node constructorNode : constructorNodes){
            constructors.add(new SrcMLConstructor(XmlUtils.elementify(constructorNode)));
        }
    }
    private void parseConstructorDecl(){
        constructors_decl = new ArrayList<>();
        List<Node> constructorNodes = XmlUtils.getImmediateChildren(blockEle, "constructor_decl");
        for (Node constructorNode : constructorNodes){
            constructors_decl.add(new SrcMLConstructor(XmlUtils.elementify(constructorNode)));
        }
    }
    private void parseContinue(){
        continues = new ArrayList<>();
        List<Node> continueNodes = XmlUtils.getImmediateChildren(blockEle, "continue");
        for (Node continueNode : continueNodes){
            continues.add(new SrcMLContinue(XmlUtils.elementify(continueNode)));
        }
    }
    private void parseDeclStmt(){
        declStmts = new ArrayList<>();
        List<Node> declStmtNodes = XmlUtils.getImmediateChildren(blockEle, "decl_stmt");
        for (Node declStmtNode : declStmtNodes){
            declStmts.add(new SrcMLDeclStmt(XmlUtils.elementify(declStmtNode)));
        }
    }
    private void parseDefault(){
        defaults = new ArrayList<>();
        List<Node> defaultNodes = XmlUtils.getImmediateChildren(blockEle, "default");
        for (Node defaultNode : defaultNodes){
            defaults.add(new SrcMLDefault(XmlUtils.elementify(defaultNode)));
        }
    }
    private void parseDo(){
        dos = new ArrayList<>();
        List<Node> doNodes = XmlUtils.getImmediateChildren(blockEle, "do");
        for (Node doNode : doNodes){
            dos.add(new SrcMLWhile(XmlUtils.elementify(doNode)));
        }
    }
    private void parseIf(){
        ifs = new ArrayList<>();
        List<Node> ifNodes = XmlUtils.getImmediateChildren(blockEle, "if");
        for (Node ifNode : ifNodes){
            ifs.add(new SrcMLIf(XmlUtils.elementify(ifNode)));
        }
    }
    private void parseElse(){
        elses = new ArrayList<>();
        List<Node> elseNodes = XmlUtils.getImmediateChildren(blockEle, "else");
        for (Node elseNode : elseNodes){
            elses.add(new SrcMLElse(XmlUtils.elementify(elseNode)));
        }
    }
    private void parseElseIf(){
        elseIfs = new ArrayList<>();
        List<Node> ifNodes = XmlUtils.getImmediateChildren(blockEle, "elseif");
        for (Node ifNode : ifNodes){
            elseIfs.add(new SrcMLIf(XmlUtils.elementify(ifNode)));
        }
    }
    private void parseExprStmt(){
        expr_stmts = new ArrayList<>();
        List<Node> expr_stmtNodes = XmlUtils.getImmediateChildren(blockEle, "expr_stmt");
        for (Node expr_stmtNode : expr_stmtNodes){
            expr_stmts.add(new SrcMLExprStmt(XmlUtils.elementify(expr_stmtNode)));
        }
    }
    private void parseJavaEnum(){
        enums = new ArrayList<>();
        List<Node> enumNodes = XmlUtils.getImmediateChildren(blockEle, "enum");
        for (Node enumNode : enumNodes){
            enums.add(new SrcMLEnum(XmlUtils.elementify(enumNode)));
        }
    }
    private void parseFinally(){
        finallies = new ArrayList<>();
        List<Node> finallyNodes = XmlUtils.getImmediateChildren(blockEle, "finally");
        for (Node finallyNode : finallyNodes){
            finallies.add(new SrcMLFinally(XmlUtils.elementify(finallyNode)));
        }
    }
    private void parseFor(){
        fors = new ArrayList<>();
        List<Node> forNodes = XmlUtils.getImmediateChildren(blockEle, "for");
        for (Node forNode : forNodes){
            fors.add(new SrcMLFor(XmlUtils.elementify(forNode)));
        }
    }
    private void parseJavaFunction(){
        functions = new ArrayList<>();
        List<Node> functionNodes = XmlUtils.getImmediateChildren(blockEle, "function");
        for (Node functionNode : functionNodes){
            functions.add(new SrcMLFunction(XmlUtils.elementify(functionNode)));
        }
    }
    private void parseJavaFunctionDecl(){
        functionDecls = new ArrayList<>();
        List<Node> functionNodes = XmlUtils.getImmediateChildren(blockEle, "function_decl");
        for (Node functionNode : functionNodes){
            functionDecls.add(new SrcMLFunction(XmlUtils.elementify(functionNode)));
        }
    }
    private void parseImport(){
        imports = new ArrayList<>();
        List<Node> importNodes = XmlUtils.getImmediateChildren(blockEle, "import");
        for (Node importNode : importNodes){
            imports.add(new SrcMLImport(XmlUtils.elementify(importNode)));
        }
    }
    private void parsePackage(){
        packages = new ArrayList<>();
        List<Node> packageNodes = XmlUtils.getImmediateChildren(blockEle, "package");
        for (Node packageNode : packageNodes){
            packages.add(new SrcMLPackage(XmlUtils.elementify(packageNode)));
        }
    }
    private void parseSpecifier(){
        specifiers = new ArrayList<>();
        List<Node> specifierNodes = XmlUtils.getImmediateChildren(blockEle, "specifier");
        for (Node specifierNode : specifierNodes){
            specifiers.add(specifierNode.getTextContent());
        }
    }
    private void parseReturn(){
        returns = new ArrayList<>();
        List<Node> returnNodes = XmlUtils.getImmediateChildren(blockEle, "return");
        for (Node returnNode : returnNodes){
            returns.add(new SrcMLReturn(XmlUtils.elementify(returnNode)));
        }
    }
    private void parseStaticBlock(){
        statics = new ArrayList<>();
        List<Node> staticNodes = XmlUtils.getImmediateChildren(blockEle, "static");
        for (Node staticNode : staticNodes){
            statics.add(new SrcMLStaticBlock(XmlUtils.elementify(staticNode)));
        }
    }
    private void parseSwitch(){
        switches = new ArrayList<>();
        List<Node> switchNodes = XmlUtils.getImmediateChildren(blockEle, "switch");
        for (Node switchNode : switchNodes){
            switches.add(new SrcMLSwitch(XmlUtils.elementify(switchNode)));
        }
    }
    private void parseThrow(){
        throws1 = new ArrayList<>();
        List<Node> throwsNodes = XmlUtils.getImmediateChildren(blockEle, "throws");
        for (Node throwsNode : throwsNodes){
            throws1.add(new SrcMLThrows(XmlUtils.elementify(throwsNode)));
        }
    }
    private void parseTry(){
        tries = new ArrayList<>();
        List<Node> tryNodes = XmlUtils.getImmediateChildren(blockEle, "try");
        for (Node tryNode : tryNodes){
            tries.add(new SrcMLTry(XmlUtils.elementify(tryNode)));
        }
    }
    private void parseWhile(){
        whiles = new ArrayList<>();
        List<Node> whileNodes = XmlUtils.getImmediateChildren(blockEle, "while");
        for (Node whileNode : whileNodes){
            whiles.add(new SrcMLWhile(XmlUtils.elementify(whileNode)));
        }
    }
    private void parseDecl(){
        decls = new ArrayList<>();
        List<Node> declNodes = XmlUtils.getImmediateChildren(blockEle, "decl");
        for (Node declNode : declNodes){
            decls.add(new SrcMLDecl(XmlUtils.elementify(declNode)));
        }
    }

    public class SrcMLCase{
        @Getter private Element caseEle;
        @Getter SrcMLName name;
        @Getter String literal;

        public SrcMLCase(Element caseEle) {
            this.caseEle = caseEle;
            parse();
        }
        private void parse(){
            parseName();
            parseLiteral();
        }
        private void parseName(){
            List<Node> nameNodes = XmlUtils.getImmediateChildren(caseEle, "name");
            for (Node nameNode : nameNodes){
                name = new SrcMLName(XmlUtils.elementify(nameNode));
            }
        }
        private void parseLiteral(){
            List<Node> nameNodes = XmlUtils.getImmediateChildren(caseEle, "literal");
            for (Node nameNode : nameNodes){
                literal = nameNode.getTextContent();
            }
        }
    }
    public class SrcMLContinue{
        @Getter
        private Element continueEle;
        private SrcMLName name;

        public SrcMLContinue(Element continueEle) {
            this.continueEle = continueEle;
            parse();
        }
        private void parse(){
            parseName();
        }
        private void parseName(){
            List<Node> nameNodes = XmlUtils.getImmediateChildren(continueEle, "name");
            for (Node nameNode : nameNodes){
                name = new SrcMLName(XmlUtils.elementify(nameNode));
            }
        }
    }
    public class SrcMLDeclStmt{
        @Getter
        private Element declStmtEle;
        private List<SrcMLDecl> decls;

        public SrcMLDeclStmt(Element declStmtEle) {
            this.declStmtEle = declStmtEle;
            parse();
        }
        private void parse(){
            decls = new ArrayList<>();
            List<Node> declNodes = XmlUtils.getImmediateChildren(declStmtEle, "decl");
            for (Node declNode : declNodes){
                decls.add(new SrcMLDecl(XmlUtils.elementify(declNode)));
            }
        }
    }
    public class SrcMLExprStmt{
        @Getter
        private Element exprStmtEle;
        @Getter
        private List<SrcMLExpression> expressions;
        private List<String> operators;

        public SrcMLExprStmt(Element exprStmtEle) {
            this.exprStmtEle = exprStmtEle;
            parse();
        }
        private void parse(){
            parseExpressions();
            parseOperators();
        }
        private void parseExpressions(){
            expressions = new ArrayList<>();
            List<Node> expressionNodes = XmlUtils.getImmediateChildren(exprStmtEle, "expr");
            for (Node expressionNode : expressionNodes){
                expressions.add(new SrcMLExpression(XmlUtils.elementify(expressionNode)));
            }
        }
        private void parseOperators(){
            operators = new ArrayList<>();
            List<Node> operatorNodes = XmlUtils.getImmediateChildren(exprStmtEle, "operator");
            for (Node operatorNode : operatorNodes){
                operators.add(operatorNode.getTextContent());
            }
        }
    }
    public class SrcMLEnum{
        @Getter
        private Element enumEle;
        private List<SrcMLAnnotation> annotations;
        private SrcMLName name;
        private SrcMLEnumBlock enumBlock;

        public SrcMLEnum(Element enumEle) {
            this.enumEle = enumEle;
            parse();
        }
        private void parse(){
            parseAnnotations();
            parseName();
            parseEnumBlock();
        }
        private void parseAnnotations(){
            annotations = new ArrayList<>();
            List<Node> annotationNodes = XmlUtils.getImmediateChildren(enumEle, "annotation");
            for (Node annotationNode : annotationNodes){
                annotations.add(new SrcMLAnnotation(XmlUtils.elementify(annotationNode)));
            }
        }
        private void parseName(){
            List<Node> nameNodes = XmlUtils.getImmediateChildren(enumEle, "name");
            for (Node nameNode : nameNodes){
                name = new SrcMLName(XmlUtils.elementify(nameNode));
            }
        }
        private void parseEnumBlock(){
            List<Node> enumBlockNodes = XmlUtils.getImmediateChildren(enumEle, "block");
            for (Node enumBlockNode : enumBlockNodes){
                enumBlock = new SrcMLEnumBlock(XmlUtils.elementify(enumBlockNode));
            }
        }
        public class SrcMLEnumBlock{
            @Getter
            private Element enumBlockEle;
            private List<SrcMLDecl> decls;
            private List<SrcMLBlock> blocks;

            public SrcMLEnumBlock(Element enumBlockEle) {
                this.enumBlockEle = enumBlockEle;
                parse();
            }
            private void parse(){
                parseDecls();
                parseBlocks();
            }
            private void parseDecls(){
                decls = new ArrayList<>();
                List<Node> declNodes = XmlUtils.getImmediateChildren(enumBlockEle, "decl");
                for (Node declNode : declNodes){
                    decls.add(new SrcMLDecl(XmlUtils.elementify(declNode)));
                }
            }
            private void parseBlocks(){
                blocks = new ArrayList<>();
                List<Node> blockNodes = XmlUtils.getImmediateChildren(enumBlockEle, "block");
                for (Node blockNode : blockNodes){
                    blocks.add(new SrcMLBlock(XmlUtils.elementify(blockNode)));
                }
            }
        }

    }
    public class SrcMLReturn{
        @Getter
        private Element returnEle;
        private SrcMLExpression expression;

        public SrcMLReturn(Element returnEle) {
            this.returnEle = returnEle;
            parse();
        }
        private void parse(){
            parseExpression();
        }
        private void parseExpression(){
            List<Node> exprNodes = XmlUtils.getImmediateChildren(returnEle, "expr");
            for (Node exprNode : exprNodes){
                expression = new SrcMLExpression(XmlUtils.elementify(exprNode));
            }
        }
    }
    public class SrcMLStaticBlock{
        @Getter
        private Element staticEle;
        private SrcMLBlock block;

        public SrcMLStaticBlock(Element staticEle) {
            this.staticEle = staticEle;
            parse();
        }
        private void parse(){
            parseBlock();
        }
        private void parseBlock(){
            List<Node> staticNodes = XmlUtils.getImmediateChildren(staticEle, "block");
            for (Node staticNode : staticNodes){
                block = new SrcMLBlock(XmlUtils.elementify(staticNode));
            }
        }
    }


}
