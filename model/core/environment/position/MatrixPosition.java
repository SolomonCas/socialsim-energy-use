package com.socialsim.model.core.environment.position;

public class MatrixPosition extends Location {

    private final int row;
    private final int column;

    public MatrixPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}