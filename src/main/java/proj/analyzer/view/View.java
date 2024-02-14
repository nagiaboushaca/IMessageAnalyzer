package proj.analyzer.view;

import java.util.ArrayList;


/**
 * interface for the view
 */
public interface View {

  /**
   * displays the given message
   *
   * @param message message to display
   */
  public void displayMessage(String message);

  /**
   * displays the end game result
   */
  public void displayStats();
}
