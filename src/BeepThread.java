import lejos.nxt.Sound;

public class BeepThread extends Thread {

    boolean mBeep;

    public void run() {
        while (true) {
            if (mBeep) {
                Sound.beep();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}