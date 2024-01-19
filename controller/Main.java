package com.socialsim.controller;

import com.socialsim.controller.office.controls.OfficeScreenController;
import com.socialsim.controller.generic.controls.ScreenController;
import com.socialsim.controller.generic.controls.WelcomeScreenController;
import com.socialsim.model.simulator.grocery.GrocerySimulator;
import com.socialsim.model.simulator.mall.MallSimulator;
import com.socialsim.model.simulator.office.OfficeSimulator;
import com.socialsim.model.simulator.university.UniversitySimulator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    public static GrocerySimulator grocerySimulator = null;
    public static MallSimulator mallSimulator = null;
    public static OfficeSimulator officeSimulator = null;
    public static UniversitySimulator universitySimulator = null;

    public static boolean hasMadeChoice = false;
    public static FXMLLoader mainScreenLoader;
    public static Parent mainRoot;
    public static ScreenController mainScreenController;
    public static String mainTitle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeSimulator();

        FXMLLoader welcomeInterfaceLoader = ScreenController.getLoader(getClass(), "/com/socialsim/view/WelcomeScreen.fxml");
        Parent welcomeRoot = welcomeInterfaceLoader.load();
        WelcomeScreenController welcomeScreenController = welcomeInterfaceLoader.getController();

        while (true) {
            welcomeScreenController.setClosedWithAction(false);

            Main.hasMadeChoice = false;

            welcomeScreenController.showWindow(welcomeRoot, "SocialSim", true, false);

            if (welcomeScreenController.isClosedWithAction()) {
                Main.hasMadeChoice = true;

                mainScreenLoader = ScreenController.getLoader(getClass(), "/com/socialsim/view/OfficeScreen.fxml");
                mainRoot = mainScreenLoader.load();
                mainScreenController = (OfficeScreenController) mainScreenLoader.getController();
                mainTitle = "Office SocialSim";
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

    private void initializeSimulator() {
        grocerySimulator = new GrocerySimulator();
        mallSimulator = new MallSimulator();
        officeSimulator = new OfficeSimulator();
        universitySimulator = new UniversitySimulator();
    }

}