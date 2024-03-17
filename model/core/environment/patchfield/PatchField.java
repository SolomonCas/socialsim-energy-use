package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.BaseObject;
import com.socialsim.model.core.environment.Patch;

import java.util.ArrayList;
import java.util.List;

public abstract class PatchField extends BaseObject {
    // VARIABLES
    private final List<Patch> associatedPatches;

    // CONSTRUCTOR
    protected PatchField() {
        super();

        this.associatedPatches = new ArrayList<>();
    }

    protected PatchField(List<Patch> patches) {
        super();

        this.associatedPatches = new ArrayList<>();
        associatedPatches.addAll(patches);
    }


    // METHODS


    // GETTERS
    public List<Patch> getAssociatedPatches() {
        return associatedPatches;
    }


    // INNER ABSTRACT CLASS
    public static abstract class PatchFieldFactory extends ObjectFactory {
    }


}