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
        NEEDS_BATHROOM, NEEDS_PRINT, NEEDS_COLLAB,
//        NEEDS_FIX_PRINTER, NEEDS_FIX_CUBICLE,
        GOING_TO_MEETING, MEETING, GOING_TO_WORK, WORKING, GOING_TO_LUNCH, EATING_LUNCH, GOING_HOME,
        GUARD,
//        RECEPTIONIST, SECRETARY,
        MAINTENANCE_BATHROOM, MAINTENANCE_PLANT,
//        CLIENT, DRIVER, VISITOR,
        INQUIRE_DIRECTOR, INQUIRE_FACULTY, INQUIRE_STUDENT, ANSWER_DIRECTOR, ANSWER_FACULTY, ANSWER_STUDENT, DISPENSER,
        REFRIGERATOR, BREAK_TIME,
        WAIT_INFRONT_OF_BATHROOM
    }
}