package it.polimi.ingsw.model;

/**
 * This enum is used to represent the playerState of a player
 * SelectGameCardsState, ChooseCardState, ChoosePawnsPositionState are setup states
 * IdleState, ActionState are match states
 * LoserState, WinnerState are final states
 */
enum PlayerStateType {
        SelectGameCardsState(),
        ChooseCardState(),
        ChoosePawnsPositionState(),
        LoserState(),
        WinnerState(),
        IdleState(),
        ActionState();

        PlayerStateType(){
        }
}
