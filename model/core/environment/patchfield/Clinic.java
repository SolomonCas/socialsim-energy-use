package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Clinic extends PatchField {

    // VARIABLES

    public List<Patch> area;
    public String variation;

    public static Clinic.ClinicFactory clinicFactory;

    static {
        clinicFactory = new Clinic.ClinicFactory();
    }

    // CONSTRUCTOR
    protected Clinic(List<Patch> patches, String str) {
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


    // INNER CLASS
    public static class ClinicFactory extends PatchField.PatchFieldFactory {
        public Clinic create(List<Patch> patches, String str) {
            return new Clinic(patches, str);
        }
    }

}
