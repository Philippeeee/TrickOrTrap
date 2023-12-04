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
    private Label speedDescrip = new Label(speedLabel + "MEDIUM"); // to help communicate user input
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

        // create and configure the Slow speed button
        slowSpeedButton = new Button(("Slow"));
        slowSpeedButton.setId("slowSpeedButton");
        slowSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        slowSpeedButton.setPrefSize(200, 50);
        slowSpeedButton.setFont(new Font(16));
        slowSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = slow;
            this.speedDescrip.setText(this.speedLabel + "SLOW");
        });

        // create and configure the Medium speed button
        medSpeedButton = new Button(("Medium"));
        medSpeedButton.setId("medSpeedButton");
        medSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        medSpeedButton.setPrefSize(200, 50);
        medSpeedButton.setFont(new Font(16));
        medSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = medium;
            this.speedDescrip.setText(this.speedLabel + "MEDIUM");
        });

        // create and configure the Fast speed button
        hiSpeedButton = new Button(("Fast"));
        hiSpeedButton.setId("hiSpeedButton");
        hiSpeedButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        hiSpeedButton.setPrefSize(200, 50);
        hiSpeedButton.setFont(new Font(16));
        hiSpeedButton.setOnAction(e -> {
            // when the user has selected one of the buttons, update the UI and the internal value
            this.adventureGameView.pause_duration = fast;
            this.speedDescrip.setText(this.speedLabel + "FAST");
        });

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
