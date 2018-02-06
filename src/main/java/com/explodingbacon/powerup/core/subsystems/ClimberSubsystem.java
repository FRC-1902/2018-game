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

    public Motor climbMotorA;
    public Motor climbMotorB;

    public ClimberSubsystem() {
        climbMotorA = new Motor(VictorSP.class, Map.CLIMBER_A);
        climbMotorB = new Motor(VictorSP.class, Map.CLIMBER_B);
    }

    public void climb(boolean a, boolean b) {
        if (a) {
            climbMotorA.setPower(1.0);
            climbMotorB.setPower(-1.0);
        } else if (b){
            climbMotorA.setPower(-1.0);
            climbMotorB.setPower(1.0);
        } else {
            climbMotorA.setPower(0);
            climbMotorB.setPower(0);
        }
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
