package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class ResearchCenter extends PatchField {

    // VARIABLES
    public static ResearchCenterFactory researchCenterFactory;

    static {
        researchCenterFactory = new ResearchCenter.ResearchCenterFactory();
    }


    // CONSTRUCTOR

    protected ResearchCenter(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Research Center Room";
    }

    // INNER CLASS
    public static class ResearchCenterFactory extends PatchFieldFactory {
        public ResearchCenter create(List<Patch> patches, String str) {
            return new ResearchCenter(patches, str);
        }
    }

}