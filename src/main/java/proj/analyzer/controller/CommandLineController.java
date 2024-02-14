package proj.analyzer.controller;

import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import proj.analyzer.model.Conversation;
import proj.analyzer.model.Model;
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
        runKeywordAnalytics();
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
     * runs and prompts user for analytics on keywords
     */
    private void runKeywordAnalytics() {
        boolean continueKeywordAnalytics = true;
        while (continueKeywordAnalytics) {
            view.displayMessage("Type in keyword(s) to get analytics");
            String keyword = this.inputScanner.nextLine();
            for (Sender s : this.model.getSenders()) {
                view.displayMessage(
                    s.getName() 
                    + ": " 
                    + this.model.countOccurences(s, keyword)
                    + " occurences of "
                    + keyword);
            }
            view.displayMessage("type q to cancel or any other key to keep running analytics");
            String response = this.inputScanner.nextLine();
            if (response == "q") {
                continueKeywordAnalytics = false;
            }
            this.inputScanner.nextLine();
        }
    }
}
