package dining_phi;

public class MainSemaphore {
    public static void main(String[] args) {
        TableSemaphore table = new TableSemaphore(5);

        new Philosopher(100, 0, table);
        new Philosopher(100, 1, table);
        new Philosopher(100, 2, table);
        new Philosopher(100, 3, table);
        new Philosopher(100, 4, table);
    }
}
