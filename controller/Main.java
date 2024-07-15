package com.socialsim.controller;

import com.socialsim.controller.controls.Controller;
import com.socialsim.controller.controls.ScreenController;
import com.socialsim.controller.controls.WelcomeScreenController;
import com.socialsim.model.simulator.Simulator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    public static FXMLLoader mainScreenLoader;
    public static Parent mainRoot;
    public static Controller mainScreenController;

    public static String mainTitle;
    public static boolean hasMadeChoice = false;
    public static Simulator simulator = null;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        simulator = new Simulator();

        FXMLLoader welcomeInterfaceLoader = Controller.getLoader(getClass(), "/com/socialsim/view/WelcomeScreen.fxml");
        Parent welcomeRoot = welcomeInterfaceLoader.load();
        WelcomeScreenController welcomeScreenController = welcomeInterfaceLoader.getController();

        while(true) {
            welcomeScreenController.setClosedWithAction(false);
            Main.hasMadeChoice = false;
            welcomeScreenController.showWindow(welcomeRoot, "SocialSim", true, false);

            if (welcomeScreenController.isClosedWithAction()) {
                Main.hasMadeChoice = true;

                if (WelcomeScreenController.environment.equals("Original Office Layout")) {
                    mainScreenLoader = Controller.getLoader(getClass(), "/com/socialsim/view/OfficeScreen.fxml");
                    mainRoot = mainScreenLoader.load();
                    mainScreenController = (ScreenController) mainScreenLoader.getController();
                    mainTitle = "Original Office Layout";
                }

                else if (WelcomeScreenController.environment.equals("Alternative Office Layout A")) {
                    mainScreenLoader = Controller.getLoader(getClass(), "/com/socialsim/view/OfficeScreenLayoutA.fxml");
                    mainRoot = mainScreenLoader.load();
                    mainScreenController = (ScreenController) mainScreenLoader.getController();
                    mainTitle = "Alternative Office Layout A";
                }

                else if (WelcomeScreenController.environment.equals("Alternative Office Layout B")) {
                    mainScreenLoader = Controller.getLoader(getClass(), "/com/socialsim/view/OfficeScreenLayoutB.fxml");
                    mainRoot = mainScreenLoader.load();
                    mainScreenController = (ScreenController) mainScreenLoader.getController();
                    mainTitle = "Alternative Office Layout B";
                }


            }
            else if (!welcomeScreenController.isClosedWithAction()) {
                break;
            }

            if (!Main.hasMadeChoice) {
                break;
            }

            mainScreenController.showWindow(mainRoot, mainTitle, true, false);
        }


    }
}