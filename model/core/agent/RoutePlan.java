package com.socialsim.model.core.agent;

import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;
import com.socialsim.model.simulator.Simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

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
        this.routePlan = new ArrayList<>();
        ArrayList<Action> actions;

        if(meetingTimes.size() == 0){
            long start = 0, end = 0;

            start = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(600 - 300 + 1) + 300;
            end = (Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) + 720) + start;

            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,1L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,2L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,3L)));

            start = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(2690 - 2550 + 1) + 2550;
            end = (Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) + 720) + start;

            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,1L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,2L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,3L)));

            start = end + 60;
            end = (Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) + 720) + start;

            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,1L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,2L)));
            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,3L)));

            Collections.shuffle(meetingTimes, new Random());
        }

        if(team > 0){
            meetingStart = meetingTimes.get(team-1).get(0);
            meetingEnd = meetingTimes.get(team-1).get(1);
            meetingRoom = meetingTimes.get(team-1).get(2);
        }

        if (agent.getPersona() == Agent.Persona.GUARD) {
            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GUARD_STAY_PUT, spawnPatch, 5760));
            routePlan.add(new State(State.Name.GUARD, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.JANITOR) {
            actions = new ArrayList<>();
            Patch randomToilet = environment.getToilets().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(3)).getAmenityBlocks().get(0).getPatch();
            actions.add(new Action(Action.Name.JANITOR_CLEAN_TOILET, randomToilet, 10));
            routePlan.add(new State(State.Name.MAINTENANCE_BATHROOM, this, agent, actions));
            actions = new ArrayList<>();
            Patch randomPlant = environment.getPlants().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(9)).getAmenityBlocks().get(0).getPatch();
            actions.add(new Action(Action.Name.JANITOR_WATER_PLANT, randomPlant, 10));
            routePlan.add(new State(State.Name.MAINTENANCE_PLANT, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.CLIENT) {
//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
//            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
//            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION,
//                    environment.getReceptionTables().get(0).getAttractors().get(0).getPatch()));
//            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
//            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.CLIENT_GO_RECEPTIONIST, environment.getReceptionTables().get(0).getAmenityBlocks().get(2).getPatch(), 12, 24));
            actions.add(new Action(Action.Name.CLIENT_GO_COUCH, 60, 180));
            actions.add(new Action(Action.Name.CLIENT_GO_OFFICE, environment.getChairs().get(1).getAmenityBlocks().get(0).getPatch(),360, 720));
            actions.add(new Action(Action.Name.CLIENT_GO_RECEPTIONIST, environment.getReceptionTables().get(0).getAmenityBlocks().get(2).getPatch(), 12, 24));
            routePlan.add(new State(State.Name.CLIENT, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.DRIVER) {
            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.DRIVER_GO_RECEPTIONIST, environment.getReceptionTables().get(0).getAmenityBlocks().get(2).getPatch(), 12, 24));
            actions.add(new Action(Action.Name.DRIVER_GO_COUCH, 60, 180));
            actions.add(new Action(Action.Name.DRIVER_GO_RECEPTIONIST, environment.getReceptionTables().get(0).getAmenityBlocks().get(2).getPatch(), 12, 24));
            routePlan.add(new State(State.Name.DRIVER, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.VISITOR) {
            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.VISITOR_GO_RECEPTIONIST, environment.getReceptionTables().get(0).getAmenityBlocks().get(2).getPatch(), 12, 24));
            actions.add(new Action(Action.Name.VISITOR_GO_OFFICE, environment.getChairs().get(5).getAmenityBlocks().get(0).getPatch(),360, 2160));
            routePlan.add(new State(State.Name.VISITOR, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.RECEPTIONIST) {
            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.RECEPTIONIST_STAY_PUT, environment.getChairs().get(2).getAmenityBlocks().get(0).getPatch(), 5760));
            routePlan.add(new State(State.Name.RECEPTIONIST, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.SECRETARY) {
            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_OFFICE_ROOM, environment.getChairs().get(3).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.SECRETARY_STAY_PUT, environment.getChairs().get(3).getAttractors().get(0).getPatch(), 360, 720));
            actions.add(new Action(Action.Name.SECRETARY_CHECK_CABINET, 12, 36));
            routePlan.add(new State(State.Name.SECRETARY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, environment.getChairs().get(3).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            actions.add(new Action(Action.Name.EXIT_LUNCH, environment.getDoors().get(1).getAttractors().get(1).getPatch()));
            this.LUNCH_INSTANCE = routePlan.get(routePlan.size()-1);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_OFFICE_ROOM, environment.getChairs().get(3).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.SECRETARY_STAY_PUT, environment.getChairs().get(3).getAttractors().get(0).getPatch(), 360, 720));
            actions.add(new Action(Action.Name.SECRETARY_CHECK_CABINET, 12, 36));
            routePlan.add(new State(State.Name.SECRETARY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.PROFESSIONAL_BOSS || agent.getPersona() == Agent.Persona.APPROACHABLE_BOSS) {
            setFromBathAM(false);
            setFromBathPM(false);
            setAtDesk(false);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_OFFICE_ROOM, environment.
                    getChairs().get(4).getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, environment.
                    getChairs().get(4).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_OFFICE_ROOM, environment.
                    getChairs().get(4).getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_BUSINESS || agent.getPersona() == Agent.Persona.EXT_BUSINESS ||
                agent.getPersona() == Agent.Persona.INT_RESEARCHER || agent.getPersona() == Agent.Persona.EXT_RESEARCHER) {
            setFromBathAM(false);
            setFromBathPM(false);
            setCOLLABORATE_COUNT(2);
            setAtDesk(false);
            setAgentCubicle(assignedCubicle);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedCubicle.getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));
            this.LUNCH_INSTANCE = routePlan.get(routePlan.size()-1);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch(),120,360));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_TECHNICAL || agent.getPersona() == Agent.Persona.EXT_TECHNICAL) {
            setFromBathAM(false);
            setFromBathPM(false);
            setTECHNICAL_PRINTER_COUNT(-1);
            setTECHNICAL_CUBICLE_COUNT(-1);
            setAtDesk(false);
            setAgentCubicle(assignedCubicle);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedCubicle.getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));
            this.LUNCH_INSTANCE = routePlan.get(routePlan.size()-1);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch(),120,360));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.MANAGER) {
            setFromBathAM(false);
            setFromBathPM(false);
            setCOLLABORATE_COUNT(2);
            setAtDesk(false);
            setAgentCubicle(assignedCubicle);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_SECURITY_QUEUE));
            actions.add(new Action(Action.Name.GO_THROUGH_SCANNER, 2));
            routePlan.add(new State(State.Name.GOING_TO_SECURITY, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedCubicle.getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            actions.add(new Action(Action.Name.EXIT_LUNCH, environment.getDoors().get(1).getAttractors().get(1).getPatch()));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));
            this.LUNCH_INSTANCE = routePlan.get(routePlan.size()-1);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getOfficeGates().get(0).getAmenityBlocks().get(0).getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedCubicle.getAttractors().get(0).getPatch(),120,360));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }

        setNextState(-1);
    }

    /***** METHODS *****/

    /***** GETTERS *****/

    /***** SETTERS *****/

}
