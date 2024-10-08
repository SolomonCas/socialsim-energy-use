package com.socialsim.controller.controls;

import com.socialsim.controller.Main;
import com.socialsim.model.core.agent.Action;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.Environment;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

public class EditInteractionsController {

    /***** VARIABLES *****/
    @FXML
    VBox container;
    @FXML
    GridPane gridPane;
    @FXML
    Button btnHelp;
    @FXML Button btnCancel;
    @FXML Button btnResetToDefault;
    @FXML Button btnSave;
    /***** CONSTRUCTOR *****/
    public EditInteractionsController() {
    }
    /***** METHODS *****/
    public void cancelChanges() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void resetToDefault() {
        for (Node node: gridPane.getChildren()) {
            if (node.getClass() == TextField.class) {
                int row = GridPane.getRowIndex(node), column = GridPane.getColumnIndex(node);
                if (row > 0 && column > 0) {
                    ((TextField) node).setText(Environment.defaultInteractionTypeChances.get(column - 1).get(row - 1).toString().replace("]", "").replace("[", ""));
                }
            }
        }
    }

    public boolean checkValidInteraction(String s) {
        s = s.replace(" ", "");
        if (s.matches("^([0-9]|[1-9][0-9]|(100))(,([0-9]|[1-9][0-9]|(100)))*$")) {
            int[] interactionArr = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
            if (!((IntStream.of(interactionArr).sum() == 0 || IntStream.of(interactionArr).sum() == 100) && interactionArr.length == 3)) {
                System.out.println(s);
                System.out.println("invalid");
            }

            return (IntStream.of(interactionArr).sum() == 0 || IntStream.of(interactionArr).sum() == 100) && interactionArr.length == 3;
        }
        else{
            return false;
        }
    }

    public void saveChanges() {
        boolean validInteractionChances = false;

        for (Node node: gridPane.getChildren()) {
            if (node.getClass() == TextField.class) {
                validInteractionChances = this.checkValidInteraction(((TextField) node).getText());
                if (!validInteractionChances){
                    System.out.println(((TextField) node).getText());
                    break;
                }
            }
        }

        if (!validInteractionChances) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            Label label = new Label("Failed to parse. Please make sure the interaction type chances are from 0-100 only, and separate them with commas (,). Also ensure that the sum of the three is only either 0 or 100.");
            label.setWrapText(true);
            alert.getDialogPane().setContent(label);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        else {
            Environment office = Main.simulator.getEnvironment();
            for (Node node: gridPane.getChildren()) {
                if (node.getClass() == TextField.class) {
                    int row = GridPane.getRowIndex(node), column = GridPane.getColumnIndex(node);
                    String s = ((TextField) node).getText();
                    Integer[] interactionArr = Arrays.stream(s.replace(" ", "").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
                    if (row > 0 && column > 0) {
                        office.getInteractionTypeChances().get(column - 1).set(row - 1, new CopyOnWriteArrayList<>(List.of(interactionArr)));
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            Label label = new Label("Interaction Type Chances successfully parsed.");
            label.setWrapText(true);
            alert.getDialogPane().setContent(label);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }
    }

    public void openHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        ImageView img = new ImageView();
        img.setImage(new Image(getClass().getResource("../../view/image/interaction_help.png").toExternalForm()));
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.getDialogPane().setContent(img);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /***** FXML *****/

    @FXML
    private void initialize() {
        Environment office = Main.simulator.getEnvironment();

        for (int i = 0; i < Agent.PersonaActionGroup.values().length + 1; i++) {
            for (int j = 0; j < Action.Name.values().length + 1; j++) {
                if (i == 0 || j == 0) {
                    if (i == 0 && j == 0) {
                        gridPane.add(new Label(""), i, j);
                    }
                    else {
                        if (i == 0) {
                            Label l = new Label(Action.Name.values()[j - 1].name());
                            l.setAlignment(Pos.CENTER);
                            gridPane.add(l, i, j);
                            GridPane.setHalignment(l, HPos.CENTER);
                        }
                        else {
                            Label l = new Label(Agent.PersonaActionGroup.values()[i - 1].name());
                            l.setAlignment(Pos.CENTER);
                            gridPane.add(l, i, j);
                            GridPane.setHalignment(l, HPos.CENTER);

                        }
                    }
                }
                else {
                    TextField tf = new TextField(office.getInteractionTypeChances().get(i - 1).get(j - 1).toString().replace("]", "").replace("[", ""));
                    tf.setAlignment(Pos.CENTER);
                    gridPane.add(tf, i, j);
                    GridPane.setHalignment(tf, HPos.CENTER);
                }
            }
        }
    }


}