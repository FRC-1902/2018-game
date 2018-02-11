package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.AnalogSensor;
import com.explodingbacon.powerup.core.Map;
import edu.wpi.first.wpilibj.*;
import java.util.List;

public class IntakeSubsystem extends Subsystem {

    Motor intakeMotorA;
    Motor intakeMotorB;

    public IntakeSubsystem() {
        intakeMotorA = new Motor(WPI_VictorSPX.class, Map.INTAKE_TOP);
        intakeMotorB = new Motor(WPI_VictorSPX.class, Map.INTAKE_BOTTOM);
    }

    public void intake(boolean intakeButton, boolean outtakeButton) {
        if (intakeButton) {
            setIntake(1, true);
            //intakeMotorA.setPower(1.0);
            //intakeMotorB.setPower(-1.0);
        } else if (outtakeButton) {
            setIntake(1, false);
            //intakeMotorA.setPower(-1.0);
            //intakeMotorB.setPower(1.0);
        } else {
            //setIntake(0, true);
            setIntake(0.2, true);

            //intakeMotorA.setPower(0);//3
            //intakeMotorB.setPower(0); //-3
        }
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
