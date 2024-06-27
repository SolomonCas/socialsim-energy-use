package com.socialsim.controller.graphics.amenity.mapper;


import com.socialsim.controller.Main;
import com.socialsim.controller.graphics.amenity.AmenityMapper;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.MeetingTable;
import com.socialsim.model.core.environment.patchobject.passable.goal.PantryTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeetingTableMapper extends AmenityMapper {

    /***** METHOD *****/
    public static void draw(List<Patch> patches, String orientation, String size, String position) {
        for (Patch patch : patches) {
            List<Amenity.AmenityBlock> amenityBlocks = new ArrayList<>();
            int origPatchRow = patch.getMatrixPosition().getRow();
            int origPatchCol = patch.getMatrixPosition().getColumn();

            // TABLE'S FIRST PATCH (UPPER LEFT CORNER)
            Amenity.AmenityBlock.AmenityBlockFactory amenityBlockFactory = MeetingTable.MeetingTableBlock.meetingTableBlockFactory;
            Amenity.AmenityBlock amenityBlock = amenityBlockFactory.create(patch, false, true);
            amenityBlocks.add(amenityBlock);
            patch.setAmenityBlock(amenityBlock);

            MeetingTable tableToAdd;
            List<MeetingTable> meetingTables = Main.simulator.getEnvironment().getMeetingTables();
            int index = 0;

            if (orientation.equals("VERTICAL")) {
                if (size.equals("LARGE")) {
                    for (int i = 0; i <= 8; i++) {
                        for (int j = 0; j <= 2; j++) {
                            if (i == 0) {
                                j++;
                            }
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }

                    tableToAdd = MeetingTable.MeetingTableFactory.create(amenityBlocks, true, orientation, size);
                    meetingTables.add(tableToAdd);
                    index = meetingTables.indexOf(tableToAdd);


                    // SOUTH
                    List<Patch> southMeetingChairLVPatches = new ArrayList<>();
                    southMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 1));
                    MeetingChairMapper.draw(southMeetingChairLVPatches, index, "SOUTH", "OFFICE");

                    // NORTH
                    List<Patch> northMeetingChairLVPatches = new ArrayList<>();
                    northMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 9, origPatchCol + 1));
                    MeetingChairMapper.draw(northMeetingChairLVPatches, index, "NORTH", "OFFICE");

                    // EAST
                    List<Patch> eastMeetingChairLVPatches = new ArrayList<>();
                    eastMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                    eastMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol - 1));
                    eastMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 5, origPatchCol - 1));
                    eastMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 7, origPatchCol - 1));
                    MeetingChairMapper.draw(eastMeetingChairLVPatches, index, "EAST", "OFFICE");

                    // WEST
                    List<Patch> westMeetingChairLVPatches = new ArrayList<>();
                    westMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 3));
                    westMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 3));
                    westMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 5, origPatchCol + 3));
                    westMeetingChairLVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 7, origPatchCol + 3));
                    MeetingChairMapper.draw(westMeetingChairLVPatches, index, "WEST", "OFFICE");
                }
                else if (size.equals("SMALL")) {
                    for (int i = 1; i <= 6; i++) {
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol);
                            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    tableToAdd = MeetingTable.MeetingTableFactory.create(amenityBlocks, true, orientation, size);
                    meetingTables.add(tableToAdd);
                    index = meetingTables.indexOf(tableToAdd);


                    // SOUTH
                    List<Patch> southMeetingChairSVPatches = new ArrayList<>();
                    southMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                    MeetingChairMapper.draw(southMeetingChairSVPatches, index, "SOUTH", "OFFICE");

                    // NORTH
                    List<Patch> northMeetingChairSVPatches = new ArrayList<>();
                    northMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 7, origPatchCol));
                    MeetingChairMapper.draw(northMeetingChairSVPatches, index, "NORTH", "OFFICE");

                    // EAST
                    List<Patch> eastMeetingChairSVPatches = new ArrayList<>();
                    eastMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol - 1));
                    eastMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol - 1));
                    eastMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 4, origPatchCol - 1));
                    eastMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol - 1));
                    MeetingChairMapper.draw(eastMeetingChairSVPatches, index, "EAST", "OFFICE");

                    // WEST
                    List<Patch> westMeetingChairSVPatches = new ArrayList<>();
                    westMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 1));
                    westMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 2, origPatchCol + 1));
                    westMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 4, origPatchCol + 1));
                    westMeetingChairSVPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 6, origPatchCol + 1));
                    MeetingChairMapper.draw(westMeetingChairSVPatches, index, "WEST", "OFFICE");
                }
            }

            else if (orientation.equals("HORIZONTAL")) {
                if (size.equals("LARGE")) {
                    for (int i = 0; i <= 2; i++) {
                        for (int j = 0; j <= 8; j++) {
                            if (i == 0) {
                                j++;
                            }
                            Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow + i, origPatchCol + j);
                            Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                            amenityBlocks.add(nextAmenityBlock);
                            nextPatch.setAmenityBlock(nextAmenityBlock);
                        }
                    }

                    tableToAdd = MeetingTable.MeetingTableFactory.create(amenityBlocks, true, orientation, size);
                    meetingTables.add(tableToAdd);
                    index = meetingTables.indexOf(tableToAdd);


                    // SOUTH
                    List<Patch> southMeetingChairLHPatches = new ArrayList<>();
                    southMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 1));
                    southMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 3));
                    southMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 5));
                    southMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 7));
                    MeetingChairMapper.draw(southMeetingChairLHPatches, index, "SOUTH", "OFFICE");

                    // NORTH
                    List<Patch> northMeetingChairLHPatches = new ArrayList<>();
                    northMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 1));
                    northMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 3));
                    northMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 5));
                    northMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 3, origPatchCol + 7));
                    MeetingChairMapper.draw(northMeetingChairLHPatches, index, "NORTH", "OFFICE");

                    // EAST
                    List<Patch> eastMeetingChairLHPatches = new ArrayList<>();
                    eastMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                    if (position.equals("LEFT")) {
                        MeetingChairMapper.draw(eastMeetingChairLHPatches, index, "EAST", "OFFICE");
                    } else {
                        eastMeetingChairLHPatches.clear();
                    }

                    // WEST
                    List<Patch> westMeetingChairLHPatches = new ArrayList<>();
                    westMeetingChairLHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol + 9));
                    if (position.equals("RIGHT")) {
                        MeetingChairMapper.draw(westMeetingChairLHPatches, index, "WEST", "OFFICE");
                    } else {
                        westMeetingChairLHPatches.clear();
                    }

                }
                else if (size.equals("SMALL")) {
                    for (int j = 1; j <= 6; j++) {
                        Patch nextPatch = Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + j);
                        Amenity.AmenityBlock nextAmenityBlock = amenityBlockFactory.create(nextPatch, false, false);
                        amenityBlocks.add(nextAmenityBlock);
                        nextPatch.setAmenityBlock(nextAmenityBlock);
                    }

                    tableToAdd = MeetingTable.MeetingTableFactory.create(amenityBlocks, true, orientation, size);
                    meetingTables.add(tableToAdd);
                    index = meetingTables.indexOf(tableToAdd);


                    // SOUTH
                    List<Patch> southMeetingChairSHPatches = new ArrayList<>();
                    southMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol - 1));
                    MeetingChairMapper.draw(southMeetingChairSHPatches, index, "SOUTH", "OFFICE");

                    // NORTH
                    List<Patch> northMeetingChairSHPatches = new ArrayList<>();
                    northMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow, origPatchCol + 7));
                    MeetingChairMapper.draw(northMeetingChairSHPatches, index, "NORTH", "OFFICE");

                    // EAST
                    List<Patch> eastMeetingChairSHPatches = new ArrayList<>();
                    eastMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol));
                    eastMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 2));
                    eastMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 4));
                    eastMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow - 1, origPatchCol + 6));
                    MeetingChairMapper.draw(eastMeetingChairSHPatches, index, "EAST", "OFFICE");

                    // WEST
                    List<Patch> westMeetingChairSHPatches = new ArrayList<>();
                    westMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol));
                    westMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                    westMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                    westMeetingChairSHPatches.add(Main.simulator.getEnvironment().getPatch(origPatchRow + 1, origPatchCol - 1));
                    MeetingChairMapper.draw(westMeetingChairSHPatches, index, "WEST", "OFFICE");
                }
            }

            amenityBlocks.forEach(ab -> ab.getPatch().getEnvironment().getAmenityPatchSet().add(ab.getPatch()));
        }
    }

}