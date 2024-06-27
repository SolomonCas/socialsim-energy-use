package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import javafx.util.Pair;

import java.util.List;

public class RefrigeratorQueue extends QueueingPatchField {


    // VARIABLES
    public static RefrigeratorQueueFactory refrigeratorQueueFactory;

    static {
        refrigeratorQueueFactory = new RefrigeratorQueueFactory();
    }



    // CONSTRUCTOR
    protected RefrigeratorQueue(List<Patch> patches, Amenity target, String string) {
        super(patches, target);

        Pair<QueueingPatchField, String> pair = new Pair<>(this, string);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class RefrigeratorQueueFactory extends PatchField.PatchFieldFactory {
        public RefrigeratorQueue create(List<Patch> patches, Amenity target, String string) {
            return new RefrigeratorQueue(patches, target, string);
        }
    }

}