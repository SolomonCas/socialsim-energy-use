package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.WhiteboardGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;

import java.util.List;

public class Whiteboard extends Goal {

    /***** VARIABLES *****/
    public static final Whiteboard.WhiteboardFactory whiteboardFactory;
    private final WhiteboardGraphic whiteboardGraphic;

    static {
        whiteboardFactory = new Whiteboard.WhiteboardFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Whiteboard(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.whiteboardGraphic = new WhiteboardGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Whiteboard" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.whiteboardGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.whiteboardGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class WhiteboardBlock extends Amenity.AmenityBlock {
        public static Whiteboard.WhiteboardBlock.WhiteboardBlockFactory whiteboardBlockFactory;

        static {
            whiteboardBlockFactory = new Whiteboard.WhiteboardBlock.WhiteboardBlockFactory();
        }

        private WhiteboardBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class WhiteboardBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Whiteboard.WhiteboardBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Whiteboard.WhiteboardBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class WhiteboardFactory extends GoalFactory {
        public static Whiteboard create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Whiteboard(amenityBlocks, enabled, facing);
        }
    }

}