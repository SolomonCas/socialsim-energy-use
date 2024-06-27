package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import javafx.util.Pair;

import java.util.List;

public class BathroomQueue extends QueueingPatchField {


    // VARIABLES
    public static BathroomQueueFactory bathroomQueueFactory;

    static {
        bathroomQueueFactory = new BathroomQueueFactory();
    }



    // CONSTRUCTOR
    protected BathroomQueue(List<Patch> patches, Amenity target, String string) {
        super(patches, target);

        Pair<QueueingPatchField, String> pair = new Pair<>(this, string);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class BathroomQueueFactory extends PatchField.PatchFieldFactory {
        public BathroomQueue create(List<Patch> patches, Amenity target, String string) {
            return new BathroomQueue(patches, target, string);
        }
    }

}