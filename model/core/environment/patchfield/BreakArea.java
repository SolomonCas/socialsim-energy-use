package com.socialsim.model.core.environment.patchfield;


import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class BreakArea extends PatchField{

    protected BreakArea(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    public static BreakArea.BreakAreaFactory breakAreaFactory;

    static {
        breakAreaFactory = new BreakArea.BreakAreaFactory();
    }

    public static class BreakAreaFactory extends PatchFieldFactory {
        public BreakArea create(List<Patch> patches, int num) {
            return new BreakArea(patches, num);
        }
    }

}
