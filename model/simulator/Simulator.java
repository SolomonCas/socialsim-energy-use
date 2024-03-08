package com.socialsim.model.simulator;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import com.socialsim.model.core.environment.office.Office;
public class Simulator {


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


    private Office office;
    private final AtomicBoolean running;
    private final SimulationTime time;
    private final Semaphore playSemaphore;

    public Simulator() {
        this.office = null;
        this.running = new AtomicBoolean(false);
        this.time = new SimulationTime(9, 0, 0);
        this.playSemaphore = new Semaphore(0);
        this.start();
    }


    public boolean isRunning() {
        return running.get();
    }

    private void start() {
        new Thread(() -> {
            final int speedAwarenessLimitMilliseconds = 10;

            while(true) {
                try {
                    playSemaphore.acquire();

                    while(this.isRunning()) {

                    }



                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

        }).start();
    }
}