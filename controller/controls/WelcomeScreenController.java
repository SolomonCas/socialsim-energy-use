package com.socialsim.controller.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class WelcomeScreenController extends Controller {

    @FXML
    ChoiceBox<String> environmentChoice;
    @FXML
    Button startButton;

    public static String environment = null;

    public WelcomeScreenController() {
    }

    @FXML
    public void gotoEnvironment() {
        environment = environmentChoice.getValue();
        this.setClosedWithAction(true);
        stage.close();
    }

}