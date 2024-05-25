package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DirectorRoom extends PatchField {

    // VARIABLES
    public static DirectorRoomFactory directorRoomFactory;

    static {
        directorRoomFactory = new DirectorRoomFactory();
    }



    // CONSTRUCTOR
    protected DirectorRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Director Room";
    }

    // INNER CLASS
    public static class DirectorRoomFactory extends PatchFieldFactory {
        public DirectorRoom create(List<Patch> patches, String str) {
            return new DirectorRoom(patches, str);
        }
    }

}