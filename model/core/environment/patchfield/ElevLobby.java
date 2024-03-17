package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class ElevLobby extends PatchField {


    // VARIABLES
    public static ElevLobbyFactory elevLobbyFactory;

    static {
        elevLobbyFactory = new ElevLobbyFactory();
    }



    // CONSTRUCTOR
    protected ElevLobby(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class ElevLobbyFactory extends PatchFieldFactory {
        public ElevLobby create(List<Patch> patches, int num) {
            return new ElevLobby(patches, num);
        }
    }

}