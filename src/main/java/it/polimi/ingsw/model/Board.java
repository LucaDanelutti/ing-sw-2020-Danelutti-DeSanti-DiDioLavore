package it.polimi.ingsw.model;


/**
 * This class represent the actual board of the concrete game.
 * It is composed of a matrix 5x5 of cells, each one containing a stack of blocks and/or a Pawn.
 */
class Board{
    private Cell[][] matrix;

    /**
     * This is the constructor for this class;
     */
    Board(){
        this.matrix=new Cell[5][5];
        for(int i=0; i<this.matrix.length; i++){
            for(int j=0; j<this.matrix[0].length; j++){
                this.matrix[i][j]=new Cell();
            }
        }
    }

    /**
     * This is the getter for the matrix
     * @return the variable matrix
     */
    Cell[][] getMatrixCopy(){
        Cell[][] clonedMatrix=new Cell[5][5];
        for (int i=0; i<clonedMatrix.length; i++){
            for(int j=0; j<clonedMatrix[0].length; j++){
                clonedMatrix[i][j]=new Cell(this.matrix[i][j]);
            }
        }
        return clonedMatrix;
    }

    /**
     * This function updates the position of the pawn inside of the board, and also updates the inside variables of the
     * Pawn class to match accordingly.
     * @param prevPosition previous position
     * @param newPosition new position
     */
    void updatePawnPosition(Position prevPosition, Position newPosition){
        Cell prevCell=matrix[prevPosition.getX()][prevPosition.getY()];
        Cell newCell=matrix[newPosition.getX()][newPosition.getY()];
        newCell.setPawn(prevCell.getPawn());
        prevCell.setPawn(null);
        newCell.getPawn().setPosition(newPosition);
        newCell.getPawn().setDeltaHeight(newCell.getSize()-prevCell.getSize());
    }

    /**
     * This function is used during the ChoosePawnPosition State of the game to set the initial position of a pawn in the Board
     * @param pawn the pawn to be set
     * @param position the position to set the pawn
     */
    void setPawnPosition(Pawn pawn, Position position){
        pawn.setPosition(position);
        pawn.setDeltaHeight(0);
        matrix[position.getX()][position.getY()].setPawn(pawn);
    }

    /**
     * This function is used when an construct action is run on the board, it pushes a new legal block on top
     * @param constructPosition the position where the block will be constructed
     * @param type The type of the Block to be added
     */
    void pawnConstruct(Position constructPosition, BlockType type){
        matrix[constructPosition.getX()][constructPosition.getY()].pushBlock(new Block(type));
    }
}

//    /**
//     * This is the copy constructor for the class Board
//     * @param toBeCopied The origin Board
//     */
//    Board(Board toBeCopied){
//        this.matrix=new Cell[5][5];
//        for (int i=0; i<5; i++){
//            for(int j=0; j<5; j++){
//                this.matrix[i][j]=new Cell(toBeCopied.matrix[i][j]);
//            }
//        }
//    }

//
//
//    /**
//     * This function returns a deep copy of the board, excluding the pawns which are shallow copy
//     * @return cloned board
//     */
//    Board getBoardCopy(){
//        return new Board(this);
//    }