package com.socialsim.model.core.agent;

import java.util.ArrayList;

public class State {

    /***** VARIABLES *****/
    private Name name;
    private RoutePlan routePlan;
    private Agent agent;
    private ArrayList<Action> actions;
    private int tickClassStart;

    /***** CONSTRUCTOR *****/
    public State(Name a, RoutePlan routePlan, Agent agent, ArrayList<Action> actions) {
        this.name = a;
        this.routePlan = routePlan;
        this.agent = agent;
        this.actions = actions;
    }


    /***** METHODS *****/
    public void addAction(Action a){
        actions.add(a);
    }

    public int findIndexAction(Action.Name name) {
        for (int i = 0; i < actions.size(); i++) {
            if (this.actions.get(i).getName()  == name) {
                return i;
            }
        }
        return -1;
    }




    /***** GETTERS *****/
    public Name getName() {
        return name;
    }
    public int getTickStart() {
        return tickClassStart;
    }
    public RoutePlan getRoutePlan() {
        return routePlan;
    }
    public Agent getAgent() {
        return agent;
    }
    public ArrayList<Action> getActions() {
        return this.actions;
    }






    /***** SETTERS *****/
    public void setName(Name name) {
        this.name = name;
    }
    public void setTickStart(int tickStart) {
        this.tickClassStart = tickStart;
    }
    public void setRoutePlan(RoutePlan routePlan) {
        this.routePlan = routePlan;
    }
    public void setAgent(Agent agent) {
        this.agent = agent;
    }




    /***** ENUM *****/
    public enum Name {
        GOING_TO_RECEPTION,
        NEEDS_BATHROOM, NEEDS_COLLAB,
        GOING_TO_MEETING, MEETING, GOING_TO_WORK, WORKING, GOING_TO_LUNCH, EATING_LUNCH, GOING_HOME,
        INSPECT_ROOMS,
        FIXING_THERMAL_COMFORT, FIXING_VISUAL_COMFORT,
        GUARD,
        MAINTENANCE_BATHROOM, MAINTENANCE_PLANT,
        INQUIRE_DIRECTOR, INQUIRE_FACULTY, INQUIRE_STUDENT, INQUIRE_GUARD, INQUIRE_MAINTENANCE, DISPENSER,
        REFRIGERATOR, BREAK_TIME,
        COFFEE,
        WAIT_FOR_ACTIVITY,
        GOING_TO_EAT_OUTSIDE
    }
}