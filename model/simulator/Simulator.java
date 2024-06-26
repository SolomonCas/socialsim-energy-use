package com.socialsim.model.simulator;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.socialsim.controller.Main;
import com.socialsim.controller.controls.ScreenController;
import com.socialsim.model.core.agent.*;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
import com.socialsim.model.core.environment.position.Coordinates;

public class Simulator {


    /***** VARIABLES ******/
    private Environment environment;
    private final AtomicBoolean running;
    private final SimulationTime time;
    private final Semaphore playSemaphore;


    public static final Random RANDOM_NUMBER_GENERATOR;

    static {
        RANDOM_NUMBER_GENERATOR = new Random();
    }
    public static double roll(){
        return RANDOM_NUMBER_GENERATOR.nextDouble();
    }

    public static int rollInt(){
        return RANDOM_NUMBER_GENERATOR.nextInt(100);
    }

    public static int rollIntIN(int num) {return RANDOM_NUMBER_GENERATOR.nextInt(num);}


    private final int MAX_DIRECTORS = 1;
    // SEATING ARRANGEMENT
    public static List<Integer> FACULTY_1 = new LinkedList<Integer>(List.of(0));
    public static List<Integer> FACULTY_2 = new LinkedList<Integer>(List.of(36));
    public static List<Integer> FACULTY_3 = new LinkedList<Integer>(List.of(44));
    public static List<Integer> FACULTY_4 = new LinkedList<Integer>(List.of(52));
    public static List<Integer> STUDENT_1 = new LinkedList<Integer>(List.of(5, 6, 7, 8));
    public static List<Integer> STUDENT_2 = new LinkedList<Integer>(List.of(12, 13, 1, 2));
    public static List<Integer> STUDENT_3 = new LinkedList<Integer>(List.of(20, 21, 22, 23, 24));
    public static List<Integer> STUDENT_4 = new LinkedList<Integer>(List.of(28, 29, 30, 31, 32));

    public static List<Integer> FREE_SPACE = new LinkedList<Integer>(List.of(14, 15, 3, 4, 37, 38, 39, 40, 41, 42, 45, 46, 47, 48, 49, 50, 53, 54, 55, 56, 57, 58));

    // Current Agent Count Per Type
    public static int currentDirectorCount = 0;
    public static int currentFacultyCount = 0;
    public static int currentStudentCount = 0;
    public static int currentMaintenanceCount = 0;
    public static int currentGuardCount = 0;


    // Current Interaction Count
    public static int currentNonverbalCount = 0;
    public static int currentCooperativeCount = 0;
    public static int currentExchangeCount = 0;

    // Current Power Consumption Interaction Count
    public static int currentAirconInteractionCount = 0;
    public static int currentLightInteractionCount = 0;
    public static int currentFridgeInteractionCount = 0;
    public static int currentWaterDispenserInteractionCount = 0;



    // Average Interaction Duration
    public static float averageNonverbalDuration = 0;
    public static float averageCooperativeDuration = 0;
    public static float averageExchangeDuration = 0;


    // Current Team Count
    public static int currentTeam1Count = 0;
    public static int currentTeam2Count = 0;
    public static int currentTeam3Count = 0;
    public static int currentTeam4Count = 0;


    // Current Director to ____ Interaction Count
    public static int currentDirectorFacultyCount = 0;
    public static int currentDirectorStudentCount = 0;
    public static int currentDirectorMaintenanceCount = 0;
    public static int currentDirectorGuardCount = 0;


    // Current Faculty to ____ Interaction Count
    public static int currentFacultyFacultyCount = 0;
    public static int currentFacultyStudentCount = 0;
    public static int currentFacultyMaintenanceCount = 0;
    public static int currentFacultyGuardCount = 0;


    // Current Student to ____ Interaction Count
    public static int currentStudentStudentCount = 0;
    public static int currentStudentMaintenanceCount = 0;
    public static int currentStudentGuardCount = 0;


    // Current Maintenance to ____ Interaction Count
    public static int currentMaintenanceMaintenanceCount = 0;
    public static int currentMaintenanceGuardCount = 0;
    

    // Current Guard to Guard Interaction Count
    public static int currentGuardGuardCount = 0;
    
    
    /** COMPILED **/

    // Current Agent Count Per Type
    public static int[] compiledCurrentDirectorCount;
    public static int[] compiledCurrentFacultyCount;
    public static int[] compiledCurrentStudentCount;
    public static int[] compiledCurrentMaintenanceCount;
    public static int[] compiledCurrentGuardCount;

    // Current Interaction Count
    public static int[] compiledCurrentNonverbalCount;
    public static int[] compiledCurrentCooperativeCount;
    public static int[] compiledCurrentExchangeCount;

    // Current Power Consumption Interaction Count
    public static int[] compiledCurrentAirconInteractionCount;
    public static int[] compiledCurrentLightInteractionCount;
    public static int[] compiledCurrentFridgeInteractionCount;
    public static int[] compiledCurrentWaterDispenserInteractionCount;

    // Average Interaction Duration
    public static float[] compiledAverageNonverbalDuration;
    public static float[] compiledAverageCooperativeDuration;
    public static float[] compiledAverageExchangeDuration;


    // Current Team Count
    public static int[] compiledCurrentTeam1Count;
    public static int[] compiledCurrentTeam2Count;
    public static int[] compiledCurrentTeam3Count;
    public static int[] compiledCurrentTeam4Count;


    // Current Director to ____ Interaction Count
    public static int[] compiledCurrentDirectorFacultyCount;
    public static int[] compiledCurrentDirectorStudentCount;
    public static int[] compiledCurrentDirectorMaintenanceCount;
    public static int[] compiledCurrentDirectorGuardCount;


    // Current Faculty to ____ Interaction Count
    public static int[] compiledCurrentFacultyFacultyCount;
    public static int[] compiledCurrentFacultyStudentCount;
    public static int[] compiledCurrentFacultyMaintenanceCount;
    public static int[] compiledCurrentFacultyGuardCount;


    // Current Student to ____ Interaction Count
    public static int[] compiledCurrentStudentStudentCount;
    public static int[] compiledCurrentStudentMaintenanceCount;
    public static int[] compiledCurrentStudentGuardCount;


    // Current Maintenance to ____ Interaction Count
    public static int[] compiledCurrentMaintenanceMaintenanceCount;
    public static int[] compiledCurrentMaintenanceGuardCount;


    // Current Guard to Guard Interaction Count
    public static int[] compiledCurrentGuardGuardCount;


    /** Patch Count **/
    public static int[][] currentPatchCount;







    

    /***** CONSTRUCTOR ******/
    public Simulator() {
        this.environment = null;
        this.running = new AtomicBoolean(false);
        this.time = new SimulationTime(11, 30, 0);
        this.playSemaphore = new Semaphore(0);
        this.start();
    }

    
    
    


    /***** METHODS ******/
    private void start() {
        new Thread(() -> {
            final int speedAwarenessLimitMilliseconds = 10;

            while(true) {
                try {
                    playSemaphore.acquire();

                    while(this.isRunning()) {
                        long currentTick = this.time.getStartTime().until(this.time.getTime(), ChronoUnit.SECONDS) / 5;
                        try {
                            updateAgentsInEnvironment(environment, currentTick, this.time);
                            spawnAgent(environment, this.time);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        ((ScreenController) Main.mainScreenController).drawEnvironmentViewForeground(Main.simulator.getEnvironment(), SimulationTime.SLEEP_TIME_MILLISECONDS.get() < speedAwarenessLimitMilliseconds);

                        this.time.tick();
                        Thread.sleep(SimulationTime.SLEEP_TIME_MILLISECONDS.get());

                        if ((this.time.getStartTime().until(this.time.getTime(), ChronoUnit.SECONDS) / 5) == 6480) {
                            ((ScreenController) Main.mainScreenController).playAction();
                            break;
                        }
                    }



                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

        }).start();
    }

    public void resetToDefaultConfiguration(Environment environment) {
        this.environment = environment;
        replenishStaticVars();
        Agent.clearAgentCounts();
        this.time.reset();
        this.running.set(false);
    }

    public void reset() {
        this.time.reset();
    }
    
    /** Environment **/


    /** Agents **/
    // Sets where agents spawn and can set what will be their assigned seat
    private void spawnAgent(Environment environment, SimulationTime time) {
//        Gate gate = environment.getGates().get(2);
        Elevator elevator = null;
        Elevator elevator1 = environment.getElevators().get(0);
        Elevator elevator2 = environment.getElevators().get(1);
        Elevator elevator3 = environment.getElevators().get(2);

        int gateNum = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(3);
        switch (gateNum) {
            case 0 -> elevator = elevator1;
            case 1 -> elevator = elevator2;
            case 2 -> elevator = elevator3;
        }


//        Agent agent = null;

        for (int i = 0; i < elevator.getSpawners().size(); i++) {
            Elevator.ElevatorBlock spawner = elevator.getSpawners().get(i);
            int spawnChance = (int) elevator.getChancePerTick();
            int CHANCE = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(100);


//            if (CHANCE > spawnChance) {
                if (!environment.getUnspawnedWorkingAgents().isEmpty()){
                    for (Agent agent : environment.getUnspawnedWorkingAgents()){
                        agent = environment.getUnspawnedWorkingAgents().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getUnspawnedWorkingAgents().size()));
                        int team = agent.getTeam();
                        if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.GUARD && Agent.guardCount != 1) { // Agent.guardCount != 1 isn't dynamic yet
                            agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
                                    spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(),
                                    environment.getReceptionTables().getFirst().getReceptionChairs().getFirst()));
                            environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                            Agent.guardCount++;
                            Agent.agentCount++;
                            currentGuardCount++;
                            System.out.println("my energy profile is: "+ agent.getEnergyProfile() + "AGENT: " + agent.getType());
                        }
                        else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.MAINTENANCE && Agent.maintenanceCount != 2) { // Agent.maintenanceCount != 2 isn't dynamic yet
                            // commenting this out because there are new changes
//                            agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
//                                     spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(), environment.getCubicles().get(FREE_SPACE.getFirst()) // Need to improve this
//                                )
//                            );
                            FREE_SPACE.removeFirst();
                            environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                            Agent.maintenanceCount++;
                            Agent.agentCount++;
                            currentMaintenanceCount++;
                            System.out.println("my energy profile is: "+ agent.getEnergyProfile() + "AGENT: " + agent.getType());
                        }
                        else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.DIRECTOR && Agent.directorCount != MAX_DIRECTORS) {
                            agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
                                    spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(),
                                    environment.getDirectorTables().getFirst().getDirectorChairs().getFirst()));
                            environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                            Agent.directorCount++;
                            Agent.agentCount++;
                            currentDirectorCount++;
                            System.out.println("my energy profile is: "+ agent.getEnergyProfile() + "AGENT: " + agent.getType());
                        }
                    }


//                    else if (agent.getType() == Agent.Type.FACULTY && team == 1 && FACULTY_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FACULTY_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FACULTY_1.remove(0);
//                        currentFacultyCount++;
//                        currentTeam1Count++;
//                        Agent.facultyCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.STUDENT && team == 1 && STUDENT_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(STUDENT_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        STUDENT_1.remove(0);
//                        currentStudentCount++;
//                        currentTeam1Count++;
//                        Agent.studentCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.FACULTY && team == 2 && FACULTY_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FACULTY_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FACULTY_2.remove(0);
//                        currentFacultyCount++;
//                        currentTeam2Count++;
//                        Agent.facultyCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.STUDENT && team == 2 && STUDENT_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(STUDENT_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        STUDENT_2.remove(0);
//                        currentStudentCount++;
//                        currentTeam2Count++;
//                        Agent.studentCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.FACULTY && team == 3 && FACULTY_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FACULTY_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FACULTY_3.remove(0);
//                        currentFacultyCount++;
//                        currentTeam3Count++;
//                        Agent.facultyCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.STUDENT && team == 3 && STUDENT_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(STUDENT_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        STUDENT_3.remove(0);
//                        currentStudentCount++;
//                        currentTeam3Count++;
//                        Agent.studentCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.FACULTY && team == 4 && FACULTY_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FACULTY_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FACULTY_4.remove(0);
//                        currentFacultyCount++;
//                        currentTeam4Count++;
//                        Agent.facultyCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.STUDENT && team == 4 && STUDENT_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(STUDENT_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        STUDENT_4.remove(0);
//                        currentStudentCount++;
//                        currentTeam4Count++;
//                        Agent.studentCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.FACULTY && FREE_SPACE.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FREE_SPACE.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FREE_SPACE.remove(0);
//                        currentFacultyCount++;
//                        Agent.facultyCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.STUDENT && FREE_SPACE.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(FREE_SPACE.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        FREE_SPACE.remove(0);
//                        currentStudentCount++;
//                        Agent.studentCount++;
//                        Agent.agentCount++;
//                    }
                }
//            }
        }
    }

    public void spawnInitialAgents(Environment environment) {
        environment.createInitialAgentDemographics();
    }

    public static void updateAgentsInEnvironment(Environment environment, long currentTick, SimulationTime time) throws InterruptedException {
        moveAll(environment, currentTick, time);

        // Current Agent Count Per Type
        compiledCurrentDirectorCount[(int) currentTick] = currentDirectorCount;
        compiledCurrentFacultyCount[(int) currentTick] = currentFacultyCount;
        compiledCurrentStudentCount[(int) currentTick] = currentStudentCount;
        compiledCurrentMaintenanceCount[(int) currentTick] = currentMaintenanceCount;
        compiledCurrentGuardCount[(int) currentTick] = currentGuardCount;


        // Current Interaction Count
        compiledCurrentNonverbalCount[(int) currentTick] = currentNonverbalCount;
        compiledCurrentCooperativeCount[(int) currentTick] = currentCooperativeCount;
        compiledCurrentExchangeCount[(int) currentTick] = currentExchangeCount;

        // Current Power Consumption Interaction Count
        compiledCurrentAirconInteractionCount[(int) currentTick] = currentNonverbalCount;
        compiledCurrentLightInteractionCount[(int) currentTick] = currentCooperativeCount;
        compiledCurrentFridgeInteractionCount[(int) currentTick] = currentExchangeCount;
        compiledCurrentWaterDispenserInteractionCount[(int) currentTick] = currentExchangeCount;

        // Average Interaction Duration
        compiledAverageNonverbalDuration[(int) currentTick] = averageNonverbalDuration;
        compiledAverageCooperativeDuration[(int) currentTick] = averageCooperativeDuration;
        compiledAverageExchangeDuration[(int) currentTick] = averageExchangeDuration;


        // Current Team Count
        compiledCurrentTeam1Count[(int) currentTick] = currentTeam1Count;
        compiledCurrentTeam2Count[(int) currentTick] = currentTeam2Count;
        compiledCurrentTeam3Count[(int) currentTick] = currentTeam3Count;
        compiledCurrentTeam4Count[(int) currentTick] = currentTeam4Count;


        // Current Director to ____ Interaction Count
        compiledCurrentDirectorFacultyCount[(int) currentTick] = currentDirectorFacultyCount;
        compiledCurrentDirectorStudentCount[(int) currentTick] = currentDirectorStudentCount;
        compiledCurrentDirectorMaintenanceCount[(int) currentTick] = currentDirectorMaintenanceCount;
        compiledCurrentDirectorGuardCount[(int) currentTick] = currentDirectorGuardCount;


        // Current Faculty to ____ Interaction Count
        compiledCurrentFacultyFacultyCount[(int) currentTick] = currentFacultyFacultyCount;
        compiledCurrentFacultyStudentCount[(int) currentTick] = currentFacultyStudentCount;
        compiledCurrentFacultyMaintenanceCount[(int) currentTick] = currentFacultyMaintenanceCount;
        compiledCurrentFacultyGuardCount[(int) currentTick] = currentFacultyGuardCount;


        // Current Student to ____ Interaction Count
        compiledCurrentStudentStudentCount[(int) currentTick] = currentStudentStudentCount;
        compiledCurrentStudentMaintenanceCount[(int) currentTick] = currentStudentMaintenanceCount;
        compiledCurrentStudentGuardCount[(int) currentTick] = currentStudentGuardCount;


        // Current Maintenance to ____ Interaction Count
        compiledCurrentMaintenanceMaintenanceCount[(int) currentTick] = currentMaintenanceMaintenanceCount;
        compiledCurrentMaintenanceGuardCount[(int) currentTick] = currentMaintenanceGuardCount;


        // Current Guard to Guard Interaction Count
        compiledCurrentGuardGuardCount[(int) currentTick] = currentGuardGuardCount;
    }

    private static void moveAll(Environment environment, long currentTick, SimulationTime time) {
        int bathroomReserves = environment.numBathroomsFree();
        for (Agent agent : environment.getMovableAgents()) {
            try {
//                if (currentTick == 2160 && agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE() != null) {
//                    agent.getAgentMovement().setNextState(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().indexOf(agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE()) - 1);
//                    agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().indexOf(agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE()));
//                    agent.getAgentMovement().setActionIndex(0);
//                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(0));
//                    agent.getAgentMovement().resetGoal();
//                }

                if(time.getTime().isBefore(LocalTime.NOON)) {
                    agent.getAgentMovement().getRoutePlan().setBathAM(true);
                    agent.getAgentMovement().getRoutePlan().setBathPM(false);
                }
                else {
                    agent.getAgentMovement().getRoutePlan().setBathAM(false);
                    agent.getAgentMovement().getRoutePlan().setBathPM(true);
                }


                // Lunch Time for Anyone except for Maintenance (Maybe need to improve on this code)
                if (time.getTime().equals(LocalTime.NOON) && agent.getType() != Agent.Type.MAINTENANCE) {
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().resetGoal();
                    }
                }

                // Lunch Time for Maintenance
                if (time.getTime().equals(LocalTime.of(11,0)) && agent.getType() != Agent.Type.MAINTENANCE) {
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().resetGoal();
                    }
                }

                if (time.getTime().equals(agent.getTimeOut())) {
                    System.out.println("LEAVING");
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                    if(index != -1) {
                        agent.getAgentMovement().getRoutePlan().setBATH_PM(-1);
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().resetGoal();
                    }

                }

//                if(agent.getAgentMovement() != null && agent.getTeam() > 0 && currentTick == agent.getAgentMovement().getRoutePlan().getMeetingStart()){
//                    if (agent.getAgentMovement().getCurrentState().getName() == State.Name.WORKING) {
//                        agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getStateIndex() - 1);
//                    }
//                    else if (agent.getAgentMovement().getCurrentState().getName() == State.Name.EATING_LUNCH) {
//                        agent.getAgentMovement().getRoutePlan().setLunchAmenity(null);
//                        agent.getAgentMovement().getRoutePlan().setLunchAttractor(null);
//                    }
//
//                    if (agent.getAgentMovement().getGoalAttractor() != null) {
//                        agent.getAgentMovement().getGoalAttractor().setIsReserved(false);
//                    }
//                    agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(agent.getAgentMovement().getStateIndex() + 1, agent.getAgentMovement().getRoutePlan().addUrgentRoute("MEETING", agent));
//                    agent.getAgentMovement().setNextState(agent.getAgentMovement().getStateIndex());
//                    agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getStateIndex() + 1);
//                    agent.getAgentMovement().setActionIndex(0);
//                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
//                    agent.getAgentMovement().resetGoal();
//                }
//                if (agent.getAgentMovement().getCurrentState().getName() == State.Name.WAIT_INFRONT_OF_BATHROOM){
//                    if (bathroomReserves > 0){
//                        agent.getAgentMovement().setNextState(agent.getAgentMovement().getStateIndex());
//                        agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getStateIndex() + 1);
//                        agent.getAgentMovement().setActionIndex(0);
//                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
//                        if (agent.getAgentMovement().getGoalAttractor() != null) {
//                            agent.getAgentMovement().getGoalAttractor().setIsReserved(false);
//                        }
//                        agent.getAgentMovement().resetGoal();
//                        bathroomReserves--;
//                    }
//                }

                moveOne(agent, currentTick, time);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void doCommonAction(AgentMovement agentMovement, State state, Action action, Agent agent, Agent.Type type,
                          Agent.Persona persona, Environment environmentInstance, long currentTick, SimulationTime time) {
        boolean isFull = false;
        if (action.getName() == Action.Name.GO_TO_STATION || action.getName() == Action.Name.GO_TO_DIRECTOR_ROOM || action.getName() == Action.Name.EXIT_LUNCH) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
            }

            if (agentMovement.chooseNextPatchInPath()) {
                agentMovement.faceNextPosition();
                agentMovement.moveSocialForce();
                if (agentMovement.hasReachedNextPatchInPath()) {
                    agentMovement.reachPatchInPath();
                    if(agentMovement.hasAgentReachedFinalPatchInPath()){
                        if(action.getName() == Action.Name.EXIT_LUNCH){
                            agentMovement.getCurrentPatch().getAgents().remove(agent);
                        }

                        agentMovement.getRoutePlan().setAtDesk(true);
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                    }
                }
            }
            else if (agentMovement.getDuration() != -1){ // if duration has been set
                System.out.println("duration: "+ agentMovement.getDuration());
                agentMovement.setDuration(agentMovement.getDuration() - 1);
                if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                    agentMovement.setActionIndex(0); // JIC if needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    }
                    agentMovement.resetGoal();
                }
                if (agentMovement.getCurrentState().getActions().isEmpty()){
                    if(action.getName() == Action.Name.EXIT_LUNCH){
                        agentMovement.getCurrentPatch().getAgents().add(agent);
                    }
                    if (state.getName() != State.Name.GOING_HOME) { // This is important for GOING_HOME state
                        agentMovement.getRoutePlan().setAtDesk(false);
                    }
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                }
            }
            else { // if duration is set to -1
                agentMovement.setSimultaneousInteractionAllowed(false);
                if (agentMovement.getGoalAmenity() == null) {
                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                }

                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) { // checks if the agent is right next to the patch
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) { // checks if the agent is in the patch itself
                            agentMovement.getRoutePlan().setCanUrgent(-1);
                            agentMovement.getRoutePlan().setAtDesk(true);
                        }
                    }

                }
                if (agentMovement.getRoutePlan().getCanUrgent() < 2) {
                    double CHANCE = Simulator.roll();
                    //System.out.println("CHANCE: " + CHANCE + " Type: " + type + " State: " + state.getName() + " Action: " + action.getName());
                    if (CHANCE < RoutePlan.BATH_CHANCE) {
                        if (agentMovement.getRoutePlan().isBathAM() && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                            agentMovement.getRoutePlan().setBathAM(true); // IG meaning that the agent will take a bathroom break in the morning (7:30 - 11:59)
                            agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setCanUrgent(1);
                        }
                        else if(agentMovement.getRoutePlan().getBATH_PM() > 0) {
                            agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setBathPM(true);
                            agentMovement.getRoutePlan().setCanUrgent(1);
                        }
                        else {

                        }
                    }
                    else if (CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.BREAK_CHANCE && agentMovement.getRoutePlan().getBREAK_COUNT() > 0) {
                        // Developer Note: Adding break action is different from the rest of the actions.
                        // Because it takes into account the urgent actions in the model. When adding an urgent route,
                        // I highly advise to Ctrl + F addUrgentRoute or CHANCE to get the structure and logic of the code.
                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BREAK", agent)); // add the break state
                        agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the break state
                        agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to break
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setBREAK_COUNT(1); // indicate how many breaks can an agent do
                    }
                }
            }

        }
        else if (action.getName()== Action.Name.GO_TO_BATHROOM) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (!agentMovement.chooseBathroomGoal(Toilet.class)) {
                    // If full add waiting state
                    // Should be +1 to the state index because it will be removed eventually
                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addWaitingRoute(agent));
                    agentMovement.setNextState(agentMovement.getStateIndex());
                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                    agentMovement.setActionIndex(0);
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    isFull = true;
                }
                else {
                    agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current location
                    if (agentMovement.getRoutePlan().isBathAM()) { // Take a bathroom break at the morning
                        agentMovement.getRoutePlan().setBathAM(false);
                        agentMovement.getRoutePlan().setBATH_AM(1);
                    }
                    else if (agentMovement.getRoutePlan().isBathPM()) { // Take a bathroom break at afternoon
                        agentMovement.getRoutePlan().setBathPM(false);
                        agentMovement.getRoutePlan().setBATH_PM(1);
                    }
                    else {
                        agentMovement.getRoutePlan().setBATH_LUNCH(1);
                    }
                }
            }

            if (isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                // Expecting to go to RELIEVE_IN_CUBICLE
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            }

                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/RELIEVE_IN_CUBICLE is not in the the state action
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                }
            }
        }
        else if (action.getName()==Action.Name.RELIEVE_IN_CUBICLE) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
            agentMovement.setDuration(agentMovement.getDuration() - 1);

            if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using the toilet
                agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.resetGoal();
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()){
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                agentMovement.setStateIndex(0); // JIC if needed
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.resetGoal();
            }
        }
        else if (action.getName() == Action.Name.FIND_SINK) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (!agentMovement.chooseBathroomGoal(Sink.class)) {
                    // if full, skip washing hands HAHAHAHA
                    isFull = true;
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                }
            }
            if (isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());

                            agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            }
                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/WASH_IN_SINK is not in the state action
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.WASH_IN_SINK) {
            agentMovement.setSimultaneousInteractionAllowed(true);
            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
            agentMovement.setDuration(agentMovement.getDuration() - 1);

            if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using the sink
                agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.resetGoal();
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()){
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                agentMovement.setStateIndex(0); // JIC if needed
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.getRoutePlan().setCanUrgent(1);
                agentMovement.resetGoal();
            }
        }
        else if(action.getName() == Action.Name.GOING_DISPENSER){
            agentMovement.setSimultaneousInteractionAllowed(false);
            if(agentMovement.getGoalAmenity() == null){
                if(!agentMovement.chooseGoal(WaterDispenser.class)){
                    isFull = true;
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                }else{
                    agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current location

                    if(currentTick < 4320){
                        agentMovement.getRoutePlan().setDISPENSER_LUNCH(0);
                    }else{
                        agentMovement.getRoutePlan().setDISPENSER_PM(0);
                    }
                }
            }
            if (isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            }
                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/GETTING_WATER is not in the the state action
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.GETTING_WATER) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
            System.out.println("duration:"+ agentMovement.getDuration());
            agentMovement.setDuration(agentMovement.getDuration() - 1);
            if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {

                agentMovement.getGoalAttractor().setIsReserved(false); // Done using the dispenser
                agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.resetGoal();
                currentWaterDispenserInteractionCount++;
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()){
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                agentMovement.setStateIndex(0); // JIC if needed
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.resetGoal();
            }
        }
        else if(action.getName() == Action.Name.GOING_FRIDGE){
            agentMovement.setSimultaneousInteractionAllowed(false);
            if(agentMovement.getGoalAmenity() == null){
                if(!agentMovement.chooseGoal(Refrigerator.class)){
                    isFull = true;
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                }else{
                    agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current location
                    if(currentTick < 4320){
                        agentMovement.getRoutePlan().setREFRIGERATOR_LUNCH(0);
                    }else{
                        agentMovement.getRoutePlan().setREFRIGERATOR_PM(0);
                    }
                }
            }
            if (isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/GETTING_FOOD is not in the the state action
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }

                        }
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.GETTING_FOOD) {
            System.out.println("duration:"+ agentMovement.getDuration());

            agentMovement.setSimultaneousInteractionAllowed(false);
            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
            agentMovement.setDuration(agentMovement.getDuration() - 1);
            if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using the toilet
                agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.resetGoal();

                currentFridgeInteractionCount++;
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()){
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                agentMovement.setStateIndex(0); // JIC if needed
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.resetGoal();
            }
        }
        else if (action.getName() == Action.Name.LEAVE_OFFICE) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {

                if(!agentMovement.getRoutePlan().isAtDesk()){
                    agentMovement.setActionIndex(agentMovement.getActionIndex() + 1); // SET ACTION TO GO_TO_STATION WHICH SHOULD BE SET ON THE ROUTE PLAN
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                }
                else{ // Use destination in route plan
                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                }
            }
            else if (agentMovement.chooseNextPatchInPath()) {
                agentMovement.faceNextPosition();
                agentMovement.moveSocialForce();
                if (agentMovement.hasReachedNextPatchInPath()) {
                    agentMovement.reachPatchInPath();
                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                        agentMovement.despawn(time.getTime());
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.GO_TO_LUNCH || action.getName() == Action.Name.GO_TO_BREAK) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (agentMovement.getRoutePlan().getLunchAmenity() == null) {
                    double CHANCE = Simulator.roll();

                    if ((type == Agent.Type.MAINTENANCE && CHANCE < RoutePlan.MAINTENANCE_LUNCH) ||
                            (type == Agent.Type.DIRECTOR && CHANCE < RoutePlan.DIRECTOR_LUNCH) ||
                            ((persona == Agent.Persona.APP_FACULTY || persona == Agent.Persona.EXT_STUDENT) && CHANCE < RoutePlan.EXT_LUNCH) ||
                            ((persona == Agent.Persona.STRICT_FACULTY || persona == Agent.Persona.INT_STUDENT) && CHANCE < RoutePlan.INT_LUNCH)) {
                        System.out.println("Eat in chosen break seat");
                        if (!agentMovement.chooseBreakSeat()) {
                            System.out.println("Eat on assigned seat");
                            // The destination will be based on the assigned seat
                            agentMovement.setGoalAmenity(agentMovement.getAssignedSeat());
                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                        }
                        else {
                            agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current location
                        }
                    }
                    else if (CHANCE < RoutePlan.EAT_OUTSIDE && type == Agent.Type.STUDENT ) {
                        // eat outside
                        System.out.println("Eat on outside");
                        if(agent.getTeam() != 0) {
                            // Eat with team members excluding the faculty.
                            // The faculty is used as the Thesis adviser or Faculty Coordinator(?) ex. Doc Briane.
                            ArrayList<Agent> agents = environmentInstance.getTeamMembers(agent.getTeam());
                            for(Agent agent1 : agents) {
                                if(agent1.getType() == Agent.Type.STUDENT) {

                                    int index = agent1.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                                    if (index != -1) {
                                        agent1.getAgentMovement().setCurrentState(index); // Set to EATING_LUNCH
                                        agent1.getAgentMovement().setStateIndex(index); // Set the index to EATING_LUNCH
                                        agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan()
                                                .remove(agentMovement.getStateIndex()); // removing finished state
                                        agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("EAT_OUTSIDE", agent, environmentInstance));
                                        agent1.getAgentMovement().setActionIndex(0); // JIC if needed
                                        agent1.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                        agent1.getAgentMovement().resetGoal();
                                    }
                                    else {
                                        System.out.println(type + " does not have EATING_LUNCH in their Route Plan or done eating");
                                    }
                                }

                            }
                        }
                        else {
                            // If no team
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("EAT_OUTSIDE", agent, environmentInstance));
                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.resetGoal();
                        }

                        isFull = true; // convenient way to skip the rest of the code
                    }

                    else {
                        System.out.println("ELSE: Eat on assigned seat");
                        agentMovement.setGoalAmenity(agentMovement.getAssignedSeat());
                        agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                    }

                }
                else {
                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
                }

            }

            if(isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.getRoutePlan().setAtDesk(false);
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            // Developer Note: DO NOT REMOVE THE GO_TO_BREAK or GO_TO_LUNCH action for it serves to know
                            // where does the agent go when doing an urgent task
                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1); // go to the next action
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                // The condition below is important it set the last duration of when the agent stop doing
                                // a task (i.e. TAKING_BREAK or EAT_LUNCH)
                                if (agentMovement.getRoutePlan().getLastDuration() == -1) { // if not set yet
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                }
                                else { // if it was set, get the last duration (Check TAKING_BREAK or EAT_LUNCH)
                                    agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
                                    agentMovement.getRoutePlan().setLastDuration(-1);
                                }
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
                                agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
                            }

                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/EAT_LUNCH or TAKING_BREAK is not in the the state action
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }

                        }
                    }
                }
            }


        }
        else if (action.getName() == Action.Name.EAT_LUNCH || action.getName() == Action.Name.TAKING_BREAK) {
            agentMovement.setSimultaneousInteractionAllowed(true);
            agentMovement.setDuration(agentMovement.getDuration() - 1);

            // Developer Note: these actions will be different from the others for it handles the urgent task.
            // To know the urgent task, refer to the RoutePlan addUrgentRoute function/s
            if (agentMovement.getDuration() <= 0) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using the amenity
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                agentMovement.setStateIndex(0); // JIC if needed
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.resetGoal();
                agentMovement.getRoutePlan().setLunchAmenity(null);
                agentMovement.getRoutePlan().setLunchAttractor(null);
            }
            else if (agentMovement.getRoutePlan().getCanUrgent() < 2) {
                double CHANCE = Simulator.roll();

                if (CHANCE < RoutePlan.BATH_CHANCE && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
                    agentMovement.setNextState(agentMovement.getStateIndex());
                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                    agentMovement.setActionIndex(0);
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                    agentMovement.getRoutePlan().setCanUrgent(1);
                }
                else if(CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.DISPENSER_CHANCE && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0){
                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent));
                    agentMovement.setNextState(agentMovement.getStateIndex());
                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                    agentMovement.setActionIndex(0);
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                    agentMovement.getRoutePlan().setCanUrgent(1);
                }
                else if(CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.DISPENSER_CHANCE + RoutePlan.REFRIGERATOR_CHANCE && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0){
                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent));
                    agentMovement.setNextState(agentMovement.getStateIndex());
                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                    agentMovement.setActionIndex(0);
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                    agentMovement.getRoutePlan().setCanUrgent(1);
                }
            }

        }

        else if (action.getName() == Action.Name.GO_TO_WAIT_AREA) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if(!agentMovement.chooseWaitPatch()){
                    // if full skip this state by deleting this entirely. and go back to the original state
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    isFull = true;
                }
            }

            if (isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                        }
                    }
                }
            }
        }

        else if(action.getName() == Action.Name.WAIT_FOR_VACANT){
            agentMovement.setSimultaneousInteractionAllowed(true);
            agentMovement.setDuration(agentMovement.getDuration() - 1);
            if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                agentMovement.getWaitPatch().getAmenityBlock().setIsReserved(false); // done waiting
                agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.resetGoal();
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()){
                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                agentMovement.setPreviousState(agentMovement.getStateIndex()); // Go back to NEED_BATHROOM State
                agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
                agentMovement.setActionIndex(0); // JIC if needed
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                agentMovement.resetGoal();
            }
        }
        else {
            System.out.println(type + " does not have that action");
        }
    }

    private static void moveOne(Agent agent, long currentTick, SimulationTime time) throws Throwable {
        AgentMovement agentMovement = agent.getAgentMovement();

        Agent.Type type = agent.getType();
        Agent.Persona persona = agent.getPersona();
        State state = agentMovement.getCurrentState();
        Action action = agentMovement.getCurrentAction();
        Environment environmentInstance = agentMovement.getEnvironment();

        System.out.println("Type: " + type + " Persona: " + persona + " State: " + state.getName() + " Action: " + action.getName());
        System.out.println("canUrgent: " + agentMovement.getRoutePlan().getCanUrgent());
        boolean isFull = false;

        if (!agentMovement.isInteracting() || agentMovement.isSimultaneousInteractionAllowed()) {
            switch (type) {
                case GUARD:
                    if (state.getName() == State.Name.GUARD) {
                        if (action.getName() == Action.Name.GUARD_STAY_PUT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                            }

                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) { // checks if the agent is right next to the patch
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) { // checks if the agent is in the patch itself
                                        agentMovement.getRoutePlan().setCanUrgent(-1);
                                        agentMovement.getRoutePlan().setAtDesk(true);
                                    }
                                }

                            }
                            if (agentMovement.getRoutePlan().getCanUrgent() < 2) {
                                double CHANCE = Simulator.roll();
                                //System.out.println("CHANCE: " + CHANCE + " Type: " + type + " State: " + state.getName() + " Action: " + action.getName());
                                if (CHANCE < RoutePlan.BATH_CHANCE) {
                                    if (currentTick < 2160 && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                                        agentMovement.getRoutePlan().setBathAM(true); // IG meaning that the agent will take a bathroom break in the morning (7:30 - 11:59)
                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                                        agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setCanUrgent(1);
                                    }
                                    else if(agentMovement.getRoutePlan().getBATH_PM() > 0) {
                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                                        agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setBathPM(true);
                                        agentMovement.getRoutePlan().setCanUrgent(1);
                                    }
                                }
                                else if (CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.GUARD_BREAK_CHANCE && agentMovement.getRoutePlan().getBREAK_COUNT() > 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BREAK", agent)); // add the break state
                                    agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the break state
                                    agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to break
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setBREAK_COUNT(1); // indicate how many breaks can an agent do

                                }
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INSPECT_ROOMS) {
                        if (action.getName() == Action.Name.INSPECTING_ROOM) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            }

                            if (agentMovement.chooseNextPatchInPath() && !agentMovement.getRoutePlan().isAtDesk()) { // isAtDesk can essentially depict as checking if the agent is in the goal
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getRoutePlan().setAtDesk(true);
                                    }
                                }
                            }else{
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                    agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                                    agentMovement.setActionIndex(0); // JIC needed
                                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    }
                                    agentMovement.resetGoal();
                                }
                                if (agentMovement.getCurrentState().getActions().isEmpty()){
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                }
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }

                    break;
                case MAINTENANCE:
                    if (state.getName() == State.Name.GOING_TO_RECEPTION) {
                        if (action.getName() == Action.Name.GOING_TO_RECEPTION_QUEUE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalQueueingPatchField() == null) {
                                // Developer Note: The assumption of this is that there can be only one reception table
                                // within the environment and it chooses the patch that has the queueing patch field
                                // set on the ReceptionTableMapper.java
                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment()
                                        .getReceptionTables().getFirst().getAmenityBlocks().getLast().getPatch()
                                        .getQueueingPatchField().getKey());
                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getReceptionTables()
                                        .getFirst());
                            }

                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.joinQueue();
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.FILL_UP_NAME) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                }
                            }
                            else {
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.leaveQueue();
                                    agentMovement.setNextState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setCanUrgent(-1);
                                }
                            }
                        }
                    }
                    else if (state.getName() == State.Name.MAINTENANCE_BATHROOM) {
                        if (action.getName() == Action.Name.MAINTENANCE_CLEAN_TOILET) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if (!agentMovement.chooseBathroomGoal(OfficeToilet.class)) {
                                    // If full cleaning is already been done
                                    isFull = true;
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                }
                            }
                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    agentMovement.checkIfStuck();
                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {

                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.getGoalAttractor().setIsReserved(false);
                                        agentMovement.resetGoal();

                                    }
                                    if (agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                }
                            }

                        }
                    }
                    else if (state.getName() == State.Name.INSPECT_ROOMS) {
                        if (action.getName() == Action.Name.INSPECTING_ROOM) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                            }

                            if (agentMovement.chooseNextPatchInPath() && !agentMovement.getRoutePlan().isAtDesk()) { // isAtDesk can essentially depict as checking if the agent is in the goal
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getRoutePlan().setAtDesk(true);
                                    }
                                }
                            }else{
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                    agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                                    agentMovement.setActionIndex(0); // JIC needed
                                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    }
                                    agentMovement.resetGoal();
                                }
                                if (agentMovement.getCurrentState().getActions().isEmpty()){
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                }
                            }
                        }
                    }
                    else if (state.getName() == State.Name.MAINTENANCE_PLANT) {
                        if (action.getName() == Action.Name.MAINTENANCE_WATER_PLANT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if (!agentMovement.choosePlantGoal()) {
                                    // If full cleaning is already been done
                                    isFull = true;
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                }
                            }
                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    agentMovement.checkIfStuck();
                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {
                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0 && !agentMovement.getCurrentState().getActions().isEmpty()) {
                                        environmentInstance.setPlantWatered(agentMovement.getGoalAmenity().getAttractors().getFirst().getPatch()); // set the plant to watered
                                        agentMovement.getCurrentState().getActions().removeFirst(); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.resetGoal();
                                    }
                                    if (agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                }
                            }

                        }
                    }
                    else if (state.getName() == State.Name.WAIT_FOR_ACTIVITY) {
                        if (action.getName() == Action.Name.GO_TO_WAIT_AREA) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                isFull = agentMovement.chooseBreakSeat();
                            }

                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath() && !agentMovement.getRoutePlan().isAtDesk()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    if (agentMovement.hasReachedNextPatchInPath()) { // checks if the agent is right next to the patch
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                            agentMovement.getRoutePlan().setCanUrgent(-2);
                                            agentMovement.getRoutePlan().setAtDesk(true);
                                            if (agentMovement.getAssignedSeat() == null) {
                                                agentMovement.getRoutePlan().setAgentSeat(agentMovement.getGoalAmenity()); // Sets maintenance's assigned sit
                                                agentMovement.setAssignedSeat(agentMovement.getGoalAmenity());
                                            }
                                        }
                                    }
                                }
                                if (agentMovement.getRoutePlan().getCanUrgent() < 2) {
                                    double CHANCE = Simulator.roll();
                                    System.out.println("CHANCE: " + CHANCE + " Type: " + type + " State: " + state.getName() + " Action: " + action.getName());
                                    if (CHANCE < RoutePlan.BATH_CHANCE) {
                                        if (currentTick < 2160 && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                                            agentMovement.getRoutePlan().setBathAM(true); // IG meaning that the agent will take a bathroom break in the morning (7:30 - 11:59)
                                            agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setCanUrgent(1);
                                        }
                                        else if(agentMovement.getRoutePlan().getBATH_PM() > 0) {
                                            agentMovement.setStateIndex(agentMovement.getStateIndex() + 1); // set the new state index to go to the bathroom state
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent)); // add the bathroom sate
                                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the bathroom state
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to the bathroom
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setBathPM(true);
                                            agentMovement.getRoutePlan().setCanUrgent(1);
                                        }
                                    }
                                    else if (CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.MAINTENANCE_BREAK_CHANCE && agentMovement.getRoutePlan().getBREAK_COUNT() > 0) {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BREAK", agent)); // add the break state
                                        agentMovement.setCurrentState(agentMovement.getStateIndex()); // set the new current state into the go to the break state
                                        agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex())); // also set the current action to go to break
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setBREAK_COUNT(1); // indicate how many breaks can an agent do

                                    }
                                }
                            }

                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }
                    break;

                case DIRECTOR:
                    if (state.getName() == State.Name.GOING_TO_RECEPTION) {
                        if (action.getName() == Action.Name.GOING_TO_RECEPTION_QUEUE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalQueueingPatchField() == null) {
                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getReceptionTables().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getReceptionTables().get(0));
                            }

                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.joinQueue();
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.FILL_UP_NAME) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                }
                            }
                            else {
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.leaveQueue();
                                    agentMovement.setNextState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setCanUrgent(-1);
                                }
                            }
                        }
                    }
                    else if (state.getName() == State.Name.MEETING) {
                        if (action.getName() == Action.Name.GO_MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.chooseMeetingGoal(agentMovement.getRoutePlan().getMeetingRoom());
                                agentMovement.getRoutePlan().setAtDesk(false);
                            }
                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                    }
                                }
                                else {
                                    if (agentMovement.getCurrentPath().getPath().size() <= 2) {
                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
                                            agentMovement.reachPatchInPath();
                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.WAIT_MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            agentMovement.setDuration(agentMovement.getDuration() - 1);
                            if (agentMovement.getDuration() <= 0) {
                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                        }
                        else if (action.getName() == Action.Name.MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            if (agentMovement.getRoutePlan().getMeetingEnd() <= currentTick) {
                                agentMovement.setNextState(agentMovement.getStateIndex());
                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.getGoalAttractor().setIsReserved(false);
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setCanUrgent(-1);
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }
                    break;

                case FACULTY: case STUDENT:
                    if (state.getName() == State.Name.GOING_TO_RECEPTION) {
                        if (action.getName() == Action.Name.GOING_TO_RECEPTION_QUEUE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalQueueingPatchField() == null) {
                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getReceptionTables().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getReceptionTables().get(0));
                            }

                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.joinQueue();
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.FILL_UP_NAME) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                }
                            }
                            else {
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.leaveQueue();
                                    agentMovement.setNextState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setCanUrgent(-1);
                                }
                            }
                        }
                    }
//                    else if (state.getName() == State.Name.WORKING) {
//                        if (action.getName() == Action.Name.GO_TO_STATION) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if(agentMovement.hasAgentReachedFinalPatchInPath()){
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.getRoutePlan().setAtDesk(true);
//                                    }
//                                }
//                            }
//                            else if ((currentTick < 2060 || (currentTick < 5660 && currentTick > 2520)) && agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < RoutePlan.BATH_CHANCE) {
//                                    if (currentTick < 2160 && agentMovement.getRoutePlan().getBATH_AM() > 0) {
//                                        agentMovement.getRoutePlan().setFromBathAM(true);
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else if (agentMovement.getRoutePlan().getBATH_PM() > 0) {
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                        agentMovement.getRoutePlan().setFromBathPM(true);
//                                    }
//                                }
//                                else {
//
//
//                                    if(CHANCE < RoutePlan.BATH_CHANCE + RoutePlan.BREAK_CHANCE && agentMovement.getRoutePlan().getBREAK_COUNT() > 0){
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BREAK", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//
//                                    if ((currentTick < 1660 || (currentTick < 5260 && currentTick > 2520)) &&
//                                            agentMovement.getRoutePlan().getCOLLABORATE_COUNT() > 0 &&
//                                            agentMovement.getRoutePlan().getCanUrgent() <= 0 &&
//                                            CHANCE < agentMovement.getRoutePlan().getCooperate(persona) + RoutePlan.BATH_CHANCE) {
//                                        ArrayList<Agent> agents = environmentInstance.getTeamMembers(agent.getTeam());
//                                        for(Agent agent1 : agents) {
//                                            if (agent1.getAgentMovement().getCurrentAction() != null && agent1.getAgentMovement().getCurrentAction().getName() == Action.Name.GO_TO_STATION) {
//                                                agent1.getAgentMovement().setStateIndex(agent1.getAgentMovement().getStateIndex() - 1);
//                                                agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(agent1.getAgentMovement().getStateIndex() + 1, agent1.getAgentMovement().getRoutePlan().addUrgentRoute("COLLABORATION", agent));
//                                                agent1.getAgentMovement().setNextState(agent1.getAgentMovement().getStateIndex());
//                                                agent1.getAgentMovement().setStateIndex(agent1.getAgentMovement().getStateIndex() + 1);
//                                                agent1.getAgentMovement().setActionIndex(0);
//                                                agent1.getAgentMovement().setCurrentAction(agent1.getAgentMovement().getCurrentState().getActions().get(agent1.getAgentMovement().getActionIndex()));
//                                                agent1.getAgentMovement().resetGoal();
//                                            }
//                                            else {
//                                                agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(agent1.getAgentMovement().getStateIndex() + 1, agent1.getAgentMovement().getRoutePlan().addUrgentRoute("COLLABORATION", agent));
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_COLLAB) {
//                        if (action.getName() == Action.Name.GO_TO_COLLAB) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (agentMovement.chooseCollaborationChair()) {
//                                    agentMovement.getRoutePlan().resetCanUrgent();
//                                    agentMovement.getRoutePlan().setCOLLABORATE_COUNT(-1);
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                }
//                                else {
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//
//                            if (isFull) {
//                                isFull = false;
//                            }
//                            else {
//                                if (agentMovement.chooseNextPatchInPath()) {
//                                    agentMovement.faceNextPosition();
//                                    agentMovement.moveSocialForce();
//                                    if (agentMovement.hasReachedNextPatchInPath()) {
//                                        agentMovement.reachPatchInPath();
//                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                        }
//                                    }
//                                    else {
//                                        if (agentMovement.getCurrentPath().getPath().size() <= 2) {
//                                            while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
//                                                agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
//                                                agentMovement.reachPatchInPath();
//                                                if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                                    agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                                else {
//                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                    if (agentMovement.getDuration() <= 0) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.getRoutePlan().setCollaborationEnd(currentTick, agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.COLLABORATE) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            if (agentMovement.getRoutePlan().getCollaborationEnd() <= currentTick) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                                agentMovement.removeCollaborationTeam();
//                            }
//                        }
//                    }
                    else if (state.getName() == State.Name.MEETING) {
                        if (action.getName() == Action.Name.GO_MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.chooseMeetingGoal(agentMovement.getRoutePlan().getMeetingRoom());
                                agentMovement.getRoutePlan().setAtDesk(false);
                            }
                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                    }
                                }
                                else {
                                    if (agentMovement.getCurrentPath().getPath().size() <= 2) {
                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
                                            agentMovement.reachPatchInPath();
                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.WAIT_MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            agentMovement.setDuration(agentMovement.getDuration() - 1);
                            if (agentMovement.getDuration() <= 0) {
                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                        }
                        else if (action.getName() == Action.Name.MEETING) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            if (agentMovement.getRoutePlan().getMeetingEnd() <= currentTick) {
                                agentMovement.setNextState(agentMovement.getStateIndex());
                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.getGoalAttractor().setIsReserved(false);
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setCanUrgent(-1);
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }
                    break;

            }
        }

        if (agentMovement.isInteracting()) {
            agentMovement.interact();
        }
        else {
            List<Patch> patches = agentMovement.get7x7Field(agentMovement.getHeading(), true, agentMovement.getFieldOfViewAngle());
            Agent agent2 = null;
            for (Patch patch: patches) {
                for (Agent otherAgent: patch.getAgents()) {
                    agent = otherAgent;
                    if (!agent.getAgentMovement().isInteracting() && !agentMovement.isInteracting())
                        if (Coordinates.isWithinFieldOfView(agentMovement.getPosition(), agent.getAgentMovement().getPosition(), agentMovement.getProposedHeading(), agentMovement.getFieldOfViewAngle()))
                            if (Coordinates.isWithinFieldOfView(agent.getAgentMovement().getPosition(), agentMovement.getPosition(), agent.getAgentMovement().getProposedHeading(), agent.getAgentMovement().getFieldOfViewAngle())){
                                agentMovement.rollAgentInteraction(agent);
                                if (agentMovement.isInteracting()) {
                                    agent2 = agent;
                                    currentPatchCount[agentMovement.getCurrentPatch().getMatrixPosition().getRow()][agentMovement.getCurrentPatch().getMatrixPosition().getColumn()]++;
                                    currentPatchCount[agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getRow()][agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getColumn()]++;
                                }
                            }
                    if (agentMovement.isInteracting())
                        break;
                }
                if (agentMovement.isInteracting())
                    break;
            }
//            patches = agentMovement.get3x3Field(agentMovement.getHeading(), true, Math.toRadians(270));
//            for (Patch patch: patches) {
//                for (Agent otherAgent: patch.getAgents()) {
//                    Agent agent = (Agent) otherAgent;
//                    if (!agent.getAgentMovement().isInteracting() && !agentMovement.isInteracting())
//                        if (Coordinates.isWithinFieldOfView(agentMovement.getPosition(), agent.getAgentMovement().getPosition(), agentMovement.getProposedHeading(), Math.toRadians(270)))
//                            if (Coordinates.isWithinFieldOfView(agent.getAgentMovement().getPosition(), agentMovement.getPosition(), agent.getAgentMovement().getProposedHeading(), Math.toRadians(270))){
//                                agentMovement.rollAgentInteraction(agent);
//                                if (agentMovement.isInteracting()) {
//                                    agent2 = agent;
//                                    currentPatchCount[agentMovement.getCurrentPatch().getMatrixPosition().getRow()][agentMovement.getCurrentPatch().getMatrixPosition().getColumn()]++;
//                                    currentPatchCount[agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getRow()][agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getColumn()]++;
//                                }
//                            }
//                    if (agentMovement.isInteracting())
//                        break;
//                }
//
//                if (agentMovement.isInteracting())
//                    break;
//            }
            if (agentMovement.isInteracting() && agentMovement.getInteractionDuration() == 0) {
                agentMovement.setInteracting(false);
                agentMovement.setInteractionType(null);
            }
            if (agent2 != null && agent2.getAgentMovement().isInteracting() && agent2.getAgentMovement().getInteractionDuration() == 0){
                agent2.getAgentMovement().setInteracting(false);
                agent2.getAgentMovement().setInteractionType(null);
            }
        }
        agent.getAgentGraphic().change();
    }

    public void replenishStaticVars() {
        // SEATING ARRANGEMENT
        FACULTY_1 = new LinkedList<Integer>(List.of(0, 1, 2, 3, 4));
        FACULTY_2 = new LinkedList<Integer>(List.of(36, 37, 38, 39, 40, 41, 42));
        FACULTY_3 = new LinkedList<Integer>(List.of(44, 45, 46, 47, 48, 49, 50));
        FACULTY_4 = new LinkedList<Integer>(List.of(52, 53, 54, 55, 56, 57, 58));
        STUDENT_1 = new LinkedList<Integer>(List.of(5, 6, 7, 8));
        STUDENT_2 = new LinkedList<Integer>(List.of(12, 13, 14, 15));
        STUDENT_3 = new LinkedList<Integer>(List.of(20, 21, 22, 23, 24));
        STUDENT_4 = new LinkedList<Integer>(List.of(28, 29, 30, 31, 32));




        // Current Agent Count Per Type
        currentDirectorCount = 0;
        currentFacultyCount = 0;
        currentStudentCount = 0;
        currentMaintenanceCount = 0;
        currentGuardCount = 0;


        // Current Interaction Count
        currentNonverbalCount = 0;
        currentCooperativeCount = 0;
        currentExchangeCount = 0;

        // Current Power Consumption Interaction Count
        currentAirconInteractionCount = 0;
        currentLightInteractionCount = 0;
        currentFridgeInteractionCount = 0;
        currentWaterDispenserInteractionCount = 0;

        // Average Interaction Duration
        averageNonverbalDuration = 0;
        averageCooperativeDuration = 0;
        averageExchangeDuration = 0;


        // Current Team Count
        currentTeam1Count = 0;
        currentTeam2Count = 0;
        currentTeam3Count = 0;
        currentTeam4Count = 0;


        // Current Director to ____ Interaction Count
        currentDirectorFacultyCount = 0;
        currentDirectorStudentCount = 0;
        currentDirectorMaintenanceCount = 0;
        currentDirectorGuardCount = 0;


        // Current Faculty to ____ Interaction Count
        currentFacultyFacultyCount = 0;
        currentFacultyStudentCount = 0;
        currentFacultyMaintenanceCount = 0;
        currentFacultyGuardCount = 0;


        // Current Student to ____ Interaction Count
        currentStudentStudentCount = 0;
        currentStudentMaintenanceCount = 0;
        currentStudentGuardCount = 0;


        // Current Maintenance to ____ Interaction Count
        currentMaintenanceMaintenanceCount = 0;
        currentMaintenanceGuardCount = 0;


        // Current Guard to Guard Interaction Count
        currentGuardGuardCount = 0;

        /** COMPILED **/

        // Current Agent Count Per Type
        compiledCurrentDirectorCount = new int[6481];
        compiledCurrentFacultyCount = new int[6481];
        compiledCurrentStudentCount = new int[6481];
        compiledCurrentMaintenanceCount = new int[6481];
        compiledCurrentGuardCount = new int[6481];

        // Current Interaction Count
        compiledCurrentNonverbalCount = new int[6481];
        compiledCurrentCooperativeCount = new int[6481];
        compiledCurrentExchangeCount = new int[6481];

        // Current Power Consumption Interaction Count
        compiledCurrentAirconInteractionCount = new int[6481];
        compiledCurrentLightInteractionCount = new int[6481];
        compiledCurrentFridgeInteractionCount = new int[6481];
        compiledCurrentWaterDispenserInteractionCount = new int[6481];


        // Average Interaction Duration
        compiledAverageNonverbalDuration = new float[6481];
        compiledAverageCooperativeDuration = new float[6481];
        compiledAverageExchangeDuration = new float[6481];


        // Current Team Count
        compiledCurrentTeam1Count = new int[6481];
        compiledCurrentTeam2Count = new int[6481];
        compiledCurrentTeam3Count = new int[6481];
        compiledCurrentTeam4Count = new int[6481];


        // Current Director to ____ Interaction Count
        compiledCurrentDirectorFacultyCount = new int[6481];
        compiledCurrentDirectorStudentCount = new int[6481];
        compiledCurrentDirectorMaintenanceCount = new int[6481];
        compiledCurrentDirectorGuardCount = new int[6481];


        // Current Faculty to ____ Interaction Count
        compiledCurrentFacultyFacultyCount = new int[6481];
        compiledCurrentFacultyStudentCount = new int[6481];
        compiledCurrentFacultyMaintenanceCount = new int[6481];
        compiledCurrentFacultyGuardCount = new int[6481];


        // Current Student to ____ Interaction Count
        compiledCurrentStudentStudentCount = new int[6481];
        compiledCurrentStudentMaintenanceCount = new int[6481];
        compiledCurrentStudentGuardCount = new int[6481];


        // Current Maintenance to ____ Interaction Count
        compiledCurrentMaintenanceMaintenanceCount = new int[6481];
        compiledCurrentMaintenanceGuardCount = new int[6481];


        // Current Guard to Guard Interaction Count
        compiledCurrentGuardGuardCount = new int[6481];


        /** Patch Count **/
        currentPatchCount = new int[environment.getRows()][environment.getColumns()];;


    }

    public static void exportToCSV() throws Exception{
        PrintWriter writer = new PrintWriter("Office Simulation Statistics.csv");
        StringBuilder sb = new StringBuilder();
        sb.append("Current Director Count");
        sb.append(",");
        sb.append("Current Faculty Count");
        sb.append(",");
        sb.append("Current Student Count");
        sb.append(",");
        sb.append("Current Maintenance Count");
        sb.append(",");
        sb.append("Current Guard Count");
        sb.append(",");
        sb.append("Current Nonverbal Count");
        sb.append(",");
        sb.append("Current Cooperative Count");
        sb.append(",");
        sb.append("Current Exchange Count");
        sb.append(",");
        sb.append("Current Aircon Interaction Count");
        sb.append(",");
        sb.append("Current Light Interaction Count");
        sb.append(",");
        sb.append("Current Fridge Interaction Count");
        sb.append(",");
        sb.append("Current Water Dispenser Interaction Count");
        sb.append(",");
        sb.append("Average Nonverbal Duration");
        sb.append(",");
        sb.append("Average Cooperative Duration");
        sb.append(",");
        sb.append("Average Exchange Duration");
        sb.append(",");
        sb.append("Current Team 1 Count");
        sb.append(",");
        sb.append("Current Team 2 Count");
        sb.append(",");
        sb.append("Current Team 3 Count");
        sb.append(",");
        sb.append("Current Team 4 Count");
        sb.append(",");
        sb.append("Current Director Faculty Count");
        sb.append(",");
        sb.append("Current Director Student Count");
        sb.append(",");
        sb.append("Current Director Maintenance Count");
        sb.append(",");
        sb.append("Current Director Guard Count");
        sb.append(",");
        sb.append("Current Faculty Faculty Count");
        sb.append(",");
        sb.append("Current Faculty Student Count");
        sb.append(",");
        sb.append("Current Faculty Maintenance Count");
        sb.append(",");
        sb.append("Current Faculty Guard Count");
        sb.append(",");
        sb.append("Current Student Student Count");
        sb.append(",");
        sb.append("Current Student Maintenance Count");
        sb.append(",");
        sb.append("Current Student Guard Count");
        sb.append(",");
        sb.append("Current Maintenance Maintenance Count");
        sb.append(",");
        sb.append("Current Maintenance Guard Count");
        sb.append(",");
        sb.append("Current Guard Guard Count");;
        sb.append("\n");
        for (int i = 0; i < 6481; i++){
            sb.append(compiledCurrentDirectorCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFacultyCount[i]);
            sb.append(",");
            sb.append(compiledCurrentStudentCount[i]);
            sb.append(",");
            sb.append(compiledCurrentMaintenanceCount[i]);
            sb.append(",");
            sb.append(compiledCurrentGuardCount[i]);
            sb.append(",");
            sb.append(compiledCurrentNonverbalCount[i]);
            sb.append(",");
            sb.append(compiledCurrentCooperativeCount[i]);
            sb.append(",");
            sb.append(compiledCurrentExchangeCount[i]);
            sb.append(",");
            sb.append(compiledCurrentAirconInteractionCount[i]);
            sb.append(",");
            sb.append(compiledCurrentLightInteractionCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFridgeInteractionCount[i]);
            sb.append(",");
            sb.append(compiledCurrentWaterDispenserInteractionCount[i]);
            sb.append(",");
            sb.append(compiledAverageNonverbalDuration[i]);
            sb.append(",");
            sb.append(compiledAverageCooperativeDuration[i]);
            sb.append(",");
            sb.append(compiledAverageExchangeDuration[i]);
            sb.append(",");
            sb.append(compiledCurrentTeam1Count[i]);
            sb.append(",");
            sb.append(compiledCurrentTeam2Count[i]);
            sb.append(",");
            sb.append(compiledCurrentTeam3Count[i]);
            sb.append(",");
            sb.append(compiledCurrentTeam4Count[i]);
            sb.append(",");
            sb.append(compiledCurrentDirectorFacultyCount[i]);
            sb.append(",");
            sb.append(compiledCurrentDirectorStudentCount[i]);
            sb.append(",");
            sb.append(compiledCurrentDirectorMaintenanceCount[i]);
            sb.append(",");
            sb.append(compiledCurrentDirectorGuardCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFacultyFacultyCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFacultyStudentCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFacultyMaintenanceCount[i]);
            sb.append(",");
            sb.append(compiledCurrentFacultyGuardCount[i]);
            sb.append(",");
            sb.append(compiledCurrentStudentStudentCount[i]);
            sb.append(",");
            sb.append(compiledCurrentStudentMaintenanceCount[i]);
            sb.append(",");
            sb.append(compiledCurrentStudentGuardCount[i]);
            sb.append(",");
            sb.append(compiledCurrentMaintenanceMaintenanceCount[i]);
            sb.append(",");
            sb.append(compiledCurrentMaintenanceGuardCount[i]);
            sb.append(",");
            sb.append(compiledCurrentGuardGuardCount[i]);
            sb.append("\n");
        }
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }

    public static void exportHeatMap() throws Exception {
        PrintWriter writer = new PrintWriter("Office Simulation Heat Map.csv");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentPatchCount.length; i++){
            for (int j = 0 ; j < currentPatchCount[i].length; j++){
                sb.append(currentPatchCount[i][j]);
                if (j != currentPatchCount[i].length - 1)
                    sb.append(",");
            }
            sb.append("\n");
        }
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }



    /***** GETTERS ******/
    public Environment getEnvironment() {
        return environment;
    }

    public AtomicBoolean getRunning() {
        return this.running;
    }

    public SimulationTime getSimulationTime() {
        return time;
    }

    public boolean isRunning() {
        return running.get();
    }
    public Semaphore getPlaySemaphore() {
        return playSemaphore;
    }






    /***** SETTERS ******/
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }


}