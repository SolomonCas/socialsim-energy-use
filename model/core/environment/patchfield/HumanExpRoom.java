package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class HumanExpRoom extends PatchField {

    // VARIABLES
    public static HumanExpRoomFactory humanExpRoomFactory;

    static {
        humanExpRoomFactory = new HumanExpRoomFactory();
    }


    // CONSTRUCTOR

    protected HumanExpRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }


    // INNER CLASS
    public static class HumanExpRoomFactory extends PatchFieldFactory {
        public HumanExpRoom create(List<Patch> patches, int num) {
            return new HumanExpRoom(patches, num);
        }
    }

}