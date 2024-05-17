package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class FacultyRoom extends PatchField {

    // VARIABLES
    public static FacultyRoomFactory facultyRoomFactory;

    static {
        facultyRoomFactory = new FacultyRoomFactory();
    }


    // CONSTRUCTOR

    protected FacultyRoom(List<Patch> patches, int num) {
        super(patches);

        Pair<PatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Faculty Room";
    }

    // INNER CLASS
    public static class FacultyRoomFactory extends PatchFieldFactory {
        public FacultyRoom create(List<Patch> patches, int num) {
            return new FacultyRoom(patches, num);
        }
    }

}