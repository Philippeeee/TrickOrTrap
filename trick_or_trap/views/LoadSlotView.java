package views;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.AccessibleRole;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class LoadSlotView.
 *
 * Creates a save slot for the player to choose from.
 */
public class LoadSlotView {

    Button saveNameButton, selectSaveButton, deleteSaveButton, confirmButton;
    Label latestSaveLabel, currentRoomLabel;
    Image currentRoomImage;
    ImageView roomImageView;
    AdventureGame model;
    Stage stage;
    private final VBox vbox = new VBox();
    int width = 300; // width of the save slot
    String fileName;
    TitleLoadView titleLoadView;
    int roomNumber;
    ArrayList<String> time;

    /**
     * Constructor
     * @param adventureGame adventure game
     * @param titleLoadView loadview from the title
     * @param fileName name of the file
     */
    public LoadSlotView(AdventureGame adventureGame, TitleLoadView titleLoadView, String fileName){
        this.model = adventureGame;
        this.stage = titleLoadView.stage;
        this.fileName = fileName;
        this.titleLoadView = titleLoadView;

        if (!fileName.equals("NewGameSave.ser")) {


            ArrayList<String> saveFile = new ArrayList<>(Arrays.asList(fileName.split("--")));
            this.roomNumber = Integer.parseInt(saveFile.get(0));

            this.time = new ArrayList<>(Arrays.asList(saveFile.get(1).split("\\.")));
            String year = time.get(0);
            String month = time.get(1);
            String day = time.get(2);
            String saveTime = year + "-" + month + "-" + day;

            String saveName = saveFile.get(2);

            // Labels
            latestSaveLabel = new Label("Latest Save: " + saveTime);
            latestSaveLabel.setId("SaveLabel");
            latestSaveLabel.setStyle("-fx-text-fill: white;");
            latestSaveLabel.setFont(new Font("Arial", 16));
            latestSaveLabel.setWrapText(true);

            currentRoomLabel = new Label("Current Room: " + adventureGame.getRooms().get(roomNumber).getRoomName());
            currentRoomLabel.setId("RoomLabel");
            currentRoomLabel.setStyle("-fx-text-fill: white;");
            currentRoomLabel.setFont(new Font("Arial", 16));
            currentRoomLabel.setWrapText(true);


            // Buttons
            saveNameButton = new Button(saveName.substring(0, saveName.length() - 4));
            saveNameButton.setId("SaveName");
            customizeButton(saveNameButton, width, 50);
            makeButtonAccessible(saveNameButton, "Save Name Button", "This button renames the save slot.", "This button renames the save slot. Click it to pop up a text input. Press Enter to rename the slot.");
            addSaveNameEvent();

            selectSaveButton = new Button("Select");
            selectSaveButton.setId("Select");
            customizeButton(selectSaveButton, width / 2, 50);
            makeButtonAccessible(selectSaveButton, "Select Save Button", "This button chooses the save slot.", "This button chooses the save slot. Click it to play on that save slot.");
            addSelectSaveEvent();

            deleteSaveButton = new Button("Delete");
            deleteSaveButton.setId("Delete");
            customizeButton(deleteSaveButton, width / 2, 50);
            makeButtonAccessible(deleteSaveButton, "Delete Button", "This button deletes the save slot.", "This button deletes the save slot. Click it delete that save slot.");
            addDeleteSaveEvent();

            // Image
            String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".jpg";
            currentRoomImage = new Image(roomImage);
            roomImageView = new ImageView(currentRoomImage);
            roomImageView.setPreserveRatio(true);

            roomImageView.setFitWidth(width);


            // Containers
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: #757575;");
            hbox.getChildren().addAll(selectSaveButton, deleteSaveButton);
            hbox.setSpacing(10);


            vbox.setMaxWidth(width);
            vbox.setSpacing(10);
            vbox.setMaxHeight(450);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(10, 10, 10, 10));
            vbox.setStyle("-fx-background-color: #757575;");
            vbox.getChildren().addAll(saveNameButton, latestSaveLabel, currentRoomLabel, roomImageView, hbox);

        }
    }

    /**
     *  Returns the save slot as a VBox
     * @return vbox of the save slot
     */
    public Node render(){
        return vbox;
    }

    /**
     * This method handles the event related to the
     * delete button.
     */
    private void addDeleteSaveEvent() {
        deleteSaveButton.setOnAction(e -> {
            stage.requestFocus();
            titleLoadView.deleteFile(fileName);
        });

    }

    /**
     * This method handles the event related to the
     * save select button.
     */
    private void addSelectSaveEvent() {
        selectSaveButton.setOnAction(e -> {
            stage.requestFocus();
            AdventureGame game = null;
            try {
                game = loadGame("Games/Saved/" + fileName);
                titleLoadView.stage.close();
                titleLoadView.adventureGameView.changeSceneToGameEvent();
                titleLoadView.adventureGameView.model = game;
                titleLoadView.adventureGameView.updateScene("");
                titleLoadView.adventureGameView.updateItems();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        });
    }

    /**
     * This method handles the event related to the
     * save name button.
     */
    private void addSaveNameEvent() {
        saveNameButton.setOnAction(e -> {
            stage.requestFocus();
            Stage namingStage = new Stage();
            namingStage.initModality(Modality.APPLICATION_MODAL);

            namingStage.setTitle("Name your save slot!");

            Label namingLabel = new Label("Name your save slot:");
            namingLabel.setId("NamingLabel");
            namingLabel.setStyle("-fx-text-fill: white;");
            namingLabel.setFont(new Font("Arial", 20));

            TextField namingTextField = new TextField("Untitled Save");
            namingTextField.setPromptText("Name your save here.");

            confirmButton = new Button("Confirm");
            confirmButton.setId("Confirm");
            customizeButton(confirmButton, width, 50);
            makeButtonAccessible(confirmButton, "Confirm Button", "This button renames the save slot base on the text input.", "This button renames the save slot base on the text input. Click it return.");

            confirmButton.setOnAction(e1 -> {
                namingStage.requestFocus();
                saveNameButton.setText(namingTextField.getText());
                File file = new File("Games/Saved/" + fileName);
                String rename = Integer.toString(roomNumber) + "--" + String.join(".", time) + "--" + namingTextField.getText();
                fileName = rename + ".ser";
                file.renameTo(new File("Games/Saved/" + fileName));
                namingStage.close();
            });

            VBox namingVBbox = new VBox();
            namingVBbox.setSpacing(10);
            namingVBbox.setPadding(new Insets(10,10,10,10));
            namingVBbox.setMaxWidth(width);
            namingVBbox.setStyle("-fx-background-color: #121212;");
            namingVBbox.getChildren().addAll(namingLabel, namingTextField, confirmButton);

            Scene namingScene = new Scene(namingVBbox, 300, 120);
            namingScene.setFill(Color.BLACK);
            namingStage.setScene(namingScene);
            namingStage.setResizable(false);
            namingStage.show();

        });
    }


    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
    }

    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * Load the Game from a file
     *
     * @param GameFile file to load
     * @return loaded selected save slot
     * @throws IOException io
     * @throws ClassNotFoundException class not found
     */
    public AdventureGame loadGame(String GameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(GameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }
}