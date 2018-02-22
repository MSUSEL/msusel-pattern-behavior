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
public class SrcMLBlock extends SrcMLNode{
    // topLevelElements:(comment|assertStmt|block|break|case|classInterfaceOrAnnotation|constructor|constructorDecl|continue|
    // declStmt|defaultCase|do|if|else|elseif|exprStmt|JavaEnum|finally|for|JavaFunction|JavaFunctionDecl|
    // import|package|specifier|return|staticBlock|switch|throw|try|while;

    //some of the documented elements here arent' needed:
    //catch|multiTypeCatch|emptyStmt|goto|label|
    private List<SrcMLBlock> blocks;
    private List<SrcMLCase> cases;
    private List<SrcMLClass> classes;
    private List<SrcMLInterface> interfaces;
    private List<SrcMLAnnotationDefn> annotationDefns;
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
        super(blockEle);
    }
    protected void parse(){
        parseComments();
        parseAssert();
        blocks = parseBlocks();
        cases = parseCases();
        classes = parseClasses();
        interfaces = parseInterfaces();
        annotationDefns = parseAnnotationDefns();
        constructors = parseConstructors();
        constructors_decl = parseConstructorDecls();
        continues = parseContinues();
        declStmts = parseDeclStmts();
        defaults = parseDefaults();
        dos = parseDos();
        ifs = parseIfs();
        elses = parseElses();
        elseIfs = parseElseIfs();
        expr_stmts = parseExprStmts();
        enums = parseEnums();
        finallies = parseFinallies();
        fors = parseFors();
        functions = parseFunctions();
        functionDecls = parseFunctionDecls();
        imports = parseImports();
        packages = parsePackages();
        specifiers = parseSpecifiers();
        returns = parseReturns();
        statics = parseStaticBlocks();
        switches = parseSwitches();
        throws1 = parseThrows();
        tries = parseTries();
        whiles = parseWhiles();
        //decl isn't explicitly included in the documentation, but it exists for enum blocks (which are blocks)
        decls = parseDecls();
    }
    private void parseComments(){
        //dont think I care about comments
    }
    private void parseAssert(){
        //dont care
    }

    private List<SrcMLCase> parseCases(){
        List<SrcMLCase> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("case")){
            nodes.add((SrcMLCase)node);
        }
        return nodes;
    }
    private List<SrcMLInterface> parseInterfaces(){
        List<SrcMLInterface> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("interface")){
            nodes.add((SrcMLInterface)node);
        }
        return nodes;
    }
    private List<SrcMLConstructor> parseConstructors(){
        List<SrcMLConstructor> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("constructor")){
            nodes.add((SrcMLConstructor)node);
        }
        return nodes;
    }
    private List<SrcMLConstructor> parseConstructorDecls(){
        List<SrcMLConstructor> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("constructor_decl")){
            nodes.add((SrcMLConstructor)node);
        }
        return nodes;
    }
    private List<SrcMLContinue> parseContinues(){
        List<SrcMLContinue> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("continue")){
            nodes.add((SrcMLContinue)node);
        }
        return nodes;
    }
    private List<SrcMLDeclStmt> parseDeclStmts(){
        List<SrcMLDeclStmt> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("decl_stmt")){
            nodes.add((SrcMLDeclStmt)node);
        }
        return nodes;
    }
    private List<SrcMLWhile> parseDos(){
        List<SrcMLWhile> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("do")){
            nodes.add((SrcMLWhile)node);
        }
        return nodes;
    }
    private List<SrcMLIf> parseIfs(){
        List<SrcMLIf> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("if")){
            nodes.add((SrcMLIf)node);
        }
        return nodes;
    }
    private List<SrcMLExprStmt> parseExprStmts(){
        List<SrcMLExprStmt> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("expr_stmt")){
            nodes.add((SrcMLExprStmt)node);
        }
        return nodes;
    }
    private List<SrcMLEnum> parseEnums(){
        List<SrcMLEnum> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("enum")){
            nodes.add((SrcMLEnum)node);
        }
        return nodes;
    }
    private List<SrcMLFor> parseFors(){
        List<SrcMLFor> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("for")){
            nodes.add((SrcMLFor)node);
        }
        return nodes;
    }
    private List<SrcMLFunction> parseFunctions(){
        List<SrcMLFunction> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("function")){
            nodes.add((SrcMLFunction)node);
        }
        return nodes;
    }
    private List<SrcMLFunction> parseFunctionDecls(){
        List<SrcMLFunction> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("function_decl")){
            nodes.add((SrcMLFunction)node);
        }
        return nodes;
    }
    private List<SrcMLImport> parseImports(){
        List<SrcMLImport> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("import")){
            nodes.add((SrcMLImport)node);
        }
        return nodes;
    }
    private List<SrcMLPackage> parsePackages(){
        List<SrcMLPackage> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("package")){
            nodes.add((SrcMLPackage)node);
        }
        return nodes;
    }
    private List<SrcMLReturn> parseReturns(){
        List<SrcMLReturn> nodes = new ArrayList<>();
        for (SrcMLNode node : getSameNameElements("return")){
            nodes.add((SrcMLReturn) node);
        }
        return nodes;
    }
    private List<SrcMLStaticBlock> parseStaticBlocks(){
        List<SrcMLStaticBlock> statics = new ArrayList<>();
        for (SrcMLNode static1 : getSameNameElements("static")){
            statics.add((SrcMLStaticBlock)static1);
        }
        return statics;
    }
    private List<SrcMLSwitch> parseSwitches(){
        List<SrcMLSwitch> switches = new ArrayList<>();
        for (SrcMLNode switcher : getSameNameElements("switch")){
            switches.add((SrcMLSwitch)switcher);
        }
        return switches;
    }
    private List<SrcMLTry> parseTries(){
        List<SrcMLTry> tries = new ArrayList<>();
        for (SrcMLNode try1 : getSameNameElements("try")){
            tries.add((SrcMLTry)try1);
        }
        return tries;
    }
    private List<SrcMLWhile> parseWhiles(){
        List<SrcMLWhile> whiles = new ArrayList<>();
        for (SrcMLNode while1 : getSameNameElements("while")){
            whiles.add((SrcMLWhile)while1);
        }
        return whiles;
    }

    public void fillCallTree(CallTreeNode<SrcMLNode> root){
        for (SrcMLNode node : this.getChildNodeOrder()){
            switch(node.getElement().getNodeName()){
                case "if":
                case "elseif":
                    root.addChild(((SrcMLIf)node).getCallTree());
                    break;
                case "expr_stmt":
                    ((SrcMLExprStmt)node).fillCallTree(root);
                    break;
                case "for":
                    root.addChild(((SrcMLFor)node).getCallTree());
                    break;
                case "block":
                    fillCallTree(root);
                    break;
                case "try":
                    //etc.



            }
        }
    }
}
