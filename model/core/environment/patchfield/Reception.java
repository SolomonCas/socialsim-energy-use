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
    protected Reception(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Reception Room";
    }

    // INNER CLASS
    public static class ReceptionFactory extends PatchFieldFactory {
        public Reception create(List<Patch> patches, String str) {
            return new Reception(patches, str);
        }
    }

}