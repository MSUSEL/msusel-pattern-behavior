package com.derek.uml.srcML;
import lombok.Getter;
import org.w3c.dom.Element;

@Getter
public class SrcMLStaticBlock extends SrcMLNode{
    private SrcMLBlock block;

    public SrcMLStaticBlock(Element staticEle) {
        super(staticEle);
    }
    protected void parse(){
        block = parseBlock();
    }
}
