package it.polimi.ingsw.view.modelview;

import it.polimi.ingsw.model.Position;

public class PawnView {
    private int id;
    private Position pawnPosition;
    private String color;
    private Boolean isSelected;

    public PawnView(int id, String color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getPawnPosition() {
        return pawnPosition;
    }

    public void setPawnPosition(Position pawnPosition) {
        this.pawnPosition = pawnPosition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
