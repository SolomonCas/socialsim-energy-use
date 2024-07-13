package com.socialsim.model.core.environment.patchfield;

import com.socialsim.model.core.environment.Patch;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Divider extends PatchField {


    // VARIABLES

    public List<Patch> area;
    public String variation;


    static {
        dividerFactory = new DividerFactory();
    }


    // CONSTRUCTOR
    protected Divider(List<Patch> patches, String str) {
        super(patches);

        this.area = patches;
        this.variation = str;

        Pair<PatchField, String> pair = new Pair<>(this, str);
        for(Patch patch : patches) {
            patch.setPatchField(pair);
            patch.signalAddDivider();
        }
    }

    public Divider(Divider other) {
        this.area = new ArrayList<>(other.area); // Assuming a shallow copy is sufficient
        this.variation = other.variation;
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

    public static DividerFactory dividerFactory;



    // INNER ABSTRACT CLASS
    public static class DividerFactory extends PatchFieldFactory {
        public Divider create(List<Patch> patches, String str) {
            return new Divider(patches, str);
        }
    }

}