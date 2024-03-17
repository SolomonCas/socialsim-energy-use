package com.socialsim.model.core.environment.patchobject;

import com.socialsim.controller.graphics.Graphic;
import com.socialsim.controller.graphics.amenity.AmenityGraphicLocation;

public interface Drawable {

    Graphic getGraphicObject();
    AmenityGraphicLocation getGraphicLocation();

}