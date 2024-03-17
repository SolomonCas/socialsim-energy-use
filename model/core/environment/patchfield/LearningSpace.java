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



    // CONSTRUCTOR
    protected LearningSpace(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class LearningSpaceFactory extends PatchFieldFactory {
        public LearningSpace create(List<Patch> patches, int num) {
            return new LearningSpace(patches, num);
        }
    }

}