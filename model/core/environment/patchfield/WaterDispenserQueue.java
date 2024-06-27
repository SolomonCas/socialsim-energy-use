package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import javafx.util.Pair;

import java.util.List;

public class WaterDispenserQueue extends QueueingPatchField {


    // VARIABLES
    public static WaterDispenserQueueFactory waterDispenserQueueFactory;

    static {
        waterDispenserQueueFactory = new WaterDispenserQueueFactory();
    }



    // CONSTRUCTOR
    protected WaterDispenserQueue(List<Patch> patches, Amenity target, String string) {
        super(patches, target);

        Pair<QueueingPatchField, String> pair = new Pair<>(this, string);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class WaterDispenserQueueFactory extends PatchField.PatchFieldFactory {
        public WaterDispenserQueue create(List<Patch> patches, Amenity target, String string) {
            return new WaterDispenserQueue(patches, target, string);
        }
    }

}