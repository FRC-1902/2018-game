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
 */

package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.powerup.core.command.*;
import com.explodingbacon.powerup.core.networktest.Server;
import com.explodingbacon.powerup.core.networktest.quneo.QuNeo;
import com.explodingbacon.powerup.core.networktest.quneo.inputs.Pad;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import com.explodingbacon.powerup.core.subsystems.ClimberSubsystem;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import com.explodingbacon.powerup.core.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotCore {

    public static boolean MAIN_ROBOT = false;

    public static DriveSubsystem drive;
    public static ClimberSubsystem climber;
    public static ArmSubsystem arm;
    public static IntakeSubsystem intake;

    public static QuNeo quneo;
    public static Server server;
    private OI oi;

    Compressor compressor;

    //public static Pad forward, back, left, right;

    public Robot(IterativeRobot r) {
        super(r);
    }


    @Override
    public void robotInit() {
        super.robotInit();

        compressor = new Compressor();
        //compressor.setClosedLoopControl(false);

        oi = new OI();
        drive = new DriveSubsystem();
        intake = new IntakeSubsystem();
        arm = new ArmSubsystem();
        //climber = new ClimberSubsystem();

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
        if (MAIN_ROBOT) {
            arm.armPID.disable();
        }
    }

    @Override
    public void disabledPeriodic() {
        Log.d("Left: " + Robot.drive.leftDriveEncoder.get() + ", Right: " + Robot.drive.rightDriveEncoder.get());
        if (!MAIN_ROBOT) {
           Log.d("Gyro: " + Robot.drive.gyro.getForPID());
        }
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        //OI.runCommand(new QuNeoDrive());
        OI.runCommand(new DriveCommand());
        OI.runCommand(new IntakeCommand());
        OI.runCommand(new ArmCommand());
        //OI.runCommand(new ClimberCommand());

        if (!MAIN_ROBOT) {
            arm.armPID.enable();
        }
    }

    @Override
    public void teleopPeriodic() {

        /*double arm = OI.driver.getRightTrigger() - OI.driver.getLeftTrigger();
        arm = Utils.deadzone(arm, 0.1);
        Robot.arm.arm.setPower(arm*.75);*/

    }

    @Override
    public void testInit() {
        super.testInit();

        OI.runCommand(new ArmCommand());
        Robot.arm.armPID.reTune(SmartDashboard.getNumber("kP", 0), SmartDashboard.getNumber("kI", 0),
                SmartDashboard.getNumber("kD", 0));

        arm.armPID.enable();

    }

    public void testPeriodic() {
        arm.armPID.logVerbose();
    }
}
