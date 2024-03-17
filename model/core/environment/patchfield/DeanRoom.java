package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DeanRoom extends PatchField {

    // VARIABLES
    public static DeanRoomFactory deanRoomFactory;

    static {
        deanRoomFactory = new DeanRoomFactory();
    }



    // CONSTRUCTOR
    protected DeanRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class DeanRoomFactory extends PatchFieldFactory {
        public DeanRoom create(List<Patch> patches, int num) {
            return new DeanRoom(patches, num);
        }
    }

}