package proj.analyzer.model;

import java.util.List;

public interface Model {
    
    /**
     * adds a text to the conversation
     * 
     * @param text the text to add
     */
    public void addText(Text text);

    /**
     * adds a sender to the conversation
     * 
     * @param sender the sender to add
     */
    public void addSender(Sender sender);

    /**
     * gets senders 
     * 
     * @return senders 
     */
    public List<Sender> getSenders();

    /**
     * scans the .txt file and populates the texts and senders fields
     */
    public void scanText();

    /**
     * Counts how many occurences a certain sender has sent a keyword
     * 
     * @param sender the sender 
     * @param keyword to search for
     * @return the number of occurences of the keyword from sender
     */
    public int countOccurences(Sender sender, String keyword);

    /**
     * counts the number of reactions for the given type
     * 
     * @param sender the sender
     * @param reactionType the reaction type 
     * @return the number of occurences of that reaction from the sender
     */
    public int countReactions(Sender sender, ReactionType reactionType);
}
