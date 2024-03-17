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

    protected ControlCenter(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }


    // INNER CLASS
    public static class ControlCenterFactory extends PatchFieldFactory {
        public ControlCenter create(List<Patch> patches, int num) {
            return new ControlCenter(patches, num);
        }
    }

}