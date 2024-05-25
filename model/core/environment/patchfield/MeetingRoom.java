package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class MeetingRoom extends PatchField {


    // VARIABLES
    public static MeetingRoom.MeetingRoomFactory meetingRoomFactory;

    static {
        meetingRoomFactory = new MeetingRoom.MeetingRoomFactory();
    }




    // CONSTRUCTOR
    protected MeetingRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Meeting Room";
    }

    // INNER CLASS
    public static class MeetingRoomFactory extends PatchFieldFactory {
        public MeetingRoom create(List<Patch> patches, String str) {
            return new MeetingRoom(patches, str);
        }
    }

}