package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class SoloRoom extends PatchField {


    // VARIABLES
    public static SoloRoomFactory soloRoomFactory;

    static {
        soloRoomFactory = new SoloRoomFactory();
    }



    // CONSTRUCTOR
    protected SoloRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class SoloRoomFactory extends PatchFieldFactory {
        public SoloRoom create(List<Patch> patches, int num) {
            return new SoloRoom(patches, num);
        }
    }

}