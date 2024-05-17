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
    protected DirectorRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
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
        public DirectorRoom create(List<Patch> patches, int num) {
            return new DirectorRoom(patches, num);
        }
    }

}