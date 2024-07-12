package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import javafx.util.Pair;

import java.util.List;

public class CoffeeQueue extends QueueingPatchField {


    // VARIABLES
    public static CoffeeQueueFactory coffeeQueueFactory;

    static {
        coffeeQueueFactory = new CoffeeQueueFactory();
    }



    // CONSTRUCTOR
    protected CoffeeQueue(List<Patch> patches, Amenity target, String string) {
        super(patches, target);

        Pair<QueueingPatchField, String> pair = new Pair<>(this, string);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class CoffeeQueueFactory extends PatchField.PatchFieldFactory {
        public CoffeeQueue create(List<Patch> patches, Amenity target, String string) {
            return new CoffeeQueue(patches, target, string);
        }
    }

}