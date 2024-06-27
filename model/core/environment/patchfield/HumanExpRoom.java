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

    protected HumanExpRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Human Experience Room";
    }

    // INNER CLASS
    public static class HumanExpRoomFactory extends PatchFieldFactory {
        public HumanExpRoom create(List<Patch> patches, String str) {
            return new HumanExpRoom(patches, str);
        }
    }

}