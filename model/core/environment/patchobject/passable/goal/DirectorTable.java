package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.DirectorTableGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectorTable extends Goal {

    /***** VARIABLES *****/
    public static final DirectorTable.DirectorTableFactory directorTableFactory;
    private final DirectorTableGraphic directorTableGraphic;
    private final List<DirectorChair> directorChairs;

    static {
        directorTableFactory = new DirectorTable.DirectorTableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected DirectorTable(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.directorChairs = Collections.synchronizedList(new ArrayList<>());

        this.directorTableGraphic = new DirectorTableGraphic(this, facing);
    }

    /***** GETTER *****/
    public List<DirectorChair> getDirectorChairs() {
        return directorChairs;
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "DirectorTable" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.directorTableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.directorTableGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class DirectorTableBlock extends Amenity.AmenityBlock {
        public static DirectorTable.DirectorTableBlock.DirectorTableBlockFactory directorTableBlockFactory;

        static {
            directorTableBlockFactory = new DirectorTable.DirectorTableBlock.DirectorTableBlockFactory();
        }

        private DirectorTableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class DirectorTableBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public DirectorTable.DirectorTableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new DirectorTable.DirectorTableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class DirectorTableFactory extends GoalFactory {
        public static DirectorTable create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new DirectorTable(amenityBlocks, enabled, facing);
        }
    }
}