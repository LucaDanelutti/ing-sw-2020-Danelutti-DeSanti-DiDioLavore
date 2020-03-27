package it.polimi.ingsw.model.playerstate;

/**
 * This enum is used to represent the state of a player
 * WaitingOtherPlayers is a pre-match state
 * SelectGameCardsState, ChooseCardState, ChoosePawnsPositionState are setup states
 * IdleState, ActionState are match states
 * LoserState, WinnerState are final states
 */
public enum PlayerStateType {
        WaitingOtherPlayers(),
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
