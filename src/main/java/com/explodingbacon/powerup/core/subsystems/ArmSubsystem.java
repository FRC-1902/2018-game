package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.DigitalInput;
import com.explodingbacon.powerup.core.AnalogSensor;
import com.explodingbacon.powerup.core.Map;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class ArmSubsystem extends Subsystem {

    public MotorGroup arm;

    public AnalogSensor armEncoder;

    public DigitalInput frontLimit, backLimit;

    public PIDController armPID;

    public static Double FLOOR_FRONT=null, SWITCH_FRONT, SWITCH_BACK, FLOOR_BACK=null,
    HECK, HUMAN_PLAYER_FRONT, HUMAN_PLAYER_BACK;

    public static final double MAX_OFFSET = 220;

    double target;
    public boolean front = true;
    public Preset preset;
    public double targetOff = 0;


    public ArmSubsystem() {

        arm = new MotorGroup(new Motor(VictorSP.class, Map.ARM_A), new Motor(VictorSP.class, Map.ARM_B));
        if (Robot.MAIN_ROBOT) {
            arm.setInverts(true, true); //false, true
            arm.setReversed(true);
        }

        armEncoder = new AnalogSensor(Map.ARM_ENCODER);
        armPID = new PIDController(arm, armEncoder, .0018, 0, 0); //.0014
        armPID.setInputInverted(true);

        frontLimit = new DigitalInput(Map.ARM_LIMIT_FRONT);
        frontLimit.setReversed(true);
        backLimit = new DigitalInput(Map.ARM_LIMIT_BACK);
        backLimit.setReversed(true);

        initPresets();

        ohHeck();
    }

    public void initPresets() {
        initPresets(false);
    }

    public void initPresets(boolean frontRelative) {
        if (FLOOR_FRONT == null && !frontRelative) {
            FLOOR_FRONT = Robot.MAIN_ROBOT ? 763 : 873d;
        }

        float flipMetric = 2320;

        if (FLOOR_BACK == null)
            FLOOR_BACK = FLOOR_FRONT + flipMetric;

        if (FLOOR_FRONT == null && frontRelative) {
            FLOOR_FRONT = FLOOR_BACK - flipMetric;
        }

        SWITCH_BACK = FLOOR_BACK - 610 - 50;
        SWITCH_FRONT = FLOOR_FRONT + 610 + 50;

        HECK = (SWITCH_FRONT+SWITCH_BACK)/2;

        double hp_off = 460+40;

        HUMAN_PLAYER_FRONT = FLOOR_FRONT + hp_off;
        HUMAN_PLAYER_BACK = FLOOR_BACK - hp_off;
    }

    public void setState(boolean front, boolean floor) {
        setState(front, floor ? Preset.FLOOR : Preset.SWITCH);
    }

    public void setState(boolean front, Preset preset) {
        this.front = front;
        this.preset = preset;

        boolean workingFront = front;
        if (Robot.MAIN_ROBOT) workingFront = !workingFront;

        double pos;

        if (workingFront) {
            if (preset == Preset.FLOOR) {
                pos = FLOOR_FRONT;
            } else if (preset == Preset.SWITCH) {
                pos = SWITCH_FRONT;
            } else if (preset == Preset.HUMAN_PLAYER) {
                pos = HUMAN_PLAYER_FRONT;
            } else {
                pos = HECK;
            }
        } else {
            if (preset == Preset.FLOOR) {
                pos = FLOOR_BACK;
            } else if (preset == Preset.SWITCH) {
                pos = SWITCH_BACK;
            } else if (preset == Preset.HUMAN_PLAYER) {
                pos = HUMAN_PLAYER_BACK;
            } else {
                pos = HECK;
            }
        }
        if (preset == Preset.HOLD) {
            pos = target;
        }
        if (target != pos) {
            target = pos;
            armPID.setTarget(target+(Robot.MAIN_ROBOT ? -targetOff : targetOff));
        }
    }

    public void ohHeck() {
        setState(front, Preset.HECK);
    }

    public void humanPlayer() {
        setState(front, Preset.HUMAN_PLAYER);
    }

    public void setOffset(double offset) {
        targetOff = offset;
        armPID.setTarget(target+(Robot.MAIN_ROBOT ? -targetOff : targetOff));
    }

    public double getPosition() {
        return armEncoder.getForPID();
    }

    public double getPositionRelative() {
        return armEncoder.getForPID() - FLOOR_FRONT;
    }

    @Override
    public void enabledInit() {
        target = armEncoder.getForPID();
        setState(front, Preset.HOLD);
    }

    @Override
    public void disabledInit() {}

    @Override
    public void stop() {}

    @Override
    public List<Motor> getAllMotors() {
        return null;
    }

    public enum Preset {
        FLOOR,
        SWITCH,
        HECK,
        HUMAN_PLAYER,
        HOLD
    }
}
