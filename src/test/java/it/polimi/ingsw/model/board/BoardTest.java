package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for Board
 */
class BoardTest {

    /**
     * This is the test for the getMatrixCopy function, we test that an empty matrix is equal to a newly created board.matrix
     */
    @Test
    void getMatrixCopy() {
        Cell[][] defaultMatrix=new Cell[5][5];
        for(int i=0; i<defaultMatrix.length; i++){
            for(int j=0; j<defaultMatrix[0].length; j++){
                defaultMatrix[i][j]=new Cell();
            }
        }

        Board b =new Board();
        Cell[][] matrixCopy=b.getMatrixCopy();

        for(int i=0; i<defaultMatrix.length; i++){
            for(int j=0; j<defaultMatrix[0].length; j++){
                assertEquals(defaultMatrix[i][j],matrixCopy[i][j],"each cell should contain the same value");
                assertNotSame(defaultMatrix[i][j],matrixCopy[i][j],"each cell should not be the same reference");
            }
        }

    }

    /**
     * This is the test for the updatePawnPosition function, inside of it we test that when a pawn position is changed
     * the variables between the previous position and the new position are switched.
     */
    @Test
    void updatePawnPosition() {
        Board tester = new Board();
        tester.pawnConstruct(null, new Position(0,1), BlockType.LEVEL1);
        tester.setPawnPosition(new Pawn("ffffff",0),new Position(0,0));
        Cell[][] original=tester.getMatrixCopy();
        tester.updatePawnPosition(new Position(0,0), new Position(0,1));
        Cell[][] mod=tester.getMatrixCopy();
        assertNotSame(original[0][0].getPawn(),mod[0][1].getPawn(),"the same reference to the pawn should not be present");
        assertEquals(mod[0][1].getPawn().getDeltaHeight(),1,"the deltaHeight should be 1");
    }


    /**
     * This is the test for the function setPawnPosition, we check that the pawn passed is the same (exact reference) to
     * the one putted in the matrix, and that the position is set correctly in the pawn inner variables
     */
    @Test
    void setPawnPosition() {
        Board tester = new Board();
        Pawn pwn = new Pawn("ffffff",0);
        tester.setPawnPosition(pwn,new Position(0,0));
        Cell[][] matrix=tester.getMatrixCopy();
        assertNotSame(pwn,matrix[0][0].getPawn(),"we have to copy the pawn");
        assertEquals(pwn.getPosition(),new Position(0,0),"the position of the pawn should be set correctly");
    }

    /**
     *This is the test for the function pawnConstruct, we test that given a specif type of block it is created correctly
     */
    @Test
    void pawnConstruct() {
        Board tester = new Board();
        tester.setPawnPosition(new Pawn("ffffff",0),new Position(0,0));
        Cell[][] original=tester.getMatrixCopy();
        tester.pawnConstruct(new Position(0,0), new Position(0,1),BlockType.LEVEL1);
        Cell[][] mod=tester.getMatrixCopy();
        assertEquals(BlockType.LEVEL1,mod[0][1].peekBlock(),"The block constructed should be correct");
    }
}