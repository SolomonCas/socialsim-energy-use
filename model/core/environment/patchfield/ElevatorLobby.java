package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class ElevatorLobby extends PatchField {


    // VARIABLES
    public List<Patch> area;
    public String variation;


    static {
        elevatorLobbyFactory = new ElevatorLobbyFactory();
    }



    // CONSTRUCTOR
    protected ElevatorLobby(List<Patch> patches, String str) {
        super(patches);

        this.area = patches;
        this.variation = str;

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    /* Getter & Setter */

    public List<Patch> getArea() {
        return area;
    }
    public void setArea(List<Patch> patches) {
        area = patches;
    }
    public String getVariation() {
        return variation;
    }
    public void setVariation(String str) {
        variation = str;
    }


    public static ElevatorLobbyFactory elevatorLobbyFactory;
    // INNER CLASS
    public static class ElevatorLobbyFactory extends PatchFieldFactory {

        public ElevatorLobby create(List<Patch> patches, String str) {
            return new ElevatorLobby(patches, str);
        }
    }

}