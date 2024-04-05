package com.socialsim.model.simulator;

import java.io.PrintWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.socialsim.controller.Main;
import com.socialsim.controller.controls.ScreenController;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.patchobject.passable.gate.Gate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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


    // SEATING ARRANGEMENT
    public static List<Integer> FACULTY_1 = new LinkedList<Integer>(List.of(0, 1, 2, 3, 4));
    public static List<Integer> FACULTY_2 = new LinkedList<Integer>(List.of(36, 37, 38, 39, 40, 41, 42));
    public static List<Integer> FACULTY_3 = new LinkedList<Integer>(List.of(44, 45, 46, 47, 48, 49, 50));
    public static List<Integer> FACULTY_4 = new LinkedList<Integer>(List.of(52, 53, 54, 55, 56, 57, 58));
    public static List<Integer> STUDENT_1 = new LinkedList<Integer>(List.of(5, 6, 7, 8));
    public static List<Integer> STUDENT_2 = new LinkedList<Integer>(List.of(12, 13, 14, 15));
    public static List<Integer> STUDENT_3 = new LinkedList<Integer>(List.of(20, 21, 22, 23, 24));
    public static List<Integer> STUDENT_4 = new LinkedList<Integer>(List.of(28, 29, 30, 31, 32));




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
        this.time = new SimulationTime(9, 0, 0);
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
                            updateAgentsInEnvironment(environment, currentTick);
                            spawnAgent(environment, currentTick);
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

    private void spawnAgent(Environment environment, long currentTick) {
//        Gate gate = environment.getGates().get(1);
//        Agent agent = null;
//
//        for (int i = 0; i < 2; i++) {
//            Gate.GateBlock spawner = gate.getSpawners().get(i);
//            int spawnChance = (int) gate.getChancePerTick();
//            int CHANCE = Simulator.RANDOM_NUMBER_GENERATOR.nextInt(100);
//
//            if (CHANCE > spawnChance) {
//                if (environment.getUnspawnedWorkingAgents().size() > 0){
//                    agent = environment.getUnspawnedWorkingAgents().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getUnspawnedWorkingAgents().size()));
//                    int team = agent.getTeam();
//                    if (agent.getType() == Agent.Type.BOSS && Agent.bossCount != MAX_BOSSES) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, 0, null));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        Agent.bossCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.MANAGER && team == 1 && MANAGERS_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(MANAGERS_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        MANAGERS_1.remove(0);
//                        currentManagerCount++;
//                        currentTeam1Count++;
//                        Agent.managerCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.BUSINESS && team == 1 && BUSINESS_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(BUSINESS_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        BUSINESS_1.remove(0);
//                        currentBusinessCount++;
//                        currentTeam1Count++;
//                        Agent.businessCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.RESEARCHER && team == 1 && RESEARCH_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(RESEARCH_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        RESEARCH_1.remove(0);
//                        currentResearchCount++;
//                        currentTeam1Count++;
//                        Agent.researcherCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.TECHNICAL && team == 1 && TECHNICAL_1.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(TECHNICAL_1.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        TECHNICAL_1.remove(0);
//                        currentTechnicalCount++;
//                        currentTeam1Count++;
//                        Agent.technicalCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.MANAGER && team == 2 && MANAGERS_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(MANAGERS_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        MANAGERS_2.remove(0);
//                        currentManagerCount++;
//                        currentTeam2Count++;
//                        Agent.managerCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.BUSINESS && team == 2 && BUSINESS_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(BUSINESS_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        BUSINESS_2.remove(0);
//                        currentBusinessCount++;
//                        currentTeam2Count++;
//                        Agent.businessCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.RESEARCHER && team == 2 && RESEARCH_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(RESEARCH_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        RESEARCH_2.remove(0);
//                        currentResearchCount++;
//                        currentTeam2Count++;
//                        Agent.researcherCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.TECHNICAL && team == 2 && TECHNICAL_2.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(TECHNICAL_2.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        TECHNICAL_2.remove(0);
//                        currentTechnicalCount++;
//                        currentTeam2Count++;
//                        Agent.technicalCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.MANAGER && team == 3 && MANAGERS_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(MANAGERS_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        MANAGERS_3.remove(0);
//                        currentManagerCount++;
//                        currentTeam3Count++;
//                        Agent.managerCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.BUSINESS && team == 3 && BUSINESS_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(BUSINESS_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        BUSINESS_3.remove(0);
//                        currentBusinessCount++;
//                        currentTeam3Count++;
//                        Agent.businessCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.RESEARCHER && team == 3 && RESEARCH_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(RESEARCH_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        RESEARCH_3.remove(0);
//                        currentResearchCount++;
//                        currentTeam3Count++;
//                        Agent.researcherCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.TECHNICAL && team == 3 && TECHNICAL_3.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(TECHNICAL_3.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        TECHNICAL_3.remove(0);
//                        currentTechnicalCount++;
//                        currentTeam3Count++;
//                        Agent.technicalCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.MANAGER && team == 4 && MANAGERS_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(MANAGERS_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        MANAGERS_4.remove(0);
//                        currentManagerCount++;
//                        currentTeam4Count++;
//                        Agent.managerCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.BUSINESS && team == 4 && BUSINESS_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(BUSINESS_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        BUSINESS_4.remove(0);
//                        currentBusinessCount++;
//                        currentTeam4Count++;
//                        Agent.businessCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.RESEARCHER && team == 4 && RESEARCH_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(RESEARCH_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        RESEARCH_4.remove(0);
//                        currentResearchCount++;
//                        currentTeam4Count++;
//                        Agent.researcherCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.TECHNICAL && team == 4 && TECHNICAL_4.size() != 0) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, team, environment.getCubicles().get(TECHNICAL_4.get(0))));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        TECHNICAL_4.remove(0);
//                        currentTechnicalCount++;
//                        currentTeam4Count++;
//                        Agent.technicalCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.SECRETARY && Agent.secretaryCount != MAX_SECRETARIES) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, 0, null));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        currentSecretaryCount++;
//                        Agent.secretaryCount++;
//                        Agent.agentCount++;
//                    }
//                }
//            }
//            else {
//                if (environment.getUnspawnedVisitingAgents().size() > 0){
//                    agent = environment.getUnspawnedVisitingAgents().get(Simulator.RANDOM_NUMBER_GENERATOR.nextInt(environment.getUnspawnedVisitingAgents().size()));
//                    if (agent.getType() == Agent.Type.CLIENT && ((currentTick >= 720 && currentTick < 1800) ||  (currentTick >= 2880 && currentTick < 4320)) && Agent.clientCount < environment.getMAX_CLIENTS()) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, 0, null));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        currentClientCount++;
//                        Agent.clientCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.DRIVER && Agent.driverCount < getEnvironment().getMAX_DRIVERS()) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, 0, null));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        currentDriverCount++;
//                        Agent.driverCount++;
//                        Agent.agentCount++;
//                    }
//                    else if (agent.getType() == Agent.Type.VISITOR && currentTick >= 3600 && currentTick < 5040 && Agent.visitorCount < getEnvironment().getMAX_VISITORS()) {
//                        agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), currentTick, 0, null));
//                        environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
//                        currentVisitorCount++;
//                        Agent.visitorCount++;
//                        Agent.agentCount++;
//                    }
//                }
//            }
//        }
    }

    public void spawnInitialAgents(Environment environment) {
//        environment.createInitialAgentDemographics(environment.getMAX_CLIENTS(), getEnvironment().getMAX_DRIVERS(), getEnvironment().getMAX_VISITORS());
//        EnvironmentAgent janitor = environment.getAgents().get(0);
//        janitor.setAgentMovement(new AgentMovement(environment.getPatch(6,23), janitor, 1.27, environment.getPatch(6,23).getPatchCenterCoordinates(), -1, 0, null));
//        environment.getAgentPatchSet().add(janitor.getAgentMovement().getCurrentPatch());
//        EnvironmentAgent.janitorCount++;
//        Agent.agentCount++;
//
//        Agent guard = environment.getAgents().get(1);
//        guard.setAgentMovement(new AgentMovement(environment.getPatch(57,25), guard, 1.27, environment.getPatch(57,25).getPatchCenterCoordinates(), -1, 0, null));
//        environment.getAgentPatchSet().add(guard.getAgentMovement().getCurrentPatch());
//        Agent.guardCount++;
//        Agent.agentCount++;
//
//        Agent receptionist = environment.getAgents().get(2);
//        receptionist.setAgentMovement(new AgentMovement(environment.getPatch(46,31), receptionist, 1.27, environment.getPatch(46,32).getPatchCenterCoordinates(), -1, 0, null));
//        environment.getAgentPatchSet().add(receptionist.getAgentMovement().getCurrentPatch());
//        Agent.receptionistCount++;
//        Agent.agentCount++;
    }

    public static void updateAgentsInEnvironment(Environment environment, long currentTick) throws InterruptedException {
        moveAll(environment, currentTick);

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

    private static void moveAll(Environment environment, long currentTick) {
//        int bathroomReserves = environment.numBathroomsFree();
//        for (Agent agent : environment.getMovableAgents()) {
//            try {
//                if (currentTick == 2160 && agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE() != null) {
//                    agent.getAgentMovement().setNextState(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().indexOf(agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE()) - 1);
//                    agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().indexOf(agent.getAgentMovement().getRoutePlan().getLUNCH_INSTANCE()));
//                    agent.getAgentMovement().setActionIndex(0);
//                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(0));
//                    agent.getAgentMovement().resetGoal();
//                }
//
//                if(currentTick == 4320){
//                    agent.getAgentMovement().getRoutePlan().setBATH_LUNCH(-1);
//                    agent.getAgentMovement().getRoutePlan().setBREAK_COUNT(-1);
//                }
//
//                if (currentTick == 5760) {
//                    agent.getAgentMovement().getRoutePlan().setBATH_PM(-1);
//                    agent.getAgentMovement().setNextState(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().size()-2);
//                    agent.getAgentMovement().setStateIndex(agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().size()-1);
//                    agent.getAgentMovement().setActionIndex(0);
//                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(0));
//                    agent.getAgentMovement().resetGoal();
//                }
//
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
//
//                moveOne(agent, currentTick);
//            } catch (Throwable ex) {
//                ex.printStackTrace();
//            }
//        }
    }

    private static void moveOne(Agent agent, long currentTick) throws Throwable {
//        AgentMovement agentMovement = agent.getAgentMovement();
//
//        Agent.Type type = agent.getType();
//        Agent.Persona persona = agent.getPersona();
//        State state = agentMovement.getCurrentState();
//        Action action = agentMovement.getCurrentAction();
//        Environment environmentInstance = agentMovement.getEnvironment();
//
//        boolean isFull = false;
//
//        if (!agentMovement.isInteracting() || agentMovement.isSimultaneousInteractionAllowed()) {
//            switch (type) {
//                case JANITOR:
//                    if (state.getName() == State.Name.MAINTENANCE_BATHROOM) {
//                        if (action.getName() == Action.Name.JANITOR_CLEAN_TOILET) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                agentMovement.checkIfStuck();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.MAINTENANCE_PLANT) {
//                        if (action.getName() == Action.Name.JANITOR_WATER_PLANT) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                agentMovement.checkIfStuck();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.setPreviousState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//
//                    break;
//
//                case BOSS:
//                    if (state.getName() == State.Name.GOING_TO_SECURITY) {
//                        if (action.getName() == Action.Name.GOING_TO_SECURITY_QUEUE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalQueueingPatchField() == null) {
//                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getSecurities().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getSecurities().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.joinQueue();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_THROUGH_SCANNER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.leaveQueue();
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setCanUrgent(-1);
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.WORKING) {
//                        if (action.getName() == Action.Name.GO_TO_OFFICE_ROOM) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination()
//                                        .getAmenityBlock().getParent());
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.getRoutePlan().setAtDesk(true);
//                                    }
//                                }
//                            }
//                            else if (currentTick >= 5760) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                            }
//                            else if ((currentTick < 2060 || (currentTick < 5660 && currentTick > 2520)) && agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE) {
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
//                                    else if(agentMovement.getRoutePlan().getBATH_PM() > 0) {
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
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.EATING_LUNCH) {
//                        if (action.getName() == Action.Name.GO_TO_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (agentMovement.getRoutePlan().getLunchAmenity() == null) {
//                                    double CHANCE = Simulator.roll();
//
//                                    if (persona == Agent.Persona.PROFESSIONAL_BOSS) {
//                                        if (CHANCE < OfficeRoutePlan.INT_LUNCH) {
//                                            if (!agentMovement.chooseBreakroomSeat()) {
//                                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                            }
//                                        }
//                                        else {
//                                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                        }
//                                    }
//                                    else {
//                                        if (CHANCE < OfficeRoutePlan.APP_BOSS_LUNCH) {
//                                            if (!agentMovement.chooseBreakroomSeat()) {
//                                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                            }
//                                        }
//                                        else {
//                                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                        }
//                                    }
//                                }
//                                else {
//                                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
//                                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
//                                }
//
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.getRoutePlan().setAtDesk(false);
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                        else {
//                                            agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                            agentMovement.getRoutePlan().setLastDuration(-1);
//                                        }
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.EAT_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setLunchAmenity(null);
//                                agentMovement.getRoutePlan().setLunchAttractor(null);
//                            }
//                            else if (agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE + OfficeRoutePlan.REFRIGERATOR_CHANCE && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.DISPENSER){
//                        if(action.getName() == Action.Name.GOING_DISPENSER){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(WaterDispenser.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setDISPENSER_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setDISPENSER_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_WATER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.REFRIGERATOR){
//                        if(action.getName() == Action.Name.GOING_FRIDGE){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(Fridge.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_FOOD) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_BATHROOM) {
//                        if (action.getName()== Action.Name.GO_TO_BATHROOM) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseBathroomGoal(Toilet.class)) {
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() - 1, agentMovement.getRoutePlan().addWaitingRoute(agent));
//                                    agentMovement.setPreviousState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() -1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    if(agentMovement.getGoalAttractor() != null) {
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                    }
//                                    agentMovement.resetGoal();
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    if (agentMovement.getRoutePlan().isFromBathAM()) {
//                                        agentMovement.getRoutePlan().setFromBathAM(false);
//                                    }
//                                    else if (agentMovement.getRoutePlan().isFromBathPM()) {
//                                        agentMovement.getRoutePlan().setFromBathPM(false);
//                                    }
//                                }
//                                else {
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if (agentMovement.getRoutePlan().isFromBathAM()) {
//                                        agentMovement.getRoutePlan().setFromBathAM(false);
//                                        agentMovement.getRoutePlan().setBATH_AM(1);
//                                    }
//                                    else if (agentMovement.getRoutePlan().isFromBathPM()) {
//                                        agentMovement.getRoutePlan().setFromBathPM(false);
//                                        agentMovement.getRoutePlan().setBATH_PM(1);
//                                    }
//                                    else {
//                                        agentMovement.getRoutePlan().setBATH_LUNCH(1);
//                                    }
//                                    agentMovement.getRoutePlan().resetCanUrgent();
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName()==Action.Name.RELIEVE_IN_CUBICLE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                        else if (action.getName() == Action.Name.FIND_SINK) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseBathroomGoal(Sink.class)) {
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.WASH_IN_SINK) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.MEETING) {
//                        if (action.getName() == Action.Name.GO_MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.chooseMeetingGoal(agentMovement.getRoutePlan().getMeetingRoom());
//                                agentMovement.getRoutePlan().setAtDesk(false);
//                            }
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                    }
//                                }
//                                else {
//                                    if (agentMovement.getCurrentPath().getPath().size() <= 2) {
//                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
//                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
//                                            agentMovement.reachPatchInPath();
//                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.WAIT_MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                            }
//                        }
//                        else if (action.getName() == Action.Name.MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            if (agentMovement.getRoutePlan().getMeetingEnd() <= currentTick) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setCanUrgent(-1);
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.GOING_HOME) {
//                        if (action.getName() == Action.Name.LEAVE_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getGates().get(0));
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                agentMovement.getRoutePlan().setAtDesk(false);
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.despawn();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.WAIT_INFRONT_OF_BATHROOM){
//                        if (action.getName() == Action.Name.GO_TO_WAIT_AREA) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if(!agentMovement.chooseWaitPatch()){
//                                    System.out.println("False wait patch");
//                                }
//                            }
//                            else {
//                                if (agentMovement.chooseNextPatchInPath()) {
//                                    agentMovement.faceNextPosition();
//                                    agentMovement.moveSocialForce();
//                                    if (agentMovement.hasReachedNextPatchInPath()) {
//                                        agentMovement.reachPatchInPath();
//                                    }
//                                }
//                                else{
//                                    agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                }
//                            }
//                        }
//                        else if(action.getName() == Action.Name.WAIT_FOR_VACANT){
//                            agentMovement.setSimultaneousInteractionAllowed(true);
////                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            if (agentMovement.getCurrentAction().getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                if (agentMovement.getGoalAttractor() != null) {
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                }
//                                agentMovement.resetGoal();
//                            }
//                            else {
//                                agentMovement.getCurrentAction().setDuration(agentMovement.getCurrentAction().getDuration() - 1);
//                            }
//                        }
//                    }
//                    break;
//
//                case MANAGER: case BUSINESS: case RESEARCHER: case TECHNICAL:
//
//                    if (state.getName() == State.Name.GOING_TO_SECURITY) {
//                        if (action.getName() == Action.Name.GOING_TO_SECURITY_QUEUE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalQueueingPatchField() == null) {
//                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getSecurities().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getSecurities().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.joinQueue();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_THROUGH_SCANNER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.leaveQueue();
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setCanUrgent(-1);
//                                }
//                            }
//                        }
//                    }
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
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE) {
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
//                                    int left = 0;
//                                    if (persona == Agent.Persona.EXT_BUSINESS || persona == Agent.Persona.INT_BUSINESS) {
//                                        left = agentMovement.getRoutePlan().getPRINT_BUSINESS();
//                                    }
//                                    else if (persona == Agent.Persona.EXT_RESEARCHER || persona == Agent.Persona.INT_RESEARCHER) {
//                                        left = agentMovement.getRoutePlan().getPRINT_RESEARCH();
//                                    }
//
//                                    if (CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.PRINT_CHANCE && left > 0) {
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("PRINT", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.PRINT_CHANCE + OfficeRoutePlan.BREAK_CHANCE && agentMovement.getRoutePlan().getBREAK_COUNT() > 0){
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BREAK", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//
//                                    if (CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.PRINT_CHANCE + OfficeRoutePlan.TECHNICAL_CUBICLE_CHANCE && agentMovement.getRoutePlan().getTECHNICAL_CUBICLE_COUNT() > 0) {
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute(agent, environmentInstance));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else if (CHANCE < OfficeRoutePlan.TECHNICAL_PRINTER_CHANCE + OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.PRINT_CHANCE + OfficeRoutePlan.TECHNICAL_CUBICLE_CHANCE && agentMovement.getRoutePlan().getTECHNICAL_PRINTER_COUNT() > 0) {
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("TECHNICAL_PRINTER", agent));
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else if ((currentTick < 1660 || (currentTick < 5260 && currentTick > 2520)) &&
//                                            agentMovement.getRoutePlan().getCOLLABORATE_COUNT() > 0 &&
//                                            agentMovement.getRoutePlan().getCanUrgent() <= 0 &&
//                                            CHANCE < agentMovement.getRoutePlan().getCooperate(persona) +
//                                                    OfficeRoutePlan.TECHNICAL_PRINTER_CHANCE + OfficeRoutePlan.BATH_CHANCE +
//                                                    OfficeRoutePlan.PRINT_CHANCE + OfficeRoutePlan.TECHNICAL_CUBICLE_CHANCE) {
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
//                    else if (state.getName() == State.Name.MEETING) {
//                        if (action.getName() == Action.Name.GO_MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.chooseMeetingGoal(agentMovement.getRoutePlan().getMeetingRoom());
//                                agentMovement.getRoutePlan().setAtDesk(false);
//                            }
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                    }
//                                }
//                                else {
//                                    if (agentMovement.getCurrentPath().getPath().size() <= 2) {
//                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
//                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
//                                            agentMovement.reachPatchInPath();
//                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.WAIT_MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                            }
//                        }
//                        else if (action.getName() == Action.Name.MEETING) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            if (agentMovement.getRoutePlan().getMeetingEnd() <= currentTick) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setCanUrgent(-1);
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_PRINT) {
//                        if (action.getName() == Action.Name.GO_TO_PRINTER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseGoal(Printer.class)) {
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                                else {
//                                    agentMovement.getRoutePlan().resetCanUrgent();
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                }
//                            }
//                            if (isFull) {
//                                isFull = false;
//                            }
//                            else {
//                                //System.out.println(agent.getId() + " Patch: " + agentMovement.getCurrentPatch());
//                                if (agentMovement.chooseNextPatchInPath()) {
//                                    agentMovement.faceNextPosition();
//                                    agentMovement.moveSocialForce();
//                                    if (agentMovement.hasReachedNextPatchInPath()) {
//                                        agentMovement.reachPatchInPath();
//                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                            if (persona == Agent.Persona.EXT_BUSINESS || persona == Agent.Persona.INT_BUSINESS) {
//                                                agentMovement.getRoutePlan().setPRINT_BUSINESS();
//                                            } else if (persona == Agent.Persona.EXT_RESEARCHER || persona == Agent.Persona.INT_RESEARCHER) {
//                                                agentMovement.getRoutePlan().setPRINT_RESEARCH();
//                                            }
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.PRINTING) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.EATING_LUNCH) {
//                        if (action.getName() == Action.Name.GO_TO_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (agentMovement.getRoutePlan().getLunchAmenity() == null) {
//                                    double CHANCE = Simulator.roll();
//
//                                    if (persona == Agent.Persona.EXT_TECHNICAL || persona == Agent.Persona.EXT_BUSINESS || persona == Agent.Persona.EXT_RESEARCHER || persona == Agent.Persona.MANAGER) {
//                                        if (CHANCE < OfficeRoutePlan.EXT_LUNCH) {
//                                            if (!agentMovement.chooseBreakroomSeat()) {
//                                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                                agentMovement.getRoutePlan().setAtDesk(true);
//                                            }else{
//                                                agentMovement.getRoutePlan().setAtDesk(false);
//                                            }
//                                        }
//                                        else {
//                                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                            agentMovement.getRoutePlan().setAtDesk(true);
//                                        }
//                                    }
//                                    else {
//                                        if (CHANCE < OfficeRoutePlan.INT_LUNCH) {
//                                            if (!agentMovement.chooseBreakroomSeat()) {
//                                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                                agentMovement.getRoutePlan().setAtDesk(true);
//                                            }else{
//                                                agentMovement.getRoutePlan().setAtDesk(false);
//                                            }
//                                        }
//                                        else {
//                                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                            agentMovement.getRoutePlan().setAtDesk(true);
//                                        }
//                                    }
//                                }
//                                else {
//                                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
//                                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
//                                    agentMovement.getRoutePlan().setAtDesk(true);
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                        else {
//                                            agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                            agentMovement.getRoutePlan().setLastDuration(-1);
//                                        }
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                    }
//                                }
//                                else {
//                                    if (agentMovement.getCurrentPath().getPath().size() <= 3) {
//                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
//                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
//                                            agentMovement.reachPatchInPath();
//                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                                if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                                }
//                                                else {
//                                                    agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                                    agentMovement.getRoutePlan().setLastDuration(-1);
//                                                }
//                                                agentMovement.getRoutePlan().setCanUrgent(-1);
//                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                                agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                                agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.EAT_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setLunchAmenity(null);
//                                agentMovement.getRoutePlan().setLunchAttractor(null);
//                            }
//                            else if (agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE + OfficeRoutePlan.REFRIGERATOR_CHANCE && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.BREAK_TIME){
//                        if (action.getName() == Action.Name.GO_TO_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (agentMovement.getRoutePlan().getLunchAmenity() == null) {
//                                    if(agentMovement.chooseBreakroomSeat()){
//                                        agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getAgentCubicle().getAttractors().get(0).getPatch().getAmenityBlock().getParent());
//                                        agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                        agentMovement.getRoutePlan().setAtDesk(true);
//                                    }else{
//                                        agentMovement.getRoutePlan().setAtDesk(false);
//                                    }
//                                }
//                                else {
//                                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
//                                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
//                                    agentMovement.getRoutePlan().setAtDesk(true);
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                        else {
//                                            agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                            agentMovement.getRoutePlan().setLastDuration(-1);
//                                        }
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                    }
//                                }
//                                else {
//                                    if (agentMovement.getCurrentPath().getPath().size() <= 3) {
//                                        while (!agentMovement.getCurrentPath().getPath().isEmpty()) {
//                                            agentMovement.setPosition(agentMovement.getCurrentPath().getPath().peek().getPatchCenterCoordinates());
//                                            agentMovement.reachPatchInPath();
//                                            if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                                if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                                }
//                                                else {
//                                                    agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                                    agentMovement.getRoutePlan().setLastDuration(-1);
//                                                }
//                                                agentMovement.getRoutePlan().setCanUrgent(-1);
//                                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                                agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                                agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.TAKING_BREAK) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setLunchAmenity(null);
//                                agentMovement.getRoutePlan().setLunchAttractor(null);
//                            }
//                            else if (agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE + OfficeRoutePlan.REFRIGERATOR_CHANCE && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_BATHROOM) {
//                        if (action.getName() == Action.Name.GO_TO_BATHROOM) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseBathroomGoal(Toilet.class)) {
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() - 1, agentMovement.getRoutePlan().addWaitingRoute(agent));
//                                    agentMovement.setPreviousState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() -1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    if(agentMovement.getGoalAttractor() != null) {
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                    }
//                                    agentMovement.resetGoal();
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    if (agentMovement.getRoutePlan().isFromBathAM()) {
//                                        agentMovement.getRoutePlan().setFromBathAM(false);
//                                    }
//                                    else if (agentMovement.getRoutePlan().isFromBathPM()) {
//                                        agentMovement.getRoutePlan().setFromBathPM(false);
//                                    }
//                                }
//                                else {
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if (agentMovement.getRoutePlan().isFromBathAM()) {
//                                        agentMovement.getRoutePlan().setFromBathAM(false);
//                                        agentMovement.getRoutePlan().setBATH_AM(1);
//                                    }
//                                    else if (agentMovement.getRoutePlan().isFromBathPM()) {
//                                        agentMovement.getRoutePlan().setFromBathPM(false);
//                                        agentMovement.getRoutePlan().setBATH_PM(1);
//                                    }
//                                    else {
//                                        agentMovement.getRoutePlan().setBATH_LUNCH(1);
//                                    }
//                                    agentMovement.getRoutePlan().resetCanUrgent();
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.RELIEVE_IN_CUBICLE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                try{
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                }catch (NullPointerException e){
//                                    System.out.print("");
//                                }
//                                agentMovement.resetGoal();
//                            }
//                        }
//                        else if (action.getName() == Action.Name.FIND_SINK) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseBathroomGoal(Sink.class)) {
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.WASH_IN_SINK) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_FIX_PRINTER) {
//                        if (action.getName() == Action.Name.TECHNICAL_GO_PRINTER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (!agentMovement.chooseGoal(Printer.class)) {
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                                else {
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    agentMovement.getRoutePlan().resetCanUrgent();
//                                    agentMovement.getRoutePlan().setTECHNICAL_PRINTER_COUNT(1);
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions()
//                                                    .get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.FIX_PRINTER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.NEEDS_FIX_CUBICLE) {
//                        if (action.getName() == Action.Name.FIX_CUBICLE){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                agentMovement.getGoalAttractor().setIsReserved(true);
//                                agentMovement.getRoutePlan().resetCanUrgent();
//                                agentMovement.getRoutePlan().setTECHNICAL_CUBICLE_COUNT(1);
//                                agentMovement.getRoutePlan().setAtDesk(false);
//                            }
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                    }
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.DISPENSER){
//                        if(action.getName() == Action.Name.GOING_DISPENSER){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(WaterDispenser.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setDISPENSER_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setDISPENSER_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_WATER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.REFRIGERATOR){
//                        if(action.getName() == Action.Name.GOING_FRIDGE){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(Fridge.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_FOOD) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.GOING_HOME) {
//                        if (action.getName() == Action.Name.LEAVE_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if(agentMovement.getRoutePlan().getBATH_PM() > 0){
//                                    double CHANCE = Simulator.roll();
//                                    if(CHANCE < OfficeRoutePlan.BATH_CHANCE){
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
//                                if(!agentMovement.getRoutePlan().isAtDesk()){
//                                    agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getGates().get(0));
//                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                }
//                            }else if (agentMovement.getGoalAmenity() == Main.simulator.getEnvironment().getGates().get(0)
//                                    && agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.despawn();
//                                    }
//                                }
//                            }
//                        }
//                        else if(action.getName() == Action.Name.GO_TO_STATION){
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
//                                        agentMovement.getRoutePlan().setAtDesk(true);
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                    }
//                                }
//                            }else{
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.setActionIndex(agentMovement.getActionIndex() - 1);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.WAIT_INFRONT_OF_BATHROOM){
//                        if (action.getName() == Action.Name.GO_TO_WAIT_AREA) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if(!agentMovement.chooseWaitPatch()){
//                                    System.out.println("False wait patch");
//                                }
//                            }
//                            else {
//                                if (agentMovement.chooseNextPatchInPath()) {
//                                    agentMovement.faceNextPosition();
//                                    agentMovement.moveSocialForce();
//                                    if (agentMovement.hasReachedNextPatchInPath()) {
//                                        agentMovement.reachPatchInPath();
//                                    }
//                                }
//                                else{
//                                    agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                }
//                            }
//                        }
//                        else if(action.getName() == Action.Name.WAIT_FOR_VACANT){
//                            agentMovement.setSimultaneousInteractionAllowed(true);
////                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            if (agentMovement.getCurrentAction().getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                if (agentMovement.getGoalAttractor() != null) {
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                }
//                                agentMovement.resetGoal();
//                            }
//                            else {
//                                agentMovement.getCurrentAction().setDuration(agentMovement.getCurrentAction().getDuration() - 1);
//                            }
//                        }
//                    }
//                    break;
//
//                case SECRETARY:
//                    if (state.getName() == State.Name.GOING_TO_SECURITY) {
//                        if (action.getName() == Action.Name.GOING_TO_SECURITY_QUEUE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalQueueingPatchField() == null) {
//                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getSecurities().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getSecurities().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.joinQueue();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_THROUGH_SCANNER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.leaveQueue();
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.SECRETARY) {
//                        if (action.getName() == Action.Name.GO_TO_OFFICE_ROOM) {
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
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.SECRETARY_STAY_PUT || action.getName() == Action.Name.SECRETARY_CHECK_CABINET) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (action.getName() == Action.Name.SECRETARY_STAY_PUT) {
//                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                }
//                                else {
//                                    agentMovement.chooseGoal(Cabinet.class);
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    if (action.getName() == Action.Name.SECRETARY_STAY_PUT) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                    }
//                                    else {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() - 1);
//                                    }
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.EATING_LUNCH) {
//                        if (action.getName() == Action.Name.GO_TO_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if(agentMovement.chooseBreakroomSeat()){
//                                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
//                                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
//                                }else{
//                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        if (agentMovement.getRoutePlan().getLastDuration() == -1) {
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                        else {
//                                            agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
//                                            agentMovement.getRoutePlan().setLastDuration(-1);
//                                        }
//                                        agentMovement.getRoutePlan().setCanUrgent(-1);
//                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
//                                        agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.EAT_LUNCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(true);
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.resetGoal();
//                                agentMovement.getRoutePlan().setLunchAmenity(null);
//                                agentMovement.getRoutePlan().setLunchAttractor(null);
//                            }
//                            else if (agentMovement.getRoutePlan().getCanUrgent() <= 0) {
//                                double CHANCE = Simulator.roll();
//
//                                if (CHANCE < OfficeRoutePlan.BATH_CHANCE && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                                else if(CHANCE < OfficeRoutePlan.BATH_CHANCE + OfficeRoutePlan.DISPENSER_CHANCE + OfficeRoutePlan.REFRIGERATOR_CHANCE && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0){
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() - 1);
//                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex() + 1, agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent));
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
//                                }
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.DISPENSER){
//                        if(action.getName() == Action.Name.GOING_DISPENSER){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(WaterDispenser.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setDISPENSER_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setDISPENSER_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_WATER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if(state.getName() == State.Name.REFRIGERATOR){
//                        if(action.getName() == Action.Name.GOING_FRIDGE){
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if(agentMovement.getGoalAmenity() == null){
//                                if(!agentMovement.chooseGoal(Fridge.class)){
//                                    isFull = true;
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }else{
//                                    agentMovement.getRoutePlan().setAtDesk(false);
//                                    if(currentTick < 4320){
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_LUNCH(0);
//                                    }else{
//                                        agentMovement.getRoutePlan().setREFRIGERATOR_PM(0);
//                                    }
//                                }
//                            }
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
//                                            agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GETTING_FOOD) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                            agentMovement.setDuration(agentMovement.getDuration() - 1);
//                            if (agentMovement.getDuration() <= 0) {
//                                agentMovement.setNextState(agentMovement.getStateIndex());
//                                agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                agentMovement.setActionIndex(0);
//                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                agentMovement.getGoalAttractor().setIsReserved(false);
//                                agentMovement.resetGoal();
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.GOING_HOME) {
//                        if (action.getName() == Action.Name.LEAVE_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getGates().get(0));
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.despawn();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    break;
//
//                case CLIENT: case DRIVER:
//                    if (state.getName() == State.Name.GOING_TO_SECURITY) {
//                        if (action.getName() == Action.Name.GOING_TO_SECURITY_QUEUE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalQueueingPatchField() == null) {
//                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getSecurities().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getSecurities().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.joinQueue();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_THROUGH_SCANNER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.leaveQueue();
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.DRIVER) {
//                        if (action.getName() == Action.Name.DRIVER_GO_RECEPTIONIST || action.getName() == Action.Name.DRIVER_GO_COUCH) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (action.getName() == Action.Name.DRIVER_GO_RECEPTIONIST) {
//                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                }
//                                else {
//                                    agentMovement.chooseGoal(Couch.class);
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    if (agentMovement.getActionIndex() >= 2) {
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                        agentMovement.resetGoal();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.CLIENT) {
//                        if (action.getName() == Action.Name.CLIENT_GO_RECEPTIONIST || action.getName() == Action.Name.CLIENT_GO_COUCH || action.getName() == Action.Name.CLIENT_GO_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                if (action.getName() == Action.Name.CLIENT_GO_RECEPTIONIST || action.getName() == Action.Name.CLIENT_GO_OFFICE) {
//                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
//                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                                }
//                                else {
//                                    agentMovement.chooseGoal(Couch.class);
//                                }
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    if (agentMovement.getActionIndex() >= agentMovement.getCurrentState().getActions().size() - 1) {
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                    else {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                        agentMovement.getGoalAttractor().setIsReserved(false);
//                                        agentMovement.resetGoal();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_TO_OFFICE_ROOM) {
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
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.resetGoal();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.GOING_HOME) {
//                        if (action.getName() == Action.Name.LEAVE_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getGates().get(0));
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.despawn();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    break;
//
//                case VISITOR:
//                    if (state.getName() == State.Name.GOING_TO_SECURITY) {
//                        if (action.getName() == Action.Name.GOING_TO_SECURITY_QUEUE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalQueueingPatchField() == null) {
//                                agentMovement.setGoalQueueingPatchField(Main.simulator.getEnvironment().getSecurities().get(0).getAmenityBlocks().get(1).getPatch().getQueueingPatchField().getKey());
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getSecurities().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.joinQueue();
//                                    }
//                                }
//                            }
//                        }
//                        else if (action.getName() == Action.Name.GO_THROUGH_SCANNER) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    agentMovement.leaveQueue();
//                                    agentMovement.setNextState(agentMovement.getStateIndex());
//                                    agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                    agentMovement.setActionIndex(0);
//                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.VISITOR) {
//                        if (action.getName() == Action.Name.VISITOR_GO_RECEPTIONIST || action.getName() == Action.Name.VISITOR_GO_OFFICE) {
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
//                                }
//                            }
//                            else {
//                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
//                                agentMovement.setDuration(agentMovement.getDuration() - 1);
//                                if (agentMovement.getDuration() <= 0) {
//                                    if (agentMovement.getActionIndex() >= agentMovement.getCurrentState().getActions().size() - 1) {
//                                        agentMovement.setNextState(agentMovement.getStateIndex());
//                                        agentMovement.setStateIndex(agentMovement.getStateIndex() + 1);
//                                        agentMovement.setActionIndex(0);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                    }
//                                    else {
//                                        agentMovement.setActionIndex(agentMovement.getActionIndex() + 1);
//                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
//                                    }
//                                    agentMovement.getGoalAttractor().setIsReserved(false);
//                                    agentMovement.resetGoal();
//                                }
//                            }
//                        }
//                    }
//                    else if (state.getName() == State.Name.GOING_HOME) {
//                        if (action.getName() == Action.Name.LEAVE_OFFICE) {
//                            agentMovement.setSimultaneousInteractionAllowed(false);
//                            if (agentMovement.getGoalAmenity() == null) {
//                                agentMovement.setGoalAmenity(Main.simulator.getEnvironment().getGates().get(0));
//                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().get(0));
//                            }
//
//                            if (agentMovement.chooseNextPatchInPath()) {
//                                agentMovement.faceNextPosition();
//                                agentMovement.moveSocialForce();
//                                if (agentMovement.hasReachedNextPatchInPath()) {
//                                    agentMovement.reachPatchInPath();
//                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
//                                        agentMovement.despawn();
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    break;
//            }
//        }
//
//        if (agentMovement.isInteracting()) {
//            agentMovement.interact();
//        }
//        else {
//            List<Patch> patches = agentMovement.get7x7Field(agentMovement.getHeading(), true, agentMovement.getFieldOfViewAngle());
//            Agent agent2 = null;
//            for (Patch patch: patches) {
//                for (Agent otherAgent: patch.getAgents()) {
//                    Agent agent = (Agent) otherAgent;
//                    if (!agent.getAgentMovement().isInteracting() && !agentMovement.isInteracting())
//                        if (Coordinates.isWithinFieldOfView(agentMovement.getPosition(), agent.getAgentMovement().getPosition(), agentMovement.getProposedHeading(), agentMovement.getFieldOfViewAngle()))
//                            if (Coordinates.isWithinFieldOfView(agent.getAgentMovement().getPosition(), agentMovement.getPosition(), agent.getAgentMovement().getProposedHeading(), agent.getAgentMovement().getFieldOfViewAngle())){
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
//                if (agentMovement.isInteracting())
//                    break;
//            }
////            patches = agentMovement.get3x3Field(agentMovement.getHeading(), true, Math.toRadians(270));
////            for (Patch patch: patches) {
////                for (Agent otherAgent: patch.getAgents()) {
////                    Agent agent = (Agent) otherAgent;
////                    if (!agent.getAgentMovement().isInteracting() && !agentMovement.isInteracting())
////                        if (Coordinates.isWithinFieldOfView(agentMovement.getPosition(), agent.getAgentMovement().getPosition(), agentMovement.getProposedHeading(), Math.toRadians(270)))
////                            if (Coordinates.isWithinFieldOfView(agent.getAgentMovement().getPosition(), agentMovement.getPosition(), agent.getAgentMovement().getProposedHeading(), Math.toRadians(270))){
////                                agentMovement.rollAgentInteraction(agent);
////                                if (agentMovement.isInteracting()) {
////                                    agent2 = agent;
////                                    currentPatchCount[agentMovement.getCurrentPatch().getMatrixPosition().getRow()][agentMovement.getCurrentPatch().getMatrixPosition().getColumn()]++;
////                                    currentPatchCount[agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getRow()][agent.getAgentMovement().getCurrentPatch().getMatrixPosition().getColumn()]++;
////                                }
////                            }
////                    if (agentMovement.isInteracting())
////                        break;
////                }
////
////                if (agentMovement.isInteracting())
////                    break;
////            }
//            if (agentMovement.isInteracting() && agentMovement.getInteractionDuration() == 0) {
//                agentMovement.setInteracting(false);
//                agentMovement.setInteractionType(null);
//            }
//            if (agent2 != null && agent2.getAgentMovement().isInteracting() && agent2.getAgentMovement().getInteractionDuration() == 0){
//                agent2.getAgentMovement().setInteracting(false);
//                agent2.getAgentMovement().setInteractionType(null);
//            }
//        }
//        agent.getAgentGraphic().change();
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