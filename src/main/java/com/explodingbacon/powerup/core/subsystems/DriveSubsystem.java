package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.Map;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class DriveSubsystem extends Subsystem {

    public MotorGroup leftDrive, rightDrive;

    public DriveSubsystem() {
        rightDrive = new MotorGroup(new Motor(VictorSP.class, Map.RIGHT_DRIVE_A), new Motor(VictorSP.class, Map.RIGHT_DRIVE_B),
                new Motor(VictorSP.class, Map.RIGHT_DRIVE_C));

        rightDrive.setReversed(true);

        leftDrive = new MotorGroup(new Motor(VictorSP.class, Map.LEFT_DRIVE_A), new Motor(VictorSP.class, Map.LEFT_DRIVE_B),
                new Motor(VictorSP.class, Map.LEFT_DRIVE_C));
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
