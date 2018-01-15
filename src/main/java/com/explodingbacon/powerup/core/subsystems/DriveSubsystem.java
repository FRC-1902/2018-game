package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.TestMap;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class DriveSubsystem extends Subsystem {

    public MotorGroup leftDrive, rightDrive;

    public DriveSubsystem() {
        rightDrive = new MotorGroup(new Motor(WPI_TalonSRX.class, TestMap.RIGHT_FRONT), new Motor(WPI_TalonSRX.class, TestMap.RIGHT_BACK));
        rightDrive.setInverts(false, true);

        leftDrive = new MotorGroup(new Motor(VictorSP.class, TestMap.LEFT_FRONT), new Motor(VictorSP.class, TestMap.LEFT_BACK));
        leftDrive.setReversed(true);
    }

    public void tankDrive(double left, double right) {
        leftDrive.setPower(left);
        rightDrive.setPower(right);
    }

    @Override
    public void enabledInit() {}

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
