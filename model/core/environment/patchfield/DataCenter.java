package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DataCenter extends PatchField {



    // VARIABLES
    public static DataCenterFactory dataCenterFactory;

    static {
        dataCenterFactory = new DataCenterFactory();
    }



    // CONSTRUCTOR
    protected DataCenter(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Data Center";
    }

    // INNER CLASS
    public static class DataCenterFactory extends PatchFieldFactory {
        public DataCenter create(List<Patch> patches, int num) {
            return new DataCenter(patches, num);
        }
    }

}