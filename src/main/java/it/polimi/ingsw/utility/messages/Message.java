package it.polimi.ingsw.utility.messages;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 2641830232979966012L;

    ArrayList<String> recipients = new ArrayList<>();

    public Message(ArrayList<String> recipients) {
        this.recipients.addAll(recipients);
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }
}
