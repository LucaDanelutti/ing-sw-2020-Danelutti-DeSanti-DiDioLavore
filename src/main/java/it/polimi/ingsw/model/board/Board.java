package it.polimi.ingsw.model.board;


import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;

/**
 * This class represent the actual board of the concrete game.
 * It is composed of a matrix 5x5 of cells, each one containing a stack of blocks and/or a Pawn.
 */
public class Board{
    private Cell[][] matrix;

    /**
     * This is the constructor for this class;
     */
    public Board(){
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
    public Cell[][] getMatrixCopy(){
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
    public void updatePawnPosition(Position prevPosition, Position newPosition){
        Cell prevCell=matrix[prevPosition.getX()][prevPosition.getY()];
        Cell newCell=matrix[newPosition.getX()][newPosition.getY()];
        newCell.setPawn(prevCell.getPawn());
        prevCell.setPawn(null);
        newCell.getPawn().setPosition(newPosition);
        newCell.getPawn().setDeltaHeight(newCell.getSize()-prevCell.getSize());
    }

    /**
     * this function updates the position of the pawn on the board, and it is used when in the newPosition an opponent pawn is present
     * the opponent pawn will be placed in the opponentPawnNewPos
     * @param prevPosition current position of the pawn
     * @param newPosition new position of the pawn (and the position of the opponentPawn)
     * @param opponentPawnNewPos the position to place the opponentPawn
     */
    public void updatePawnPosition(Position prevPosition, Position newPosition, Position opponentPawnNewPos){
        Cell prevCell=matrix[prevPosition.getX()][prevPosition.getY()];
        Cell newCell=matrix[newPosition.getX()][newPosition.getY()];
        Cell opponentPawnCell=matrix[opponentPawnNewPos.getX()][opponentPawnNewPos.getY()];

        //prepare the opponent Pawn to be moved
        Pawn opponentPawn=newCell.getPawn();
        opponentPawn.setPosition(opponentPawnNewPos);
        opponentPawn.setDeltaHeight(0);

        //move the pawn in prevPosition to newPosition
        newCell.setPawn(prevCell.getPawn());
        newCell.getPawn().setPosition(newPosition);
        newCell.getPawn().setDeltaHeight(newCell.getSize()-prevCell.getSize());

        //set the prevCell to have no Pawn
        prevCell.setPawn(null);

        //opponentPawn still holds the reference to the pawn in newCell
        //by postponing this set, if prevPos and opponentPawnNewPos are the same, no problem occurs.
        opponentPawnCell.setPawn(opponentPawn);
    }

    /**
     * This function is used during the ChoosePawnPosition State of the game to set the initial position of a pawn in the Board
     * @param pawn the pawn to be set
     * @param position the position to set the pawn
     */
    public void setPawnPosition(Pawn pawn, Position position){
        pawn.setPosition(position);
        pawn.setDeltaHeight(0);
        matrix[position.getX()][position.getY()].setPawn(pawn);
    }

    /**
     * This function is used to remove a pawn from the game by removing it from the board
     * @param pawn The pawn to be removed
     */
    public void removePawnFromGame(Pawn pawn){
        matrix[pawn.getPosition().getX()][pawn.getPosition().getY()].setPawn(null);
    }

    /**
     * This function is used when an construct action is run on the board, it pushes a new legal block on top
     * @param constructPosition the position where the block will be constructed
     * @param type The type of the Block to be added
     */
    public void pawnConstruct(Position selectedPawnPos,Position constructPosition, BlockType type){
        matrix[constructPosition.getX()][constructPosition.getY()].pushBlock(type);
        if(selectedPawnPos!=null)
            matrix[selectedPawnPos.getX()][selectedPawnPos.getY()].getPawn().setLastBuildPosition(constructPosition);
    }

    /**
     * This function returns a copy of the pawn in the provided position
     * @param pawnPosition the position where to retrieve the pawn
     * @return the copy of the pawn
     */
    public Pawn getPawnCopy(Position pawnPosition){
        return new Pawn(matrix[pawnPosition.getX()][pawnPosition.getY()].getPawn());
    }

    public ArrayList<Position> availablePositionsForPawnInitialPlacement(){
        ArrayList<Position> availablePositions = new ArrayList<>();
        for(int row=0; row<matrix.length;row++){
            for(int column=0; column<matrix[0].length;column++){
                if(matrix[row][column].getPawn()==null){
                    availablePositions.add(new Position(row,column));
                }
            }
        }
        return availablePositions;
    }
}

