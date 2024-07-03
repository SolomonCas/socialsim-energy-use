package com.socialsim.model.core.environment.patchobject;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.BaseObject;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Amenity extends PatchObject {

    // VARIABLES
    private final List<AmenityBlock> amenityBlocks;
    private final List<AmenityBlock> attractors;


    // CONSTRUCTOR
    protected Amenity(List<AmenityBlock> amenityBlocks) {
        this.amenityBlocks = amenityBlocks;
        // Only proceed when this amenity has blocks
        if (this.amenityBlocks != null) {
            this.attractors = new ArrayList<>();
            // Set the parent of each amenity block to this amenity
            // In turn, set the contents of the patch in each amenity block to the amenity block
            // Signal to this patch's neighbors that an amenity was placed in here
            // Also, set all this amenity's attractors to the pertinent list
            for (AmenityBlock amenityBlock : this.amenityBlocks) {
                amenityBlock.setParent(this);
                amenityBlock.getPatch().setAmenityBlock(amenityBlock);
                if (amenityBlock.getParent().getClass() != Chair.class
                        && amenityBlock.getParent().getClass() != Light.class
                        && amenityBlock.getParent().getClass() != WindowBlinds.class
                        && amenityBlock.getParent().getClass() != Aircon.class
                        && amenityBlock.getParent().getClass() != Switch.class
                        && amenityBlock.getParent().getClass() != Toilet.class
                        && amenityBlock.getParent().getClass() != Sink.class
                        && amenityBlock.getParent().getClass() != OfficeToilet.class
                        && amenityBlock.getParent().getClass() != OfficeSink.class) {
                    amenityBlock.getPatch().signalAddAmenityBlock();
                }

                if (amenityBlock.isAttractor()) {
                    this.attractors.add(amenityBlock);
                }
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



        public abstract static class AmenityBlockFactory extends ObjectFactory {
            public abstract AmenityBlock create(Patch patch, boolean attractor, boolean hasGraphic);
        }

    }


    public abstract static class AmenityFactory extends ObjectFactory {
    }
}