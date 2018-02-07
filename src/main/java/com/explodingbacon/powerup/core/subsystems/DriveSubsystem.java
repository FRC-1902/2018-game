package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.actuators.Solenoid;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.BNOGyro;
import com.explodingbacon.bcnlib.sensors.Encoder;
import com.explodingbacon.powerup.core.Map;

import java.util.List;

public class DriveSubsystem extends Subsystem {

    public MotorGroup leftDrive, rightDrive;
    public PIDController positionPIDRight;
    public PIDController positionPIDLeft;
    public PIDController rotatePID;
    public Encoder rightDriveEncoder;
    public Encoder leftDriveEncoder;
    public BNOGyro gyro;

    public Solenoid shift, light;

    public FakeMotor rightPositionPIDOutput, leftPositionPIDOutput, rotatePIDOutput;

    public DriveSubsystem() {

        gyro = new BNOGyro(true);

        shift = new Solenoid(Map.SHIFT);
        light = new Solenoid(1);

        rotatePID = new PIDController(rotatePIDOutput, gyro, .1,.1,.1);

        rightDrive = new MotorGroup(new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_A), new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_B),
                new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_C));

        rightDrive.setReversed(true);

        leftDrive = new MotorGroup(new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_A), new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_B),
                new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_C));

        rightPositionPIDOutput = new FakeMotor();
        leftPositionPIDOutput = new FakeMotor();
        rotatePIDOutput = new FakeMotor();

        rightDriveEncoder = new Encoder(Map.DRIVE_RIGHT_ENCODER_A, Map.DRIVE_RIGHT_ENCODER_B);
        leftDriveEncoder = new Encoder(Map.DRIVE_LEFT_ENCODER_A, Map.DRIVE_LEFT_ENCODER_B);

        rightDriveEncoder.setReversed(true);


        //positionPIDRight = new PIDController(rightPositionPIDOutput, rightDriveEncoder, .1, .1, .1);
        //positionPIDLeft = new PIDController(leftPositionPIDOutput, leftDriveEncoder, .1, .1, .1);

        rotatePID.setRotational(true);

    }

    public double getRate() {
        return (Math.abs(rightDriveEncoder.getRate() + leftDriveEncoder.getRate()))/2;
    }

    public double getRateNotAbs() {
        return (rightDriveEncoder.getRate() + leftDriveEncoder.getRate())/2;
    }

    public void tankDrive(double left, double right) {
        leftDrive.setPower(left);
        rightDrive.setPower(right);
    }

    public static double inchesTORotations(){
        return -1;
    }

    public double getRightVelocityInchesPerSec(){
         return -1;
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

    public void setPIDEnabled(boolean state){
        if(state){
            rotatePID.enable();
        } else{
            rotatePID.disable();
        }
    }
}
