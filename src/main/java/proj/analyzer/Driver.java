package proj.analyzer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import proj.analyzer.controller.CommandLineController;
import proj.analyzer.controller.Controller;
import proj.analyzer.model.Conversation;
import proj.analyzer.model.Model;
import proj.analyzer.view.CommandLineView;
import proj.analyzer.view.View;

/**
 * This is the main driver of this project.
 */
public class Driver {


  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    View view = new CommandLineView();
    Controller controller = new CommandLineController(view);
    controller.run();
  }
}