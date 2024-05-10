package com.socialsim.model.core.agent;

import com.socialsim.controller.Main;
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

    private Cubicle agentCubicle;

    private Amenity.AmenityBlock lunchAttractor;
    private Amenity lunchAmenity;

    State LUNCH_INSTANCE = null;

    public static final double DIRECTOR_LUNCH = 0.7;
    public static final double GUARD_LUNCH = 0.7;
    public static final double MAINTENANCE_LUNCH = 0.7;
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
            setFromBathAM(false);
            setFromBathPM(false);
            setAtDesk(false);

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GUARD_STAY_PUT, environment.getReceptionTables().getFirst().getReceptionChairs()
                    .getFirst().getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GUARD, this, agent, actions));


            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, environment.
                    getChairs().get(4).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));




            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, environment.getReceptionTables().getFirst().getReceptionChairs()
                    .getFirst().getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.MAINTENANCE) {
            setFromBathAM(false);
            setFromBathPM(false);
            setAtDesk(false);
            actions = new ArrayList<>();
            Patch randomToilet = environment.getToilets().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(3)).getAmenityBlocks().get(0).getPatch();
            actions.add(new Action(Action.Name.MAINTENANCE_CLEAN_TOILET, randomToilet, 10));
            routePlan.add(new State(State.Name.MAINTENANCE_BATHROOM, this, agent, actions));

            actions = new ArrayList<>();
            Patch randomPlant = environment.getPlants().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(9)).getAmenityBlocks().get(0).getPatch();
            actions.add(new Action(Action.Name.MAINTENANCE_WATER_PLANT, randomPlant, 10));
            routePlan.add(new State(State.Name.MAINTENANCE_PLANT, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            actions.add(new Action(Action.Name.GO_TO_STATION, environment.getStaffRooms().getFirst().getAssociatedPatches().getFirst().getAmenityBlock().getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.DIRECTOR) {
            setFromBathAM(false);
            setFromBathPM(false);
            setAtDesk(false);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_DIRECTOR_ROOM, environment.getChairs().getLast().getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_LUNCH, environment.
                    getChairs().get(4).getAttractors().get(0).getPatch()));
            actions.add(new Action(Action.Name.EAT_LUNCH, 180, 360));
            routePlan.add(new State(State.Name.EATING_LUNCH, this, agent, actions));

            actions = new ArrayList<>();
            actions.add(new Action(Action.Name.GO_TO_DIRECTOR_ROOM, environment.getChairs().getLast().getAttractors().getFirst().getPatch()));
            routePlan.add(new State(State.Name.WORKING, this, agent, actions));

            actions = new ArrayList<>();
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.STRICT_FACULTY || agent.getPersona() == Agent.Persona.APP_FACULTY) {
            setFromBathAM(false);
            setFromBathPM(false);
            setCOLLABORATE_COUNT(2);
            setAtDesk(false);
            setAgentCubicle(assignedCubicle);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

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
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }
        else if (agent.getPersona() == Agent.Persona.INT_STUDENT || agent.getPersona() == Agent.Persona.EXT_STUDENT) {
            setFromBathAM(false);
            setFromBathPM(false);
            setCOLLABORATE_COUNT(2);
            setAtDesk(false);
            setAgentCubicle(assignedCubicle);

//            actions = new ArrayList<>();
//            actions.add(new Action(Action.Name.GOING_TO_RECEPTION_QUEUE));
//            actions.add(new Action(Action.Name.FILL_UP_NAME, 2));
//            routePlan.add(new State(State.Name.GOING_TO_RECEPTION, this, agent, actions));

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
            int exit = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getGates().size());
            actions.add(new Action(Action.Name.LEAVE_OFFICE, environment.getGates().get(exit).getAmenityBlocks().getFirst().getPatch()));
            routePlan.add(new State(State.Name.GOING_HOME, this, agent, actions));
        }

        setNextState(-1);
    }

    /***** METHODS *****/
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
//            case "PRINT" -> {
//                actions = new ArrayList<>();
//                actions.add(new OfficeAction(OfficeAction.Name.GO_TO_PRINTER));
//                //actions.add(new OfficeAction(OfficeAction.Name.QUEUE_PRINTER));
//                actions.add(new OfficeAction(OfficeAction.Name.PRINTING, 4, 36));
//                officeState = new State(State.Name.NEEDS_PRINT, this, agent, actions);
//            }
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
//            case "TECHNICAL_PRINTER" -> {
//                actions = new ArrayList<>();
//                actions.add(new Action(Action.Name.TECHNICAL_GO_PRINTER, 12, 120));
//                actions.add(new Action(Action.Name.FIX_PRINTER));
//                officeState = new State(State.Name.NEEDS_FIX_PRINTER, this, agent, actions);
//            }
            case "DISPENSER" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GOING_DISPENSER));
                actions.add(new Action(Action.Name.GETTING_WATER, 2, 8));
                officeState = new State(State.Name.DISPENSER, this, agent, actions);
            }
            case "REFRIGERATOR" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GOING_FRIDGE));
                actions.add(new Action(Action.Name.GETTING_FOOD, 2, 10));
                officeState = new State(State.Name.REFRIGERATOR, this, agent, actions);
            }
            case "BREAK" -> {
                actions = new ArrayList<>();
                actions.add(new Action(Action.Name.GO_TO_LUNCH));
                actions.add(new Action(Action.Name.TAKING_BREAK, getAgentCubicle().getAttractors().get(0).getPatch(), 120, 240));
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

//    public State addUrgentRoute(Agent agent, Environment environment) {
//        ArrayList<Action> actions;
//
//        actions = new ArrayList<>();
//        Patch randomCubicle = environment.getCubicles().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(60)).
//                getAmenityBlocks().get(0).getPatch();
//        actions.add(new Action(Action.Name.FIX_CUBICLE, randomCubicle, 120));
//        return new State(State.Name.NEEDS_FIX_CUBICLE, this, agent, actions);
//    }

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
    public State addWaitingRoute(Agent agent){
        ArrayList<Action> actions;
        actions = new ArrayList<>();
        actions.add(new Action(Action.Name.GO_TO_WAIT_AREA));
        actions.add(new Action(Action.Name.WAIT_FOR_VACANT,5,20));
        return new State(State.Name.WAIT_INFRONT_OF_BATHROOM,this, agent, actions);
    }

    public void resetCanUrgent(){this.canUrgent = 2;}

    /***** GETTERS *****/
    public ArrayList<State> getCurrentRoutePlan() {
        return routePlan;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Cubicle getAgentCubicle() {
        return agentCubicle;
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
    public boolean isFromBathPM() {
        return fromBathPM;
    }
    public boolean isFromBathAM() {
        return fromBathAM;
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
    public void setFromBathPM(boolean fromBathPM) {
        this.fromBathPM = fromBathPM;
    }
    public void setFromBathAM(boolean fromBathAM) {
        this.fromBathAM = fromBathAM;
    }
    public void setAgentCubicle(Cubicle agentCubicle) {
        this.agentCubicle = agentCubicle;
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
        this.DISPENSER_LUNCH = DISPENSER_LUNCH;
    }
    public void setDISPENSER_PM(int DISPENSER_PM) {
        this.DISPENSER_PM = DISPENSER_PM;
    }
    public void setREFRIGERATOR_LUNCH(int REFRIGERATOR_LUNCH) {
        this.REFRIGERATOR_LUNCH = REFRIGERATOR_LUNCH;
    }
    public void setREFRIGERATOR_PM(int REFRIGERATOR_PM) {
        this.REFRIGERATOR_PM = REFRIGERATOR_PM;
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
