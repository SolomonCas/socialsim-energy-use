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
    private final Amenity target;
    private List<Agent> queueingAgents;
    private Agent currentAgent;




    // CONSTRUCTOR
    protected QueueingPatchField(List<Patch> patches, Amenity target) {
        super();

        this.associatedPatches = new ArrayList<>();
        associatedPatches.addAll(patches);
        this.target = target;
        this.queueingAgents = new ArrayList<>();
        this.currentAgent = null;
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

    public Patch getLastQueuePatch() {
        return this.associatedPatches.get(associatedPatches.size() - 1);
    }

    public Agent getCurrentAgent() {
        return currentAgent;
    }





    // SETTERS
    public void setCurrentAgent(Agent currentAgent) {
        this.currentAgent = currentAgent;
    }






    // INNER ABSTRACT CLASS
    public static abstract class QueueingPatchFieldFactory extends ObjectFactory {
    }
}