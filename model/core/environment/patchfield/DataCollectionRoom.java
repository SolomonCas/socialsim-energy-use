package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.List;

public class DataCollectionRoom extends PatchField {

    // VARIABLES

    public List<Patch> area;
    public String variation;
    private boolean isRoomBig;

    public static DataCollectionRoomFactory dataCollectionRoomFactory;

    static {
        dataCollectionRoomFactory = new DataCollectionRoomFactory();
    }


    // CONSTRUCTOR

    protected DataCollectionRoom(List<Patch> patches, String str) {
        super(patches);

        this.area = patches;
        this.variation = str;

        this.isRoomBig = patches.size() >= Patch.MAX_NUM_PATCH_FOR_BIG_ROOM;

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
            patch.setRoomBig(this.isRoomBig);
        }
    }

    /* Getter & Setter */

    public List<Patch> getArea() {
        return area;
    }

    public void setArea(List<Patch> patches) {
        area = patches;
    }
    public String getVariation() {
        return variation;
    }

    public void setVariation(String str) {
        variation = str;
    }

    // OVERRIDE
    @Override
    public String toString() {
        return "Data Collection";
    }


    // INNER CLASS
    public static class DataCollectionRoomFactory extends PatchFieldFactory {
        public DataCollectionRoom create(List<Patch> patches, String str) {
            return new DataCollectionRoom(patches, str);
        }
    }

}