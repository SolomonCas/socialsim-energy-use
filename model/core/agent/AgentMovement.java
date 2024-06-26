package com.socialsim.model.core.agent;

import com.socialsim.controller.Main;
import com.socialsim.model.core.agent.pathfinding.AgentPath;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.core.environment.position.Vector;
import com.socialsim.model.simulator.Simulator;

import java.time.LocalTime;
import java.util.*;
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

    private final Agent parent;
    private final Coordinates position;
    private final Environment environment;
    private final double baseWalkingDistance;
    private double preferredWalkingDistance;
    private double currentWalkingDistance;
    private double proposedHeading;
    private double heading;
    private int team;
    private Amenity assignedSeat;
    private Patch currentPatch;
    private Amenity currentAmenity;
    private PatchField currentPatchField;
    private Patch goalPatch;
    private Patch waitPatch;
    private Amenity.AmenityBlock goalAttractor;
    private Amenity goalAmenity;
    private PatchField goalPatchField;
    private QueueingPatchField goalQueueingPatchField;
    private Patch goalNearestQueueingPatch;
    private RoutePlan routePlan;
    private AgentPath currentPath;
    private int stateIndex;
    private int actionIndex;
    private State currentState;
    private Action currentAction;
    private boolean isWaitingOnAmenity;
    private boolean hasEncounteredAgentToFollow;
    private Agent agentFollowedWhenAssembling;
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
    private AgentMovement.InteractionType interactionType;
    private Patch collabTablePatch;

    public enum InteractionType {
        NON_VERBAL, COOPERATIVE, EXCHANGE
    }

    /***** CONSTRUCTOR *****/
    public AgentMovement(Patch spawnPatch, Agent parent, double baseWalkingDistance, Coordinates coordinates, int team, Amenity assignedSeat) { // For inOnStart agents
        this.parent = parent;
        this.position = new Coordinates(coordinates.getX(), coordinates.getY());
        this.team = team;
        this.assignedSeat = assignedSeat;

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
            this.fieldOfViewAngle = this.environment.getFieldOfView();
        }
        else {
            this.proposedHeading = Math.toRadians(90.0);
            this.heading = Math.toRadians(90.0);
            this.fieldOfViewAngle = this.environment.getFieldOfView();
        }

        this.currentPatchField = null;

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
            this.currentAmenity = environment.getGates().getFirst(); // Getting Entrance Gate
        }
        if (this.currentAction.getDestination() != null) {
            this.goalAttractor = this.currentAction.getDestination().getAmenityBlock();
        }
        if (this.currentAction.getDuration() != 0) {
            this.duration = this.currentAction.getDuration();
        }

        this.isInteracting = false;

        this.collabTablePatch = null;
    }

    /***** METHODS *****/
    public void resetGoal() { // Reset the agent's goal
        this.goalPatch = null;
        this.waitPatch = null;
        this.goalAmenity = null;
        this.goalAttractor = null;
        this.goalPatchField = null;
        this.currentPath = null;
        this.currentAmenity = null;
        this.goalQueueingPatchField = null;
        this.goalNearestQueueingPatch = null;
        this.hasEncounteredAgentToFollow = false;
        this.isWaitingOnAmenity = false;
        this.agentFollowedWhenAssembling = null;
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
                if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor;

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
            else if (parent.getType() == Agent.Type.MAINTENANCE &&
                    this.currentAction.getName() == Action.Name.MAINTENANCE_CLEAN_TOILET) {
                for(int i = 0; i < this.environment.getToilets().size(); i++) {
//                    if (!this.environment.getToilets().get(i).isClean()) {
//                        temp.add(this.environment.getToilets().get(i));
//                    }
                }
            }
            else {

                for (Amenity amenity : environment.getAmenityList(nextAmenityClass)) {
                    if (parent.getGender() == Agent.Gender.MALE) {
                        if (amenity.getAmenityBlocks().getFirst().getPatch().getPatchField().getValue() == "male") {
                            temp.add(amenity);
                        }
                    }
                    else if (parent.getGender() == Agent.Gender.FEMALE) {
                        if (amenity.getAmenityBlocks().getFirst().getPatch().getPatchField().getValue() == "female") {
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
                if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor;

                    getGoalAttractor().setIsReserved(true);
                    return true;
                }
            }

            return false;
        }

        return false;

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
                this.getParent().getAgentMovement().setInteractionType(AgentMovement.InteractionType.NON_VERBAL);
                agent.getAgentMovement().setInteractionType(AgentMovement.InteractionType.NON_VERBAL);
                interactionMean = getEnvironment().getNonverbalMean();
                interactionStdDeviation = getEnvironment().getNonverbalStdDev();
            }
            else if (CHANCE < (CHANCE_NONVERBAL1 + CHANCE_NONVERBAL2 + CHANCE_COOPERATIVE1 + CHANCE_COOPERATIVE2) / 2){
                Simulator.currentCooperativeCount++;
                this.getParent().getAgentMovement().setInteractionType(AgentMovement.InteractionType.COOPERATIVE);
                agent.getAgentMovement().setInteractionType(AgentMovement.InteractionType.COOPERATIVE);
                CHANCE1 = Simulator.roll() * IOS1;
                CHANCE2 = Simulator.roll() * IOS2;
                interactionMean = getEnvironment().getCooperativeMean();
                interactionStdDeviation = getEnvironment().getCooperativeStdDev();
            }
            else if (CHANCE < (CHANCE_NONVERBAL1 + CHANCE_NONVERBAL2 + CHANCE_COOPERATIVE1 + CHANCE_COOPERATIVE2 + CHANCE_EXCHANGE1 + CHANCE_EXCHANGE2) / 2){
                Simulator.currentExchangeCount++;
                this.getParent().getAgentMovement().setInteractionType(AgentMovement.InteractionType.EXCHANGE);
                agent.getAgentMovement().setInteractionType(AgentMovement.InteractionType.EXCHANGE);
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
            if (agent.getAgentMovement().getInteractionType() == AgentMovement.InteractionType.NON_VERBAL)
                Simulator.averageNonverbalDuration = (Simulator.averageNonverbalDuration * (Simulator.currentNonverbalCount - 1) + this.interactionDuration) / Simulator.currentNonverbalCount;
            else if (agent.getAgentMovement().getInteractionType() == AgentMovement.InteractionType.COOPERATIVE)
                Simulator.averageCooperativeDuration = (Simulator.averageCooperativeDuration * (Simulator.currentCooperativeCount - 1) + this.interactionDuration) / Simulator.currentCooperativeCount;
            else if (agent.getAgentMovement().getInteractionType() == AgentMovement.InteractionType.EXCHANGE)
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
                if(getWaitPatch() != null){
                    path.push(goalPatch);
                }
                else if(    goalAmenity.getClass() == DirectorChair.class
                        ||  goalAmenity.getClass() == LearningChair.class
                        ||  goalAmenity.getClass() == MeetingChair.class
                        ||  goalAmenity.getClass() == PantryChair.class
                        ||  goalAmenity.getClass() == ReceptionChair.class
                        ||  goalAmenity.getClass() == ResearchChair.class
                        ||  goalAmenity.getClass() == Door.class
                        ||  goalAmenity.getClass() == MainEntranceDoor.class
                        ||  goalAmenity.getClass() == FemaleBathroomDoor.class
                        ||  goalAmenity.getClass() == MaleBathroomDoor.class
                        ||  goalAmenity.getClass() == Toilet.class
                        ||  goalAmenity.getClass() == Couch.class
                        ||  goalAmenity.getClass() == Gate.class
                        ||  goalAmenity.getClass() == Cubicle.class
                        ||  goalAmenity.getClass() == DirectorTable.class
                        ||  goalAmenity.getClass() == LearningTable.class
                        ||  goalAmenity.getClass() == MeetingTable.class
                        ||  goalAmenity.getClass() == PantryTable.class
                        ||  goalAmenity.getClass() == ReceptionTable.class
                        ||  goalAmenity.getClass() == ResearchTable.class
                ) {
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

                return new AgentPath(length, path);
            }
            openSet.remove(patchToExplore);

            List<Patch> patchToExploreNeighbors = patchToExplore.getNeighbors();
            for (Patch patchToExploreNeighbor : patchToExploreNeighbors) {
                if ((patchToExploreNeighbor.getAmenityBlock() == null && patchToExploreNeighbor.getPatchField() == null)
                        || (patchToExploreNeighbor.getAmenityBlock() != null && patchToExploreNeighbor.getPatchField() == null && patchToExploreNeighbor.getAmenityBlock().getParent() == goalAmenity)
                        || (patchToExploreNeighbor.getAmenityBlock() != null && patchToExploreNeighbor.getPatchField() != null && patchToExploreNeighbor.getAmenityBlock().getParent() == goalAmenity)
                        || (patchToExploreNeighbor.getAmenityBlock() != null && (patchToExploreNeighbor.getAmenityBlock().getParent().getClass() == Door.class
                        || patchToExploreNeighbor.getAmenityBlock().getParent().getClass() == MainEntranceDoor.class
                        || patchToExploreNeighbor.getAmenityBlock().getParent().getClass() == MaleBathroomDoor.class
                        || patchToExploreNeighbor.getAmenityBlock().getParent().getClass() == FemaleBathroomDoor.class
                        || patchToExploreNeighbor.getAmenityBlock().getParent().getClass() == ReceptionTable.class))
                        || (patchToExploreNeighbor.getPatchField() != null && patchToExploreNeighbor.getPatchField().getKey().getClass() != Wall.class)
                        || (!includeStartingPatch && patchToExplore.equals(startingPatch) || !includeGoalPatch && patchToExploreNeighbor.equals(goalPatch))) {
                    double obstacleClosenessPenalty = (patchToExploreNeighbor.getAmenityBlocksAround() + patchToExploreNeighbor.getWallsAround()) * 2.0;
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
    public boolean chooseWaitPatch(){
        ArrayList<Patch> patchesToConsider = new ArrayList<>();
        if (this.getParent().getGender() == Agent.Gender.MALE){
            for (int i = 11; i < 16; i++) {
                patchesToConsider.add(environment.getPatch(i, 135));
            }
        }
        else{
            for (int i = 35; i > 30; i--) {
                patchesToConsider.add(environment.getPatch(i, 135));
            }
        }
//        this.waitPatch = patchesToConsider.get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(patchesToConsider.size()));
        for(Patch patch : patchesToConsider) {
            if(patch.getAmenityBlock().getIsReserved()) {
                this.waitPatch = patch;
                getWaitPatch().getAmenityBlock().setIsReserved(true);
                return true;
            }
        }
        return false;
    }
    public void removeCollaborationTeam(){
        if(this.collabTablePatch != null && this.collabTablePatch.getTeam() != -1){
            this.collabTablePatch.setTeam(-1);
            this.collabTablePatch = null;
        }
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
                if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor;

                    getGoalAttractor().setIsReserved(true);
                    return true;
                }else if(temp == sortedDistances.size()){
                    return false;
                }
            }
        }

        return false;
    }

    public boolean chooseBreakSeat() {
        if (this.goalAmenity == null) {
            List<Amenity> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();

            // Pantry Chair Type A
            for(int i = 0; i < this.environment.getPantryTables().size(); i++) {
                temp.addAll(this.environment.getPantryTables().get(i).getPantryChairs());
            }

            // chelsea: commented this out; not sure if I should replace this with all kinds of chairs
            // temp.addAll(this.environment.getChairs());

            // Agents are not allowed to eat their lunch on the couch
            if(this.getCurrentAction().getName() != Action.Name.GO_TO_LUNCH) {
                temp.addAll(this.environment.getCouches());
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
                if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor;

                    getGoalAttractor().setIsReserved(true);
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    public boolean chooseCollaborationChair(){
        if(this.goalAmenity == null){
            List<Amenity> temp = new ArrayList<>();
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();
            int count = 1;
            int table = -1;

            List<? extends Amenity> amenityListInFloor = this.environment.getAmenityList(LearningTable.class);
            for (Amenity amenity : amenityListInFloor) {
                if(amenity.getAttractors().get(0).getPatch().getTeam() == this.team){
                    table = count;
                    this.collabTablePatch = amenity.getAttractors().get(0).getPatch();
                    break;
                }
                count++;
                if(count>Main.simulator.getEnvironment().getLearningTables().size()){
                    count = 0;
                    break;
                }
            }

            if(table == -1){
                for (Amenity amenity : amenityListInFloor) {
                    count++;
                    if(amenity.getAttractors().get(0).getPatch().getTeam() == -1){
                        table = count;
                        this.collabTablePatch = amenity.getAttractors().get(0).getPatch();
                        amenity.getAttractors().get(0).getPatch().setTeam(this.team);
                        break;
                    }
                    if(count == Main.simulator.getEnvironment().getLearningTables().size()){
                        break;
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

                HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
                for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                    sortedDistances.put(aa.getKey(), aa.getValue());
                }

                for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                    Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();

                    if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                        this.goalAmenity = candidateAttractor.getParent();
                        this.goalAttractor = candidateAttractor;

                        getGoalAttractor().setIsReserved(true);
                        return true;
                    }
                }

            }
            return false;
        }

        return false;
    }



    public void chooseMeetingGoal(int room){
        if(this.goalAmenity == null){
            HashMap<Amenity.AmenityBlock, Double> distancesToAttractors = new HashMap<>();

            List<Amenity> temp = new ArrayList<>();
            temp.addAll(Main.simulator.getEnvironment().getMeetingTables().get(room - 1).getMeetingChairs());

            for (Amenity amenity : temp) {
                for (Amenity.AmenityBlock attractor : amenity.getAttractors()) {
                    double distanceToAttractor = Coordinates.distance(this.currentPatch, attractor.getPatch());
                    distancesToAttractors.put(attractor, distanceToAttractor);
                }
            }

            List<Map.Entry<Amenity.AmenityBlock, Double> > list =
                    new LinkedList<Map.Entry<Amenity.AmenityBlock, Double> >(distancesToAttractors.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Amenity.AmenityBlock, Double> >() {
                public int compare(Map.Entry<Amenity.AmenityBlock, Double> o1,
                                   Map.Entry<Amenity.AmenityBlock, Double> o2)
                {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            HashMap<Amenity.AmenityBlock, Double> sortedDistances = new LinkedHashMap<Amenity.AmenityBlock, Double>();
            for (Map.Entry<Amenity.AmenityBlock, Double> aa : list) {
                sortedDistances.put(aa.getKey(), aa.getValue());
            }

            for (Map.Entry<Amenity.AmenityBlock, Double> distancesToAttractorEntry : sortedDistances.entrySet()) {
                Amenity.AmenityBlock candidateAttractor = distancesToAttractorEntry.getKey();

                if (candidateAttractor.getPatch().getAmenityBlock().getIsReserved()) {
                    this.goalAmenity =  candidateAttractor.getParent();
                    this.goalAttractor = candidateAttractor;

                    getGoalAttractor().setIsReserved(true);
                    break;
                }
            }
        }
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
        if(getWaitPatch() != null){
            distance = Coordinates.distance(this.position,getWaitPatch().getPatchCenterCoordinates());
            minimumDistance = distance;
        }
        else{
            for (Amenity.AmenityBlock attractor : goal.getAttractors()) {
                distance = Coordinates.distance(this.position, attractor.getPatch().getPatchCenterCoordinates());
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestAttractor = attractor;
                }
            }
        }


        assert nearestAttractor != null;

        if (minimumDistance < walkingDistance) {
            if(getWaitPatch() != null){
                return new Coordinates(getWaitPatch().getPatchCenterCoordinates().getX(), getWaitPatch().getPatchCenterCoordinates().getY());
            }
            else{
                return new Coordinates(nearestAttractor.getPatch().getPatchCenterCoordinates().getX(), nearestAttractor.getPatch().getPatchCenterCoordinates().getY());
            }
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
        final int noNewPatchesSeenTicksThreshold = 5;
        final double noMovementThreshold = 0.01 * this.preferredWalkingDistance;
        final double noNewPatchesSeenThreshold = 5;
        final double slowdownStartDistance = 2.0;
        int numberOfObstacles = 0;
        final double minimumAgentStopDistance = 0.6;
        final double minimumObstacleStopDistance = 0.6;

        List<Patch> patchesToExplore = this.get7x7Field(this.proposedHeading, true, Math.toRadians(360.0));

        this.repulsiveForceFromAgents.clear();
        this.repulsiveForcesFromObstacles.clear();
        this.attractiveForce = null;
        this.motivationForce = null;

        TreeMap<Double, Patch> obstaclesEncountered = new TreeMap<>();

        List<Vector> vectorsToAdd = new ArrayList<>();

        Coordinates proposedNewPosition = this.getFuturePosition(this.preferredWalkingDistance);
        this.preferredWalkingDistance = this.baseWalkingDistance;

        final double distanceSlowdownStart = 5.0;
        final double speedDecreaseFactor = 0.5;

        double distanceToGoal;
        if(getWaitPatch() != null){
            distanceToGoal = Coordinates.distance(this.currentPatch, getWaitPatch());
            if (distanceToGoal < distanceSlowdownStart && this.hasClearLineOfSight(this.position, getWaitPatch().getPatchCenterCoordinates(), true)) {
                this.preferredWalkingDistance *= speedDecreaseFactor;
            }
        }
        else{
            distanceToGoal = Coordinates.distance(this.currentPatch, this.getGoalAmenity().getAttractors().get(0).getPatch());
            if (distanceToGoal < distanceSlowdownStart && this.hasClearLineOfSight(this.position, this.goalAmenity.getAttractors().get(0).getPatch().getPatchCenterCoordinates(), true)) {
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
            if (hasObstacle(patch, goalAmenity)) {
                numberOfObstacles++;

                double distanceToObstacle = Coordinates.distance(this.position, patch.getPatchCenterCoordinates());
                if (distanceToObstacle <= slowdownStartDistance) {
                    obstaclesEncountered.put(distanceToObstacle, patch);
                }
            }

            if(this.currentState.getName() != State.Name.GOING_TO_RECEPTION && this.currentState.getName() != State.Name.GOING_HOME
                    && (this.currentPatch.getPatchField() != null && this.currentPatch.getPatchField().getKey().getClass() != Bathroom.class)
                    && (this.currentPatch.getPatchField() != null && this.currentPatch.getPatchField().getKey().getClass() != FacultyRoom.class)
                    && (this.currentPatch.getPatchField() != null && this.currentPatch.getPatchField().getKey().getClass() != BreakerRoom.class)
                    && (this.currentPatch.getPatchField() != null && this.currentPatch.getPatchField().getKey().getClass() != MeetingRoom.class)) {
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

                    if (this.currentAction.getName() != Action.Name.FILL_UP_NAME) {
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

                    this.timeSinceLeftPreviousGoal++;

                    return;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

        this.hasEncounteredAgentToFollow = this.agentFollowedWhenAssembling != null;
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
    public void joinQueue() {
        this.goalQueueingPatchField.getQueueingAgents().add(this.parent);

        Stack<Patch> path = new Stack<>();
        for (int i = 0; i < this.goalQueueingPatchField.getAssociatedPatches().size(); i++) {
            path.push(this.goalQueueingPatchField.getAssociatedPatches().get(i));
        }
        this.currentPath = new AgentPath(0, path);
        this.duration = currentAction.getDuration();
    }
    public void leaveQueue() {
        this.goalQueueingPatchField.getQueueingAgents().remove(this.parent);
        this.goalQueueingPatchField = null;
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
    public boolean hasAgentReachedFinalPatchInPath() { // Check if this agent has reached the final patch in its current path
        return this.currentPath.getPath().isEmpty();
    }
    public boolean isCloseToFinalPatchInPath() {
        return (this.currentPath.getPath().size() == 1);
    }
    public void despawn(LocalTime returnOfficeTime) {
        if (this.currentPatch != null) {
            this.currentPatch.getAgents().remove(this.parent);
            this.getEnvironment().getAgents().remove(this.parent);

            // If agent will eat outside the office
            // Since the agent will be removed in the list
            // Add the agent again but changing it's timeIn value based on the randomized returnOfficeTime
            // While retaining everything
            if (Action.Name.EXIT_LUNCH == this.getCurrentAction().getName()) {
                this.parent.setTimeIn(returnOfficeTime);
                this.getEnvironment().getAgents().add(this.parent);
            }

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

            switch (this.getParent().getTeam()) {
                case 1 -> Simulator.currentTeam1Count--;
                case 2 -> Simulator.currentTeam2Count--;
                case 3 -> Simulator.currentTeam3Count--;
                case 4 -> Simulator.currentTeam4Count--;
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

            if (goalQueueingPatchField == null && waitPatch == null) {
                agentPath = computePath(this.currentPatch, this.goalAttractor.getPatch(), true, true);
            }
            else if (this.waitPatch != null){
                agentPath = computePath(this.currentPatch,this.waitPatch , true, true);
            }
            else {
                agentPath = computePath(this.currentPatch, this.goalQueueingPatchField.getLastQueuePatch(), true, true);
            }

            if (agentPath != null) {
                this.currentPath = new AgentPath(agentPath);
                wasPathJustGenerated = true;
            }
        }

        if (this.currentPath == null || this.currentPath.getPath().isEmpty()) {
            return false;
        }

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
        if (hasObstacle(this.environment.getPatch(targetCoordinates), goalAmenity)) {
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
                if (hasObstacle(this.environment.getPatch(currentPosition), goalAmenity)) {
                    return false;
                }
            }

            currentPosition = this.getFuturePosition(currentPosition, headingToTargetCoordinates, resolution);
            distanceCovered += resolution;
        }

        return true;
    }
    private boolean hasObstacle(Patch patch, Amenity amenity) {
        if (patch.getPatchField() != null && patch.getPatchField().getKey().getClass() == Wall.class) {
            return true;
        }
        else if (patch.getAmenityBlock() != null && !patch.getAmenityBlock().getParent().equals(amenity)) {
            if (patch.getAmenityBlock().getParent().getClass() == Door.class ||
                    patch.getAmenityBlock().getParent().getClass() == MainEntranceDoor.class ||
                    patch.getAmenityBlock().getParent().getClass() == FemaleBathroomDoor.class ||
                    patch.getAmenityBlock().getParent().getClass() == MaleBathroomDoor.class ||
                    patch.getAmenityBlock().getParent().getClass() == DirectorChair.class ||
                    patch.getAmenityBlock().getParent().getClass() == LearningChair.class ||
                    patch.getAmenityBlock().getParent().getClass() == MeetingChair.class ||
                    // chelsea: wanted to add pantry chair type a and b but not sure if it should be here
                    patch.getAmenityBlock().getParent().getClass() == ReceptionChair.class ||
                    patch.getAmenityBlock().getParent().getClass() == ResearchChair.class ||
                    patch.getAmenityBlock().getParent().getClass() == Toilet.class ||
                    patch.getAmenityBlock().getParent().getClass() == TrashCan.class ||
                    patch.getAmenityBlock().getParent().getClass() == Whiteboard.class
            ) {
                return false;
            }
            else {
                return true;
            }
        }

        return false;
    }
    public void checkIfStuck() {
        if (this.currentAmenity == null) {
            if (this.currentPatch.getAmenityBlock() != null &&
                    this.currentPatch.getAmenityBlock().getParent().getClass() != Door.class &&
                    this.currentPatch.getAmenityBlock().getParent().getClass() != MainEntranceDoor.class &&
                    this.currentPatch.getAmenityBlock().getParent().getClass() != FemaleBathroomDoor.class &&
                    this.currentPatch.getAmenityBlock().getParent().getClass() != MaleBathroomDoor.class &&  this.currentPatch.getAmenityBlock().getParent() != this.goalAmenity) {
                List<Patch> candidatePatches = this.currentPatch.getNeighbors();
                for (Patch candidate: candidatePatches) {
                    if (candidate.getAmenityBlock() == null && (candidate.getPatchField() == null || (candidate.getPatchField() != null && candidate.getPatchField().getKey().getClass() != Wall.class))) {
                        this.setPosition(candidate.getPatchCenterCoordinates());
                        break;
                    }
                }
            }
            else if (this.currentPatch.getPatchField() != null && this.currentPatch.getPatchField().getKey().getClass() == Wall.class) {
                List<Patch> candidatePatches = this.currentPatch.getNeighbors();
                for (Patch candidate: candidatePatches) {
                    if (candidate.getAmenityBlock() == null && (candidate.getPatchField() == null || (candidate.getPatchField() != null && candidate.getPatchField().getKey().getClass() != Wall.class))) {
                        this.setPosition(candidate.getPatchCenterCoordinates());
                        break;
                    }
                }
            }
        }
    }
    private Vector computeAttractiveForce(final Coordinates startingPosition, final double proposedHeading, final Coordinates proposedNewPosition, final double preferredWalkingDistance) {
        return new Vector(startingPosition, proposedHeading, proposedNewPosition, preferredWalkingDistance);
    }

    /***** GETTERS *****/
    private boolean isOnOrCloseToPatch(Patch patch) {
        return Coordinates.distance(this.position, patch.getPatchCenterCoordinates()) <= this.preferredWalkingDistance;
    }

    public Patch getWaitPatch() {
        return waitPatch;
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

    public Amenity getAssignedSeat() {
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
    public PatchField getCurrentPatchField() {
        return currentPatchField;
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
    public PatchField getGoalPatchField() {
        return goalPatchField;
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
    public boolean isWaitingOnAmenity() {
        return isWaitingOnAmenity;
    }
    public Agent getAgentFollowedWhenAssembling() {
        return agentFollowedWhenAssembling;
    }

    public boolean hasEncounteredAgentToFollow() {
        return hasEncounteredAgentToFollow;
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
        this.currentState = this.currentState.getRoutePlan().setState(i);
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
    public void setCurrentPatchField(PatchField currentPatchField) {
        this.currentPatchField = currentPatchField;
    }
    public void setGoalAttractor(Amenity.AmenityBlock goalAttractor) {
        this.goalAttractor = goalAttractor;
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

    public void setAssignedSeat(Amenity assignedSeat) {
        this.assignedSeat = assignedSeat;
    }
}