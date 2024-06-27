package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class StaffArea extends PatchField {

    // VARIABLES
    public static StaffAreaFactory staffAreaFactory;

    static {
        staffAreaFactory = new StaffAreaFactory();
    }



    // CONSTRUCTOR
    protected StaffArea(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Staff Area";
    }

    // INNER CLASS
    public static class StaffAreaFactory extends PatchFieldFactory {
        public StaffArea create(List<Patch> patches, String str) {
            return new StaffArea(patches, str);
        }
    }

}