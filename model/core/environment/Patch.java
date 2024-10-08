package com.socialsim.model.core.environment;

import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.patchfield.PatchField;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.core.environment.position.MatrixPosition;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.patchfield.QueueingPatchField;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.util.Pair;


public class Patch extends BaseObject implements Comparable<Patch> {

    // VARIABLES
    public static final double PATCH_SIZE_IN_SQUARE_METERS = 1.0;
    public static final int MAX_NUM_PATCH_FOR_BIG_ROOM = 1122;
    private final MatrixPosition matrixPosition;
    private final Coordinates patchCenterCoordinates;
    private CopyOnWriteArrayList<Agent> agent;
    private Amenity.AmenityBlock amenityBlock; // Denotes the amenity block present on this patch
    private Pair<PatchField, String> patchField;
    private Pair<QueueingPatchField, String> queueingPatchField;
    private final Environment environment;
    private final List<MatrixPosition> neighborIndices;
    private final List<MatrixPosition> neighbor7x7Indices; // Denotes the positions of the neighbors of this patch within a 7x7 range
    private final List<MatrixPosition> neighbor3x3Indices; // Denotes the positions of the neighbors of this patch within a 7x7 range
    private int amenityBlocksAround; // Denotes the number of amenity blocks around this patch
    private int dividersAround; // Denotes the number of amenity blocks around this patch
    private int team;
    // To indicate if the patch is either in a big room or not
    private boolean isRoomBig;




    // CONSTRUCTOR
    public Patch(Environment environment, MatrixPosition matrixPosition) {
        super();

        this.matrixPosition = matrixPosition;
        this.patchCenterCoordinates = Coordinates.getPatchCenterCoordinates(this);
        this.agent = new CopyOnWriteArrayList<>();
        this.amenityBlock = null;
        this.patchField = null;
        this.queueingPatchField = null;
        this.environment = environment;
        this.neighborIndices = this.computeNeighboringPatches();
        this.neighbor7x7Indices = this.compute7x7Neighbors();
        this.neighbor3x3Indices = this.compute7x7Neighbors();
        this.amenityBlocksAround = 0;
        this.dividersAround = 0;
        this.team = -1;
        this.isRoomBig = false;

    }




    // METHODS: PATCHES
    private List<MatrixPosition> computeNeighboringPatches() {
        int patchRow = this.matrixPosition.getRow();
        int patchColumn = this.matrixPosition.getColumn();

        List<MatrixPosition> neighboringPatchIndices = new ArrayList<>();

        if (patchRow - 1 >= 0 && patchColumn - 1 >= 0) { // Top-left of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow - 1, patchColumn - 1));
        }

        if (patchRow - 1 >= 0) { // Top of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow - 1, patchColumn));
        }

        if (patchRow - 1 >= 0 && patchColumn + 1 < this.getEnvironment().getColumns()) { // Top-right of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow - 1, patchColumn + 1));
        }

        if (patchColumn - 1 >= 0) { // Left of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow, patchColumn - 1));
        }

        if (patchColumn + 1 < this.getEnvironment().getColumns()) { // Right of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow, patchColumn + 1));
        }

        if (patchRow + 1 < this.getEnvironment().getRows() && patchColumn - 1 >= 0) { // Bottom-left of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow + 1, patchColumn - 1));
        }

        if (patchRow + 1 < this.getEnvironment().getRows()) { // Bottom of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow + 1, patchColumn));
        }

        if (patchRow + 1 < this.getEnvironment().getRows() && patchColumn + 1 < this.getEnvironment().getColumns()) { // Bottom-right of patch
            neighboringPatchIndices.add(new MatrixPosition(patchRow + 1, patchColumn + 1));
        }

        return neighboringPatchIndices;
    }

    private List<MatrixPosition> compute7x7Neighbors() { // Field of vision
        int patchRow = this.matrixPosition.getRow();
        int patchColumn = this.matrixPosition.getColumn();

        int truncatedX = (int) (this.getPatchCenterCoordinates().getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int truncatedY = (int) (this.getPatchCenterCoordinates().getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS);

        List<MatrixPosition> patchIndicesToExplore = new ArrayList<>();

        for (int rowOffset = -3; rowOffset <= 3; rowOffset++) {
            for (int columnOffset = -3; columnOffset <= 3; columnOffset++) {
                boolean xCondition;
                boolean yCondition;

                if (rowOffset < 0) { // Separate upper and lower rows
                    yCondition = truncatedY + rowOffset > 0;
                }
                else if (rowOffset > 0) {
                    yCondition = truncatedY + rowOffset < environment.getRows();
                }
                else {
                    yCondition = true;
                }

                if (columnOffset < 0) { // Separate left and right columns
                    xCondition = truncatedX + columnOffset > 0;
                }
                else if (columnOffset > 0) {
                    xCondition = truncatedX + columnOffset < environment.getColumns();
                }
                else {
                    xCondition = true;
                }

                if (xCondition && yCondition) { // Insert the patch to the list of patches to be explored if the patches are within the bounds of the floor
                    patchIndicesToExplore.add(new MatrixPosition(patchRow + rowOffset, patchColumn + columnOffset));
                }
            }
        }

        return patchIndicesToExplore;
    }

    public List<Patch> getNeighbors() {
        List<Patch> neighboringPatches = new ArrayList<>();

        for (MatrixPosition neighboringPatchIndex : this.neighborIndices) {
            Patch patch = this.getEnvironment().getPatch(neighboringPatchIndex.getRow(), neighboringPatchIndex.getColumn());

            if (patch != null) {
                neighboringPatches.add(patch);
            }
        }

        return neighboringPatches;
    }

    public List<Patch> get7x7Neighbors(boolean includeCenterPatch) {
        List<Patch> neighboringPatches = new ArrayList<>();

        for (MatrixPosition neighboringPatchIndex : this.neighbor7x7Indices) {
            Patch patch = this.getEnvironment().getPatch(neighboringPatchIndex.getRow(), neighboringPatchIndex.getColumn());

            if (patch != null) {
                if (!includeCenterPatch || !patch.equals(this)) {
                    neighboringPatches.add(patch);
                }
            }
        }

        return neighboringPatches;
    }

    public List<Patch> get3x3Neighbors(boolean includeCenterPatch) {
        List<Patch> neighboringPatches = new ArrayList<>();

        for (MatrixPosition neighboringPatchIndex : this.neighbor3x3Indices) {
            Patch patch = this.getEnvironment().getPatch(neighboringPatchIndex.getRow(), neighboringPatchIndex.getColumn());

            if (patch != null) {
                if (!includeCenterPatch || !patch.equals(this)) {
                    neighboringPatches.add(patch);
                }
            }
        }

        return neighboringPatches;
    }

    public void signalAddDivider() { // Signal to this patch and to its neighbors that an amenity block was added here
        this.incrementDividersAround();

        for (Patch neighbor : this.getNeighbors()) {
            neighbor.incrementDividersAround();
        }
    }

    private void incrementDividersAround() {
        this.dividersAround++;
    }





    // METHODS: AMENITIES
    private void incrementAmenityBlocksAround() {
        this.amenityBlocksAround++;
    }

    public void signalAddAmenityBlock() { // Signal to this patch and to its neighbors that an amenity block was added here
        this.incrementAmenityBlocksAround();

        for (Patch neighbor : this.getNeighbors()) {
            neighbor.incrementAmenityBlocksAround();
        }
    }





    // METHODS: AGENTS
    public void addAgent(Agent newAgent) {
        getAgents().add(newAgent);
    }

    public void removeAgent(Agent newAgent) {
        getAgents().remove(newAgent);
    }







    // GETTERS
    public Environment getEnvironment() {
        return environment;
    }
    public MatrixPosition getMatrixPosition() {
        return matrixPosition;
    }

    public Coordinates getPatchCenterCoordinates() {
        return patchCenterCoordinates;
    }
    public CopyOnWriteArrayList<Agent> getAgents() {
        return agent;
    }
    public Pair<PatchField, String> getPatchField() {
        return patchField;
    }
    public Pair<QueueingPatchField, String> getQueueingPatchField() {
        return queueingPatchField;
    }
    public int getDividersAround() {
        return dividersAround;
    }
    public Amenity.AmenityBlock getAmenityBlock() {
        return amenityBlock;
    }
    public int getAmenityBlocksAround() {
        return amenityBlocksAround;
    }
    public int getTeam() {
        return team;
    }
    public boolean isRoomBig() {
        return isRoomBig;
    }







    // SETTERS
    public void setPatchField(Pair<PatchField, String> patchField) {
        this.patchField = patchField;
    }
    public void setQueueingPatchField(Pair<QueueingPatchField, String> queueingPatchField) {
        this.queueingPatchField = queueingPatchField;
    }
    public void setDividersAround(int dividersAround) {
        this.dividersAround = dividersAround;
    }

    public void setAmenityBlock(Amenity.AmenityBlock amenityBlock) {
        this.amenityBlock = amenityBlock;
    }
    public void setTeam(int team) {
        this.team = team;
    }

    public void setRoomBig(boolean roomBig) {
        isRoomBig = roomBig;
    }

    // OVERRIDE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patch patch = (Patch) o;

        return matrixPosition.equals(patch.matrixPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matrixPosition);
    }

    @Override
    public int compareTo(Patch patch) {
        int thisRow = this.getMatrixPosition().getRow();
        int patchRow = patch.getMatrixPosition().getRow();

        int thisColumn = this.getMatrixPosition().getColumn();
        int patchColumn = patch.getMatrixPosition().getColumn();

        if (thisRow > patchRow) {
            return 1;
        }
        else if (thisRow == patchRow) {
            return Integer.compare(thisColumn, patchColumn);
        }
        else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "[" + this.getMatrixPosition().getRow() + ", " + this.getMatrixPosition().getColumn() + "]";
    }

    public static class PatchPair {
        private final Patch patch1;
        private final Patch patch2;

        public PatchPair(Patch patch1, Patch patch2) {
            this.patch1 = patch1;
            this.patch2 = patch2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PatchPair patchPair = (PatchPair) o;
            return patch1.equals(patchPair.patch1) && patch2.equals(patchPair.patch2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(patch1, patch2);
        }

        @Override
        public String toString() {
            return "(" + patch1 + ", " + patch2 + ")";
        }
    }








    public static class Offset { // Denotes the offset of a specific offset of an object in terms of its matrix position
        private final MatrixPosition offset;

        public Offset(int rowOffset, int columnOffset) {
            this.offset = new MatrixPosition(rowOffset, columnOffset);
        }

        public int getRowOffset() {
            return this.offset.getRow();
        }

        public int getColumnOffset() {
            return this.offset.getColumn();
        }

        public static Offset getOffsetFromPatch(Patch patch, Patch reference) {
            int rowOffset = patch.getMatrixPosition().getRow() - reference.getMatrixPosition().getRow();
            int columnOffset = patch.getMatrixPosition().getColumn() - reference.getMatrixPosition().getColumn();

            return new Offset(rowOffset, columnOffset);
        }

        public static Patch getPatchFromOffset(Environment environment, Patch reference, Offset offset) {
            int newRow = reference.getMatrixPosition().getRow() + offset.getRowOffset();
            int newColumn = reference.getMatrixPosition().getColumn() + offset.getColumnOffset();

            if (newRow >= 0 && newRow < environment.getRows() && newColumn >= 0 && newColumn < environment.getColumns()) {
                Patch patch = environment.getPatch(newRow, newColumn);

                if (patch.getAmenityBlock() == null) {
                    return patch;
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
    }
}