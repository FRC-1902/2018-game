package com.explodingbacon.powerup.core.networktest;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.networking.Communicator;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeoColor;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.HorizontalArrow;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.HorizontalSlider;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.Pad;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.Rotary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Server code (robot-side) for communication between a robot and a client. Currently has some robot-specific code in
 * it (this will eventually be separated from this server "template").
 *
 * @author Ryan Shavell
 * @version 2016.10.3
 */
public class Server extends Communicator {

    public static int PORT = 5800;

    private ServerSocket server;
    private Socket client;

    public Server() {
        super();
        Utils.runInOwnThread(() -> {
            try {
                server = new ServerSocket(PORT);
                client = server.accept();

                PrintWriter o = new PrintWriter(client.getOutputStream(), true);
                BufferedReader i = new BufferedReader(new InputStreamReader(client.getInputStream()));

                initialize(o, i);
                Log.i("Server init");

            } catch (Exception e) {
                Log.e("Server constructor exception!");
                e.printStackTrace();
            }
        });
    }

    boolean first = true;

    public Pad[][] pads;

    @Override
    public void onReceiveMessage(String message) {
        Log.i(String.format("Server got message \"%s\".", message));
        if (first) {
            pads = new Pad[4][4];
            int padNum = 48;
            for (int y=0;y<4;y++) {
                for (int x=0;x<4;x++) {
                    pads[x][y] = new Pad(padNum);

                    //System.out.println("Pad " + padNum);
                    padNum++;
                }
                padNum -=8;
            }

            HorizontalSlider[] sliders = {new HorizontalSlider(0),
            new HorizontalSlider(1),
            new HorizontalSlider(2),
            new HorizontalSlider(3)};

            boolean yes = true;
            for (HorizontalSlider a : sliders) {
                if (yes) a.setLights(1);
                else a.setLights(0);
                yes = ! yes;
                a.onNotePress(data -> {
                    a.setLights(a.getLocation());
                });
            }

            HashMap<Pad, Boolean> states = new HashMap<>();

            /*
            for (Pad[] array : pads) {
                for (Pad p : array) {
                    p.onNotePress(data -> {
                        p.setColor(QuNeoColor.ORANGE, 1);
                        //System.out.println("pressure: " + pads[1][1].getPressure());
                    });
                    p.onNoteRelease(data -> {
                        states.putIfAbsent(p, false);
                        p.setColor(states.get(p) ? QuNeoColor.GREEN : QuNeoColor.RED, 1);
                        states.put(p, !states.get(p));
                    });
                }
            }*/

            Rotary r = new Rotary(5);
            r.onControlChange((cc, data) -> r.setLED(r.getDirection()));

            new HorizontalArrow(18).setColor(true, 1);

            new HorizontalArrow(19).setColor(true, 1);

            first = false;

            for (Pad[] padArray : pads) {
                for (Pad p : padArray) {
                    p.setGridOff();
                    p.setColorOff();
                }
            }
/*
            Robot.forward = pads[2][0];
            Robot.forward.onNotePress(data -> {
                System.out.println("pressed");
            });
            Robot.forward.setColor(QuNeoColor.RED, 1);

            Robot.left = pads[1][1];
            Robot.left.setColor(QuNeoColor.ORANGE, 1);

            Robot.right = pads[3][1];
            Robot.right.setColor(QuNeoColor.ORANGE, 1);

            Robot.back = pads[2][1];
            Robot.back.setColor(QuNeoColor.GREEN, 1);*/

            //AnimationHandler.doAnimation(pads, .4, true, "one.png", "nine.png", "zero.png", "two.png");
        }

        if (message.startsWith("quneo:")) {
            message = message.replace("quneo:", "");
            if (Robot.quneo != null) Robot.quneo.handlePacket(message);
        }/* else if (message.startsWith("keyboard:")) { //example message: "keyboard:72:true"
            message = message.replace("keyboard:", "");
            String[] data = message.split(":");
            int keycode = Integer.parseInt(data[0]);
            KeyboardButton b = KeyboardButton.getButton(keycode);
            if (b != null) {
                b.set(Boolean.parseBoolean(data[1]));
            }
        }*/
    }
}
