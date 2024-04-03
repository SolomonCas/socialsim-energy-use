package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.controller.graphics.amenity.AmenityGraphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;
import com.socialsim.controller.graphics.amenity.graphic.TableGraphic;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Goal;

import java.util.List;

public class Table extends Goal {

    /***** VARIABLES *****/
    public static final Table.TableFactory tableFactory;
    private final TableGraphic tableGraphic;

    static {
        tableFactory = new Table.TableFactory();
    }

    /***** CONSTRUCTOR *****/
    protected Table(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
        super(amenityBlocks, enabled);

        this.tableGraphic = new TableGraphic(this, facing);
    }

    /***** OVERRIDE *****/
    @Override
    public String toString() {
        return "Table" + ((this.enabled) ? "" : " (disabled)");
    }

    @Override
    public AmenityGraphic getGraphicObject() {
        return this.tableGraphic;
    }

    @Override
    public AmenityGraphicLocation getGraphicLocation() {
        return this.tableGraphic.getGraphicLocation();
    }


    /***** INNER STATIC CLASS *****/
    public static class TableBlock extends Amenity.AmenityBlock {
        public static Table.TableBlock.TableBlockFactory tableBlockFactory;

        static {
            tableBlockFactory = new Table.TableBlock.TableBlockFactory();
        }

        private TableBlock(Patch patch, boolean attractor, boolean hasGraphic) {
            super(patch, attractor, hasGraphic);
        }

        public static class TableBlockFactory extends Amenity.AmenityBlock.AmenityBlockFactory {
            @Override
            public Table.TableBlock create(Patch patch, boolean attractor, boolean hasGraphic) {
                return new Table.TableBlock(patch, attractor, hasGraphic);
            }
        }
    }

    public static class TableFactory extends GoalFactory {
        public static Table create(List<AmenityBlock> amenityBlocks, boolean enabled, String facing) {
            return new Table(amenityBlocks, enabled, facing);
        }
    }
}