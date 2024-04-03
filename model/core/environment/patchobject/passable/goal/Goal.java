package com.socialsim.model.core.environment.patchobject.passable.goal;

import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.Drawable;
import com.socialsim.model.core.environment.patchobject.passable.NonObstacle;

import java.util.List;

public abstract class Goal extends NonObstacle implements Drawable {


    /***** CONSTRUCTOR *****/
    protected Goal(List<AmenityBlock> amenityBlocks, boolean enabled) {
        super(amenityBlocks, enabled);
    }



    /***** METHODS *****/
    public static boolean isGoal(Amenity amenity) {
        return amenity instanceof Goal;
    }

    public static Goal toGoal(Amenity amenity) {
        if (isGoal(amenity)) {
            return (Goal) amenity;
        }
        else {
            return null;
        }
    }


    /***** INNER STATIC CLASS *****/
    public static abstract class GoalFactory extends AmenityFactory {
    }

}