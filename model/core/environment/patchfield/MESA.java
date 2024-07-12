package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class MESA extends PatchField {



    // VARIABLES
    public static MESAFactory MESAFactory;

    static {
        MESAFactory = new MESAFactory();
    }



    // CONSTRUCTOR
    protected MESA(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "MESA";
    }

    // INNER CLASS
    public static class MESAFactory extends PatchFieldFactory {
        public MESA create(List<Patch> patches, String str) {
            return new MESA(patches, str);
        }
    }

}