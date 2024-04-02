package com.socialsim.model.simulator;

import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.socialsim.controller.Main;
import com.socialsim.controller.controls.ScreenController;
import com.socialsim.model.core.environment.Environment;
import com.socialsim.model.core.agent.Agent;

public class Simulator {


    // VARIABLES
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

    private Environment environment;
    private final AtomicBoolean running;
    private final SimulationTime time;
    private final Semaphore playSemaphore;




    // CONSTRUCTOR(S)
    public Simulator() {
        this.environment = null;
        this.running = new AtomicBoolean(false);
        this.time = new SimulationTime(9, 0, 0);
        this.playSemaphore = new Semaphore(0);
        this.start();
    }




    // METHODS
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

    public static void updateAgentsInEnvironment(Environment environment, long currentTick) throws InterruptedException {
        moveAll(environment, currentTick);
        // Insert code
    }

    private static void moveAll(Environment environment, long currentTick) {
        // Insert code
    }

    private void spawnAgent(Environment environment, long currentTick) {
        // Insert code
    }

    public void spawnInitialAgents(Environment environment) {
        // Insert code
    }

    public void replenishStaticVars() {
        // Insert code
    }

    public void resetToDefaultConfiguration(Environment environment) {
        this.environment = environment;
        replenishStaticVars();
        Agent.clearAgentCounts();
        this.time.reset();
        this.running.set(false);
    }



    // GETTERS
    public Environment getEnvironment() {
        return environment;
    }

    public Semaphore getPlaySemaphore() {
        return playSemaphore;
    }

    public boolean isRunning() {
        return running.get();
    }







    // SETTERS

    public void setRunning(boolean running) {
        this.running.set(running);
    }


}