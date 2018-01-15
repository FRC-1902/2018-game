package com.explodingbacon.powerup.core;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.controllers.LogitechController;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.command.DriveCommand;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends RobotCore {

    public static DriveSubsystem drive;
    private OI oi;

    public Robot(IterativeRobot r) {
        super(r);
    }

    MotorGroup arm, intakeTop, intakeBottom;

    @Override
    public void robotInit() {
        super.robotInit();

        oi = new OI();
        drive = new DriveSubsystem();

        arm = new MotorGroup(new Motor(WPI_TalonSRX.class, TestMap.ARM_1), new Motor(VictorSP.class, TestMap.ARM_2));
        arm.setReversed(true);

        intakeTop = new MotorGroup(new Motor(VictorSP.class, TestMap.INTAKE_TOP));
        intakeBottom = new MotorGroup(new Motor(VictorSP.class, TestMap.INTAKE_BOTTOM));
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        OI.runCommand(new DriveCommand());
    }

    @Override
    public void teleopPeriodic() {

        if (OI.driver.leftTrigger.get()) {
            arm.setPower(.7);
        } else if (OI.driver.rightTrigger.get()) {
            arm.setPower(-.7);
        } else {
            arm.setPower(0);
        }

        if (OI.driver.rightBumper.get()) {
            intakeTop.setPower(-1);
            intakeBottom.setPower(1);
        } else if (OI.driver.leftBumper.get()) {
            intakeTop.setPower(1);
            intakeBottom.setPower(-1);
        } else {
            intakeTop.setPower(0);
            intakeBottom.setPower(0);
        }
    }

    @Override
    public void testInit() {
        super.testInit();
        try {
             for (Motor m : drive.leftDrive.getMotors()) {
                 System.out.println("Left");
                 m.setPower(0.5);
                 Thread.sleep(500);
                 m.setPower(0);
                 Thread.sleep(250);
             }
            for (Motor m : drive.rightDrive.getMotors()) {
                 System.out.println("Right");
                m.setPower(0.5);
                Thread.sleep(500);
                m.setPower(0);
                Thread.sleep(250);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
