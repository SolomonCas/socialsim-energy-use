package com.socialsim.controller.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.HashMap;

public class Controller {
    private final HashMap<String, Object> windowInput;
    private final HashMap<String, Object> windowOutput;
    private boolean closedWithAction;
    protected Stage stage;

    public Controller() {
        this.closedWithAction = false;
        this.windowInput = new HashMap<>();
        this.windowOutput = new HashMap<>();
        this.stage = null;
    }

    public Stage getStage() {
        return this.stage;
    }

    public static FXMLLoader getLoader(Class<?> classType, String interfaceLocation) {
        return new FXMLLoader(classType.getResource(interfaceLocation));
    }

    public boolean isClosedWithAction() {
        return this.closedWithAction;
    }

    public void setClosedWithAction(boolean closedWithAction) {
        this.closedWithAction = closedWithAction;
    }

    public HashMap<String, Object> getWindowInput() {
        return windowInput;
    }

    public HashMap<String, Object> getWindowOutput() {
        return windowOutput;
    }

    private void closeAction() {}

    public void showWindow(Parent loadedRoot, String title, boolean showAndWait, boolean isAlwaysOnTop) {

        // Initialize the container inside the screen (scene) and assign it the content to display on it (loadedRoot)
        Scene scene = loadedRoot.getScene();
        if (scene == null) {
            scene = new Scene(loadedRoot);
        }
        else {
            scene.setRoot(loadedRoot);
        }
        scene.getRoot().setStyle("-fx-font-family: 'serif'"); // Add css styles


        // Initialize the window itself
        Stage stage = new Stage();
        // Set window attributes
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        this.stage = stage;


        stage.setOnCloseRequest(event -> { closeAction(); });

        // Add an event handler to the stage to listen for key presses.
        this.stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            // Check if the pressed key is the Escape key.
            if (event.getCode() == KeyCode.ESCAPE) {
                // Call the closeAction method if the Escape key is pressed.
                closeAction();
            }
        });

        // If 'showAndWait' is true, make the stage a modal window and wait for user interaction.
        if (showAndWait) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        // Otherwise, set 'isAlwaysOnTop' to determine if the stage should always stay on top and show the stage without blocking user interaction.
        else {
            stage.setAlwaysOnTop(isAlwaysOnTop);
            stage.show();
        }
    }


}