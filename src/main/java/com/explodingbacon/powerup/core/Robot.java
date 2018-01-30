package com.explodingbacon.powerup.core;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.command.ArmCommand;
import com.explodingbacon.powerup.core.command.ClimberCommand;
import com.explodingbacon.powerup.core.command.DriveCommand;
import com.explodingbacon.powerup.core.command.IntakeCommand;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import com.explodingbacon.powerup.core.subsystems.ClimberSubsystem;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import com.explodingbacon.powerup.core.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.ArrayList;
import java.util.List;

public class Robot extends RobotCore {

    public static DriveSubsystem drive;
    public static ClimberSubsystem climber;
    public static ArmSubsystem arm;
    public static IntakeSubsystem intake;
    private OI oi;

    public Robot(IterativeRobot r) {
        super(r);
    }

    MotorGroup intakeTop, intakeBottom;

    @Override
    public void robotInit() {
        super.robotInit();

        oi = new OI();
        drive = new DriveSubsystem();
        arm = new ArmSubsystem();
        intake = new IntakeSubsystem();
        //climber = new ClimberSubsystem();

        //arm = new MotorGroup(new Motor(VictorSP.class, 2));
        //arm = new MotorGroup(new Motor(VictorSP.class, Map.ARM_A), new Motor(WPI_TalonSRX.class, Map.ARM_B));
        //arm.setInverts(false, false);

        //intakeTop = new MotorGroup(new Motor(VictorSP.class, Map.INTAKE_TOP));
        //intakeTop.setReversed(true);
        //intakeBottom = new MotorGroup(new Motor(VictorSP.class, Map.INTAKE_BOTTOM));

    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        OI.runCommand(new DriveCommand());
        OI.runCommand(new ClimberCommand());
        OI.runCommand(new IntakeCommand());
        OI.runCommand(new ArmCommand());
    }

    @Override
    public void teleopPeriodic() {

        /*if (OI.driver.leftTrigger.get()) {
            arm.setPower(.7); //.7
        } else if (OI.driver.rightTrigger.get()) {
            arm.setPower(-.7);
        } else {
            arm.setPower(0);
        }

        if (OI.driver.leftBumper.get()) {
            intakeTop.setPower(-1);
            intakeBottom.setPower(1);
        } else if (OI.driver.rightBumper.get()) {
            intakeTop.setPower(1);
            intakeBottom.setPower(-1);
        } else {
            intakeTop.setPower(0);
            intakeBottom.setPower(0);
        }*/
    }

    @Override
    public void testInit() {
        super.testInit();
        int mid = 0;
        for (Motor m : drive.rightDrive.getMotors()) {
            try {
                System.out.println(mid + " upcoming");
                drive.rightDriveEncoder.reset();
                Thread.sleep(2000);
                m.setPower(1);
                Thread.sleep(500);
                m.setPower(0);
                System.out.println(mid + ": " + drive.rightDriveEncoder.get());
                mid++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
