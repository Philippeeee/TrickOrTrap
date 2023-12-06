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
 * Class OverviewView.
 *
 * Provides a summary of the game and how to play.
 */
public class OverviewView {
    Label summaryLabel = new Label("Summary:");
    Label howToPlayLabel = new Label("How To Play:");
    Label summaryContentLabel = new Label();
    ScrollPane howToPlayContentScrollPane = new ScrollPane();
    Label howToPlayContentLabel = new Label();
    Button returnToTitleScreenButton = new Button("Return to Title Screen");
    private final Stage stage = new Stage();
    AdventureGameView adventureGameView;


    /**
     * Constructor
     * @param adventureGameView adventure game view
     */
    public OverviewView(AdventureGameView adventureGameView){
        this.adventureGameView = adventureGameView;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(adventureGameView.stage);
        this.stage.setTitle("Summary");

        // Labels
        howToPlayLabel.setStyle("-fx-text-fill: white;");
        howToPlayLabel.setFont(new Font("Arial", 60));

        howToPlayContentLabel.setText(this.adventureGameView.model.getInstructions());
        howToPlayContentLabel.setWrapText(true);
        howToPlayContentLabel.setStyle("-fx-background-color: #121212; -fx-text-fill: white;");
        howToPlayContentLabel.setFont(new Font(16));
        howToPlayContentLabel.setWrapText(true);
        howToPlayContentLabel.setPrefWidth(1000);

        howToPlayContentScrollPane.setStyle("-fx-text-fill: white;");
        howToPlayContentScrollPane.setContent(howToPlayContentLabel);
        howToPlayContentScrollPane.setFitToWidth(true);
        howToPlayContentScrollPane.setStyle("-fx-background-color: #121212;");
        howToPlayContentScrollPane.setPrefHeight(300);

        summaryLabel.setStyle("-fx-text-fill: white;");
        summaryLabel.setFont(new Font("Arial", 60));

        summaryContentLabel.setStyle("-fx-text-fill: white;");
        summaryContentLabel.setFont(new Font("Arial", 16));
        summaryContentLabel.setText("You went out trick or treating with your friends and stumbled upon what looked like to be a great house, however your friends vanish and you find yourself trapped alone! Escape the house and find your friends! Be quick, you don't have much time...");
        summaryContentLabel.setWrapText(true);


        this.returnToTitleScreenButton.setId("ReturnToTitleScreen");
        this.returnToTitleScreenButton.setPrefSize(200, 100);
        this.returnToTitleScreenButton.setFont(new Font("Arial", 16));
        this.returnToTitleScreenButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
        makeButtonAccessible(this.returnToTitleScreenButton, "Return to Title Screen Button", "This button returns to the title screen.", "This button returns to the title screen. Click it to close this window.");
        addReturnEvent();


        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(summaryLabel, summaryContentLabel);
        vBox1.setStyle("-fx-background-color: #121212;");
        vBox1.setSpacing(20);

        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(howToPlayLabel, howToPlayContentScrollPane);
        vBox2.setStyle("-fx-background-color: #121212;");
        vBox2.setSpacing(20);

        VBox vBoxMain = new VBox();
        vBoxMain.setStyle("-fx-background-color: #121212;");
        vBoxMain.setPadding(new Insets(20, 20,20,20));
        vBoxMain.setSpacing(40);
        vBoxMain.getChildren().addAll(vBox1, vBox2, returnToTitleScreenButton);

        Scene scene = new Scene(vBoxMain, 1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * This method closes this stage to return to the title screen.
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