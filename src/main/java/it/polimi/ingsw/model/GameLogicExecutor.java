package it.polimi.ingsw.model;

import java.util.Observable;

public class GameLogicExecutor implements Observer {
    //TODO: maybe the GameLogicExecutor could be the only access for the controller to the model, giving a
    // fully-blind access to the model
    private Game game;

    GameLogicExecutor(Game game){
        this.game=game;
    }

    void execute(Action currentAction){
        switch (currentAction.getActionType()){
            case CONSTRUCT:{
                ConstructAction constructAction=(ConstructAction)currentAction;
                game.getBoard().pawnConstruct(constructAction.getChosenPosition(),constructAction.getSelectedBlockType());
            }
            case MOVE: {
                //TODO: implement
            }
            case GENERAL:{
                //TODO: implement
            }
            default:{
                //TODO: implement
            }
        }
    }

    @Override
    public void update(Object o) {
        execute((Action)o);
    }
}
