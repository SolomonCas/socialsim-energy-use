package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.BaseObject;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.*;

public class QueueingPatchField extends BaseObject {


    // VARIABLES
    private final List<Patch> associatedPatches;
    private final Amenity target;
    private final List<Agent> queueingAgents;




    // CONSTRUCTOR
    protected QueueingPatchField(List<Patch> patches, Amenity target) {
        super();

        this.associatedPatches = Collections.synchronizedList(new ArrayList<>());
        associatedPatches.addAll(patches);
        this.target = target;
        this.queueingAgents = Collections.synchronizedList(new ArrayList<>());
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