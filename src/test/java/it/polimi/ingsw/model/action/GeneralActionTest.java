package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GeneralActionTest {

    /**
     * This test checks whether the availableCells() function of GeneralAction works properly
     */
    @Test
    void availableCells() {
        ArrayList<Position> notAvailableCellsTester = new ArrayList<>();
        GeneralAction generalActionTester = new GeneralAction(true, notAvailableCellsTester, true, false);

        Pawn selectedPawnTester = new Pawn("white");
        Pawn notSelectedPawnTester = new Pawn("white");
        selectedPawnTester.setPosition(new Position(1,2));
        notSelectedPawnTester.setPosition(new Position(1,1));

        generalActionTester.setSelectedPawn(selectedPawnTester);
        generalActionTester.setNotSelectedPawn(notSelectedPawnTester);


        Board boardTester = new Board();

//        boardTester.pawnConstruct(new Position(1,1), BlockType.DOME);
        boardTester.setPawnPosition(notSelectedPawnTester, new Position(1,1));

        Pawn enemyPawn = new Pawn("grey");
        boardTester.setPawnPosition(enemyPawn, new Position(2,3));


//        boardTester.pawnConstruct(new Position(3,4), BlockType.DOME);



        ArrayList<Position> availableCellsTester = generalActionTester.availableCells(boardTester.getMatrixCopy());
//        availableCellsTester.forEach(value -> System.out.println(value.getX() + " " + value.getY()));

        assertEquals(new Position(2,3), availableCellsTester.get(0), "The position should be the same");
    }
}