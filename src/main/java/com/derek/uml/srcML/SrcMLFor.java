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

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

@Getter
public class SrcMLFor extends SrcMLNode{
    private Element forEle;
    private SrcMLControl control;
    private SrcMLBlock block;

    public SrcMLFor(Element forEle) {
        super(forEle);
        this.forEle = forEle;
        parse();
    }
    protected void parse(){
        parseControl();
        parseBlock();
    }
    private void parseControl(){
        List<Node> controlNodes = XmlUtils.getImmediateChildren(forEle, "control");
        for (Node controlNode : controlNodes){
            control = new SrcMLControl(XmlUtils.elementify(controlNode));
        }
    }
    private void parseBlock(){
        List<Node> blockNodes = XmlUtils.getImmediateChildren(forEle, "block");
        for (Node blockNode : blockNodes){
            block = new SrcMLBlock(XmlUtils.elementify(blockNode));
        }
    }


    @Getter
    public class SrcMLControl extends SrcMLNode{
        //'control ' element is only used by for loops.
        private Element controlEle;
        private SrcMLInit init;
        private SrcMLCondition condition;
        private SrcMLIncr incr;

        public SrcMLControl(Element controlEle)
        {
            super(controlEle);
            this.controlEle = controlEle;
        }

        protected void parse(){
            parseInit();
            parseCondition();
            parseIncr();
        }
        private void parseInit(){
            List<Node> initNodes = XmlUtils.getImmediateChildren(controlEle, "init");
            for (Node initNode : initNodes){
                //should only be 1 anyway
                init = new SrcMLInit(XmlUtils.elementify(initNode));
            }
        }
        private void parseCondition(){
            List<Node> conditionNodes = XmlUtils.getImmediateChildren(controlEle, "condition");
            for (Node conditionNode : conditionNodes){
                //should only be 1 anyway
                condition = new SrcMLCondition(XmlUtils.elementify(conditionNode));
            }
        }
        private void parseIncr(){
            List<Node> incrNodes = XmlUtils.getImmediateChildren(controlEle, "incr");
            for (Node incrNode : incrNodes){
                //should only be 1 anyway
                incr = new SrcMLIncr(XmlUtils.elementify(incrNode));
            }
        }

        @Getter
        public class SrcMLIncr extends SrcMLNode{
            //putting a nested class in here because 'incr' only goes to expression or nothing.. not worth amking a new file for it.
            private Element incrEle;
            private SrcMLExpression expression;

            public SrcMLIncr(Element incrEle) {
                super(incrEle);
                this.incrEle = incrEle;
                parse();
            }
            protected void parse(){
                parseExpression();
            }
            private void parseExpression(){
                List<Node> expressionNodes = XmlUtils.getImmediateChildren(incrEle, "expr");
                for (Node expressionNode : expressionNodes){
                    //can have 0 or 1
                    expression = new SrcMLExpression(XmlUtils.elementify(expressionNode));
                }
            }
        }
    }
}
