package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DataCollectionRoom extends PatchField {

    // VARIABLES
    public static DataCollectionRoomFactory dataCollectionRoomFactory;

    static {
        dataCollectionRoomFactory = new DataCollectionRoomFactory();
    }


    // CONSTRUCTOR

    protected DataCollectionRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Data Collection";
    }


    // INNER CLASS
    public static class DataCollectionRoomFactory extends PatchFieldFactory {
        public DataCollectionRoom create(List<Patch> patches, String str) {
            return new DataCollectionRoom(patches, str);
        }
    }

}