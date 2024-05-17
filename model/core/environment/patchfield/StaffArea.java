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
    protected StaffArea(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
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
        public StaffArea create(List<Patch> patches, int num) {
            return new StaffArea(patches, num);
        }
    }

}