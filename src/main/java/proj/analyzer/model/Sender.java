package proj.analyzer.model;

/**
 * represents sender information
 */
public class Sender {
    private String name;
    private String number;

    public Sender(String number) {
        this.number = number;
    }

    /**
     * sets the name to the given name
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the number of this sender, or Me if it is the user
     * 
     * @return the number of this sender
     */
    public String getNumber() {
        return this.number;
    }

}
