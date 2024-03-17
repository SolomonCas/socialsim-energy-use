package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Reception extends PatchField {


    // VARIABLES
    public static Reception.ReceptionFactory receptionFactory;

    static {
        receptionFactory = new Reception.ReceptionFactory();
    }



    // CONSTRUCTOR
    protected Reception(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }


    // INNER CLASS
    public static class ReceptionFactory extends PatchFieldFactory {
        public Reception create(List<Patch> patches, int num) {
            return new Reception(patches, num);
        }
    }

}