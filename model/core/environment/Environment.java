package com.socialsim.model.core.environment;

import com.socialsim.model.core.environment.position.MatrixPosition;

import java.io.Serializable;

public abstract class Environment extends BaseObject implements Serializable {

    // VARIABLES
    private final int rows;
    private final int columns;
    private final Patch[][] patches;


    // CONSTRUCTOR(S)
    public Environment(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.patches = new Patch[rows][columns];
        initializePatches();
    }



    // METHODS
    private void initializePatches() {
        MatrixPosition matrixPosition;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                matrixPosition = new MatrixPosition(row, column);
                patches[row][column] = new Patch(this, matrixPosition);
            }
        }
    }

    public void convertIOSToChances() {
        // Insert code
    }

    public static void configureDefaultIOS() {
        // Insert code
    }

    public static void configureDefaultInteractionTypeChances() {
        // Insert code
    }

    public void copyDefaultToIOS() {
        // Insert code
    }

    public void copyDefaultToInteractionTypeChances(){
        // Insert code
    }



    // SETTERS
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }









    public static class Factory extends BaseObject.ObjectFactory {
        public static Environment create(int rows, int columns) {
            return new Environment(rows, columns) {
            };
        }
    }
}