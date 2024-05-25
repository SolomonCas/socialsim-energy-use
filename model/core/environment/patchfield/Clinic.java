package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class Clinic extends PatchField {

    // VARIABLES
    public static Clinic.ClinicFactory clinicFactory;

    static {
        clinicFactory = new Clinic.ClinicFactory();
    }




    // CONSTRUCTOR
    protected Clinic(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class ClinicFactory extends PatchField.PatchFieldFactory {
        public Clinic create(List<Patch> patches, String str) {
            return new Clinic(patches, str);
        }
    }

}
