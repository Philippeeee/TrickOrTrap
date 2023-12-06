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
import javafx.scene.AccessibleRole;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class TitleLoadView.
 *
 * Provides the save slots that the player can choose from.
 */
public class TitleLoadView {

    Label selectionLabel = new Label("Choose your load slot to play:");
    AdventureGameView adventureGameView;
    AdventureGame model;
    HBox saveSlotsHBox = new HBox();
    Stage stage = new Stage();
    Button returnToTitleScreenButton = new Button("Return to Title Screen");
    ScrollPane saveSlotsScrollPane;
    Label noSaveSlotsLabel = new Label();
    private ArrayList<String> filesList = new ArrayList<String>(); // list of the save files

    /**
     * Constructor
     * @param model model
     * @param adventureGameView adventure game view
     */
    public TitleLoadView(AdventureGame model, AdventureGameView adventureGameView){
        this.model = model;
        this.adventureGameView = adventureGameView;
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initOwner(adventureGameView.stage);
        this.stage.setTitle("Load Slots");

        // Buttons
        this.returnToTitleScreenButton.setId("Game Summary");
        this.returnToTitleScreenButton.setPrefSize(200, 100);
        this.returnToTitleScreenButton.setFont(new Font("Arial", 16));
        this.returnToTitleScreenButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        makeButtonAccessible(this.returnToTitleScreenButton, "Return to Title Screen Button", "This button returns to the title screen.", "This button returns to the title screen. Click it to close this window.");
        addReturnEvent();

        // Labels
        selectionLabel.setStyle("-fx-text-fill: white;");
        selectionLabel.setFont(new Font("Arial", 50));

        // Containers
        saveSlotsScrollPane = new ScrollPane();
        saveSlotsScrollPane.setFitToWidth(true);
        saveSlotsScrollPane.setFitToHeight(true);
        saveSlotsScrollPane.setContent(saveSlotsHBox);
        saveSlotsScrollPane.setStyle("-fx-background-color: #121212;");
        saveSlotsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        getFiles(filesList);

        noSaveSlotsLabel = new Label("Play a new game!");
        noSaveSlotsLabel.setFont(new Font(40));
        noSaveSlotsLabel.setStyle("-fx-text-fill: grey");

        if (filesList.size() == 1) {
            saveSlotsHBox.getChildren().add(noSaveSlotsLabel);
        } else {
            for (String file: filesList) {
                saveSlotsHBox.getChildren().add(new LoadSlotView(model, this, file).render());
            }
        }

        saveSlotsHBox.setStyle("-fx-background-color: #121212;");
        saveSlotsHBox.setSpacing(30);
        saveSlotsHBox.setAlignment(Pos.CENTER);
        saveSlotsHBox.setPadding(new Insets(30,30,30,0));

        VBox vBoxMain = new VBox();
        vBoxMain.setStyle("-fx-background-color: #121212;");
        vBoxMain.setPadding(new Insets(30));
        vBoxMain.setSpacing(40);
        vBoxMain.getChildren().addAll(selectionLabel,saveSlotsScrollPane,returnToTitleScreenButton);

        Scene scene = new Scene(vBoxMain, 1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * This method closes the load view with the save slots and returns to the title screen.
     */
    private void addReturnEvent() {
        this.returnToTitleScreenButton.setOnAction(e -> {
            this.stage.requestFocus();
            this.stage.close();
        });    }

    /**
     * Deletes the given file from
     * @param fileName name of the save file to delete
     */
    public void deleteFile(String fileName) {
        File file = new File("Games/Saved/" + fileName);
        if (file.delete()) {

            getFiles(filesList);
            saveSlotsHBox.getChildren().clear();
            if (filesList.size() == 1) {
                saveSlotsHBox.getChildren().add(noSaveSlotsLabel);
            } else {
                for (String f: filesList) {
                    saveSlotsHBox.getChildren().add(new LoadSlotView(model, this, f).render());
                }
            }
        };
        this.stage.show();

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
     * Get Files to display in a save slot
     * Populate the filesList attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param list the ArrayList containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ArrayList<String> list) {
        list.clear();
        File file = new File("Games/Saved");
        File[] fileLst = file.listFiles();
        if (fileLst != null) {
            for (File f: fileLst) {
                if (f.getName().endsWith(".ser")) {
                    list.add(f.getName());
                }
            }
        }
    }
}