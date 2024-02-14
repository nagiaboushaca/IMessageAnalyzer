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
    private Readable input;
    private Model model;
    private View view;

    /**
     * constructor for the command line controller 
     * 
     * @param view the view to be used
     */
    public CommandLineController(View view) {
        this.view = view;
        this.input = new InputStreamReader(System.in);
        this.inputScanner = new Scanner(input);
    }

    /**
     * Runs the application
     */
    @Override
    public void run() {
        this.promptFile();
        this.model.scanText();
        this.promptNames();
        for (Sender s : this.model.getSenders()) {
            System.out.println(s.getName() + ": " + s.getNumber());
        }
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

        view.displayMessage("What is your name?");
        String name = this.inputScanner.next();
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
            String name = this.inputScanner.next();
            s.setName(name);
        }
    }
    
}
