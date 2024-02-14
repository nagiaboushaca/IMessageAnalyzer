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
        return 0;
    }

    /**
     * scans the .txt files and populates the texts and senders fields
     */
    public void scanText() {
        try {
            this.scanner = new Scanner(this.filePath);
        } catch (IOException e) {
            System.err.println("could not scan document");
            e.printStackTrace();
        }

        while (this.scanner.hasNextLine()) {
            String dateLine = this.scanner.nextLine();
            if (dateLine.equals("") 
            || dateLine.startsWith(" ") 
            || dateLine.startsWith("\t")) {
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
        String month = line.substring(0,2);
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
                break;
        }
        Month monthObject = Month.of(monthNum);
        int dayOfMonth = Integer.parseInt(line.substring(4, 5));
        int year = Integer.parseInt(line.substring(8, 11));
        LocalTime localTime = LocalTime.parse(line.substring(13, 20));

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
        return new Sender(line);
    } 

    /**
     * finds and returns the reaction given the line it specifies
     * 
     * @param line the line that is being read
     * 
     * @return the appropriate reaction object
     */
    private Reaction findReaction(String line) {
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
        } else {
            throw new IllegalStateException("Unknown Reaction");
        }
    } 


    
}
