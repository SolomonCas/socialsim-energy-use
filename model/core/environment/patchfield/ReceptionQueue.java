package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.passable.goal.ReceptionTable;
import javafx.util.Pair;

import java.util.List;

public class ReceptionQueue extends QueueingPatchField {


    // VARIABLES
    public static ReceptionQueueFactory receptionQueueFactory;

    static {
        receptionQueueFactory = new ReceptionQueueFactory();
    }



    // CONSTRUCTOR
    protected ReceptionQueue(List<Patch> patches, ReceptionTable target, String string) {
        super(patches, target);

        Pair<QueueingPatchField, String> pair = new Pair<>(this, string);
        for(Patch patch : patches) {
            patch.setQueueingPatchField(pair);
        }
    }


    // INNER CLASS
    public static class ReceptionQueueFactory extends PatchField.PatchFieldFactory {
        public ReceptionQueue create(List<Patch> patches, ReceptionTable target, String string) {
            return new ReceptionQueue(patches, target, string);
        }
    }

}