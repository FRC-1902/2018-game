package com.explodingbacon.powerup.core.subsystems;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.Subsystem;
import com.explodingbacon.powerup.core.Map;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.List;

public class ClimberSubsystem extends Subsystem {

    public MotorGroup winch;

    //public Solenoid climbSol;

    public ClimberSubsystem() {
        winch = new MotorGroup(new Motor(VictorSP.class, Map.CLIMBER_A), new Motor(VictorSP.class, Map.CLIMBER_B));
        winch.setInverts(false, true);
        //winch.setReversed(true);
        //winch.setInverts(true, true);

        //climbSol = new Solenoid(Map.CLIMBER);
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
