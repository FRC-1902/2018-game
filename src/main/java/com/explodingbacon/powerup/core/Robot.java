package com.explodingbacon.powerup.core;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.powerup.core.command.*;
import com.explodingbacon.powerup.core.networktest.Server;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeo;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeoColor;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.Pad;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import com.explodingbacon.powerup.core.subsystems.ClimberSubsystem;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import com.explodingbacon.powerup.core.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.ArrayList;
import java.util.List;

public class Robot extends RobotCore {

    public static DriveSubsystem drive;
    public static ClimberSubsystem climber;
    public static ArmSubsystem arm;
    public static IntakeSubsystem intake;

    public static QuNeo quneo;
    public static Server server;
    private OI oi;

    Compressor compressor;

    public static Pad forward, back, left, right;

    public Robot(IterativeRobot r) {
        super(r);
    }


    @Override
    public void robotInit() {
        super.robotInit();

        compressor = new Compressor();

        compressor.setClosedLoopControl(false);

        oi = new OI();
        drive = new DriveSubsystem();
        intake = new IntakeSubsystem();
        //arm = new ArmSubsystem();

        //quneo = new QuNeo();
        //server = new Server();
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
        //OI.runCommand(new QuNeoDrive());
        OI.runCommand(new DriveCommand());
        OI.runCommand(new IntakeCommand());
        //OI.runCommand(new ClimberCommand());
    }

    @Override
    public void teleopPeriodic() {
        System.out.println("pot: " + intake.test.get());
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
        try {
            for (Motor m : Robot.drive.leftDrive.getMotors()) {
                System.out.println("about to move: ");
                Thread.sleep(3000);
                m.setPower(0.4);
                Thread.sleep(500);
                m.setPower(0);
            }
            for (Motor m : Robot.drive.rightDrive.getMotors()) {
                System.out.println("about to move: ");
                Thread.sleep(3000);
                m.setPower(0.4);
                Thread.sleep(500);
                m.setPower(0);
            }
            /*
            List<Motor> motors = new ArrayList<>();
            for (int i=0; i<9; i++) {
                System.out.println("about to move: " + i);
                Thread.sleep(3000);
                Motor m = new Motor(WPI_VictorSPX.class, i);
                motors.add(m);
                m.setPower(0.4);
                Thread.sleep(500);
                m.setPower(0);
            }*/
            /*
            for (int i=0; i<5; i++) {
                System.out.println("about to move: " + i);
                Thread.sleep(3000);
                Motor m = new Motor(WPI_TalonSRX.class, i);
                motors.add(m);
                m.setPower(0.4);
                Thread.sleep(500);
                m.setPower(0);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
