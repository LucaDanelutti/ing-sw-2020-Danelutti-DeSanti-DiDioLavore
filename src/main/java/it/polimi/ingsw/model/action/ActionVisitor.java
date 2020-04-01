package it.polimi.ingsw.model.action;

public interface ActionVisitor {
    void executeAction(MoveAction moveAction);
    void executeAction(ConstructAction constructAction);
    //void visit(GeneralAction generalAction);
}
