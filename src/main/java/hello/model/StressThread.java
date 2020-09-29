package hello.model;

import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

/**
 * https://gist.github.com/astromechza/38ab4d097aecf12bcc372575eb01613c
 */
public class StressThread implements Runnable {
    private final Random rng;
    private final LongAdder calculationsPerformed;
    private boolean stopped;
    private double store;

    public StressThread(LongAdder calculationsPerformed) {
        this.calculationsPerformed = calculationsPerformed;
        this.stopped = false;
        this.rng = new Random();
        this.store = 1;
    }

    public void stop() {
        this.stopped = true;
    }

    @Override
    public void run() {
        while (!this.stopped) {
            double r = this.rng.nextFloat();
            double v = Math.sin(Math.cos(Math.sin(Math.cos(r))));
            this.store *= v;
            this.calculationsPerformed.add(1);
        }
    }
}
