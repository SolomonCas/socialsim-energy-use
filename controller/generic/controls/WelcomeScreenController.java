package com.socialsim.controller.generic.controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

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