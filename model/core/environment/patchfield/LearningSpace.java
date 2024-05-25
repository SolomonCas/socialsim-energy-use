package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class LearningSpace extends PatchField {


    // VARIABLES
    public static LearningSpaceFactory learningSpaceFactory;

    static {
        learningSpaceFactory = new LearningSpaceFactory();
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Learning Space Room";
    }

    // CONSTRUCTOR
    protected LearningSpace(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class LearningSpaceFactory extends PatchFieldFactory {
        public LearningSpace create(List<Patch> patches, String str) {
            return new LearningSpace(patches, str);
        }
    }

}