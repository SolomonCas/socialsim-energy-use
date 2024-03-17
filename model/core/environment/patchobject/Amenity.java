package com.socialsim.model.core.environment.patchobject;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.BaseObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Amenity extends PatchObject {

    // VARIABLES
    private final List<AmenityBlock> amenityBlocks;
    private final List<AmenityBlock> attractors;


    // CONSTRUCTOR
    protected Amenity(List<AmenityBlock> amenityBlocks) {
        this.amenityBlocks = amenityBlocks;

        if (this.amenityBlocks != null) {
            this.attractors = new ArrayList<>();

            for (AmenityBlock amenityBlock : this.amenityBlocks) {
                amenityBlock.setParent(this);
                amenityBlock.getPatch().setAmenityBlock(amenityBlock);
//                if (amenityBlock.getParent().getClass() != Door.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.Door.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.Chair.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.Table.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.MeetingDesk.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.Toilet.class
//                        && amenityBlock.getParent().getClass() != com.socialsim.model.core.environment.patchobject.passable.goal.Sink.class
//                        && amenityBlock.getParent().getClass() != Security.class) {
//                    amenityBlock.getPatch().signalAddAmenityBlock();
//                }
//
//                if (amenityBlock.isAttractor()) {
//                    this.attractors.add(amenityBlock);
//                }
            }
        }
        else {
            this.attractors = null;
        }
    }


    // METHODS



    // GETTERS
    public List<AmenityBlock> getAmenityBlocks() {
        return amenityBlocks;
    }

    public List<AmenityBlock> getAttractors() {
        return attractors;
    }



    // SETTERS


    public abstract static class AmenityBlock {

        // VARIABLES
        private Amenity parent;
        private final Patch patch;
        private final boolean attractor;
        private final boolean hasGraphic;
        private boolean isReserved;



        // CONSTRUCTOR
        protected AmenityBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            this.patch = patch;
            this.attractor = attractor;
            this.hasGraphic = hasGraphic;
            this.isReserved = false;
        }



        // METHODS



        // GETTERS
        public Amenity getParent() {
            return parent;
        }
        public Patch getPatch() {
            return patch;
        }
        public boolean isAttractor() {
            return attractor;
        }
        public boolean hasGraphic() {
            return hasGraphic;
        }
        public boolean getIsReserved() {
            return isReserved;
        }



        // SETTERS
        public void setParent(Amenity parent) {
            this.parent = parent;
        }
        public void setIsReserved(boolean isReserved) {
            this.isReserved = isReserved;
        }



        public abstract static class AmenityBlockFactory extends BaseObject.ObjectFactory {
            public abstract AmenityBlock create(Patch patch, boolean attractor, boolean hasGraphic);
        }

    }


    public abstract static class AmenityFactory extends BaseObject.ObjectFactory {
    }
}