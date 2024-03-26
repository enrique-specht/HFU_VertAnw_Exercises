package multithreading.eieruhr;

public class EieruhrThread extends Thread {
    private final int time;
    private final String text;

    public EieruhrThread(int time, String text) {
        this.time = time;
        this.text = text;
    }

    @Override
    public void run() {
        schlafen(time);
        System.out.println(text);
    }

    public static void schlafen(int m) {
        for (int i = m; i >= 0; i--) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException t) {
                throw new RuntimeException(t);
            }
        }
    }
}
