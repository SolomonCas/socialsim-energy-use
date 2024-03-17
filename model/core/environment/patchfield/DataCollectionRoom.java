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

    protected DataCollectionRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }


    // INNER CLASS
    public static class DataCollectionRoomFactory extends PatchFieldFactory {
        public DataCollectionRoom create(List<Patch> patches, int num) {
            return new DataCollectionRoom(patches, num);
        }
    }

}