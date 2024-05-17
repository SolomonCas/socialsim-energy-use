package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class StorageRoom extends PatchField {


    // VARIABLES
    public static StorageRoomFactory storageRoomFactory;

    static {
        storageRoomFactory = new StorageRoomFactory();
    }



    // CONSTRUCTOR
    protected StorageRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Storage Room";
    }

    // INNER CLASS
    public static class StorageRoomFactory extends PatchFieldFactory {
        public StorageRoom create(List<Patch> patches, int num) {
            return new StorageRoom(patches, num);
        }
    }

}