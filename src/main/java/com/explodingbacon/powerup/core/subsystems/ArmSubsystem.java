package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import com.explodingbacon.powerup.core.Map;
import com.explodingbacon.powerup.core.OI;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class ArmSubsystem extends Subsystem {

    MotorGroup arm;
    Encoder armEncoder;
    PIDController armPID;

    int positionOne;
    int positionTwo;
    int positionThree;
    int positionFour;

    public ArmSubsystem() {

        arm = new MotorGroup(new Motor(VictorSP.class, Map.ARM_A), new Motor(VictorSP.class, Map.ARM_B));
        armEncoder = new Encoder(Map.ARM_ENCODER_A, Map.ARM_ENCODER_B);
        armPID = new PIDController(arm, armEncoder, .1, .1, .1);
        armEncoder.setPIDMode(AbstractEncoder.PIDMode.POSITION);

        positionOne = 0;
        positionTwo = 0;
        positionThree = 0;
        positionFour = 0;

        armPID.setTarget(0);
    }


    public void moveDirectionA() {
        arm.setPower(1.0);
    }
    public void moveDirectionB() {
        arm.setPower(-1.0);
    }


    public void toPosition (boolean buttonOnePressed, boolean buttomTwoPressed, boolean buttonThreePressed, boolean buttonFourPressed) {

        int armPosition = armEncoder.get();

        if (buttonOnePressed) {
            if (armPosition != positionOne) {
                if (armPosition < positionOne) {
                    moveDirectionA();
                }

            } else {
                moveDirectionB();
            }

        } else if (buttomTwoPressed) {
            if (armPosition != positionTwo ) {
                if (armPosition < positionTwo) {
                    moveDirectionA();
                }else {
                    moveDirectionB();
                }
            }

        } else if (buttonThreePressed) {
            if (armPosition != positionThree){
                if (armPosition < positionThree){
                    moveDirectionA();
                }else {
                    moveDirectionB();
                }
            }

        } else if (buttonFourPressed) {
            if (armPosition != positionFour) {
                if (armPosition < positionFour){
                    moveDirectionA();
                }else{
                    moveDirectionB();
                }
            }

        }
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
}
