package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;

public class LoopingAnalogSensor extends AnalogSensor {

    Double lastVal = null;
    double offset = 0;
    double scalar = 1;

    public LoopingAnalogSensor(int channel) {
        super(channel);
    }

    @Override
    public double getForPID() {
        double currVal = input.getValue();
        if (lastVal == null) lastVal = currVal;

        if (lastVal > 3000 && currVal < 1000) {
            offset += 4000;
        } else if (lastVal < 1000 && currVal > 3000) {
            offset -= 4000;
        }

        lastVal = currVal;

        //Log.i("RawVal: " + currVal);

        double returnVal = (currVal + offset) * scalar;

        //Log.i("Val: " + returnVal);
        //Log.i("Offset: " + offset);

        return (currVal + offset) * scalar;
    }

    public double getRaw() {
        return input.getValue();
    }

    public void setScalar(double d) {
        scalar = d;
    }

    @Override
    public void reset() {
        offset = -input.getValue();
    }
}
