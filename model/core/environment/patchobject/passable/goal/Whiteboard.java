package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.WhiteboardGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Whiteboard extends Goal {

    /***** VARIABLES *****/
    public static final WhiteboardFactory whiteboardFactory;
    private final WhiteboardGraphic whiteboardGraphic;

    static {
        whiteboardFactory = new WhiteboardFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Whiteboard(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String length) {
        super(amenityBlocks, enabled);

        this.whiteboardGraphic = new WhiteboardGraphic(this, facing, length);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "White Board" + ((this.enabled) ? "" : " (disabled)");
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
    public static class WhiteboardBlock extends AmenityBlock {
        public static WhiteboardBlockFactory whiteboardBlockFactory;

        static {
            whiteboardBlockFactory = new WhiteboardBlockFactory();
        }

        private WhiteboardBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class WhiteboardBlockFactory extends AmenityBlockFactory {
            @Override
            public WhiteboardBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new WhiteboardBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class WhiteboardFactory extends GoalFactory {
        public static Whiteboard create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing, String length) {
            return new Whiteboard(amenityBlocks, enabled, facing, length);
        }
    }

}