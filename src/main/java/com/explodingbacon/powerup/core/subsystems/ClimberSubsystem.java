package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.Map;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class ClimberSubsystem extends Subsystem {

    //How are we controlling how far up and down the climber goes?
    //Time, encoders?
    //User control? While pressed?

    public Motor climbMotorA = new Motor(VictorSP.class, Map.CLIMBER_A);
    public Motor climbMotorB = new Motor(VictorSP.class, Map.CLIMBER_B);

    public void climberOut(boolean a) {
        while (a) {
            climbMotorA.setPower(1.0);
            climbMotorB.setPower(-1.0);
        }
    }

    public void climberIn(boolean b) {
        while (b) {
            climbMotorA.setPower(-1.0);
            climbMotorB.setPower(1.0);
        }
    }

    public void climb(boolean boo) {

    }

    @Override
    public void enabledInit() {

    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void stop() {

    }

    @Override
    public List<Motor> getAllMotors() {
        return null;
    }
}
