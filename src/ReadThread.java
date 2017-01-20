import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReadThread extends Thread {

    private NXT nxt;

    public ReadThread(NXT nxt) {
        this.nxt = nxt;
    }

    public void run() {
        System.out.println("Start");

        BTConnection conn = Bluetooth.waitForConnection(Integer.MAX_VALUE, 0);

        if (conn == null) {
            System.out.println("Connection failed");
        } else {
            System.out.println("Connected");

            DataInputStream inputStream = conn.openDataInputStream();
            DataOutputStream outputStream = conn.openDataOutputStream();

            Motor.B.flt();
            System.out.println("Press enter to calibrate!");
            Button.ENTER.waitForPressAndRelease();
            nxt.calibrateSteering();

            while (!Button.ESCAPE.isPressed()) {
                try {
                    if (inputStream.available() > 0) {
                        String string = inputStream.readUTF();
                        System.out.println(string);
                        try {
                            if (string.equals("beep")) {
                                Sound.beep();
                            } else if (string.equals("twobeeps")) {
                                Sound.twoBeeps();
                            } else if (string.equals("halt")) {
                                System.exit(0);
                                break;
                            } else if (string.equals("sound")) {
                                nxt.setSound(!nxt.isSound());

                                if (nxt.isSound()) {
                                    System.out.println("Sound on");
                                } else {
                                    System.out.println("Sound off");
                                }
                            } else if (string.equals("cameraUp")) {
                                nxt.setMotorC(-10);
                            } else if (string.equals("cameraDown")) {
                                nxt.setMotorC(10);
                            } else if (string.equals("mode")) {
                                nxt.mode *= -1;
                            } else {
                                nxt.setMotorA(Integer.parseInt(string.substring(0, string.indexOf(";"))));

                                string = string.substring(string.indexOf(";") + 1);

                                nxt.setMotorB(Integer.parseInt(string));
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.println("Invalid string: " + string);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("ReadException");
                    System.exit(1);
                }

                if (nxt.isSound()) {
                    Sound.setVolume(Sound.VOL_MAX);
                } else {
                    Sound.setVolume(0);
                }
            }
        }
    }
}