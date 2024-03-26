package multithreading.eieruhr;

public class Eieruhr {
    public static void main(String[] args) {
        eieruhr(8, "Eieruhr 1 abgelaufen");
        eieruhr(4, "Eieruhr 2 abgelaufen");
        eieruhr(6, "Eieruhr 3 abgelaufen");
        EieruhrThread.schlafen(10);
    }
    public static void eieruhr(int time, String text) {
        EieruhrThread eieruhrThread = new EieruhrThread(time, text);
        eieruhrThread.start();
    }
}
