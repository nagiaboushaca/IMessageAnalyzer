package proj.analyzer.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Represents a singular text message
 */
public class Text {
    private Sender sender;
    private String message;
    private LocalDateTime time;
    private List<Reaction> reactions;

    /**
     * constructor with reactions
     */
    public Text(Sender sender, String message, LocalDateTime time, List<Reaction> reactions) {
        this(sender, message, time);
        this.reactions = reactions;
    }

    /**
     * constructor with required parameters
     * 
     * @param sender sender of message
     * @param message contents of message
     * @param time time message was sent
     */
    public Text(Sender sender, String message, LocalDateTime time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    /**
     * getter for senders
     * 
     * @return the Sender of this text
     */
    public Sender getSender() {
        return this.sender;
    }

    /**
     * getter for message
     * 
     * @return the message of this text
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * getter for date time of text
     * 
     * @return the datetime of this text
     */
    public LocalDateTime getTime() {
        return this.time;
    }

    /**
     * getter for reactions
     * 
     * @return the reactions on this text
     */
    public List<Reaction> getReactions() {
        return this.reactions;
    }


}
