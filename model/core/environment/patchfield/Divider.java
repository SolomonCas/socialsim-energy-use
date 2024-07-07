package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Divider extends PatchField {


    // VARIABLES
    public static WallFactory wallFactory;

    static {
        wallFactory = new WallFactory();
    }


    // CONSTRUCTOR
    protected Divider(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
            patch.signalAddWall();
        }
    }



    // INNER ABSTRACT CLASS
    public static class WallFactory extends PatchFieldFactory {
        public Divider create(List<Patch> patches, String str) {
            return new Divider(patches, str);
        }
    }

}