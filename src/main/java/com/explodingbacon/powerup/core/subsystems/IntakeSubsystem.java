package com.explodingbacon.powerup.core.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.Solenoid;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import com.explodingbacon.powerup.core.Map;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.VictorSP;
import java.util.List;

public class IntakeSubsystem extends Subsystem {

    Motor intakeMotorA; //Top on main, left on csc
    Motor intakeMotorB;

    Motor wrist;
    Solenoid grasp;
    AbstractEncoder wristEncoder;
    PIDController wristPID;

    boolean passiveIntake = false;

    public IntakeSubsystem() {
        Class intakeMotorClass = Robot.MAIN_ROBOT ? VictorSP.class : WPI_VictorSPX.class;
        intakeMotorA = new Motor(intakeMotorClass, Map.INTAKE_TOP);
        intakeMotorB = new Motor(intakeMotorClass, Map.INTAKE_BOTTOM);

        if (Robot.MAIN_ROBOT) {
            intakeMotorA.setReversed(true);
            intakeMotorB.setReversed(true);
        }
        else {
            intakeMotorB.setReversed(true);
            wrist = new Motor(intakeMotorClass, Map.WRIST);
            wristEncoder = new Encoder(Map.WRIST_ENCODER_A, Map.WRIST_ENCODER_B);
            grasp = new Solenoid(Map.INTAKE_GRASP);

            wristPID = new PIDController(wrist, wristEncoder, 0.001, 0, 0);
        }
    }

    public void setIntake(double pow, boolean in) {
        setIntake(pow, pow, in);
    }

    public void setIntake(double powTop, double powBot, boolean in) {
        double dir = in ? 1 : -1;
        boolean front = Robot.arm.front;
        intakeMotorA.setPower((front ? powTop : powBot) * dir);
        intakeMotorB.setPower(-1 * (!front ? powTop : powBot) * dir);
    }

    public void setGrasp(boolean b) {
        grasp.set(b);
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
