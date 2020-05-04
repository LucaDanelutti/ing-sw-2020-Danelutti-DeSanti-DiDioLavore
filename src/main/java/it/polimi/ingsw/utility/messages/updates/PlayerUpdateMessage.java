package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class PlayerUpdateMessage extends Message {
    String name,color;
    int workerId1,workerId2;


    public PlayerUpdateMessage(ArrayList<String> recipients, String name, String color, int workerId1, int workerId2) {
        super(recipients);
        this.name = name;
        this.color = color;
        this.workerId1 = workerId1;
        this.workerId2 = workerId2;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getWorkerId1() {
        return workerId1;
    }

    public int getWorkerId2() {
        return workerId2;
    }
}
