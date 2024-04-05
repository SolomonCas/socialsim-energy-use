package com.socialsim.model.core.agent;

import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.simulator.Simulator;

public class Action {


    /***** VARIABLES *****/
    private Name name;
    private int duration;
    private Patch destination;



    /***** CONSTRUCTORS *****/
    public Action(Name name){
        this.name = name;
    }

    public Action(Name name, int duration){
        this.name = name;
        this.duration = duration;
    }

    public Action(Name name, int minimumDuration, int maximumDuration){
        this.name = name;
        this.duration = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(maximumDuration - minimumDuration + 1) + minimumDuration;
    }

    public Action(Name name, Patch destination, int minimumDuration, int maximumDuration){
        this.name = name;
        this.destination = destination;
        this.duration = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(maximumDuration - minimumDuration + 1) + minimumDuration;
    }

    public Action(Name name, Patch destination, int duration) {
        this.name = name;
        this.destination = destination;
        this.duration = duration;
    }



    /***** GETTERS *****/
    public Name getName() {
        return name;
    }
    public int getDuration() {
        return duration;
    }
    public Patch getDestination() {
        return destination;
    }




    /***** SETTERS *****/
    public void setName(Name name) {
        this.name = name;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setDestination(Patch destination) {
        this.destination = destination;
    }





    /***** ENUM *****/
    public enum Name {
        LEAVE_OFFICE(), GO_TO_LUNCH(), EAT_LUNCH(), EXIT_LUNCH(),
        GOING_TO_RECEPTION_QUEUE(), FILL_UP_NAME(),
        GUARD_STAY_PUT(), GREET_PERSON(),
        MAINTENANCE_CLEAN_TOILET(), MAINTENANCE_WATER_PLANT(),
        TURN_ON_AC(), TURN_ON_LIGHT(),
        TURN_OFF_AC(), TURN_OFF_LIGHT(),
//        CLIENT_GO_RECEPTIONIST(), CLIENT_GO_COUCH(), CLIENT_GO_OFFICE(),
//        DRIVER_GO_RECEPTIONIST(), DRIVER_GO_COUCH(),
//        VISITOR_GO_RECEPTIONIST(), VISITOR_GO_OFFICE(),
//        RECEPTIONIST_STAY_PUT(),
//        SECRETARY_STAY_PUT(), SECRETARY_CHECK_CABINET(), SECRETARY_GO_BOSS(),
        GO_TO_STATION(), GO_TO_OFFICE_ROOM(), GO_TO_DIRECTOR(), ASK_DIRECTOR(),
        GO_TO_FACULTY(), GO_TO_STUDENT(), ASK_FACULTY(), ASK_STUDENT(), ANSWER_DIRECTOR(),
        ANSWER_FACULTY(), ANSWER_STUDENT(),
        GO_TO_BATHROOM(), RELIEVE_IN_CUBICLE(), FIND_SINK(), WASH_IN_SINK(),
//        GO_TO_PRINTER(), QUEUE_PRINTER(), PRINTING(),
        GO_TO_COLLAB(), WAIT_FOR_COLLAB(), COLLABORATE(),
//        TECHNICAL_GO_PRINTER(), FIX_PRINTER(), FIX_CUBICLE(),
        GO_MEETING(), WAIT_MEETING(), MEETING(), GOING_DISPENSER(),
        GETTING_WATER(), GOING_FRIDGE(), GETTING_FOOD(), TAKING_BREAK();
//        GO_TO_WAIT_AREA(),WAIT_FOR_VACANT();

        final int ID;

        Name() {
            this.ID = this.ordinal();
        }

        public int getID() {
            return ID;
        }
    }

}
