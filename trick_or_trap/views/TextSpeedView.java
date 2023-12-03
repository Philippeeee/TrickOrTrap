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

    private AdventureGameView adventureGameView;
    private Button closeWindowButton, slowSpeedButton, medSpeedButton, hiSpeedButton;
    private final String speedLabel = "You are now on text speed: ";
    private Label speedDescrip = new Label(speedLabel + "MEDIUM");
    private final int slow = 10;
    private final int medium = 5;
    private final int fast = 2;

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

        slowSpeedButton = new Button(("Slow"));
        slowSpeedButton.setId("slowSpeedButton");
        slowSpeedButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        slowSpeedButton.setPrefSize(200, 50);
        slowSpeedButton.setFont(new Font(16));
        slowSpeedButton.setOnAction(e -> {
            this.adventureGameView.pause_duration = slow;
            this.speedDescrip.setText(this.speedLabel + "SLOW");
        });

        medSpeedButton = new Button(("Medium"));
        medSpeedButton.setId("medSpeedButton");
        medSpeedButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        medSpeedButton.setPrefSize(200, 50);
        medSpeedButton.setFont(new Font(16));
        medSpeedButton.setOnAction(e -> {
            this.adventureGameView.pause_duration = medium;
            this.speedDescrip.setText(this.speedLabel + "MEDIUM");
        });

        hiSpeedButton = new Button(("Fast"));
        hiSpeedButton.setId("hiSpeedButton");
        hiSpeedButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        hiSpeedButton.setPrefSize(200, 50);
        hiSpeedButton.setFont(new Font(16));
        hiSpeedButton.setOnAction(e -> {
            this.adventureGameView.pause_duration = fast;
            this.speedDescrip.setText(this.speedLabel + "FAST");
        });

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "Close Window", "This is a button to close the text speed toggle menu.", "Click this button to close the text speed toggle menu.");

        VBox buttons = new VBox(20);
        buttons.setPadding(new Insets(40, 40, 40, 40));
        buttons.setStyle("-fx-background-color: #121212;");
        buttons.getChildren().addAll(slowSpeedButton, medSpeedButton, hiSpeedButton, closeWindowButton);
        buttons.setAlignment(Pos.CENTER);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        dialogVbox.getChildren().addAll(buttons, this.speedDescrip, closeWindowButton);
        dialogVbox.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialogScene.setFill(Color.BLACK);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
