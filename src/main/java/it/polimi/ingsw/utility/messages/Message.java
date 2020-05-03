package it.polimi.ingsw.utility.messages;

import java.util.ArrayList;

public class Message {
    ArrayList<String> recipients = new ArrayList<>();

    public Message(ArrayList<String> recipients) {
        this.recipients.addAll(recipients);
    }
    public ArrayList<String> getRecipients() {
        return recipients;
    }

}
