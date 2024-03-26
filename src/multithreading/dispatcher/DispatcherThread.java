package multithreading.dispatcher;

public class DispatcherThread extends Thread {
    private final F f;
    private final int x;
    private final Result result;

    public DispatcherThread(F f, int x, Result result) {
        this.f = f;
        this.x = x;
        this.result = result;
    }

    @Override
    public void run() {
        result.setResult(f.f(x), x);
    }
}
