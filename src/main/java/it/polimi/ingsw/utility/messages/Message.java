package it.polimi.ingsw.utility.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    ArrayList<String> recipients = new ArrayList<>();

    public Message(ArrayList<String> recipients) {
        this.recipients.addAll(recipients);
    }
    public ArrayList<String> getRecipients() {
        return recipients;
    }

}
