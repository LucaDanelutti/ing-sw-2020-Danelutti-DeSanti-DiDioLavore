package it.polimi.ingsw.model;


import java.util.ArrayList;

/**
 * This class represents every player in the game
 */
class Player {
    final String name;
    ArrayList<Pawn> pawnList;
    Card currentCard;
    Boolean isLoser;
    Boolean isWinner;
    //PlayerState state;

    //===================================================
    //NEW THINGS
    Position selectedPawnPosition;
    Position unselectedPawnPosition;

    public Pawn getSelectedPawn(){
        for(Pawn p : pawnList){
            if(p.getPosition().equals(selectedPawnPosition)){
                return p;
            }
        }
        return null;
    }
    public Pawn getUnselectedPawn(){
        for(Pawn p : pawnList){
            if(p.getPosition().equals(unselectedPawnPosition)){
                return p;
            }
        }
        return null;
    }
    public void setUnselectedPawnPosition(Pawn p){
        if(p==null) {
            unselectedPawnPosition = null;
        }
        else {
            unselectedPawnPosition = new Position(p.getPosition().getX(), p.getPosition().getY());
        }
    }
    public void setSelectedPawnPosition(Pawn p){
        if(p==null){
            selectedPawnPosition=null;
        }
        else {
            selectedPawnPosition = new Position(p.getPosition().getX(), p.getPosition().getY());
        }
    }


    //======================================================

    /**
     * Constructor of this class
     * @param name defines the name of the player
     */
    Player(String name) {
        this.name = name;
        this.isLoser=false;
        this.isWinner=false;
        //this.state = state;
        pawnList = new ArrayList<Pawn>();
    }

    Player(Player toBeCopied){
        this.name=toBeCopied.name;
        this.pawnList=new ArrayList<>();
        for(Pawn p: toBeCopied.pawnList){
            this.pawnList.add(new Pawn(p));
        }
        this.isWinner=toBeCopied.isWinner;
        this.isLoser=toBeCopied.isLoser;
        this.currentCard=new Card(toBeCopied.currentCard);
    }

    String getName() {
        return name;
    }

    ArrayList<Pawn> getPawnList() {
        return pawnList;
    }

    /**
     * addPawn method to add the provided Pawn to the pawnList
     * @param pawn is the pawn to add
     */
    void addPawn(Pawn pawn) {
        this.pawnList.add(pawn);
    }

    /**
     * removePawn method to remove the provided Pawn from the pawnList
     * @param pawn is the pawn to remove
     */
    void removePawn(Pawn pawn) {
        if(pawn==null){
            return;
        }
        this.pawnList.remove(pawn);
    }
    void removeAllPawns(){
        this.pawnList=new ArrayList<>();
    }

    Card getCurrentCard() {
        return currentCard;
    }

    void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public Boolean getLoser() {
        return isLoser;
    }

    public void setLoser(Boolean loser) {
        isLoser = loser;
    }

    public Boolean getWinner() {
        return isWinner;
    }

    public void setWinner(Boolean winner) {
        isWinner = winner;
    }

    /*PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
    }*/
}
