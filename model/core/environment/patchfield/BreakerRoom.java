package com.socialsim.model.core.environment.patchfield;


import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class BreakerRoom extends PatchField{

    // VARIABLES
    public static BreakerRoom.BreakerRoomFactory breakerRoomFactory;

    static {
        breakerRoomFactory = new BreakerRoom.BreakerRoomFactory();
    }




    // CONSTRUCTOR
    protected BreakerRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }




    // INNER CLASS
    public static class BreakerRoomFactory extends PatchFieldFactory {
        public BreakerRoom create(List<Patch> patches, String str) {
            return new BreakerRoom(patches, str);
        }
    }
}
