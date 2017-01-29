import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

class NXT {

    private static final int MAX_STEERING_ANGLE = 64;

    private int mMotorA;
    private int mMotorB;
    private int mMotorC;

    private BeepThread mBeepThread;

    private boolean mSound;

    private boolean mIsCalibrated;

    void start() {
        Motor.A.setAcceleration(1000);
        Motor.B.setAcceleration(600);

        Motor.C.setSpeed(20);
        Motor.C.setAcceleration(500);

        System.out.println("Waiting for connection");

        BTConnection connection = Bluetooth.waitForConnection();

        if (connection == null) {
            System.out.println("Connection failed");
            Button.ENTER.waitForPressAndRelease();
            System.exit(1);
        }

        new ReadThread(connection, this).start();

        mBeepThread = new BeepThread();
        mBeepThread.start();

        Motor.B.flt();

        System.out.println("Press ENTER to calibrate!");
        Button.ENTER.waitForPressAndRelease();

        Motor.B.resetTachoCount();

        mIsCalibrated = true;

        runForever();
    }

    private void runForever() {
        while (!Button.ESCAPE.isDown()) {
            Motor.A.setSpeed(Math.abs(mMotorA));

            if (mMotorA > 0) {
                Motor.A.forward();
            } else if (mMotorA < 0) {
                Motor.A.backward();
            }

            Motor.B.setSpeed(200);

            if (mMotorB != Integer.MAX_VALUE) {
                if (mMotorB > MAX_STEERING_ANGLE) {
                    mMotorB = MAX_STEERING_ANGLE;
                } else if (mMotorB < -MAX_STEERING_ANGLE) {
                    mMotorB = -MAX_STEERING_ANGLE;
                }

                //int angle = (int) Math.pow(1.10781128, mMotorB);

                Motor.B.rotateTo((int) (mMotorB * 2.1), true);

                mMotorB = Integer.MAX_VALUE;
            }

            if (mMotorC != 0) {
                Motor.C.rotate(mMotorC, false);
                mMotorC = 0;
            } else {
                Motor.C.stop();
            }

            if (mMotorA < -10) {
                if (mSound) {
                    mBeepThread.mBeep = true;
                }
            } else {
                mBeepThread.mBeep = false;
            }
        }
    }


    boolean isCalibrated() {
        return mIsCalibrated;
    }

    boolean isSound() {
        return mSound;
    }

    void setSound(boolean sound) {
        this.mSound = sound;
    }


    // ================================================= MOTORS ========================================================

    void setMotorA(int motorA) {
        this.mMotorA = motorA;
    }

    void setMotorB(int motorB) {
        this.mMotorB = motorB;
    }

    void setMotorC(int motorC) {
        this.mMotorC = motorC;
    }
}
