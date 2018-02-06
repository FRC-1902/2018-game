package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.Encoder;
import com.explodingbacon.powerup.core.Map;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

import java.util.List;

public class IntakeSubsystem extends Subsystem {

    Motor intakeMotorA;
    Motor intakeMotorB;
    public Potentiometer test;

    public IntakeSubsystem() {
        intakeMotorA = new Motor(WPI_VictorSPX.class, Map.INTAKE_TOP);

        test = new AnalogPotentiometer(0);
    }

    public void intake(boolean intakeButton, boolean outtakeButton) {
        if (intakeButton) {
            intakeMotorA.setPower(1.0);
            intakeMotorB.setPower(-1.0);
        } else if (outtakeButton) {
            intakeMotorA.setPower(-1.0);
            intakeMotorB.setPower(1.0);
        } else {
            intakeMotorA.setPower(0);
            intakeMotorB.setPower(-0.3);
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
