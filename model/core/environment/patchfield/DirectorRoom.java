package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DirectorRoom extends PatchField {

    // VARIABLES
    public static DeanRoomFactory deanRoomFactory;

    static {
        deanRoomFactory = new DeanRoomFactory();
    }



    // CONSTRUCTOR
    protected DirectorRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class DeanRoomFactory extends PatchFieldFactory {
        public DirectorRoom create(List<Patch> patches, int num) {
            return new DirectorRoom(patches, num);
        }
    }

}