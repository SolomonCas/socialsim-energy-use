package com.socialsim.model.core.agent;

import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;

import java.util.ArrayList;

public class RoutePlan {

    /***** VARIABLES *****/
    private State currentState;
    private ArrayList<State> routePlan;
    private boolean fromBathPM, fromBathAM, isAtDesk;
    private int lastDuration = -1;
    private int canUrgent = 2;
    private long collaborationEnd = 0, meetingStart = -1, meetingEnd, meetingRoom;

    private int BATH_AM = 2, BATH_PM = 2, BATH_LUNCH = 1;
    private int PRINT_BUSINESS = 5, PRINT_RESEARCH = 2;
    private int TECHNICAL_PRINTER_COUNT = 0, TECHNICAL_CUBICLE_COUNT = 0;
    private int COLLABORATE_COUNT = 0, BREAK_COUNT = 0;
    private int DISPENSER_LUNCH = 1, DISPENSER_PM = 1;
    private int REFRIGERATOR_LUNCH = 1, REFRIGERATOR_PM = 1;

//    private Cubicle agentCubicle;

    private Amenity.AmenityBlock lunchAttractor;
    private Amenity lunchAmenity;

    State LUNCH_INSTANCE = null;

    public static final double DIRECTOR_LUNCH = 0.7;
    public static final double INT_LUNCH = 0.3;
    public static final double EXT_LUNCH = 1.0;
    public static final double STRICT_FACULTY_COOPERATE = 0.6;
    public static final double APP_FACULTY_COOPERATE = 0.9;
    public static final double INT_STUDENT_COOPERATE = 0.6;
    public static final double EXT_STUDENT_COOPERATE = 0.9;
    public static final double  BATH_CHANCE = 0.15,
                                DISPENSER_CHANCE = 0.1,
                                REFRIGERATOR_CHANCE = 0.3,
                                BREAK_CHANCE = 0.1;
    public static ArrayList<ArrayList<Long>> meetingTimes = new ArrayList<>();

    /***** CONSTRUCTOR *****/

    public RoutePlan(Agent agent, Environment environment, Patch spawnPatch, int tickEntered, int team, Cubicle assignedCubicle) {

    }

    /***** METHODS *****/

    /***** GETTERS *****/

    /***** SETTERS *****/

}
