package AdventureModel;

import java.io.*;
import java.util.*;

/**
 * Class AdventureGame.  Handles all the necessary tasks to run the Adventure game.
 */
public class AdventureGame implements Serializable {
    /**
     * An attribute to store the Introductory text of the game.
     */
    private final String directoryName;
    /**
     * A variable to store the Help text of the game. This text is displayed when the user types "HELP" command.
     */
    private String helpText;
    /**
     * A list of all the rooms in the game.
     */
    private final HashMap<Integer, Room> rooms;
    /**
     * A HashMap to store synonyms of commands.
     */
    private HashMap<String,String> synonyms = new HashMap<>();
    /**
     * List of action verbs (other than motions) that exist in all games. Motion vary depending on the room and game.
     *
     */
    private final String[] actionVerbs = {"QUIT","INVENTORY","TAKE","DROP"};
    /**
     * the player of the game
     */
    public Player player;
    /**
     * A variable to store the Summary text of the game. This text is displayed when the user clicks the "Summary" button.
     */
    private String summaryText;
    /**
     * A counter to store the number of lines of text in the Summary, in order to scale the ScrollPane appropriately.
     */
    private int numSumTextLines = 0;

    /**
     * Adventure Game Constructor
     * __________________________
     * Initializes attributes
     *
     * @param name the name of the adventure
     */
    public AdventureGame(String name){
        this.synonyms = new HashMap<>();
        this.rooms = new HashMap<>();
        this.directoryName = "Games/" + name; //all games files are in the Games directory!
        try {
            setUpGame();
        } catch (IOException e) {
            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    /**
     * Save the current state of the game to a file
     *
     * @param file pointer to file to write to
     */
    public void saveModel(File file) {
        try {
            FileOutputStream outfile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outfile);
            oos.writeObject(this);
            oos.close();
            outfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setUpGame
     * __________________________
     *
     * @throws IOException in the case of a file I/O error
     */
    public void setUpGame() throws IOException {

        String directoryName = this.directoryName;
        AdventureLoader loader = new AdventureLoader(this, directoryName);
        loader.loadGame();

        // set up the player's current location
        this.player = new Player(this.rooms.get(101));
    }

    /**
     * tokenize
     * __________________________
     *
     * @param input string from the command line
     * @return a string array of tokens that represents the command.
     */
    public String[] tokenize(String input){

        input = input.toUpperCase();
        String[] commandArray = input.split(" ");

        int i = 0;
        while (i < commandArray.length) {
            if(this.synonyms.containsKey(commandArray[i])){
                commandArray[i] = this.synonyms.get(commandArray[i]);
            }
            i++;
        }
        return commandArray;

    }

    /**
     * movePlayer
     * __________________________
     * Moves the player in the given direction, if possible.
     * Return false if the player wins or dies as a result of the move.
     *
     * @param direction the move command
     * @return false, if move results in death or a win (and game is over).  Else, true.
     */
    public boolean movePlayer(String direction) {

        direction = direction.toUpperCase();
        PassageTable motionTable = this.player.getCurrentRoom().getMotionTable(); //where can we move?
        if (!motionTable.optionExists(direction)) return true; //no move

        ArrayList<Passage> possibilities = new ArrayList<>();
        for (Passage entry : motionTable.getDirection()) {
            if (entry.getDirection().equals(direction)) { //this is the right direction
                possibilities.add(entry); // are there possibilities?
            }
        }

        //try the blocked passages first
        Passage chosen = null;
        for (Passage entry : possibilities) {
            System.out.println(entry.getIsBlocked());
            System.out.println(entry.getKeyName());

            if (chosen == null && entry.getIsBlocked()) {
                if (this.player.getInventory().contains(entry.getKeyName())) {
                    chosen = entry; //we can make it through, given our stuff
                    break;
                } if (this.player.getInventory().contains(entry.getKeyName())) {
                    chosen = entry; //we can make it through, given our stuff
                    break;
                }
            } else { chosen = entry; } //the passage is unlocked
        }

        if (chosen == null) return true; //doh, we just can't move.

        int roomNumber = chosen.getDestinationRoom();
        Room room = this.rooms.get(roomNumber);

        // to remember for the summary message
        Room temp = this.player.getCurrentRoom();

        this.player.setCurrentRoom(room);

        // added for summary feature
        addToSummaryText("You have moved from " + temp.getRoomName() + " to " + this.player.getCurrentRoom().getRoomName() + ".");

        for(int i = 0; i<room.objectsInRoom.size();i++){
            this.player.addToInventory(room.objectsInRoom.get(i));
        }
        room.objectsInRoom.clear();

        return !this.player.getCurrentRoom().getMotionTable().getDirection().get(0).getDirection().equals("FORCED");
    }

    /**
     * interpretAction
     * interpret the user's action.
     *
     * @param command String representation of the command.
     * @return the action to be taken from the command
     */
    public String interpretAction(String command){

        String[] inputArray = tokenize(command); //look up synonyms

        PassageTable motionTable = this.player.getCurrentRoom().getMotionTable(); //where can we move?

        if (motionTable.optionExists(inputArray[0])) {
            if (!movePlayer(inputArray[0])) {
                if (this.player.getCurrentRoom().getMotionTable().getDirection().get(0).getDestinationRoom() == 0) {
                    // added for summary feature
                    addToSummaryText("GAME OVER");

                    return "GAME OVER";
                } else {
                    // added for summary feature
                    addToSummaryText("Forced movement.");

                    return "FORCED";
                }
            } //something is up here! We are dead or we won.
            return null;
        } else if (inputArray[0].equals("SAVE")){
            return "SAVE!";
        } else if(Arrays.asList(this.actionVerbs).contains(inputArray[0])) {
            if(inputArray[0].equals("QUIT")) {
                // added for summary feature
                addToSummaryText("You quit the game.");

                return "GAME OVER"; //time to stop!
            } else if(inputArray[0].equals("INVENTORY") && this.player.getInventory().size() == 0) {
                return "INVENTORY!";
            } else if(inputArray[0].equals("INVENTORY") && this.player.getInventory().size() > 0) {
                return "INVENTORY!";
            } else if(inputArray[0].equals("TAKE") && inputArray.length < 2) {
                return "THE TAKE COMMAND REQUIRES AN OBJECT";
            }
//            else if(inputArray[0].equals("DROP") && inputArray.length < 2) {
//                return "THE DROP COMMAND REQUIRES AN OBJECT";
             else if(inputArray[0].equals("TAKE") && inputArray.length == 2) {
                if(this.player.getCurrentRoom().checkIfObjectInRoom(inputArray[1])) {
                    this.player.takeObject(inputArray[1]);

                    // added for summary feature
                    addToSummaryText("You picked up: " + inputArray[1] + " from " + this.player.getCurrentRoom().getRoomName() + ".");

                    return "YOU HAVE TAKEN:\n " + inputArray[1];
                } else {
                    return "THIS OBJECT IS NOT HERE:\n " + inputArray[1];
                }
            }
//            else if(inputArray[0].equals("DROP") && inputArray.length == 2) {
//                if(this.player.checkIfObjectInInventory(inputArray[1])) {
//                    this.player.dropObject(inputArray[1]);
//
//                    // added for summary feature
//                    addToSummaryText("You dropped: "+ inputArray[1] + " in " + this.player.getCurrentRoom().getRoomName() + ".");
//
//                    return "YOU HAVE DROPPED:\n " + inputArray[1];
//                } else {
//                    return "THIS OBJECT IS NOT IN YOUR INVENTORY:\n " + inputArray[1];
//                }
//            }
        }
        return "INVALID COMMAND.";
    }

    /**
     * getDirectoryName
     * __________________________
     * Getter method for directory
     * @return directoryName
     */
    public String getDirectoryName() {
        return this.directoryName;
    }

    /**
     * getInstructions
     * __________________________
     * Getter method for instructions
     * @return helpText
     */
    public String getInstructions() {
        return helpText;
    }

    /**
     * getPlayer
     * __________________________
     * Getter method for Player
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * getRooms
     * __________________________
     * Getter method for rooms
     * @return map of key value pairs (integer to room)
     */
    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    /**
     * getSynonyms
     * __________________________
     * Getter method for synonyms
     * @return map of key value pairs (synonym to command)
     */
    public HashMap<String, String> getSynonyms() {
        return this.synonyms;
    }

    /**
     * setHelpText
     * __________________________
     * Setter method for helpText
     * @param help which is text to set
     */
    public void setHelpText(String help) {
        this.helpText = help;
    }

    /**
     * getSummaryText
     * __________________________
     * Getter method for summaryText
     * @return summaryText a string with everything that's happened so far
     */
    public String getSummaryText() {return this.summaryText;}

    /**
     * addToSummaryText
     * __________________________
     * Method that adds new text to summaryText
     * @param new_event which is text to add
     */
    public void addToSummaryText(String new_event) {
        this.summaryText += new_event;
        this.summaryText += "\n\n\n";

        addtoNumSumLines();
    }

    /**
     * addtoNumSumLines
     * __________________________
     * Method that increments the counter for the number of lines of text in the summary
     */
    private void addtoNumSumLines() {
        this.numSumTextLines += 1;
    }

    /**
     * getNumSumLines
     * __________________________
     * Getter for number of lines of text in the summary
     * @return the number of lines of text in the summary
     */
    public int getNumSumLines() {
        return this.numSumTextLines;
    }
}
