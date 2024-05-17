package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Pantry extends PatchField {

    // VARIABLES
    public static PantryFactory pantryFactory;

    static {
        pantryFactory = new PantryFactory();
    }




    // CONSTRUCTOR
    protected Pantry(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Pantry Room";
    }

    // INNER CLASS
    public static class PantryFactory extends PatchFieldFactory {
        public Pantry create(List<Patch> patches, int num) {
            return new Pantry(patches, num);
        }
    }

}