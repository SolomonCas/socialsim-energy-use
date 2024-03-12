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

    public static final double PATCH_SIZE_IN_SQUARE_METERS = 1.0;
    private final MatrixPosition matrixPosition;
    private final Coordinates patchCenterCoordinates;
    private CopyOnWriteArrayList<Agent> agent;
    private Amenity.AmenityBlock amenityBlock; // Denotes the amenity block present on this patch
    private Pair<PatchField, Integer> patchField;
    private Pair<QueueingPatchField, Integer> queueingPatchField;
    private final Environment environment;
    private final List<MatrixPosition> neighborIndices;
    private final List<MatrixPosition> neighbor7x7Indices; // Denotes the positions of the neighbors of this patch within a 7x7 range
    private final List<MatrixPosition> neighbor3x3Indices; // Denotes the positions of the neighbors of this patch within a 7x7 range
    private int amenityBlocksAround; // Denotes the number of amenity blocks around this patch
    private int wallsAround; // Denotes the number of amenity blocks around this patch
    private int team;

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
        this.wallsAround = 0;
        this.team = -1;
    }

    private List<MatrixPosition> computeNeighboringPatches() {
        return null;
    }

    private List<MatrixPosition> compute7x7Neighbors() { // Field of vision
        return null;
    }

    public MatrixPosition getMatrixPosition() {
        return matrixPosition;
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
}