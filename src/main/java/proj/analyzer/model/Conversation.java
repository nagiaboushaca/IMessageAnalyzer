package proj.analyzer.model;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * represents a conversation between multiple senders
 */
public class Conversation implements Model {
    private List<Sender> senders;
    private List<Text> texts;
    private Path filePath;
    private Scanner scanner;

    /**
     * constructor for conversation
     * 
     * @param senders the senders listed
     */
    public Conversation(List<Sender> senders, Path filePath) {
        this.senders = senders;
        this.filePath = filePath;
        this.texts = new ArrayList<>();
    }

    /**
     * adds a text to the conversation
     */
    public void addText(Text text) {
        this.texts.add(text);
    }

    /**
     * adds a sender to the list of senders in the conversation
     */
    public void addSender(Sender sender) {
        this.senders.add(sender);
    }

    /**
     * gets senders
     */
    public List<Sender> getSenders() {
        return this.senders;
    }

    /**
     * Counts how many occurences a certain keyword appears from a sender
     */
    public int countOccurences(Sender sender, String keyword) {
        String trimmedKeyword = keyword.trim();
        int count = 0;
        for (Text t : this.texts) {
            if (t.getSender().equals(sender) && t.getMessage().toLowerCase().contains(trimmedKeyword.toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    /**
     * counts the number of reactions for the given type
     * 
     * @param sender the sender
     * @param reactionType the reaction type 
     * @return the number of occurences of that reaction from the sender
     */
    public int countReactions(Sender sender, ReactionType reactionType) {
        int count = 0;
        for (Text t : this.texts) {
            for (Reaction r: t.getReactions()) {
                if (r.getSender().equals(sender) && r.getReactionType().equals(reactionType)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * scans the .txt files and populates the texts and senders fields
     */
    public void scanText() {
        try {
            this.scanner = new Scanner(this.filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("file could not be opened");
        }

        while (this.scanner.hasNextLine()) {
            String dateLine = this.scanner.nextLine();
            if (dateLine.equals("") 
            || dateLine.startsWith(" ") 
            || dateLine.startsWith("\t")
            || !this.checkIfDate(dateLine)) {
                continue;
            } 
            LocalDateTime dateTime = this.findDateTime(dateLine);

            String senderLine = this.scanner.nextLine();
            if (senderLine.equals("")) {
                continue;
            }
            Sender sender = this.findSender(senderLine);

            String messageLine = this.scanner.nextLine();

            List<Reaction> reactions = new ArrayList<Reaction>();
            if (this.scanner.nextLine().startsWith("Reactions:")) {
                String reactionLine = this.scanner.nextLine();
                while (!reactionLine.equals("")) {
                    reactions.add(this.findReaction(reactionLine));
                    reactionLine = this.scanner.nextLine();
                }
            }
            this.addText(new Text(sender, messageLine, dateTime, reactions));
        }
    }

    /**
     * finds and returns the local date time from the given line
     * 
     * @param line the line being read
     * @return the local date time of the message
     */
    private LocalDateTime findDateTime(String line) {
        String month = line.substring(0,3);
        int monthNum = 0;
        switch (month) {
            case "Jan":
            monthNum = 1;
                break;
            case "Feb":
            monthNum = 2;
                break;
            case "Mar":
            monthNum = 3;
                break;
            case "Apr":
            monthNum = 4;
                break;
            case "May":
            monthNum = 5;
                break;
            case "Jun":
            monthNum = 6;
                break;
            case "Jul":
            monthNum = 7;
                break;
            case "Aug":
            monthNum = 8;
                break;
            case "Sep":
            monthNum = 9;
                break;
            case "Oct":
            monthNum = 10;
                break;
            case "Nov":
            monthNum = 11;
                break;
            case "Dec":
            monthNum = 12;
                break;
            default:
                return null;
        }
        Month monthObject = Month.of(monthNum);
        int dayOfMonth = Integer.parseInt(line.substring(4, 6));
        int year = Integer.parseInt(line.substring(8, 12));
        String localTimeString = line.substring(13, 21);
        if (localTimeString.startsWith(" ")) {
            localTimeString = "0" + localTimeString.trim();
        }
        LocalTime localTime = LocalTime.parse(localTimeString);

        return LocalDateTime.of(year, monthObject, dayOfMonth, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
    }

    /**
     * finds the sender from the given line
     * 
     * @param line the line being read
     * @return the sender of the message
     */
    private Sender findSender(String line) {
        for (Sender s : this.senders) {
            if (s.getNumber().equals(line)) {
                return s;
            }
        }
        Sender newSender = new Sender(line);
        this.addSender(newSender);
        return newSender;
    } 

    /**
     * finds and returns the reaction given the line it specifies
     * 
     * @param line the line that is being read
     * 
     * @return the appropriate reaction object
     */
    private Reaction findReaction(String line) {
        if (line.contains("Sticker")) {
            if (line.contains("Me") && !line.contains("+")) {
                return new Reaction(ReactionType.STICKER, this.findSender("Me"));
            } else {
                return new Reaction(ReactionType.STICKER, this.findSender(line.substring(line.indexOf("+"), line.indexOf(":"))));
            }
            
        }
        int index = line.indexOf("by") + 3;
        Sender sender = this.findSender(line.substring(index));
        if (line.startsWith("Loved")) { 
            return new Reaction(ReactionType.LOVED, sender);
        } else if (line.startsWith("Liked")) {
            return new Reaction(ReactionType.LIKED, sender);
        } else if (line.startsWith("Emphasized")) {
            return new Reaction(ReactionType.EMPHASIZED, sender);
        } else if (line.startsWith("Questioned")) {
            return new Reaction(ReactionType.QUESTIONED, sender);
        } else if (line.startsWith("Disliked")) {
            return new Reaction(ReactionType.DISLIKED, sender);
        } else if (line.startsWith("Laughed")) {
            return new Reaction(ReactionType.LAUGHED, sender);
        } else {
            System.out.println(line);
            throw new IllegalStateException("Unknown Reaction");
        }
    } 

    /**
     * returns true if the line starts with a date 
     * 
     * @param line line being read
     * @return true if the line starts with a month
     */
    private boolean checkIfDate(String line) {
        return line.startsWith("Jan") 
        || line.startsWith("Feb")
        || line.startsWith("Mar")
        || line.startsWith("Apr")
        || line.startsWith("May")
        || line.startsWith("Jun")
        || line.startsWith("Jul")
        || line.startsWith("Aug")
        || line.startsWith("Sep")
        || line.startsWith("Oct")
        || line.startsWith("Nov")
        || line.startsWith("Dec");
    }


    
}
