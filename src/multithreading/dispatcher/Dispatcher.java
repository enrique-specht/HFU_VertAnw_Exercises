package multithreading.dispatcher;

import java.util.Arrays;

public class Dispatcher {
    public static void main(String[] args) {
        int[] yArray = execute(x -> 3*x+1, 200);
        System.out.println(Arrays.toString(yArray));
    }

    public static int[] execute(F f, int n) {
        Result result = new Result(n);

        for (int i = 0; i < n; i++) {
            new DispatcherThread(f, i, result).start();
        }

        return result.getResults();
    }
}
