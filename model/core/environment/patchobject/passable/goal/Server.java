package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.ServerGraphic;
import com.socialsim.model.core.environment.Patch;

import java.util.List;

public class Server extends Goal {

    /***** VARIABLES *****/
    public static final ServerFactory serverFactory;
    private final ServerGraphic serverGraphic;

    static {
        serverFactory = new ServerFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Server(List<AmenityBlock> amenityBlocks, boolean enabled, String type) {
        super(amenityBlocks, enabled);

        this.serverGraphic = new ServerGraphic(this, type);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Server" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.serverGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.serverGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class ServerBlock extends AmenityBlock {
        public static ServerBlockFactory serverBlockFactory;

        static {
            serverBlockFactory = new ServerBlockFactory();
        }

        private ServerBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ServerBlockFactory extends AmenityBlockFactory {
            @Override
            public ServerBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new ServerBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ServerFactory extends GoalFactory {
        public static Server create(List<AmenityBlock> amenityBlocks, boolean enabled, String type) {
            return new Server(amenityBlocks, enabled, type);
        }
    }

}