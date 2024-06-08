package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;
import javafx.util.Pair;

import java.util.List;

public class FridgeQueue extends QueueingPatchField {


    // VARIABLES
    public static FridgeQueueFactory fridgeQueueFactory;

    static {
        fridgeQueueFactory = new FridgeQueueFactory();
    }



    // CONSTRUCTOR
    protected FridgeQueue(List<Patch> patches, Amenity target, int num) {
        super(patches, target);

        Pair<QueueingPatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class FridgeQueueFactory extends PatchField.PatchFieldFactory {
        public FridgeQueue create(List<Patch> patches, Amenity target, int num) {
            return new FridgeQueue(patches, target, num);
        }
    }

}