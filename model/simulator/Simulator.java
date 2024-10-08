package com.socialsim.model.simulator;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.socialsim.controller.Main;
import com.socialsim.controller.controls.ScreenController;
import com.socialsim.model.core.agent.*;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.environment.Patch;
import com.socialsim.model.core.environment.patchfield.LearningSpace;
import com.socialsim.model.core.environment.patchfield.PatchField;
import com.socialsim.model.core.environment.patchobject.Amenity;
import com.socialsim.model.core.environment.patchobject.passable.elevator.Elevator;
import com.socialsim.model.core.environment.patchobject.passable.goal.*;
import com.socialsim.model.core.environment.position.Coordinates;
import javafx.util.Pair;

import static java.lang.Math.max;

public class Simulator {


    /***** VARIABLES ******/
    private Environment environment;
    private final AtomicBoolean running;
    private final SimulationTime time;
    private final Semaphore playSemaphore;


    // Maximum Time of the simulation
    // Developer Note: The model won't stop on this tick.
    private final static int MAX_CURRENT_TICKS = 17280;

    public static final Random RANDOM_NUMBER_GENERATOR;
    // Change
    static {
        //12345L; 67890L; //11121L; //22232L; //33343L;
        RANDOM_NUMBER_GENERATOR = new Random(33343L);
    }
    public static double roll(){
        return RANDOM_NUMBER_GENERATOR.nextDouble();
    }

    public static int rollInt(){
        return RANDOM_NUMBER_GENERATOR.nextInt(100);
    }

    public static int rollIntIN(int num) {return RANDOM_NUMBER_GENERATOR.nextInt(num);}


    private final int MAX_DIRECTORS = 1;
    private static int elevIndex = 0;
    private static int gateIndex = 0;

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
    //FOR AIRCONS, LIGHTS, AND MONITORS, COUNT THE NUMBER OF TURNED ON APPLIANCES
    public static int currentAirconCount = 0;
    public static int currentLightCount = 0;
    public static int currentMonitorCount = 0;
    public static int totalMonitorInteractionCount = 0;

    public static int currentCoffeeMakerCount = 0;

    public static int currentAirconTurnOnCount = 0;
    public static int currentAirconTurnOffCount = 0;

    public static int currentLightTurnOnCount = 0;
    public static int currentLightTurnOffCount = 0;

    public static int currentFridgeInteractionCount = 0;
    public static int currentWaterDispenserInteractionCount = 0;

    // Total Wattage Count
    public static float totalWattageCount = 0.0f;
    public static float miscWattageCount = 11.8f;

    public static int currentAirconChangeTempInteraction = 0;

    public static float currentPowerConsumption = 0.0f;

    //Aircon
    public static float airconWattage = 0.7F;
    public static float airconWattageActive = 40.5f;
    //Light
    public static float lightWattage = 0.7F;

    //Fridge
    public static float fridgeWattage = 0.1F;
    public static float fridgeWattageInUse = 0.1F;
    public static float fridgeWattageActive = 1.3F;
    //Water Dispenser
    public static float waterDispenserWattage = 0.1F;
    public static float waterDispenserWattageInUse = 0.1F;
    public static float waterDispenserWattageActive= 0.5F;
    public static float waterDispenserWattageActiveHigh =  4.3f;

    //Monitor
    public static float monitorWattage = 0.1F;

    //COFFEE
    public static float coffeeMakerWattageLow = 0.4f;
    public static float coffeeMakerWattageHigh = 12.1F;

    // Average Interaction Duration
    public static float averageNonverbalDuration = 0;
    public static float averageCooperativeDuration = 0;
    public static float averageExchangeDuration = 0;


    // Current Interaction Count
    public static int currentTotalSocialInteractionCountPer5Mins;


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

    // AGENT CHANCES
    public static double greenChance = 0.75;
    public static double nonGreenChance = 0.10;
    public static double neutralChance = 0.15;

    public static int studentNum = 44;
    public static int facultyNum = 3;
    public static int teamNum = 10;
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
    public static int[] compiledCurrentAirconCount;
    public static int[] compiledCurrentLightCount;
    public static int[] compiledCurrentMonitorCount;
    public static int[] compiledTotalMonitorInteractionCount;


    public static int[] compiledCurrentAirconTurnOnCount;
    public static int[] compiledCurrentAirconTurnOffCount;
    public static int[] compiledCurrentLightTurnOnCount;
    public static int[] compiledCurrentLightTurnOffCount;

    public static int[] compiledCurrentFridgeInteractionCount;
    public static int[] compiledCurrentWaterDispenserInteractionCount;

    // WATTAGE COUNT

    public static float[] compiledTotalWattageCount;
    public static float[] compiledCurrentPowerConsumption;
    public static int[] compiledCurrentAirconChangeTempInteraction;

    // Average Interaction Duration
    public static float[] compiledAverageNonverbalDuration;
    public static float[] compiledAverageCooperativeDuration;
    public static float[] compiledAverageExchangeDuration;


    // Current Team Count
    public static int[] compiledTotalSocialInteractionper5min;

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

    public static Set<Monitor> countedMonitors = new HashSet<>();







    /***** CONSTRUCTOR ******/
    public Simulator() {
        this.environment = null;
        this.running = new AtomicBoolean(false);
        this.time = new SimulationTime(7, 0, 0);
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
                            updateEnvironment(environment, currentTick, this.time);
                            updateAgentsInEnvironment(environment, currentTick, this.time);
                            spawnAgent(environment, this.time, currentTick);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        ((ScreenController) Main.mainScreenController).drawEnvironmentViewForeground(Main.simulator.getEnvironment(), SimulationTime.SLEEP_TIME_MILLISECONDS.get() < speedAwarenessLimitMilliseconds);

                        this.time.tick();
                        Thread.sleep(SimulationTime.SLEEP_TIME_MILLISECONDS.get());

                        if (this.time.getTime().equals(LocalTime.of(20,0))) {
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

    private void spawnAgent(Environment environment, SimulationTime time, long currentTick) {

        // Every 20 seconds change elev entrance
        if ((currentTick + 5L) % 4L == 0L) {
            if (elevIndex < environment.getElevators().size() - 1) {
                elevIndex++;
            }
            else {
                elevIndex = 0;
            }
        }

        Elevator gate = environment.getElevators().get(elevIndex);

        Elevator.ElevatorBlock spawner = gate.getSpawners().get(gateIndex);


        if (!environment.getUnspawnedWorkingAgents().isEmpty()){
            for (Agent agent : environment.getUnspawnedWorkingAgents()){
                int team = agent.getTeam();
                if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.GUARD) {
                    agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
                            spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(),
                            environment.getReceptionTables().getFirst().getReceptionChairs().get(1))); // assigned seat not dynamic
                    environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                    Agent.guardCount++;
                    Agent.agentCount++;
                    currentGuardCount++;
                    break;
                }
                else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.MAINTENANCE) {
                    agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
                            spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(), null));
                    environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                    Agent.maintenanceCount++;
                    Agent.agentCount++;
                    currentMaintenanceCount++;
                    break;
                }
                else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.DIRECTOR) {
                    agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27,
                            spawner.getPatch().getPatchCenterCoordinates(), agent.getTeam(),
                            environment.getDirectorTables().getFirst().getDirectorChairs().getFirst())); // assigned seat not dynamic
                    environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                    Agent.directorCount++;
                    Agent.agentCount++;
                    currentDirectorCount++;
                    break;
                }
                else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.FACULTY) {
                    agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), team, null));
                    environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                    currentFacultyCount++;
                    Agent.facultyCount++;
                    Agent.agentCount++;
                    break;
                }
                else if (time.getTime().isAfter(agent.getTimeIn()) && agent.getType() == Agent.Type.STUDENT) {
                    agent.setAgentMovement(new AgentMovement(spawner.getPatch(), agent, 1.27, spawner.getPatch().getPatchCenterCoordinates(), team, null));
                    environment.getAgentPatchSet().add(agent.getAgentMovement().getCurrentPatch());
                    currentStudentCount++;
                    Agent.studentCount++;
                    Agent.agentCount++;
                    break;
                }
            }
            if (gateIndex < gate.getSpawners().size() - 1) {
                gateIndex++;
            }
            else {
                gateIndex = 0;
            }
        }


    }

    public void spawnInitialAgents(Environment environment) {
        environment.createInitialAgentDemographics(greenChance, nonGreenChance, neutralChance, studentNum, facultyNum, teamNum);

    }

    public static void updateEnvironment (Environment environment, long currentTick, SimulationTime time) {
        // Change to night
        if (currentTick % 60 == 0) {
            currentTotalSocialInteractionCountPer5Mins = 0;
        }
        currentPowerConsumption = miscWattageCount;
        totalWattageCount += miscWattageCount * 5 / 3600;
        if (time.getTime().isAfter(LocalTime.of(16,30))) {
            for (WindowBlinds windowBlinds : environment.getWindowBlinds()) {
                if (windowBlinds.isOpened()) {
                    windowBlinds.open(false);
                    windowBlinds.getWindowBlindsGraphic().change();
                }
            }
        }
    }

    public static void updateAgentsInEnvironment(Environment environment, long currentTick, SimulationTime time) throws InterruptedException {
        moveAll(environment, currentTick, time);
        environment.tempChanger();
        runWattageCount(currentTick, environment);
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
        compiledCurrentAirconCount[(int) currentTick] = currentAirconCount;
        compiledCurrentLightCount[(int) currentTick] = currentLightCount;
        compiledCurrentMonitorCount[(int) currentTick] = currentMonitorCount;
        compiledTotalMonitorInteractionCount[(int) currentTick] = totalMonitorInteractionCount;

        compiledCurrentAirconTurnOnCount[(int) currentTick] = currentAirconTurnOnCount;
        compiledCurrentAirconTurnOffCount[(int) currentTick] = currentAirconTurnOffCount;

        compiledCurrentLightTurnOnCount[(int) currentTick] = currentLightTurnOnCount;
        compiledCurrentLightTurnOffCount[(int) currentTick] = currentLightTurnOffCount;

        compiledCurrentFridgeInteractionCount[(int) currentTick] = currentFridgeInteractionCount;
        compiledCurrentWaterDispenserInteractionCount[(int) currentTick] = currentWaterDispenserInteractionCount;

        compiledCurrentAirconChangeTempInteraction[(int) currentTick] = currentAirconChangeTempInteraction;

        //Wattage Count
        compiledCurrentPowerConsumption[(int) currentTick] = currentPowerConsumption;
        compiledTotalWattageCount[(int) currentTick] = totalWattageCount;

        // Average Interaction Duration
        compiledAverageNonverbalDuration[(int) currentTick] = averageNonverbalDuration;
        compiledAverageCooperativeDuration[(int) currentTick] = averageCooperativeDuration;
        compiledAverageExchangeDuration[(int) currentTick] = averageExchangeDuration;


        // Current Total Social Interaction per 5 min
        compiledTotalSocialInteractionper5min [(int) currentTick] = currentTotalSocialInteractionCountPer5Mins;


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
        for (Agent agent : environment.getMovableAgents()) {
            try {

                if(time.getTime().isBefore(LocalTime.NOON)) {
                    agent.getAgentMovement().getRoutePlan().setBathAM(true);
                    agent.getAgentMovement().getRoutePlan().setBathPM(false);
                }
                else {
                    agent.getAgentMovement().getRoutePlan().setBathAM(false);
                    agent.getAgentMovement().getRoutePlan().setBathPM(true);
                }


                // Before going Maintenance and Guard to go home in their timeOut schedule, they will inspect every room
                if ((agent.getType() == Agent.Type.MAINTENANCE || agent.getType() == Agent.Type.GUARD) &&
                        time.getTime().equals(agent.getTimeOut().minusMinutes(60))) {
                    agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(0, agent.getAgentMovement().getRoutePlan().addUrgentRoute("INSPECT", agent, environment));
                    agent.getAgentMovement().setCurrentState(0);
                    agent.getAgentMovement().setStateIndex(0);
                    agent.getAgentMovement().setActionIndex(0);
                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                    agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration()); // setting the new duration of the action
                    agent.getAgentMovement().resetGoal();
                    agent.getAgentMovement().getRoutePlan().setAtDesk(false); // JIC needed
                }


                // Lunch Time for Anyone except for Maintenance
                int randomHour = RANDOM_NUMBER_GENERATOR.nextInt(11, 14);
                int randomMinute = RANDOM_NUMBER_GENERATOR.nextInt(0, 59);
                if (agent.getAgentMovement().getRoutePlan().isAtDesk() && time.getTime().equals(LocalTime.of(randomHour, randomMinute)) && agent.getType() != Agent.Type.MAINTENANCE) {
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        agent.getAgentMovement().getRoutePlan().setTakingLunch(true);
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().resetGoal();
                    }
                }

                // Lunch Time for Maintenance
                randomMinute = RANDOM_NUMBER_GENERATOR.nextInt(0, 16);
                if (time.getTime().equals(LocalTime.of(11,randomMinute)) && agent.getType() == Agent.Type.MAINTENANCE) {
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        if (agent.getAgentMovement().getGoalAttractor() != null) {
                            agent.getAgentMovement().getGoalAttractor().setIsReserved(false);
                        }
                        agent.getAgentMovement().getRoutePlan().setTakingLunch(true);
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().resetGoal();
                    }
                }

                // Open Lights at 4pm
                if (agent.getType() == Agent.Type.MAINTENANCE &&
                        time.getTime().equals(LocalTime.of(16,0))) {
                    agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(0, agent.getAgentMovement().getRoutePlan().addUrgentRoute("OPEN_HALLWAY_LIGHTS", agent, environment));
                    agent.getAgentMovement().setCurrentState(0);
                    agent.getAgentMovement().setStateIndex(0);
                    agent.getAgentMovement().setActionIndex(0);
                    if (!agent.getAgentMovement().getCurrentState().getActions().isEmpty()) {
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration()); // setting the new duration of the action
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false); // JIC needed
                    }
                    else {
                        agent.getAgentMovement().setCurrentState(0);
                        agent.getAgentMovement().setStateIndex(0);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                    }
                }

                // Agents leave on their scheduled timeOut
                if (time.getTime().isAfter(agent.getTimeOut()) && !agent.getAgentMovement().getRoutePlan().isLeaving()) {
                    agent.getAgentMovement().getRoutePlan().setLeaving(true);
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                    if(index != -1) {
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                    }

                }
                else if (agent.getAgentMovement().getRoutePlan().isLeaving() && agent.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME) {
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                    if(index != -1) {
                        agent.getAgentMovement().getRoutePlan().setLeaving(true);
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                    }
                }
                else if (((Agent.Type.STUDENT == agent.getType() && time.getTime().isAfter(LocalTime.of(19,0))) ||
                        (Agent.Type.MAINTENANCE == agent.getType() && time.getTime().isAfter(LocalTime.of(20,0))) ||
                        (Agent.Type.FACULTY == agent.getType() && time.getTime().isAfter(LocalTime.of(22,0))) ||
                        (Agent.Type.GUARD == agent.getType() && time.getTime().isAfter(LocalTime.of(22,0)))) && !agent.getAgentMovement().getRoutePlan().isLeaving()) {
                    agent.getAgentMovement().getRoutePlan().setLeaving(true);
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                    if(index != -1) {
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                    }

                }

                moveOne(agent, currentTick, time);
            } catch (Throwable ex) {
                if (time.getTime().isAfter(agent.getTimeOut()) || (Agent.Type.STUDENT == agent.getType() && time.getTime().equals(LocalTime.of(19,0))) ||
                        (Agent.Type.MAINTENANCE == agent.getType() && time.getTime().equals(LocalTime.of(20,0))) ||
                        (Agent.Type.FACULTY == agent.getType() && time.getTime().equals(LocalTime.of(22,0))) ||
                        (Agent.Type.GUARD == agent.getType() && time.getTime().equals(LocalTime.of(22,0)))) {
                    agent.getAgentMovement().getRoutePlan().setLeaving(true);
                    int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                    if(index != -1) {
                        agent.getAgentMovement().setCurrentState(index);
                        agent.getAgentMovement().setStateIndex(index);
                        agent.getAgentMovement().setActionIndex(0);
                        agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                        agent.getAgentMovement().resetGoal();
                        agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                    }

                }
                else {
                    agent.getAgentMovement().getRoutePlan().getCurrentRoutePlan().remove(agent.getAgentMovement().getStateIndex()); // removing finished state
                    agent.getAgentMovement().setCurrentState(0);
                    agent.getAgentMovement().setStateIndex(0);
                    agent.getAgentMovement().setActionIndex(0);
                    agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                    agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                    agent.getAgentMovement().resetGoal();
                    agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                }
            }
        }
    }

    private static void doCommonAction(AgentMovement agentMovement, State state, Action action, Agent agent, Agent.Type type,
                                       Agent.Persona persona, Environment environmentInstance, long currentTick, SimulationTime time) {
        boolean isFull = false;
        // is this where isAtDesk is set to true
        // Mainly used by Director, Faculty, and Student agents
        // where urgent task happen for Director, Faculty, and Student agents
        if (action.getName() == Action.Name.GO_TO_STATION) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if((type == Agent.Type.STUDENT || type == Agent.Type.FACULTY) && agentMovement.getAssignedSeat() == null) {
                    if(!agentMovement.chooseWorkingSeat()) {
                        isFull = true;
                    }

                }
                else if (agentMovement.getAssignedSeat() != null) {
                    agentMovement.setGoalAmenity(agentMovement.getAssignedSeat());
                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                }
                else if (agentMovement.getCurrentAction().getDestination() != null){
                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                }
                else {
                    isFull = true;
                }

            }

            if(isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();

                    if (agentMovement.hasReachedGoal()) {
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.setHeading(agentMovement.getWorkingSeatHeading());
                        agentMovement.reachGoal();
                    }
                    else if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.setHeading(agentMovement.getWorkingSeatHeading());
                            agentMovement.reachGoal();
                        }
                    }

                }
                else {
                    if ((agentMovement.getCurrentState().getName() == State.Name.GOING_TO_EAT_OUTSIDE ||
                            agentMovement.getCurrentState().getName() == State.Name.GOING_HOME)) {

                        agentMovement.getRoutePlan().setAtDesk(true); // signalling that the agent is in his/her desk

                        agentMovement.setDuration(agentMovement.getDuration() - 1);
                        if (agentMovement.getDuration() <= 0) {
                            if (agentMovement.getCurrentState().getName() != State.Name.GOING_HOME) // This is used in GOING_HOME state for it needs the GO_TO_STATION action when agents will turn off the lights and/or air cons
                                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action

                            agentMovement.setActionIndex(0); // JIC if needed to set the new action
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action

                            // turn off monitor (only if the current amenity is a cubicle with a monitor),
                            // if the student is going home, remove its reservation for it is not his or her assigned seat.
                            if (state.getName() == State.Name.GOING_HOME) {
                                agentMovement.reachGoal();
                                if (agentMovement.getCurrentAmenity() instanceof Monitor) {
                                    if (( (Monitor) agentMovement.getCurrentAmenity()).isOn()) {
                                        ((Monitor) agentMovement.getCurrentAmenity()).setOn(false);
                                    }
                                }
                                if (type == Agent.Type.STUDENT) {
                                    agentMovement.getGoalAttractor().setIsReserved(false);
                                }
                            }
                            // setting isAtDesk true is important for leaving the environment permanently/partially
                            if (state.getName() != State.Name.GOING_TO_EAT_OUTSIDE && state.getName() != State.Name.GOING_HOME) {
                                agentMovement.getRoutePlan().setAtDesk(false);
                            }

                            agentMovement.resetGoal();
                        }
                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed to set the new action
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                        }
                    }
                    else {
                        if (agentMovement.getCurrentAmenity() != null && agentMovement.getCurrentAmenity() instanceof Monitor) {
                            agentMovement.setUsingAppliance(true);
                            if (!( (Monitor) agentMovement.getCurrentAmenity()).isOn()) {
                                ((Monitor) agentMovement.getCurrentAmenity()).setOn(true);
                            }
                        }
                        agentMovement.getRoutePlan().setAtDesk(true); // signalling that the agent is in his/her desk
                        if (agentMovement.getRoutePlan().getCanUrgent()) {
                            double CHANCE = Simulator.roll();

                            if (CHANCE >= agentMovement.getRoutePlan().getWORKING_CHANCE()) {
                                if (agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentFacultyCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_FACULTY", agent, environmentInstance));
                                    agentMovement.setCurrentState(0);
                                    agentMovement.setStateIndex(0);
                                    agentMovement.setActionIndex(0);
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();

                                }
                                else if (agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentStudentCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_STUDENT", agent, environmentInstance));
                                    agentMovement.setCurrentState(0);
                                    agentMovement.setStateIndex(0);
                                    agentMovement.setActionIndex(0);
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else if (agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE()  && currentGuardCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                    agentMovement.setCurrentState(0);
                                    agentMovement.setStateIndex(0);
                                    agentMovement.setActionIndex(0);
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else if (agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentMaintenanceCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                    agentMovement.setCurrentState(0);
                                    agentMovement.setStateIndex(0);
                                    agentMovement.setActionIndex(0);
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else if (agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentDirectorCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                    agentMovement.setCurrentState(0);
                                    agentMovement.setStateIndex(0);
                                    agentMovement.setActionIndex(0);
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else if (agentMovement.getRoutePlan().getBATHROOM_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_BATHROOM_COOL_DOWN_DURATION)) {
                                    if (agentMovement.getRoutePlan().isBathAM() && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                                        agentMovement.setStateIndex(agentMovement.getStateIndex());
                                        agentMovement.setActionIndex(0);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.getRoutePlan().setBATH_AM(1);
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }
                                    else if (agentMovement.getRoutePlan().isBathPM() && agentMovement.getRoutePlan().getBATH_PM() > 0) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                                        agentMovement.setStateIndex(agentMovement.getStateIndex());
                                        agentMovement.setActionIndex(0);
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.getRoutePlan().setBATH_PM(1);
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }

                                }
                                else if (agentMovement.getRoutePlan().getCOFFEE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getCOFFEE_COUNT() > 0 && agentMovement.coolDown(AgentMovement.MAX_COFFEE_COOL_DOWN_DURATION)) {
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("COFFEE", agent, environmentInstance));
                                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setCOFFEE_COUNT(1);
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                }
                                else if (agentMovement.getRoutePlan().getDISPENSER_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getDISPENSER() > 0 && agentMovement.coolDown(AgentMovement.MAX_DISPENSER_COOL_DOWN_DURATION)) {
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent, environmentInstance));
                                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setDISPENSER(1);
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                }
                                else if (agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() + agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getREFRIGERATOR() > 0 && agentMovement.coolDown(AgentMovement.MAX_REFRIGERATOR_COOL_DOWN_DURATION)) {
                                    agentMovement.setUsingAppliance(false);
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent, environmentInstance));
                                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setREFRIGERATOR(1);
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                }
                            }


                            if (agentMovement.airconChecker() && agentMovement.thermalComfortCoolDown()) {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("FIX_THERMAL_COMFORT", agent, environmentInstance));
                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                }
                                else {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed to set the new action
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }

                            }
                            if (!agentMovement.visualComfortChecker(time) && agentMovement.visualComfortCoolDown()) {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("FIX_VISUAL_COMFORT", agent, environmentInstance));
                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false);
                            }
                        }
                    }
                }
            }


        }

        else if (action.getName() == Action.Name.LEAVE_OFFICE || action.getName() == Action.Name.EXIT_LUNCH) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {

                double chance = Simulator.roll();
                if (action.getName() == Action.Name.LEAVE_OFFICE &&
                        agentMovement.getRoutePlan().isAtDesk() && ((agent.getEnergyProfile() == Agent.EnergyProfile.GREEN && chance < 0.75) ||
                        (agent.getEnergyProfile() == Agent.EnergyProfile.NEUTRAL && chance < 0.50) || (agent.getEnergyProfile() == Agent.EnergyProfile.NONGREEN && chance < 0.25))) {
                    PatchField patchField = agentMovement.getCurrentPatch().getPatchField().getKey();
                    String keyField = agentMovement.getCurrentPatch().getPatchField().getValue();
                    if (agentMovement.getCurrentState().findIndexAction(Action.Name.TURN_OFF_LIGHT) != -1) {
                        if (agentMovement.closestLight()) {
                            int closeAgentCount = 0;
                            for(Agent agent1 : environmentInstance.getMovableAgents()){
                                for (Amenity.AmenityBlock attractor : agentMovement.getLightsToOpen().getAttractors()) {
                                    if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                        double distanceToLight = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                        if(distanceToLight < agentMovement.getLightsToOpen().getLightRange()){
                                            closeAgentCount++;
                                            break;
                                        }
                                    }
                                }
                            }


                            if (closeAgentCount != 0) {
                                // Use destination in route plan
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                            }
                            else {
                                int actionIndex = agentMovement.getCurrentState().findIndexAction(Action.Name.TURN_OFF_LIGHT);
                                if (actionIndex != -1) {
                                    agentMovement.setActionIndex(actionIndex);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else{
                                    // Use destination in route plan
                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                }
                            }
                        }
                        else {
                            // Use destination in route plan
                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                        }
                    }
                    else if (agentMovement.getCurrentState().findIndexAction(Action.Name.TURN_OFF_AC) != -1) {
                        if (agentMovement.closestAircon()) {
                            int closeAgentCount = 0;
                            for(Agent agent1 : environmentInstance.getMovableAgents()){
                                for (Amenity.AmenityBlock attractor : agentMovement.getAirconToChange().getAttractors()) {
                                    if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                        double distanceToAircon = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                        if(distanceToAircon < agentMovement.getAirconToChange().getCoolingRange()){
                                            closeAgentCount++;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (closeAgentCount != 0) {
                                // Use destination in route plan
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                            }
                            else {
                                int actionIndex = agentMovement.getCurrentState().findIndexAction(Action.Name.TURN_OFF_AC);
                                if (actionIndex != -1) {
                                    agentMovement.setActionIndex(actionIndex);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                }
                                else{
                                    // Use destination in route plan
                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                }
                            }
                        }
                        else {
                            // Use destination in route plan
                            agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                            agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                        }
                    }
                    else {
                        // Use destination in route plan
                        agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                        agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                    }
                }
                // check if the agent is in his/her work place before leaving
                else if(!agentMovement.getRoutePlan().isAtDesk() && action.getName() == Action.Name.LEAVE_OFFICE){
                    // SET ACTION TO GO_TO_STATION WHICH SHOULD BE SET ON THE ROUTE PLAN
                    int actionIndex = agentMovement.getCurrentState().findIndexAction(Action.Name.GO_TO_STATION);
                    if (actionIndex != -1) {
                        agentMovement.setActionIndex(actionIndex);
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                    }
                    else{
                        // Use destination in route plan
                        agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                        agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                    }
                }
                else{
                    // Use destination in route plan
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
                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // set the duration for the agent to be back in the office
                        if(action.getName() == Action.Name.EXIT_LUNCH){
                            agentMovement.getCurrentPatch().getAgents().remove(agent); // temporary remove the agent in the GUI
                            agentMovement.getRoutePlan().setAtDesk(false);
                        }
                        else if (action.getName() == Action.Name.LEAVE_OFFICE) {
                            agentMovement.despawn();
                        }

                    }
                }
            }
            else if (action.getName() == Action.Name.EXIT_LUNCH){ // if duration has been set

                agentMovement.setDuration(agentMovement.getDuration() - 1);
                if (agentMovement.getDuration() <= 0) {
                    if(action.getName() == Action.Name.EXIT_LUNCH){
                        agentMovement.getCurrentPatch().getAgents().add(agent); // add the agent in the GUI again
                    }
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed to set the new action
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.resetGoal();
                }
            }
        }

        // Used for waiting for group mates to eat outside (discontinued)
        else if (action.getName() == Action.Name.WAIT_FOR_COLLEAGUE) {
            agentMovement.setSimultaneousInteractionAllowed(true);
//            if (agent.getTeam() != 0 && type == Agent.Type.STUDENT) {
//                int countAgent = 0;
//                int totalAgent = 0;
//                for (Agent agent1 : environmentInstance.getPresentTeamMembers(agent.getTeam())) {
//                    if (type == agent1.getType()) {
//                        if (agent1.getAgentMovement().getCurrentAction() != null
//                                && agent1.getAgentMovement().getCurrentAction().getName() == Action.Name.WAIT_FOR_COLLEAGUE) {
//                            countAgent++;
//                        }
//                        totalAgent++;
//                    }
//                }
//                // System.out.println("Number of Agents ready: " + countAgent);
//                if (countAgent == environmentInstance.getPresentTeamMembers(agent.getTeam()).size()) {
//                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
//                    agentMovement.setActionIndex(0); // JIC needed
//                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
//                    agentMovement.resetGoal();
//                    for(Agent agent1 : environmentInstance.getPresentTeamMembers(agent.getTeam())) {
//                        if(agent1 != agent && agent1.getType() == type) {
////                            // System.out.println("CHANGING CURRENT ACTION");
//                            agent1.getAgentMovement().getCurrentState().getActions().remove(agent1.getAgentMovement().getActionIndex()); // removing finished action
//                            agent1.getAgentMovement().setActionIndex(0); // JIC needed
//                            agent1.getAgentMovement().setCurrentAction(agent1.getAgentMovement().getCurrentState().getActions().get(agentMovement.getActionIndex()));
//                            agent1.getAgentMovement().setDuration(agent1.getAgentMovement().getCurrentAction().getDuration()); // setting the new duration of the action
//                            agent1.getAgentMovement().resetGoal();
//                        }
//                    }
//                }
//            }
//            else {
            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
            agentMovement.setActionIndex(0); // JIC needed
            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
            }
            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
//            }
        }

        else if (action.getName() == Action.Name.GOING_TO_RECEPTION_QUEUE || action.getName() == Action.Name.GO_TO_WAIT_AREA) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalQueueingPatchField() == null || agentMovement.getGoalAmenity() == null) {
                if (state.getName() == State.Name.GOING_TO_RECEPTION) {
                    // Go to the end of the ReceptionQueue
                    agentMovement.chooseReceptionQueue();
                }
                else if (state.getName() == State.Name.NEEDS_BATHROOM) {
                    agentMovement.chooseBathroomQueue();
                }
                else if (state.getName() == State.Name.DISPENSER) {
                    agentMovement.chooseWaterDispenserQueue();
                }
                else if (state.getName() == State.Name.REFRIGERATOR) {
                    agentMovement.chooseFridgeQueue();
                } else if (state.getName() == State.Name.COFFEE) {
                    agentMovement.chooseCoffeeQueue();
                }
                else {
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.getRoutePlan().setAtDesk(false);
                    agentMovement.resetGoal();
                }
            }
            else if (agentMovement.chooseNextPatchInPath()) {
                // Make this agent face that patch
                agentMovement.faceNextPosition();

                // Move towards that patch
                agentMovement.moveSocialForce();

                if (agentMovement.isCloseOrOnQueue()) {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.joinQueue();
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                }
                else if (agentMovement.hasReachedNextPatchInPath()) {
                    // The agent has reached the next patch in the path, so remove this from
                    // this agent's current path
                    agentMovement.reachPatchInPath();

                    // Check if there are still patches left in the path
                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.joinQueue();
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    }
                }

            }

        }
        else if (action.getName() == Action.Name.WAIT_FOR_VACANT) {
            agentMovement.setSimultaneousInteractionAllowed(true);

            if (state.getName() == State.Name.NEEDS_BATHROOM) {
                agentMovement.setGoalAmenity(null); // needs reset because we used chooseBathroomQueue
                if (type == Agent.Type.DIRECTOR && agentMovement.chooseBathroomGoal(OfficeToilet.class)) {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.leaveQueue();
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                }
                else if (agentMovement.isFirstInLine() && agentMovement.chooseBathroomGoal(Toilet.class)) {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.leaveQueue();
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                }
            }
            else {
                if (agentMovement.getGoalQueueingPatchField() == null) {
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.getRoutePlan().setAtDesk(false);
                    agentMovement.resetGoal();
                }
                else if (agentMovement.isFirstInLine() && !agentMovement.getGoalAttractor().getIsReserved()) {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.getGoalAttractor().setIsReserved(true);
                        agentMovement.leaveQueue();
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                }
            }
        }
        else if (action.getName() == Action.Name.FILL_UP_NAME) {
            agentMovement.setSimultaneousInteractionAllowed(false);

            if(agentMovement.chooseNextPatchInPath()) {
                agentMovement.faceNextPosition();
                agentMovement.moveSocialForce();
                if (agentMovement.hasReachedNextPatchInPath()) {
                    agentMovement.reachPatchInPath();
                    if (agentMovement.isCloseToFinalPatchInPath()) {
                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    }
                }
            }
            else {
                agentMovement.setDuration(agentMovement.getDuration() - 1);

                if (agentMovement.getDuration() <= 0) {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    }

                    agentMovement.getGoalAttractor().setIsReserved(false); // done using the amenity
                    agentMovement.getRoutePlan().setAtDesk(false); // reset it back
                    agentMovement.resetGoal();
                }

                if (agentMovement.getCurrentState().getActions().isEmpty()){
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.getRoutePlan().setAtDesk(false);
                    agentMovement.resetGoal();
                }
            }

        }


        else if (action.getName()== Action.Name.GO_TO_BATHROOM) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if ((type == Agent.Type.DIRECTOR && !agentMovement.chooseBathroomGoal(OfficeToilet.class)) ||
                        (type != Agent.Type.DIRECTOR && !agentMovement.chooseBathroomGoal(Toilet.class))) {
                    // If full add waiting state or go back to eating
                    if (agentMovement.getRoutePlan().isTakingLunch()) {
                        int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                        if (index != -1) {
                            agentMovement.setCurrentState(index);
                            agentMovement.setStateIndex(index);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                            agentMovement.resetGoal();
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex());
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                            agentMovement.setCurrentState(0);
                            agentMovement.setStateIndex(0);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                        }
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex());
                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                        agentMovement.setCurrentState(0);
                        agentMovement.setStateIndex(0);
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                    }

                    isFull = true;
                    agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current location
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
                            agentMovement.reachGoal();
                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                // Expecting to go to RELIEVE_IN_CUBICLE
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/RELIEVE_IN_CUBICLE is not in the state action
                                if (agentMovement.getRoutePlan().isTakingLunch()) {
                                    int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                                    if (index != -1) {
                                        agentMovement.setCurrentState(index);
                                        agentMovement.setStateIndex(index);
                                        agentMovement.setActionIndex(0);
                                        agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                    else {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.FIND_SINK) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if ((type != Agent.Type.DIRECTOR && !agentMovement.chooseBathroomGoal(Sink.class))
                        || (type == Agent.Type.DIRECTOR && !agentMovement.chooseBathroomGoal(OfficeSink.class))) {
                    isFull = true;
                    if (agentMovement.getRoutePlan().isTakingLunch()) {
                        int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                        if (index != -1) {
                            agentMovement.setCurrentState(index);
                            agentMovement.setStateIndex(index);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                            agentMovement.resetGoal();
                        }
                    }
                    else {
                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }

                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
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
                            agentMovement.reachGoal();
                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                // Expecting that there are no remaining action/WASH_IN_SINK is not in the state action
                                if (agentMovement.getRoutePlan().isTakingLunch()) {
                                    int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                                    if (index != -1) {
                                        agentMovement.setCurrentState(index);
                                        agentMovement.setStateIndex(index);
                                        agentMovement.setActionIndex(0);
                                        agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                    else {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }
                            }
                        }
                    }
                }
            }
        }

        else if (action.getName()==Action.Name.RELIEVE_IN_CUBICLE) {
            agentMovement.setDuration(agentMovement.getDuration() - 1);

            agentMovement.setSimultaneousInteractionAllowed(true);
            if (agentMovement.getDuration() <= 0) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using amenity
                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                agentMovement.setActionIndex(0); // JIC needed
                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                }
                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                agentMovement.resetGoal();
            }
            if (agentMovement.getCurrentState().getActions().isEmpty()) {
                if (agentMovement.getRoutePlan().isTakingLunch()) {
                    int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        agentMovement.setCurrentState(index);
                        agentMovement.setStateIndex(index);
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agentMovement.resetGoal();
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                    }
                }
                else {
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.resetGoal();
                }
            }

        }

        else if (action.getName() == Action.Name.WASH_IN_SINK || action.getName() == Action.Name.GETTING_WATER || action.getName() == Action.Name.GETTING_FOOD || action.getName() == Action.Name.MAKE_COFFEE) {
            agentMovement.setDuration(agentMovement.getDuration() - 1);

            agentMovement.setSimultaneousInteractionAllowed(true);

            // This is to confirm if the agent is interacting with the electric appliance
            if (state.getName() == State.Name.DISPENSER && action.getName() == Action.Name.GETTING_WATER) {
                if (agentMovement.getCurrentAmenity() != null && agentMovement.getCurrentAmenity() instanceof WaterDispenser) {
                    ((WaterDispenser) agentMovement.getCurrentAmenity()).setWaterLevel(((WaterDispenser) agentMovement.getCurrentAmenity()).getWaterLevel() - 5);
                }
                agentMovement.setUsingAppliance(true);
                totalWattageCount+= ((waterDispenserWattageInUse * 5) / 3600);
                currentPowerConsumption += waterDispenserWattageInUse;
            }
            else if (state.getName() == State.Name.REFRIGERATOR && action.getName() == Action.Name.GETTING_FOOD) {
                if (agentMovement.getCurrentAmenity() != null && agentMovement.getCurrentAmenity() instanceof Refrigerator) {
                    ((Refrigerator) agentMovement.getCurrentAmenity()).setCoolnessLevel(((Refrigerator) agentMovement.getCurrentAmenity()).getCoolnessLevel() - 5);
                }
                agentMovement.setUsingAppliance(true);
                totalWattageCount += ((fridgeWattageInUse * 5) / 3600);
                currentPowerConsumption += fridgeWattageInUse;
            }
            else if (state.getName() == State.Name.COFFEE && action.getName() == Action.Name.MAKE_COFFEE) {
                if(coffeeMakerWattageLow < coffeeMakerWattageHigh) {
                    agentMovement.setUsingAppliance(true);
                    float watt = RANDOM_NUMBER_GENERATOR.nextFloat(coffeeMakerWattageLow, coffeeMakerWattageHigh);
                    totalWattageCount += ((watt * 5) / 3600);
                    currentPowerConsumption += watt;
                }
            }

            if (agentMovement.getDuration() <= 0) {
                agentMovement.getGoalAttractor().setIsReserved(false); // Done using amenity
                agentMovement.setUsingAppliance(false);
                if (agentMovement.getRoutePlan().isTakingLunch()) {
                    int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                    if (index != -1) {
                        agentMovement.setCurrentState(index);
                        agentMovement.setStateIndex(index);
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                        agentMovement.resetGoal();
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                    }
                }
                else {
                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                    agentMovement.setStateIndex(0); // JIC if needed
                    agentMovement.setActionIndex(0); // JIC if needed
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    agentMovement.resetGoal();
                }

                if (state.getName() == State.Name.DISPENSER && action.getName() == Action.Name.GETTING_WATER) {
                    currentWaterDispenserInteractionCount++;
                }
                else if (state.getName() == State.Name.REFRIGERATOR && action.getName() == Action.Name.GETTING_FOOD) {
                    currentFridgeInteractionCount++;
                }
                else if (state.getName() == State.Name.COFFEE && action.getName() == Action.Name.MAKE_COFFEE) {
                    currentCoffeeMakerCount++;
                }
                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
            }
        }

        else if(action.getName() == Action.Name.GOING_FRIDGE){
            agentMovement.setSimultaneousInteractionAllowed(false);
            if(agentMovement.getGoalAmenity() == null){
                if(!agentMovement.chooseGoal(Refrigerator.class)){
                    isFull = true;
                    if (agentMovement.getRoutePlan().isTakingLunch()) {
                        int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                        if (index != -1) {
                            agentMovement.setCurrentState(index);
                            agentMovement.setStateIndex(index);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                            agentMovement.resetGoal();
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
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
                        if (agentMovement.isCloseToFinalPatchInPath()) {
                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                            environmentInstance.getUsedAmenities().add(agentMovement.getCurrentAmenity());
                        }
                    }
                }
                else {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        // Expecting that there are no remaining action/GETTING_FOOD is not in the state action
                        if (agentMovement.getRoutePlan().isTakingLunch()) {
                            int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                            if (index != -1) {
                                agentMovement.setCurrentState(index);
                                agentMovement.setStateIndex(index);
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agentMovement.resetGoal();
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }

                    }
                }
            }
        }
        else if(action.getName() == Action.Name.GOING_COFFEEMAKER){
            agentMovement.setSimultaneousInteractionAllowed(false);
            if(agentMovement.getGoalAmenity() == null){
                if(!agentMovement.chooseGoal(CoffeeMakerBar.class)){
                    isFull = true;
                    if (agentMovement.getRoutePlan().isTakingLunch()) {
                        int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                        if (index != -1) {
                            agentMovement.setCurrentState(index);
                            agentMovement.setStateIndex(index);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                            agentMovement.resetGoal();
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
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
                        if (agentMovement.isCloseToFinalPatchInPath()) {
                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                            environmentInstance.getUsedAmenities().add(agentMovement.getCurrentAmenity());
                        }
                    }
                }
                else {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    }
                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        // Expecting that there are no remaining action/GETTING_FOOD is not in the state action
                        if (agentMovement.getRoutePlan().isTakingLunch()) {
                            int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                            if (index != -1) {
                                agentMovement.setCurrentState(index);
                                agentMovement.setStateIndex(index);
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agentMovement.resetGoal();
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }

                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }

                    }
                }
            }
        }
        else if(action.getName() == Action.Name.GOING_DISPENSER){
            agentMovement.setSimultaneousInteractionAllowed(false);
            if(agentMovement.getGoalAmenity() == null){
                if(!agentMovement.chooseGoal(WaterDispenser.class)){
                    isFull = true;
                    if (agentMovement.getRoutePlan().isTakingLunch()) {
                        int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                        if (index != -1) {
                            agentMovement.setCurrentState(index);
                            agentMovement.setStateIndex(index);
                            agentMovement.setActionIndex(0);
                            agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                            agentMovement.resetGoal();
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                        }
                    }
                    else {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
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
                        if (agentMovement.isCloseToFinalPatchInPath()) {
                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                            environmentInstance.getUsedAmenities().add(agentMovement.getCurrentAmenity());

                        }
                    }
                }
                else {
                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                    agentMovement.setActionIndex(0); // JIC needed
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                    }
                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        // Expecting that there are no remaining action/GETTING_WATER is not in the state action
                        if (agentMovement.getRoutePlan().isTakingLunch()) {
                            int index = agentMovement.getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                            if (index != -1) {
                                agentMovement.setCurrentState(index);
                                agentMovement.setStateIndex(index);
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agentMovement.resetGoal();
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                        }
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }
                }
            }
        }


        else if (action.getName() == Action.Name.GO_TO_LUNCH) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (agentMovement.getRoutePlan().getLunchAmenity() == null) {
                    double CHANCE = Simulator.roll();


                    if (type != Agent.Type.MAINTENANCE && type != Agent.Type.GUARD && state.getName() == State.Name.EATING_LUNCH && CHANCE < agentMovement.getRoutePlan().getEAT_OUTSIDE_CHANCE()) {
                        // eat outside
                        if(agent.getTeam() != 0 && type == Agent.Type.STUDENT) {
                            int randomDurationTicks = RANDOM_NUMBER_GENERATOR.nextInt(180, 360);
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("EAT_OUTSIDE", agent, environmentInstance, randomDurationTicks));
                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            // Eat with team members excluding the faculty.
                            // The faculty is used as the Thesis adviser or Faculty Coordinator(?) ex. Doc Briane.

                            for(Agent agent1 : environmentInstance.getPresentTeamMembers(agent.getTeam())) {
                                if(agent1 != agent && agent1.getType() == type) {
                                    int index = agent1.getAgentMovement().getRoutePlan().findIndexState(State.Name.EATING_LUNCH);
                                    if (index != -1) {
                                        agent1.getAgentMovement().setStateIndex(index); // Set the index to EATING_LUNCH
                                        agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan()
                                                .remove(agentMovement.getStateIndex()); // removing finished state
                                        agent1.getAgentMovement().getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("EAT_OUTSIDE", agent, environmentInstance, randomDurationTicks));
                                        agent1.getAgentMovement().setCurrentState(agentMovement.getStateIndex()); // Set to EAT_OUTSIDE
                                        agent1.getAgentMovement().setActionIndex(0); // JIC if needed
                                        agent1.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agent1.getAgentMovement().resetGoal();
                                    }
                                }
                            }
                        }
                        else {
                            int randomDurationTicks = RANDOM_NUMBER_GENERATOR.nextInt(180, 360);
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("EAT_OUTSIDE", agent, environmentInstance, randomDurationTicks));
                            agentMovement.setCurrentState(agentMovement.getStateIndex()); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                        }

                        isFull = true; // convenient way to skip the rest of the code
                    }
                    else if (CHANCE < agentMovement.getRoutePlan().getEAT_FROM_WORKPLACE() + agentMovement.getRoutePlan().getEAT_OUTSIDE_CHANCE() && agentMovement.getAssignedSeat() != null) {
                        agentMovement.setGoalAmenity(agentMovement.getAssignedSeat());
                        agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                        agentMovement.setSeatHeading(agentMovement.getWorkingSeatHeading()); // JIC needed
                    }
                    else if (agentMovement.chooseBreakSeat()) {
                        agentMovement.getRoutePlan().setAtDesk(false); // The agent is leaving to his or her current

                    }
                    else {
                        isFull = true;
                    }

                }
                else {
                    // use this when the agent is doing something in between when taking lunch or taking a break
                    agentMovement.setGoalAmenity(agentMovement.getRoutePlan().getLunchAmenity());
                    agentMovement.setGoalAttractor(agentMovement.getRoutePlan().getLunchAttractor());
                }

            }

            if(isFull) {
                isFull = false;
            }
            else {
                if (agentMovement.chooseNextPatchInPath()) {
                    agentMovement.faceNextPosition();
                    agentMovement.moveSocialForce();
                    if (agentMovement.hasReachedGoal()) {
                        agentMovement.reachGoal();
                        agentMovement.setHeading(agentMovement.getSeatHeading());

                    }
                    else if (agentMovement.hasReachedNextPatchInPath()) {
                        agentMovement.reachPatchInPath();
                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                            agentMovement.reachGoal();
                            agentMovement.setHeading(agentMovement.getSeatHeading());
                        }
                    }
                }
                else {
                    // Developer Note: DO NOT REMOVE THE GO_TO_BREAK or GO_TO_LUNCH action for it serves to know
                    // where does the agent go when doing an urgent task
                    agentMovement.setActionIndex(agentMovement.getActionIndex() + 1); // go to the next action
                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        // The condition below is important it set the last duration of when the agent stop doing
                        // a task (i.e. TAKING_BREAK or EAT_LUNCH)
                        if (agentMovement.getRoutePlan().getLastDuration() <= -1) { // if not set yet
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        }
                        else { // if it was set, get the last duration (Check TAKING_BREAK or EAT_LUNCH)
                            agentMovement.setDuration(agentMovement.getRoutePlan().getLastDuration());
                            agentMovement.getRoutePlan().setLastDuration(-1);
                        }
                        agentMovement.getRoutePlan().setLunchAmenity(agentMovement.getGoalAmenity());
                        agentMovement.getRoutePlan().setLunchAttractor(agentMovement.getGoalAttractor());
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        // Expecting that there are no remaining action/EAT_LUNCH or TAKING_BREAK is not in the state action
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        agentMovement.getRoutePlan().setTakingLunch(false);
                        agentMovement.getRoutePlan().setLunchAmenity(null);
                        agentMovement.getRoutePlan().setLunchAttractor(null);
                    }
                }
            }


        }
        else if (action.getName() == Action.Name.EAT_LUNCH) {
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
                agentMovement.getRoutePlan().setTakingLunch(false);
                agentMovement.getRoutePlan().setLunchAmenity(null);
                agentMovement.getRoutePlan().setLunchAttractor(null);
            }
            else {
                double CHANCE = Simulator.roll();
                if (agentMovement.getRoutePlan().getBATHROOM_CHANCE() != 0.0  && CHANCE < agentMovement.getRoutePlan().getBATHROOM_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_BATHROOM_COOL_DOWN_DURATION)) {
                    if (action.getName() == Action.Name.EAT_LUNCH && agentMovement.getRoutePlan().getBATH_LUNCH() > 0) {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                        agentMovement.setStateIndex(agentMovement.getStateIndex());
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                        agentMovement.getRoutePlan().setBATH_LUNCH(1);
                    }

                }
                else if(agentMovement.getRoutePlan().getDISPENSER_CHANCE() != 0.0  && CHANCE < agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_DISPENSER_COOL_DOWN_DURATION)){
                    if (action.getName() == Action.Name.EAT_LUNCH && agentMovement.getRoutePlan().getDISPENSER_LUNCH() > 0) {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent, environmentInstance));
                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                        agentMovement.setStateIndex(agentMovement.getStateIndex());
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                        agentMovement.getRoutePlan().setDISPENSER_LUNCH(1);
                    }
                }
                else if(agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() != 0.0  && CHANCE < agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() + agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_REFRIGERATOR_COOL_DOWN_DURATION)){
                    if (action.getName() == Action.Name.EAT_LUNCH && agentMovement.getRoutePlan().getREFRIGERATOR_LUNCH() > 0) {
                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent, environmentInstance));
                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                        agentMovement.setStateIndex(agentMovement.getStateIndex());
                        agentMovement.setActionIndex(0);
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                        agentMovement.getRoutePlan().setREFRIGERATOR_LUNCH(1);
                    }
                }
                else if(agentMovement.getRoutePlan().getCOFFEE_CHANCE() != 0.0 && CHANCE < agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() + agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() && agentMovement.getRoutePlan().getCOFFEE_COUNT() > 0 && agentMovement.coolDown(AgentMovement.MAX_COFFEE_COOL_DOWN_DURATION)){
                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("COFFEE", agent, environmentInstance));
                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                    agentMovement.setActionIndex(0);
                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                    agentMovement.resetGoal();
                    agentMovement.getRoutePlan().setLastDuration(agentMovement.getDuration());
                    agentMovement.getRoutePlan().setCOFFEE_COUNT(1);
                }
            }

        }

        else if (action.getName() == Action.Name.CLOSE_BLINDS || action.getName() == Action.Name.OPEN_BLINDS) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (agentMovement.chooseWindowBlindGoal()) {
                    agentMovement.getRoutePlan().setAtDesk(false);
                }
                else {
                    isFull = true;
                    // Skip this action
                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                        agentMovement.setStateIndex(0); // JIC if needed
                        agentMovement.setActionIndex(0); // JIC if needed
                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
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
                        if (agentMovement.isCloseToFinalPatchInPath()) {
                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                        }
                    }
                }
                else {
                    if (agentMovement.getCurrentAmenity() != null && agentMovement.getCurrentAmenity() instanceof WindowBlinds) {
                        if (action.getName() == Action.Name.CLOSE_BLINDS) {
                            ((WindowBlinds) agentMovement.getCurrentAmenity()).open(false);
                            ((WindowBlinds) agentMovement.getCurrentAmenity()).getWindowBlindsGraphic().change(); // change graphic of window
                        }
                        else if (action.getName() == Action.Name.OPEN_BLINDS) {
                            ((WindowBlinds) agentMovement.getCurrentAmenity()).open(true);
                            ((WindowBlinds) agentMovement.getCurrentAmenity()).getWindowBlindsGraphic().change(); // change graphic of window
                        }


                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setBlindsToOpen(null);
                            agentMovement.getGoalAttractor().setIsReserved(false);
                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                            agentMovement.setActionIndex(0); // JIC needed
                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            }
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }

                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
                    }


                }
            }
        }
        else if (action.getName() == Action.Name.TURN_OFF_LIGHT || action.getName() == Action.Name.TURN_ON_LIGHT) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (agentMovement.chooseLightGoal()) {
                    agentMovement.getRoutePlan().setAtDesk(false);
                    agentMovement.setUsingAppliance(true);
                }
                else {
                    isFull = true;
                    if (agentMovement.getLightsToOpen() != null) {
                        agentMovement.setLightsToOpen(null);
                    }
                    // Skip this action
                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        if (agentMovement.getRoutePlan().isLeaving()) {
                            int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                            if(index != -1) {
                                agent.getAgentMovement().setCurrentState(index);
                                agent.getAgentMovement().setStateIndex(index);
                                agent.getAgentMovement().setActionIndex(0);
                                agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                                agent.getAgentMovement().resetGoal();
                                agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
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
                        if (agentMovement.isCloseToFinalPatchInPath()) {
                            agentMovement.setCurrentAmenity(agentMovement.getLightsToOpen());
                            environmentInstance.getUsedAmenities().add(agentMovement.getCurrentAmenity());
                        }
                    }
                }
                else {
                    if (action.getName() == Action.Name.TURN_OFF_LIGHT) {
                        if (agentMovement.getLightsToOpen().isOn()) {
                            currentLightTurnOffCount++;
                            Pair<PatchField, String> patchField = agentMovement.getLightsToOpen().getAttractors().getFirst().getPatch().getPatchField();
                            for (Light light : environmentInstance.getLights()) {
                                for (Amenity.AmenityBlock attractor : light.getAttractors()) {
                                    if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.getKey().toString()) &&
                                            attractor.getPatch().getPatchField().getValue().equals(patchField.getValue())) {
                                        light.setOn(false);
                                        break;
                                    }
                                }
                            }

                            environmentInstance.updatePatchfieldVariation(patchField, true);
                        }
                    }
                    else if (action.getName() == Action.Name.TURN_ON_LIGHT) {

                        Pair<PatchField, String> patchField = agentMovement.getLightsToOpen().getAttractors().getFirst().getPatch().getPatchField();

                        if (!agentMovement.getLightsToOpen().isOn()) {
                            currentLightTurnOnCount++;
                            for (Light light : environmentInstance.getLights()) {
                                for (Amenity.AmenityBlock attractor : light.getAttractors()) {
                                    if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.getKey().toString()) &&
                                            attractor.getPatch().getPatchField().getValue().equals(patchField.getValue())) {
                                        light.setOn(true);
                                        break;
                                    }
                                }
                            }

                            environmentInstance.updatePatchfieldVariation(patchField, false);
                        }

                    }

                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setUsingAppliance(false);
                        agentMovement.setLightsToOpen(null);
                        agentMovement.getGoalAttractor().setIsReserved(false);
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        if (agentMovement.getRoutePlan().isLeaving()) {
                            int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                            if(index != -1) {
                                agent.getAgentMovement().setCurrentState(index);
                                agent.getAgentMovement().setStateIndex(index);
                                agent.getAgentMovement().setActionIndex(0);
                                agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                                agent.getAgentMovement().resetGoal();
                                agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
                    }
                }
            }
        }
        else if (action.getName() == Action.Name.TURN_OFF_AC || action.getName() == Action.Name.TURN_ON_AC ||
                action.getName() == Action.Name.SET_AC_TO_COOL || action.getName() == Action.Name.SET_AC_TO_WARM) {
            agentMovement.setSimultaneousInteractionAllowed(false);
            if (agentMovement.getGoalAmenity() == null) {
                if (agentMovement.chooseAirConGoal()) {
                    agentMovement.setUsingAppliance(true);
                    agentMovement.getRoutePlan().setAtDesk(false);
                }
                else {
                    isFull = true;
                    if (agentMovement.getAirconToChange() != null) {
                        agentMovement.setAirconToChange(null);
                    }
                    // skip
                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        if (agentMovement.getRoutePlan().isLeaving()) {
                            int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                            if(index != -1) {
                                agent.getAgentMovement().setCurrentState(index);
                                agent.getAgentMovement().setStateIndex(index);
                                agent.getAgentMovement().setActionIndex(0);
                                agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                                agent.getAgentMovement().resetGoal();
                                agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
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
                            agentMovement.setCurrentAmenity(agentMovement.getAirconToChange());
                            environmentInstance.getUsedAmenities().add(agentMovement.getCurrentAmenity());
                        }
                    }
                }
                else {
                    if (action.getName() == Action.Name.TURN_OFF_AC && agentMovement.getAirconToChange().isTurnedOn()) {
                        agentMovement.getAirconToChange().setOn(false);
                        agentMovement.getAirconToChange().getAirconGraphic().change();
                        currentAirconTurnOffCount++;
                    }
                    else if (action.getName() == Action.Name.TURN_ON_AC && !agentMovement.getAirconToChange().isTurnedOn()) {
                        agentMovement.getAirconToChange().setOn(true);
                        if (agent.getEnergyProfile() == Agent.EnergyProfile.GREEN) {
                            if (agent.getTempPreference() + 2 <= 25) {
                                agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference() + 2);
                            } else {
                                agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference());
                            }
                        } else {
                            agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference());
                        }
                        agentMovement.getAirconToChange().getAirconGraphic().change();
                        currentAirconTurnOnCount++;
                    }
                    else if(action.getName() == Action.Name.SET_AC_TO_COOL || action.getName() == Action.Name.SET_AC_TO_WARM && !agentMovement.getAirconToChange().isTurnedOn()){
                        if (agent.getEnergyProfile() == Agent.EnergyProfile.GREEN) {
                            if (agent.getTempPreference() + 2 <= 25) {
                                agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference() + 2);
                            } else {
                                agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference());
                            }
                        } else {
                            agentMovement.getAirconToChange().setAirconTemp(agent.getTempPreference());
                        }
                        agentMovement.setToCool(false);
                        agentMovement.setToHeat(false);
                        currentAirconChangeTempInteraction++;
                    }

                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                        agentMovement.setUsingAppliance(false);
                        agentMovement.setAirconToChange(null);
                        agentMovement.getGoalAttractor().setIsReserved(false);
                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                        agentMovement.setActionIndex(0); // JIC needed
                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                        }
                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                        agentMovement.resetGoal();
                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                    }

                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                        if (agentMovement.getRoutePlan().isLeaving()) {
                            int index = agent.getAgentMovement().getRoutePlan().findIndexState(State.Name.GOING_HOME);
                            if(index != -1) {
                                agent.getAgentMovement().setCurrentState(index);
                                agent.getAgentMovement().setStateIndex(index);
                                agent.getAgentMovement().setActionIndex(0);
                                agent.getAgentMovement().setCurrentAction(agent.getAgentMovement().getCurrentState().getActions().get(agent.getAgentMovement().getActionIndex()));
                                agent.getAgentMovement().setDuration(agent.getAgentMovement().getCurrentAction().getDuration());
                                agent.getAgentMovement().resetGoal();
                                agent.getAgentMovement().getRoutePlan().setAtDesk(false);
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0); // JIC if needed
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                agentMovement.resetGoal();
                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                            }
                        }
                        else {
                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                            agentMovement.setStateIndex(0); // JIC if needed
                            agentMovement.setActionIndex(0); // JIC if needed
                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                            agentMovement.resetGoal();
                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                        }
                    }
                }
            }
        }
    }

    private static void moveOne(Agent agent, long currentTick, SimulationTime time) throws Throwable {
        AgentMovement agentMovement = agent.getAgentMovement();

        Agent.Type type = agent.getType();
        Agent.Persona persona = agent.getPersona();
        State state = agentMovement.getCurrentState();
        Action action = agentMovement.getCurrentAction();
        Environment environmentInstance = agentMovement.getEnvironment();

        boolean isFull = false;

        if (!agentMovement.isInteracting() || agentMovement.isSimultaneousInteractionAllowed()) {
            switch (type) {
                case GUARD:
                    // is this where isAtDesk is set to true
                    // where urgent task happen for GUARD
                    if (state.getName() == State.Name.GUARD) {
                        if (action.getName() == Action.Name.GUARD_STAY_PUT) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            if (agentMovement.getGoalAmenity() == null) {
                                // assigned seat has been set already when guard is spawned
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                            }

                            if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();

                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                        agentMovement.reachGoal();
                                        agentMovement.setHeading(agentMovement.getWorkingSeatHeading());
                                        agentMovement.getRoutePlan().setAtDesk(true);
                                    }
                                }

                            }
                            else {
                                agentMovement.getRoutePlan().setAtDesk(true); // signalling that the agent is in his/her desk
                                if (agentMovement.getRoutePlan().getCanUrgent()) {
                                    double CHANCE = Simulator.roll();

                                    if (CHANCE >= agentMovement.getRoutePlan().getWORKING_CHANCE()) {
                                        if (agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentFacultyCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_FACULTY", agent, environmentInstance));
                                            agentMovement.setCurrentState(0);
                                            agentMovement.setStateIndex(0);
                                            agentMovement.setActionIndex(0);
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();

                                        }
                                        else if (agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentStudentCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_STUDENT", agent, environmentInstance));
                                            agentMovement.setCurrentState(0);
                                            agentMovement.setStateIndex(0);
                                            agentMovement.setActionIndex(0);
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                        }
                                        else if (agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE()  && currentGuardCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                            agentMovement.setCurrentState(0);
                                            agentMovement.setStateIndex(0);
                                            agentMovement.setActionIndex(0);
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                        }
                                        else if (agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentMaintenanceCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                            agentMovement.setCurrentState(0);
                                            agentMovement.setStateIndex(0);
                                            agentMovement.setActionIndex(0);
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                        }
                                        else if (agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentDirectorCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                            agentMovement.setCurrentState(0);
                                            agentMovement.setStateIndex(0);
                                            agentMovement.setActionIndex(0);
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                        }
                                        else if (agentMovement.getRoutePlan().getBATHROOM_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_BATHROOM_COOL_DOWN_DURATION)) {
                                            if (agentMovement.getRoutePlan().isBathAM() && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                agentMovement.setActionIndex(0);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.resetGoal();
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.getRoutePlan().setBATH_AM(1);
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            else if (agentMovement.getRoutePlan().isBathPM() && agentMovement.getRoutePlan().getBATH_PM() > 0) {
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                agentMovement.setActionIndex(0);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.resetGoal();
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.getRoutePlan().setBATH_PM(1);
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }

                                        }
                                        else if (agentMovement.getRoutePlan().getCOFFEE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getCOFFEE_COUNT() > 0 && agentMovement.coolDown(AgentMovement.MAX_COFFEE_COOL_DOWN_DURATION)) {
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("COFFEE", agent, environmentInstance));
                                            agentMovement.setCurrentState(agentMovement.getStateIndex());
                                            agentMovement.setStateIndex(agentMovement.getStateIndex());
                                            agentMovement.setActionIndex(0);
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setCOFFEE_COUNT(1);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else if (agentMovement.getRoutePlan().getDISPENSER_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getDISPENSER() > 0 && agentMovement.coolDown(AgentMovement.MAX_DISPENSER_COOL_DOWN_DURATION)) {
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent, environmentInstance));
                                            agentMovement.setCurrentState(agentMovement.getStateIndex());
                                            agentMovement.setStateIndex(agentMovement.getStateIndex());
                                            agentMovement.setActionIndex(0);
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setDISPENSER(1);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else if (agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() + agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getREFRIGERATOR() > 0 && agentMovement.coolDown(AgentMovement.MAX_REFRIGERATOR_COOL_DOWN_DURATION)) {
                                            agentMovement.setUsingAppliance(false);
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent, environmentInstance));
                                            agentMovement.setCurrentState(agentMovement.getStateIndex());
                                            agentMovement.setStateIndex(agentMovement.getStateIndex());
                                            agentMovement.setActionIndex(0);
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setREFRIGERATOR(1);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                    }

                                    if (agentMovement.airconChecker() && agentMovement.thermalComfortCoolDown()) {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("FIX_THERMAL_COMFORT", agent, environmentInstance));
                                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                                        agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else {
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed to set the new action
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                        }

                                    }
                                    if (!agentMovement.visualComfortChecker(time) && agentMovement.visualComfortCoolDown()) {
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("FIX_VISUAL_COMFORT", agent, environmentInstance));
                                        agentMovement.setCurrentState(agentMovement.getStateIndex());
                                        agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false);
                                    }

                                }
                            }
                        }
                    }


                    else if (state.getName() == State.Name.INSPECT_ROOMS) {
                        if (action.getName() == Action.Name.INSPECTING_ROOM) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {

                                int agentCount = 0;
                                PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                // checking if there is an agent present in the room
                                for (Agent agent1 : environmentInstance.getMovableAgents()) {
                                    if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())) {
                                        agentCount++;
                                    }
                                }
                                if (agentCount == 0) {
                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                }
                                else {
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                }
                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                    }
                                }
                            }else{
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0) {
                                    if (agentMovement.getCurrentAmenity() == null && agentMovement.getCurrentAmenity() instanceof Monitor) {
                                        if (((Monitor) agentMovement.getCurrentAmenity()).isOn()) {
                                            ((Monitor) agentMovement.getCurrentAmenity()).setOn(false);
                                        }
                                    }
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                        else if (action.getName() == Action.Name.TURN_OFF_LIGHT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                for (Light light : environmentInstance.getLights()) {
                                    if (light.getAttractors().getFirst().getPatch().equals(agentMovement.getCurrentAction().getDestination())) {
                                        agentMovement.setLightsToOpen(light);
                                        break;
                                    }
                                }

                                if (agentMovement.getLightsToOpen() == null) {
                                    isFull = true;
                                    if (agentMovement.getLightsToOpen() != null) {
                                        agentMovement.setLightsToOpen(null);
                                    }
                                    // Skip this action
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                    String keyField = agentMovement.getCurrentAction().getDestination().getPatchField().getValue();
                                    int closeAgentCount = 0;
                                    for(Agent agent1 : environmentInstance.getMovableAgents()){
                                        for (Amenity.AmenityBlock attractor : agentMovement.getLightsToOpen().getAttractors()) {
                                            if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                                double distanceToLight = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                                if(distanceToLight < agentMovement.getLightsToOpen().getLightRange()) {
                                                    closeAgentCount++;
                                                    break;
                                                }
                                            }
                                        }
                                    }


                                    if (closeAgentCount != 0) {
                                        isFull = true;
                                        if (agentMovement.getLightsToOpen() != null) {
                                            agentMovement.setLightsToOpen(null);
                                        }
                                        // Skip this action
                                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                            agentMovement.setActionIndex(0); // JIC needed
                                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                    else {
                                        if (agentMovement.chooseLightGoal()) {
                                            agentMovement.setUsingAppliance(true);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else {
                                            isFull = true;
                                            if (agentMovement.getLightsToOpen() != null) {
                                                agentMovement.setLightsToOpen(null);
                                            }
                                            // Skip this action
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                                agentMovement.setActionIndex(0); // JIC needed
                                                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }

                                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                                agentMovement.setStateIndex(0); // JIC if needed
                                                agentMovement.setActionIndex(0); // JIC if needed
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }
                                        }
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
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {
                                    if (action.getName() == Action.Name.TURN_OFF_LIGHT) {
                                        Pair<PatchField, String> patchField = agentMovement.getLightsToOpen().getAttractors().getFirst().getPatch().getPatchField();
                                        for (Light light : environmentInstance.getLights()) {
                                            for (Amenity.AmenityBlock attractor : light.getAttractors()) {
                                                if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.getKey().toString()) &&
                                                        attractor.getPatch().getPatchField().getValue().equals(patchField.getValue())) {
                                                    light.setOn(false);
                                                    break;
                                                }
                                            }
                                        }
                                        currentLightTurnOffCount++;
                                        environmentInstance.updatePatchfieldVariation(patchField, true);
                                    }

                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.setLightsToOpen(null);
                                        agentMovement.getGoalAttractor().setIsReserved(false);
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                        }

                        else if (action.getName() == Action.Name.TURN_OFF_AC) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                for (Aircon aircon : environmentInstance.getAircons()) {
                                    if (aircon.getAttractors().getFirst().getPatch().equals(agentMovement.getCurrentAction().getDestination())) {
                                        agentMovement.setAirconToChange(aircon);
                                        break;
                                    }
                                }

                                if (agentMovement.getAirconToChange() == null) {
                                    isFull = true;
                                    if (agentMovement.getAirconToChange() != null) {
                                        agentMovement.setAirconToChange(null);
                                    }
                                    // Skip this action
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                    String keyField = agentMovement.getCurrentAction().getDestination().getPatchField().getValue();

                                    int closeAgentCount = 0;
                                    for(Agent agent1 : environmentInstance.getMovableAgents()){
                                        for (Amenity.AmenityBlock attractor : agentMovement.getAirconToChange().getAttractors()) {
                                            if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                                double distanceToAircon = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                                if(distanceToAircon < agentMovement.getAirconToChange().getCoolingRange()){
                                                    closeAgentCount++;
                                                    break;
                                                }
                                            }
                                        }
                                    }


                                    if (closeAgentCount != 0) {
                                        isFull = true;
                                        if (agentMovement.getAirconToChange() != null) {
                                            agentMovement.setAirconToChange(null);
                                        }
                                        // Skip this action
                                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                            agentMovement.setActionIndex(0); // JIC needed
                                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                    else {
                                        if (agentMovement.chooseAirConGoal()) {
                                            agentMovement.setUsingAppliance(true);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else {
                                            isFull = true;
                                            if (agentMovement.getAirconToChange() != null) {
                                                agentMovement.setAirconToChange(null);
                                            }
                                            // Skip this action
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                                agentMovement.setActionIndex(0); // JIC needed
                                                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }

                                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                                agentMovement.setStateIndex(0); // JIC if needed
                                                agentMovement.setActionIndex(0); // JIC if needed
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }
                                        }
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
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {

                                    if (action.getName() == Action.Name.TURN_OFF_AC) {
                                        agentMovement.getAirconToChange().setOn(false);
                                        agentMovement.getAirconToChange().getAirconGraphic().change();
                                        //currentAirconCount--;
                                        currentAirconTurnOffCount++;
                                    }

                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.setAirconToChange(null);
                                        agentMovement.getGoalAttractor().setIsReserved(false);
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_FACULTY || state.getName() == State.Name.INQUIRE_STUDENT) {
                        if (action.getName() == Action.Name.GO_TO_FACULTY || action.getName() == Action.Name.GO_TO_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_FACULTY && currentFacultyCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_STUDENT && currentStudentCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student

                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_FACULTY || action.getName() == Action.Name.ASK_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_GUARD || state.getName() == State.Name.INQUIRE_MAINTENANCE) {
                        if (action.getName() == Action.Name.GO_TO_GUARD || action.getName() == Action.Name.GO_TO_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_GUARD && currentGuardCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_MAINTENANCE && currentMaintenanceCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_GUARD || action.getName() == Action.Name.ASK_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }

                    break;
                case MAINTENANCE:
                    if (state.getName() == State.Name.MAINTENANCE_BATHROOM) {
                        if (action.getName() == Action.Name.MAINTENANCE_CLEAN_TOILET) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                // Developer Note: OfficeToilet.class is just to satisfy the parameter of the function.
                                // chooseBathroomGoal handles for cleaning of office sinks and office toilets
                                if (!agentMovement.chooseBathroomGoal(OfficeToilet.class)) {
                                    // If full cleaning is already been done
                                    isFull = true;
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {

                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0) {
                                        environmentInstance.setOfficeToiletCleaned(agentMovement.getGoalAmenity().getAttractors().getFirst().getPatch(), true); // set the office toilet to cleaned
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                        if (action.getName() == Action.Name.MAINTENANCE_CLEAN_SINK) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                // Developer Note: OfficeSink.class is just to satisfy the parameter of the function.
                                // chooseBathroomGoal handles for cleaning of sinks and toilets
                                if (!agentMovement.chooseBathroomGoal(OfficeSink.class)) {
                                    // If full cleaning is already been done
                                    isFull = true;
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {

                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0) {
                                        environmentInstance.setOfficeSinkCleaned(agentMovement.getGoalAmenity().getAttractors().getFirst().getPatch(), true); // set the office sink to cleaned
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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

                                int agentCount = 0;
                                PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                // checking if there is an agent present in the room
                                for (Agent agent1 : environmentInstance.getMovableAgents()) {
                                    if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                            && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())) {
                                        agentCount++;
                                    }
                                }
                                if (agentCount == 0) {
                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                }
                                else {
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.resetGoal();
                                    }
                                }
                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                    }
                                }
                            }else{
                                agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                agentMovement.setDuration(agentMovement.getDuration() - 1);
                                if (agentMovement.getDuration() <= 0) {
                                    if (agentMovement.getCurrentAmenity() == null && agentMovement.getCurrentAmenity() instanceof Monitor) {
                                        if (((Monitor) agentMovement.getCurrentAmenity()).isOn()) {
                                            ((Monitor) agentMovement.getCurrentAmenity()).setOn(false);
                                        }
                                    }
                                    agentMovement.getRoutePlan().setAtDesk(false);
                                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                        else if (action.getName() == Action.Name.TURN_OFF_LIGHT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                for (Light light : environmentInstance.getLights()) {
                                    if (light.getAttractors().getFirst().getPatch().equals(agentMovement.getCurrentAction().getDestination())) {
                                        agentMovement.setLightsToOpen(light);
                                        break;
                                    }
                                }

                                if (agentMovement.getLightsToOpen() == null) {
                                    isFull = true;
                                    if (agentMovement.getLightsToOpen() != null) {
                                        agentMovement.setLightsToOpen(null);
                                    }
                                    // Skip this action
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                    String keyField = agentMovement.getCurrentAction().getDestination().getPatchField().getValue();
                                    int closeAgentCount = 0;
                                    for(Agent agent1 : environmentInstance.getMovableAgents()){
                                        for (Amenity.AmenityBlock attractor : agentMovement.getLightsToOpen().getAttractors()) {
                                            if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                                double distanceToLight = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                                if(distanceToLight < agentMovement.getLightsToOpen().getLightRange()){
                                                    closeAgentCount++;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (closeAgentCount != 0) {
                                        isFull = true;
                                        if (agentMovement.getLightsToOpen() != null) {
                                            agentMovement.setLightsToOpen(null);
                                        }
                                        // Skip this action
                                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                            agentMovement.setActionIndex(0); // JIC needed
                                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                    else {
                                        if (agentMovement.chooseLightGoal()) {
                                            agentMovement.setUsingAppliance(true);
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else {
                                            isFull = true;
                                            if (agentMovement.getLightsToOpen() != null) {
                                                agentMovement.setLightsToOpen(null);
                                            }
                                            // Skip this action
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                                agentMovement.setActionIndex(0); // JIC needed
                                                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }

                                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                                agentMovement.setStateIndex(0); // JIC if needed
                                                agentMovement.setActionIndex(0); // JIC if needed
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }
                                        }
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
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {

                                    if (action.getName() == Action.Name.TURN_OFF_LIGHT) {
                                        Pair<PatchField, String> patchField = agentMovement.getLightsToOpen().getAttractors().getFirst().getPatch().getPatchField();
                                        for (Light light : environmentInstance.getLights()) {
                                            for (Amenity.AmenityBlock attractor : light.getAttractors()) {
                                                if (attractor.getPatch().getPatchField().getKey().toString().equals(patchField.getKey().toString()) &&
                                                        attractor.getPatch().getPatchField().getValue().equals(patchField.getValue())) {
                                                    light.setOn(false);
                                                    break;
                                                }
                                            }
                                        }
                                        currentLightTurnOffCount++;
                                        environmentInstance.updatePatchfieldVariation(patchField, true);
                                    }

                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.setLightsToOpen(null);
                                        agentMovement.getGoalAttractor().setIsReserved(false);
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                        }

                        else if (action.getName() == Action.Name.TURN_OFF_AC) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                for (Aircon aircon : environmentInstance.getAircons()) {
                                    if (aircon.getAttractors().getFirst().getPatch().equals(agentMovement.getCurrentAction().getDestination())) {
                                        agentMovement.setAirconToChange(aircon);
                                        break;
                                    }
                                }

                                if (agentMovement.getAirconToChange() == null) {
                                    isFull = true;
                                    if (agentMovement.getAirconToChange() != null) {
                                        agentMovement.setAirconToChange(null);
                                    }
                                    // Skip this action
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                                else {
                                    PatchField patchField = agentMovement.getCurrentAction().getDestination().getPatchField().getKey();
                                    String keyField = agentMovement.getCurrentAction().getDestination().getPatchField().getValue();

                                    int closeAgentCount = 0;
                                    for(Agent agent1 : environmentInstance.getMovableAgents()){
                                        for (Amenity.AmenityBlock attractor : agentMovement.getAirconToChange().getAttractors()) {
                                            if (agent1 != agent && agent1.getAgentMovement().getCurrentState().getName() != State.Name.GOING_HOME
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getKey().toString().equals(patchField.toString())
                                                    && agent1.getAgentMovement().getCurrentPatch().getPatchField().getValue().equals(keyField)) {
                                                double distanceToAircon = Coordinates.distance(agent1.getAgentMovement().getCurrentPatch(), attractor.getPatch() );
                                                if(distanceToAircon < agentMovement.getAirconToChange().getCoolingRange()){
                                                    closeAgentCount++;
                                                    break;
                                                }
                                            }
                                        }
                                    }


                                    if (closeAgentCount != 0) {
                                        isFull = true;
                                        if (agentMovement.getAirconToChange() != null) {
                                            agentMovement.setAirconToChange(null);
                                        }
                                        // Skip this action
                                        if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                            agentMovement.setActionIndex(0); // JIC needed
                                            if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            }
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                    else {
                                        if (agentMovement.chooseAirConGoal()) {
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }
                                        else {
                                            isFull = true;
                                            if (agentMovement.getAirconToChange() != null) {
                                                agentMovement.setAirconToChange(null);
                                            }
                                            // Skip this action
                                            if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                                agentMovement.setActionIndex(0); // JIC needed
                                                if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }

                                            if (agentMovement.getCurrentState().getActions().isEmpty()){
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                                agentMovement.setStateIndex(0); // JIC if needed
                                                agentMovement.setActionIndex(0); // JIC if needed
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                            }
                                        }
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
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {
                                    agentMovement.setUsingAppliance(true);
                                    if (action.getName() == Action.Name.TURN_OFF_AC) {
                                        agentMovement.getAirconToChange().setOn(false);
                                        agentMovement.getAirconToChange().getAirconGraphic().change();
                                        //currentAirconCount--;
                                        currentAirconTurnOffCount++;

                                    }

                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.setAirconToChange(null);
                                        agentMovement.getGoalAttractor().setIsReserved(false);
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.setUsingAppliance(false);
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
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
                                    if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }

                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                        agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                    }
                                }
                            }
                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();
                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration());
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }
                                }
                                else {
                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0) {
                                        environmentInstance.setPlantWatered(agentMovement.getGoalAmenity().getAttractors().getFirst().getPatch(), true); // set the plant to watered
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
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
                    // is this where isAtDesk is set to true
                    // where urgent task happen for MAINTENANCE
                    else if (state.getName() == State.Name.WAIT_FOR_ACTIVITY) {
                        if (action.getName() == Action.Name.GO_TO_WAIT_AREA) {
                            agentMovement.setSimultaneousInteractionAllowed(true);
                            if (agentMovement.getGoalAmenity() == null) {
                                if (agentMovement.getAssignedSeat() != null) {
                                    agentMovement.setGoalAmenity(agentMovement.getAssignedSeat());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());
                                    agentMovement.setSeatHeading(agentMovement.getWorkingSeatHeading());
                                }
                                else {
                                    isFull = !agentMovement.chooseBreakSeat(); // goalAmenity and attractor is set here if true
                                }
                            }

                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();

                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.hasAgentReachedFinalPatchInPath()) {
                                            agentMovement.setHeading(agentMovement.getSeatHeading());
                                            agentMovement.reachGoal();
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        }
                                    }

                                }
                                else {
                                    agentMovement.getRoutePlan().setAtDesk(true); // signalling that the agent is in his/her desk

                                    if (agentMovement.getRoutePlan().getCanUrgent()) {
                                        double CHANCE = Simulator.roll();
                                        if (CHANCE >= agentMovement.getRoutePlan().getWORKING_CHANCE()) {
                                            if (agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentFacultyCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_FACULTY", agent, environmentInstance));
                                                agentMovement.setCurrentState(0);
                                                agentMovement.setStateIndex(0);
                                                agentMovement.setActionIndex(0);
                                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();

                                            }
                                            else if (agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentStudentCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_STUDENT", agent, environmentInstance));
                                                agentMovement.setCurrentState(0);
                                                agentMovement.setStateIndex(0);
                                                agentMovement.setActionIndex(0);
                                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                            }
                                            else if (agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE()  && currentGuardCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                                agentMovement.setCurrentState(0);
                                                agentMovement.setStateIndex(0);
                                                agentMovement.setActionIndex(0);
                                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                            }
                                            else if (agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentMaintenanceCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                                agentMovement.setCurrentState(0);
                                                agentMovement.setStateIndex(0);
                                                agentMovement.setActionIndex(0);
                                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                            }
                                            else if (agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && currentDirectorCount > 0 && agentMovement.coolDown(AgentMovement.MAX_INQUIRE_COOL_DOWN_DURATION)){
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(0, agentMovement.getRoutePlan().addUrgentRoute("INQUIRE_GUARD", agent, environmentInstance));
                                                agentMovement.setCurrentState(0);
                                                agentMovement.setStateIndex(0);
                                                agentMovement.setActionIndex(0);
                                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                agentMovement.resetGoal();
                                            }
                                            else if (agentMovement.getRoutePlan().getBATHROOM_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.coolDown(AgentMovement.MAX_BATHROOM_COOL_DOWN_DURATION)) {
                                                if (agentMovement.getRoutePlan().isBathAM() && agentMovement.getRoutePlan().getBATH_AM() > 0) {
                                                    agentMovement.setUsingAppliance(false);
                                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                    agentMovement.setActionIndex(0);
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.resetGoal();
                                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                    agentMovement.getRoutePlan().setBATH_AM(1);
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }
                                                else if (agentMovement.getRoutePlan().isBathPM() && agentMovement.getRoutePlan().getBATH_PM() > 0) {
                                                    agentMovement.setUsingAppliance(false);
                                                    agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("BATHROOM", agent, environmentInstance));
                                                    agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                    agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                    agentMovement.setActionIndex(0);
                                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                    agentMovement.resetGoal();
                                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                                    agentMovement.getRoutePlan().setBATH_PM(1);
                                                    agentMovement.getRoutePlan().setAtDesk(false);
                                                }

                                            }
                                            else if (agentMovement.getRoutePlan().getCOFFEE_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getCOFFEE_COUNT() > 0 && agentMovement.coolDown(AgentMovement.MAX_COFFEE_COOL_DOWN_DURATION)) {
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("COFFEE", agent, environmentInstance));
                                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                agentMovement.setActionIndex(0);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setCOFFEE_COUNT(1);
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            else if (agentMovement.getRoutePlan().getDISPENSER_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getDISPENSER() > 0 && agentMovement.coolDown(AgentMovement.MAX_DISPENSER_COOL_DOWN_DURATION)) {
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("DISPENSER", agent, environmentInstance));
                                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                agentMovement.setActionIndex(0);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setDISPENSER(1);
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                            else if (agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() != 0.0 && 1.0 - CHANCE < agentMovement.getRoutePlan().getREFRIGERATOR_CHANCE() + agentMovement.getRoutePlan().getDISPENSER_CHANCE() + agentMovement.getRoutePlan().getCOFFEE_CHANCE() + agentMovement.getRoutePlan().getBATHROOM_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_DIRECTOR_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_MAINTENANCE_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_GUARD_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_STUDENT_CHANCE() + agentMovement.getRoutePlan().getINQUIRE_FACULTY_CHANCE() && agentMovement.getRoutePlan().getREFRIGERATOR() > 0 && agentMovement.coolDown(AgentMovement.MAX_REFRIGERATOR_COOL_DOWN_DURATION)) {
                                                agentMovement.setUsingAppliance(false);
                                                agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("REFRIGERATOR", agent, environmentInstance));
                                                agentMovement.setCurrentState(agentMovement.getStateIndex());
                                                agentMovement.setStateIndex(agentMovement.getStateIndex());
                                                agentMovement.setActionIndex(0);
                                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                                agentMovement.resetGoal();
                                                agentMovement.getRoutePlan().setREFRIGERATOR(1);
                                                agentMovement.getRoutePlan().setAtDesk(false);
                                            }
                                        }

                                        if (!agentMovement.visualComfortChecker(time) && agentMovement.visualComfortCoolDown()) {
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().add(agentMovement.getStateIndex(), agentMovement.getRoutePlan().addUrgentRoute("FIX_VISUAL_COMFORT", agent, environmentInstance));
                                            agentMovement.setCurrentState(agentMovement.getStateIndex());
                                            agentMovement.setStateIndex(agentMovement.getStateIndex()); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }

                                    }
                                }

                            }

                        }
                    }

                    // Going to storage cabinet
                    else if (state.getName() == State.Name.GOING_TO_WORK) {
                        if (action.getName() == Action.Name.GO_TO_STATION) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());

                            }

                            if(isFull) {
                                isFull = false;
                            }
                            else {
                                if (agentMovement.chooseNextPatchInPath()) {
                                    agentMovement.faceNextPosition();
                                    agentMovement.moveSocialForce();

                                    if (agentMovement.hasReachedNextPatchInPath()) {
                                        agentMovement.reachPatchInPath();
                                        if (agentMovement.isCloseToFinalPatchInPath()) {
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        }
                                    }

                                }
                                else if (agentMovement.getCurrentState().getName() == State.Name.GOING_TO_EAT_OUTSIDE ||
                                        agentMovement.getCurrentState().getName() == State.Name.GOING_HOME || agentMovement.getDuration() > -1) {

                                    agentMovement.getRoutePlan().setAtDesk(true); // signalling that the agent is in his/her desk

                                    agentMovement.setDuration(agentMovement.getDuration() - 1);
                                    if (agentMovement.getDuration() <= 0) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC if needed to set the new action
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action


                                        // setting isAtDesk true is important for leaving the environment permanently/partially
                                        if (state.getName() != State.Name.GOING_TO_EAT_OUTSIDE && state.getName() != State.Name.GOING_HOME) {

                                            agentMovement.getRoutePlan().setAtDesk(false);
                                        }

                                        agentMovement.resetGoal();
                                    }
                                    if (agentMovement.getCurrentState().getActions().isEmpty()){
                                        agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                        agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                        agentMovement.setStateIndex(0); // JIC if needed
                                        agentMovement.setActionIndex(0); // JIC if needed to set the new action
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();
                                    }
                                }
                            }


                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_FACULTY || state.getName() == State.Name.INQUIRE_STUDENT) {
                        if (action.getName() == Action.Name.GO_TO_FACULTY || action.getName() == Action.Name.GO_TO_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_FACULTY && currentFacultyCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_STUDENT && currentStudentCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student

                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_FACULTY || action.getName() == Action.Name.ASK_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_GUARD || state.getName() == State.Name.INQUIRE_MAINTENANCE) {
                        if (action.getName() == Action.Name.GO_TO_GUARD || action.getName() == Action.Name.GO_TO_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_GUARD && currentGuardCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_MAINTENANCE && currentMaintenanceCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student

                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_GUARD || action.getName() == Action.Name.ASK_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }
                    break;

                case DIRECTOR:
                    if (state.getName() == State.Name.INQUIRE_FACULTY) {
                        if (action.getName() == Action.Name.GO_TO_FACULTY) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                // For director the destination is set in RoutePlan addUrgentRoute
                                if (agentMovement.getCurrentAction().getDestination() != null) {
                                    agentMovement.setGoalAmenity(agentMovement.getCurrentAction().getDestination().getAmenityBlock().getParent());
                                    agentMovement.setGoalAttractor(agentMovement.getGoalAmenity().getAttractors().getFirst());

                                    for (Agent agent1 : environmentInstance.getMovableAgents()) {
                                        if (agent1 != agent && agent1.getAgentMovement().getAssignedSeat() != null &&
                                                agentMovement.getGoalAttractor().getPatch().equals(agent1.getAgentMovement().getAssignedSeat().getAttractors().getFirst().getPatch())) {
                                            agentMovement.setAgentToInquire(agent1);
                                            break;
                                        }
                                    }
                                }
                                else if (currentFacultyCount <= 0) { // no faculty
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }
                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.setCurrentAmenity(agentMovement.getGoalAmenity());
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_FACULTY) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                    agentMovement.setActionIndex(0); // JIC needed
                                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    }
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
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
                            else {
                                if (!agentMovement.getCurrentState().getActions().isEmpty()) {
                                    agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                    agentMovement.setActionIndex(0); // JIC needed
                                    if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                        agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    }
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
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
                    else if (state.getName() == State.Name.INQUIRE_STUDENT) {
                        if (action.getName() == Action.Name.GO_TO_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_STUDENT && currentStudentCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_GUARD || state.getName() == State.Name.INQUIRE_MAINTENANCE) {
                        if (action.getName() == Action.Name.GO_TO_GUARD || action.getName() == Action.Name.GO_TO_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_GUARD && currentGuardCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_MAINTENANCE && currentMaintenanceCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_GUARD || action.getName() == Action.Name.ASK_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else {
                        doCommonAction(agentMovement, state, action, agent, type, persona, environmentInstance, currentTick, time);
                    }
                    break;

                case FACULTY: case STUDENT:
                    if (state.getName() == State.Name.INQUIRE_FACULTY || state.getName() == State.Name.INQUIRE_STUDENT) {
                        if (action.getName() == Action.Name.GO_TO_FACULTY || action.getName() == Action.Name.GO_TO_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_FACULTY && currentFacultyCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_STUDENT && currentStudentCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_FACULTY || action.getName() == Action.Name.ASK_STUDENT) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
                            }
                        }
                    }
                    else if (state.getName() == State.Name.INQUIRE_GUARD || state.getName() == State.Name.INQUIRE_MAINTENANCE) {
                        if (action.getName() == Action.Name.GO_TO_GUARD || action.getName() == Action.Name.GO_TO_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);
                            if (agentMovement.getGoalAmenity() == null) {
                                if ((action.getName() == Action.Name.GO_TO_GUARD && currentGuardCount <= 0) ||
                                        (action.getName() == Action.Name.GO_TO_MAINTENANCE && currentMaintenanceCount <= 0) || !agentMovement.chooseAgentAsGoal()) { // no faculty or student
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0); // JIC if needed
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                    agentMovement.resetGoal();
                                    agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                }

                            }
                            else if (agentMovement.chooseNextPatchInPath()) {
                                agentMovement.faceNextPosition();
                                agentMovement.moveSocialForce();
                                if (agentMovement.hasReachedNextPatchInPath()) {
                                    agentMovement.reachPatchInPath();
                                    if (agentMovement.isCloseToFinalPatchInPath()) {
                                        agentMovement.getCurrentState().getActions().remove(agentMovement.getActionIndex()); // removing finished action
                                        agentMovement.setActionIndex(0); // JIC needed
                                        if(!agentMovement.getCurrentState().getActions().isEmpty()) {
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                        }
                                        agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                        agentMovement.resetGoal();

                                        if (agentMovement.getCurrentState().getActions().isEmpty()){
                                            agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                            agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                            agentMovement.setStateIndex(0); // JIC if needed
                                            agentMovement.setActionIndex(0); // JIC if needed
                                            agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                            agentMovement.setDuration(agentMovement.getCurrentAction().getDuration()); // setting the new duration of the action
                                            agentMovement.resetGoal();
                                            agentMovement.getRoutePlan().setAtDesk(false); // JIC if needed
                                        }
                                    }
                                }
                            }
                        }
                        else if (action.getName() == Action.Name.ASK_GUARD || action.getName() == Action.Name.ASK_MAINTENANCE) {
                            agentMovement.setSimultaneousInteractionAllowed(false);

                            if (agentMovement.getAgentToInquire() != null &&
                                    agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().isAtDesk()) {
                                agentMovement.setDuration(agentMovement.getDuration() - 1);


                                if (agentMovement.getDuration() <= 0) {
                                    agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                    agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                    agentMovement.setStateIndex(0); // JIC if needed
                                    agentMovement.setActionIndex(0);
                                    agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                    // Set faculty's canUrgent to true. Expecting that the director is done inquiring to all
                                    // faculties.
                                    if (agentMovement.getAgentToInquire() != null) {
                                        agentMovement.getAgentToInquire().getAgentMovement().getRoutePlan().setCanUrgent(true);
                                    }
                                    agentMovement.resetGoal();
                                }
                            }
                            else {
                                agentMovement.getRoutePlan().getCurrentRoutePlan().remove(agentMovement.getStateIndex()); // removing finished state
                                agentMovement.setCurrentState(0); // JIC if needed to setting the next current state based on the agent's route plan
                                agentMovement.setStateIndex(0); // JIC if needed
                                agentMovement.setActionIndex(0);
                                agentMovement.setCurrentAction(agentMovement.getCurrentState().getActions().get(agentMovement.getActionIndex()));
                                agentMovement.resetGoal();
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


    public static void runWattageCount(long currentTick, Environment environment){
        //PUT RANDOM TIMES OF FLUCTUATION
        //multiplied to 5 since 5 seconds per tick?
        //APPLIANCES
        // int waterDispenserCount = environment.getWaterDispensers().size();
        //int fridgeCount = environment.getRefrigerators().size();
        int activeLightCount = 0; //check how many lights are on
        int activeMonitorCount = 0; //check how many monitors are on;
        int activeAirConCount = 0; //check how many airCon are on;


        for(WaterDispenser dispenser : environment.getWaterDispensers()){
            if(!dispenser.isActiveCycle()){
                totalWattageCount += ((waterDispenserWattage * 5) / 3600);
                currentPowerConsumption += waterDispenserWattage;
            }
        }
        for(Refrigerator refrigerator : environment.getRefrigerators()){
            if(!refrigerator.isActiveCycle()){
                totalWattageCount += ((fridgeWattage * 5) / 3600);
                currentPowerConsumption += fridgeWattage;
            }
        }

        for (Aircon aircon : environment.getAircons()) {
            if (aircon.isTurnedOn() && airconWattage < airconWattageActive) {
                activeAirConCount++;
                if (!aircon.isInActiveCycle()) {
                    totalWattageCount+= ((airconWattage * 5) / 3600);
                    currentPowerConsumption += airconWattage;
                }
                else {
                    float watts = RANDOM_NUMBER_GENERATOR.nextFloat(airconWattage, airconWattageActive);
                    totalWattageCount+= (watts * 5) / 3600;
                    currentPowerConsumption += watts;
                }
            }
        }

        //set current switched on aircon
        currentAirconCount = activeAirConCount;

        for (Light light : environment.getLights()) {
            if (light.isOn()) {
                activeLightCount++;
            }
        }
        currentLightCount = activeLightCount;

        // Check Monitor in Cubicle
        for (Cubicle cubicle : environment.getCubicles()) {
            for (Monitor monitor : cubicle.getMonitors()) {
                if (monitor.isOn()) {
                    activeMonitorCount++;
                    if(!countedMonitors.contains(monitor)){
                        totalMonitorInteractionCount++;
                    }
                    countedMonitors.add(monitor);
                }
            }
        }
        // Check Monitor in DirectorTable
        for (DirectorTable directorTable : environment.getDirectorTables()) {
            for (Monitor monitor : directorTable.getMonitors()) {
                if (monitor.isOn()) {
                    activeMonitorCount++;
                    if(!countedMonitors.contains(monitor)){
                        totalMonitorInteractionCount++;
                    }
                    countedMonitors.add(monitor);
                }
            }
        }
        // Check Monitor in ResearchTable
        for (ResearchTable researchTable : environment.getResearchTables()) {
            for (Monitor monitor : researchTable.getMonitors()) {
                if (monitor.isOn()) {
                    activeMonitorCount++;
                    if(!countedMonitors.contains(monitor)){
                        totalMonitorInteractionCount++;
                    }
                    countedMonitors.add(monitor);
                }
            }
        }

        currentMonitorCount = activeMonitorCount;


        //TODO: EVERY USE OF REF, COOLNESS LEVEL GOES DOWN BY 1 OR 2
        //ACTIVE CYCLE FOR EVERY REFRIGERATOR
        for(Refrigerator ref : environment.getRefrigerators()){
            if(fridgeWattageActive <= 0 || fridgeWattageInUse <= 0 || fridgeWattage <= 0){
                break;
            }
            //IF REF IS IN ACTIVE CYCLE
            if(ref.isActiveCycle()){
                //increase coolness level
                ref.setCoolnessLevel((ref.getCoolnessLevel() + 1));
                if(ref.getDuration() > 0){
                    float watt = RANDOM_NUMBER_GENERATOR.nextFloat(fridgeWattage, fridgeWattageActive);
                    totalWattageCount += (watt * 5) / 3600;
                    currentPowerConsumption += watt;
                    ref.setDuration((ref.getDuration() - 1));
                }
                else{
                    ref.setActiveCycle(false);
                }
            }//IF REF IS NOT IN ACTIVE CYCLE
            else{
                //decrease coolness level by decay rate
                ref.setCoolnessLevel((ref.getCoolnessLevel() - environment.getDecayRateRef()));
                //EVERY 60 TICKS CALCULATE CHANCE OF ACTIVE CYCLE

                if(currentTick % 60L == 0){
                    double activeCycleChance = max(0,(100 - ref.getCoolnessLevel()) / 100);
                    double CHANCE = Simulator.roll();
                    if(CHANCE < activeCycleChance){
                        ref.setActiveCycle(true);
                        ref.setDuration(RANDOM_NUMBER_GENERATOR.nextInt(120,240));
                    }
                }
            }
        }
        for(WaterDispenser dispenser : environment.getWaterDispensers()){
            if(waterDispenserWattageActive <= 0 || waterDispenserWattageInUse <= 0 || waterDispenserWattage <= 0 || waterDispenserWattageActiveHigh <= 0){
                break;
            }
            //IF DISPENSER IS IN ACTIVE CYCLE
            if(dispenser.isActiveCycle()){
                //increase coolness level
                dispenser.setCoolnessLevel((dispenser.getCoolnessLevel() + 1));
                if(dispenser.getDuration() > 0){

                    // Check if in high wattage active cycle
                    if(dispenser.isHighActiveCycle() && dispenser.getWaterLevel() < 80){
                        totalWattageCount += (waterDispenserWattageActiveHigh * 5) / 3600;
                        currentPowerConsumption += waterDispenserWattageActiveHigh;
                        dispenser.setWaterLevel(dispenser.getWaterLevel() + 1);
                    } else if(dispenser.isActiveCycle()){
                        float watt = RANDOM_NUMBER_GENERATOR.nextFloat(waterDispenserWattage, waterDispenserWattageActive);
                        totalWattageCount += ((watt * 5) / 3600);
                        currentPowerConsumption += watt;
                    }
                    dispenser.setDuration((dispenser.getDuration() - 1));
                }
                else if(dispenser.getDuration() <= 0 && dispenser.isHighActiveCycle()){
                    dispenser.setHighActiveCycle(false);
                    dispenser.setDuration((240 + RANDOM_NUMBER_GENERATOR.nextInt(61) - 5));
                }
                else if(dispenser.getDuration() <= 0 && dispenser.isActiveCycle()){
                    dispenser.setActiveCycle(false);
                    dispenser.setHighActiveCycle(false);
                }
            }//IF dispenser IS NOT IN ACTIVE CYCLE
            else{
                //decrease coolness level by decay rate
                dispenser.setCoolnessLevel((dispenser.getCoolnessLevel() - environment.getDecayRateDispenser()));
                //EVERY 60 TICKS CALCULATE CHANCE OF ACTIVE CYCLE

                if(currentTick % 60L == 0){
                    double activeCycleChance = max(0,(100 - dispenser.getCoolnessLevel()) / 100);
                    double CHANCE = Simulator.roll();


                    if(CHANCE < activeCycleChance && !dispenser.isHighActiveCycle()){
                        dispenser.setActiveCycle(true);
                        dispenser.setDuration(240 + RANDOM_NUMBER_GENERATOR.nextInt(61) - 5); // Duration 235 to 245 ticks
                    }
                }
                double CHANCE = Simulator.roll();
                double lowWater_Threshold = 50;
                // Check if water level is low to trigger high wattage active cycle
                if(dispenser.getWaterLevel() < lowWater_Threshold){
                    double highActiveCycleChance = (lowWater_Threshold - dispenser.getWaterLevel()) / lowWater_Threshold;

                    if(CHANCE < highActiveCycleChance){
                        dispenser.setActiveCycle(true);
                        dispenser.setHighActiveCycle(true); // Set high wattage cycle flag
                        dispenser.setDuration((int) (100 - dispenser.getWaterLevel())); // Duration depends on water level
                    }
                }
            }
        }

        totalWattageCount+= ((lightWattage * activeLightCount * 5) / 3600);
        currentPowerConsumption += lightWattage * activeLightCount;

        totalWattageCount+= ((monitorWattage * activeMonitorCount * 5) / 3600);
        currentPowerConsumption += monitorWattage * activeMonitorCount;
    }
    public void replenishStaticVars() {

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
        //FOR AIRCONS, LIGHTS, AND MONITORS, COUNT THE NUMBER OF TURNED ON APPLIANCES
        currentAirconCount = 0;
        currentLightCount = 0;
        currentMonitorCount = 0;
        totalMonitorInteractionCount = 0;

        currentAirconTurnOnCount = 0;
        currentAirconTurnOffCount = 0;
        currentLightTurnOnCount = 0;
        currentLightTurnOffCount = 0;
        currentFridgeInteractionCount = 0;
        currentWaterDispenserInteractionCount = 0;

        // Average Interaction Duration
        averageNonverbalDuration = 0;
        averageCooperativeDuration = 0;
        averageExchangeDuration = 0;


        // Current Total Social Interaction Count per 5 mins
        currentTotalSocialInteractionCountPer5Mins = 0;


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
        compiledCurrentDirectorCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentFacultyCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentStudentCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentMaintenanceCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentGuardCount = new int[MAX_CURRENT_TICKS];

        // Current Interaction Count
        compiledCurrentNonverbalCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentCooperativeCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentExchangeCount = new int[MAX_CURRENT_TICKS];

        // Current Power Consumption Interaction Count
        compiledCurrentAirconCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentLightCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentMonitorCount = new int[MAX_CURRENT_TICKS];
        compiledTotalMonitorInteractionCount = new int[MAX_CURRENT_TICKS];

        compiledCurrentAirconTurnOnCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentAirconTurnOffCount = new int[MAX_CURRENT_TICKS];

        compiledCurrentLightTurnOnCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentLightTurnOffCount = new int[MAX_CURRENT_TICKS];

        compiledCurrentFridgeInteractionCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentWaterDispenserInteractionCount = new int[MAX_CURRENT_TICKS];


        compiledCurrentAirconChangeTempInteraction = new int[MAX_CURRENT_TICKS];
        compiledCurrentPowerConsumption = new float[MAX_CURRENT_TICKS];
        compiledTotalWattageCount = new float [MAX_CURRENT_TICKS];




        // Average Interaction Duration
        compiledAverageNonverbalDuration = new float[MAX_CURRENT_TICKS];
        compiledAverageCooperativeDuration = new float[MAX_CURRENT_TICKS];
        compiledAverageExchangeDuration = new float[MAX_CURRENT_TICKS];


        // Current Team Count
        compiledTotalSocialInteractionper5min = new int[MAX_CURRENT_TICKS];

        // Current Director to ____ Interaction Count
        compiledCurrentDirectorFacultyCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentDirectorStudentCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentDirectorMaintenanceCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentDirectorGuardCount = new int[MAX_CURRENT_TICKS];


        // Current Faculty to ____ Interaction Count
        compiledCurrentFacultyFacultyCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentFacultyStudentCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentFacultyMaintenanceCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentFacultyGuardCount = new int[MAX_CURRENT_TICKS];


        // Current Student to ____ Interaction Count
        compiledCurrentStudentStudentCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentStudentMaintenanceCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentStudentGuardCount = new int[MAX_CURRENT_TICKS];


        // Current Maintenance to ____ Interaction Count
        compiledCurrentMaintenanceMaintenanceCount = new int[MAX_CURRENT_TICKS];
        compiledCurrentMaintenanceGuardCount = new int[MAX_CURRENT_TICKS];


        // Current Guard to Guard Interaction Count
        compiledCurrentGuardGuardCount = new int[MAX_CURRENT_TICKS];


        /** Patch Count **/
        currentPatchCount = new int[environment.getRows()][environment.getColumns()];;


    }

    public static void exportToCSV() throws Exception{
        PrintWriter writer = new PrintWriter("Office Simulation Statistics.csv");
        StringBuilder sb = new StringBuilder();
        sb.append("Energy Consumption");
        sb.append(",");
        sb.append("Power Consumption");
        sb.append(",");
        sb.append("Total Social Interaction per 5 mins");
        sb.append(",");
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
        sb.append("Current Aircon Count");
        sb.append(",");
        sb.append("Current Light Count");
        sb.append(",");
        sb.append("Current Monitor Count");
        sb.append(",");
        sb.append("Total Monitor Interaction Count");
        sb.append(",");
        sb.append("Current Aircon Turn On Count");
        sb.append(",");
        sb.append("Current Aircon Turn Off Count");
        sb.append(",");
        sb.append("Current Aircon Change Temp Count");
        sb.append(",");
        sb.append("Current Light Turn On Count");
        sb.append(",");
        sb.append("Current Light Turn Off Count");
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
        sb.append("Current Guard Guard Count");
        sb.append("\n");
        for (int i = 0; i < MAX_CURRENT_TICKS; i++){
            sb.append(compiledTotalWattageCount[i]);
            sb.append(",");
            sb.append(compiledCurrentPowerConsumption[i]);
            sb.append(",");
            sb.append(compiledTotalSocialInteractionper5min[i]);
            sb.append(",");
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
            sb.append(compiledCurrentAirconCount[i]);
            sb.append(",");
            sb.append(compiledCurrentLightCount[i]);
            sb.append(",");
            sb.append(compiledCurrentMonitorCount[i]);
            sb.append(",");
            sb.append(compiledTotalMonitorInteractionCount[i]);
            sb.append(",");
            sb.append(compiledCurrentAirconTurnOnCount[i]);
            sb.append(",");
            sb.append(compiledCurrentAirconTurnOffCount[i]);
            sb.append(",");
            sb.append(compiledCurrentAirconChangeTempInteraction[i]);
            sb.append(",");
            sb.append(compiledCurrentLightTurnOnCount[i]);
            sb.append(",");
            sb.append(compiledCurrentLightTurnOffCount[i]);
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

    public static float getWaterDispenserWattage() {
        return waterDispenserWattage;
    }

    public static float getWaterDispenserWattageInUse() {
        return waterDispenserWattageInUse;
    }

    public static float getWaterDispenserWattageActive() {
        return waterDispenserWattageActive;
    }

    public static float getFridgeWattage() {
        return fridgeWattage;
    }

    public static float getFridgeWattageInUse() {
        return fridgeWattageInUse;
    }

    public static float getFridgeWattageActive() {
        return fridgeWattageActive;
    }

    public static float getAirconWattage() {
        return airconWattage;
    }

    public static float getAirconWattageActive() {
        return airconWattageActive;
    }

    public static float getLightWattage() {
        return lightWattage;
    }

    public static float getMonitorWattage() {
        return monitorWattage;
    }

    public static float getCoffeeMakerWattageLow() {
        return coffeeMakerWattageLow;
    }

    public static float getCoffeeMakerWattageHigh() {
        return coffeeMakerWattageHigh;
    }

    public static float getTotalWattageCount(){
        return totalWattageCount;
    }

    public static double getGreenChance() {
        return greenChance;
    }

    public static double getNonGreenChance() {
        return nonGreenChance;
    }

    public static double getNeutralChance() {
        return neutralChance;
    }

    public static int getStudentNum(){
        return studentNum;
    }

    public static int getFacultyNum(){
        return facultyNum;
    }

    public static int getTeamNum(){
        return teamNum;
    }
    /***** SETTERS ******/
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public void setWaterDispenserWattage(float waterDispenserWattage) {
        Simulator.waterDispenserWattage = waterDispenserWattage;
    }

    public void setWaterDispenserWattageInUse(float waterDispenserWattageInUse) {
        Simulator.waterDispenserWattageInUse = waterDispenserWattageInUse;
    }

    public void setWaterDispenserWattageActive(float waterDispenserWattageActive) {
        Simulator.waterDispenserWattageActive = waterDispenserWattageActive;
    }

    public void setFridgeWattage(float fridgeWattage) {
        Simulator.fridgeWattage = fridgeWattage;
    }

    public void setFridgeWattageInUse(float fridgeWattageInUse) {
        Simulator.fridgeWattageInUse = fridgeWattageInUse;
    }

    public void setFridgeWattageActive(float fridgeWattageActive) {
        Simulator.fridgeWattageActive = fridgeWattageActive;
    }

    public static void setAirconWattage(float airconWattage) {
        Simulator.airconWattage = airconWattage;
    }

    public static void setAirconWattageActive(float airconWattageActive) {
        Simulator.airconWattageActive = airconWattageActive;
    }

    public static void setLightWattage(float lightWattage) {
        Simulator.lightWattage = lightWattage;
    }

    public static void setMonitorWattage(float monitorWattage) {
        Simulator.monitorWattage = monitorWattage;
    }

    public static void setCoffeeMakerWattageLow(float coffeeMakerWattageLow) {
        Simulator.coffeeMakerWattageLow = coffeeMakerWattageLow;
    }

    public static void setCoffeeMakerWattageHigh(float coffeeMakerWattageHigh) {
        Simulator.coffeeMakerWattageHigh = coffeeMakerWattageHigh;
    }

    public static void setTotalWattageCount(float totalWattageCount){
        Simulator.totalWattageCount = totalWattageCount;
    }

    public static void setGreenChance(double greenChance) {
        Simulator.greenChance = greenChance;
    }

    public static void setNonGreenChance(double nonGreenChance) {
        Simulator.nonGreenChance = nonGreenChance;
    }

    public static void setNeutralChance(double neutralChance) {
        Simulator.neutralChance = neutralChance;
    }

    public static void setStudentNum(int studentNum){
        Simulator.studentNum = studentNum;
    }

    public static void setFacultyNum(int facultyNum){
        Simulator.facultyNum = facultyNum;
    }

    public static void setTeamNum(int teamNum){
        Simulator.teamNum = teamNum;
    }
}