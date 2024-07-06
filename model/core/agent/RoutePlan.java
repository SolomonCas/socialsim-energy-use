package com.socialsim.model.core.agent;

import com.socialsim.controller.Main;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.PatchField;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
import com.socialsim.model.simulator.Simulator;

import java.util.*;

public class RoutePlan {

    /***** VARIABLES *****/
    private State currentState;
    private ArrayList<State> routePlan;
    private boolean bathPM, bathAM, isAtDesk;
    private int lastDuration = -1;

    private long collaborationEnd = 0, meetingStart = -1, meetingEnd, meetingRoom;



    private Chair agentSeat;

    private Amenity.AmenityBlock lunchAttractor;
    private Amenity lunchAmenity;
    private boolean isTakingLunch = false;

    State LUNCH_INSTANCE = null;

    public static final double DIRECTOR_LUNCH = 0.7;
    public static final double EAT_OUTSIDE = 0.9;
    public static final double INT_LUNCH = 0.3;
    public static final double EXT_LUNCH = 1.0;
    public static final double STRICT_FACULTY_COOPERATE = 0.6;
    public static final double APP_FACULTY_COOPERATE = 0.9;
    public static final double INT_STUDENT_COOPERATE = 0.6;
    public static final double EXT_STUDENT_COOPERATE = 0.9;

    /*** Responsible for doing Urgent Task ***/
    private boolean canUrgent = true;
    private int BATH_AM = 2, BATH_PM = 2, BATH_LUNCH = 1;
    private int BREAK_COUNT = 2;
    private int DISPENSER_LUNCH = 1, DISPENSER_PM = 1;
    private int REFRIGERATOR_LUNCH = 1, REFRIGERATOR_PM = 1;
    public static final double  BATH_CHANCE = 0.15,
                                DISPENSER_CHANCE = 0.25,
                                REFRIGERATOR_CHANCE = 0.3,
                                BREAK_CHANCE = 0.5,
                                INQUIRE_FACULTY_CHANCE = 0.3,
                                INQUIRE_STUDENT_CHANCE = 0.3,
                                INQUIRE_MAINTENANCE_CHANCE = 0.3,
                                INQUIRE_GUARD_CHANCE = 0.3,
                                CHECK_TEMP_CHANCE = 0.15;
    public static ArrayList<ArrayList<Long>> meetingTimes = new ArrayList<>();

    /***** CONSTRUCTOR *****/

    public RoutePlan(Agent agent, Environment environment, Patch spawnPatch, int team, Chair assignedSeat) {
        this.routePlan = new ArrayList<>();
        ArrayList<Action> actions;

        if(meetingTimes.isEmpty()){
            long start = 0, end = 0;

            start = /*Simulator.RANDOM_NUMBER_GENERATOR.nextInt(600 - 300 + 1) +*/ 300;
            end = (/*Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) +*/ 720) + start;

            for (int i = 0; i < Main.simulator.getEnvironment().getMeetingRooms().size(); i++) {
                meetingTimes.add(new ArrayList<Long>(Arrays.asList(start, end, (long) i + 1)));
            }

            start = /*Simulator.RANDOM_NUMBER_GENERATOR.nextInt(2690 - 2550 + 1) +*/ 2550;
            end = (/*Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) + */720) + start;

            for (int i = 0; i < Main.simulator.getEnvironment().getMeetingRooms().size(); i++) {
                meetingTimes.add(new ArrayList<Long>(Arrays.asList(start, end, (long) i + 1)));
            }

            start = end + 60;
            end = (/*Simulator.RANDOM_NUMBER_GENERATOR.nextInt(1440 - 720 + 1) + */720) + start;

            for (int i = 0; i < Main.simulator.getEnvironment().getMeetingRooms().size(); i++) {
                meetingTimes.add(new ArrayList<Long>(Arrays.asList(start, end, (long) i + 1)));
            }




//
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,1L)));
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,2L)));
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,3L)));
//

//
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,1L)));
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,2L)));
//            meetingTimes.add(new ArrayList<Long>(Arrays.asList(start,end,3L)));

            Collections.shuffle(meetingTimes, new Random());
        }

        if(team > 0){
            meetingStart = meetingTimes.get(team-1).get(0);
            meetingEnd = meetingTimes.get(team-1).get(1);
            meetingRoom = meetingTimes.get(team-1).get(2);
        }

        if (agent.getPersona() == Agent.Persona.GUARD) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch(), 3));
            routePlan.add(new State(State.Name.GOING_TO_WORK, this, agent, actions));

            actions = new ArrayList<>();
            // Inspect Meeting Room/s
            for(int i = 0; i < environment.getMeetingRooms().size(); i++) {
                maintenanceInspect(environment.getMeetingRooms().get(i), environment, actions);
            }
            // Inspect Human Experience Room/s
            for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
                maintenanceInspect(environment.getHumanExpRooms().get(i), environment, actions);
            }
            // Inspect Data Collection Room/s
            for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
                maintenanceInspect(environment.getDataCollectionRooms().get(i), environment, actions);
            }
            // Inspect Research Centers Room/s
            for(int i = 0; i < environment.getResearchCenters().size(); i++) {
                maintenanceInspect(environment.getResearchCenters().get(i), environment, actions);
            }
            // Inspect Faculty Room/s
            for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
                maintenanceInspect(environment.getFacultyRooms().get(i), environment, actions);
            }

            // Inspect Learning Space Room/s
            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
            }

            // Inspect Pantry Room/s
            for(int i = 0; i < environment.getPantries().size(); i++) {
                maintenanceInspect(environment.getPantries().get(i), environment, actions);
            }
            // Inspect Control Center Room/s
            for(int i = 0; i < environment.getControlCenters().size(); i++) {
                maintenanceInspect(environment.getControlCenters().get(i), environment, actions);
            }
//            // Inspect Data Center Room/s
//            for(int i = 0; i < environment.getDataCenters().size(); i++) {
//                maintenanceInspect(environment.getDataCenters().get(i), environment, actions);
//            }
            // Inspect Solo Room/s
//            for(int i = 0; i < environment.getSoloRooms().size(); i++) {
//                maintenanceInspect(environment.getSoloRooms().get(i), environment, actions);
//            }
            // Inspect Staff Room/s
            for(int i = 0; i < environment.getStaffRooms().size(); i++) {
                maintenanceInspect(environment.getStaffRooms().get(i), environment, actions);
            }

            // Inspect Storage Room/s
            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
            }

            // Inspect Director Room/s
            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
            }

//            routePlan.add(new State(State.Name.INSPECT_ROOMS, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GUARD_STAY_PUT, assignedSeat.getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GUARD, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedSeat.getAttractors().getFirst().getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

//            actions = new ArrayList<>();
//            // Inspect Meeting Room/s
//            for(int i = 0; i < environment.getMeetingRooms().size(); i++) {
//                maintenanceInspect(environment.getMeetingRooms().get(i), environment, actions);
//            }
//            // Inspect Human Experience Room/s
//            for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
//                maintenanceInspect(environment.getHumanExpRooms().get(i), environment, actions);
//            }
//            // Inspect Data Collection Room/s
//            for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
//                maintenanceInspect(environment.getDataCollectionRooms().get(i), environment, actions);
//            }
//            // Inspect Research Centers Room/s
//            for(int i = 0; i < environment.getResearchCenters().size(); i++) {
//                maintenanceInspect(environment.getResearchCenters().get(i), environment, actions);
//            }
//            // Inspect Faculty Room/s
//            for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
//                maintenanceInspect(environment.getFacultyRooms().get(i), environment, actions);
//            }
//
//            // Inspect Learning Space Room/s
//            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
//                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
//            }
//
//            // Inspect Pantry Room/s
//            for(int i = 0; i < environment.getPantries().size(); i++) {
//                maintenanceInspect(environment.getPantries().get(i), environment, actions);
//            }
//            // Inspect Control Center Room/s
//            for(int i = 0; i < environment.getControlCenters().size(); i++) {
//                maintenanceInspect(environment.getControlCenters().get(i), environment, actions);
//            }
//            // Inspect Data Center Room/s
//            for(int i = 0; i < environment.getDataCenters().size(); i++) {
//                maintenanceInspect(environment.getDataCenters().get(i), environment, actions);
//            }
//            // Inspect Solo Room/s
//            for(int i = 0; i < environment.getSoloRooms().size(); i++) {
//                maintenanceInspect(environment.getSoloRooms().get(i), environment, actions);
//            }
//            // Inspect Staff Room/s
//            for(int i = 0; i < environment.getStaffRooms().size(); i++) {
//                maintenanceInspect(environment.getStaffRooms().get(i), environment, actions);
//            }
//
//            // Inspect Storage Room/s
//            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
//                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
//            }
//
//            // Inspect Director Room/s
//            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
//                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
//            }

            actions = new ArrayList<>();
            // Developer Note: The thesis does not need to implement elevator behavior, where agents will wait for an elevator to leave
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
            // This is for when the agent isn't in his station or is a behavior of getting he/she's belongings before going home
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch(), 3));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));

        }
        else if (agent.getPersona() == Agent.Persona.MAINTENANCE) {
            setBathAM(true);
            setBathPM(true);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            // Maintenance put their stuff in the Clinic Room/ Storage Room
            actions.add(new Action(Action.Name.GO_TO_STATION,
                    environment.getStorages().getFirst().getAttractors().getFirst().getPatch(), 5));
            routePlan.add(new State(State.Name.GOING_TO_WORK, this, agent, actions));

            actions = new ArrayList<>();

            // Inspect Meeting Room/s
            for(int i = 0; i < environment.getMeetingRooms().size(); i++) {
                maintenanceInspect(environment.getMeetingRooms().get(i), environment, actions);
            }
            // Inspect Human Experience Room/s
            for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
                maintenanceInspect(environment.getHumanExpRooms().get(i), environment, actions);
            }
            // Inspect Data Collection Room/s
            for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
                maintenanceInspect(environment.getDataCollectionRooms().get(i), environment, actions);
            }
            // Inspect Research Centers Room/s
            for(int i = 0; i < environment.getResearchCenters().size(); i++) {
                maintenanceInspect(environment.getResearchCenters().get(i), environment, actions);
            }
            // Inspect Faculty Room/s
            for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
                maintenanceInspect(environment.getFacultyRooms().get(i), environment, actions);
            }
            // Inspect Storage Room/s
            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
            }
            // Inspect Pantry Room/s
            for(int i = 0; i < environment.getPantries().size(); i++) {
                maintenanceInspect(environment.getPantries().get(i), environment, actions);
            }
            // Inspect Learning Space Room/s
            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
            }
            // Inspect Control Center Room/s
            for(int i = 0; i < environment.getControlCenters().size(); i++) {
                maintenanceInspect(environment.getControlCenters().get(i), environment, actions);
            }
            // Inspect Data Center Room/s
            for(int i = 0; i < environment.getDataCenters().size(); i++) {
                maintenanceInspect(environment.getDataCenters().get(i), environment, actions);
            }
            // Inspect Solo Room/s
            for(int i = 0; i < environment.getSoloRooms().size(); i++) {
                maintenanceInspect(environment.getSoloRooms().get(i), environment, actions);
            }
            // Inspect Staff Room/s
            for(int i = 0; i < environment.getStaffRooms().size(); i++) {
                maintenanceInspect(environment.getStaffRooms().get(i), environment, actions);
            }
            // Inspect Director Room/s
            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
            }
            routePlan.add(new State(State.Name.INSPECT_ROOMS, this, agent, actions));

            actions = new ArrayList<>();
            for (int i = 0; i < environment.getOfficeToilets().size(); i++) {
                actions.add(new Action(Action.Name.MAINTENANCE_CLEAN_TOILET, 10)); // Cleans only OfficeToilet
            }
            routePlan.add(new State(State.Name.MAINTENANCE_BATHROOM, this, agent, actions));

            actions = new ArrayList<>();
            for (int i = 0; i < environment.getOfficeSinks().size(); i++) {
                actions.add(new Action(Action.Name.MAINTENANCE_CLEAN_SINK, 10)); // Cleans only OfficeToilet
            }
            routePlan.add(new State(State.Name.MAINTENANCE_BATHROOM, this, agent, actions));

            actions = new ArrayList<>();
            for(int i = 0; i < environment.getPlants().size(); i++) {
                actions.add(new Action(Action.Name.MAINTENANCE_WATER_PLANT, 10));
            }
            routePlan.add(new State(State.Name.MAINTENANCE_PLANT, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_WAIT_AREA)); // The destination is set on the Simulator.java
            routePlan.add(new State(State.Name.WAIT_FOR_ACTIVITY, this, agent, actions));

            actions = new ArrayList<>();

            // Inspect Meeting Room/s
            for(int i = 0; i < environment.getMeetingRooms().size(); i++) {
                maintenanceInspect(environment.getMeetingRooms().get(i), environment, actions);
            }
            // Inspect Human Experience Room/s
            for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
                maintenanceInspect(environment.getHumanExpRooms().get(i), environment, actions);
            }
            // Inspect Data Collection Room/s
            for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
                maintenanceInspect(environment.getDataCollectionRooms().get(i), environment, actions);
            }
            // Inspect Research Centers Room/s
            for(int i = 0; i < environment.getResearchCenters().size(); i++) {
                maintenanceInspect(environment.getResearchCenters().get(i), environment, actions);
            }
            // Inspect Faculty Room/s
            for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
                maintenanceInspect(environment.getFacultyRooms().get(i), environment, actions);
            }
            // Inspect Storage Room/s
            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
            }
            // Inspect Pantry Room/s
            for(int i = 0; i < environment.getPantries().size(); i++) {
                maintenanceInspect(environment.getPantries().get(i), environment, actions);
            }
            // Inspect Learning Space Room/s
            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
            }
            // Inspect Control Center Room/s
            for(int i = 0; i < environment.getControlCenters().size(); i++) {
                maintenanceInspect(environment.getControlCenters().get(i), environment, actions);
            }
            // Inspect Data Center Room/s
            for(int i = 0; i < environment.getDataCenters().size(); i++) {
                maintenanceInspect(environment.getDataCenters().get(i), environment, actions);
            }
            // Inspect Solo Room/s
            for(int i = 0; i < environment.getSoloRooms().size(); i++) {
                maintenanceInspect(environment.getSoloRooms().get(i), environment, actions);
            }
            // Inspect Staff Room/s
            for(int i = 0; i < environment.getStaffRooms().size(); i++) {
                maintenanceInspect(environment.getStaffRooms().get(i), environment, actions);
            }
            // Inspect Director Room/s
            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
            }
//            routePlan.add(new State(State.Name.INSPECT_ROOMS, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH)); // Maintenance does not have an assigned seat in the model
            actions.add(new Action(Action.Name.EAT_LUNCH, 720));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));

            actions.add(new Action(Action.Name.GO_TO_STATION, environment.getStorages().getFirst().getAttractors().getFirst().getPatch(), 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.DIRECTOR) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedSeat.getAttractors().getFirst().getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.STRICT_FACULTY || agent.getPersona() == Agent.Persona.APP_FACULTY) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
            actions.add(new Action(Action.Name.FILL_UP_NAME, 3));
            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_STUDENT || agent.getPersona() == Agent.Persona.EXT_STUDENT) {
//            setCOLLABORATE_COUNT(2);
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, 3));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            routePlan.add(addUrgentRoute("FIX_VISUAL_COMFORT", agent, environment));

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GO_TO_LUNCH));
//            actions.add(new Action(Action.Name.EAT_LUNCH, 720));
//            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }

        setNextState(-1);
    }

    /***** METHODS *****/
    public void guardInspect(PatchField room, Environment environment, ArrayList<Action> actions) {
        List<Amenity> allAmenities = new ArrayList<>();
        allAmenities.addAll(environment.getLights());
        allAmenities.addAll(environment.getAircons());


        for (Amenity amenity : allAmenities) {
            // Check if the amenity is in the selected room
            if (amenity.getAmenityBlocks().get(0).getPatch().getPatchField() != null && amenity.getAmenityBlocks().get(0).getPatch().getPatchField().getKey() == room) {
                // Add an action if the amenity is in the selected room
                actions.add(new Action(Action.Name.INSPECTING_ROOM,
                        amenity.getAttractors().getFirst().getPatch(),
                        3));
            }
        }
    }

    public void maintenanceInspect(PatchField room, Environment environment, ArrayList<Action> actions) {

        // Get all the list of tables, cubicles, and desk in the model

        List<Amenity> allAmenities = new ArrayList<>();
        allAmenities.addAll(environment.getLearningTables());
        allAmenities.addAll(environment.getResearchTables());
        allAmenities.addAll(environment.getHumanExpTables());
        allAmenities.addAll(environment.getDataCollTables());
        allAmenities.addAll(environment.getPantryTables());
        allAmenities.addAll(environment.getDirectorTables());
        allAmenities.addAll(environment.getCubicles());
        allAmenities.addAll(environment.getMesaTables());
        allAmenities.addAll(environment.getMeetingTables());
        allAmenities.addAll(environment.getSoloTables());
        int ctr = 0;
        for (Amenity amenity : allAmenities) {
            // Check if the amenity is in the selected room
            if (amenity.getAmenityBlocks().get(0).getPatch().getPatchField() != null && amenity.getAmenityBlocks().get(0).getPatch().getPatchField().getKey() == room) {
                // Add an action if the amenity is in the selected room
                actions.add(new Action(Action.Name.INSPECTING_ROOM,
                        amenity.getAttractors().getFirst().getPatch(),
                        3));
                ctr++;
            }

            if (ctr >= 2)
                break;
        }
    }

    public int findIndexState(State.Name name) {
        for(int i = 0; i < this.routePlan.size(); i++) {
            if (this.routePlan.get(i).getName() == name) {
                return i;
            }
        }
        return -1;
    }

    public int findIndexAction(Action.Name name, int stateIndex) {
        for (int i = 0; i < this.routePlan.get(stateIndex).getActions().size(); i++) {
            if (this.routePlan.get(stateIndex).getActions().get(i).getName()  == name) {
                return i;
            }
        }
        return -1;
    }

    public State addUrgentRoute(String s, Agent agent, Environment environment) {
        ArrayList<Action> actions;
        State officeState = null;

        switch (s) {
            case "BATHROOM" -> {
                actions = new ArrayList<>();
                if (agent.getType() != Agent.Type.DIRECTOR) {
                    actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
                    actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
                }
                actions.add(new Action(Action.Name.GO_TO_BATHROOM));
                actions.add(new Action(Action.Name.RELIEVE_IN_CUBICLE, 12, 60));
                actions.add(new Action(Action.Name.FIND_SINK));
                actions.add(new Action(Action.Name.WASH_IN_SINK, 12));
                officeState = new State(State.Name.NEEDS_BATHROOM, this, agent, actions);
            }
            case "DISPENSER" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
                actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
                actions.add(new Action(Action.Name.GOING_DISPENSER));
                actions.add(new Action(Action.Name.GETTING_WATER, 1, 4));
                officeState = new State(State.Name.DISPENSER, this, agent, actions);
            }
            case "REFRIGERATOR" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
                actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
                actions.add(new Action(Action.Name.GOING_FRIDGE));
                if(agent.getEnergyProfile() == Agent.EnergyProfile.GREEN){ actions.add(new Action(Action.Name.GETTING_FOOD, 1, 2)); }
                else if(agent.getEnergyProfile() == Agent.EnergyProfile.NONGREEN){ actions.add(new Action(Action.Name.GETTING_FOOD,  3, 5)); }
                else if(agent.getEnergyProfile() == Agent.EnergyProfile.NEUTRAL){ actions.add(new Action(Action.Name.GETTING_FOOD, 1, 5)); }

                officeState = new State(State.Name.REFRIGERATOR, this, agent, actions);
            }
            case "BREAK" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_BREAK));
                actions.add(new Action(Action.Name.TAKING_BREAK, 120, 240));
                officeState = new State(State.Name.BREAK_TIME, this, agent, actions);
            }
            case "FIX_THERMAL_COMFORT" -> {
                actions = new ArrayList<>();
                // TODO: Need to determine what action to do (e.g. set AC to cool or warm, or turn on or off AC)
                actions.add(new Action(Action.Name.SET_AC_TO_COOL));
                actions.add(new Action(Action.Name.SET_AC_TO_WARM));
                actions.add(new Action(Action.Name.TURN_OFF_AC));
                actions.add(new Action(Action.Name.TURN_OFF_AC));
                officeState = new State(State.Name.FIXING_THERMAL_COMFORT, this, agent, actions);
            }
            case "FIX_VISUAL_COMFORT" -> {
                actions = new ArrayList<>();
                // TODO: Need to determine what action to do (e.g. open or close blinds, or turn on or off Lights)
                actions.add(new Action(Action.Name.TURN_OFF_LIGHT));
                actions.add(new Action(Action.Name.TURN_ON_LIGHT));
                actions.add(new Action(Action.Name.OPEN_BLINDS));
                actions.add(new Action(Action.Name.CLOSE_BLINDS));
                officeState = new State(State.Name.FIXING_VISUAL_COMFORT, this, agent, actions);
            }
            case "EAT_OUTSIDE" -> {
                actions = new ArrayList<>();
                Patch randomExit = environment.getElevators().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size()))
                        .getAmenityBlocks().getFirst().getPatch();
                actions.add(new Action(Action.Name.GO_TO_STATION, 3));
                actions.add(new Action(Action.Name.WAIT_FOR_COLLEAGUE));
                actions.add(new Action(Action.Name.EXIT_LUNCH, randomExit, 180, 360));
                officeState = new State(State.Name.GOING_TO_EAT_OUTSIDE, this, agent, actions);
            }
            case "INQUIRE_FACULTY" -> {
                actions = new ArrayList<>();
                if (agent.getType() == Agent.Type.DIRECTOR) {
                    for (Agent agent1 : environment.getMovableAgents()) {
                        if (agent1 != agent && agent1.getType() == Agent.Type.FACULTY && agent1.getAgentMovement().getRoutePlan().isAtDesk()) { // for director inquire to all directors
                            agent1.getAgentMovement().getRoutePlan().setCanUrgent(false); // turn off urgent so faculties will wait for the director
                            actions.add(new Action(Action.Name.GO_TO_FACULTY, agent1.getAgentMovement().getAssignedSeat().getAttractors().getFirst().getPatch()));
                            actions.add(new Action(Action.Name.ASK_FACULTY, 5));
                        }
                    }
                }
                else { // otherwise go to chooseAgentAsGoal for which faculty to inquire
                    actions.add(new Action(Action.Name.GO_TO_FACULTY));
                    actions.add(new Action(Action.Name.ASK_FACULTY, 12, 32));
                }
                officeState = new State(State.Name.INQUIRE_FACULTY, this, agent, actions);
            }
            case "INQUIRE_STUDENT" -> {
                actions = new ArrayList<>();
                // go to chooseAgentAsGoal for which student to inquire
                actions.add(new Action(Action.Name.GO_TO_STUDENT));
                actions.add(new Action(Action.Name.ASK_STUDENT, 12, 32));
                officeState = new State(State.Name.INQUIRE_STUDENT, this, agent, actions);
            }
            case "INQUIRE_GUARD" -> {
                actions = new ArrayList<>();
                // go to chooseAgentAsGoal for which guard to inquire
                actions.add(new Action(Action.Name.GO_TO_GUARD));
                actions.add(new Action(Action.Name.ASK_GUARD, 12, 40));
                officeState = new State(State.Name.INQUIRE_GUARD, this, agent, actions);
            }
        }
        return officeState;
    }

    public double getCooperate(Agent.Persona persona){

        double chance = 0;

        switch (persona){
            case EXT_STUDENT -> chance = EXT_STUDENT_COOPERATE;
            case DIRECTOR -> chance = 1.0;
            case INT_STUDENT -> chance = INT_STUDENT_COOPERATE;
            case STRICT_FACULTY -> chance = STRICT_FACULTY_COOPERATE;
            case APP_FACULTY -> chance = APP_FACULTY_COOPERATE;
            default -> chance = 0;
        }

        return chance;
    }

    /***** GETTERS *****/
    public ArrayList<State> getCurrentRoutePlan() {
        return routePlan;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Chair getAgentSeat() {
        return agentSeat;
    }
    public int getBATH_AM() {
        return BATH_AM;
    }
    public int getBATH_PM() {
        return BATH_PM;
    }
    public int getBATH_LUNCH() {
        return BATH_LUNCH;
    }
    public int getDISPENSER_LUNCH(){return this.DISPENSER_LUNCH;}
    public int getDISPENSER_PM(){return this.DISPENSER_PM;}
    public boolean isBathPM() {
        return bathPM;
    }
    public boolean isBathAM() {
        return bathAM;
    }
    public int getREFRIGERATOR_LUNCH(){return this.REFRIGERATOR_LUNCH;}
    public boolean getCanUrgent() {
        return canUrgent;
    }
    public int getREFRIGERATOR_PM(){return this.REFRIGERATOR_PM;}
    public int getLastDuration() {
        return lastDuration;
    }
    public long getCollaborationEnd() {
        return collaborationEnd;
    }
    public long getMeetingStart() {
        return meetingStart;
    }
    public long getMeetingEnd() {
        return meetingEnd;
    }
    public int getMeetingRoom() {
        return (int) meetingRoom;
    }
    public Amenity.AmenityBlock getLunchAttractor() {
        return lunchAttractor;
    }
    public Amenity getLunchAmenity() {
        return lunchAmenity;
    }
    public State getLUNCH_INSTANCE() {
        return LUNCH_INSTANCE;
    }
    public boolean isAtDesk() {
        return isAtDesk;
    }
    public int getBREAK_COUNT() {
        return BREAK_COUNT;
    }

    public boolean isTakingLunch() {
        return isTakingLunch;
    }

    /***** SETTERS *****/
    public State setState(int i) {
        this.currentState = this.routePlan.get(i);
        return this.currentState;
    }
    public State setNextState(int i) {
        this.currentState = this.routePlan.get(i+1);
        return this.currentState;
    }
    public State setPreviousState(int i){
        this.currentState = this.routePlan.get(i-1);
        return this.currentState;
    }
    public void setBathPM(boolean bathPM) {
        this.bathPM = bathPM;
    }
    public void setBathAM(boolean bathAM) {
        this.bathAM = bathAM;
    }
    public void setAgentSeat(Chair agentSeat) {
        this.agentSeat = agentSeat;
    }
    public void setBATH_AM(int BATH_AM) {
        this.BATH_AM -= BATH_AM;
    }
    public void setBATH_PM(int BATH_PM) {
        this.BATH_PM -= BATH_PM;
    }
    public void setBATH_LUNCH(int BATH_LUNCH) {
        this.BATH_LUNCH -= BATH_LUNCH;
    }
    public void setDISPENSER_LUNCH(int DISPENSER_LUNCH) {
        this.DISPENSER_LUNCH -= DISPENSER_LUNCH;
    }
    public void setDISPENSER_PM(int DISPENSER_PM) {
        this.DISPENSER_PM -= DISPENSER_PM;
    }
    public void setREFRIGERATOR_LUNCH(int REFRIGERATOR_LUNCH) {
        this.REFRIGERATOR_LUNCH -= REFRIGERATOR_LUNCH;
    }
    public void setREFRIGERATOR_PM(int REFRIGERATOR_PM) {
        this.REFRIGERATOR_PM -= REFRIGERATOR_PM;
    }
    public void setLastDuration(int lastDuration) {
        this.lastDuration = lastDuration;
    }
    public void setCanUrgent(boolean canUrgent) {
        this.canUrgent = canUrgent;
    }
    public void setCollaborationEnd(long tick, int duration) {
        this.collaborationEnd = tick + duration;
    }
    public void setLunchAttractor(Amenity.AmenityBlock lunchAttractor) {
        this.lunchAttractor = lunchAttractor;
    }
    public void setLunchAmenity(Amenity lunchAmenity) {
        this.lunchAmenity = lunchAmenity;
    }
    public void setAtDesk(boolean atDesk) {
        isAtDesk = atDesk;
    }
    public void setBREAK_COUNT(int BREAK_COUNT) {
        this.BREAK_COUNT -= BREAK_COUNT;
    }

    public void setTakingLunch(boolean takingLunch) {
        isTakingLunch = takingLunch;
    }
}
