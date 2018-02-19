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
import com.explodingbacon.powerup.core.Robot;

import java.util.List;

public class DriveSubsystem extends Subsystem {

    public MotorGroup leftDrive, rightDrive;
    public PIDController positionPIDRight;
    public PIDController positionPIDLeft;
    public PIDController rotatePID, rotateDrivingPID;
    public Encoder rightDriveEncoder;
    public Encoder leftDriveEncoder;
    public BNOGyro gyro;

    public Solenoid shift, light;

    public FakeMotor rightPositionPIDOutput, leftPositionPIDOutput, rotatePIDOutput, rotateDrivingPIDOutput;

    public DriveSubsystem() {

        gyro = new BNOGyro(true);

        shift = new Solenoid(Map.SHIFT);
        light = new Solenoid(1);

        rightPositionPIDOutput = new FakeMotor();
        leftPositionPIDOutput = new FakeMotor();
        rotatePIDOutput = new FakeMotor();
        rotateDrivingPIDOutput = new FakeMotor();

        rotatePID = new PIDController(rotatePIDOutput, gyro, .0085,0.001,0);
        rotatePID.setRotational(true);

        rotateDrivingPID = new PIDController(rotateDrivingPIDOutput, gyro, .0085,0,0);
        rotateDrivingPID.setRotational(true);

        rightDrive = new MotorGroup(new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_A), new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_B),
                new Motor(WPI_VictorSPX.class, Map.RIGHT_DRIVE_C));

        rightDrive.setReversed(true);


        leftDrive = new MotorGroup(new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_A), new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_B),
                new Motor(WPI_VictorSPX.class, Map.LEFT_DRIVE_C));


        rightDriveEncoder = new Encoder(Map.DRIVE_RIGHT_ENCODER_A, Map.DRIVE_RIGHT_ENCODER_B);
        leftDriveEncoder = new Encoder(Map.DRIVE_LEFT_ENCODER_A, Map.DRIVE_LEFT_ENCODER_B);

        rightDriveEncoder.setReversed(true);


        //positionPIDRight = new PIDController(rightPositionPIDOutput, rightDriveEncoder, .1, .1, .1);
        //positionPIDLeft = new PIDController(leftPositionPIDOutput, leftDriveEncoder, .1, .1, .1);
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

    /**
     * Converts inches to strafe encoder clicks.
     *
     * @param inches The inches to be converted.
     * @return The encoder clicks equivalent to the inches provided.
     */
    public static double inchesToClicks(double inches) {
        return inchesToRotations(inches) * 360;
    }

    public static double inchesPerSecondToRpm(double inches_per_second) {
        return inchesToRotations(inches_per_second) * 60;
    }

    public static double inchesToRotations(double inches) {
        return inches / (Math.PI * 6);
}

    public static double clicksToInches(double clicks) {
        return rotationsToInches(clicks / 360);
    }

    private static double rpmToInchesPerSecond(double rpm) {
        return rotationsToInches(rpm) / 60;
    }

    public static double rotationsToInches(double rotations) {
        return rotations * (Math.PI * 6);
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
