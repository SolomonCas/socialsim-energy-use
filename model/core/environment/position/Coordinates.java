package com.socialsim.model.core.environment.position;

import com.socialsim.model.core.environment.Patch;

public class Coordinates extends Location {

    private double x;
    private double y;

    public Coordinates(Coordinates coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates getPatchCenterCoordinates(Patch patch) {
        double column = patch.getMatrixPosition().getColumn();
        double row = patch.getMatrixPosition().getRow();

        double centeredX = column * Patch.PATCH_SIZE_IN_SQUARE_METERS + Patch.PATCH_SIZE_IN_SQUARE_METERS * 0.5;
        double centeredY = row * Patch.PATCH_SIZE_IN_SQUARE_METERS + Patch.PATCH_SIZE_IN_SQUARE_METERS * 0.5;

        return new Coordinates(centeredX, centeredY);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}