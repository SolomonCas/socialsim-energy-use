package com.socialsim.model.core.environment;

import com.socialsim.model.core.agent.Action;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
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

    private final List<Floor> floors;
    private final List<Divider> dividers;
    private final List<Bathroom> bathrooms;
    private final List<Reception> receptions;
    private final List<StaffArea> staffAreas;
    private final List<SoloRoom> soloRooms;
    private final List<DataCenter> dataCenters;
    private final List<MESA> MESAs;
    private final List<ControlCenter> controlCenters;
    private final List<LearningSpace> learningSpaces;
    private final List<BreakerRoom> breakerRooms;
    private final List<MeetingRoom> meetingRooms;
    private final List<ConferenceRoom> conferenceRooms;
    private final List<StorageRoom> storageRooms;
    private final List<FacultyRoom> facultyRooms;
    private final List<ResearchCenter> researchCenters;
    private final List<DataCollectionRoom> dataCollectionRooms;
    private final List<HumanExpRoom> humanExpRooms;
    private final List<Clinic> clinics;
    private final List<DirectorRoom> directorRooms;
    private final List<Pantry> pantries;

    private final List<ReceptionQueue> receptionQueues;
    private final List<WaterDispenserQueue> waterDispenserQueues;
    private final List<RefrigeratorQueue> refrigeratorQueues;
    private final List<CoffeeQueue> coffeeQueues;
    private final List<BathroomQueue> bathroomQueues;




    // Amenities
    private final SortedSet<Patch> amenityPatchSet;

    private final List<Cubicle> cubicles;
    private final List<MESATable> mesaTables;
    private final List<ResearchTable> researchTables;
    private final List<MeetingTable> meetingTables;
    private final List<LearningTable> learningTables;
    private final List<PantryTable> pantryTables;
    private final List<DirectorTable> directorTables;
    private final List<Table2x2> table2x2s;
    private final List<ReceptionTable> receptionTables;
    private final List<SoloTable> soloTables;

    private final List<HumanExpTable> humanExpTables;
    private final List<DataCollTable> dataCollTables;
    private final List<Whiteboard> whiteboards;
    private final List<Refrigerator> refrigerators;
    private final List<PantryCabinet> pantryCabinets;
    private final List<CoffeeMakerBar> coffeeMakerBars;
    private final List<KettleBar> kettleBars;
    private final List<MicrowaveBar> microwaveBars;
    private final List<WindowBlinds> windowBlinds;



    private final List<Chair> chairs;
    private final List<Elevator> elevators;
    private final List<CabinetDrawer> cabinetDrawers;
    private final List<Couch> couches;
    private final List<Plant> plants;
    private final List<Sink> sinks;
    private final List<OfficeSink> officeSinks;
    private final List<Server> servers;
    private final List<Toilet> toilets;
    private final List<OfficeToilet> officeToilets;
    private final List<TrashCan> trashCans;
//    private final List<Box> boxes;
    private final List<WaterDispenser> waterDispensers;
    private final List<Storage> storages;
    private final List<Aircon> aircons;
    private final List<Light> lights;
    private final List<Switch> switches;
    private final Set<Amenity> usedAmenities;


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

    //ACTIVE CYCLE RELATED
    private int activeCycleTimerRefrigerator = 0;
    private int activeCycleTimerDispenser = 0;
    private double decayRateRef = 0.5;
    private double decayRateDispenser = 0.5;

    // Static
    public static final Factory officeFactory;

    static {
        officeFactory = new Factory();
    }




    // CONSTRUCTOR
    public Environment(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.patches = new Patch[rows][columns];
        initializePatches();

        // Environment
        this.floors = Collections.synchronizedList(new ArrayList<>());
        this.dividers = Collections.synchronizedList(new ArrayList<>());
        this.bathrooms = Collections.synchronizedList(new ArrayList<>());
        this.receptions = Collections.synchronizedList(new ArrayList<>());
        this.staffAreas = Collections.synchronizedList(new ArrayList<>());
        this.soloRooms = Collections.synchronizedList(new ArrayList<>());
        this.dataCenters = Collections.synchronizedList(new ArrayList<>());
        this.MESAs = Collections.synchronizedList(new ArrayList<>());
        this.controlCenters = Collections.synchronizedList(new ArrayList<>());
        this.learningSpaces = Collections.synchronizedList(new ArrayList<>());
        this.breakerRooms = Collections.synchronizedList(new ArrayList<>());
        this.meetingRooms = Collections.synchronizedList(new ArrayList<>());
        this.conferenceRooms = Collections.synchronizedList(new ArrayList<>());
        this.facultyRooms = Collections.synchronizedList(new ArrayList<>());
        this.storageRooms = Collections.synchronizedList(new ArrayList<>());
        this.researchCenters = Collections.synchronizedList(new ArrayList<>());
        this.dataCollectionRooms = Collections.synchronizedList(new ArrayList<>());
        this.humanExpRooms = Collections.synchronizedList(new ArrayList<>());
        this.clinics = Collections.synchronizedList(new ArrayList<>());
        this.directorRooms = Collections.synchronizedList(new ArrayList<>());
        this.pantries = Collections.synchronizedList(new ArrayList<>());

        this.receptionQueues = Collections.synchronizedList(new ArrayList<>());
        this.waterDispenserQueues = Collections.synchronizedList(new ArrayList<>());
        this.refrigeratorQueues = Collections.synchronizedList(new ArrayList<>());
        this.bathroomQueues = Collections.synchronizedList(new ArrayList<>());
        this.coffeeQueues = Collections.synchronizedList(new ArrayList<>());

        this.usedAmenities = Collections.synchronizedSet(new HashSet<>());



        // Amenities
        this.amenityPatchSet = Collections.synchronizedSortedSet(new TreeSet<>());

        this.elevators = Collections.synchronizedList(new ArrayList<>());

        this.cubicles = Collections.synchronizedList(new ArrayList<>());
        this.mesaTables = Collections.synchronizedList(new ArrayList<>());
        this.researchTables = Collections.synchronizedList(new ArrayList<>());
        this.meetingTables = Collections.synchronizedList(new ArrayList<>());
        this.learningTables = Collections.synchronizedList(new ArrayList<>());
        this.pantryTables = Collections.synchronizedList(new ArrayList<>());
        this.directorTables = Collections.synchronizedList(new ArrayList<>());
        this.table2x2s = Collections.synchronizedList(new ArrayList<>());
        this.receptionTables = Collections.synchronizedList(new ArrayList<>());
        this.soloTables = Collections.synchronizedList(new ArrayList<>());
        this.humanExpTables = Collections.synchronizedList(new ArrayList<>());
        this.dataCollTables = Collections.synchronizedList(new ArrayList<>());
        this.whiteboards = Collections.synchronizedList(new ArrayList<>());
        this.refrigerators = Collections.synchronizedList(new ArrayList<>());
        this.trashCans = Collections.synchronizedList(new ArrayList<>());
//        this.boxes = Collections.synchronizedList(new ArrayList<>());
        this.couches = Collections.synchronizedList(new ArrayList<>());
        this.waterDispensers = Collections.synchronizedList(new ArrayList<>());
        this.plants = Collections.synchronizedList(new ArrayList<>());
        this.pantryCabinets = Collections.synchronizedList(new ArrayList<>());
        this.coffeeMakerBars = Collections.synchronizedList(new ArrayList<>());
        this.kettleBars = Collections.synchronizedList(new ArrayList<>());
        this.microwaveBars = Collections.synchronizedList(new ArrayList<>());
        this.windowBlinds = Collections.synchronizedList(new ArrayList<>());







        this.chairs = Collections.synchronizedList(new ArrayList<>());
        this.cabinetDrawers = Collections.synchronizedList(new ArrayList<>());
        this.sinks = Collections.synchronizedList(new ArrayList<>());
        this.officeSinks = Collections.synchronizedList(new ArrayList<>());
        this.servers = Collections.synchronizedList(new ArrayList<>());
        this.toilets = Collections.synchronizedList(new ArrayList<>());
        this.officeToilets = Collections.synchronizedList(new ArrayList<>());
        this.storages = Collections.synchronizedList(new ArrayList<>());
        this.aircons = Collections.synchronizedList(new ArrayList<>());
        this.lights = Collections.synchronizedList(new ArrayList<>());
        this.switches = Collections.synchronizedList(new ArrayList<>());

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
    public CopyOnWriteArrayList<Agent> getPresentTeamMembers(int team){
        CopyOnWriteArrayList<Agent> agents = new CopyOnWriteArrayList<>();
        for (Agent agent: getAgents()){
            if (agent.getAgentMovement() != null && agent.getTeam() == team){
                agents.add(agent);
            }
        }

        return agents;
    }

    // Developer Note: The timeIn and timeOut of janitors and guard are set this way for this is based on our interview
    // where they specifically indicate what time they enter and exit the office
    public void createInitialAgentDemographics(double green, double nonGreen, double neutral, int numOfStudent, int numOfFaculty, int numOfTeams){
        int offset = 30; // equivalent to 30 mins
        // Calculate the number of students per team
        int studentsPerTeam = Math.min(numOfStudent / numOfTeams, 4);
        int remainingStudents = numOfStudent % numOfTeams;


        for (int team = 0; team < numOfTeams; team++) {
            int studentsInThisTeam = studentsPerTeam + (remainingStudents > 0 ? 1 : 0);
            remainingStudents--;

            for (int i = 0; i < studentsInThisTeam; i++) {
                Agent agent = AgentFactory.create(Type.STUDENT, true, team, LocalTime.of(7, 0), LocalTime.of(20, 0), Agent.energyProfilePicker(green, nonGreen, neutral));
                this.getAgents().add(agent);
            }
        }

//        for (int i = 0; i < numOfFaculty; i++) {
//            Agent agent = AgentFactory.create(Type.FACULTY, true, 0, LocalTime.of(7,0, i), LocalTime.of(20,0), Agent.energyProfilePicker(green, nonGreen, neutral));
//            this.getAgents().add(agent);
//        }
//
//
//        for (int i = 0; i < 1; i++) {
//            Agent agent = AgentFactory.create(Type.MAINTENANCE, true, 0, LocalTime.of(7,0, i), LocalTime.of(20,0), Agent.energyProfilePicker(green, nonGreen, neutral));
//            this.getAgents().add(agent);
//        }
//
//        for (int i = 0; i < 1; i++) {
//            Agent agent = AgentFactory.create(Type.GUARD, true, 0, LocalTime.of(7,0, i), LocalTime.of(20,0), Agent.energyProfilePicker(green, nonGreen, neutral));
//            this.getAgents().add(agent);
//        }

    }

    // METHODS: IOS
    public static void configureDefaultIOS(){
        defaultIOS = new CopyOnWriteArrayList<>();

        for (int i = 0; i < Persona.values().length; i++){
            CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> personaIOS = new CopyOnWriteArrayList<>();

            for (int j = 0; j < Persona.values().length; j++){
                Persona persona1 = Persona.values()[i];
                Persona persona2 = Persona.values()[j];

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

        for (int i = 0; i < PersonaActionGroup.values().length; i++){
            CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> interactionChances = new CopyOnWriteArrayList<>();

            for (int j = 0; j < Action.Name.values().length; j++){
                PersonaActionGroup personaGroup = PersonaActionGroup.values()[i];
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
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
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
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 60)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 50)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 20, 70)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 60)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 60)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 60)));
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
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 40)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 40)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 40)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 40)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 50, 40)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(30, 30, 40)));
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
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(40, 30, 30)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 50, 50)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 40, 40)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case MAINTENANCE -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(10, 40, 50)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 0, 50)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case GOING_DISPENSER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_WATER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_FRIDGE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case GETTING_FOOD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TAKING_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_WAIT_AREA -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_VACANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                        }
                    }
                    case GUARD -> {
                        switch(action){
                            case LEAVE_OFFICE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(50, 10, 40)));
                            case GO_TO_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_BREAK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EAT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case EXIT_LUNCH -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_TO_RECEPTION_QUEUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case FILL_UP_NAME -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GUARD_STAY_PUT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 70, 30)));
                            case MAINTENANCE_CLEAN_TOILET -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_CLEAN_SINK -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAINTENANCE_WATER_PLANT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case TURN_ON_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case TURN_ON_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case TURN_OFF_AC -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case TURN_OFF_LIGHT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case SET_AC_TO_COOL -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case SET_AC_TO_WARM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(20, 30, 50)));
                            case OPEN_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case CLOSE_BLINDS -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STATION -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_DIRECTOR -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_GUARD -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_STUDENT -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_FACULTY -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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
                            case WAIT_FOR_COLLEAGUE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case INSPECTING_ROOM -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GO_TO_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case ASK_MAINTENANCE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case GOING_COFFEEMAKER -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case USE_MICROWAVE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
                            case MAKE_COFFEE -> interactionChances.add(new CopyOnWriteArrayList<>(List.of(0, 0, 0)));
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

    public void setPlantWatered(Patch plantWatered, boolean isWatered) {
        for(int i = 0; i < this.getPlants().size(); i++) {
            if(this.getPlants().get(i).getAmenityBlocks()
                    .getFirst().getPatch().equals(plantWatered)) {
                this.getPlants().get(i).setWatered(isWatered);
            }
        }
    }

    public void setOfficeToiletCleaned(Patch toiletCleaned, boolean isCleaned) {
        for(int i = 0; i < this.getOfficeToilets().size(); i++) {
            if(this.getOfficeToilets().get(i).getAmenityBlocks()
                    .getFirst().getPatch().equals(toiletCleaned)) {
                this.getOfficeToilets().get(i).setClean(isCleaned);
            }
        }
    }

    public void setOfficeSinkCleaned(Patch sinkCleaned, boolean isCleaned) {
        for(int i = 0; i < this.getOfficeSinks().size(); i++) {
            if(this.getOfficeSinks().get(i).getAmenityBlocks()
                    .getFirst().getPatch().equals(sinkCleaned)) {
                this.getOfficeSinks().get(i).setClean(isCleaned);
            }
        }
    }

    //TODO: coolingtime will differ based on whether there are nearby groups of people minimum of 4
    public void tempChanger(){
        for(Aircon aircon : this.getAircons()) {
            int closeAgentCount = 0;
            for(Agent agent : this.getMovableAgents()){
                for (Amenity.AmenityBlock attractor : aircon.getAttractors()) {
                    if (agent.getAgentMovement() != null && agent.getAgentMovement().getRoutePlan().isAtDesk()) {
                        double distanceToAircon = Coordinates.distance(agent.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                        if(distanceToAircon < aircon.getCoolingRange()){
                            closeAgentCount++;
                            break;
                        }
                    }
                }
            }

//            System.out.println("close agent count: "+closeAgentCount);
            int coolingTicks = 0;
            //IF TEMP IS GOING HIGHER OR HEATING

            if(aircon.getRoomTemp() < aircon.getAirconTemp() && aircon.isOn()){
                if(closeAgentCount < 4){
                    aircon.setInActiveCycle(false);
                    coolingTicks = 24;
                }
                else{
                    aircon.setInActiveCycle(true);
                    coolingTicks = 20;
                }
                System.out.println("is aircon active cycle: "+ aircon.isInActiveCycle());
                //THIS MEANS THAT 2 MINUTES OR GREATER HAS PASSED
                if(coolingTimer(aircon, coolingTicks)){
                    System.out.println("HEATING");
                    int newTemp = aircon.getRoomTemp();
                    newTemp++;

                    aircon.setRoomTemp(newTemp);
                    aircon.setCoolingTimeInTicks(24);
                }
            }//TEMP IS LOWERING OR COOLING
            else if(aircon.getRoomTemp() > aircon.getAirconTemp() && aircon.isOn()){
                if(closeAgentCount < 4)
                {
                    aircon.setInActiveCycle(false);
                    coolingTicks = 20;
                }
                else{
                    aircon.setInActiveCycle(true);
                    coolingTicks = 42;
                }
                System.out.println("is aircon active cycle: "+ aircon.isInActiveCycle());
                //THIS MEANS THAT 1 MINUTES OR GREATER HAS PASSED
                if(coolingTimer(aircon, coolingTicks)){
                    System.out.println("COOLING ");
                    int newTemp = aircon.getRoomTemp();
                    newTemp --;

                    aircon.setRoomTemp(newTemp);
                    aircon.setCoolingTimeInTicks(12);
                }
            }
        }
    }

    public boolean coolingTimer(Aircon aircon, int duration) {
        if (aircon.getCoolingTimeInTicks() <= 0) {
            aircon.setCoolingTimeInTicks(duration); // set cool down duration
            return true;
        }
        if(aircon.isInActiveCycle()){
            Simulator.setTotalWattageCount(Simulator.getAirconWattageActive());
        }
        aircon.setCoolingTimeInTicks(aircon.getCoolingTimeInTicks() - 1);
        return false;
    }

    public boolean activeCycleTimerRefrigerator() {
//        System.out.println("ACTIVE CYCLE TIMER: "+ activeCycleTimerRefrigerator);
        if (this.activeCycleTimerRefrigerator > 0) {
            activeCycleTimerRefrigerator--;
            return true;
        }
        return false;
    }

    public boolean activeCycleTimerDispenser() {
//        System.out.println("ACTIVE CYCLE TIMER: "+ activeCycleTimerDispenser);
        if (this.activeCycleTimerDispenser > 0) {
            activeCycleTimerDispenser--;
            return true;
        }

        return false;
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
    public List<Floor> getFloors() {
        return floors;
    }
    public List<Divider> getDividers() {
        return dividers;
    }
    public List<Bathroom> getBathrooms() {
        return bathrooms;
    }
    public List<Reception> getReceptions() {
        return receptions;
    }
    public List<StaffArea> getStaffRooms() {
        return staffAreas;
    }
    public List<SoloRoom> getSoloRooms() {
        return soloRooms;
    }
    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }
    public List<MESA> getMESAs() {
        return MESAs;
    }
    public List<ControlCenter> getControlCenters() {
        return controlCenters;
    }
    public List<LearningSpace> getLearningSpaces() {
        return learningSpaces;
    }
    public List<BreakerRoom> getBreakerRooms() {
        return breakerRooms;
    }
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }
    public List<ConferenceRoom> getConferenceRooms() {
        return conferenceRooms;
    }
    public List<StorageRoom> getStorageRooms(){
        return storageRooms;
    }
    public List<FacultyRoom> getFacultyRooms() {
        return facultyRooms;
    }
    public List<ResearchCenter> getResearchCenters() {
        return researchCenters;
    }
    public List<DataCollectionRoom> getDataCollectionRooms() {
        return dataCollectionRooms;
    }
    public List<HumanExpRoom> getHumanExpRooms() {
        return humanExpRooms;
    }
    public List<Clinic> getClinics() {
        return clinics;
    }
    public List<DirectorRoom> getDirectorRooms() {
        return directorRooms;
    }
    public List<Pantry> getPantries() {
        return pantries;
    }


    public List<ReceptionQueue> getReceptionQueues() {
        return receptionQueues;
    }
    public List<WaterDispenserQueue> getWaterDispenserQueues() {
        return waterDispenserQueues;
    }
    public List<RefrigeratorQueue> getRefrigeratorQueues() {
        return refrigeratorQueues;
    }
    public List<BathroomQueue> getBathroomQueues() {
        return bathroomQueues;
    }

    public List<CoffeeQueue> getCoffeeQueues() {
        return coffeeQueues;
    }

    // GETTERS: AMENITIES
    public SortedSet<Patch> getAmenityPatchSet() {
        return amenityPatchSet;
    }

    public List<Cubicle> getCubicles() {
        return cubicles;
    }

    public List<MESATable> getMesaTables() {
        return mesaTables;
    }

    public List<ResearchTable> getResearchTables() {
        return researchTables;
    }
    public List<MeetingTable> getMeetingTables() {
        return meetingTables;
    }
    public List<LearningTable> getLearningTables() {
        return learningTables;
    }
    public List<PantryTable> getPantryTables() {
        return pantryTables;
    }
    public List<DirectorTable> getDirectorTables() {
        return directorTables;
    }
    public List<Table2x2> getTable2x2s() {
        return table2x2s;
    }
    public List<Whiteboard> getWhiteboards() {
        return whiteboards;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
    public List<CabinetDrawer> getCabinetDrawers() {
        return cabinetDrawers;
    }
    public List<Couch> getCouches() {
        return couches;
    }
    public List<Plant> getPlants() {
        return plants;
    }
    public List<PantryCabinet> getPantryCabinets() {
        return pantryCabinets;
    }
    public List<CoffeeMakerBar> getCoffeeMakerBars() {
        return coffeeMakerBars;
    }
    public List<KettleBar> getKettleBars() {
        return kettleBars;
    }
    public List<MicrowaveBar> getMicrowaveBars() {
        return microwaveBars;
    }
    public List<WindowBlinds> getWindowBlinds() {
        return windowBlinds;
    }

    public List<ReceptionTable> getReceptionTables() {
        return receptionTables;
    }
    public List<SoloTable> getSoloTables() {
        return soloTables;
    }
    public List<HumanExpTable> getHumanExpTables() {
        return humanExpTables;
    }
    public List<DataCollTable> getDataCollTables() {
        return dataCollTables;
    }
    public List<Refrigerator> getRefrigerators() {
        return refrigerators;
    }


    public List<Chair> getChairs() {
        return chairs;
    }

    public List<Sink> getSinks() {
        return sinks;
    }
    public List<OfficeSink> getOfficeSinks() {
        return officeSinks;
    }
    public List<Server> getServers() {
        return servers;
    }
    public List<Toilet> getToilets() {
        return toilets;
    }
    public List<OfficeToilet> getOfficeToilets() {
        return officeToilets;
    }
    public List<TrashCan> getTrashCans() {
        return trashCans;
    }
//    public List<Box> getBoxes() {
//        return boxes;
//    }
    public List<WaterDispenser> getWaterDispensers() {
        return waterDispensers;
    }
    public List<Storage> getStorages() {
        return storages;
    }
    public List<Aircon> getAircons() {
        return aircons;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Switch> getSwitches() {
        return switches;
    }

    public List<? extends Amenity> getAmenityList(Class<? extends Amenity> amenityClass) {
        if (amenityClass == Elevator.class) {
            return this.getElevators();
        }
        else if (amenityClass == Cubicle.class) {
            return this.getCubicles();
        }
        else if (amenityClass == MESATable.class) {
            return  this.getMesaTables();
        }
        else if (amenityClass == PantryTable.class) {
            return this.getPantryTables();
        }
        else if (amenityClass == DirectorTable.class) {
            return this.getDirectorTables();
        }
        else if (amenityClass == Table2x2.class) {
            return this.getTable2x2s();
        }
        else if (amenityClass == ReceptionTable.class) {
            return this.getReceptionTables();
        }
        else if (amenityClass == WindowBlinds.class) {
            return this.getWindowBlinds();
        }
        else if (amenityClass == MicrowaveBar.class) {
            return this.getMicrowaveBars();
        }

        else if (amenityClass == Server.class) {
            return this.getServers();
        }
        else if (amenityClass == CabinetDrawer.class) {
            return this.getCabinetDrawers();
        }
        else if (amenityClass == Storage.class) {
            return this.getStorages();
        }

        else if (amenityClass == Couch.class) {
            return this.getCouches();
        }
        else if(amenityClass == Refrigerator.class){
            return this.getRefrigerators();
        }
        else if (amenityClass == Plant.class) {
            return this.getPlants();
        }
        else if (amenityClass == Sink.class) {
            return this.getSinks();
        }
        else if (amenityClass == OfficeSink.class) {
            return this.getOfficeSinks();
        }
        else if (amenityClass == Toilet.class) {
            return this.getToilets();
        }
        else if (amenityClass == OfficeToilet.class) {
            return this.getOfficeToilets();
        }
        else if(amenityClass == WaterDispenser.class){
            return this.getWaterDispensers();
        }
        else if(amenityClass == TrashCan.class) {
            return this.getTrashCans();
        }
//        else if(amenityClass == Box.class) {
//            return this.getBoxes();
//        }
        else if(amenityClass == Whiteboard.class) {
            return this.getWhiteboards();
        }
        else if (amenityClass == Aircon.class) {
            return this.getAircons();
        }
        else if (amenityClass == Light.class) {
            return this.getLights();
        }
        else if (amenityClass == Switch.class) {
            return this.getSwitches();
        }
        else {
            return null;
        }
    }

    public Set<Amenity> getUsedAmenities() {
        return usedAmenities;
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

    //GETTERS: TIMERS
    public int getActiveCycleTimerRefrigerator() {
        return activeCycleTimerRefrigerator;
    }

    public int getActiveCycleTimerDispenser() {
        return activeCycleTimerDispenser;
    }

    public double getDecayRateRef() {
        return decayRateRef;
    }

    public double getDecayRateDispenser() {
        return decayRateDispenser;
    }



    // SETTERS: IOS
    public void setIOSScales(CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> IOSScales){
        this.IOSScales = IOSScales;
    }

    // SETTERS: TIMERS
    public void setActiveCycleTimerRefrigerator(int activeCycleTimerRefrigerator){
        this.activeCycleTimerRefrigerator = activeCycleTimerRefrigerator;
    }
    public void setActiveCycleTimerDispenser(int activeCycleTimerDispenser){
        this.activeCycleTimerDispenser = activeCycleTimerDispenser;
    }

    public void setDecayRateRef(double decayRateRef) {
        this.decayRateRef = decayRateRef;
    }

    public void setDecayRateDispenser(double decayRateDispenser) {
        this.decayRateDispenser = decayRateDispenser;
    }



    // INNER STATIC CLASS
    public static class Factory extends ObjectFactory {
        public static Environment create(int rows, int columns) {
            return new Environment(rows, columns) {
            };
        }
    }
}