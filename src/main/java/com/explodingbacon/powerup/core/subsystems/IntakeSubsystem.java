package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.AnalogSensor;
import com.explodingbacon.powerup.core.Map;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.*;
import java.util.List;

public class IntakeSubsystem extends Subsystem {

    Motor intakeMotorA;
    Motor intakeMotorB;

    boolean passiveIntake = false;

    public IntakeSubsystem() {
        Class intakeMotorClass = Robot.MAIN_ROBOT ? VictorSP.class : WPI_VictorSPX.class;
        intakeMotorA = new Motor(intakeMotorClass, Map.INTAKE_TOP);
        intakeMotorB = new Motor(intakeMotorClass, Map.INTAKE_BOTTOM);
        //setPassiveIntake(true);
    }

    public void intake(boolean intakeButton, boolean outtakeButton) {
        if (intakeButton) {
            setIntake(1, true); //1
        } else if (outtakeButton) {
            double pow = Robot.arm.floor ? 1 : 0.7; //formerly always 1.0
            setIntake(pow, false);
        } else {
            boolean moving = Math.abs(Robot.arm.armPID.getCurrentError()) > 180;
            //Log.d("Moving: " + moving);
            setIntake(moving ? 0.2 : 0, true);
        }
    }

    public void setPassiveIntake(boolean on) {
        passiveIntake = on;
    }

    public void setIntake(double pow, boolean in) {
        double dir = in ? 1 : -1;

        double topChange = 1;
        double bottomChange = 1;
        Log.d("Front: " + Robot.arm.front);
        if (Robot.arm.front) {
            topChange = in && pow != 0.2 ? .5 : 1;
        } else {
            bottomChange = in && pow != 0.2 ? .5 : 1;
        }

        intakeMotorA.setPower(pow * dir * topChange);
        intakeMotorB.setPower(-pow * dir * bottomChange);
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
