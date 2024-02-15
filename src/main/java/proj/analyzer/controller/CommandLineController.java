package proj.analyzer.controller;

import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import proj.analyzer.model.Conversation;
import proj.analyzer.model.Model;
import proj.analyzer.model.ReactionType;
import proj.analyzer.model.Sender;
import proj.analyzer.view.View;

/**
 * controller for the command line application
 */
public class CommandLineController implements Controller {
    private Scanner inputScanner;
    private Model model;
    private View view;

    /**
     * constructor for the command line controller 
     * 
     * @param view the view to be used
     */
    public CommandLineController(View view) {
        this.view = view;
        this.inputScanner = new Scanner(System.in);
    }

    /**
     * Runs the application
     */
    @Override
    public void run() {
        promptFile();
        this.model.scanText();
        promptNames();
        for (Sender s : this.model.getSenders()) {
            System.out.println(s.getName() + ": " + s.getNumber());
        }
        analyticsMode();
        this.inputScanner.close();
        view.displayMessage("Thank you for running IMessageAnalyzer");
    }

    /**
     * prompts file and asks name of the user
     */
    private void promptFile() {
        view.displayMessage("Provide path to .txt file obtained from IMessage Exporter");
        String stringPath = this.inputScanner.next();
        Path path = null;
        try {
            path = Path.of(stringPath);
        } catch (InvalidPathException e) {
            System.err.println("Path given is invalid");
        }
        this.inputScanner.nextLine();
        view.displayMessage("What is your name?");
        String name = this.inputScanner.nextLine();
        Sender me = new Sender("Me");
        me.setName(name);
        List<Sender> listSenders = new ArrayList<Sender>();
        listSenders.add(me);
        this.model = new Conversation(listSenders, path);

        view.displayMessage("Hi, " + name + ", we will ask the names of any appearing numbers as we scan the text");
    }

    /**
     * prompts names of all senders in the conversation, and sets the numbers to their names
     */
    private void promptNames() {
        for (Sender s : this.model.getSenders()) {
            if (s.getNumber().equals("Me")) {
                continue;
            }
            view.displayMessage("What is the contact name of this number?: " + s.getNumber());
            String name = this.inputScanner.nextLine();
            s.setName(name);
        }
    }

    /**
     * runs the main loop for the analytics mode
     */
    private void analyticsMode() {
        view.displayMessage("Press the key for which analytics mode you would like to run: ");
        view.displayMessage("a: keywordAnalytics, b: reactionsAnalytics, c: timestamp analytics");
        String answer = this.inputScanner.next();
        switch (answer) {
            case "a":
                runKeywordAnalytics();
                break;
            case "b":
                runReactionsAnalytics();
                break;
            case "c":
                // runTimestampAnalytics();
                break;
            case "q":
                return;
            default:
                view.displayMessage("Invalid mode");
                break;
        }
        this.inputScanner.nextLine();
        analyticsMode();
    }
    
    /**
     * runs and prompts user for analytics on keywords
     */
    private void runKeywordAnalytics() {
        boolean continueAnalytics = true;
        while (continueAnalytics) {
            view.displayMessage("Type in keyword(s) to get analytics. Separate keywords by commas");
            view.displayMessage("E.g., hello, hi will total up occurences of both per sender");
            String keywords = this.inputScanner.nextLine();
            String[] keywordArray = keywords.split("[,]");
            for (Sender s : this.model.getSenders()) {
                int total = 0;
                for (String str: keywordArray) {
                    total += this.model.countOccurences(s, str);
                }
                view.displayMessage(
                    s.getName() 
                    + ": " 
                    + total
                    + " occurences of "
                    + keywords);
            }

            view.displayMessage("type q to cancel or any other key to keep running analytics");
            String response = this.inputScanner.nextLine();
            if (response == "q") {
                continueAnalytics = false;
            }
            this.inputScanner.nextLine();
        }
    }

     /**
     * runs and prompts user for analytics on reactions
     */
    private void runReactionsAnalytics() {
        view.displayMessage("Showing Reactions by Type: ");
        for (Sender s : this.model.getSenders()) {
            for (ReactionType r: ReactionType.values()) {
                view.displayMessage(
                    s.getName()
                    + " has "
                    + this.model.countReactions(s, r)
                    + " occurences of "
                    + r.name()
                    + " reactions");
            }
            view.displayMessage("\n");
        }
    }
}
