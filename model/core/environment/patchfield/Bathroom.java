package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.PatchField;
import javafx.util.Pair;

import java.util.List;

public class Bathroom extends PatchField {

    // VARIABLES

    public List<Patch> area;
    public String variation;

    public static BathroomFactory bathroomFactory;

    static {
        bathroomFactory = new BathroomFactory();
    }


    // CONSTRUCTOR
    protected Bathroom(List<Patch> patches, String str) {
        super(patches);
        this.area = patches;
        this.variation = str;

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    /* Getter & Setter */

    public List<Patch> getArea() {
        return area;
    }

    public void setArea(List<Patch> patches) {
        area = patches;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String str) {
        variation = str;
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Bathroom";
    }

    // INNER CLASS
    public static class BathroomFactory extends PatchFieldFactory {
        public Bathroom create(List<Patch> patches, String str) {
            return new Bathroom(patches, str);
        }
    }

}