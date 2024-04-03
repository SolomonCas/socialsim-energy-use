package com.socialsim.model.core.agent;

import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.PatchField;
import com.socialsim.model.core.environment.patchfield.QueueingPatchField;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.goal.Cubicle;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
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

//    private final OfficeAgent parent;
//    private final Coordinates position;
//    private final Office office;
//    private final double baseWalkingDistance;
//    private double preferredWalkingDistance;
//    private double currentWalkingDistance;
//    private double proposedHeading;
//    private double heading;
//    private int team;
//    private Cubicle assignedCubicle;
//    private Patch currentPatch;
//    private Amenity currentAmenity;
//    private PatchField currentPatchField;
//    private Patch goalPatch;
//    private Patch waitPatch;
//    private Amenity.AmenityBlock goalAttractor;
//    private Amenity goalAmenity;
//    private PatchField goalPatchField;
//    private QueueingPatchField goalQueueingPatchField;
//    private Patch goalNearestQueueingPatch;
//    private OfficeRoutePlan routePlan;
//    private AgentPath currentPath;
//    private int stateIndex;
//    private int actionIndex;
//    private OfficeState currentState;
//    private OfficeAction currentAction;
//    private boolean isWaitingOnAmenity;
//    private boolean hasEncounteredAgentToFollow;
//    private OfficeAgent agentFollowedWhenAssembling;
//    private double distanceMovedInTick;
//    private int tickEntered;
//    private int duration;
//    private int noMovementCounter;
//    private int movementCounter;
//    private int noNewPatchesSeenCounter;
//    private int newPatchesSeenCounter;
//    private boolean isStuck;
//    private int stuckCounter;
//    private int timeSinceLeftPreviousGoal;
//    private final double fieldOfViewAngle;
//    private boolean isReadyToFree;
//    private final ConcurrentHashMap<Patch, Integer> recentPatches;
//    private final List<Vector> repulsiveForceFromAgents;
//    private final List<Vector> repulsiveForcesFromObstacles;
//    private Vector attractiveForce;
//    private Vector motivationForce;
//    private boolean isInteracting;
//    private boolean isSimultaneousInteractionAllowed;
//    private int interactionDuration;
//    private OfficeAgentMovement.InteractionType interactionType;
//    private Patch collabTablePatch;
//
//    public enum InteractionType {
//        NON_VERBAL, COOPERATIVE, EXCHANGE
//    }



    /***** CONSTRUCTOR *****/
    public AgentMovement(Patch spawnPatch, Agent parent, double baseWalkingDistance, Coordinates coordinates, long tickEntered, int team, Cubicle assignedCubicle) { // For inOnStart agents
//        this.parent = parent;
//        this.position = new Coordinates(coordinates.getX(), coordinates.getY());
//        this.team = team;
//        this.assignedCubicle = assignedCubicle;
//
//        final double interQuartileRange = 0.12;
//        this.baseWalkingDistance = baseWalkingDistance + Simulator.RANDOM_NUMBER_GENERATOR.nextGaussian() * interQuartileRange;
//        this.preferredWalkingDistance = this.baseWalkingDistance;
//        this.currentWalkingDistance = preferredWalkingDistance;
//
//        this.currentPatch = spawnPatch;
//        this.currentPatch.getAgents().add(parent);
//        this.office = (Office) currentPatch.getEnvironment();
//
//        if (parent.getInOnStart()) {
//            this.proposedHeading = Math.toRadians(270.0);
//            this.heading = Math.toRadians(270.0);
//            this.fieldOfViewAngle = this.office.getFieldOfView();
//        }
//        else {
//            this.proposedHeading = Math.toRadians(90.0);
//            this.heading = Math.toRadians(90.0);
//            this.fieldOfViewAngle = this.office.getFieldOfView();
//        }
//
//        this.currentPatchField = null;
//        this.tickEntered = (int) tickEntered;
//
//        this.recentPatches = new ConcurrentHashMap<>();
//        repulsiveForceFromAgents = new ArrayList<>();
//        repulsiveForcesFromObstacles = new ArrayList<>();
//        resetGoal();
//
//        this.routePlan = new OfficeRoutePlan(parent, office, currentPatch, (int) tickEntered, team, assignedCubicle);
//        this.stateIndex = 0;
//        this.actionIndex = 0;
//        this.currentState = this.routePlan.getCurrentState();
//        this.currentAction = this.routePlan.getCurrentState().getActions().get(actionIndex);
//        if (!parent.getInOnStart()) {
//            this.currentAmenity = office.getOfficeGates().get(1); // Getting Entrance Gate
//        }
//        if (this.currentAction.getDestination() != null) {
//            this.goalAttractor = this.currentAction.getDestination().getAmenityBlock();
//        }
//        if (this.currentAction.getDuration() != 0) {
//            this.duration = this.currentAction.getDuration();
//        }
//
//        this.isInteracting = false;
//
//        this.collabTablePatch = null;
    }

    /***** METHODS *****/




    /***** GETTERS *****/

//    public Patch getWaitPatch() {
//        return waitPatch;
//    }
//
//    public Agent getParent() {
//        return parent;
//    }
//
//    public Coordinates getPosition() {
//        return position;
//
//    }
//    public Environment getEnvironment() {
//        return environment;
//    }
//
//    public int getTeam() {
//        return team;
//    }
//
//    public Cubicle getAssignedCubicle() {
//        return assignedCubicle;
//    }
//
//    public double getCurrentWalkingDistance() {
//        return currentWalkingDistance;
//    }
//
//    public double getProposedHeading() {
//        return proposedHeading;
//    }
//
//    public double getHeading() {
//        return heading;
//    }
//
//    public Patch getCurrentPatch() {
//        return currentPatch;
//    }
//    public AgentPath getCurrentPath() {
//        return currentPath;
//    }
//    public int getStateIndex() {
//        return stateIndex;
//    }
//    public int getActionIndex() {
//        return actionIndex;
//    }
//    public State getCurrentState() {
//        return currentState;
//    }
//    public Action getCurrentAction() {
//        return currentAction;
//    }
//    public Amenity getCurrentAmenity() {
//        return currentAmenity;
//    }
//    public PatchField getCurrentPatchField() {
//        return currentPatchField;
//    }
//    public Patch getGoalPatch() {
//        return goalPatch;
//    }
//
//    public Amenity.AmenityBlock getGoalAttractor() {
//        return goalAttractor;
//    }
//    public Amenity getGoalAmenity() {
//        return goalAmenity;
//    }
//    public PatchField getGoalPatchField() {
//        return goalPatchField;
//    }
//    public QueueingPatchField getGoalQueueingPatchField() {
//        return goalQueueingPatchField;
//    }
//    public Patch getGoalNearestQueueingPatch() {
//        return goalNearestQueueingPatch;
//    }
//    public OfficeRoutePlan getRoutePlan() {
//        return routePlan;
//    }
//    public boolean isWaitingOnAmenity() {
//        return isWaitingOnAmenity;
//    }
//    public OfficeAgent getAgentFollowedWhenAssembling() {
//        return agentFollowedWhenAssembling;
//    }
//
//    public boolean hasEncounteredAgentToFollow() {
//        return hasEncounteredAgentToFollow;
//    }
//
//    public ConcurrentHashMap<Patch, Integer> getRecentPatches() {
//        return recentPatches;
//    }
//
//    public double getDistanceMovedInTick() {
//        return distanceMovedInTick;
//    }
//
//    public int getDuration() {
//        return this.duration;
//    }
//    public int getTickEntered() {
//        return tickEntered;
//    }
//    public double getFieldOfViewAngle() {
//        return fieldOfViewAngle;
//    }
//
//    public int getNoMovementCounter() {
//        return noMovementCounter;
//    }
//
//    public int getMovementCounter() {
//        return movementCounter;
//    }
//
//    public int getNoNewPatchesSeenCounter() {
//        return noNewPatchesSeenCounter;
//    }
//
//    public int getNewPatchesSeenCounter() {
//        return newPatchesSeenCounter;
//    }
//
//    public int getStuckCounter() {
//        return stuckCounter;
//    }
//
//    public int getTimeSinceLeftPreviousGoal() {
//        return timeSinceLeftPreviousGoal;
//    }
//
//    public boolean isStuck() {
//        return isStuck;
//    }
//
//    public boolean isReadyToFree() {
//        return isReadyToFree;
//    }
//
//    public List<Vector> getRepulsiveForceFromAgents() {
//        return repulsiveForceFromAgents;
//    }
//
//    public List<Vector> getRepulsiveForcesFromObstacles() {
//        return repulsiveForcesFromObstacles;
//    }
//
//    public Vector getAttractiveForce() {
//        return attractiveForce;
//    }
//
//    public Vector getMotivationForce() {
//        return motivationForce;
//    }
//
//    public Goal getGoalAmenityAsGoal() {
//        return Goal.toGoal(this.goalAmenity);
//    }
//
//    public QueueableGoal getGoalAmenityAsQueueableGoal() {
//        return QueueableGoal.toQueueableGoal(this.goalAmenity);
//    }
//    public boolean isInteracting() {
//        return isInteracting;
//    }
//    public boolean isSimultaneousInteractionAllowed() {
//        return isSimultaneousInteractionAllowed;
//    }
//    public int getInteractionDuration() {
//        return interactionDuration;
//    }
//    public InteractionType getInteractionType() {
//        return interactionType;
//    }


    /***** SETTERS *****/
//    public void setPosition(Coordinates coordinates) {
////        final int timeElapsedExpiration = 10;
////        Patch previousPatch = this.currentPatch;
////        this.position.setX(coordinates.getX());
////        this.position.setY(coordinates.getY());
////
////        Patch newPatch = this.office.getPatch(new Coordinates(coordinates.getX(), coordinates.getY()));
////        if (!previousPatch.equals(newPatch)) {
////            previousPatch.removeAgent(this.parent);
////            newPatch.addAgent(this.parent);
////
////            SortedSet<Patch> previousPatchSet = previousPatch.getEnvironment().getAgentPatchSet();
////            SortedSet<Patch> newPatchSet = newPatch.getEnvironment().getAgentPatchSet();
////
////            if (previousPatchSet.contains(previousPatch) && hasNoAgent(previousPatch)) {
////                previousPatchSet.remove(previousPatch);
////            }
////
////            newPatchSet.add(newPatch);
////            this.currentPatch = newPatch;
////            updateRecentPatches(this.currentPatch, timeElapsedExpiration);
////        }
////        else {
////            updateRecentPatches(null, timeElapsedExpiration);
////        }
//    }
//    public void setCurrentPatch(Patch currentPatch) {
//        this.currentPatch = currentPatch;
//    }
//    public void setCurrentPath(AgentPath currentPath) {
//        this.currentPath = currentPath;
//    }
//    public void setStateIndex(int stateIndex) {
//        this.stateIndex = stateIndex;
//    }
//    public void setActionIndex(int actionIndex) {
//        this.actionIndex = actionIndex;
//    }
//    public void setNextState(int i) {
//        this.currentState = this.currentState.getRoutePlan().setNextState(i);
//    }
//    public void setPreviousState(int i) {
//        this.currentState = this.currentState.getRoutePlan().setPreviousState(i);
//    }
//    public void setCurrentAction(Action currentAction) {
//        this.currentAction = currentAction;
//    }
//    public void setCurrentAmenity(Amenity currentAmenity) {
//        this.currentAmenity = currentAmenity;
//    }
//    public void setCurrentPatchField(PatchField currentPatchField) {
//        this.currentPatchField = currentPatchField;
//    }
//    public void setGoalAttractor(Amenity.AmenityBlock goalAttractor) {
//        this.goalAttractor = goalAttractor;
//    }
//    public void setGoalAmenity(Amenity goalAmenity) {
//        this.goalAmenity = goalAmenity;
//    }
//    public void setGoalQueueingPatchField(QueueingPatchField goalQueueingPatchField) {
//        this.goalQueueingPatchField = goalQueueingPatchField;
//    }
//    public void setRoutePlan(RoutePlan routePlan) {
//        this.routePlan = routePlan;
//    }
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//    public void setTickEntered(int tickEntered) {
//        this.tickEntered = tickEntered;
//    }
//    public void setInteracting(boolean interacting) {
//        isInteracting = interacting;
//    }
//    public void setSimultaneousInteractionAllowed(boolean simultaneousInteractionAllowed) {
//        isSimultaneousInteractionAllowed = simultaneousInteractionAllowed;
//    }
//    public void setInteractionDuration(int interactionDuration) {
//        this.interactionDuration = interactionDuration;
//    }
//    public void setInteractionType(InteractionType interactionType) {
//        this.interactionType = interactionType;
//    }

}