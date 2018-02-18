package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
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
    }

    public void intake(boolean intakeButton, boolean outtakeButton) {
        if (intakeButton) {
            setIntake(1, true);
        } else if (outtakeButton) {
            setIntake(0.7, false); //formerly 1.0
        } else {
            setIntake(passiveIntake ? 0.2 : 0, true);
        }
    }

    public void setPassiveIntake(boolean on) {
        passiveIntake = on;
    }

    private void setIntake(double pow, boolean in) {
        double dir = in ? 1 : -1;
        intakeMotorA.setPower(pow * dir);
        intakeMotorB.setPower(-pow * dir);
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
