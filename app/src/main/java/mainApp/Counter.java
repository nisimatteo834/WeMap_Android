package mainApp;

/**
 * Created by Stefano on 09/01/2018.
 */

public class Counter {
    static int cnt = 1;
    static void Increment() {
        cnt++;
    }
    static void Decrease() {
        cnt--;
    }
    static int Total () {
        return cnt;
    }
    static void setCnt (int counterSetter) {
        cnt = counterSetter;
    }
}