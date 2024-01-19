package com.socialsim.controller.generic.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeScreenController extends ScreenController {

    @FXML Button startButton;

    public static String environment = "Office";

    public WelcomeScreenController() {
    }

    @FXML
    public void openSimulator() {
        this.setClosedWithAction(true);
        stage.close();
    }

    @Override
    protected void closeAction() {
    }

}