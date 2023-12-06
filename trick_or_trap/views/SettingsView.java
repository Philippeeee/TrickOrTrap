package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.AccessibleRole;

/**
 * Class SettingsView.
 *
 * Provides a summary of the game and how to play.
 */
public class SettingsView {
    Label settingsLabel = new Label("Settings:");
    Button returnToTitleScreenButton = new Button("Return to Title Screen");
    Button exitGameButton = new Button("Exit Game");
    private final Stage stage = new Stage();
    AdventureGameView adventureGameView;


    /**
     * Constructor
     * @param adventureGameView adventure game view
     */
    public SettingsView(AdventureGameView adventureGameView){
        this.adventureGameView = adventureGameView;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(adventureGameView.stage);
        this.stage.setTitle("Settings");

        // Labels
        settingsLabel.setStyle("-fx-text-fill: white;");
        settingsLabel.setFont(new Font("Arial", 60));


        // Buttons
        this.returnToTitleScreenButton.setId("ReturnToTitleScreen");
        this.returnToTitleScreenButton.setPrefSize(200, 100);
        this.returnToTitleScreenButton.setFont(new Font("Arial", 16));
        this.returnToTitleScreenButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        makeButtonAccessible(this.returnToTitleScreenButton, "Return to Title Screen Button", "This button returns to the title screen.", "This button returns to the title screen. Click it to close this window.");
        addReturnEvent();

        this.exitGameButton.setId("ExitGame");
        this.exitGameButton.setPrefSize(200, 100);
        this.exitGameButton.setFont(new Font("Arial", 16));
        this.exitGameButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        makeButtonAccessible(this.exitGameButton, "Exit game Button", "This button exits the game.", "This button exits the game. Click it to close the game.");
        addExitGameEvent();


        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(settingsLabel);
        vBox1.setStyle("-fx-background-color: #121212;");
        vBox1.setSpacing(20);


        VBox vBoxMain = new VBox();
        vBoxMain.setStyle("-fx-background-color: #121212;");
        vBoxMain.setPadding(new Insets(20, 20,20,20));
        vBoxMain.setSpacing(40);
        adventureGameView.textSpeedButton.setPrefSize(200, 100);
        vBoxMain.getChildren().addAll(vBox1, returnToTitleScreenButton, adventureGameView.textSpeedButton, exitGameButton);

        Scene scene = new Scene(vBoxMain, 1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * This method closes the title screen stage.
     * Handles the event related to the exit game button.
     */
    private void addExitGameEvent() {
        this.exitGameButton.setOnAction(e -> {
            this.stage.requestFocus();
            adventureGameView.stage.close();
        });    }

    /**
     * This method closes this stage and returns to the title screen.
     * Handles the event related to the return to title screen button.
     */
    private void addReturnEvent() {
        this.returnToTitleScreenButton.setOnAction(e -> {
            this.stage.requestFocus();
            this.stage.close();
        });    }


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
}