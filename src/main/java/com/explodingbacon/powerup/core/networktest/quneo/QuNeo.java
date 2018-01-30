package com.explodingbacon.powerup.core.networktest.quneo;

import com.explodingbacon.bcnlib.midi.MidiAPI;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.QuNeoInput;

/**
 * A class for communicating with and controlling a QuNeo over a network.
 *
 * @author Ryan Shavell
 * @version 2016.10.1
 */
public class QuNeo {

    /**
     * Subscribes the server to updates on the specified notes.
     *
     * @param t The type of event that is being tracked.
     * @param channel The channel on which the events are happening.
     * @param notes The notes that are launching the events.
     */
    public void subscribeTo(Type t, int channel, int...notes) {
        String noteString = "";
        for (int i : notes) noteString += ":" + i;
       Robot.server.sendMessage("quneo:subscribe:" + t.getInt() + noteString);
    }

    /**
     * Handles a QuNeo-related packet that has been sent by the client.
     *
     * @param message The QuNeo-related packet.
     */
    public void handlePacket(String message) {
        if (message.startsWith("update:")) {
            //Log.d("QuNeo.handlePacket()");
            message = message.replaceFirst("update:", "");
            String[] data = message.split(":");
            int type = Integer.parseInt(data[0]);
            int note = Integer.parseInt(data[1]);
            int updateData = Integer.parseInt(data[2]);
            int cc = -1;
            if (type == Type.CONTROL_CHANGE.getInt()) {
                cc = note;
                QuNeoInput possibleInput = QuNeoInput.getInputByCC(cc);
                if (possibleInput != null) {
                    note = possibleInput.getNote();
                } else {
                    //Log.e("Received packet for CC \"" + cc + "\", but there is no QuNeoInput that listens to it!");
                }
            }
            //Log.d(QuNeoInput.quNeoInputs.size() + " size");
            QuNeoInput qInput = QuNeoInput.getInput(note);
            if (qInput != null) {
                if (type == MidiAPI.NOTE_ON) {
                    qInput.triggerNotePress(updateData);
                } else if (type == MidiAPI.NOTE_OFF) {
                    qInput.triggerNoteRelease(updateData);
                } else if (type == MidiAPI.CONTROL_CHANGE) {
                    qInput.triggerControlChange(cc, updateData);
                }
            }
        }
    }

    public void setColor(int channel, int colorCC, boolean on, int strength) {
        Robot.server.sendMessage("quneo:setlight:" + channel + ":" + colorCC + ":" + on + ":" + strength);
    }

    public void setCC(int channel, int note, int data) {
        //Log.d("Server set cc");
        Robot.server.sendMessage("quneo:setcc:" + channel + ":" + note + ":" + data);
    }

    public enum Type {
        NOTE_ON(MidiAPI.NOTE_ON),
        NOTE_OFF(MidiAPI.NOTE_OFF),
        CONTROL_CHANGE(MidiAPI.CONTROL_CHANGE);

        int t;

        Type(int i) {
            t = i;
        }

        public int getInt() {
            return t;
        }

        public String getString() {
            return this.toString().toLowerCase();
        }
    }

    /**
     * Converts an int to a 0-1 scale, like WPILib and BCNLib uses.
     *
     * @param i The int to be converted.
     * @param oldMax The maximum number on i's scale. (i.e. 127 in the case of a range of 0-127)
     * @return i converted to a 0-1 scale.
     */
    public static double toRobotScale(int i, int oldMax) {
        double d = (i * 1.0) / (oldMax * 1.0);
        d = Utils.roundToDecimals(d, 5);
        return d;
    }

    /**
     * Converts a robot scale double (range 0-1) to a 0-max scale.
     *
     * @param i The robot scale double.
     * @param max The maximum number of the new scale. (i.e. 127 in the case of a range of 0-127)
     * @return i converted to a 0-max scale.
     */
    public static int toScale(double i, int max) {
        return ((Double)Math.floor((i * max))).intValue();
    }
}
