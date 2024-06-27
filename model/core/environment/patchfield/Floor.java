package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Floor extends PatchField {


    // VARIABLES
    public static FloorFactory floorFactory;

    static {
        floorFactory = new FloorFactory();
    }



    // CONSTRUCTOR
    protected Floor(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class FloorFactory extends PatchFieldFactory {

        public Floor create(List<Patch> patches, String str) {
            return new Floor(patches, str);
        }
    }

}