package multithreading.dispatcher;

public class Result {
    private final int[] results;

    public Result(int n) {
        results = new int[n];
    }

    public synchronized void setResult(int result, int index) {
        results[index] = result;
        notify();
    }

    public synchronized int[] getResults() {
        try {
            wait();
        } catch (InterruptedException t) {
            throw new RuntimeException(t);
        }
        return results;
    }
}
