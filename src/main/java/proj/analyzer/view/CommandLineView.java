package proj.analyzer.view;

/**
 * Command Line View of this application
 */
public class CommandLineView implements View {

    public CommandLineView() {}

    /**
     * displays given message
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * displays stats as a chart 
     */
    public void displayStats() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayStats'");
    }
    
}
