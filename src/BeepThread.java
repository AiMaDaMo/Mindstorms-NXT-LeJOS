import lejos.nxt.Sound;

public class BeepThread extends Thread {
    boolean beep;

    public void run() {
        while (true) {
            if (beep) {
                Sound.beep();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}