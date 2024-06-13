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
    private int canUrgent = 0;
    private long collaborationEnd = 0, meetingStart = -1, meetingEnd, meetingRoom;

    private int BATH_AM = 2, BATH_PM = 2, BATH_LUNCH = 1;
    private int COLLABORATE_COUNT = 0, BREAK_COUNT = 2;
    private int DISPENSER_LUNCH = 1, DISPENSER_PM = 1;
    private int REFRIGERATOR_LUNCH = 1, REFRIGERATOR_PM = 1;

    private Amenity agentSeat;

    private Amenity.AmenityBlock lunchAttractor;
    private Amenity lunchAmenity;

    State LUNCH_INSTANCE = null;

    public static final double DIRECTOR_LUNCH = 0.7;
    public static final double GUARD_LUNCH = 0.7;
    public static final double MAINTENANCE_LUNCH = 0.7;
    public static final double EAT_OUTSIDE = 0.9;
    public static final double INT_LUNCH = 0.3;
    public static final double EXT_LUNCH = 1.0;
    public static final double STRICT_FACULTY_COOPERATE = 0.6;
    public static final double APP_FACULTY_COOPERATE = 0.9;
    public static final double INT_STUDENT_COOPERATE = 0.6;
    public static final double EXT_STUDENT_COOPERATE = 0.9;
    public static final double  BATH_CHANCE = 0.15,
                                DISPENSER_CHANCE = 0.1,
                                REFRIGERATOR_CHANCE = 0.3,
                                MAINTENANCE_BREAK_CHANCE = 0.1,
                                GUARD_BREAK_CHANCE = 0.5,
                                BREAK_CHANCE = 0.1;
    public static ArrayList<ArrayList<Long>> meetingTimes = new ArrayList<>();

    /***** CONSTRUCTOR *****/

    public RoutePlan(Agent agent, Environment environment, Patch spawnPatch, int team, Amenity assignedSeat) {
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
//            // Inspect Storage Room/s
//            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
//                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
//            }
//            // Inspect Pantry Room/s
//            for(int i = 0; i < environment.getPantries().size(); i++) {
//                maintenanceInspect(environment.getPantries().get(i), environment, actions);
//            }
//            // Inspect Learning Space Room/s
//            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
//                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
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
//            // Inspect Director Room/s
//            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
//                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
//            }
//            // Inspect Break Room/s
//            for(int i = 0; i < environment.getBreakAreas().size(); i++) {
//                maintenanceInspect(environment.getBreakAreas().get(i), environment, actions);
//            }
//            routePlan.add(new State(State.Name.INSPECT_ROOMS, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GUARD_STAY_PUT, assignedSeat.getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GUARD, this, agent, actions));

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedSeat.getAttractors().getFirst().getPatch()));
//            actions.add(new Action(Action.Name.EAT_LUNCH, 360));
//            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            // Developer Note: The thesis does not need to implement elevator behavior, where agents will wait for an elevator to leave
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            // This is for when the agent isn't in his station or is a behavior of getting he/she's belongings before going home
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch(), 3));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));

        }
        else if (agent.getPersona() == Agent.Persona.MAINTENANCE) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

//            actions = new ArrayList<>();
//            // Maintenance put their stuff in the Clinic Room/ Storage Room
//            actions.add(new Action(Action.Name.GO_TO_STATION,
//                    environment.getStorageCabinets().getFirst().getAttractors().getFirst().getPatch(), 5));
//            routePlan.add(new State(State.Name.GOING_TO_WORK, this, agent, actions));

//            actions = new ArrayList<>();
//
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
//            // Inspect Storage Room/s
//            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
//                maintenanceInspect(environment.getStorageRooms().get(i), environment, actions);
//            }
//            // Inspect Pantry Room/s
//            for(int i = 0; i < environment.getPantries().size(); i++) {
//                maintenanceInspect(environment.getPantries().get(i), environment, actions);
//            }
//            // Inspect Learning Space Room/s
//            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
//                maintenanceInspect(environment.getLearningSpaces().get(i), environment, actions);
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
//            // Inspect Director Room/s
//            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
//                maintenanceInspect(environment.getDirectorRooms().get(i), environment, actions);
//            }
//            // Inspect Break Room/s
//            for(int i = 0; i < environment.getBreakAreas().size(); i++) {
//                maintenanceInspect(environment.getBreakAreas().get(i), environment, actions);
//            }
//            routePlan.add(new State(State.Name.INSPECT_ROOMS, this, agent, actions));

//            actions = new ArrayList<>();
//            for (int i = 0; i < environment.getOfficeToilets().size(); i++) {
//                actions.add(new Action(Action.Name.MAINTENANCE_CLEAN_TOILET, 10)); // Cleans only OfficeToilet
//            }
//            routePlan.add(new State(State.Name.MAINTENANCE_BATHROOM, this, agent, actions));

//            actions = new ArrayList<>();
//            for(int i = 0; i < environment.getPlants().size(); i++) {
//                actions.add(new Action(Action.Name.MAINTENANCE_WATER_PLANT, 10));
//            }
//            routePlan.add(new State(State.Name.MAINTENANCE_PLANT, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH)); // Maintenance does not have an assigned seat in the model
            actions.add(new Action(Action.Name.EAT_LUNCH, 720));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_WAIT_AREA)); // The destination is set on the Simulator.java
            routePlan.add(new State(State.Name.WAIT_FOR_ACTIVITY, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));

            actions.add(new Action(Action.Name.GO_TO_STATION, environment.getStorageCabinets().getFirst().getAttractors().getFirst().getPatch(), 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.DIRECTOR) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));
//            routePlan.add(addUrgentRoute("BATHROOM", agent));

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedSeat.getAttractors().getFirst().getPatch()));
//            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
//            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.STRICT_FACULTY || agent.getPersona() == Agent.Persona.APP_FACULTY) {
//            setCOLLABORATE_COUNT(2);
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

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GO_TO_LUNCH));
//            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
//            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));
//            this.LUNCH_INSTANCE = routePlan.get(routePlan.size()-1);

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_STUDENT || agent.getPersona() == Agent.Persona.EXT_STUDENT) {
//            setCOLLABORATE_COUNT(2);
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);
            setAgentSeat(assignedSeat);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));


            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH));
            actions.add(new Action(Action.Name.EAT_LUNCH, 720));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }

        setNextState(-1);
    }

    /***** METHODS *****/

    public void maintenanceInspect(PatchField room, Environment environment, ArrayList<Action> actions) {

        // Get all the list of tables, cubicles, and desk in the model
        List<? extends Amenity> collabDesk = environment.getAmenityList(CollabDesk.class);
        List<? extends Amenity> cubicle = environment.getAmenityList(Cubicle.class);
        List<? extends Amenity> meetingDesk = environment.getAmenityList(MeetingDesk.class);
        List<? extends Amenity> pantry = environment.getAmenityList(PantryTable.class);
        List<? extends Amenity> table = environment.getAmenityList(Table.class);
        List<? extends Amenity> officeDesk = environment.getAmenityList(OfficeDesk.class);

        List<Amenity> allAmenities = new ArrayList<>();
//        allAmenities.addAll(collabDesk);
//        allAmenities.addAll(cubicle);
        allAmenities.addAll(meetingDesk);
        allAmenities.addAll(pantry);
//        allAmenities.addAll(table);
        allAmenities.addAll(officeDesk);

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

    public int findIndexState(State.Name name) {
        for(int i = 0; i < this.routePlan.size(); i++) {
            if (this.routePlan.get(i).getName() == name) {
                return i;
            }
        }
        return -1;
    }

    public State addUrgentRoute(String s, Agent agent){
        ArrayList<Action> actions;
        State officeState;

        switch (s) {
            case "BATHROOM" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
                actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
                actions.add(new Action(Action.Name.GO_TO_BATHROOM));
                actions.add(new Action(Action.Name.RELIEVE_IN_CUBICLE, 12, 60));
                actions.add(new Action(Action.Name.FIND_SINK));
                actions.add(new Action(Action.Name.WASH_IN_SINK, 12));
                officeState = new State(State.Name.NEEDS_BATHROOM, this, agent, actions);
            }
            case "COLLABORATION" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_COLLAB, 60));
                actions.add(new Action(Action.Name.COLLABORATE, 60, 300));
                officeState = new State(State.Name.NEEDS_COLLAB, this, agent, actions);
            }
            case "INQUIRE_DIRECTOR" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_DIRECTOR));
                actions.add(new Action(Action.Name.ASK_DIRECTOR));
                officeState = new State(State.Name.INQUIRE_DIRECTOR, this, agent, actions);
            }
            case "INQUIRE_FACULTY" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_FACULTY));
                actions.add(new Action(Action.Name.ASK_FACULTY));
                officeState = new State(State.Name.INQUIRE_FACULTY, this, agent, actions);
            }
            case "INQUIRE_STUDENT" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_STUDENT));
                actions.add(new Action(Action.Name.ASK_STUDENT));
                officeState = new State(State.Name.INQUIRE_STUDENT, this, agent, actions);
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
            default -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_MEETING));
                actions.add(new Action(Action.Name.WAIT_MEETING, 60));
                actions.add(new Action(Action.Name.MEETING));
                officeState = new State(State.Name.MEETING, this, agent, actions);
            }
        }

        return officeState;
    }

    public State addUrgentRoute(String s, Agent agent, Environment environment) {
        ArrayList<Action> actions;
        State officeState = null;

        switch (s) {
            case "EAT_OUTSIDE" -> {
                actions = new ArrayList<>();
                Patch randomExit = environment.getGates().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size()))
                        .getAmenityBlocks().getFirst().getPatch();
                actions.add(new Action(Action.Name.GO_TO_STATION, 3));
                actions.add(new Action(Action.Name.WAIT_FOR_COLLEAGUE));
                actions.add(new Action(Action.Name.EXIT_LUNCH, randomExit, 180, 360));
                officeState = new State(State.Name.GOING_TO_EAT_OUTSIDE, this, agent, actions);
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

    public Amenity getAgentSeat() {
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
    public int getCanUrgent() {
        return canUrgent;
    }
    public int getREFRIGERATOR_PM(){return this.REFRIGERATOR_PM;}
    public int getLastDuration() {
        return lastDuration;
    }
    public int getCOLLABORATE_COUNT() {
        return COLLABORATE_COUNT;
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
    public void setAgentSeat(Amenity agentSeat) {
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
    public void setCanUrgent(int canUrgent) {
        this.canUrgent += canUrgent;
    }
    public void setCOLLABORATE_COUNT(int count) {
        this.COLLABORATE_COUNT += count;
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
}
