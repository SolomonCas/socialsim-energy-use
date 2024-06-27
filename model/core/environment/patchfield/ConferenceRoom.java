package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class ConferenceRoom extends PatchField {

    // VARIABLES
    public static ConferenceRoom.ConferenceRoomFactory conferenceRoomFactory;

    static {
        conferenceRoomFactory = new ConferenceRoom.ConferenceRoomFactory();
    }




    // CONSTRUCTOR
    protected ConferenceRoom(List<Patch> patches, String str) {
        super(patches);

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
        }
    }



    // INNER CLASS
    public static class ConferenceRoomFactory extends PatchField.PatchFieldFactory {
        public ConferenceRoom create(List<Patch> patches, String str) {
            return new ConferenceRoom(patches, str);
        }
    }
}
