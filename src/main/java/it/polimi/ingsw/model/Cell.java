package it.polimi.ingsw.model;

import java.util.Stack;

class Cell{
    private Stack<Block> blockStack;
    private Pawn pawn;

    Cell(){
        this.blockStack=new Stack<>();
    }
    Cell(Cell toBeCopied){
        this.pawn=toBeCopied.pawn;
        this.blockStack=new Stack<Block>();
        Object[] temp= toBeCopied.blockStack.toArray();
        for (Object i : temp){
            this.blockStack.push((Block)i);
        }
    }
    void pushBlock(Block block){
        blockStack.push(block);
    }
    Block peekBlock(){
        return blockStack.peek();
    }
    Block popBlock(){
        return blockStack.pop();
    }
    int getSize(){
        return this.blockStack.size();
    }
    boolean isEmpty(){
        return blockStack.isEmpty();
    }
    Pawn getPawn(){
        return pawn;
    }
    void setPawn(Pawn pawn){
        this.pawn=pawn;
    }

}
