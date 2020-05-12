package it.polimi.ingsw.view.modelview;

import it.polimi.ingsw.model.board.BlockType;

import java.util.Stack;

public class CellView {
    private Stack<BlockType> blockStack;

    public CellView(){
        this.blockStack = new Stack<>();
        this.blockStack.push(BlockType.TERRAIN);
    }

    public void addBlock(BlockType blockType){
        blockStack.add(blockType);
    }
    public int getSize(){
        return this.blockStack.size();
    }

    public boolean isEmpty(){
        return blockStack.isEmpty();
    }

    public Stack<BlockType> getBlockStack() {
        return blockStack;
    }

    public void setBlockStack(Stack<BlockType> blockStack) {
        this.blockStack = blockStack;
    }

    public BlockType getPeek(){
        return blockStack.peek();
    }
}
