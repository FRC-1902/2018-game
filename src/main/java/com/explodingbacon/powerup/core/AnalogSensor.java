package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.framework.PIDSource;
import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogSensor implements PIDSource {

    private AnalogInput input;

    public AnalogSensor(int channel) {
        input = new AnalogInput(channel);
    }

    @Override
    public double getForPID() {
        return input.getValue();
    }

    @Override
    public void reset() {

    }
}
