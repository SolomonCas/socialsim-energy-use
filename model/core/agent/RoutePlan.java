package com.socialsim.model.core.agent;

import com.socialsim.controller.Main;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.Floor;
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




    private Amenity.AmenityBlock lunchAttractor;
    private Amenity lunchAmenity;
    private boolean isTakingLunch = false, isLeaving = false;

    State LUNCH_INSTANCE = null;

    

    /*** Responsible for doing Urgent Task ***/
    private boolean canUrgent = true;
    private int BATH_AM = 1, BATH_PM = 1, BATH_LUNCH = 1;
    private int COFFEE_COUNT = 2;
    private int DISPENSER_LUNCH = 1, DISPENSER = 1;
    private int REFRIGERATOR_LUNCH = 1, REFRIGERATOR = 1;
    private double  BATHROOM_CHANCE,
            WORKING_CHANCE,
            DISPENSER_CHANCE,
            REFRIGERATOR_CHANCE,
            COFFEE_CHANCE,
            EAT_OUTSIDE_CHANCE,
            EAT_FROM_WORKPLACE,
            INQUIRE_STUDENT_CHANCE,
            INQUIRE_FACULTY_CHANCE,
            INQUIRE_DIRECTOR_CHANCE,
            INQUIRE_GUARD_CHANCE,
            INQUIRE_MAINTENANCE_CHANCE;
    public static ArrayList<ArrayList<Long>> meetingTimes = new ArrayList<>();

    /***** CONSTRUCTOR *****/

    public RoutePlan(Agent agent, Environment environment, Patch spawnPatch, int team, Amenity assignedSeat) {
        this.routePlan = new ArrayList<>();
        ArrayList<Action> actions;

        if (agent.getPersona() == Agent.Persona.GUARD) {
            WORKING_CHANCE = 0.8;
            INQUIRE_STUDENT_CHANCE = 0.0;
            INQUIRE_FACULTY_CHANCE = 0.0;
            INQUIRE_DIRECTOR_CHANCE = 0.0;
            INQUIRE_GUARD_CHANCE = 0.0;
            INQUIRE_MAINTENANCE_CHANCE = 0.0;
            BATHROOM_CHANCE = 0.15;
            COFFEE_CHANCE = 0.0;

            DISPENSER_CHANCE = 0.02;
            REFRIGERATOR_CHANCE = 0.03;

            EAT_OUTSIDE_CHANCE = 0.0;
            EAT_FROM_WORKPLACE = 1.0;



            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);


            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GUARD_STAY_PUT, assignedSeat.getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GUARD, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, assignedSeat.getAttractors().getFirst().getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

//            actions = new ArrayList<>();
//            // Developer Note: The thesis does not need to implement elevator behavior, where agents will wait for an elevator to leave
//            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
//            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
//            // This is for when the agent isn't in his station or is a behavior of getting he/she's belongings before going home
//            actions.add(new Action(Action.Name.GO_TO_STATION, assignedSeat.getAttractors().getFirst().getPatch(), 3));
//            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));

        }
        else if (agent.getPersona() == Agent.Persona.MAINTENANCE) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);

            WORKING_CHANCE = 0.8;
            INQUIRE_STUDENT_CHANCE = 0.0;
            INQUIRE_FACULTY_CHANCE = 0.0;
            INQUIRE_DIRECTOR_CHANCE = 0.0;
            INQUIRE_GUARD_CHANCE = 0.07;
            INQUIRE_MAINTENANCE_CHANCE = 0.02;
            BATHROOM_CHANCE = 0.10;
            COFFEE_CHANCE = 0.0;

            DISPENSER_CHANCE = 0.02;
            REFRIGERATOR_CHANCE = 0.03;

            EAT_OUTSIDE_CHANCE = 0.0;
            EAT_FROM_WORKPLACE = 1.0;

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
                inspectRoom(environment.getMeetingRooms().get(i), environment, actions);
            }
            // Inspect Human Experience Room/s
            for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
                inspectRoom(environment.getHumanExpRooms().get(i), environment, actions);
            }
            // Inspect Data Collection Room/s
            for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
                inspectRoom(environment.getDataCollectionRooms().get(i), environment, actions);
            }
            // Inspect Research Centers Room/s
            for(int i = 0; i < environment.getResearchCenters().size(); i++) {
                inspectRoom(environment.getResearchCenters().get(i), environment, actions);
            }
            // Inspect Faculty Room/s
            for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
                inspectRoom(environment.getFacultyRooms().get(i), environment, actions);
            }
            // Inspect Storage Room/s
            for(int i = 0; i < environment.getStorageRooms().size(); i++) {
                inspectRoom(environment.getStorageRooms().get(i), environment, actions);
            }
            // Inspect Pantry Room/s
            for(int i = 0; i < environment.getPantries().size(); i++) {
                inspectRoom(environment.getPantries().get(i), environment, actions);
            }
            // Inspect Learning Space Room/s
            for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
                inspectRoom(environment.getLearningSpaces().get(i), environment, actions);
            }
            // Inspect Control Center Room/s
            for(int i = 0; i < environment.getControlCenters().size(); i++) {
                inspectRoom(environment.getControlCenters().get(i), environment, actions);
            }
            // Inspect Data Center Room/s
            for(int i = 0; i < environment.getDataCenters().size(); i++) {
                inspectRoom(environment.getDataCenters().get(i), environment, actions);
            }
            // Inspect Solo Room/s
            for(int i = 0; i < environment.getSoloRooms().size(); i++) {
                inspectRoom(environment.getSoloRooms().get(i), environment, actions);
            }
            // Inspect MESA
            for(int i = 0; i < environment.getMESAs().size(); i++) {
                inspectRoom(environment.getMESAs().get(i), environment, actions);
            }
            // Inspect Director Room/s
            for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
                inspectRoom(environment.getDirectorRooms().get(i), environment, actions);
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

            WORKING_CHANCE = 0.8;
            INQUIRE_STUDENT_CHANCE = 0.0;
            INQUIRE_FACULTY_CHANCE = 0.0;
            INQUIRE_DIRECTOR_CHANCE = 0.0;
            INQUIRE_GUARD_CHANCE = 0.0;
            INQUIRE_MAINTENANCE_CHANCE = 0.0;
            BATHROOM_CHANCE = 0.15;
            COFFEE_CHANCE = 0.04;

            DISPENSER_CHANCE = 0.02;
            REFRIGERATOR_CHANCE = 0.03;

            EAT_OUTSIDE_CHANCE = 0.5;
            EAT_FROM_WORKPLACE = 0.4;


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
            actions.add(new Action(Action.Name.TURN_OFF_AC));
            actions.add(new Action(Action.Name.TURN_OFF_LIGHT));
            actions.add(new Action(Action.Name.GO_TO_STATION, 3));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.STRICT_FACULTY || agent.getPersona() == Agent.Persona.APP_FACULTY) {
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);

            WORKING_CHANCE = 0.8;
            BATHROOM_CHANCE = 0.15;
            COFFEE_CHANCE = 0.02;

            DISPENSER_CHANCE = 0.13;
            REFRIGERATOR_CHANCE = 0.1;

            EAT_OUTSIDE_CHANCE = 0.8;
            EAT_FROM_WORKPLACE = 0.1;

            if (agent.getPersona() == Agent.Persona.STRICT_FACULTY) {
                INQUIRE_STUDENT_CHANCE = 0.0;
                INQUIRE_FACULTY_CHANCE = 0.0;
                INQUIRE_DIRECTOR_CHANCE = 0.0;
                INQUIRE_GUARD_CHANCE = 0.0;
                INQUIRE_MAINTENANCE_CHANCE = 0.0;
            }
            else if (agent.getPersona() == Agent.Persona.APP_FACULTY) {
                INQUIRE_STUDENT_CHANCE = 0.1;
                INQUIRE_FACULTY_CHANCE = 0.1;
                INQUIRE_DIRECTOR_CHANCE = 0.0;
                INQUIRE_GUARD_CHANCE = 0.0;
                INQUIRE_MAINTENANCE_CHANCE = 0.0;
            }


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
            actions.add(new Action(Action.Name.TURN_OFF_AC));
            actions.add(new Action(Action.Name.TURN_OFF_LIGHT));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_STUDENT || agent.getPersona() == Agent.Persona.EXT_STUDENT) {
//            setCOLLABORATE_COUNT(2);
            setBathAM(false);
            setBathPM(false);
            setAtDesk(false);

            WORKING_CHANCE = 0.8;
            BATHROOM_CHANCE = 0.14;
            COFFEE_CHANCE = 0.01;

            DISPENSER_CHANCE = 0.25;
            REFRIGERATOR_CHANCE = 0.1;

            EAT_OUTSIDE_CHANCE = 0.8;
            EAT_FROM_WORKPLACE = 0.1;

            if (agent.getPersona() == Agent.Persona.INT_STUDENT) {
                INQUIRE_STUDENT_CHANCE = 0.0;
                INQUIRE_FACULTY_CHANCE = 0.0;
                INQUIRE_DIRECTOR_CHANCE = 0.0;
                INQUIRE_GUARD_CHANCE = 0.0;
                INQUIRE_MAINTENANCE_CHANCE = 0.0;
            }
            else if (agent.getPersona() == Agent.Persona.EXT_STUDENT) {
                INQUIRE_STUDENT_CHANCE = 0.1;
                INQUIRE_FACULTY_CHANCE = 0.1;
                INQUIRE_DIRECTOR_CHANCE = 0.0;
                INQUIRE_GUARD_CHANCE = 0.0;
                INQUIRE_MAINTENANCE_CHANCE = 0.0;
            }

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
            actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_STATION));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH));
            actions.add(new Action(Action.Name.EAT_LUNCH, 720));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getElevators().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.TURN_OFF_AC));
            actions.add(new Action(Action.Name.TURN_OFF_LIGHT));
            actions.add(new Action(Action.Name.GO_TO_STATION, 2));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }

        setNextState(-1);
    }

    /***** METHODS *****/
    public void inspect(PatchField room, Environment environment, ArrayList<Action> actions) {
        System.out.println("@inspect");
        for (Amenity amenity : environment.getUsedAmenities()) {
            // Check if the amenity is in the selected room
            if (amenity.getAmenityBlocks().get(0).getPatch().getPatchField() != null && amenity.getAmenityBlocks().get(0).getPatch().getPatchField().getKey() == room) {
                // Add an action if the amenity is in the selected room
                if ((amenity instanceof Aircon && ((Aircon) amenity).isTurnedOn())) {
                    System.out.println("ac is on");
                    actions.add(new Action(Action.Name.TURN_OFF_AC, amenity.getAttractors().getFirst().getPatch()));
                }
                else if ((amenity instanceof Light && ((Light) amenity).isOn())) {
                    actions.add(new Action(Action.Name.TURN_OFF_LIGHT, amenity.getAttractors().getFirst().getPatch()));
                }
                else {
                    actions.add(new Action(Action.Name.INSPECTING_ROOM,
                            amenity.getAttractors().getFirst().getPatch(),
                            3));
                }
            }
        }
    }

    public void inspectRoom(PatchField room, Environment environment, ArrayList<Action> actions) {

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

        Collections.shuffle(allAmenities, Simulator.RANDOM_NUMBER_GENERATOR);

        int ctr = 0;
        for (Amenity amenity : allAmenities) {
            // Check if the amenity is in the selected room
            if (amenity.getAmenityBlocks().get(0).getPatch().getPatchField() != null &&
                    amenity.getAmenityBlocks().get(0).getPatch().getPatchField().getKey() == room) {
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

    public State addUrgentRoute(String s, Agent agent, Environment environment) {
        return addUrgentRoute(s, agent, environment, 0);
    }

    public State addUrgentRoute(String s, Agent agent, Environment environment, int randomDuration) {
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
            case "COFFEE" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
                actions.add(new Action(Action.Name.WAIT_FOR_VACANT));
                actions.add(new Action(Action.Name.GOING_COFFEEMAKER));
                actions.add(new Action(Action.Name.MAKE_COFFEE, 70));
                officeState = new State(State.Name.COFFEE, this, agent, actions);
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
            case "FIX_THERMAL_COMFORT" -> {
                actions = new ArrayList<>();
                if  (!agent.getAgentMovement().getAirconToChange().isTurnedOn())
                    actions.add(new Action(Action.Name.TURN_ON_AC));
                else if (agent.getAgentMovement().isToCool() && agent.getAgentMovement().getAirconToChange().isTurnedOn())
                    actions.add(new Action(Action.Name.SET_AC_TO_COOL));
                else if (agent.getAgentMovement().isToHeat() && agent.getAgentMovement().getAirconToChange().isTurnedOn())
                    actions.add(new Action(Action.Name.SET_AC_TO_WARM));

                officeState = new State(State.Name.FIXING_THERMAL_COMFORT, this, agent, actions);
            }
            case "FIX_VISUAL_COMFORT" -> {
                actions = new ArrayList<>();
                if (agent.getAgentMovement().getLightsToOpen() != null) {
                    actions.add(new Action(Action.Name.TURN_ON_LIGHT));
                }
                else if (agent.getAgentMovement().getBlindsToOpen() != null) {
                    actions.add(new Action(Action.Name.OPEN_BLINDS));
                }
                officeState = new State(State.Name.FIXING_VISUAL_COMFORT, this, agent, actions);
            }
            case "EAT_OUTSIDE" -> {
                actions = new ArrayList<>();
                Patch randomExit = environment.getElevators().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getElevators().size()))
                        .getAmenityBlocks().getFirst().getPatch();
                actions.add(new Action(Action.Name.GO_TO_STATION, 3));
                actions.add(new Action(Action.Name.WAIT_FOR_COLLEAGUE));
                if(agent.getTeam() != 0 && agent.getType() == Agent.Type.STUDENT){
                    actions.add(new Action(Action.Name.EXIT_LUNCH, randomExit, randomDuration));

                }
                else{
                    actions.add(new Action(Action.Name.EXIT_LUNCH, randomExit, 180, 360));
                }
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
            case "OPEN_HALLWAY_LIGHTS" -> {
                actions = new ArrayList<>();
                // go to chooseAgentAsGoal for which guard to inquire
                for (Light light : environment.getLights()) {
                    for (Amenity.AmenityBlock attractor : light.getAttractors()) {
                        if (attractor.getPatch().getPatchField().getKey() instanceof Floor && !light.isOn()) {
                            agent.getAgentMovement().setLightsToOpen(light);
                            actions.add(new Action(Action.Name.TURN_ON_LIGHT));
                            break;
                        }
                    }
                    if (agent.getAgentMovement().getLightsToOpen() != null) {
                        break;
                    }
                }
                officeState = new State(State.Name.FIXING_VISUAL_COMFORT, this, agent, actions);
            }
            case "INSPECT" -> {
                actions = new ArrayList<>();

                // Inspect Meeting Room/s
                for(int i = 0; i < environment.getMeetingRooms().size(); i++) {
                    inspect(environment.getMeetingRooms().get(i), environment, actions);
                }
                // Inspect Human Experience Room/s
                for(int i = 0; i < environment.getHumanExpRooms().size(); i++) {
                    inspect(environment.getHumanExpRooms().get(i), environment, actions);
                }
                // Inspect Data Collection Room/s
                for(int i = 0; i < environment.getDataCollectionRooms().size(); i++) {
                    inspect(environment.getDataCollectionRooms().get(i), environment, actions);
                }
                // Inspect Research Centers Room/s
                for(int i = 0; i < environment.getResearchCenters().size(); i++) {
                    inspect(environment.getResearchCenters().get(i), environment, actions);
                }
                // Inspect Faculty Room/s
                for(int i = 0; i < environment.getFacultyRooms().size(); i++) {
                    inspect(environment.getFacultyRooms().get(i), environment, actions);
                }
                // Inspect Storage Room/s
                for(int i = 0; i < environment.getStorageRooms().size(); i++) {
                    inspect(environment.getStorageRooms().get(i), environment, actions);
                }
                // Inspect Pantry Room/s
                for(int i = 0; i < environment.getPantries().size(); i++) {
                    inspect(environment.getPantries().get(i), environment, actions);
                }
                // Inspect Learning Space Room/s
                for(int i = 0; i < environment.getLearningSpaces().size(); i++) {
                    inspect(environment.getLearningSpaces().get(i), environment, actions);
                }
                // Inspect Control Center Room/s
                for(int i = 0; i < environment.getControlCenters().size(); i++) {
                    inspect(environment.getControlCenters().get(i), environment, actions);
                }
                // Inspect Data Center Room/s
                for(int i = 0; i < environment.getDataCenters().size(); i++) {
                    inspect(environment.getDataCenters().get(i), environment, actions);
                }
                // Inspect Solo Room/s
                for(int i = 0; i < environment.getSoloRooms().size(); i++) {
                    inspect(environment.getSoloRooms().get(i), environment, actions);
                }
                for (int i = 0; i < environment.getFloors().size(); i++) {
                    inspect(environment.getFloors().get(i), environment, actions);
                }
                // Inspect MESA
                for(int i = 0; i < environment.getMESAs().size(); i++) {
                    inspect(environment.getMESAs().get(i), environment, actions);
                }
                // Inspect Director Room/s
                for(int i = 0; i < environment.getDirectorRooms().size(); i++) {
                    inspect(environment.getDirectorRooms().get(i), environment, actions);
                }
                officeState = new State(State.Name.INSPECT_ROOMS, this, agent, actions);
            }
        }
        return officeState;
    }

    /***** GETTERS *****/
    public ArrayList<State> getCurrentRoutePlan() {
        return routePlan;
    }
    public State getCurrentState() {
        return currentState;
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
    public int getDISPENSER(){return this.DISPENSER;}
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
    public int getREFRIGERATOR(){return this.REFRIGERATOR;}
    public int getLastDuration() {
        return lastDuration;
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
    public boolean isTakingLunch() {
        return isTakingLunch;
    }

    public double getBATHROOM_CHANCE() {
        return BATHROOM_CHANCE;
    }

    public double getDISPENSER_CHANCE() {
        return DISPENSER_CHANCE;
    }

    public double getEAT_FROM_WORKPLACE() {
        return EAT_FROM_WORKPLACE;
    }

    public double getEAT_OUTSIDE_CHANCE() {
        return EAT_OUTSIDE_CHANCE;
    }

    public double getINQUIRE_DIRECTOR_CHANCE() {
        return INQUIRE_DIRECTOR_CHANCE;
    }

    public double getINQUIRE_FACULTY_CHANCE() {
        return INQUIRE_FACULTY_CHANCE;
    }

    public double getINQUIRE_GUARD_CHANCE() {
        return INQUIRE_GUARD_CHANCE;
    }

    public double getINQUIRE_MAINTENANCE_CHANCE() {
        return INQUIRE_MAINTENANCE_CHANCE;
    }

    public double getINQUIRE_STUDENT_CHANCE() {
        return INQUIRE_STUDENT_CHANCE;
    }

    public double getREFRIGERATOR_CHANCE() {
        return REFRIGERATOR_CHANCE;
    }

    public int getCOFFEE_COUNT() {
        return COFFEE_COUNT;
    }

    public double getCOFFEE_CHANCE() {
        return COFFEE_CHANCE;
    }

    public double getWORKING_CHANCE() {
        return WORKING_CHANCE;
    }

    public boolean isLeaving() {
        return isLeaving;
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
    public void setDISPENSER(int DISPENSER) {
        this.DISPENSER -= DISPENSER;
    }
    public void setREFRIGERATOR_LUNCH(int REFRIGERATOR_LUNCH) {
        this.REFRIGERATOR_LUNCH -= REFRIGERATOR_LUNCH;
    }
    public void setREFRIGERATOR(int REFRIGERATOR) {
        this.REFRIGERATOR -= REFRIGERATOR;
    }
    public void setCOFFEE_COUNT(int COFFEE_COUNT) {
        this.COFFEE_COUNT -= COFFEE_COUNT;
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

    public void setTakingLunch(boolean takingLunch) {
        isTakingLunch = takingLunch;
    }

    public void setLeaving(boolean leaving) {
        isLeaving = leaving;
    }
}
