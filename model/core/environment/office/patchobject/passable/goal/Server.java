package com.socialsim.model.core.environment.office.patchobject.passable.goal;

import com.socialsim.controller.generic.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.office.graphics.amenity.OfficeAmenityGraphic;
import com.socialsim.controller.office.graphics.amenity.graphic.ServerGraphic;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.Amenity;
import com.socialsim.model.core.environment.generic.patchobject.passable.goal.Goal;

import java.util.List;

public class Server extends Goal {

    public static final Server.ServerFactory serverFactory;
    private final ServerGraphic serverGraphic;

    static {
        serverFactory = new Server.ServerFactory();
    }

    protected Server(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);

        this.serverGraphic = new ServerGraphic(this);
    }


    @Override
    public String toString() {
        return "Server" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public OfficeAmenityGraphic getGraphicObject() {
        return this.serverGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.serverGraphic.getGraphicLocation();
    }

    public static class ServerBlock extends Amenity.AmenityBlock {
        public static Server.ServerBlock.ServerBlockFactory serverBlockFactory;

        static {
            serverBlockFactory = new Server.ServerBlock.ServerBlockFactory();
        }

        private ServerBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class ServerBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Server.ServerBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Server.ServerBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class ServerFactory extends GoalFactory {
        public static Server create(List<AmenityBlock> amenityBlocks, boolean enabled) {
            return new Server(amenityBlocks, enabled);
        }
    }

}