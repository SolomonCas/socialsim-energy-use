package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.BaseObject;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.ArrayList;
import java.util.List;

public class QueueingPatchField extends BaseObject {


    // VARIABLES
    private final List<Patch> associatedPatches;
    private final int originalSize;
    private final Amenity target;
    private List<Agent> queueingAgents;




    // CONSTRUCTOR
    protected QueueingPatchField(List<Patch> patches, Amenity target) {
        super();

        this.associatedPatches = new ArrayList<>();
        associatedPatches.addAll(patches);
        this.originalSize = associatedPatches.size();
        this.target = target;
        this.queueingAgents = new ArrayList<>();
    }


    // METHODS
    public boolean isEmpty() {
        return queueingAgents.isEmpty();
    }

    // This is used to revert to the original size of the queue/line;
    public void reset() {
        while(originalSize != associatedPatches.size()) {
            associatedPatches.removeLast();
        }
    }

    public Patch getLastQueuePatch() {
        return this.associatedPatches.get(associatedPatches.size() - 1);
    }

    public boolean isQueueFull() {
        return getAssociatedPatches().size() == getQueueingAgents().size();
    }


    // GETTERS
    public List<Patch> getAssociatedPatches() {
        return associatedPatches;
    }

    public Amenity getTarget() {
        return target;
    }

    public List<Agent> getQueueingAgents() {
        return queueingAgents;
    }




    // INNER ABSTRACT CLASS
    public static abstract class QueueingPatchFieldFactory extends ObjectFactory {
    }
}