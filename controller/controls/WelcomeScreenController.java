package com.socialsim.controller.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeScreenController extends Controller {

    @FXML
    Button startButton;

    public WelcomeScreenController() {
    }

    @FXML
    public void openSimulator() {
        this.setClosedWithAction(true);
        stage.close();
    }
}