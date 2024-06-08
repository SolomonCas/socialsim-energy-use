package com.socialsim.model.core.environment;

import com.socialsim.model.core.agent.Action;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.core.environment.position.MatrixPosition;
import com.socialsim.model.simulator.Simulator;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;

import static com.socialsim.model.core.agent.Agent.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Environment extends BaseObject implements Serializable {

    // VARIABLES

    // General
    private final int rows;
    private final int columns;
    private final Patch[][] patches;



    // Environment
    private final List<Wall> walls;
    private final List<MeetingRoom> meetingRooms;
    private final List<HumanExpRoom> humanExpRooms;
    private final List<DataCollectionRoom> dataCollectionRooms;
    private final List<ResearchCenter> researchCenters;
    private final List<FacultyRoom> facultyRooms;
    private final List<StorageRoom> storageRooms;
    private final List<Pantry> pantries;
    private final List<LearningSpace> learningSpaces;
    private final List<ControlCenter> controlCenters;
    private final List<DataCenter> dataCenters;
    private final List<SoloRoom> soloRooms;
    private final List<StaffArea> staffAreas;
    private final List<Bathroom> bathrooms;
    private final List<Reception> receptions;
    private final List<ReceptionQueue> receptionQueues;
    private final List<WaterDispenserQueue> waterDispenserQueues;
    private final List<FridgeQueue> fridgeQueues;
    private final List<BathroomQueue> bathroomQueues;
    private final List<DirectorRoom> directorRooms;
    private final List<BreakArea> breakAreas;
    private final List<Floor> floors;





    // Amenities
    private final SortedSet<Patch> amenityPatchSet;

    private final List<Gate> gates;
    private final List<Cabinet> cabinets;
    private final List<Chair> chairs;
    private final List<CollabDesk> collabDesks;
    private final List<Couch> couches;
    private final List<Cubicle> cubicles;
    private final List<Door> doors;
    private final List<MeetingDesk> meetingDesks;
    private final List<OfficeDesk> officeDesks;
    private final List<Plant> plants;
    private final List<ReceptionTable> receptionTables;
    private final List<PantryTable> pantryTables;
    private final List<Table> tables;
    private final List<Sink> sinks;
    private final List<Server> servers;
    private final List<Toilet> toilets;
    private final List<Trash> trashes;
    private final List<Fridge> fridges;
    private final List<WaterDispenser> waterDispensers;
    private final List<Whiteboard> whiteboards;
    private final List<MaleBathroomDoor> maleBathroomDoors;
    private final List<FemaleBathroomDoor> femaleBathroomDoors;
    private final List<MainEntranceDoor> mainEntranceDoors;
    private final List<StorageCabinet> storageCabinets;
    private final List<OfficeSink> officeSinks;
    private final List<OfficeToilet> officeToilets;
    private final List<DirectorTable> directorTables;

    


    // Agents
    private final CopyOnWriteArrayList<Agent> agents;
    private final SortedSet<Patch> agentPatchSet;



    // IOS Levels
    public static CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> defaultIOS;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> IOSScales;



    // Edit Interaction Type Chances
    public static CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> defaultInteractionTypeChances;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Double>> IOSInteractionChances;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> interactionTypeChances;


    // Social Interactions
    private int nonverbalMean;
    private int nonverbalStdDev;
    private int cooperativeMean;
    private int cooperativeStdDev;
    private int exchangeMean;
    private int exchangeStdDev;
    private int fieldOfView;


    // Static
    public static final Environment.Factory officeFactory;

    static {
        officeFactory = new Environment.Factory();
    }




    // CONSTRUCTOR
    public Environment(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.patches = new Patch[rows][columns];
        initializePatches();
        
        // Environment
        this.walls = Collections.synchronizedList(new ArrayList<>());
        this.meetingRooms = Collections.synchronizedList(new ArrayList<>());
        this.humanExpRooms = Collections.synchronizedList(new ArrayList<>());
        this.dataCollectionRooms = Collections.synchronizedList(new ArrayList<>());
        this.researchCenters = Collections.synchronizedList(new ArrayList<>());
        this.facultyRooms = Collections.synchronizedList(new ArrayList<>());
        this.storageRooms = Collections.synchronizedList(new ArrayList<>());
        this.pantries = Collections.synchronizedList(new ArrayList<>());
        this.learningSpaces = Collections.synchronizedList(new ArrayList<>());
        this.controlCenters = Collections.synchronizedList(new ArrayList<>());
        this.dataCenters = Collections.synchronizedList(new ArrayList<>());
        this.soloRooms = Collections.synchronizedList(new ArrayList<>());
        this.staffAreas = Collections.synchronizedList(new ArrayList<>());
        this.bathrooms = Collections.synchronizedList(new ArrayList<>());
        this.receptions = Collections.synchronizedList(new ArrayList<>());
        this.receptionQueues = Collections.synchronizedList(new ArrayList<>());
        this.directorRooms = Collections.synchronizedList(new ArrayList<>());
        this.breakAreas = Collections.synchronizedList(new ArrayList<>());
        this.floors = Collections.synchronizedList(new ArrayList<>());
        this.bathroomQueues = Collections.synchronizedList(new ArrayList<>());
        this.waterDispenserQueues = Collections.synchronizedList(new ArrayList<>());
        this.fridgeQueues = Collections.synchronizedList(new ArrayList<>());

        // Amenities
        this.amenityPatchSet = Collections.synchronizedSortedSet(new TreeSet<>());

        this.gates = Collections.synchronizedList(new ArrayList<>());
        this.cabinets = Collections.synchronizedList(new ArrayList<>());
        this.chairs = Collections.synchronizedList(new ArrayList<>());
        this.collabDesks = Collections.synchronizedList(new ArrayList<>());
        this.couches = Collections.synchronizedList(new ArrayList<>());
        this.cubicles = Collections.synchronizedList(new ArrayList<>());
        this.doors = Collections.synchronizedList(new ArrayList<>());
        this.meetingDesks = Collections.synchronizedList(new ArrayList<>());
        this.officeDesks = Collections.synchronizedList(new ArrayList<>());
        this.plants = Collections.synchronizedList(new ArrayList<>());
        this.receptionTables = Collections.synchronizedList(new ArrayList<>());
        this.tables = Collections.synchronizedList(new ArrayList<>());
        this.pantryTables = Collections.synchronizedList(new ArrayList<>());
        this.sinks = Collections.synchronizedList(new ArrayList<>());
        this.servers = Collections.synchronizedList(new ArrayList<>());
        this.toilets = Collections.synchronizedList(new ArrayList<>());
        this.trashes = Collections.synchronizedList(new ArrayList<>());
        this.fridges = Collections.synchronizedList(new ArrayList<>());
        this.waterDispensers = Collections.synchronizedList(new ArrayList<>());
        this.whiteboards = Collections.synchronizedList(new ArrayList<>());
        this.maleBathroomDoors = Collections.synchronizedList(new ArrayList<>());
        this.femaleBathroomDoors = Collections.synchronizedList(new ArrayList<>());
        this.mainEntranceDoors = Collections.synchronizedList(new ArrayList<>());
        this.storageCabinets = Collections.synchronizedList(new ArrayList<>());
        this.officeSinks = Collections.synchronizedList(new ArrayList<>());
        this.officeToilets = Collections.synchronizedList(new ArrayList<>());
        this.directorTables = Collections.synchronizedList(new ArrayList<>());

        // Agents
        this.agents = new CopyOnWriteArrayList<>();
        this.agentPatchSet = Collections.synchronizedSortedSet(new TreeSet<>());



        // IOS

        this.IOSInteractionChances = new CopyOnWriteArrayList<>();
    }






    // METHODS: ENVIRONMENT
    private void initializePatches() {
        MatrixPosition matrixPosition;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                matrixPosition = new MatrixPosition(row, column);
                patches[row][column] = new Patch(this, matrixPosition);
            }
        }
    }


    // METHODS: AMENITIES






    // METHODS: AGENTS

    public CopyOnWriteArrayList<Agent> getMovableAgents() {
        CopyOnWriteArrayList<Agent> movable = new CopyOnWriteArrayList<>();
        for (Agent agent: getAgents()){
            if (agent.getAgentMovement() != null)
                movable.add(agent);
        }
        return movable;
    }

    public CopyOnWriteArrayList<Agent> getUnspawnedWorkingAgents() {
        CopyOnWriteArrayList<Agent> unspawned = new CopyOnWriteArrayList<>();
        ArrayList<Type> working = new ArrayList<>(Arrays.asList(Type.GUARD, Type.MAINTENANCE, Type.DIRECTOR, Type.FACULTY, Type.STUDENT));
        for (Agent agent: getAgents()){
            if (agent.getAgentMovement() == null && working.contains(agent.getType()))
                unspawned.add(agent);
        }
        return unspawned;
    }

    // Get all team members that are present in the office
    public ArrayList<Agent> getPresentTeamMembers(int team){
        ArrayList<Agent> agents = new ArrayList<>();
        for (Agent agent: getAgents()){
            if (agent.getAgentMovement() != null && agent.getTeam() == team){
                agents.add(agent);
            }
        }

        return agents;
    }

    public ArrayList<Agent> getTeamMembers(int team){
        ArrayList<Agent> agents = new ArrayList<>();
        for (Agent agent: getAgents()){
            if (agent.getTeam() == team){
                agents.add(agent);
            }
        }
        return agents;
    }

    // Developer Note: The timeIn and timeOut of janitors and guard are set this way for this is based on our interview
    // where they specifically indicate what time they enter and exit the office
    public void createInitialAgentDemographics(){
        int offset = 30; // equivalent to 30 mins
//        Agent janitor = Agent.AgentFactory.create(Type.MAINTENANCE, true, 0, LocalTime.of(9,0 + Simulator.rollIntIN(offset)), LocalTime.of(18,0 + Simulator.rollIntIN(offset)));
//        this.getAgents().add(janitor);
//
//        Agent janitor2 = Agent.AgentFactory.create(Type.MAINTENANCE, true, 0, LocalTime.of(9,0 + Simulator.rollIntIN(offset)), LocalTime.of(18,0 + Simulator.rollIntIN(offset)));
//        this.getAgents().add(janitor2);

        for (int i = 0; i < 4; i++) {
            Agent janitor = Agent.AgentFactory.create(Type.STUDENT, true, 0, LocalTime.of(9,0, i), LocalTime.of(10,0));
            this.getAgents().add(janitor);
        }

//        Agent guard = Agent.AgentFactory.create(Type.GUARD, true, 0, LocalTime.of(7,30 + Simulator.rollIntIN(offset)), LocalTime.of(10,0 + Simulator.rollIntIN(offset)));
//        this.getAgents().add(guard);

//        Agent director = Agent.AgentFactory.create(Type.DIRECTOR, true, 0, 0, 7560);
//        this.getAgents().add(director);
//
//        Agent faculty_1 = Agent.AgentFactory.create(Type.FACULTY, true, 1, 0, 7560);
//        this.getAgents().add(faculty_1);
//
//        for (int i = 0; i < 4; i++){
//            Agent student_1 = Agent.AgentFactory.create(Type.STUDENT, true, 1, LocalTime.of(7,30 + Simulator.rollIntIN(offset)), LocalTime.of(12,0 + Simulator.rollIntIN(offset)));
//            this.getAgents().add(student_1);
//        }

    }

    // METHODS: IOS
    public static void configureDefaultIOS(){
        defaultIOS = new CopyOnWriteArrayList<>();

        for (int i = 0; i < Agent.Persona.values().length; i++){
            CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> personaIOS = new CopyOnWriteArrayList<>();

            for (int j = 0; j < Agent.Persona.values().length; j++){
                Agent.Persona persona1 = Agent.Persona.values()[i];
                Agent.Persona persona2 = Agent.Persona.values()[j];

                switch (persona1){
                    case DIRECTOR -> {
                        switch (persona2) {
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case STRICT_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APP_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case EXT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case STRICT_FACULTY -> {
                        switch (persona2) {
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case STRICT_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            }
                            case APP_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            }
                            case INT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case APP_FACULTY -> {
                        switch (persona2) {
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case STRICT_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            }
                            case APP_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            }
                            case INT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                            }
                            case EXT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                            }
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }

                    case INT_STUDENT -> {
                        switch (persona2){
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case STRICT_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case APP_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }


                    case EXT_STUDENT -> {
                        switch (persona2){
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5)));
                            case STRICT_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case APP_FACULTY -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_STUDENT -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }


                    case MAINTENANCE -> {
                        switch (persona2){
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case STRICT_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APP_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1,2,3)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }

                    case GUARD -> {
                        switch (persona2){
                            case DIRECTOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case STRICT_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APP_FACULTY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_STUDENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MAINTENANCE -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1,2,3)));
                        }
                    }
                }

            }
            defaultIOS.add(personaIOS);
        }
    }
    public void convertIOSToChances(){
        IOSInteractionChances = new CopyOnWriteArrayList<>();
        IOSScales.toString();

        for(int i = 0; i < agents.size(); i++){
            IOSInteractionChances.add(new CopyOnWriteArrayList<>());

            for(int j = 0; j < agents.size(); j++){
                if (i == j){
                    IOSInteractionChances.get(i).add((double) 0);
                }
                else{
                    Agent agent1 = agents.get(i), agent2 = agents.get(j);
                    int IOS;

                    if (   agent1.getPersonaActionGroup() == PersonaActionGroup.STRICT_FACULTY
                        || agent1.getPersonaActionGroup() == PersonaActionGroup.APP_FACULTY
                        || agent1.getPersonaActionGroup() == PersonaActionGroup.INT_STUDENT
                        || agent1.getPersonaActionGroup() == PersonaActionGroup.EXT_STUDENT) {

                        int offset;
                        switch (agent2.getPersona()){
                            case DIRECTOR, STRICT_FACULTY, APP_FACULTY -> offset = 0;
                            case INT_STUDENT -> offset = 1;
                            case EXT_STUDENT -> offset = 2;
                            case MAINTENANCE -> offset = 3;
                            default -> offset = 4;
                        }
                        if (agent2.getTeam() == 0){
                            IOS = IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset).get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset).size()));
                        }
                        else if (agent1.getTeam() > 0 && agent1.getTeam() == agent2.getTeam())
                            IOS = IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset).get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset).size()));
                        else
                            IOS = IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset + 1).get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID() + offset + 1).size()));
                    }

                    else
                        IOS = IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID()).get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(IOSScales.get(agent1.getPersona().getID()).get(agent2.getPersona().getID()).size()));
                    IOSInteractionChances.get(i).add(this.convertToChanceInteraction(IOS));
                }
            }
        }
    }

    public void copyDefaultToIOS() {
        this.IOSScales = new CopyOnWriteArrayList<>();
        for(int i = 0; i < defaultIOS.size(); i++){
            this.IOSScales.add(new CopyOnWriteArrayList<>());
            for(int j = 0; j < defaultIOS.get(i).size(); j++){
                this.IOSScales.get(i).add(new CopyOnWriteArrayList<>());
                for (int k = 0; k < defaultIOS.get(i).get(j).size(); k++){
                    this.IOSScales.get(i).get(j).add(defaultIOS.get(i).get(j).get(k));
                }
            }
        }
    }




    // METHODS: INTERACTIONS

    public double convertToChanceInteraction(int x){
        double CHANCE = ((double) x - 1) / 7 + Simulator.RANDOM_NUMBER_GENERATOR.nextDouble() * 1/7;
        return CHANCE;
    }


    public static void configureDefaultInteractionTypeChances() {
        defaultInteractionTypeChances = new CopyOnWriteArrayList<>();

        for (int i = 0; i < Agent.PersonaActionGroup.values().length; i++){
            CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> interactionChances = new CopyOnWriteArrayList<>();

            for (int j = 0; j < Action.Name.values().length; j++){
                Agent.PersonaActionGroup personaGroup = Agent.PersonaActionGroup.values()[i];
                Action.Name action = Action.Name.values()[j];


                switch (personaGroup){
                    case DIRECTOR -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case STRICT_FACULTY -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 20, 80)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(60, 0, 40)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(60, 0, 40)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case APP_FACULTY -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case INT_STUDENT -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 20, 50)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(60, 0, 40)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(60, 0, 40)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case EXT_STUDENT -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case MAINTENANCE -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 90, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 90, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 90, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case GUARD -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 100, 0)));
                            case GREET_PERSON -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 0, 90)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_FACULTY_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ANSWER_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_BATHROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case RELIEVE_IN_CUBICLE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FIND_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WASH_IN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLAB -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case COLLABORATE  -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MEETING -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }

                }
            }
            defaultInteractionTypeChances.add(interactionChances);
        }
    }

    public void copyDefaultToInteractionTypeChances(){
        this.interactionTypeChances = new CopyOnWriteArrayList<>();
        for(int i = 0; i < defaultInteractionTypeChances.size(); i++){
            this.interactionTypeChances.add(new CopyOnWriteArrayList<>());
            for(int j = 0; j < defaultInteractionTypeChances.get(i).size(); j++){
                this.interactionTypeChances.get(i).add(new CopyOnWriteArrayList<>());
                for (int k = 0; k < defaultInteractionTypeChances.get(i).get(j).size(); k++){
                    this.interactionTypeChances.get(i).get(j).add(defaultInteractionTypeChances.get(i).get(j).get(k));
                }
            }
        }
    }

    public void setPlantWatered(Patch plantWatered) {
        for(int i = 0; i < this.getPlants().size(); i++) {
            if(this.getPlants().get(i).getAmenityBlocks()
                    .getFirst().getPatch().equals(plantWatered)) {
                this.getPlants().get(i).setWatered(true);
            }
        }
    }


    // GETTERS: GENERAL
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Patch getPatch(Coordinates coordinates) {
        return getPatch((int) (coordinates.getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS), (int) (coordinates.getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS));
    }

    public Patch getPatch(MatrixPosition matrixPosition) {
        return getPatch(matrixPosition.getRow(), matrixPosition.getColumn());
    }

    public Patch getPatch(int row, int column) {
        return patches[row][column];
    }

    public Patch[][] getPatches() {
        return this.patches;
    }


    // GETTERS: ENVIRONMENT
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }
    public List<HumanExpRoom> getHumanExpRooms() {
        return humanExpRooms;
    }
    public List<DataCollectionRoom> getDataCollectionRooms() {
        return dataCollectionRooms;
    }
    public List<ResearchCenter> getResearchCenters() {
        return researchCenters;
    }
    public List<FacultyRoom> getFacultyRooms() {
        return facultyRooms;
    }
    public List<StorageRoom> getStorageRooms(){
        return storageRooms;
    }
    public List<Pantry> getPantries() {
        return pantries;
    }
    public List<LearningSpace> getLearningSpaces() {
        return learningSpaces;
    }
    public List<ControlCenter> getControlCenters() {
        return controlCenters;
    }
    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }
    public List<SoloRoom> getSoloRooms() {
        return soloRooms;
    }
    public List<StaffArea> getStaffRooms() {
        return staffAreas;
    }
    public List<Bathroom> getBathrooms() {
        return bathrooms;
    }
    public List<Reception> getReceptions() {
        return receptions;
    }
    public List<ReceptionQueue> getReceptionQueues() {
        return receptionQueues;
    }
    public List<WaterDispenserQueue> getWaterDispenserQueues() {
        return waterDispenserQueues;
    }
    public List<FridgeQueue> getFridgeQueues() {
        return fridgeQueues;
    }
    public List<DirectorRoom> getDirectorRooms() {
        return directorRooms;
    }
    public List<BreakArea> getBreakAreas() {
        return breakAreas;
    }
    public List<Floor> getFloors() {
        return floors;
    }
    public List<Wall> getWalls() {
        return walls;
    }



    // GETTERS: AMENITIES
    public SortedSet<Patch> getAmenityPatchSet() {
        return amenityPatchSet;
    }
    public List<Gate> getGates() {
        return gates;
    }
    public List<Cabinet> getCabinets() {
        return cabinets;
    }
    public List<Chair> getChairs() {
        return chairs;
    }
    public List<CollabDesk> getCollabDesks() {
        return collabDesks;
    }
    public List<Couch> getCouches() {
        return couches;
    }
    public List<Cubicle> getCubicles() {
        return cubicles;
    }
    public List<Door> getDoors() {
        return doors;
    }
    public List<MeetingDesk> getMeetingDesks() {
        return meetingDesks;
    }
    public List<OfficeDesk> getOfficeDesks() {
        return officeDesks;
    }
    public List<Plant> getPlants() {
        return plants;
    }
    public List<ReceptionTable> getReceptionTables() {
        return receptionTables;
    }
    public List<Table> getTables() {
        return tables;
    }
    public List<PantryTable> getPantryTables() {
        return pantryTables;
    }
    public List<Sink> getSinks() {
        return sinks;
    }
    public List<Server> getServers() {
        return servers;
    }
    public List<Toilet> getToilets() {
        return toilets;
    }
    public List<Trash> getTrashes() {
        return trashes;
    }
    public List<Fridge> getFridges() {
        return fridges;
    }
    public List<WaterDispenser> getWaterDispensers() {
        return waterDispensers;
    }
    public List<Whiteboard> getWhiteboards() {
        return whiteboards;
    }
    public List<MaleBathroomDoor> getMaleBathroomDoors() {
        return maleBathroomDoors;
    }
    public List<FemaleBathroomDoor> getFemaleBathroomDoors() {
        return femaleBathroomDoors;
    }
    public List<MainEntranceDoor> getMainEntranceDoors() {
        return mainEntranceDoors;
    }
    public List<StorageCabinet> getStorageCabinets() {
        return storageCabinets;
    }
    public List<OfficeSink> getOfficeSinks() {
        return officeSinks;
    }
    public List<OfficeToilet> getOfficeToilets() {
        return officeToilets;
    }
    public List<DirectorTable> getDirectorTables() {
        return directorTables;
    }

    public List<? extends Amenity> getAmenityList(Class<? extends Amenity> amenityClass) {
        if (amenityClass == Gate.class) {
            return this.getGates();
        }
        else if (amenityClass == Server.class) {
            return this.getServers();
        }
        else if (amenityClass == Cabinet.class) {
            return this.getCabinets();
        }
        else if (amenityClass == Chair.class) {
            return this.getChairs();
        }
        else if (amenityClass == PantryTable.class) {
            return this.getPantryTables();
        }
        else if (amenityClass == CollabDesk.class) {
            return this.getCollabDesks();
        }
        else if (amenityClass == Couch.class) {
            return this.getCouches();
        }
        else if (amenityClass == Cubicle.class) {
            return this.getCubicles();
        }
        else if (amenityClass == Door.class) {
            return this.getDoors();
        }
        else if (amenityClass == MainEntranceDoor.class) {
            return this.getMainEntranceDoors();
        }
        else if (amenityClass == MaleBathroomDoor.class) {
            return this.getMaleBathroomDoors();
        }
        else if (amenityClass == FemaleBathroomDoor.class) {
            return this.getFemaleBathroomDoors();
        }
        else if(amenityClass == Fridge.class){
            return this.getFridges();
        }
        else if (amenityClass == MeetingDesk.class) {
            return this.getMeetingDesks();
        }
        else if (amenityClass == OfficeDesk.class) {
            return this.getOfficeDesks();
        }
        else if (amenityClass == Plant.class) {
            return this.getPlants();
        }
        else if (amenityClass == ReceptionTable.class) {
            return this.getReceptionTables();
        }
        else if (amenityClass == Table.class) {
            return this.getTables();
        }
        else if (amenityClass == Sink.class) {
            return this.getSinks();
        }
        else if (amenityClass == Toilet.class) {
            return this.getToilets();
        }
        else if(amenityClass == WaterDispenser.class){
            return this.getWaterDispensers();
        }
        else if(amenityClass == Trash.class) {
            return this.getTrashes();
        }
        else if(amenityClass == Whiteboard.class) {
            return this.getWhiteboards();
        }
        else if(amenityClass == OfficeSink.class) {
            return this.getOfficeSinks();
        }
        else if(amenityClass == OfficeToilet.class) {
            return this.getOfficeToilets();
        }
        else {
            return null;
        }
    }





    // GETTERS: AGENTS
    public CopyOnWriteArrayList<Agent> getAgents() {
        return agents;
    }
    public SortedSet<Patch> getAgentPatchSet() {
        return agentPatchSet;
    }



    // GETTERS: IOS
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Double>> getIOS() {
        return this.IOSInteractionChances;
    }

    public CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> getIOSScales(){
        return this.IOSScales;
    }




    // GETTERS: INTERACTIONS
    public CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> getInteractionTypeChances(){
        return this.interactionTypeChances;
    }
    public int getNonverbalMean() {
        return nonverbalMean;
    }

    public void setNonverbalMean(int nonverbalMean) {
        this.nonverbalMean = nonverbalMean;
    }

    public int getNonverbalStdDev() {
        return nonverbalStdDev;
    }

    public void setNonverbalStdDev(int nonverbalStdDev) {
        this.nonverbalStdDev = nonverbalStdDev;
    }

    public int getCooperativeMean() {
        return cooperativeMean;
    }

    public void setCooperativeMean(int cooperativeMean) {
        this.cooperativeMean = cooperativeMean;
    }

    public int getCooperativeStdDev() {
        return cooperativeStdDev;
    }

    public void setCooperativeStdDev(int cooperativeStdDev) {
        this.cooperativeStdDev = cooperativeStdDev;
    }

    public int getExchangeMean() {
        return exchangeMean;
    }

    public void setExchangeMean(int exchangeMean) {
        this.exchangeMean = exchangeMean;
    }

    public int getExchangeStdDev() {
        return exchangeStdDev;
    }

    public void setExchangeStdDev(int exchangeStdDev) {
        this.exchangeStdDev = exchangeStdDev;
    }

    public int getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(int fieldOfView) {
        this.fieldOfView = fieldOfView;
    }







    // SETTERS: IOS
    public void setIOSScales(CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> IOSScales){
        this.IOSScales = IOSScales;
    }



    // INNER STATIC CLASS
    public static class Factory extends BaseObject.ObjectFactory {
        public static Environment create(int rows, int columns) {
            return new Environment(rows, columns) {
            };
        }
    }
}