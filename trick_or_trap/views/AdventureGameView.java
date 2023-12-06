package views;


import AdventureModel.AdventureGame;
import AdventureModel.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 *
 */
public class AdventureGameView {


    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton, settingsButton, inventoryButton, settingsBackButton, inventoryBackButton, summaryButton, summaryBackButton, textSpeedButton, replayButton; //buttons
    Boolean helpToggle = false; //is help on display?
    Boolean settingsToggle = false; //is settings on display?
    Boolean inventoryToggle = false; //is inventory on display?
    Boolean summaryToggle = false; // is summary tab on display?
    ImageView pfp; //to hold character headshot if applicable
    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    Label commandLabel = new Label(); //to hold legal commands
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input
    ScrollPane inventory; //to hold player inventory
    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    private javafx.scene.Node imageNode; // store the image and text after displaying instructions
    private javafx.scene.Node Column; // store the icons column after displaying settings
    /**
     * num seconds that the user wants in between new text (e.g. during forced movement)
     */
    public int pause_duration = 5;
    /**
     * medium text speed toggle
     */
    public String speed = "MEDIUM";
    Button newGameButton, loadGameButton, titleScreenSettingsButton, gameSummaryButton; // title screen buttons
    Button titleBackButton; // return to title screen button from game
    Label gameTitleLabel = new Label("Trick Or Trap"); // title label
    Scene titleScene; // the scene for the title screen
    Scene mainGameScene; // the scene where the game is played
    KeyFrame keyFramemed, keyFrameslow, keyFramefast; // the keyframes for each mode of text speed
    private AtomicBoolean check = new AtomicBoolean(true); // boolean for lambda for text animation
    private IntegerProperty num = new SimpleIntegerProperty(0); // integer for text animation
    private final Timeline line = new Timeline(); // the timeline which the text animation's keyframes are on




    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     * @param model the model
     * @param stage the stage
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
        titleScreenUI();
    }

    /**
     * Initializes the title screen.
     */
    public void titleScreenUI() {
        stopArticulation();
        // setting up the stage
        this.stage.setTitle("group_39's Adventure Game");


        // Buttons
        newGameButton = new Button("New Game");
        newGameButton.setId("New Game");
        customizeButton2(newGameButton, 100, 100);
        makeButtonAccessible(newGameButton, "New Game Button", "This button plays a new game.", "This button plays a new game. Click it to create a new save slot and play a new game.");
        addNewGameEvent();

        loadGameButton = new Button("Load Game");
        loadGameButton.setId("Load Game");
        customizeButton2(loadGameButton, 100, 100);
        makeButtonAccessible(loadGameButton, "Load Game Button", "This button plays a previous played game.", "This button plays a previously played game. Click it to choose from your save slots.");
        addLoadGameEvent();

        titleScreenSettingsButton = new Button("Settings");
        titleScreenSettingsButton.setId("Settings");
        customizeButton2(titleScreenSettingsButton, 100, 100);
        makeButtonAccessible(titleScreenSettingsButton, "Settings Button", "This button opens the settings.", "This button opens the settings. Click it to edit your preferences for more suitable gameplay.");
        addTitleSettingsEvent();

        gameSummaryButton = new Button("Game Instructions");
        gameSummaryButton.setId("Game Summary");
        customizeButton2(gameSummaryButton, 100, 100);
        makeButtonAccessible(gameSummaryButton, "Game Summary Button", "This button provides a summary of the game.", "This button provides a summary of the game. Click it to learn how to play.");
        addGameSummaryEvent();

        gameTitleLabel.setStyle("-fx-text-fill: #801906;");
        gameTitleLabel.setFont(new Font("Chiller", 130));

        VBox vboxButtons = new VBox();
        vboxButtons.setAlignment(Pos.CENTER);
        vboxButtons.setPadding(new Insets(100));
        vboxButtons.setSpacing(25);
        newGameButton.setPrefWidth(300);
        loadGameButton.setPrefWidth(300);
        titleScreenSettingsButton.setPrefWidth(300);
        gameSummaryButton.setPrefWidth(300);
        vboxButtons.getChildren().addAll(newGameButton, loadGameButton, titleScreenSettingsButton, gameSummaryButton);

        HBox hboxTitle = new HBox();
        hboxTitle.setAlignment(Pos.CENTER);
        hboxTitle.getChildren().add(gameTitleLabel);

        VBox vboxMain = new VBox();
//       IMAGE FROM https://tenor.com/view/fnkgif-haunted-house-lightening-storm-is-coming-scary-gif-13292679
        BackgroundImage background = new BackgroundImage(
                new Image("https://media.tenor.com/YYaBvE0VgxsAAAAC/fnkgif-haunted-house.gif",1000,800
                        ,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        vboxMain.setBackground(new Background(background));

        vboxMain.getChildren().addAll(hboxTitle,vboxButtons);
        vboxMain.setPadding(new Insets(50, 50, 50, 50));
        titleScene = new Scene(vboxMain, 1000, 800);
        titleScene.setFill(Color.BLACK);
        this.stage.setScene(titleScene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * This method handles the event related to the
     * title summary button.
     */
    private void addGameSummaryEvent() {
        gameSummaryButton.setOnAction(e -> {
            gridPane.requestFocus();
            OverviewView summaryView = new OverviewView(this);
        });    }

    /**
     * This method handles the event related to the
     * title settings button.
     */
    private void addTitleSettingsEvent() {
        titleScreenSettingsButton.setOnAction(e -> {
            gridPane.requestFocus();
            SettingsView settingsView = new SettingsView(this);
        });
    }

    /**
     * This method handles the event related to the
     * title laod game button.
     */
    private void addLoadGameEvent() {
        loadGameButton.setOnAction(e -> {
            gridPane.requestFocus();
            TitleLoadView titleLoadView = new TitleLoadView(model, this);
        });
    }

    /**
     * This method handles the event related to the
     * select game button.
     */
    public void changeSceneToGameEvent() {
        gridPane.requestFocus();
        stage.setScene(mainGameScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method handles the event related to the
     * title new game button.
     */
    private void addNewGameEvent() {
        newGameButton.setOnAction(e -> {
            gridPane.requestFocus();
            stage.setScene(mainGameScene);
            stage.setResizable(false);
            AdventureGame game = null;
            try {
                game = LoadView.loadGame("Games/Saved/" + "NewGameSave.ser");
                model = game;
                updateScene("");
                updateItems();
                stage.show();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        });    }


    /**
     * Initialize the UI
     */
    public void intiUI() {


        // setting up the stage
        this.stage.setTitle("group_39's Adventure Game");


//        Inventory + Room items
        objectsInInventory.setSpacing(20);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInInventory.setStyle("-fx-background-color: #000000;");
//        objectsInRoom.setSpacing(10);
//        objectsInRoom.setAlignment(Pos.TOP_CENTER);


        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));


        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(250);
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints(100);
        column2.setHgrow( Priority.SOMETIMES );


        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints(200);
        RowConstraints row3 = new RowConstraints(150 );
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );


        gridPane.getColumnConstraints().addAll( column2, column3 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );


        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton2(saveButton, 100, 100);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();


        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton2(loadButton, 100, 100);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();


        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton2(helpButton, 100, 100);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();


        replayButton = new Button(" Audio \nReplay");
        replayButton.setId("Audio Replay");
        customizeButton2(replayButton, 100, 100);
        makeButtonAccessible(replayButton, "Audio Replay Button", "This button gives replays room audio", "This button gives replays room description dictation audio.");
        addReplayEvent();


        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        customizeButton2(settingsButton, 100, 100);
        makeButtonAccessible(settingsButton, "Settings Button", "This button gives access to other buttons related to settings", "This button gives access to other buttons related to settings. Click it to get access.");
        addSettingsEvent();


        settingsBackButton = new Button("Back");
        settingsBackButton.setId("Back");
        customizeButton2(settingsBackButton, 100, 100);
        makeButtonAccessible(settingsBackButton, "Settings Back Button", "This button will return to the view before pressing 'Settings'", "This button will return to the view before pressing 'Settings'. Click it to return.");
        addSettingsBackEvent();


        inventoryButton = new Button("Inventory");
        inventoryButton.setId("Inventory");
        customizeButton2(inventoryButton, 100, 100);
        makeButtonAccessible(inventoryButton, "Inventory Button", "This button gives access to player's inventory", "This button gives access to images of the items in the player's inventory. Click it to get access.");
        addInventoryEvent();


        inventoryBackButton = new Button("Back");
        inventoryBackButton.setId("Back");
        inventoryBackButton.setStyle("-fx-background-color: #000000;");
        customizeButton2(inventoryBackButton, 100, 100);
        makeButtonAccessible(inventoryBackButton, "Inventory Back Button", "This button will return to the view before pressing 'Inventory'", "This button will return to the view before pressing 'Inventory'. Click it to return.");
        addInventoryBackEvent();


        summaryButton = new Button("Summary");
        summaryButton.setId("Summary");
        customizeButton2(summaryButton, 100, 100);
        makeButtonAccessible(summaryButton, "Summary Button", "This button opens the summary tab", "This button opens the summary tab, which will show what's happened in the game so far. Click to open the menu.");
        addSummaryEvent();


        summaryBackButton = new Button("Back");
        summaryBackButton.setId("SummaryBack");
        customizeButton2(summaryBackButton, 100, 100);
        makeButtonAccessible(summaryBackButton, "Summary Back Button", "This button closes the summary tab", "This button closes the summary tab and reverts the button UI to the default. Click to close the menu.");
        addSummaryBackEvent();

        titleBackButton = new Button("Return to Title Screen");
        titleBackButton.setId("Return to Title Screen");
        titleBackButton.setWrapText(true);
        customizeButton2(titleBackButton, 100, 100);
        makeButtonAccessible(titleBackButton, "Return to Title Screen Button", "This button will return to the title screen.", "This button will return to the title screen. Click it to return.");
        addTitleReturnEvent();


        textSpeedButton = new Button("Text\nSpeed\nToggle");
        textSpeedButton.setId("TextSpeed");
        customizeButton2(textSpeedButton, 100, 100);
        makeButtonAccessible(textSpeedButton, "Text Speed Toggle Button", "This button allows you to toggle text speed.", "This button opens the menu in which you can toggle the speed that the text moves. Click to open the menu.");
        addTextSpeedEvent();


        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);


        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field


        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms


        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );


        // Render everything
        mainGameScene = new Scene( gridPane ,  1000, 800);
        mainGameScene.setFill(Color.BLACK);
//        this.stage.setScene(scene);
//        this.stage.setResizable(false);
//        this.stage.show();


    }

    /**
     * This method handles the event related to the
     * return to title button.
     */
    private void addTitleReturnEvent() {
        titleBackButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            int width = 300;
            int roomNumber = model.player.getCurrentRoom().getRoomNumber();

            stage.requestFocus();
            Stage namingStage = new Stage();
            namingStage.initModality(Modality.APPLICATION_MODAL);

            namingStage.setTitle("Save?");

            Label namingLabel = new Label("Would you like to save? \n Name your save slot:");
            namingLabel.setId("NamingLabel");
            namingLabel.setStyle("-fx-text-fill: white;");
            namingLabel.setFont(new Font("Arial", 20));

            TextField namingTextField = new TextField("Untitled Save");
            namingTextField.setPromptText("Name your save here.");

            Button yesButton = new Button("Yes");
            yesButton.setId("Yes");
            customizeButton2(yesButton, width, 50);
            makeButtonAccessible(yesButton, "Yes Button", "This button names the save slot base on the text input.", "This button names the save slot base on the text input. Click it return.");

            yesButton.setOnAction(e1 -> {
                namingStage.requestFocus();
                SaveView.SaveGameWithName(model, namingTextField.getText());
                namingStage.close();
                stage.setScene(titleScene);
                stage.setResizable(false);
                stage.show();
            });

            Button noButton = new Button("No");
            noButton.setId("No");
            customizeButton2(noButton, width, 50);
            makeButtonAccessible(noButton, "No Button", "This button denies naming the save slot.", "This button denies naming the save slot. Click it return.");

            noButton.setOnAction(e1 -> {
                namingStage.requestFocus();
                namingStage.close();
                stage.setScene(titleScene);
                stage.setResizable(false);
                stage.show();
            });

            HBox buttonsHBox = new HBox();
            buttonsHBox.getChildren().addAll(yesButton, noButton);
            buttonsHBox.setSpacing(10);

            VBox namingVBbox = new VBox();
            namingVBbox.setSpacing(10);
            namingVBbox.setPadding(new Insets(10, 10, 10, 10));
            namingVBbox.setMaxWidth(width);
            namingVBbox.setStyle("-fx-background-color: #121212;");
            namingVBbox.getChildren().addAll(namingLabel, namingTextField, buttonsHBox);

            Scene namingScene = new Scene(namingVBbox, 300, 120);
            namingScene.setFill(Color.BLACK);
            namingStage.setScene(namingScene);
            namingStage.setResizable(false);
            namingStage.show();

//            SaveView.quickSaveGame(model);
        });
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
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * customizeButton2
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton2(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #cc872d; -fx-text-fill: white;");
    }


    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        if (inventoryToggle) {
            showInventory();
        } EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    submitEvent(inputTextField.getText().strip());
                    inputTextField.setText("");
                } else if (keyEvent.getCode().equals(KeyCode.TAB)) {
                    gridPane.requestFocus();
                }
            }
        };
        this.inputTextField.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler);


    }

    /**
     *
     * This method is the helper function for the submitEvent method. This method handles consecutive forced events.
     */
    private void forcedHelper() {
        if (inventoryToggle) {
            showInventory();
        }
        List<Passage> passages = model.player.getCurrentRoom().getMotionTable().getDirection();

        int temp = this.pause_duration;
        if (model.player.getCurrentRoom().getRoomNumber() == 30) {
            this.pause_duration = 15;
        }

        PauseTransition pause = new PauseTransition(Duration.seconds((double) model.player.getCurrentRoom().getRoomDescription().length() / 10 * this.pause_duration));
        this.pause_duration = temp;


        pause.setOnFinished(event -> {
//            model.movePlayer("FORCED");
            submitEvent("FORCED");
//            updateScene("");
//            updateItems();
        });
        for (Passage p: passages) {
            if (p.getDirection().equals("FORCED") && p.getDestinationRoom() != 0) {
                pause.play();
            }
        }

        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.RIGHT)){
                    submitEvent("FORCED");
                    inputTextField.setText("");
                }
            }
        };
        this.inputTextField.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler);
    }
    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {
        if (inventoryToggle) {
            showInventory();
        }


        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop


        if (text.equalsIgnoreCase("OBSERVE") || text.equalsIgnoreCase("O")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            if (output == null){
                updateScene(output);
                updateItems();
            } else if (output.equals("INVENTORY!")){
                if (settingsToggle) {
                    showSettings();
                }
                showInventory();
            } else if (output.equals("SAVE!")) {
                SaveView.quickSaveGame(model);
            }
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            updateScene("");
            updateItems();
            forcedHelper();
        }
    }




    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        Room room = model.player.getCurrentRoom();
        String text = "Commands: ";
        text += room.getCommands();
        commandLabel.setText(text);
        commandLabel.setStyle("-fx-text-fill: white;");
    }




    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */

    public void updateScene(String textToDisplay) {
        // find room image
        javafx.scene.Node j = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(0)) {
                j = node;
            }
        }
        // remove room image
        gridPane.getChildren().remove(j);

        getRoomImage(); //get the image of the current room
        check.set(false);
        num.set(num.get()+1);

        getPfpImage(); // get the image of the current pfp if applicable
        roomDescLabel.setText("");
        check.set(false);
        num.set(num.get()+1);

        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(555);
        roomDescLabel.setPrefHeight(400);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_LEFT);
        roomPane.setStyle("-fx-background-color: #000000;");
        HBox bottomthang = new HBox(10);
        bottomthang.getChildren().addAll(pfp, roomDescLabel);
        bottomthang.setPadding(new Insets(10));
        bottomthang.setAlignment(Pos.TOP_LEFT);
        bottomthang.setStyle("-fx-background-color: #000000;");
        showCommands();
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );


        gridPane.add(roomPane, 0, 0, 2, 1);
        gridPane.add(bottomthang, 0, 1, 2, 1);
        stage.sizeToScene();
        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }


    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    public void formatText(String textToDisplay) {
        String roomText;
        String otherText;
        //if (textToDisplay == null || textToDisplay.isBlank()) {
//            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
//            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
//            if (objectString != null && !objectString.isEmpty()) roomText = roomDesc + "\n\nObjects in this room:\n" + objectString;
//            else roomText = roomDesc;
            //roomText = "";
            //roomText = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            //Room room = this.model.getPlayer().getCurrentRoom();
            //System.out.println(room.getRoomNumber());
            //System.out.println(roomText);
        //} else
            roomText = this.model.getPlayer().getCurrentRoom().getRoomDescription();

        //roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER_LEFT);

        IntegerProperty i = new SimpleIntegerProperty(0);


        check.set(false);
        IntegerProperty comparenum = num;

        line.getKeyFrames().removeAll(keyFramefast, keyFrameslow, keyFramemed);
        if (speed.equals("SLOW")) {
            line.stop();
            this.keyFrameslow = new KeyFrame(
                    Duration.seconds(.5),
                    event -> {
                        if (i.get() >= roomText.length() && (comparenum.get() != num.get())) {
                            line.stop();
                        } else {
                            try{
                                roomDescLabel.setText((roomText.substring(0, i.get())));
                                i.set(i.get() + 1);
                            } catch (Exception e){
                                roomDescLabel.setText(roomText);
                                i.set(i.get() + 1);
                            }
                        }
                    });
            line.getKeyFrames().add(keyFrameslow);
            System.out.println("slow");
        }
        if (speed.equals("MEDIUM")) {
            line.stop();
            this.keyFramemed = new KeyFrame(
                    Duration.seconds(.07),
                    event -> {
                        if (i.get() >= roomText.length() && (comparenum.get() != num.get())) {
                            line.stop();
                        } else {
                            try{
                                roomDescLabel.setText((roomText.substring(0, i.get())));
                                i.set(i.get() + 1);
                            } catch (Exception e){
                                roomDescLabel.setText(roomText);
                                i.set(i.get() + 1);
                            }
                        }
                    });
            line.getKeyFrames().add(keyFramemed);
            System.out.println("med");
        }
        if (speed.equals("FAST")){
            line.stop();
            this.keyFramefast = new KeyFrame(
                    Duration.seconds(.02),
                    event -> {
                        if (i.get() >= roomText.length() && (comparenum.get() != num.get())) {
                            line.stop();
                        } else {
                            try{
                                roomDescLabel.setText((roomText.substring(0, i.get())));
                                i.set(i.get() + 1);
                            } catch (Exception e){
                                roomDescLabel.setText(roomText);
                                i.set(i.get() + 1);
                            }
                        }
                    });
            line.getKeyFrames().add(keyFramefast);
            System.out.println("fast");
        }

        line.setCycleCount(Animation.INDEFINITE);
        line.play();

        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.SPACE)){
                    //String roomt = roomDescLabel.getText();
                        line.stop();
                        roomDescLabel.setText(roomText);
                        inputTextField.setText("");
                }
            }
        };
        this.inputTextField.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler);
    }


    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {


        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".jpg";


        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(false);
        roomImageView.setFitWidth(800);
        roomImageView.setFitHeight(400);
        /////////////////////////////////////
        ////////////////////////////////////
        ////////////////////////////////////
        pfp = new ImageView(roomImageFile);
        pfp.setPreserveRatio(false);
        pfp.setFitWidth(200);
        pfp.setFitHeight(200);
        /////////////////////////////////////
        ////////////////////////////////////
        /////////////////////////////////////


        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * getButtons
     * Gets the buttons to display as objects
     * @param objs list of objects
     * @param yes true for room objects, false for inventory objects
     * @return
     */
    private ArrayList<Button> getButtons(ArrayList<AdventureObject> objs, boolean yes) {
        ArrayList<Button> buttons = new ArrayList<>();
        String[] objects = model.player.getCurrentRoom().getObjectString().split(", ");
        for (AdventureObject object: objs) {
            String o = object.getName();
            Image objectImageFile = new Image(this.model.getDirectoryName() + "/objectImages/" + o + ".jpg");
            ImageView objectImageView = new ImageView(objectImageFile);
            Button objectButton = new Button();
            objectImageView.setPreserveRatio(true);
            objectImageView.setFitWidth(80);
            objectButton.setAlignment(Pos.CENTER);
            objectButton.setGraphic(objectImageView);
            objectButton.setStyle("-fx-background-color: #000000;");


//            objectImageView.setFitHeight(100);
//            gridPane.add(objectButton, 0, 1);
            assert false;
            if (yes) {
                makeButtonAccessible(objectButton, object.getName(), "This object is in the current room.", "This object is in the current room. Click it to add the object to your inventory.");
                objectButton.setOnAction(e -> {
                    String text = model.interpretAction("take " + object.getName());
                    updateScene(text);
                    updateItems();
                });
            } else {
                makeButtonAccessible(objectButton, object.getName(), "This object is in your inventory.", "This object is in your inventory. Click it to add the object to the current room.");
                objectButton.setOnAction(e -> {
                    String text = model.interpretAction("drop " + object.getName());
                    updateScene(text);
                    updateItems();
                });
            }
            buttons.add(objectButton);




        }
        return buttons;
    }
    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {


        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
//        ArrayList<Button> buttonsRoom = getButtons(model.player.getCurrentRoom().objectsInRoom, true);


        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        ArrayList<Button> buttonsInventory = getButtons(model.player.inventory, false);


        //please use setAccessibleText to add "alt" descriptions to your images!


        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";



        VBox box = new VBox();
        box.setSpacing(0);
        box.setPadding(new Insets(0));
        for (Button button: buttonsInventory) {
            box.getChildren().add(button);
        }
        ScrollPane scO = new ScrollPane(box);
        scO.setFitToWidth(true);
        scO.setStyle("-fx-background: #000000; -fx-background-color:black;");
        inventory = scO;


        /////////////////////////////////////
        ////////////////////////////////////
        ////////////////////////////////////
        ScrollPane scI = new ScrollPane();
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:black;");
        gridPane.add(scI,2,0, 1, 2);
        VBox box2 = new VBox();
        if (box2.getChildren().isEmpty()) {
            box2.setSpacing(10);
            box2.setPadding(new Insets(10));
        }
        box2.getChildren().add(settingsButton);
        box2.getChildren().add(inventoryButton);
        box2.getChildren().add(summaryButton);
        box2.getChildren().add(replayButton);
        if (settingsToggle) {
            settingsToggle = false;
            addSettingsBackEvent();
        }
        if (inventoryToggle) {
            inventoryToggle = false;
            addInventoryBackEvent();
        }
        if (summaryToggle) {
            addSummaryEvent();
        }

        scI.setContent(box2);
        /////////////////////////////////////
        ///////////////////////////////////
        /////////////////////////////////////


    }


    /**
     * Show the game instructions.
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        javafx.scene.Node n = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(0)) {
                n = node;
            }
        }
        if (!helpToggle) {
            imageNode = n;
            gridPane.getChildren().remove(n);
            Label label = new Label(model.getInstructions());
            label.setStyle("-fx-text-fill: white;-fx-background-color: #121212;");
            label.setFont(new Font("Arial", 12));
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(735);
//            label.setPrefHeight(800);
            label.setTextOverrun(OverrunStyle.CLIP);
            label.setWrapText(true);

            ScrollPane sp = new ScrollPane();
            sp.setPrefHeight(400);
            sp.setContent(label);

            gridPane.add(sp, 0, 0);
            helpToggle = true;
        } else {
            gridPane.getChildren().remove(n);
            gridPane.add(imageNode, 0, 0);
            articulateRoomDescription();
            helpToggle = false;
        }
    }


    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method shows the inventory.
     */
    public void showInventory() {
        javafx.scene.Node n = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(2)) {
                n = node;
            }
        }
        if (!inventoryToggle) {
            Column = n;
            gridPane.getChildren().remove(n);
            VBox invent = new VBox(inventoryBackButton, inventory);
            invent.setSpacing(11);
            invent.setPadding(new Insets(11, 11, 11, 11));
            gridPane.add(invent, 2, 0, 1, 2);
            inventoryToggle = true;
        } else {
            gridPane.getChildren().remove(n);
            gridPane.add(Column, 2, 0, 1, 2);
            inventoryToggle = false;
        }
    }


    /**
     * This method handles the event related to the
     * inventory button.
     */
    public void addInventoryEvent() {
        inventoryButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInventory();
        });
    }


    /**
     * This method handles the event related to the
     * inventory button.
     */
    public void addInventoryBackEvent() {
        inventoryBackButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInventory();
        });
    }

    /**
     * This method shows the settings.
     */
    public void showSettings() {
        javafx.scene.Node n = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(2)) {
                n = node;
            }
        }
        if (!settingsToggle) {
            Column = n;
            gridPane.getChildren().remove(n);
            VBox box = new VBox();
            box.setSpacing(10);
            box.setPadding(new Insets(11));
            textSpeedButton.setPrefSize(100, 100);
            box.getChildren().addAll(settingsBackButton, saveButton, helpButton, textSpeedButton, titleBackButton);
            gridPane.add(box, 2, 0, 1, 2);
            settingsToggle = true;
        } else {
            gridPane.getChildren().remove(n);
            gridPane.add(Column, 2, 0);
            settingsToggle = false;
        }
    }


    /**
     * showSummary
     * __________________________
     * This method displays the summary and updates the buttons accordingly.
     */
    public void showSummary() {
        // find three buttons' node
        javafx.scene.Node n = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(2)) {
                n = node;
            }
        }

        // store + remove three buttons' node
        Column = n;
        gridPane.getChildren().remove(n);

        // add "back" button
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(11));
        box.getChildren().add(summaryBackButton);
        gridPane.add(box, 2, 0, 1, 2);

        summaryToggle = true;

        // find room image
        javafx.scene.Node j = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(0)) {
                j = node;
            }
        }

        // store room image
        imageNode = j;

        // remove room image
        gridPane.getChildren().remove(j);


        // replace room image with summary

        // create Label (for ScrollPane)
        if (this.model.getSummaryText().isEmpty()) {
            this.model.addToSummaryText("Beginning of game.");
        }
        Label summary_text = new Label(this.model.getSummaryText());
        summary_text.setStyle("-fx-text-fill: white;-fx-background-color: #000000;");
        summary_text.setFont(new Font("Arial", 12));
        summary_text.setAlignment(Pos.CENTER);
        summary_text.setPrefWidth(721);
        summary_text.setPrefHeight(this.model.getNumSumLines() * 50);
//        summary_text.setTextOverrun(OverrunStyle.CLIP);
        summary_text.setWrapText(true);

        // create ScrollPane
        ScrollPane summary_scroll = new ScrollPane();
        summary_scroll.setPrefSize(735, 421);
        // put summary text in ScrollPane
        summary_scroll.setContent(summary_text);
        // start user at bottom of ScrollPane
        summary_scroll.setVvalue(summary_scroll.getVmax());
        // display ScrollPane (where room image was)
        gridPane.add(summary_scroll, 0, 0);
    }

    /**
     * hideSummary
     * __________________________
     * This method closes the summary and updates the buttons accordingly.
     */
    public void hideSummary() {
        javafx.scene.Node k = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(2)) {
                k = node;
            }
        }

        // change the back button to the original 3 buttons
        gridPane.getChildren().remove(k);
        gridPane.add(Column, 2, 0);

        summaryToggle = false;

        // find the summary
        javafx.scene.Node l = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node).equals(0) && GridPane.getColumnIndex(node).equals(0)) {
                l = node;
            }
        }

        // remove the summary
        gridPane.getChildren().remove(l);

        // put the room image back
        gridPane.add(imageNode, 0, 0);
    }


    /**
     * This method handles the event related to the
     * settings button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showSettings();
        });
    }

    /**
     * This method handles the event related to the
     * replay button.
     */
    public void addReplayEvent() {
        replayButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            articulateRoomDescription();
        });
    }


    /**
     * This method handles the event related to the
     * settings back button.
     */
    public void addSettingsBackEvent() {
        settingsBackButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showSettings();
        });
    }


    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
//            SaveView.quickSaveGame(model);
            int width = 300;
            Stage namingStage = new Stage();
            namingStage.initModality(Modality.APPLICATION_MODAL);

            namingStage.setTitle("Name your save slot!");

            Label namingLabel = new Label("Name your save slot:");
            namingLabel.setId("NamingLabel");
            namingLabel.setStyle("-fx-text-fill: white;");
            namingLabel.setFont(new Font("Arial", 20));

            TextField namingTextField = new TextField("Untitled Save");
            namingTextField.setPromptText("Name your save here.");

            Button confirmButton = new Button("Confirm");
            confirmButton.setId("Confirm");
            customizeButton2(confirmButton, width, 50);
            makeButtonAccessible(confirmButton, "Confirm Button", "This button renames the save slot base on the text input.", "This button renames the save slot base on the text input. Click it return.");

            confirmButton.setOnAction(e1 -> {
                namingStage.requestFocus();
                SaveView.SaveGameWithName(model, namingTextField.getText());
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
     * Save event used for non button clicks
     */
    public void addSaveEvent2() {
        gridPane.requestFocus();
        SaveView saveView = new SaveView(this);
    }


    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }


    /**
     * addSummaryEvent
     * __________________________
     * This method handles the event related to the summary button.
     */
    public void addSummaryEvent() {
        summaryButton.setOnAction(e -> {
            stopArticulation();
            showSummary();
        });
    }

    /**
     * addSummaryBackEvent
     * __________________________
     * This method handles the event related to the button that brings the user "back"
     * from the summary menu
     */
    public void addSummaryBackEvent() {
        summaryBackButton.setOnAction(e -> {
            stopArticulation();
            hideSummary();
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        stopArticulation();
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        Integer roomNumber_ = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomNumber = roomNumber_.toString();

        musicFile = "./" + adventureName + "/sounds/" + roomNumber + ".mp3" ;

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;
    }


    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    /**
     * getPfpImage
     * __________________________
     *
     * Get the image for the current headshot and place
     * it in the pfp
     */
    private void getPfpImage() {
        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        if (100 < roomNumber && roomNumber < 200) {
            String roomImage = this.model.getDirectoryName() + "/headshots/" + roomNumber + ".jpg";
            Image roomImageFile = new Image(roomImage);
            pfp = new ImageView(roomImageFile);
            pfp.setPreserveRatio(false);
            pfp.setFitWidth(200);
            pfp.setFitHeight(200);
        } else {
            String roomImage = this.model.getDirectoryName() + "/headshots/" + "1000" + ".jpg";
            Image roomImageFile = new Image(roomImage);
            pfp = new ImageView(roomImageFile);
            pfp.setPreserveRatio(false);
            pfp.setFitWidth(200);
            pfp.setFitHeight(200);
        }

    }

    /**
     * addTextSpeedEvent
     *
     * This button opens the text speed toggle menu from settings.
     */
    public void addTextSpeedEvent() {
        textSpeedButton.setOnAction(e -> {
            gridPane.requestFocus();
            TextSpeedView textSpeedView = new TextSpeedView(this);
        });
    }
}

