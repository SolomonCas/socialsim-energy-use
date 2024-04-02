package com.socialsim.controller.graphics;

public abstract class GraphicLocation {

    // VARIABLES
    protected final int graphicRow;
    protected final int graphicColumn;
    protected int graphicWidth;
    protected int graphicHeight;




    // CONSTRUCTOR
    public GraphicLocation(int graphicRow, int graphicColumn) {
        this.graphicRow = graphicRow;
        this.graphicColumn = graphicColumn;
    }



    // METHODS
    public abstract int getSourceY();

    public abstract int getSourceX();

    public abstract int getSourceWidth();

    public abstract int getSourceHeight();




    // GETTERS
    public int getGraphicRow() {
        return graphicRow;
    }

    public int getGraphicColumn() {
        return graphicColumn;
    }




    // SETTERS
    public void setGraphicWidth(int graphicWidth) {
        this.graphicWidth = graphicWidth;
    }

    public void setGraphicHeight(int graphicHeight) {
        this.graphicHeight = graphicHeight;
    }


}