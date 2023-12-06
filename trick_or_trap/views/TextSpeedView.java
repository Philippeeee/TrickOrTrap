package views;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class TextSpeedView.
 *
 * Allows players to toggle speed that text automatically scrolls.
 */
public class TextSpeedView {

    private AdventureGameView adventureGameView; // the view
    private Button closeWindowButton; // button to close the menu
    private Button slowSpeedButton, medSpeedButton, hiSpeedButton; // pre-set button options for text speed
    private final String speedLabel = "You are now on text speed: "; // to help communicate user input
    private Label speedDescrip = new Label(); // to help communicate user input
    private final int slow = 10; // pre-set text speed value
    private final int medium = 5; // pre-set text speed value
    private final int fast = 2; // pre-set text speed value

    /**
     * Constructor for class TextSpeedView.
     *
     * @param adventureGameView for applying changes to the view
     */
    public TextSpeedView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        speedDescrip.setStyle("-fx-text-fill: white;");
        speedDescrip.setText(speedLabel + this.adventureGameView.speed);

        // create and configure the Slow speed button
        slowSpeedButton = new Button(("Slow"));
        slowSpeedButton.setId("slowSpeedButton");
        slowSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        slowSpeedButton.setPrefSize(200, 50);
        slowSpeedButton.setFont(new Font(16));
        slowSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = slow;
            this.adventureGameView.speed = "SLOW";
            this.speedDescrip.setText(this.speedLabel + this.adventureGameView.speed);
        });
        AdventureGameView.makeButtonAccessible(slowSpeedButton, "Slow Speed", "This is a button to choose a slow wait time in between text slides, and a slow rate at which text rolls out.", "Click this button to make the rate at which entirely new slides of text appear on screen at a slow speed, and for the text to unfurl at a slow pace.");


        // create and configure the Medium speed button
        medSpeedButton = new Button(("Medium"));
        medSpeedButton.setId("medSpeedButton");
        medSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        medSpeedButton.setPrefSize(200, 50);
        medSpeedButton.setFont(new Font(16));
        medSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = medium;
            this.adventureGameView.speed = "MEDIUM";
            this.speedDescrip.setText(this.speedLabel + this.adventureGameView.speed);
        });
        AdventureGameView.makeButtonAccessible(medSpeedButton, "Medium Speed", "This is a button to choose a medium wait time in between text slides, and a medium rate at which text rolls out.", "Click this button to make the rate at which entirely new slides of text to unfurl on screen at a medium speed.");


        // create and configure the Fast speed button
        hiSpeedButton = new Button(("Fast"));
        hiSpeedButton.setId("hiSpeedButton");
        hiSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        hiSpeedButton.setPrefSize(200, 50);
        hiSpeedButton.setFont(new Font(16));
        hiSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = fast;
            this.adventureGameView.speed = "FAST";
            this.speedDescrip.setText(this.speedLabel + this.adventureGameView.speed);
        });
        AdventureGameView.makeButtonAccessible(hiSpeedButton, "Fast Speed", "This is a button to choose a fast wait time in between text slides, and a fast rate at which text rolls out.", "Click this button to make the rate at which entirely new slides of text appear on screen at a fast speed, and for the text to unfurl at a fast pace.");

        // create and configure the Close Window button
        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "Close Window", "This is a button to close the text speed toggle menu.", "Click this button to close the text speed toggle menu.");

        // Create the Node that holds the 3 speed buttons
        VBox buttons = new VBox(20);
        buttons.setPadding(new Insets(40, 40, 40, 40));
        buttons.setStyle("-fx-background-color: #121212;");
        buttons.getChildren().addAll(slowSpeedButton, medSpeedButton, hiSpeedButton);
        buttons.setAlignment(Pos.CENTER);

        // Create the Node that holds the buttons node, the speed change message, and the close window button
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        dialogVbox.getChildren().addAll(buttons, this.speedDescrip, closeWindowButton);
        dialogVbox.setAlignment(Pos.CENTER);

        // Display the changes
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        // To prevent white flashing in between some button presses
        dialogScene.setFill(Color.BLACK);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
