package com.socialsim.model.core.environment.office.patchfield;

import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchfield.PatchField;
import javafx.util.Pair;

import java.util.List;

public class Cafeteria extends PatchField {

    protected Cafeteria(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    public static CafeteriaFactory cafeteriaFactory;

    static {
        cafeteriaFactory = new CafeteriaFactory();
    }

    public static class CafeteriaFactory extends PatchFieldFactory {
        public Cafeteria create(List<Patch> patches, int num) {
            return new Cafeteria(patches, num);
        }
    }

}