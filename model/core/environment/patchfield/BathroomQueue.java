package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;
import javafx.util.Pair;

import java.util.List;

public class BathroomQueue extends QueueingPatchField {


    // VARIABLES
    public static BathroomQueueFactory bathroomQueueFactory;

    static {
        bathroomQueueFactory = new BathroomQueueFactory();
    }



    // CONSTRUCTOR
    protected BathroomQueue(List<Patch> patches, Amenity target, int num) {
        super(patches, target);

        Pair<QueueingPatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class BathroomQueueFactory extends PatchField.PatchFieldFactory {
        public BathroomQueue create(List<Patch> patches, Amenity target, int num) {
            return new BathroomQueue(patches, target, num);
        }
    }

}