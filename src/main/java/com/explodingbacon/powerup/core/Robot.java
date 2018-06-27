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

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.RobotCore;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.command.*;
import com.explodingbacon.powerup.core.positioning.RobotStateGenerator;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import com.explodingbacon.powerup.core.subsystems.ClimberSubsystem;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import com.explodingbacon.powerup.core.subsystems.IntakeSubsystem;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotCore {

    public static boolean MAIN_ROBOT = true;

    public static DriveSubsystem drive;
    public static ClimberSubsystem climber;
    public static ArmSubsystem arm;
    public static IntakeSubsystem intake;

    private OI oi;

    public static Compressor compressor;

    public static UsbCamera camera;

    public static SendableChooser<String> autoSelector;
    public static SendableChooser<LowGearAutoCommand.ThreeCubeEnding> threeCubeEnding;

    public Command testauto;

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

        SmartDashboard.putNumber("Autonomous Cubes", 3);

        SmartDashboard.putNumber("kP", 0.0014);
        SmartDashboard.putNumber("kI", 0);
        SmartDashboard.putNumber("kD", 0);

        Robot.drive.gyro.rezero();

        autoSelector = new SendableChooser<>();
        autoSelector.addDefault("LOW Gear 3 Cube Switch Auto (Middle)", "middle_low");
        autoSelector.addObject("Dump If Side (Left)", "left");
        autoSelector.addObject("Dump If Side (Right)", "right");
        SmartDashboard.putData("Auto Selector", autoSelector);


        threeCubeEnding = new SendableChooser<>();
        threeCubeEnding.addDefault("3 Cube End: Back Up", LowGearAutoCommand.ThreeCubeEnding.BACK_UP);
        threeCubeEnding.addObject("3 Cube End: Attempt Cube #4", LowGearAutoCommand.ThreeCubeEnding.CUBE_4);
        threeCubeEnding.addObject("3 Cube End: Go to Exchange", LowGearAutoCommand.ThreeCubeEnding.EXCHANGE);
        SmartDashboard.putData("3 Cube  Auto Ending", threeCubeEnding);

        camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setFPS(15);

        testauto = new auto254();

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
        //Log.d("Front: " + arm.frontLimit.get() + ", back: " + arm.backLimit.get());
       //Log.d("Left: " + Robot.drive.leftDriveEncoder.get() + ", Right: " + Robot.drive.rightDriveEncoder.get());
        //Log.d("Gyro: " + drive.gyro.getForPID());
        Log.d("Arm: " + arm.armEncoder.getForPID());
    }

    @Override
    public void autonomousInit() {
        if (!ArmSafetyCommand.ACTIVE) {
            Log.i("init autonomous safety command");
            OI.runCommand(new ArmSafetyCommand());
        } else {
            Log.i("Not enabling autonomous safety commend due to safety command still running");
        }
        /*
        String autoType = autoSelector.getSelected();
        AbstractAutoCommand auto = null;
        if (autoType.equals("middle_low")) {
            auto = new LowGearAutoCommand();
        } else if (autoType.equals("left")) {
            auto = new DriveForwardAuto(true);
        } else if (autoType.equals("right")) {
            auto = new DriveForwardAuto(false);
        }
        if (auto != null) OI.runCommand(auto);*/
        RobotStateGenerator.getInstance().start();


        OI.runCommand(testauto);
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

        //arm.armPID.reTune(SmartDashboard.getNumber("kP", 0), SmartDashboard.getNumber("kI", 0),
          //      SmartDashboard.getNumber("kD", 0));

        arm.armPID.enable();

        //arm.armPID.disable();
    }

    @Override
    public void teleopPeriodic() {
        //Log.d("Arm: " + arm.getPosition());

        // Log.d("Left: " + Robot.drive.leftDriveEncoder.getRate() + ", Right: " + Robot.drive.rightDriveEncoder.getRate());
    }

    @Override
    public void testInit() {
        /*arm.arm.getMotors().get(0).setPower(0.5);
        try {
            Thread.sleep(1000);
        } catch ( Exception e) {

        }
        arm.arm.getMotors().get(0).setPower(0);
        */arm.arm.testEachWait(0.5, 0.2);
        //drive.leftDrive.testEachWait(0.5, 0.5);
        //drive.rightDrive.testEachWait(0.5, 0.5);
    }

    @Override
    public void testPeriodic() {
        drive.shift(OI.driver.y.get());
    }
}
