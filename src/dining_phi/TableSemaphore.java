package dining_phi;

import java.util.SortedSet;
import java.util.concurrent.Semaphore;

public class TableSemaphore {
    private static final int THINKING = 0;
    private static final int HUNGRY = 1;
    private static final int EATING = 2;

    private int nPhilosophers;

    // Keep track of the philosophers' states
    private Integer[] states;

    // Semaphore that controls the access to the critical section
    private Semaphore mutex = new Semaphore(1);
    // Semaphore that stores the state of each philosopher's semaphore
    private Semaphore[] philosopherSems;

    // Initialize the philosophers' states and sempaphores correctly
    public TableSemaphore(int nPhilosophers) {
        this.nPhilosophers = nPhilosophers;
        this.states = new Integer[nPhilosophers];
        this.philosopherSems = new Semaphore[nPhilosophers];

        for (int i = 0; i < nPhilosophers; i++) {
            states[i] = THINKING;
            philosopherSems[i] = new Semaphore(0);
        }
    }

    // Critical section which the philosopher takes two forks near him if possible
    public void take_forks(int philosopherId) {
        try {
            this.mutex.acquire(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        states[philosopherId] = HUNGRY;
        test(philosopherId);

        this.mutex.release(1);

        try {
            philosopherSems[philosopherId].acquire(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        System.out.println(String.format("Philosopher %d took the forks.", philosopherId));
    }

    // Critical section which the philosopher releases two forks near him so other philosophers can eat
    // It also "wakes up" the philosophers right next to him if they can eat (in the test method)
    public void release_forks(int philosopherId) {
        int leftId = (nPhilosophers + philosopherId + 1) % nPhilosophers;
        int rightId = (philosopherId + 1) % nPhilosophers;

        try {
            mutex.acquire(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        states[philosopherId] = THINKING;
        test(leftId);
        test(rightId);

        mutex.release(1);

        System.out.println(String.format("Philosopher %d released the forks.", philosopherId));
    }

    // Sets the state of the philosopher to EATING if his neighbours are not eating and if he is in the HUNGRY state.
    void test(int philosopherId) {
        int leftId = (nPhilosophers + philosopherId - 1) % nPhilosophers;
        int rightId = (philosopherId + 1) % nPhilosophers;

        if (states[philosopherId] == HUNGRY && states[leftId] != EATING && states[rightId] != EATING) {
            states[philosopherId] = EATING;

            philosopherSems[philosopherId].release(1);
        }
    }
}
