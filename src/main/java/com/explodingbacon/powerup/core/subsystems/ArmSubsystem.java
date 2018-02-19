package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.Encoder;
import com.explodingbacon.powerup.core.AnalogSensor;
import com.explodingbacon.powerup.core.Map;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class ArmSubsystem extends Subsystem {

    public MotorGroup arm;

    public AnalogSensor armEncoder;
    public PIDController armPID;

    public static double FLOOR_FRONT=0, SWITCH_FRONT, SWITCH_BACK, FLOOR_BACK;

    public static final double MAX_OFFSET = 220;

    double target;
    public boolean front = true;
    public boolean floor = true;
    public double targetOff = 0;

    int positionOne;
    int positionTwo;
    int positionThree;
    int positionFour;

    public ArmSubsystem() {

        arm = new MotorGroup(new Motor(VictorSP.class, Map.ARM_A), new Motor(VictorSP.class, Map.ARM_B));
        if (Robot.MAIN_ROBOT) {
            arm.setInverts(false, true);
        }

        armEncoder = new AnalogSensor(Map.ARM_ENCODER);
        armPID = new PIDController(arm, armEncoder, .001, 0, 0);
        armPID.setInputInverted(true);
        //armEncoder.setPIDMode(AbstractEncoder.PIDMode.POSITION);

        positionOne = 0;
        positionTwo = 0;
        positionThree = 0;
        positionFour = 0;

        initPresets();

        //armPID.enable();
        setState(true, false);
        armPID.setTarget(target);
    }

    public void initPresets() {
        if (FLOOR_FRONT == 0)
            FLOOR_FRONT = Robot.MAIN_ROBOT ? 1120 : 1411;
        SWITCH_FRONT = FLOOR_FRONT + 658;
        FLOOR_BACK = FLOOR_FRONT + 2303;
        SWITCH_BACK = FLOOR_BACK - 658;
    }

    public void setState(boolean front, boolean floor) {
        this.front = front;
        this.floor = floor;

        boolean workingFront = front;
        if (Robot.MAIN_ROBOT) workingFront = !workingFront;

        double pos;
        if (workingFront) {
            if (this.floor) {
                pos = FLOOR_FRONT;
            } else {
                pos = SWITCH_FRONT;
            }
        } else {
            if (this.floor) {
                pos = FLOOR_BACK;
            } else {
                pos = SWITCH_BACK;
            }
        }
        if (target != pos) {
            target = pos;
            armPID.setTarget(target+(Robot.MAIN_ROBOT ? -targetOff : targetOff));
        }
    }

    public void setOffset(double offset) {
        targetOff = offset;
        armPID.setTarget(target+(Robot.MAIN_ROBOT ? -targetOff : targetOff));
    }

    public void setFloor(boolean floor) {
        setState(front, floor);
    }

    public void setSide(boolean front) {
        setState(front, floor);
    }

    public void flipSide() {
        setSide(!front);
    }

    public double getPosition() {
        return armEncoder.getForPID();
    }

    public double getPositionRelative() {
        return armEncoder.getForPID() - FLOOR_FRONT;
    }


    public void moveDirectionA() {
        arm.setPower(1.0);
    }
    public void moveDirectionB() {
        arm.setPower(-1.0);
    }


    public void toPosition (boolean buttonOnePressed, boolean buttomTwoPressed, boolean buttonThreePressed, boolean buttonFourPressed) {

        double armPosition = armEncoder.getForPID();

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
