package com.socialsim.model.core.agent;

import com.socialsim.controller.Main;
import com.socialsim.model.core.agent.pathfinding.AgentPath;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.core.environment.position.Vector;
import com.socialsim.model.simulator.SimulationTime;
import com.socialsim.model.simulator.Simulator;

import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AgentMovement {

    /***** VARIABLES *****/
    public static int defaultNonverbalMean = 1;
    public static int defaultNonverbalStdDev = 1;
    public static int defaultCooperativeMean = 24;
    public static int defaultCooperativeStdDev = 6;
    public static int defaultExchangeMean = 24;
    public static int defaultExchangeStdDev = 6;
    public static int defaultFieldOfView = 30;


    public static final int MAX_COOL_DOWN_DURATION = 1440;
    public static final int MAX_BATHROOM_COOL_DOWN_DURATION = 1440;
    public static final int MAX_BREAK_COOL_DOWN_DURATION = 1440;
    public static final int MAX_REFRIGERATOR_COOL_DOWN_DURATION = 1440;
    public static final int MAX_DISPENSER_COOL_DOWN_DURATION = 1440;
    public static final int MAX_INQUIRE_COOL_DOWN_DURATION = 1440;
    public static final int MAX_AGENT_COOL_DOWN_DURATION = 1440;
    public static final int MAX_CHANGE_THERMAL_COOL_DOWN_DURATION = 1440;
    public static final int MAX_CHANGE_VISUAL_COOL_DOWN_DURATION = 1440;
    public static final int MAX_COFFEE_COOL_DOWN_DURATION = 4320;

    public int coolDown = 0;
    public int changeThermalCoolDown = 0;
    public int changeVisualCoolDown = 0;

    private final Agent parent;
    private final Coordinates position;
    private final Environment environment;
    private final double baseWalkingDistance;
    private double preferredWalkingDistance;
    private double currentWalkingDistance;
    private double proposedHeading;
    private double heading;
    private final int team;
    private Chair assignedSeat;
    private Patch currentPatch;
    private Amenity currentAmenity;
    private Patch goalPatch;
    private Amenity.AmenityBlock goalAttractor;
    private Amenity goalAmenity;
    private QueueingPatchField goalQueueingPatchField;
    private Patch goalNearestQueueingPatch;
    private RoutePlan routePlan;
    private AgentPath currentPath;
    private int stateIndex;
    private int actionIndex;
    private State currentState;
    private Action currentAction;
    private double distanceMovedInTick;
    private int duration;
    private int noMovementCounter;
    private int movementCounter;
    private int noNewPatchesSeenCounter;
    private int newPatchesSeenCounter;
    private boolean isStuck;
    private int stuckCounter;
    private int timeSinceLeftPreviousGoal;
    private final double fieldOfViewAngle;
    private boolean isReadyToFree;
    private final ConcurrentHashMap<Patch, Integer> recentPatches;
    private final List<Vector> repulsiveForceFromAgents;
    private final List<Vector> repulsiveForcesFromObstacles;
    private Vector attractiveForce;
    private Vector motivationForce;
    private boolean isInteracting;
    private boolean isSimultaneousInteractionAllowed;
    private int interactionDuration;
    private InteractionType interactionType;
    private double previousHeading;
    private Agent agentToInquire = null;
    private double workingSeatHeading;
    private double seatHeading;
    private boolean isLeader = false;

    //AIRCON LOGIC STUFF
    private Aircon airconToChange = null;
    private boolean isToCool = false;
    private boolean isToHeat = false;

    private WindowBlinds blindsToOpen = null;
    private Light lightsToOpen = null;
    public enum InteractionType {
        NON_VERBAL, COOPERATIVE, EXCHANGE
    }

    /***** CONSTRUCTOR *****/
    public AgentMovement(Patch spawnPatch, Agent parent, double baseWalkingDistance, Coordinates coordinates, int team, Chair assignedSeat) { // For inOnStart agents
        this.parent = parent;
        this.position = new Coordinates(coordinates.getX(), coordinates.getY());
        this.team = team;
        this.assignedSeat = assignedSeat;

        this.coolDown(MAX_COOL_DOWN_DURATION);
        changeThermalCoolDown = 0;
        changeVisualCoolDown = 0;

        if (assignedSeat != null) {
            switch (assignedSeat.getFacing()) {
                case "NORTH" -> this.workingSeatHeading = Math.toRadians(90.0);
                case "EAST" -> this.workingSeatHeading = Math.toRadians(0.0);
                case "WEST" -> this.workingSeatHeading = Math.toRadians(180.0);
                case "SOUTH" -> this.workingSeatHeading = Math.toRadians(270.0);
            }
        }

        final double interQuartileRange = 0.12;
        this.baseWalkingDistance = baseWalkingDistance + Simulator.RANDOM_NUMBER_GENERATOR.nextGaussian() * interQuartileRange;
        this.preferredWalkingDistance = this.baseWalkingDistance;
        this.currentWalkingDistance = preferredWalkingDistance;

        this.currentPatch = spawnPatch;
        this.currentPatch.getAgents().add(parent);
        this.environment = currentPatch.getEnvironment();

        if (parent.getInOnStart()) {
            this.proposedHeading = Math.toRadians(270.0);
            this.heading = Math.toRadians(270.0);
            this.previousHeading = Math.toRadians(270.0);
            this.fieldOfViewAngle = this.environment.getFieldOfView();
        }
        else {
            this.proposedHeading = Math.toRadians(90.0);
            this.heading = Math.toRadians(90.0);
            this.previousHeading = Math.toRadians(90.0);
            this.fieldOfViewAngle = this.environment.getFieldOfView();
        }


        this.recentPatches = new ConcurrentHashMap<>();
        repulsiveForceFromAgents = new ArrayList<>();
        repulsiveForcesFromObstacles = new ArrayList<>();
        resetGoal();

        this.routePlan = new RoutePlan(parent, environment, currentPatch, team, assignedSeat);
        this.stateIndex = 0;
        this.actionIndex = 0;
        this.currentState = this.routePlan.getCurrentState();
        this.currentAction = this.routePlan.getCurrentState().getActions().get(actionIndex);
        if (!parent.getInOnStart()) {
            this.currentAmenity = environment.getElevators().getFirst(); // Getting Entrance Gate
        }
        if (this.currentAction.getDestination() != null) {
            this.goalAmenity = this.currentAction.getDestination().getAmenityBlock().getParent();
            this.setGoalAttractor(this.goalAmenity.getAttractors().getFirst());
        }
        this.duration = this.currentAction.getDuration();

        this.isInteracting = false;

        this.free();
    }

    /***** METHODS *****/
    public void resetGoal() { // Reset the agent's goal
        this.goalPatch = null;
        this.goalAmenity = null;
        this.goalAttractor = null;
        this.currentPath = null;
        this.currentAmenity = null;
        this.goalQueueingPatchField = null;
        this.goalNearestQueueingPatch = null;
        this.distanceMovedInTick = 0.0;
        this.noMovementCounter = 0;
        this.movementCounter = 0;
        this.noNewPatchesSeenCounter = 0;
        this.newPatchesSeenCounter = 0;
        this.timeSinceLeftPreviousGoal = 0;
        this.recentPatches.clear();
        this.free();
    }

    public void free() {
        this.isStuck = false;
        this.stuckCounter = 0;
        this.noMovementCounter = 0;
        this.noNewPatchesSeenCounter = 0;
        this.currentPath = null;
        this.isReadyToFree = false;
    }

    private boolean hasNoAgent(Patch patch) {
        if (patch == null) {
            return true;
        }

        List<Agent> agentsOnPatchWithoutThisAgent = patch.getAgents();
        agentsOnPatchWithoutThisAgent.remove(this.parent);

        return agentsOnPatchWithoutThisAgent.isEmpty();
    }



    private void updateRecentPatches(Patch currentPatch, final int timeElapsedExpiration) {
        List<Patch> patchesToForget = new ArrayList<>();

        for (Map.Entry<Patch, Integer> recentPatchesAndTimeElapsed : this.recentPatches.entrySet()) {
            this.recentPatches.put(recentPatchesAndTimeElapsed.getKey(), recentPatchesAndTimeElapsed.getValue() + 1);
            if (recentPatchesAndTimeElapsed.getValue() == timeElapsedExpiration) {
                patchesToForget.add(recentPatchesAndTimeElapsed.getKey());
            }
        }

        if (currentPatch != null) {
            this.recentPatches.put(currentPatch, 0);
        }

        for (Patch patchToForget : patchesToForget) {
            this.recentPatches.remove(patchToForget);
        }
    }

    public void rollAgentInteraction(Agent agent){
        double IOS1 = environment.getIOS().get(this.getParent().getId()).get(agent.getId());
        double IOS2 = environment.getIOS().get(agent.getId()).get(this.getParent().getId());
        double CHANCE1 = Simulator.roll();
        double CHANCE2 = Simulator.roll();
        double interactionStdDeviation, interactionMean;
        if (CHANCE1 < IOS1 && CHANCE2 < IOS2){
            CHANCE1 = Simulator.roll() * IOS1;
            CHANCE2 = Simulator.roll() * IOS2;
            double CHANCE = (CHANCE1 + CHANCE2) / 2;
            double CHANCE_NONVERBAL1 = ((double) environment.getInteractionTypeChances().get(this.getParent().getPersonaActionGroup().getID()).get(this.getParent().getAgentMovement().getCurrentAction().getName().getID()).get(0)) / 100,
                    CHANCE_COOPERATIVE1 = ((double) environment.getInteractionTypeChances().get(this.getParent().getPersonaActionGroup().getID()).get(this.getParent().getAgentMovement().getCurrentAction().getName().getID()).get(1)) / 100,
                    CHANCE_EXCHANGE1 = ((double) environment.getInteractionTypeChances().get(this.getParent().getPersonaActionGroup().getID()).get(this.getParent().getAgentMovement().getCurrentAction().getName().getID()).get(2)) / 100,
                    CHANCE_NONVERBAL2 = ((double) environment.getInteractionTypeChances().get(agent.getPersonaActionGroup().getID()).get(agent.getAgentMovement().getCurrentAction().getName().getID()).get(0)) / 100,
                    CHANCE_COOPERATIVE2 = ((double) environment.getInteractionTypeChances().get(agent.getPersonaActionGroup().getID()).get(agent.getAgentMovement().getCurrentAction().getName().getID()).get(1)) / 100,
                    CHANCE_EXCHANGE2 = ((double) environment.getInteractionTypeChances().get(agent.getPersonaActionGroup().getID()).get(agent.getAgentMovement().getCurrentAction().getName().getID()).get(2)) / 100;
            if (CHANCE < (CHANCE_NONVERBAL1 + CHANCE_NONVERBAL2) / 2){
                Simulator.currentNonverbalCount++;
                this.getParent().getAgentMovement().setInteractionType(InteractionType.NON_VERBAL);
                agent.getAgentMovement().setInteractionType(InteractionType.NON_VERBAL);
                interactionMean = getEnvironment().getNonverbalMean();
                interactionStdDeviation = getEnvironment().getNonverbalStdDev();
            }
            else if (CHANCE < (CHANCE_NONVERBAL1 + CHANCE_NONVERBAL2 + CHANCE_COOPERATIVE1 + CHANCE_COOPERATIVE2) / 2){
                Simulator.currentCooperativeCount++;
                this.getParent().getAgentMovement().setInteractionType(InteractionType.COOPERATIVE);
                agent.getAgentMovement().setInteractionType(InteractionType.COOPERATIVE);
                CHANCE1 = Simulator.roll() * IOS1;
                CHANCE2 = Simulator.roll() * IOS2;
                interactionMean = getEnvironment().getCooperativeMean();
                interactionStdDeviation = getEnvironment().getCooperativeStdDev();
            }
            else if (CHANCE < (CHANCE_NONVERBAL1 + CHANCE_NONVERBAL2 + CHANCE_COOPERATIVE1 + CHANCE_COOPERATIVE2 + CHANCE_EXCHANGE1 + CHANCE_EXCHANGE2) / 2){
                Simulator.currentExchangeCount++;
                this.getParent().getAgentMovement().setInteractionType(InteractionType.EXCHANGE);
                agent.getAgentMovement().setInteractionType(InteractionType.EXCHANGE);
                CHANCE1 = Simulator.roll() * IOS1;
                CHANCE2 = Simulator.roll() * IOS2;
                interactionMean = getEnvironment().getExchangeMean();
                interactionStdDeviation = getEnvironment().getExchangeStdDev();
            }
            else{
                return;
            }

            this.isInteracting = true;
            agent.getAgentMovement().setInteracting(true);
            if (this.parent.getType() == Agent.Type.DIRECTOR){
                switch (agent.getType()){
                    case FACULTY -> Simulator.currentDirectorFacultyCount++;
                    case STUDENT -> Simulator.currentDirectorStudentCount++;
                    case MAINTENANCE -> Simulator.currentDirectorMaintenanceCount++;
                    case GUARD -> Simulator.currentDirectorGuardCount++;
                }
            }
            else if (this.parent.getType() == Agent.Type.FACULTY){
                switch (agent.getType()){
                    case DIRECTOR -> Simulator.currentDirectorFacultyCount++;
                    case FACULTY -> Simulator.currentFacultyFacultyCount++;
                    case STUDENT -> Simulator.currentFacultyStudentCount++;
                    case MAINTENANCE -> Simulator.currentFacultyMaintenanceCount++;
                    case GUARD -> Simulator.currentFacultyGuardCount++;
                }
            }
            else if (this.parent.getType() == Agent.Type.STUDENT){
                switch (agent.getType()){
                    case DIRECTOR -> Simulator.currentDirectorStudentCount++;
                    case FACULTY -> Simulator.currentFacultyStudentCount++;
                    case STUDENT -> Simulator.currentStudentStudentCount++;
                    case MAINTENANCE -> Simulator.currentStudentMaintenanceCount++;
                    case GUARD -> Simulator.currentStudentGuardCount++;
                }
            }
            else if (this.parent.getType() == Agent.Type.MAINTENANCE){
                switch (agent.getType()){
                    case DIRECTOR -> Simulator.currentDirectorMaintenanceCount++;
                    case FACULTY -> Simulator.currentFacultyMaintenanceCount++;
                    case STUDENT -> Simulator.currentStudentMaintenanceCount++;
                    case MAINTENANCE -> Simulator.currentMaintenanceMaintenanceCount++;
                    case GUARD -> Simulator.currentMaintenanceGuardCount++;
                }
            }
            else if (this.parent.getType() == Agent.Type.GUARD){
                switch (agent.getType()){
                    case DIRECTOR -> Simulator.currentDirectorGuardCount++;
                    case FACULTY -> Simulator.currentFacultyGuardCount++;
                    case STUDENT -> Simulator.currentStudentGuardCount++;
                    case MAINTENANCE -> Simulator.currentMaintenanceGuardCount++;
                    case GUARD -> Simulator.currentGuardGuardCount++;
                }
            }
            this.interactionDuration = (int) (Math.floor(Simulator.RANDOM_NUMBER_GENERATOR.nextGaussian() * interactionStdDeviation + interactionMean));
            if (this.interactionDuration < 0)
                this.interactionDuration = 0;
            agent.getAgentMovement().setInteractionDuration(this.interactionDuration);
            if (agent.getAgentMovement().getInteractionType() == InteractionType.NON_VERBAL)
                Simulator.averageNonverbalDuration = (Simulator.averageNonverbalDuration * (Simulator.currentNonverbalCount - 1) + this.interactionDuration) / Simulator.currentNonverbalCount;
            else if (agent.getAgentMovement().getInteractionType() == InteractionType.COOPERATIVE)
                Simulator.averageCooperativeDuration = (Simulator.averageCooperativeDuration * (Simulator.currentCooperativeCount - 1) + this.interactionDuration) / Simulator.currentCooperativeCount;
            else if (agent.getAgentMovement().getInteractionType() == InteractionType.EXCHANGE)
                Simulator.averageExchangeDuration = (Simulator.averageExchangeDuration * (Simulator.currentExchangeCount - 1) + this.interactionDuration) / Simulator.currentExchangeCount;
        }
    }
    public void interact(){
        if (this.interactionDuration <= 0){
            this.isInteracting = false;
            this.interactionType = null;
        }
        else{
            this.interactionDuration--;
        }
    }
    public AgentPath computePath(Patch startingPatch, Patch goalPatch, boolean includeStartingPatch, boolean includeGoalPatch) {
        HashSet<Patch> openSet = new HashSet<>();
        HashMap<Patch, Double> gScores = new HashMap<>();
        HashMap<Patch, Double> fScores = new HashMap<>();
        HashMap<Patch, Patch> cameFrom = new HashMap<>();

        for (Patch[] patchRow : startingPatch.getEnvironment().getPatches()) {
            for (Patch patch : patchRow) {
                gScores.put(patch, Double.MAX_VALUE);
                fScores.put(patch, Double.MAX_VALUE);
            }
        }

        gScores.put(startingPatch, 0.0);
        fScores.put(startingPatch, Coordinates.distance(startingPatch, goalPatch));
        openSet.add(startingPatch);

        while (!openSet.isEmpty()) {
            Patch patchToExplore;
            double minimumDistance = Double.MAX_VALUE;
            Patch patchWithMinimumDistance = null;
            for (Patch patchInQueue : openSet) {
                double fScore = fScores.get(patchInQueue);
                if (fScore < minimumDistance) {
                    minimumDistance = fScore;
                    patchWithMinimumDistance = patchInQueue;
                }
            }

            patchToExplore = patchWithMinimumDistance;
            if (patchToExplore != null && patchToExplore.equals(goalPatch)) {
                Stack<Patch> path = new Stack<>();

                if (includeGoalPatch) {
                    path.push(goalPatch);
                }

                double length = 0.0;
                Patch currentPatch = goalPatch;
                while (cameFrom.containsKey(currentPatch)) {
                    Patch previousPatch = cameFrom.get(currentPatch);
                    length += Coordinates.distance(previousPatch.getPatchCenterCoordinates(), currentPatch.getPatchCenterCoordinates());
                    currentPatch = previousPatch;
                    path.push(currentPatch);
                }

                if (!includeStartingPatch) {
                    path.remove(startingPatch);
                }

                return new AgentPath(length, path);
            }
            openSet.remove(patchToExplore);

            List<Patch> patchToExploreNeighbors = patchToExplore.getNeighbors();
            for (Patch patchToExploreNeighbor : patchToExploreNeighbors) {
                if ((patchToExploreNeighbor.getAmenityBlock() == null && patchToExploreNeighbor.getPatchField() == null)
                        || (patchToExploreNeighbor.getPatchField() != null && patchToExploreNeighbor.getPatchField().getKey().getClass() != Divider.class)
                        || (patchToExploreNeighbor.getAmenityBlock() != null && (patchToExploreNeighbor.getAmenityBlock().getParent().getClass() != Light.class || patchToExploreNeighbor.getAmenityBlock().getParent().getClass() != Aircon.class))
                        || ((includeStartingPatch && patchToExplore.equals(startingPatch)) || (includeGoalPatch && patchToExploreNeighbor.equals(goalPatch)))) {
                    double obstacleClosenessPenalty = (patchToExploreNeighbor.getAmenityBlocksAround() + patchToExploreNeighbor.getDividersAround()) * 2.0;
                    double tentativeGScore = gScores.get(patchToExplore) + Coordinates.distance(patchToExplore, patchToExploreNeighbor) + obstacleClosenessPenalty;
                    if (tentativeGScore < gScores.get(patchToExploreNeighbor)) {
                        cameFrom.put(patchToExploreNeighbor, patchToExplore);
                        gScores.put(patchToExploreNeighbor, tentativeGScore);
                        fScores.put(patchToExploreNeighbor, gScores.get(patchToExploreNeighbor) + Coordinates.distance(patchToExploreNeighbor, goalPatch));
                        openSet.add(patchToExploreNeighbor);
                    }
                }
            }
        }

        return null;
    }


    // This is hardcoded specifically for this environment.
    // Developer Note: The group decided that the bathroom layout in Mckinley microcampus shouldn't be edited.
    // Because the bathroom isn't only for DLSU staff and student, but for all people in that floor.
    // Hence, it is outside the design of DLSU Mckinley microcampus and shouldn't be edited.
    // If you are deciding to add more bathrooms inside the DLSU Mckinley microcampus,
    // you are required to change this whatever you want
    public void chooseBathroomQueue(){
        ArrayList<Patch> patchesToConsider = new ArrayList<>();
        if (this.parent.getType() == Agent.Type.DIRECTOR) {
            chooseBathroomGoal(OfficeToilet.class);
        }
        else if (this.getParent().getGender() == Agent.Gender.MALE){
            patchesToConsider.add(environment.getPatch(19, 190));
            this.goalQueueingPatchField = BathroomQueue.bathroomQueueFactory.create(patchesToConsider, null, "bathroomQueue");
            this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getFirst();
            chooseBathroomGoal(Toilet.class);
        }
        else{
            patchesToConsider.add(environment.getPatch(57, 190));
            this.goalQueueingPatchField = BathroomQueue.bathroomQueueFactory.create(patchesToConsider, null, "bathroomQueue");
            this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getFirst();
            chooseBathroomGoal(Toilet.class);
        }


    }
    public void chooseReceptionQueue(){
        int random = Simulator.rollIntIN(environment.getReceptionQueues().size());
        goalQueueingPatchField = environment.getReceptionQueues().get(random);
        this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getLast();
        this.goalAmenity = environment.getReceptionTables().get(random);
        this.goalAttractor = getGoalAmenity().getAttractors().getFirst(); // Needed in chooseNextInPath
        this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed //JIC if needed
    }

    public void chooseCoffeeQueue(){
        int random = Simulator.rollIntIN(environment.getReceptionQueues().size());
        goalQueueingPatchField = environment.getCoffeeQueues().get(random);
        this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getLast();
        this.goalAmenity = environment.getCoffeeMakerBars().get(random);
        this.goalAttractor = getGoalAmenity().getAttractors().getFirst(); // Needed in chooseNextInPath
        this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed //JIC if needed
    }

    public void chooseWaterDispenserQueue(){
        int random = Simulator.rollIntIN(environment.getWaterDispenserQueues().size());
        goalQueueingPatchField = environment.getWaterDispenserQueues().get(random);
        this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getLast();
        this.goalAmenity = environment.getWaterDispensers().get(random);
        this.goalAttractor = getGoalAmenity().getAttractors().getFirst(); // Needed in chooseNextInPath
        this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed //JIC if needed
    }
    public void chooseFridgeQueue(){
        int random = Simulator.rollIntIN(environment.getRefrigeratorQueues().size());
        goalQueueingPatchField = environment.getRefrigeratorQueues().get(random);
        this.goalNearestQueueingPatch = goalQueueingPatchField.getAssociatedPatches().getLast();
        this.goalAmenity = environment.getRefrigerators().get(random);
        this.goalAttractor = getGoalAmenity().getAttractors().getFirst(); // Needed in chooseNextInPath
        this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed //JIC if needed
    }
    public void joinQueue() {
        this.goalQueueingPatchField.getQueueingAgents().add(this.parent);
    }
    public void leaveQueue() {
        this.goalQueueingPatchField.getQueueingAgents().remove(this.parent);
        this.goalQueueingPatchField = null;
        this.currentPath = null; // Reset
    }
    public boolean isFirstInLine() {
        int index = this.goalQueueingPatchField.getQueueingAgents().indexOf(this.parent);
        return index == 0;
    }

    public boolean chooseGoal(Class<? extends Amenity> nextAmenityClass) {
        if (this.goalAmenity == null && this.environment.getAmenityList(nextAmenityClass) != null) {
            List<? extends Amenity> amenityListInFloor = this.environment.getAmenityList(nextAmenityClass);
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();

            for (Amenity amenity : amenityListInFloor) {
                for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                    double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                    distancesToAttractors.put(attractor, distanceToAttractor);
                }
            }

            List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }
            int temp = 0;
            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) { // Look for a vacant amenity
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                temp++;
                if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    getGoalAttractor().setIsReserved(true);
                    return true;
                }else if(temp == sortedDistances.size()){
                    return false;
                }
            }
        }

        return false;
    }

    // Only gets AC that is on (used for turning off ACs)
    public boolean closestAircon() {
        HashMap<Amenity.AmenityBlock, Double> distancesToAircon = new HashMap<>();
        List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

        if(environment.getAircons().isEmpty()){
            //do nothing;
            return false;
        }
        for (Aircon amenity : environment.getAircons()) {
            for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                double distanceToAircon = Coordinates.distance(this.currentPatch, attractor.getPatch());
                distancesToAircon.put(attractor, distanceToAircon);
            }
        }


        //THIS IS FOR SORTING ALL AIRCONS TO NEAREST TO FARTHEST
        list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAircon.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
            public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        //STORE IT BACK TO HASH IN SORTED MODE
        HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
        Set<Aircon> uniqueAircons = new HashSet<>();

        for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
            Aircon airconParent = (Aircon) aa.getKey().getParent();
            if (aa.getValue() <= airconParent.getCoolingRange() && uniqueAircons.add(airconParent)) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }
        }

        // IF NO AIRCON FOUND WITHIN DISTANCE OF < COOLING RANGE
        if(sortedDistances.isEmpty()){
            return false;
        }

        // for each attractor in aircon get it's Patchfield and compare to currentPatch of agent
        for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAirconEntry : sortedDistances.entrySet()) {
            // check if ac is in the same room
            PatchField patchField = distancesToAirconEntry.getKey().getPatch().getPatchField().getKey();
            if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString())) {
                // check if ac is on
                if(((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
                    airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                    return true;
                }
                else {
                    break;
                }
            }
        }
        //if not in same field, find the closest to the agent within the aircon cooling range and do the thermal comfort logic
        // for each attractor get the aircon near the agent
        for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAirconEntry : sortedDistances.entrySet()) {
            if(((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
                airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                return true;
            }
            else {
                break;
            }
        }

        return false;
    }

    // Only gets Lights that is on (used for turning off Lights)
    public boolean closestLight() {
        HashMap<Amenity.AmenityBlock, Double> sortedDistancesLight = getNearLights();

        if (sortedDistancesLight.isEmpty()) {
            return false;
        }

        // check first if there are lights near the agent that is within the same room
        for (Map.Entry<Amenity.AmenityBlock, Double> distancesToLightEntry : sortedDistancesLight.entrySet()) {

            PatchField patchField = distancesToLightEntry.getKey().getPatch().getPatchField().getKey();

            //if same patchfield, check if within the light range to do visual comfort logic
            if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString())) {
                if (((Light) distancesToLightEntry.getKey().getParent()).isOn()) {
                    lightsToOpen = ((Light) distancesToLightEntry.getKey().getParent());
                    return true;
                }
                else {
                    break;
                }
            }

        }
        // check maybe the light is outside the room (this is for the lights for Solo Rooms)
//        for (Map.Entry<Amenity.AmenityBlock, Double> distancesToLightEntry : sortedDistancesLight.entrySet()) {
//            //range of light
//            //IF NOT SAME PATCH FIELD BUT MAYBE CLOSEST TO THE AGENT
//            if (( (Light) distancesToLightEntry.getKey().getParent()).isOn()){
//                lightsToOpen = ((Light) distancesToLightEntry.getKey().getParent());
//                return true;
//            }
//            else {
//                break;
//            }
//        }

        return false;
    }

    public boolean airconChecker(){
        System.out.println("@airconChecker");
        HashMap<Amenity.AmenityBlock, Double> distancesToAircon = new HashMap<>();
        List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();
        isToCool = false;
        isToHeat = false;

        if(environment.getAircons().isEmpty()){
            //do nothing;
            return false;
        }
        for (Aircon amenity : environment.getAircons()) {
            for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                double distanceToAircon = Coordinates.distance(this.currentPatch, attractor.getPatch());
                distancesToAircon.put(attractor, distanceToAircon);
            }
        }


        //THIS IS FOR SORTING ALL AIRCONS TO NEAREST TO FARTHEST
        list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAircon.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
            public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        //STORE IT BACK TO HASH IN SORTED MODE
        HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
        Set<Aircon> uniqueAircons = new HashSet<>();

        for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
            Aircon airconParent = (Aircon) aa.getKey().getParent();
            if (aa.getValue() <= airconParent.getCoolingRange() && uniqueAircons.add(airconParent)) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }
        }

        // IF NO AIRCON FOUND WITHIN DISTANCE OF < COOLING RANGE
        if(sortedDistances.isEmpty()){
            return false;
        }

        double CHANCE = Simulator.roll();
        System.out.println("CHANCE: " + CHANCE);

        if ((this.parent.getEnergyProfile() == Agent.EnergyProfile.GREEN && CHANCE < 0.01)
                || (this.parent.getEnergyProfile() == Agent.EnergyProfile.NEUTRAL && CHANCE < 0.5) || this.parent.getEnergyProfile() == Agent.EnergyProfile.NONGREEN) {
            // for each attractor in aircon get it's Patchfield and compare to currentPatch of agent
            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAirconEntry : sortedDistances.entrySet()) {
                System.out.println("hello preference: "+ this.parent.getTempPreference() + " hello room temp: "+ ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() + "hello aircon temp: "+ ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp());

                // check if ac is in the same room
                PatchField patchField = distancesToAirconEntry.getKey().getPatch().getPatchField().getKey();
                if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString()) && this.currentPatch.getPatchField().getValue().equals(distancesToAirconEntry.getKey().getPatch().getPatchField().getValue())) {
                    // check if ac is on
                    if(((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
                        //SETS TEMP PREFERENCE RANGE TO COMPROMISE WITH OTHER AGENTS TEMP PREFERENCE
                        boolean withinRange = Math.abs(this.parent.getTempPreference() - ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp()) <= 2;
                        boolean withinRangeWithACTemp = Math.abs(this.parent.getTempPreference() - ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp()) <= 2;

                        // if same, check if within the cooling range then do the thermal comfort logic
                        // do thermal comfort logic
                        // CHECKS WHETHER AGENT TEMP PREFERENCE IS WITHIN TO ROOM TEMP RANGE (+2)
                        if(this.parent.getTempPreference() < ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() && !withinRange) {
                            //IF ROOM TEMP IS LOWER, COMPARE TEMP PREFERENCE TO AIRCON TEMP
                            if(this.parent.getTempPreference() < ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp() && !withinRangeWithACTemp){
                                //IF ITS NOT THE SAME, LOWER THE AIRCON TEMP
                                //TODO: PASS THE CHOSEN AIRCON TO AIRCON VARIABLE //DONE
                                airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                                isToCool = true;
                                System.out.println("GUSTO KO BABAAN");
                                return true;
                            }
                            else{
                                //IF IT'S THE SAME, DO NOTHING
                                return false;
                            }
                        }
                        //CHECKS WHETHER AGENT TEMP PREFERENCE IS WITHIN TO ROOM TEMP RANGE (-2)
                        if (this.parent.getTempPreference() > ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() && !withinRange){
                            //IF ROOM TEMP IS LOWER, COMPARE TEMP PREFERENCE TO AIRCON TEMP
                            if(this.parent.getTempPreference() > ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp() && !withinRangeWithACTemp){
                                //IF ITS NOT THE SAME, HIGHER THE AIRCON TEMP
                                //TODO: PASS THE CHOSEN AIRCON TO AIRCON VARIABLE //DONE
                                airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                                isToHeat = true;
                                System.out.println("GUSTO KO TAASAN");
                                return true;
                            }
                            else{
                                //IF IT'S THE SAME, DO NOTHING
                                return false;
                            }
                        }
                        System.out.println("hello me chekcing aircon1");
                    }
                    else {
                        break;
                    }
                }
            }
            //if not in same field, find the closest to the agent within the aircon cooling range and do the thermal comfort logic
            // for each attractor get the aircon near the agent
            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAirconEntry : sortedDistances.entrySet()) {
//            System.out.println("hello preference: "+ this.parent.getTempPreference() + " hello room temp: "+ ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() + "hello aircon temp: "+ ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp());

                if(((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
                    //SETS TEMP PREFERENCE RANGE TO COMPROMISE WITH OTHER AGENTS TEMP PREFERENCE
                    boolean withinRange = Math.abs(this.parent.getTempPreference() - ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp()) <= 2;
                    boolean withinRangeWithACTemp = Math.abs(this.parent.getTempPreference() - ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp()) <= 2;

                    //do thermal comfort logic

                    //CHECKS WHETHER AGENT TEMP PREFERENCE IS WITHIN TO ROOM TEMP RANGE (+2)
                    if(this.parent.getTempPreference() < ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() && !withinRange) {
                        //IF ROOM TEMP IS LOWER, COMPARE TEMP PREFERENCE TO AIRCON TEMP
                        if(this.parent.getTempPreference() < ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp() && !withinRangeWithACTemp){
                            //IF ITS NOT THE SAME, LOWER THE AIRCON TEMP
                            airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                            isToCool = true;
                            System.out.println("GUSTO KO BABAAN");

                            return true;
                        }
                        else{
                            //IF IT'S THE SAME, DO NOTHING
                            return false;
                        }
                    }
                    //CHECKS WHETHER AGENT TEMP PREFERENCE IS WITHIN TO ROOM TEMP RANGE (-2)
                    else if(this.parent.getTempPreference() > ( (Aircon) distancesToAirconEntry.getKey().getParent()).getRoomTemp() && !withinRange){
                        //IF ROOM TEMP IS LOWER, COMPARE TEMP PREFERENCE TO AIRCON TEMP
                        if(this.parent.getTempPreference() > ( (Aircon) distancesToAirconEntry.getKey().getParent()).getAirconTemp() && !withinRangeWithACTemp){
                            //IF ITS NOT THE SAME, HIGHER THE AIRCON TEMP
                            airconToChange = (Aircon) distancesToAirconEntry.getKey().getParent();
                            isToHeat = true;
                            System.out.println("GUSTO KO TAASAN");
                            return true;
                        }
                        else{
                            //IF IT'S THE SAME, DO NOTHING
                            return false;
                        }
                    }
                }
                else {
                    break;
                }
            }
        }



        // maybe the aircon is not on at all
        for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAirconEntry : sortedDistances.entrySet()) {
            PatchField patchField = distancesToAirconEntry.getKey().getPatch().getPatchField().getKey();
            //if same, check if within the cooling range then do the thermal comfort logic
            if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString())) {
                if(!((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
//                    System.out.println("turn on ac");
                    airconToChange = ( (Aircon) distancesToAirconEntry.getKey().getParent());
                    isToCool = false;
                    isToHeat = false;
                    return true;
                }
                else{
                    break;
                }
            }
            else{
                if(!((Aircon) distancesToAirconEntry.getKey().getParent()).isOn()){
                    airconToChange = ( (Aircon) distancesToAirconEntry.getKey().getParent());
                }
                else{
                    break;
                }
            }
        }
        //RETURN TRUE IF AIRCON IS FOUND BUT NOT WITHIN SAME PATCHFIELD
        if(airconToChange != null){
//            System.out.println("turn on ac, but outside patchfield");
            isToCool = false;
            isToHeat = false;
            return true;
        }
        return false;
    }


    private HashMap<Amenity.AmenityBlock, Double> getNearLights(){
        HashMap<Amenity.AmenityBlock, Double> distancesToLight = new HashMap<>();
        List<Map.Entry<Amenity.AmenityBlock, Double> > lightList = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

        // get all lights within distance threshold near agent. sort it. Then remove the values that are
        // outside threshold.

        if(environment.getLights().isEmpty()){
            return null; // no lights
        }
        for (Light amenity : environment.getLights()) {
            for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                double distanceToLight = Coordinates.distance(this.currentPatch, attractor.getPatch());
                distancesToLight.put(attractor, distanceToLight);
            }
        }


        //THIS IS FOR SORTING ALL LIGHTS TO NEAREST TO FARTHEST
        lightList = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToLight.entrySet());

        Collections.sort(lightList, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
            public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });


        // get window blinds within threshold
        HashMap<Amenity.AmenityBlock, Double> sortedDistancesLight = new LinkedHashMap<Amenity.AmenityBlock, Double>();
        for (Map.Entry<Amenity.AmenityBlock, Double> aa : lightList) {
            if (aa.getValue() < ( (Light) aa.getKey().getParent()).getLightRange())
                sortedDistancesLight.put(aa.getKey(), aa.getValue());
        }

        // for each attractor in LIGHTS get it's Patchfield and compare to currentPatch of agent
        return sortedDistancesLight;
    }
    private HashMap<Amenity.AmenityBlock, Double> getNearBlinds() {
        HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
        List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

        for (Amenity amenity : this.environment.getWindowBlinds()) {
            for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                distancesToAttractors.put(attractor, distanceToAttractor);
            }
        }

        list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
            public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
        for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
            if (aa.getValue() < ((WindowBlinds) aa.getKey().getParent()).getLightRange())
                sortedDistances.put(aa.getKey(), aa.getValue());
        }

        return sortedDistances;
    }


    public boolean visualComfortChecker(SimulationTime time){
        System.out.println("@visualComfortChecker");
        HashMap<Amenity.AmenityBlock, Double> sortedDistancesLight = getNearLights();

        HashMap<Amenity.AmenityBlock, Double> sortedDistancesBlinds = getNearBlinds();

        if (sortedDistancesBlinds.isEmpty() && sortedDistancesLight.isEmpty()) {
            return true; // since there are no nearest blinds and light from agent do nothing
        }

        //CHECK IF AGENT IS GREEN, NEUTRAL OR NONGREEN
        //IF GREEN, CHECK BLINDS FIRST THEN LIGHTS IF NO BLINDS WITHIN RANGE FOUND
        double neutralChance = Simulator.roll();
        //FOR GREEN OR NEUTRAL CHANCE
        if((this.parent.getEnergyProfile() == Agent.EnergyProfile.GREEN || (this.parent.getEnergyProfile() == Agent.EnergyProfile.NEUTRAL && neutralChance < 0.5)) && !sortedDistancesBlinds.isEmpty()
                && time.getTime().isBefore(LocalTime.of(16,0))) {
            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistancesBlinds.entrySet()) {
                PatchField patchField = distancesToAttractorEntry.getKey().getPatch().getPatchField().getKey();
                if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString()) && this.currentPatch.getPatchField().getValue().equals(distancesToAttractorEntry.getKey().getPatch().getPatchField().getValue())) {
                    if (((WindowBlinds) distancesToAttractorEntry.getKey().getParent()).isOpened()) {
                        return true;
                    }
                    else {
                        blindsToOpen = ((WindowBlinds) distancesToAttractorEntry.getKey().getParent());
                        return false;
                    }
                }
            }
        }

        if(!sortedDistancesLight.isEmpty()){
            // check first if there are lights near the agent that is within the same room
            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToLightEntry : sortedDistancesLight.entrySet()) {

                PatchField patchField = distancesToLightEntry.getKey().getPatch().getPatchField().getKey();

                //if same patchfield, check if within the light range to do visual comfort logic
                if (this.currentPatch.getPatchField().getKey().toString().equals(patchField.toString()) && this.currentPatch.getPatchField().getValue().equals(distancesToLightEntry.getKey().getPatch().getPatchField().getValue())) {
                    if (( (Light) distancesToLightEntry.getKey().getParent()).isOn()){
                        return true;
                    }
                    else {
                        lightsToOpen = ((Light) distancesToLightEntry.getKey().getParent());
                        return false;
                    }
                }


            }
        }
        return true;
    }

    public boolean chooseAirConGoal () {
        if (goalAmenity == null) {
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

            Aircon closestAircon = airconToChange;
            // get the nearest Aircon to the agent

            if (closestAircon == null) { // failed getting the nearest aircon for some reason
                System.out.println("failed finding aircon");
                return false;
            }

            // for each attractor in aircon get it's Patchfield maybe the switch is in the same room as the aircon
            for (Amenity.AmenityBlock airconAttractor : closestAircon.getAttractors()) {
                PatchField patchField = airconAttractor.getPatch().getPatchField().getKey();
                for (Switch amenity: environment.getSwitches()) {
                    // get switches are for AC
                    if (amenity.getType().equals("AC")) {
                        for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                            // get switches are in the same Patchfield as from the AC
                            if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.toString())) {
                                // get the distance between the switch and the agent
                                // the assumption is that the switch might be near the agent
                                double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                                distancesToAttractors.put(attractor, distanceToAttractor);
                            }

                        }
                    }
                }

                list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

                Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                    public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });

                HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
                for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                    sortedDistances.put(aa.getKey(), aa.getValue());
                }

                if (!sortedDistances.isEmpty()) {
                    for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                        Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                        if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                            this.goalAmenity =  candidateAttractor.getParent();
                            this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                            this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                            getGoalAttractor().setIsReserved(true);

                            return true;
                        }
                    }
                    return false;
                }
            }

            // reuse variables
            distancesToAttractors.clear();
            list.clear();

            // if the aircon switch is not the same room as the aircon maybe it is nearest to the aircon
            for (Amenity.AmenityBlock airconAttractor : closestAircon.getAttractors()) {
                for (Switch amenity: environment.getSwitches()) {
                    // get switches are for AC
                    if (amenity.getType().equals("AC")) {
                        for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                            // get the distance between the switch and the agent
                            // the assumption is that the switch might be near the aircon
                            double distanceToAttractor = Coordinates.distance(airconAttractor.getPatch(), attractor.getPatch());
                            distancesToAttractors.put(attractor, distanceToAttractor);
                        }
                    }
                }
            }

            list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    getGoalAttractor().setIsReserved(true);

                    return true;
                }
            }

            // if not i don't know anymore
            System.out.println("idk");
            return false;

        }

        return false;
    }
    public boolean chooseLightGoal () {
        if (goalAmenity == null) {
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

            Light closestLight = lightsToOpen;

            if (closestLight == null) { // failed getting the nearest light for some reason
                System.out.println("failed finding lights");
                return false;
            }

            // for each attractor in light get it's Patchfield maybe the switch is in the same room as the light
            for (Amenity.AmenityBlock lightAttractor : closestLight.getAttractors()) {
                PatchField patchField = lightAttractor.getPatch().getPatchField().getKey();
                for (Switch amenity: environment.getSwitches()) {
                    // get switches are for LIGHT
                    if (amenity.getType().equals("LIGHT")) {
                        for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                            // get switches are in the same Patchfield as from the LIGHT
                            if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.toString())) {
                                // get the distance between the switch and the agent
                                // the assumption is that the switch might be near the agent
                                double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                                distancesToAttractors.put(attractor, distanceToAttractor);
                            }

                        }
                    }
                }

                list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

                Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                    public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });

                HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
                for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                    sortedDistances.put(aa.getKey(), aa.getValue());
                }

                if (!sortedDistances.isEmpty()) {
                    for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                        Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                        if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                            this.goalAmenity =  candidateAttractor.getParent();
                            this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                            this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                            getGoalAttractor().setIsReserved(true);
                            return true;
                        }
                    }

                    return false;
                }
            }

            // reuse variables
            distancesToAttractors.clear();
            list.clear();

            // if the light switch is not the same room as the light maybe it is nearest to the light
            for (Amenity.AmenityBlock lightAttractor : closestLight.getAttractors()) {
                for (Switch amenity: environment.getSwitches()) {
                    // get switches are for AC
                    if (amenity.getType().equals("LIGHT")) {
                        for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                            // get the distance between the switch and the light
                            // the assumption is that the switch might be near the light
                            double distanceToAttractor = Coordinates.distance(lightAttractor.getPatch(), attractor.getPatch());
                            distancesToAttractors.put(attractor, distanceToAttractor);
                        }
                    }
                }
            }

            list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); // JIC if needed
                    getGoalAttractor().setIsReserved(true);
                    return true;
                }
            }

            // if not i don't know anymore
            System.out.println("idk");
            return false;


        }

        return false;
    }
    public boolean chooseWindowBlindGoal() {
        if (goalAmenity == null) {
            List<WindowBlinds> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            List<Map.Entry<Amenity.AmenityBlock, Double> > list = new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

            for (WindowBlinds amenity : this.environment.getWindowBlinds()) {
                if (this.currentAction.getName() == Action.Name.CLOSE_BLINDS && amenity.isOpened()) {
                    int attractorCount = 0;
                    // This part checks for amenities with more than 1 attractors
                    for(int i = 0; i < amenity.getAttractors().size(); i++) {
                        if (amenity.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                            attractorCount++;
                        }
                    }
                    if(attractorCount == 0) {
                        temp.add(amenity);
                    }
                }
                else if (this.currentAction.getName() == Action.Name.OPEN_BLINDS && !amenity.isOpened()) {
                    int attractorCount = 0;
                    // This part checks for amenities with more than 1 attractors
                    for(int i = 0; i < amenity.getAttractors().size(); i++) {
                        if (amenity.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                            attractorCount++;
                        }
                    }
                    if(attractorCount == 0) {
                        temp.add(amenity);
                    }
                }
            }

            for (Amenity amenity : temp) {
                for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                    double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                    distancesToAttractors.put(attractor, distanceToAttractor);
                }
            }

            list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());


            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    getGoalAttractor().setIsReserved(true);
                    return true;
                }
            }

            return false;


        }

        return false;
    }
    public boolean chooseBathroomGoal(Class<? extends Amenity> nextAmenityClass){
        if(this.goalAmenity == null){
            List<Amenity> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();

            if (parent.getType() == Agent.Type.DIRECTOR) {
                temp.addAll(environment.getAmenityList(nextAmenityClass));
            }
            else if (parent.getType() == Agent.Type.MAINTENANCE && this.currentState.getName() == State.Name.MAINTENANCE_BATHROOM) {
                if (this.currentAction.getName() == Action.Name.MAINTENANCE_CLEAN_TOILET) {
                    for(int i = 0; i < this.environment.getOfficeToilets().size(); i++) {
                        if (!this.environment.getOfficeToilets().get(i).isClean()) {
                            temp.add(this.environment.getOfficeToilets().get(i));
                        }
                    }
                }
                else if (this.currentAction.getName() == Action.Name.MAINTENANCE_CLEAN_SINK) {
                    for(int i = 0; i < this.environment.getOfficeSinks().size(); i++) {
                        if (!this.environment.getOfficeSinks().get(i).isClean()) {
                            temp.add(this.environment.getOfficeSinks().get(i));
                        }
                    }
                }
            }
            else {
                // Only add amenities that aren't reserved
                for (Amenity amenity : environment.getAmenityList(nextAmenityClass)) {

                    if (parent.getGender() == Agent.Gender.MALE) {
                        if (amenity.getAttractors().getFirst().getPatch().getPatchField().getValue().equals("maleBathroom")) {
                            temp.add(amenity);
                        }
                    }
                    else if (parent.getGender() == Agent.Gender.FEMALE) {
                        if (amenity.getAttractors().getFirst().getPatch().getPatchField().getValue().equals("femaleBathroom")) {
                            temp.add(amenity);
                        }
                    }
                }
            }

            for (Amenity amenity : temp) {
                for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                    double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                    distancesToAttractors.put(attractor, distanceToAttractor);
                }
            }

            List<Map.Entry<Amenity.AmenityBlock, Double> > list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());


            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                if (this.currentAction.getName() == Action.Name.GO_TO_WAIT_AREA) {
                    // These are essential for the moveSocialForce function
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    return true;
                }
                else if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;

                    getGoalAttractor().setIsReserved(true);
                    return true;
                }

            }

            return false;
        }

        return false;

    }
    public boolean choosePlantGoal() {
        if (this.goalAmenity == null) {
            List<Amenity> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            for(int i = 0; i < this.environment.getPlants().size(); i++) {
                if (!this.environment.getPlants().get(i).isWatered()) {
                    temp.add(this.environment.getPlants().get(i));
                }
            }


            for (Amenity amenity : temp) {
                for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                    double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                    distancesToAttractors.put(attractor, distanceToAttractor);
                }
            }

            List<Map.Entry<Amenity.AmenityBlock, Double> > list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());


            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();
                if (!candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor; // Needed in chooseNextInPath;
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    getGoalAttractor().setIsReserved(true);
                    return true;
                }
            }

            return false;
        }

        return false;
    }
    public boolean chooseBreakSeat() {
        if (this.goalAmenity == null) {
            List<Amenity> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            HashMap<Amenity.AmenityBlock, Double> amenityAndHeading = new HashMap<>();
            List<Map.Entry<Amenity.AmenityBlock, Double> > list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

            for(int i = 0; i < this.environment.getPantryTables().size(); i++) {
                temp.addAll(this.environment.getPantryTables().get(i).getPantryChairs());
            }

            if (Agent.Type.MAINTENANCE == this.parent.getType()) {
                for (SoloTable amenity : environment.getSoloTables()) {
                    for (Chair soloChair : amenity.getSoloChairs()) {
                        int attractorCount = 0;
                        // This part checks for amenities with more than 1 attractors
                        for(int i = 0; i < soloChair.getAttractors().size(); i++) {
                            if (soloChair.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                                attractorCount++;
                            }
                        }
                        if(attractorCount == 0) {
                            temp.add(soloChair);
                        }
                    }
                }

                for (MESATable amenity : environment.getMesaTables()) {
                    for (Chair mesaChair : amenity.getMesaChairs()) {
                        int attractorCount = 0;
                        // This part checks for amenities with more than 1 attractors
                        for(int i = 0; i < mesaChair.getAttractors().size(); i++) {
                            if (mesaChair.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                                attractorCount++;
                            }
                        }
                        if(attractorCount == 0) {
                            temp.add(mesaChair);
                        }
                    }
                }
            }

          // This can be altered
//            temp.addAll(this.environment.getChairs());

            // Agents are not allowed to work and eat their lunch on the couch
            if(this.getCurrentState().getName() != State.Name.GOING_TO_LUNCH) {
                temp.addAll(this.environment.getCouches());
            }

            for (Amenity amenity : temp) {
                if (amenity instanceof Chair) {
                    switch (((Chair) amenity).getFacing()) {
                        case "NORTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(90.0));
                        case "EAST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(0.0));
                        case "WEST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(180.0));
                        case "SOUTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(270.0));
                    }
                }
                else if (amenity instanceof Couch) {
                    switch (((Couch) amenity).getFacing()) {
                        case "NORTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(90.0));
                        case "EAST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(0.0));
                        case "WEST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(180.0));
                        case "SOUTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(270.0));
                    }
                }
            }
            list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(amenityAndHeading.entrySet());
            Collections.shuffle(list);

            for (Map.Entry<Amenity.AmenityBlock, Double> candidateAttractors: list) {
                for (Amenity amenity : temp) {
                    if (!candidateAttractors.getKey().getPatch().getAmenityBlock().getIsReserved() &&
                            amenity.getAttractors().getFirst().getPatch().equals(candidateAttractors.getKey().getPatch())) {
                        this.goalAmenity = amenity;
                        this.goalAttractor = candidateAttractors.getKey(); // Needed in chooseNextInPath;
                        this.goalPatch = this.goalAttractor.getPatch(); // JIC if needed
                        this.seatHeading = candidateAttractors.getValue();
                        getGoalAttractor().setIsReserved(true);
                        return true;
                    }
                }

            }

            return false;
        }

        return false;
    }
    public boolean chooseWorkingSeat() {
        if (this.assignedSeat == null) {
            List<Chair> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            HashMap<Amenity.AmenityBlock, Double> amenityAndHeading = new HashMap<>();
            List<Map.Entry<Amenity.AmenityBlock, Double> > list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >();

            // Only add amenities that aren't reserved
            if (Agent.Type.FACULTY == this.parent.getType()) {
                for(Cubicle amenity: environment.getCubicles()) {
                    for(Chair chair : amenity.getCubicleChairs()) {
                        int attractorCount = 0;
                        // This part checks for amenities with more than 1 attractors
                        for(int i = 0; i < chair.getAttractors().size(); i++) {
                            if (chair.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                                attractorCount++;
                            }
                        }
                        if(attractorCount == 0) {
                            temp.add(chair);
                        }
                    }
                }


            }
            else if (Agent.Type.STUDENT == this.parent.getType()) {
                for(ResearchTable researchTable: environment.getResearchTables()) {
                    for (Chair researchChair : researchTable.getResearchChairs()) {
                        int attractorCount = 0;
                        // This part checks for amenities with more than 1 attractors
                        for(int i = 0; i < researchChair.getAttractors().size(); i++) {
                            if (researchChair.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                                attractorCount++;
                            }
                        }
                        if(attractorCount == 0) {
                            temp.add(researchChair);
                        }
                    }
                }

                // for people with no group
                if (this.parent.getTeam() == 0) {
                    for (SoloTable amenity : environment.getSoloTables()) {
                        for (Chair soloChair : amenity.getSoloChairs()) {
                            int attractorCount = 0;
                            // This part checks for amenities with more than 1 attractors
                            for(int i = 0; i < soloChair.getAttractors().size(); i++) {
                                if (soloChair.getAttractors().get(i).getPatch().getAmenityBlock().getIsReserved()) {
                                    attractorCount++;
                                }
                            }
                            if(attractorCount == 0) {
                                temp.add(soloChair);
                            }
                        }
                    }
                }
            }


            if (this.team == 0) {
                for (Chair amenity : temp) {
                    switch (amenity.getFacing()) {
                        case "NORTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(90.0));
                        case "EAST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(0.0));
                        case "WEST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(180.0));
                        case "SOUTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(270.0));
                    }
                }
                list =
                        new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(amenityAndHeading.entrySet());
                Collections.shuffle(list);
            }
            else if (this.team > 0){
                // Get the groupmate that already chosen a seat
                Agent agentLeader = null;
                for (Agent agent : environment.getPresentTeamMembers(this.team)) {
                    if (agent != this.parent && agent.getType() == Agent.Type.STUDENT && agent.getAgentMovement().getAssignedSeat() != null && agent.getAgentMovement().isLeader()) {
                        agentLeader = agent;
                    }
                }


                if(agentLeader == null) {
                    if (this.parent.getType() == Agent.Type.STUDENT) {
                        this.setLeader(true);
                    }
                    for (Chair amenity : temp) {
                        switch (amenity.getFacing()) {
                            case "NORTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(90.0));
                            case "EAST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(0.0));
                            case "WEST" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(180.0));
                            case "SOUTH" -> amenityAndHeading.put(amenity.getAttractors().getFirst(), Math.toRadians(270.0));
                        }
                    }
                    list =
                            new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(amenityAndHeading.entrySet());
                    Collections.shuffle(list);
                }
                else {
                    for (Chair amenity : temp) {
                        double distanceToAttractor = Coordinates.distance(agentLeader.getAgentMovement().getAssignedSeat().getAttractors().getFirst().getPatch(),
                                amenity.getAttractors().getFirst().getPatch());
                        distancesToAttractors.put(amenity.getAttractors().getFirst(), distanceToAttractor);
                    }
                    list =
                            new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());
                    list.sort(new Comparator<Map.Entry<Amenity.AmenityBlock, Double>>() {
                        public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1, Map.Entry<Amenity.AmenityBlock, Double> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

                }
            }

//            for (Map.Entry<Amenity.AmenityBlock, Double> candidateAttractors: list) {
//                for (Chair amenity : temp) {
//                    if (!candidateAttractors.getKey().getPatch().getAmenityBlock().getIsReserved() &&
//                            amenity.getAttractors().getFirst().getPatch().equals(candidateAttractors.getKey().getPatch())) {
//                        this.goalAmenity = amenity;
//                        this.goalAttractor = candidateAttractors.getKey(); // Needed in chooseNextInPath;
//                        this.goalPatch = this.goalAttractor.getPatch(); // JIC if needed
//                        switch (amenity.getFacing()) {
//                            case "NORTH" -> this.workingSeatHeading =  Math.toRadians(90.0);
//                            case "EAST" -> this.workingSeatHeading = Math.toRadians(0.0);
//                            case "WEST" -> this.workingSeatHeading = Math.toRadians(180.0);
//                            case "SOUTH" -> this.workingSeatHeading = Math.toRadians(270.0);
//                        }
//                        this.setAssignedSeat(amenity);
//                        this.getRoutePlan().setAgentSeat(this.assignedSeat);
//                        getGoalAttractor().setIsReserved(true);
//                        return true;
//                    }
//                }
//
//            }

            // if no available seats for research tables and solo rooms (if solo)
            // go check for collaboration seats
            return chooseCollaborationChair();
        }

        return false;
    }

    public boolean chooseCollaborationChair() {
        System.out.println("@chooseCollab");
        if (this.goalAmenity == null) {
            List<Chair> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();

            int count = 1;
            int table = -1;
            List<? extends Amenity> amenityListInFloor = this.environment.getLearningTables();

            // if not group not randomize which table to choose
            if (this.team == 0) {
                table = Simulator.rollIntIN(amenityListInFloor.size()) + 1;
            }

            else {
                for (Amenity amenity : amenityListInFloor) {
                    if(amenity.getAmenityBlocks().getFirst().getPatch().getTeam() == this.team){
                        table = count;
                        break;
                    }
                    count++;
                    if(count>Main.simulator.getEnvironment().getLearningTables().size()){
                        count = 0;
                        break;
                    }
                }

                if(table == -1) {
                    for (Amenity amenity : amenityListInFloor) {
                        count++;
                        if(amenity.getAmenityBlocks().getFirst().getPatch().getTeam() == -1){
                            table = count;
                            amenity.getAmenityBlocks().getFirst().getPatch().setTeam(this.team);
                            break;
                        }
                        if(count == Main.simulator.getEnvironment().getLearningTables().size()){
                            break;
                        }
                    }
                }
            }

            if (table != -1) {

                temp.addAll(Main.simulator.getEnvironment().getLearningTables().get(table - 1).getLearningChairs());

                for (Amenity amenity : temp) {
                    for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                        double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                        distancesToAttractors.put(attractor, distanceToAttractor);
                    }
                }

                List<Map.Entry<Amenity.AmenityBlock, Double>> list =
                        new LinkedList<Map.Entry<Amenity.AmenityBlock, Double>>(distancesToAttractors.entrySet());

                list.sort(new Comparator<Map.Entry<Amenity.AmenityBlock, Double>>() {
                    public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1,
                                       Map.Entry<Amenity.AmenityBlock, Double> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });

                for (Chair amenity : temp) {
                    for (Map.Entry<Amenity.AmenityBlock, Double> candidateAttractors: list) {
                        if (!candidateAttractors.getKey().getPatch().getAmenityBlock().getIsReserved() &&
                                amenity.getAttractors().getFirst().getPatch().equals(candidateAttractors.getKey().getPatch())) {
                            this.goalAmenity = amenity;
                            this.goalAttractor = candidateAttractors.getKey(); // Needed in chooseNextInPath;
                            this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                            switch (amenity.getFacing()) {
                                case "NORTH" -> this.workingSeatHeading =  Math.toRadians(90.0);
                                case "EAST" -> this.workingSeatHeading = Math.toRadians(0.0);
                                case "WEST" -> this.workingSeatHeading = Math.toRadians(180.0);
                                case "SOUTH" -> this.workingSeatHeading = Math.toRadians(270.0);
                            }
                            if (this.assignedSeat == null) {
                                this.setAssignedSeat(amenity);
                                this.getRoutePlan().setAgentSeat(this.assignedSeat);
                                getGoalAttractor().setIsReserved(true);
                            }
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        return false;
    }

    public boolean chooseAgentAsGoal() {
        if (goalAmenity == null) {
            List<Agent> candidateAgents = new ArrayList<>();

            if (this.currentState.getName() == State.Name.INQUIRE_GUARD) {
                for(Agent agent : environment.getMovableAgents()) {
                    // check if the agent is not the same agent, if the agent has an assigned seat, if the agent is right
                    // type of agent needed for the given state (i.e. INQUIRE_GUARD)
                    if (agent != this.parent && agent.getAgentMovement().getRoutePlan().isAtDesk() && agent.getType() == Agent.Type.GUARD) {
                        candidateAgents.add(agent);
                    }
                }
                Collections.shuffle(candidateAgents);
            }
            else if (this.currentState.getName() == State.Name.INQUIRE_FACULTY) {
                // if agent is a student and is in a team inquire with the faculty handling the team
                if (this.parent.getType() == Agent.Type.STUDENT && this.team != 0) {
                    for(Agent agent : environment.getPresentTeamMembers(this.team)) {
                        if (agent != this.parent && agent.getAgentMovement().getRoutePlan().isAtDesk() && agent.getType() == Agent.Type.FACULTY) {
                            candidateAgents.add(agent);
                        }
                    }
                }
                // else random faculty
                else {
                    for (Agent agent : environment.getMovableAgents()) {
                        if (agent != this.parent && agent.getAgentMovement().getRoutePlan().isAtDesk() && agent.getType() == Agent.Type.FACULTY) {
                            candidateAgents.add(agent);
                        }
                    }
                }
                Collections.shuffle(candidateAgents);
            }
            else if (this.currentState.getName() == State.Name.INQUIRE_STUDENT) {
                if (this.parent.getTeam() == 0) {
                    for (Agent agent : environment.getMovableAgents()) {
                        if (agent != this.parent && agent.getAgentMovement().getRoutePlan().isAtDesk() && agent.getType() == Agent.Type.STUDENT) {
                            candidateAgents.add(agent);
                        }
                    }
                }

                Collections.shuffle(candidateAgents);
            }
            else if (this.currentState.getName() == State.Name.INQUIRE_MAINTENANCE) {
                for (Agent agent : environment.getMovableAgents()) {
                    if (agent != this.parent && agent.getAgentMovement().getRoutePlan().isAtDesk() && agent.getType() == Agent.Type.MAINTENANCE) {
                        candidateAgents.add(agent);
                    }
                }
                Collections.shuffle(candidateAgents);
            }


            for (Agent agent1 : candidateAgents) {
                // only get agents who is not busy (i.e. can do urgent task )
                if (agent1.getAgentMovement().getRoutePlan().getCanUrgent() && agent1.getAgentMovement().getRoutePlan().isAtDesk()) {
                    this.agentToInquire = agent1;
                    this.goalAmenity =  agent1.getAgentMovement().getAssignedSeat();
                    this.goalAttractor = this.goalAmenity.getAttractors().getFirst();
                    this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
                    agent1.getAgentMovement().getRoutePlan().setCanUrgent(false);
                    return true;
                }
            }

        }
        return false;
    }

    private Coordinates getFuturePosition(double walkingDistance) {
        return getFuturePosition(this.goalAmenity, this.proposedHeading, walkingDistance);
    }

    public Coordinates getFuturePosition(Coordinates startingPosition, double heading, double magnitude) {
        return Coordinates.computeFuturePosition(startingPosition, heading, magnitude);
    }

    public Coordinates getFuturePosition(Amenity goal, double heading, double walkingDistance) {
        double minimumDistance = Double.MAX_VALUE; // Get the nearest attractor to this agent
        double distance;

        Amenity.AmenityBlock nearestAttractor = null;
        for (Amenity.AmenityBlock attractor : goal.getAttractors()) {
            distance = Coordinates.distance(this.position, attractor.getPatch().getPatchCenterCoordinates());
            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestAttractor = attractor;
            }
        }


        assert nearestAttractor != null;

        if (minimumDistance < walkingDistance) {
            return new Coordinates(nearestAttractor.getPatch().getPatchCenterCoordinates().getX(), nearestAttractor.getPatch().getPatchCenterCoordinates().getY());
        }
        else {
            Coordinates futurePosition = this.getFuturePosition(this.position, heading, walkingDistance);
            double newX = futurePosition.getX();
            double newY = futurePosition.getY();

            if (newX < 0) {
                newX = 0.0;
            }
            else if (newX > Main.simulator.getEnvironment().getColumns() - 1) {
                newX = Main.simulator.getEnvironment().getColumns() - 0.5;
            }

            if (newY < 0) {
                newY = 0.0;
            }
            else if (newY > Main.simulator.getEnvironment().getRows()- 1) {
                newY = Main.simulator.getEnvironment().getRows() - 0.5;
            }

            return new Coordinates(newX, newY);
        }
    }

    public void moveSocialForce() {
        final int noNewPatchesSeenTicksThreshold = 2;
        final double noMovementThreshold = 0.01 * this.preferredWalkingDistance;
        final double noNewPatchesSeenThreshold = 2;
        final double slowdownStartDistance = 2.0;
        int numberOfObstacles = 0;
        final double minimumAgentStopDistance = 0.6;
        final double minimumObstacleStopDistance = 0.6;

        // If the agent has been moving a sufficient distance for at least this number of ticks, this passenger will
        // be out of the stuck state, if it was
        final int unstuckTicksThreshold = 20;

        List<Patch> patchesToExplore = this.get7x7Field(this.proposedHeading, true, Math.toRadians(360.0));

        this.repulsiveForceFromAgents.clear();
        this.repulsiveForcesFromObstacles.clear();
        this.attractiveForce = null;
        this.motivationForce = null;

        TreeMap<Double, Patch> obstaclesEncountered = new TreeMap<>();

        List<Vector> vectorsToAdd = new ArrayList<>();

        this.previousHeading = this.heading;

        Coordinates proposedNewPosition = this.getFuturePosition(this.preferredWalkingDistance);
        this.preferredWalkingDistance = this.baseWalkingDistance;

        final double distanceSlowdownStart = 5.0;
        final double speedDecreaseFactor = 0.5;

        double distanceToGoal;

        if (this.goalAttractor != null) {
            distanceToGoal = Coordinates.distance(this.currentPatch, this.goalAttractor.getPatch());
            if (distanceToGoal < distanceSlowdownStart && this.hasClearLineOfSight(this.position, this.goalAttractor.getPatch().getPatchCenterCoordinates(), true)) {
                this.preferredWalkingDistance *= speedDecreaseFactor;
            }
        }

        if (this.isStuck || this.noNewPatchesSeenCounter > noNewPatchesSeenTicksThreshold) {
            this.isStuck = true;
            this.stuckCounter++;
        }

        int agentsProcessed = 0;
        final int agentsProcessedLimit = 5;

        for (Patch patch : patchesToExplore) {
            if (hasObstacle(patch)) {
                numberOfObstacles++;

                double distanceToObstacle = Coordinates.distance(this.position, patch.getPatchCenterCoordinates());
                if (distanceToObstacle <= slowdownStartDistance) {
                    obstaclesEncountered.put(distanceToObstacle, patch);
                }
            }

            if (this.currentState.getName() != State.Name.GOING_TO_RECEPTION && this.currentAction.getName() != Action.Name.GOING_TO_RECEPTION_QUEUE && this.currentAction.getName() != Action.Name.GO_TO_WAIT_AREA) {
                for (Agent otherAgent : patch.getAgents()) {
                    if (agentsProcessed == agentsProcessedLimit) {
                        break;
                    }

                    if (!otherAgent.equals(this.getParent())) {
                        double distanceToOtherAgent = Coordinates.distance(this.position, otherAgent.getAgentMovement().getPosition());

                        if (distanceToOtherAgent <= slowdownStartDistance) {
                            final int maximumAgentCountTolerated = 5;
                            final int minimumAgentCount = 1;
                            final double maximumDistance = 2.0;
                            final int maximumAgentCount = 5;
                            final double minimumDistance = 0.7;

                            double computedMaximumDistance = computeMaximumRepulsionDistance(numberOfObstacles, maximumAgentCountTolerated, minimumAgentCount, maximumDistance, maximumAgentCount, minimumDistance);
                            Vector agentRepulsiveForce = computeSocialForceFromAgent(otherAgent, distanceToOtherAgent, computedMaximumDistance, minimumAgentStopDistance, this.preferredWalkingDistance);
                            this.repulsiveForceFromAgents.add(agentRepulsiveForce);

                            agentsProcessed++;
                        }
                    }
                }
            }
        }

        this.attractiveForce = this.computeAttractiveForce(new Coordinates(this.position), this.proposedHeading, proposedNewPosition, this.preferredWalkingDistance);
        vectorsToAdd.add(attractiveForce);

        vectorsToAdd.addAll(this.repulsiveForceFromAgents);
        Vector partialMotivationForce = Vector.computeResultantVector(new Coordinates(this.position), vectorsToAdd);
        if (partialMotivationForce != null) {
            final int minimumObstacleCount = 1;
            final double maximumDistance = 2.0;
            final int maximumObstacleCount = 2;
            final double minimumDistance = 0.7;
            final int maximumObstacleCountTolerated = 2;
            double computedMaximumDistance = computeMaximumRepulsionDistance(numberOfObstacles, maximumObstacleCountTolerated, minimumObstacleCount, maximumDistance, maximumObstacleCount, minimumDistance);

            int obstaclesProcessed = 0;
            final int obstaclesProcessedLimit = 4;

            for (Map.Entry<Double, Patch> obstacleEntry : obstaclesEncountered.entrySet()) {
                if (obstaclesProcessed == obstaclesProcessedLimit) {
                    break;
                }

                this.repulsiveForcesFromObstacles.add(computeSocialForceFromObstacle(obstacleEntry.getValue(), obstacleEntry.getKey(), computedMaximumDistance, minimumObstacleStopDistance, partialMotivationForce.getMagnitude()));
                obstaclesProcessed++;
            }

            vectorsToAdd.clear();
            vectorsToAdd.add(partialMotivationForce);
            vectorsToAdd.addAll(this.repulsiveForcesFromObstacles);
            this.motivationForce = Vector.computeResultantVector(new Coordinates(this.position), vectorsToAdd); // Finally, compute the final motivation force

            if (this.motivationForce != null) {
                if (this.motivationForce.getMagnitude() > this.preferredWalkingDistance) {
                    this.motivationForce.adjustMagnitude(this.preferredWalkingDistance);
                }

                this.motivationForce.adjustHeading(this.motivationForce.getHeading() + Simulator.RANDOM_NUMBER_GENERATOR.nextGaussian() * Math.toRadians(5));

                try {
                    double newHeading = motivationForce.getHeading(); // Set the new heading
                    Coordinates candidatePosition = this.motivationForce.getFuturePosition();
                    if (hasClearLineOfSight(this.position, candidatePosition, false)) {
                        this.move(candidatePosition);
                    }
                    else {
                        double revisedHeading;
                        Coordinates newFuturePosition;
                        int attempts = 0;
                        final int attemptLimit = 5;
                        boolean freeSpaceFound;

                        do {
                            revisedHeading = (motivationForce.getHeading() + Math.toRadians(180)) % Math.toRadians(360);

                            revisedHeading += Simulator.RANDOM_NUMBER_GENERATOR.nextGaussian() * Math.toRadians(90);
                            revisedHeading %= Math.toRadians(360);

                            newFuturePosition = this.getFuturePosition(this.position, revisedHeading, this.preferredWalkingDistance * 0.25);
                            freeSpaceFound = hasClearLineOfSight(this.position, newFuturePosition, false);

                            attempts++;
                        } while (attempts < attemptLimit && !freeSpaceFound);

                        if (attempts != attemptLimit || freeSpaceFound) {
                            this.move(newFuturePosition);
                        }
                    }

                    if (!this.isStuck || Coordinates.headingDifference(this.heading, newHeading) <= Math.toDegrees(90.0) || this.currentWalkingDistance > noMovementThreshold) {
                        this.heading = newHeading;
                    }
                    this.currentWalkingDistance = motivationForce.getMagnitude();
                    this.distanceMovedInTick = motivationForce.getMagnitude();

                    if (this.currentAction.getName() != Action.Name.WAIT_FOR_VACANT) {
                        if (this.recentPatches.size() <= noNewPatchesSeenThreshold) {
                            this.noNewPatchesSeenCounter++;
                            this.newPatchesSeenCounter = 0;
                        }
                        else {
                            this.noNewPatchesSeenCounter = 0;
                            this.newPatchesSeenCounter++;
                        }
                    }
                    else {
                        if (this.distanceMovedInTick < noMovementThreshold) {
                            this.noMovementCounter++;
                            this.movementCounter = 0;
                        }
                        else {
                            this.noMovementCounter = 0;
                            this.movementCounter++;
                        }
                    }

                    if (
                            this.isStuck && this.newPatchesSeenCounter >= unstuckTicksThreshold
                    ) {
                        this.isReadyToFree = true;
                    }

                    this.timeSinceLeftPreviousGoal++;
                    return;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

        this.stop();
        this.distanceMovedInTick = 0.0;
        this.noMovementCounter++;
        this.movementCounter = 0;
        this.timeSinceLeftPreviousGoal++;


    }

    private double computeMaximumRepulsionDistance(int objectCount, final int maximumObjectCountTolerated, final int minimumObjectCount, final double maximumDistance, final int maximumObjectCount, final double minimumDistance) {
        if (objectCount > maximumObjectCountTolerated) {
            objectCount = maximumObjectCountTolerated;
        }

        final double a = (maximumDistance - minimumDistance) / (minimumObjectCount - maximumDistance);
        final double b = minimumDistance - a * maximumObjectCount;

        return a * objectCount + b;
    }
    private Vector computeSocialForceFromObstacle(Patch wallPatch, final double distanceToObstacle, final double maximumDistance, double minimumDistance, final double maximumMagnitude) {
        final double maximumRepulsionFactor = 1.0;
        final double minimumRepulsionFactor = 0.0;

        Coordinates repulsionVectorStartingPosition = wallPatch.getPatchCenterCoordinates();

        double modifiedDistanceToObstacle;
        modifiedDistanceToObstacle = Math.max(distanceToObstacle, minimumDistance);
        double repulsionMagnitudeCoefficient;
        double repulsionMagnitude;

        repulsionMagnitudeCoefficient = computeRepulsionMagnitudeFactor(modifiedDistanceToObstacle, maximumDistance, minimumRepulsionFactor, minimumDistance, maximumRepulsionFactor);
        repulsionMagnitude = repulsionMagnitudeCoefficient * maximumMagnitude;

        if (this.isStuck) {
            final double factor = 0.05;
            repulsionMagnitude -= this.stuckCounter * factor;
            if (repulsionMagnitude <= 0.0001 * this.preferredWalkingDistance) {
                repulsionMagnitude = 0.0001 * this.preferredWalkingDistance;
            }
        }

        double headingFromOtherObstacle = Coordinates.headingTowards(repulsionVectorStartingPosition, this.position); // Compute the heading from that origin point to this agent
        Coordinates obstacleRepulsionVectorFuturePosition = this.getFuturePosition(repulsionVectorStartingPosition, headingFromOtherObstacle, repulsionMagnitude); // Then compute for a future position given the obstacle's position, the heading, and the magnitude

        return new Vector(repulsionVectorStartingPosition, headingFromOtherObstacle, obstacleRepulsionVectorFuturePosition, repulsionMagnitude); // Finally, given the current position, heading, and future position, create the vector from the obstacle to the current agent
    }
    private void move(Coordinates futurePosition) { // Make the agent move given the future position
        this.setPosition(futurePosition);
    }
    public void stop() { // Have the agent stop
        this.currentWalkingDistance = 0.0;
    }
    public boolean hasReachedNextPatchInPath() { // Check if this agent is on the next patch of its path
        return isOnOrCloseToPatch(this.currentPath.getPath().peek());
    }


    public void reachPatchInPath() {
        Patch nextPatch;

        do {
            this.currentPath.getPath().pop();

            if (!this.currentPath.getPath().isEmpty()) {
                nextPatch = this.currentPath.getPath().peek();
            }
            else {
                break;
            }
        } while (!this.currentPath.getPath().isEmpty() && nextPatch.getAmenityBlocksAround() == 0
                && this.isOnOrCloseToPatch(nextPatch) && this.hasClearLineOfSight(this.position, nextPatch.getPatchCenterCoordinates(), true));
    }

    public void despawn() {
        if (this.currentPatch != null) {
            this.currentPatch.getAgents().remove(this.parent);
            this.getEnvironment().getAgents().remove(this.parent);

            SortedSet<Patch> currentPatchSet = this.getEnvironment().getAgentPatchSet();
            if (currentPatchSet.contains(this.currentPatch) && hasNoAgent(this.currentPatch)) {
                currentPatchSet.remove(this.currentPatch);
            }

            switch (this.getParent().getType()) {
                case DIRECTOR -> Simulator.currentDirectorCount--;
                case FACULTY -> Simulator.currentFacultyCount--;
                case STUDENT -> Simulator.currentStudentCount--;
                case MAINTENANCE -> Simulator.currentMaintenanceCount--;
                case GUARD -> Simulator.currentGuardCount--;
            }
            
        }
    }
    public void faceNextPosition() {
        this.proposedHeading = Coordinates.headingTowards(this.position, this.goalPatch.getPatchCenterCoordinates());
    }
    public boolean chooseNextPatchInPath() {
        boolean wasPathJustGenerated = false;
        final int recomputeThreshold = 10;



        if (this.currentPath == null || this.isStuck && this.noNewPatchesSeenCounter > recomputeThreshold) {
            AgentPath agentPath = null;

            if (this.isStuck && this.noNewPatchesSeenCounter > recomputeThreshold) {
                free();
            }

            if (this.getGoalQueueingPatchField() != null) {
                // Head towards the queue of the goal
                List<Agent> agentsQueueing = this.getGoalQueueingPatchField().getQueueingAgents();

                if (agentsQueueing.isEmpty()) {
                    agentPath = computePath(
                            this.currentPatch,
                            this.goalNearestQueueingPatch,
                            false,
                            false);
                }
                else {
                    // If there are passengers in the queue, this passenger should only follow the last passenger in
                    // that queue if that passenger is assembling
                    // If the last passenger is not assembling, simply head for the goal patch instead
                    Agent lastAgent = agentsQueueing.getLast();

                    if (lastAgent.getAgentMovement().getCurrentAction().getName() == Action.Name.WAIT_FOR_VACANT) {
                        double distanceToGoalPatch = Coordinates.distance(this.currentPatch, this.goalNearestQueueingPatch
                        );

                        double distanceToLastAgent = Coordinates.distance(
                                this.currentPatch,
                                lastAgent.getAgentMovement().getCurrentPatch()
                        );

                        // Head to whichever is closer to this agent, the last passenger, or the nearest queueing
                        // patch
                        if (distanceToGoalPatch <= distanceToLastAgent) {
                            agentPath = computePath(
                                    this.currentPatch,
                                    this.goalNearestQueueingPatch,
                                    false,
                                    false
                            );
                        } else {
                            agentPath = computePath(
                                    this.currentPatch,
                                    lastAgent.getAgentMovement().getCurrentPatch(),
                                    false,
                                    false
                            );
                        }
                    }
                    else {
                        agentPath = computePath(
                                this.currentPatch,
                                this.goalNearestQueueingPatch,
                                false,
                                false
                        );
                    }
                }
            }
            else {
                agentPath = computePath(
                        this.currentPatch,
                        this.goalAttractor.getPatch(),
                        false,
                        true
                );
            }

            if (agentPath != null) {
                this.currentPath = new AgentPath(agentPath);
                wasPathJustGenerated = true;
            }
        }


        if (this.currentPath == null || this.currentPath.getPath().isEmpty() || this.currentAmenity != null) {
            return false;
        }

//        System.out.println("Path");
//        for(Patch patch : this.currentPath.getPath()) {
//            System.out.println("PATCH: " + patch);
//        }

        if (wasPathJustGenerated) {
            Patch nextPatchInPath;

            while (true) {
                nextPatchInPath = this.currentPath.getPath().peek();
                if (!(this.currentPath.getPath().size() > 1 && nextPatchInPath.getAmenityBlocksAround() == 0
                        && this.isOnOrCloseToPatch(nextPatchInPath)
                        && this.hasClearLineOfSight(this.position, nextPatchInPath.getPatchCenterCoordinates(), true))) {
                    break;
                }
                this.currentPath.getPath().pop();
            }
            this.goalPatch = nextPatchInPath;
        }
        else {
            this.goalPatch = this.currentPath.getPath().peek();
        }
        return true;
    }
    private Vector computeSocialForceFromAgent(Agent agent, final double distanceToOtherAgent, final double maximumDistance, final double minimumDistance, final double maximumMagnitude) {
        final double maximumRepulsionFactor = 1.0;
        final double minimumRepulsionFactor = 0.0;

        Coordinates agentPosition = agent.getAgentMovement().getPosition();

        double modifiedDistanceToObstacle = Math.max(distanceToOtherAgent, minimumDistance);
        double repulsionMagnitudeCoefficient;
        double repulsionMagnitude;

        repulsionMagnitudeCoefficient = computeRepulsionMagnitudeFactor(modifiedDistanceToObstacle, maximumDistance, minimumRepulsionFactor, minimumDistance, maximumRepulsionFactor);
        repulsionMagnitude = repulsionMagnitudeCoefficient * maximumMagnitude;

        if (this.isStuck) {
            final double factor = 0.05;
            repulsionMagnitude -= this.stuckCounter * factor;
            if (repulsionMagnitude <= 0.0001 * this.preferredWalkingDistance) {
                repulsionMagnitude = 0.0001 * this.preferredWalkingDistance;
            }
        }

        double headingFromOtherAgent = Coordinates.headingTowards(agentPosition, this.position);
        Coordinates agentRepulsionVectorFuturePosition = this.getFuturePosition(agentPosition, headingFromOtherAgent, repulsionMagnitude);

        return new Vector(agentPosition, headingFromOtherAgent, agentRepulsionVectorFuturePosition, repulsionMagnitude);
    }
    private double computeRepulsionMagnitudeFactor(final double distance, final double maximumDistance, final double minimumRepulsionFactor, final double minimumDistance, final double maximumRepulsionFactor) {
        double differenceOfSquaredDistances = Math.pow(maximumDistance, 2.0) - Math.pow(minimumDistance, 2.0);
        double productOfMaximumRepulsionAndMinimumDistance = Math.pow(maximumRepulsionFactor, 2.0) * Math.pow(minimumDistance, 2.0);

        double a = (Math.pow(maximumDistance, 2.0) * (minimumRepulsionFactor * Math.pow(maximumDistance, 2.0) - minimumRepulsionFactor * Math.pow(minimumDistance, 2.0) + productOfMaximumRepulsionAndMinimumDistance)) / differenceOfSquaredDistances;
        double b = -(productOfMaximumRepulsionAndMinimumDistance / differenceOfSquaredDistances);

        double repulsion = a / Math.pow(distance, 2.0) + b;
        if (repulsion <= 0.0) {
            repulsion = 0.0;
        }

        return repulsion;
    }
    public List<Patch> get7x7Field(double heading, boolean includeCenterPatch, double fieldOfViewAngle) {
        Patch centerPatch = this.currentPatch;
        List<Patch> patchesToExplore = new ArrayList<>();
        boolean isCenterPatch;

        for (Patch patch : centerPatch.get7x7Neighbors(includeCenterPatch)) {
            isCenterPatch = patch.equals(centerPatch);
            if ((includeCenterPatch && isCenterPatch) || Coordinates.isWithinFieldOfView(centerPatch.getPatchCenterCoordinates(), patch.getPatchCenterCoordinates(), heading, fieldOfViewAngle)) {
                patchesToExplore.add(patch);
            }
        }

        return patchesToExplore;
    }
    private boolean hasClearLineOfSight(Coordinates sourceCoordinates, Coordinates targetCoordinates, boolean includeStartingPatch) {
        if (hasObstacle(this.environment.getPatch(targetCoordinates))) {
            return false;
        }

        final double resolution = 0.2;
        final double distanceToTargetCoordinates = Coordinates.distance(sourceCoordinates, targetCoordinates);
        final double headingToTargetCoordinates = Coordinates.headingTowards(sourceCoordinates, targetCoordinates);

        Patch startingPatch = this.environment.getPatch(sourceCoordinates);
        Coordinates currentPosition = new Coordinates(sourceCoordinates);
        double distanceCovered = 0.0;

        while (distanceCovered <= distanceToTargetCoordinates) {
            if (includeStartingPatch || !this.environment.getPatch(currentPosition).equals(startingPatch)) {
                if (hasObstacle(this.environment.getPatch(currentPosition))) {
                    return false;
                }
            }

            currentPosition = this.getFuturePosition(currentPosition, headingToTargetCoordinates, resolution);
            distanceCovered += resolution;
        }

        return true;
    }
    private boolean hasObstacle(Patch patch) {
        // check if the patch is not the whitelist of amenity or a wall
        if ((patch.getAmenityBlock() != null && (!patch.getAmenityBlock().getParent().equals(this.goalAmenity) &&
                patch.getAmenityBlock().getParent().getClass() != Chair.class &&
                patch.getAmenityBlock().getParent().getClass() != WindowBlinds.class &&
                patch.getAmenityBlock().getParent().getClass() != Aircon.class &&
                patch.getAmenityBlock().getParent().getClass() != Light.class &&
                patch.getAmenityBlock().getParent().getClass() != Switch.class &&
                patch.getAmenityBlock().getParent().getClass() != Toilet.class &&
                patch.getAmenityBlock().getParent().getClass() != Couch.class &&
                patch.getAmenityBlock().getParent().getClass() != TrashCan.class &&
                patch.getAmenityBlock().getParent().getClass() != Whiteboard.class &&
                patch.getAmenityBlock().getParent().getClass() != ReceptionTable.class
        )) || (patch.getPatchField() != null && patch.getPatchField().getKey().getClass() == Divider.class)) {
            return true;
        }


        return false;
    }

    private Vector computeAttractiveForce(final Coordinates startingPosition, final double proposedHeading, final Coordinates proposedNewPosition, final double preferredWalkingDistance) {
        return new Vector(startingPosition, proposedHeading, proposedNewPosition, preferredWalkingDistance);
    }

    public boolean hasAgentReachedFinalPatchInPath() { // Check if this agent has reached the final patch in its current path
        return this.currentPath.getPath().isEmpty();
    }
    public boolean isCloseToFinalPatchInPath() {
        return this.currentPath.getPath().size() == 1;
    }
    public boolean isCloseOrOnQueue() {
        if (this.goalQueueingPatchField != null) {
            if (this.goalQueueingPatchField.getQueueingAgents().isEmpty()) {
                return this.isOnOrCloseToPatch(goalNearestQueueingPatch);
            }

            return this.isOnOrCloseToPatch(this.goalQueueingPatchField.getQueueingAgents().getLast().getAgentMovement().getCurrentPatch());
        }
        return false;
    }

    // Check if this passenger has reached its goal
    public boolean hasReachedGoal() {
        return isOnOrCloseToPatch(this.goalAttractor.getPatch());
    }

    // Set the passenger's current amenity and position as it reaches the next goal
    public void reachGoal() {
        // Just in case the passenger isn't actually on its goal, but is adequately close to it, just move the passenger
        // there
        // Make sure to offset the passenger from the center a little so a force will be applied to this passenger
        Coordinates patchCenter = this.goalAttractor.getPatch().getPatchCenterCoordinates();
//        Coordinates offsetPatchCenter = this.getFuturePosition(
//                patchCenter,
//                this.previousHeading,
//                Patch.PATCH_SIZE_IN_SQUARE_METERS * 0.1
//        );

        this.setPosition(patchCenter);

        // Set the current amenity
        double agentHeading = this.heading;
        double agentHeadingDegrees = Math.toDegrees(agentHeading);
        int originalRow = this.goalAttractor.getPatch().getMatrixPosition().getRow();
        int originalCol = this.goalAttractor.getPatch().getMatrixPosition().getColumn();
        // Looking east
        if (agentHeadingDegrees >= 315 && agentHeadingDegrees < 360 || agentHeadingDegrees >= 0 && agentHeadingDegrees < 45) {
            Patch infrontPatch = environment.getPatch(originalRow, originalCol + 1);
            if (infrontPatch.getAmenityBlock() != null && infrontPatch.getAmenityBlock().getParent() instanceof Monitor) {
                System.out.println("Is monitor");
                currentAmenity = infrontPatch.getAmenityBlock().getParent();
            }
            else {
                currentAmenity = goalAmenity;
            }
        }
        // Looking north
        else if (agentHeadingDegrees >= 45 && agentHeadingDegrees < 135) {
            Patch infrontPatch = environment.getPatch(originalRow - 1, originalCol);
            if (infrontPatch.getAmenityBlock() != null && infrontPatch.getAmenityBlock().getParent() instanceof Monitor) {
                System.out.println("Is monitor");
                currentAmenity = infrontPatch.getAmenityBlock().getParent();
            }
            else {
                currentAmenity = goalAmenity;
            }
        }
        // Looking west
        else if (agentHeadingDegrees >= 135 && agentHeadingDegrees < 225) {
            Patch infrontPatch = environment.getPatch(originalRow, originalCol - 1);
            if (infrontPatch.getAmenityBlock() != null && infrontPatch.getAmenityBlock().getParent() instanceof Monitor) {
                System.out.println("Is monitor");
                currentAmenity = infrontPatch.getAmenityBlock().getParent();
            }
            else {
                currentAmenity = goalAmenity;
            }
        }
        // Looking south
        else if (agentHeadingDegrees >= 225 && agentHeadingDegrees < 315) {
            Patch infrontPatch = environment.getPatch(originalRow + 1, originalCol);
            if (infrontPatch.getAmenityBlock() != null && infrontPatch.getAmenityBlock().getParent() instanceof Monitor) {
                System.out.println("Is monitor");
                currentAmenity = infrontPatch.getAmenityBlock().getParent();
            }
            else {
                currentAmenity = goalAmenity;
            }
        }
        this.environment.getUsedAmenities().add(currentAmenity);
        this.currentPath = null;
    }

    public boolean coolDown(int maxCoolDown) {
        System.out.println("Cooldown: " + coolDown);
        if (this.coolDown <= 0) {
            this.coolDown = Simulator.rollIntIN(maxCoolDown); // set cool down duration
            return true;
        }
        this.coolDown--;
        return false;
    }

    public boolean thermalComfortCoolDown() {
        System.out.println("Thermal Cooldown: " + this.changeThermalCoolDown);
        if (this.changeThermalCoolDown <= 0) {
            this.changeThermalCoolDown = Simulator.rollIntIN(MAX_CHANGE_THERMAL_COOL_DOWN_DURATION); // set cool down duration
            return true;
        }
        this.changeThermalCoolDown--;
        return false;
    }

    public boolean visualComfortCoolDown() {
        System.out.println("Thermal Cooldown: " + this.changeVisualCoolDown);
        if (this.changeVisualCoolDown <= 0) {
            this.changeVisualCoolDown = Simulator.rollIntIN(MAX_CHANGE_VISUAL_COOL_DOWN_DURATION); // set cool down duration
            return true;
        }
        this.changeVisualCoolDown--;
        return false;
    }

    /***** GETTERS *****/
    private boolean isOnOrCloseToPatch(Patch patch) {
        return Coordinates.distance(this.position, patch.getPatchCenterCoordinates()) <= this.preferredWalkingDistance;
    }

    public Agent getParent() {
        return parent;
    }

    public Coordinates getPosition() {
        return position;

    }
    public Environment getEnvironment() {
        return environment;
    }

    public int getTeam() {
        return team;
    }

    public Chair getAssignedSeat() {
        return assignedSeat;
    }

    public double getCurrentWalkingDistance() {
        return currentWalkingDistance;
    }

    public double getProposedHeading() {
        return proposedHeading;
    }

    public double getHeading() {
        return heading;
    }

    public Patch getCurrentPatch() {
        return currentPatch;
    }
    public AgentPath getCurrentPath() {
        return currentPath;
    }
    public int getStateIndex() {
        return stateIndex;
    }
    public int getActionIndex() {
        return actionIndex;
    }
    public State getCurrentState() {
        return currentState;
    }
    public Action getCurrentAction() {
        return currentAction;
    }
    public Amenity getCurrentAmenity() {
        return currentAmenity;
    }
    public Patch getGoalPatch() {
        return goalPatch;
    }

    public Amenity.AmenityBlock getGoalAttractor() {
        return goalAttractor;
    }
    public Amenity getGoalAmenity() {
        return goalAmenity;
    }
    public QueueingPatchField getGoalQueueingPatchField() {
        return goalQueueingPatchField;
    }
    public Patch getGoalNearestQueueingPatch() {
        return goalNearestQueueingPatch;
    }
    public RoutePlan getRoutePlan() {
        return routePlan;
    }

    public ConcurrentHashMap<Patch, Integer> getRecentPatches() {
        return recentPatches;
    }

    public double getDistanceMovedInTick() {
        return distanceMovedInTick;
    }

    public int getDuration() {
        return this.duration;
    }
    public double getFieldOfViewAngle() {
        return fieldOfViewAngle;
    }

    public int getNoMovementCounter() {
        return noMovementCounter;
    }

    public int getMovementCounter() {
        return movementCounter;
    }

    public int getNoNewPatchesSeenCounter() {
        return noNewPatchesSeenCounter;
    }

    public int getNewPatchesSeenCounter() {
        return newPatchesSeenCounter;
    }

    public int getStuckCounter() {
        return stuckCounter;
    }

    public int getTimeSinceLeftPreviousGoal() {
        return timeSinceLeftPreviousGoal;
    }

    public boolean isStuck() {
        return isStuck;
    }

    public boolean isReadyToFree() {
        return isReadyToFree;
    }

    public List<Vector> getRepulsiveForceFromAgents() {
        return repulsiveForceFromAgents;
    }

    public List<Vector> getRepulsiveForcesFromObstacles() {
        return repulsiveForcesFromObstacles;
    }

    public Vector getAttractiveForce() {
        return attractiveForce;
    }

    public Vector getMotivationForce() {
        return motivationForce;
    }

    public Goal getGoalAmenityAsGoal() {
        return Goal.toGoal(this.goalAmenity);
    }

    public QueueableGoal getGoalAmenityAsQueueableGoal() {
        return QueueableGoal.toQueueableGoal(this.goalAmenity);
    }
    public boolean isInteracting() {
        return isInteracting;
    }
    public boolean isSimultaneousInteractionAllowed() {
        return isSimultaneousInteractionAllowed;
    }
    public int getInteractionDuration() {
        return interactionDuration;
    }
    public InteractionType getInteractionType() {
        return interactionType;
    }
    public Agent getAgentToInquire() {
        return agentToInquire;
    }

    public double getPreviousHeading() {
        return previousHeading;
    }

    public double getWorkingSeatHeading() {
        return workingSeatHeading;
    }

    public double getSeatHeading() {
        return seatHeading;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public Aircon getAirconToChange() {
        return airconToChange;
    }

    public WindowBlinds getBlindsToOpen() {
        return blindsToOpen;
    }

    public Light getLightsToOpen() {
        return lightsToOpen;
    }


    public boolean isToCool() {
        return isToCool;
    }

    public boolean isToHeat() {
        return isToHeat;
    }

    /***** SETTERS *****/
    public void setPosition(Coordinates coordinates) {
        final int timeElapsedExpiration = 10;
        Patch previousPatch = this.currentPatch;
        this.position.setX(coordinates.getX());
        this.position.setY(coordinates.getY());

        Patch newPatch = this.environment.getPatch(new Coordinates(coordinates.getX(), coordinates.getY()));
        if (!previousPatch.equals(newPatch)) {
            previousPatch.removeAgent(this.parent);
            newPatch.addAgent(this.parent);

            SortedSet<Patch> previousPatchSet = previousPatch.getEnvironment().getAgentPatchSet();
            SortedSet<Patch> newPatchSet = newPatch.getEnvironment().getAgentPatchSet();

            if (previousPatchSet.contains(previousPatch) && hasNoAgent(previousPatch)) {
                previousPatchSet.remove(previousPatch);
            }

            newPatchSet.add(newPatch);
            this.currentPatch = newPatch;
            updateRecentPatches(this.currentPatch, timeElapsedExpiration);
        }
        else {
            updateRecentPatches(null, timeElapsedExpiration);
        }
    }
    public void setCurrentPatch(Patch currentPatch) {
        this.currentPatch = currentPatch;
    }
    public void setCurrentPath(AgentPath currentPath) {
        this.currentPath = currentPath;
    }
    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }
    public void setActionIndex(int actionIndex) {
        this.actionIndex = actionIndex;
    }
    public void setCurrentState(int i) {
        this.currentState = this.getRoutePlan().setState(i);
    }
    public void setNextState(int i) {
        this.currentState = this.currentState.getRoutePlan().setNextState(i);
    }
    public void setPreviousState(int i) {
        this.currentState = this.currentState.getRoutePlan().setPreviousState(i);
    }
    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }
    public void setCurrentAmenity(Amenity currentAmenity) {
        this.currentAmenity = currentAmenity;
    }
    public void setGoalAttractor(Amenity.AmenityBlock goalAttractor) {
        this.goalAttractor = goalAttractor;
        this.goalPatch = this.goalAttractor.getPatch(); //JIC if needed
    }
    public void setGoalAmenity(Amenity goalAmenity) {
        this.goalAmenity = goalAmenity;
    }
    public void setGoalQueueingPatchField(QueueingPatchField goalQueueingPatchField) {
        this.goalQueueingPatchField = goalQueueingPatchField;
    }
    public void setRoutePlan(RoutePlan routePlan) {
        this.routePlan = routePlan;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setInteracting(boolean interacting) {
        isInteracting = interacting;
    }
    public void setSimultaneousInteractionAllowed(boolean simultaneousInteractionAllowed) {
        isSimultaneousInteractionAllowed = simultaneousInteractionAllowed;
    }
    public void setInteractionDuration(int interactionDuration) {
        this.interactionDuration = interactionDuration;
    }
    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }
    public void setAssignedSeat(Chair assignedSeat) {
        this.assignedSeat = assignedSeat;
    }
    public void setAgentToInquire(Agent agentToInquire) {
        this.agentToInquire = agentToInquire;
    }
    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setPreviousHeading(double previousHeading) {
        this.previousHeading = previousHeading;
    }

    public void setWorkingSeatHeading(double workingSeatHeading) {
        this.workingSeatHeading = workingSeatHeading;
    }

    public void setSeatHeading(double seatHeading) {
        this.seatHeading = seatHeading;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public void setAirconToChange(Aircon airconToChange) {
        this.airconToChange = airconToChange;
    }

    public void setBlindsToOpen(WindowBlinds blindsToOpen) {
        this.blindsToOpen = blindsToOpen;
    }

    public void setLightsToOpen(Light lightsToOpen) {
        this.lightsToOpen = lightsToOpen;
    }

    //SETTER aircon
    public void setToHeat(boolean toHeat) {
        isToHeat = toHeat;
    }

    public void setToCool(boolean toCool) {
        isToCool = toCool;
    }
}