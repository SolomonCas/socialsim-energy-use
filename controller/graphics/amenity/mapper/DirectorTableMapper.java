package com.socialsim.controller.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.DirectorTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DirectorTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            List<Patch> directorChairPatches = new ArrayList<>();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = DirectorTable.DirectorTableBlock.directorTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            if (Objects.equals(facing, "HORIZONTAL")) {
                for (int i = 1; i < 4; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + i);
                    Amenity.AmenityBlock amenityBlockBack =
                            amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }
                directorChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 2));
            }
            else {
                for (int i = 1; i < 4; i++) {
                    Patch patchBack = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                    Amenity.AmenityBlock amenityBlockBack =
                            amenityBlockFactory.create(patchBack, true, true);
                    amenityBlocks.add(amenityBlockBack);
                    patchBack.setAmenityBlock(amenityBlockBack);
                }
                directorChairPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 1));
            }

            DirectorTable directorTableToAdd = DirectorTable.DirectorTableFactory.create(amenityBlocks, true, facing);
            Main.simulator.getEnvironment().getDirectorTables().add(directorTableToAdd);
            int index = Main.simulator.getEnvironment().getDirectorTables().indexOf(directorTableToAdd);
            MeetingChairMapper.draw(directorChairPatches, index);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}