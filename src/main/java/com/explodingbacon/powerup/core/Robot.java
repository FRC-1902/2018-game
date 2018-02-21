/*
 _______  ___   _______  __   __  _______  ___
|       ||   | |       ||  |_|  ||       ||   |
|    _  ||   | |    ___||       ||    ___||   |
|   |_| ||   | |   | __ |       ||   |___ |   |
|    ___||   | |   ||  | |     | |    ___||   |___
|   |    |   | |   |_| ||   _   ||   |___ |       |
|___|    |___| |_______||__| |__||_______||_______|


Written for the 2018 FIRST Robotics Competition game "POWER UP". All code here is either written by students from
team 1902 or is open source/publicly available to the FIRST community.

Written by:
Ryan S
Varun A
Natalie B
Ruth P
Jeffrey S
Sebastian V
 */

package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.powerup.core.command.*;
import com.explodingbacon.powerup.core.networktest.Server;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeo;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import com.explodingbacon.powerup.core.subsystems.ClimberSubsystem;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import com.explodingbacon.powerup.core.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotCore {

    public static boolean MAIN_ROBOT = true;

    public static DriveSubsystem drive;
    public static ClimberSubsystem climber;
    public static ArmSubsystem arm;
    public static IntakeSubsystem intake;

    public static QuNeo quneo;
    public static Server server;
    private OI oi;

    public static Compressor compressor;

    //public static Pad forward, back, left, right;

    public Robot(IterativeRobot r) {
        super(r);
    }


    @Override
    public void robotInit() {
        super.robotInit();

        compressor = new Compressor();

        oi = new OI();
        drive = new DriveSubsystem();
        intake = new IntakeSubsystem();
        arm = new ArmSubsystem();
        if (!MAIN_ROBOT) {
            climber = new ClimberSubsystem();
        }

        SmartDashboard.putNumber("kP", 0.001);
        SmartDashboard.putNumber("kI", 0);
        SmartDashboard.putNumber("kD", 0);

        //quneo = new QuNeo();
        //server = new Server();

        Robot.drive.gyro.rezero();

        if (MAIN_ROBOT) {
            Log.i("PIGXEL mode.");
        } else {
            Log.i("Coil Spring Container mode! (PRACTICE BOT)");
        }
    }

    @Override
    public void disabledInit() {
        arm.armPID.disable();
    }

    @Override
    public void disabledPeriodic() {
        Log.d("Front: " + arm.frontLimit.get() + ", back: " + arm.backLimit.get());
        //Log.d("Left: " + Robot.drive.leftDriveEncoder.get() + ", Right: " + Robot.drive.rightDriveEncoder.get());
        //Log.d("Gyro: " + drive.gyro.getForPID());
        Log.d("Arm: " + arm.getPosition());
    }

    @Override
    public void autonomousInit() {
        SmartDashboard.putBoolean("Start at Left", true);
        SmartDashboard.putBoolean("Back up from switch", true);

        OI.runCommand(new ArmSafetyCommand());
        OI.runCommand(new AutonomousCommand());
    }

    @Override
    public void teleopInit() {
        OI.runCommand(new DriveCommand());
        OI.runCommand(new IntakeCommand());
        OI.runCommand(new ArmCommand());
        if (!ArmSafetyCommand.ACTIVE) {
            Log.i("init teleop safety command");
            OI.runCommand(new ArmSafetyCommand());
        } else {
            Log.i("Not enabling teleop safety commend due to safety command still running");
        }
        if (!MAIN_ROBOT) {
            OI.runCommand(new ClimberCommand());
        }

        arm.armPID.enable();

    }

    @Override
    public void teleopPeriodic() {
        Log.d("Arm: " + arm.getPosition());

        // Log.d("Left: " + Robot.drive.leftDriveEncoder.getRate() + ", Right: " + Robot.drive.rightDriveEncoder.getRate());
    }

    @Override
    public void testInit() {
        if (!MAIN_ROBOT) {
            climber.winch.testEachWait(0.5, 0.5);
        } else {
            drive.shift.set(true);
            drive.leftDrive.testEachWait(0.5,0.5);
            drive.rightDrive.testEachWait(0.5,0.5);
        }
    }

    @Override
    public void testPeriodic() {
        /*if (OI.driver.y.get()) {
            drive.rotatePID.setTarget(0);
        } else if (OI.driver.b.get()) {
            drive.rotatePID.setTarget(90);
        } else if (OI.driver.a.get()) {
            drive.rotatePID.setTarget(180);
        } else if (OI.driver.x.get()) {
            drive.rotatePID.setTarget(270);
        }
        double out = drive.rotatePIDOutput.getPower();
        //Log.d("Out: " + out);

        //Robot.drive.leftDrive.setPower(1);
        //Robot.drive.rightDrive.setPower(-1);

        Robot.drive.shift.set(true);
        Robot.drive.tankDrive(out, -out);
        drive.rotatePID.logVerbose();*/
    }
}
