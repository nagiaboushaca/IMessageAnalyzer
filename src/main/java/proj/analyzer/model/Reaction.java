package proj.analyzer.model;

/**
 * represents a reaction to a text
 */
public class Reaction {
    private ReactionType reactionType;
    private Sender sender;

    /**
     * constructor for reaction
     * 
     * @param reactionType type of IMessage reaction
     * @param sender person who reacted 
     */
    public Reaction(ReactionType reactionType, Sender sender) {
        this.reactionType = reactionType;
        this.sender = sender;
    }

    /**
     * getter for reactiontype
     * @return the reaction type of this reaction
     */
    public ReactionType getReactionType() {
        return this.reactionType;
    }

    /**
     * getter for sender of reaction
     * @return the sender of this reaction
     */
    public Sender getSender() {
        return this.sender;
    }
}
