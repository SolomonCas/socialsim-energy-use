package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Wall extends PatchField {


    // VARIABLES
    public static WallFactory wallFactory;

    static {
        wallFactory = new WallFactory();
    }


    // CONSTRUCTOR
    protected Wall(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
            patch.signalAddWall();
        }
    }



    // INNER ABSTRACT CLASS
    public static class WallFactory extends PatchFieldFactory {
        public Wall create(List<Patch> patches, int num) {
            return new Wall(patches, num);
        }
    }

}