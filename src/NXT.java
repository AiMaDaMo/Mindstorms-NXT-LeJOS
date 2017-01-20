import lejos.nxt.Button;
import lejos.nxt.Motor;

public class NXT {
    private int motorA;
    private int motorB;
    private int motorC;

    public long start;
    public int mode = 1;

    private BeepThread beepThread;

    boolean sound;

    public void init() {
        Motor.C.setSpeed(20);

        beepThread = new BeepThread();
        beepThread.start();

        Motor.A.setAcceleration(2000);
        Motor.B.setAcceleration(2000);
        Motor.C.setAcceleration(2000);

        new ReadThread(this).start();

        run();
    }

    public void run() {
        while (!Button.ESCAPE.isPressed()) {
            Motor.A.setSpeed(Math.abs(motorA));

            if (motorA > 0) {
                Motor.A.backward();
            } else if (motorA < 0) {
                Motor.A.forward();
            }

            Motor.B.setSpeed(150);
            if (motorB != Integer.MAX_VALUE) {
                int angle = (motorB - Motor.B.getTachoCount()) > 80 ? 80 : (motorB - Motor.B.getTachoCount()) < -80
                        ? -80 : (motorB - Motor.B.getTachoCount());
                Motor.B.rotate(angle, true);
                motorB = Integer.MAX_VALUE;
            }

            if (motorC != 0) {
                Motor.C.rotate(motorC, false);
                motorC = 0;
            } else {
                Motor.C.stop();
            }

            if (motorA < -10 || motorB < -10) {
                if (sound) beepThread.beep = true;
            } else {
                beepThread.beep = false;
            }
        }
    }

    public void calibrateSteering() {
        Motor.B.stop();
        Motor.B.resetTachoCount();
    }

    public int getMotorA() {
        return motorA;
    }

    public void setMotorA(int motorA) {
        this.motorA = motorA;
    }

    public int getMotorB() {
        return motorB;
    }

    public void setMotorB(int motorB) {
        this.motorB = motorB;
    }

    public int getMotorC() {
        return motorC;
    }

    public void setMotorC(int motorC) {
        this.motorC = motorC;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
}
