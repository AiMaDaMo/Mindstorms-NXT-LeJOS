import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadThread extends Thread {

    private NXT mNxt;
    private BTConnection mConnection;

    ReadThread(BTConnection connection, NXT nxt) {
        mNxt = nxt;
        mConnection = connection;
    }

    public void run() {
        DataInputStream inputStream = mConnection.openDataInputStream();

        while (!Button.ESCAPE.isDown()) {
            try {
                if (inputStream.available() > 0) {
                    String string = inputStream.readUTF();

                    if (!mNxt.isCalibrated()) {
                        continue;
                    }

                    System.out.println(string);

                    if (string.equals("b")) {
                        Sound.beep();
                    } else if (string.equals("tb")) {
                        Sound.twoBeeps();
                    } else if (string.equals("h")) {
                        System.exit(0);
                        break;
                    } else if (string.equals("s")) {
                        mNxt.setSound(!mNxt.isSound());

                        if (mNxt.isSound()) {
                            Sound.setVolume(Sound.VOL_MAX);
                            System.out.println("Sound on");
                        } else {
                            Sound.setVolume(0);
                            System.out.println("Sound off");
                        }
                    } else if (string.equals("cu")) {
                        mNxt.setMotorC(-10);
                    } else if (string.equals("cd")) {
                        mNxt.setMotorC(10);
                    } else {
                        try {
                            mNxt.setMotorA(Integer.parseInt(string.substring(0, string.indexOf(";"))));

                            string = string.substring(string.indexOf(";") + 1);

                            mNxt.setMotorB(Integer.parseInt(string));
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.println("Invalid string: " + string);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("ReadException");
                Button.ENTER.waitForPressAndRelease();
                System.exit(1);
            }
        }
    }
}