package com.socialsim.model.core.agent;

import com.socialsim.controller.graphics.agent.AgentGraphic;
import com.socialsim.model.core.environment.patchobject.PatchObject;
import com.socialsim.model.simulator.Simulator;

import java.time.LocalTime;
import java.util.Objects;

public class Agent extends PatchObject {

    // VARIABLES
    public static int originValue = 22;


    private static int idCtr = 0;
    public static int agentCount = 0;
    public static int directorCount = 0;
    public static int facultyCount = 0;
    public static int studentCount = 0;
    public static int maintenanceCount = 0;
    public static int guardCount = 0;
    private final int id;
    private final Agent.Type type;
    private Agent.Gender gender;
    private int team;

    private LocalTime timeIn;
    private LocalTime timeOut;


    private Agent.AgeGroup ageGroup = null;
    private Agent.Persona persona = null;

    private PersonaActionGroup personaActionGroup = null;
    private boolean inOnStart;

    private Agent.EnergyProfile energyProfile = null;
    private int tempPreference = 19;

    private final AgentGraphic agentGraphic;
    private AgentMovement agentMovement;
    public static final AgentFactory agentFactory;

    static {
        agentFactory = new AgentFactory();
    }



    // CONSTRUCTOR
    private Agent(Agent.Type type, boolean inOnStart, int team, LocalTime timeIn, LocalTime timeOut, EnergyProfile energyProfile) {
        this.id = idCtr++;
        this.type = type;
        this.team = team;
        this.inOnStart = inOnStart;
        this.gender = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean() ? Agent.Gender.FEMALE : Agent.Gender.MALE;
        this.timeIn = timeIn;
        this.timeOut = timeOut;


        // Director
        if(this.type == Type.DIRECTOR) {
            this.ageGroup = AgeGroup.FROM_25_TO_54;
            this.gender = Gender.MALE;
            this.persona = Persona.DIRECTOR;
            this.personaActionGroup = PersonaActionGroup.DIRECTOR;
            this.energyProfile = EnergyProfile.GREEN;
            if (energyProfile.equals(EnergyProfile.GREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(originValue, 26);
            else if (energyProfile.equals(EnergyProfile.NONGREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19, 23);
            else
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19,26);
        }



        // Faculty
        else if (this.type == Type.FACULTY) {
            this.ageGroup = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean() ? Agent.AgeGroup.FROM_25_TO_54 : Agent.AgeGroup.FROM_55_TO_64;
            this.gender = Gender.MALE;
            boolean isStrict = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean();
            if (isStrict) {
                this.persona = Persona.STRICT_FACULTY;
                this.personaActionGroup = PersonaActionGroup.STRICT_FACULTY;
            }
            else {
                this.persona = Persona.APP_FACULTY;
                this.personaActionGroup = PersonaActionGroup.APP_FACULTY;
            }
            this.energyProfile = energyProfile;
            if (energyProfile.equals(EnergyProfile.GREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(originValue, 26);
            else if (energyProfile.equals(EnergyProfile.NONGREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19, 23);
            else
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19,26);
        }



        // Student
        else if (this.type == Type.STUDENT) {
            this.ageGroup = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean() ? AgeGroup.FROM_15_TO_24 : AgeGroup.FROM_25_TO_54;

            boolean isIntrovert = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean();
            if (isIntrovert) {
                this.persona = Persona.INT_STUDENT;
                this.personaActionGroup = PersonaActionGroup.INT_STUDENT;
            }
            else {
                this.persona = Persona.EXT_STUDENT;
                this.personaActionGroup = PersonaActionGroup.EXT_STUDENT;
            }
            this.energyProfile = energyProfile;
            if (energyProfile.equals(EnergyProfile.GREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(originValue, 26);
            else if (energyProfile.equals(EnergyProfile.NONGREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19, 23);
            else
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19,26);
        }




        // Maintenance
        else if (this.type == Type.MAINTENANCE) {
            this.ageGroup = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean() ? AgeGroup.FROM_25_TO_54 : AgeGroup.FROM_55_TO_64;
            this.persona = Persona.MAINTENANCE;
            this.personaActionGroup = PersonaActionGroup.MAINTENANCE;

            this.energyProfile = EnergyProfile.GREEN;
            if (energyProfile.equals(EnergyProfile.GREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(originValue, 26);
            else if (energyProfile.equals(EnergyProfile.NONGREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19, 23);
            else
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19,26);
        }



        // Guard
        else if (this.type == Agent.Type.GUARD) {
            this.ageGroup = Simulator.RANDOM_NUMBER_GENERATOR.nextBoolean() ? Agent.AgeGroup.FROM_25_TO_54 : Agent.AgeGroup.FROM_55_TO_64;
            this.persona = Agent.Persona.GUARD;
            this.personaActionGroup = PersonaActionGroup.GUARD;

            this.energyProfile = EnergyProfile.GREEN;
            if (energyProfile.equals(EnergyProfile.GREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(originValue, 26);
            else if (energyProfile.equals(EnergyProfile.NONGREEN))
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19, 23);
            else
                this.tempPreference = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(19,26);
        }


        this.agentGraphic = new AgentGraphic(this);
    }



    public static EnergyProfile energyProfilePicker(double greenProb, double nonGreenProb, double neutralProb) {

        // Generate a random number between 0 and 1
        double randomValue = Simulator.RANDOM_NUMBER_GENERATOR.nextDouble();

        // Assign energy profile based on the random value and probabilities
        if (randomValue < greenProb) {
            return EnergyProfile.GREEN;

        } else if (randomValue < greenProb + nonGreenProb) {
            return EnergyProfile.NONGREEN;
        } else {
            return EnergyProfile.NEUTRAL;
        }
    }

    // METHODS
    public static void clearAgentCounts() {
        idCtr = 0;
        agentCount = 0;
        directorCount = 0;
        facultyCount = 0;
        studentCount = 0;
        maintenanceCount = 0;
        guardCount = 0;
    }



    // GETTERS
    public int getId() {
        return id;
    }

    public Agent.Type getType() {
        return type;
    }

    public Agent.Gender getGender() {
        return gender;
    }

    public Agent.AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public Agent.Persona getPersona() {
        return persona;
    }

    public Agent.PersonaActionGroup getPersonaActionGroup() {
        return personaActionGroup;
    }

    public boolean getInOnStart() {
        return inOnStart;
    }

    public int getTeam() {
        return team;
    }

    public AgentGraphic getAgentGraphic() {
        return agentGraphic;
    }

    public AgentMovement getAgentMovement() {
        return agentMovement;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public Agent.EnergyProfile getEnergyProfile() {
        return energyProfile;
    }

    public int getTempPreference() {
        return tempPreference;
    }


    // SETTERS
    public void setAgentMovement(AgentMovement agentMovement) {
        this.agentMovement = agentMovement;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }



    // OVERRIDE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;

        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }



    // ENUM
    public enum Type {
        DIRECTOR, FACULTY, STUDENT, MAINTENANCE, GUARD
    }

    public enum Gender {
        FEMALE, MALE
    }

    public enum AgeGroup {
        YOUNGER_THAN_OR_14,
        FROM_15_TO_24,
        FROM_25_TO_54,
        FROM_55_TO_64, OLDER_THAN_OR_65
    }

    public enum Persona {
        DIRECTOR(0),
        STRICT_FACULTY(1), APP_FACULTY(2),
        INT_STUDENT(3), EXT_STUDENT(4),
        MAINTENANCE(5),
        GUARD(6);

        private final int ID;

        Persona(int ID){
            this.ID = ID;
        }

        public int getID() {
            return ID;
        }
    }

    public enum PersonaActionGroup {
        DIRECTOR(),
        STRICT_FACULTY(),
        APP_FACULTY(),
        INT_STUDENT(),
        EXT_STUDENT(),
        MAINTENANCE(),
        GUARD();

        final int ID;
        PersonaActionGroup(){
            this.ID = this.ordinal();
        }
        public int getID() {
            return ID;
        }
    }

    public enum EnergyProfile {
        GREEN(0),
        NONGREEN(1),
        NEUTRAL(2);

        private final int ID;

        EnergyProfile(int ID){
            this.ID = ID;
        }

        public int getID() {
            return ID;
        }
    }

    // INNER STATIC CLASS
    public static class AgentFactory extends ObjectFactory {
        public static Agent create(Agent.Type type, boolean inOnStart, int team, LocalTime timeIn, LocalTime timeOut, EnergyProfile energyProfile) {
            return new Agent(type, inOnStart, team, timeIn, timeOut, energyProfile);
        }
    }



}