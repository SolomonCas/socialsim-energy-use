package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;
import javafx.util.Pair;

import java.util.List;

public class WaterDispenserQueue extends QueueingPatchField {


    // VARIABLES
    public static WaterDispenserQueueFactory waterDispenserQueueFactory;

    static {
        waterDispenserQueueFactory = new WaterDispenserQueueFactory();
    }



    // CONSTRUCTOR
    protected WaterDispenserQueue(List<Patch> patches, Amenity target, int num) {
        super(patches, target);

        Pair<QueueingPatchField, Integer> pair = new Pair<>(this, num);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class WaterDispenserQueueFactory extends PatchField.PatchFieldFactory {
        public WaterDispenserQueue create(List<Patch> patches, Amenity target, int num) {
            return new WaterDispenserQueue(patches, target, num);
        }
    }

}