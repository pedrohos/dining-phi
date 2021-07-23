package dining_phi;

public class Philosopher implements Runnable {
    private int actionTime;
    private TableSemaphore tableSemaphore;
    private int id;

    public Philosopher(int actionTime, int id, TableSemaphore tableSemaphore) {
        this.actionTime = actionTime; // Time the philosopher eating and thinking.
        this.tableSemaphore = tableSemaphore;
        this.id = id;
        (new Thread( this, "Philosopher")).start();
    }

    private void think() {
        try {
            Thread.sleep(this.actionTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Philosopher %d: I am thinking", this.id));
    }

    private void eat() {
        try {
            Thread.sleep(this.actionTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Philosopher %d: It's lunch time!!", this.id));
    }

    // Philosopher infinite routine
    @Override
    public void run() {
        while (true) {
            think();
            this.tableSemaphore.take_forks(id);
            eat();
            this.tableSemaphore.release_forks(id);
        }
    }
}
