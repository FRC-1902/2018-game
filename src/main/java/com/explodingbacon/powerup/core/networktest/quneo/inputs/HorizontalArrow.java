package com.explodingbacon.powerup.core.networktest.quneo.inputs;

import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeo;

/**
 * A class that represents any Horizontal Arrow on the QuNeo.
 *
 * @author Ryan Shavell
 * @version 2016.10.3
 */
public class HorizontalArrow extends QuNeoInput {

    private int pressure = 0;
    private final int cc_pressure;

    public HorizontalArrow(int note) {
        super(note);
        inputType = InputType.HORIZONTAL_ARROW;

        cc_pressure = 71 + (note - 11);

        subscribeToCC(cc_pressure);
    }

    @Override
    public void updateData(int cc, int data) {
        if (cc == cc_pressure) {
            pressure = data;
        }
        //Log.d("Arrow pressure: " + pressure);
    }

    /**
     * Gets the current pressure on this HorizontalArrow. Ranges 0-1.
     *
     * @return The current pressure on this HorizontalArrow.
     */
    public double getPressure() {
        return QuNeo.toRobotScale(pressure, 63);
    }

    /**
     * Sets if the this HorizontalArrow's LED should be on.
     *
     * @param on If the this HorizontalArrow's LED should be on.
     * @param strength How strong the LED should be. Ranges 0-1.
     */
    public void setColor(boolean on, double strength) {
        int colorCC = 36 + (note - 11);
        Robot.quneo.setColor(0, colorCC, on, QuNeo.toScale(strength, 127));
    }
}