package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.utility.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class NicknameSetMessage extends Message implements Serializable {
    private static final long serialVersionUID = -8735018507258677142L;

    String name;

    public NicknameSetMessage(ArrayList<String> recipients,String name) {
        super(recipients);
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
