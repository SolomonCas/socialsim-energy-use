package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.WindowBlindsGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class WindowBlinds extends Goal {

    /***** VARIABLES *****/
    public static final WindowBlindsFactory windowBlindsFactory;
    private final WindowBlindsGraphic windowBlindsGraphic;
    static {
        windowBlindsFactory = new WindowBlindsFactory();
    }
    private boolean isOpened;
    private int lightRange;

    /***** CONSTRUCTOR *****/
    protected WindowBlinds(List<AmenityBlock> amenityBlocks, boolean enabled, String state, boolean isOpened) {
        super(amenityBlocks, enabled);
        this.windowBlindsGraphic = new WindowBlindsGraphic(this, state);
        this.isOpened = isOpened;
        this.lightRange = 25;
    }

    /***** GETTERS *****/

    public boolean isOpened() {
        return isOpened;
    }

    public WindowBlindsGraphic getWindowBlindsGraphic() {
        return windowBlindsGraphic;
    }

    public int getLightRange(){ return lightRange;}

    /***** SETTER *****/
    public void open(boolean opened) {
        isOpened = opened;
    }

    public void setLightRange(int range){ lightRange = range;}

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "WindowBlinds" + ((this.enabled) ? "" : " (disabled)");
    }
    @Override
    public AmenityGraphic getGraphicObject() {
        return this.windowBlindsGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.windowBlindsGraphic.getGraphicLocation();
    }

    /***** INNER STATIC CLASS *****/
    public static class WindowBlindsBlock extends AmenityBlock {
        public static WindowBlindsBlockFactory windowBlindsBlockFactory;

        static {
            windowBlindsBlockFactory = new WindowBlindsBlockFactory();
        }

        private WindowBlindsBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class WindowBlindsBlockFactory extends AmenityBlockFactory {
            @Override
            public WindowBlindsBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new WindowBlindsBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class WindowBlindsFactory extends GoalFactory {
        public static WindowBlinds create(List<AmenityBlock> amenityBlocks, boolean enabled, String state, boolean isOpened) {
            return new WindowBlinds(amenityBlocks, enabled, state, isOpened);
        }
    }
}