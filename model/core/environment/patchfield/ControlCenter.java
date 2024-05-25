package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class ControlCenter extends PatchField {

    // VARIABLES
    public static ControlCenterFactory controlCenterFactory;

    static {
        controlCenterFactory = new ControlCenterFactory();
    }


    // CONSTRUCTOR

    protected ControlCenter(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Control Center";
    }


    // INNER CLASS
    public static class ControlCenterFactory extends PatchFieldFactory {
        public ControlCenter create(List<Patch> patches, String str) {
            return new ControlCenter(patches, str);
        }
    }

}