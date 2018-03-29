package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.utils.Utils;
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
        if (Robot.MAIN_ROBOT) {
            intakeMotorA.setReversed(false);
            intakeMotorB.setReversed(false);
        }
        else {
            intakeMotorA.setReversed(true);
            intakeMotorB.setReversed(true);
        }
        //setPassiveIntake(true);
    }

    public void setIntake(double pow, boolean in) {
        setIntake(pow, pow, in);
    }

    public void setIntake(double powTop, double powBot, boolean in) {
        double dir = in ? 1 : -1;
        boolean front = Robot.arm.front;
        if (Robot.MAIN_ROBOT) front = !front;
        intakeMotorA.setPower((front ? powTop : powBot) * dir);
        intakeMotorB.setPower(-1 * (!front ? powTop : powBot) * dir);
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
