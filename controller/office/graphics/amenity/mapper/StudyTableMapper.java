package com.socialsim.controller.office.graphics.amenity.mapper;

import com.socialsim.controller.Main;
import com.socialsim.controller.generic.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.generic.Patch;
import com.socialsim.model.core.environment.generic.patchobject.Amenity;
import com.socialsim.model.core.environment.office.patchobject.passable.goal.StudyTable;

import java.util.ArrayList;
import java.util.List;

public class StudyTableMapper extends AmenityMapper {

    public static void draw(List<Patch> patches, String facing) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = StudyTable.StudyTableBlock.studyTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, true, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);
            patch.setPatchField(null);

            if(facing.equals("UP") || facing.equals("DOWN")) {
                Patch rightPatch = Main.officeSimulator.getOffice().getPatch(origPatchRow, origPatchCol + 1);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(rightPatch, true, true);
                amenityBlocks.add(amenityBlock2);
                rightPatch.setAmenityBlock(amenityBlock2);
                rightPatch.setPatchField(null);

                Patch rightPatch2 = Main.officeSimulator.getOffice().getPatch(origPatchRow, origPatchCol + 2);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(rightPatch2, true, true);
                amenityBlocks.add(amenityBlock3);
                rightPatch2.setAmenityBlock(amenityBlock3);
                rightPatch2.setPatchField(null);
            }
            else {
                Patch lowerPatch = Main.officeSimulator.getOffice().getPatch(origPatchRow + 1, origPatchCol);
                Amenity.AmenityBlock amenityBlock2 = amenityBlockFactory.create(lowerPatch, true, true);
                amenityBlocks.add(amenityBlock2);
                lowerPatch.setAmenityBlock(amenityBlock2);
                lowerPatch.setPatchField(null);

                Patch lowerPatch2 = Main.officeSimulator.getOffice().getPatch(origPatchRow + 1, origPatchCol + 2);
                Amenity.AmenityBlock amenityBlock3 = amenityBlockFactory.create(lowerPatch2, true, true);
                amenityBlocks.add(amenityBlock3);
                lowerPatch2.setAmenityBlock(amenityBlock3);
                lowerPatch2.setPatchField(null);
            }

            StudyTable studyTableToAdd = StudyTable.StudyTableFactory.create(amenityBlocks, true, facing);
            Main.officeSimulator.getOffice().getStudyTables().add(studyTableToAdd);
            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}