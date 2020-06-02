package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.view.modelview.CellView;

import java.util.Objects;
import java.util.Stack;

/**
 * The board is composed of Cells, each Cells contains a Stack of Blocks, like the real game, each one with its characteristics.
 * If a pawn is present on a Cell its reference is stored on the variable pawn.
 */
public class Cell{
    private final Stack<BlockType> blockStack;
    private Pawn pawn;

                                                //CONSTRUCTORS

    /**
     * This is the default constructor for the class Cell.
     * pawn is explicitly set to null (avoidable) and blockStack initialized.
     */
    Cell(){
        this.blockStack=new Stack<>();
        this.blockStack.push(BlockType.TERRAIN);
        pawn = null;
    }
    /**
     * This function is the copy constructor for the class Cell.
     * By using this method, there is no need to implement Clonable.
     * Example: Cell n = new Cell(toBeCopied);
     * @param toBeCopied this is the original Cell to be copied.
     */
    Cell(Cell toBeCopied){
        if(toBeCopied.pawn!=null)
            this.pawn=new Pawn(toBeCopied.pawn);
        this.blockStack= new Stack<>();
        Object[] temp= toBeCopied.blockStack.toArray();
        for (Object i : temp){
            this.blockStack.push((BlockType)i);
        }
    }
    /**
     * This function is used to return a cell copy but without pawns in it
     * @return the copied cell without pawns
     */
    public Cell getCellCopyWithoutPawn(){
        Cell copy=new Cell();
        Object[] temp= this.blockStack.toArray();
        for (int i=1; i<temp.length; i++){
            copy.blockStack.push((BlockType)temp[i]);
        }
        return copy;
    }




                                            //SPECIFIC FUNCTIONS

    /**
     * This function creates cellView from the current cell
     * @return the cellView
     */
    public CellView cell_to_cellView(){
        CellView copy=new CellView();
        Object[] temp= this.blockStack.toArray();
        for (int i=1; i<temp.length; i++){
            copy.addBlock((BlockType)temp[i]);
        }
        return copy;
    }



                                            //BLOCKSTACK SPECIFIC FUNCTIONS

    /**
     * This provides access to the Stack push function.
     * @param type The block to be pushed into the Stack
     */
    public void pushBlock(BlockType type){
        blockStack.push(type);
    }
    /**
     * This provides access to the Stack peek function.
     * @return It returns the peek of the Stack
     */
    public BlockType peekBlock(){
        return blockStack.peek();
    }
    /**
     * This provides access to the Stack pop function.
     * @return It returns the peek of the Stack and removes it from the stack
     */
    public BlockType popBlock(){
        return blockStack.pop();
    }
    /**
     * This provides access to the Stack size function.
     * @return It returns the size of the stack
     */
    public int getSize(){
        return this.blockStack.size();
    }
    /**
     * This provides access to the Stack isEmpty function.
     * @return It returns a boolean as a response to the "isEmpty" query on the stack
     */
    public boolean isEmpty(){
        return blockStack.isEmpty();
    }

                                            //GETTERS AND SETTERS FOR THE PAWN

    /**
     * This is the getter for the variable pawn
     * @return the variable pawn
     */
    public Pawn getPawn(){
        return pawn;
    }
    /**
     * This is the setter for the variable pawn
     * @param pawn the variable to assign to pawn
     */
    public void setPawn(Pawn pawn){
        this.pawn=pawn;
    }


    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        if(!Objects.equals(pawn, cell.pawn))
            return false;
        Object[] temp1=blockStack.toArray();
        Object[] temp2=cell.blockStack.toArray();
        BlockType appo1,appo2;
        for(int i=0; i<temp1.length; i++){
            appo1=(BlockType)temp1[i];
            appo2=(BlockType)temp2[i];
            if(appo1!=appo2)
                return false;
        }
        return true;
    }
    @Override public int hashCode() {
        return Objects.hash(blockStack, pawn);
    }
}
