package com.explodingbacon.powerup.test;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.RobotCore;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.VictorSP;

public class TestRobot extends RobotCore {
    Motor one, two, three, pwm;
    long i;

    public TestRobot(IterativeRobot r) {
        super(r);

        one = new Motor(new WPI_TalonSRX(1));
        two = new Motor(new WPI_TalonSRX(2));
        three = new Motor(new WPI_TalonSRX(3));
        pwm = new Motor(new VictorSP(5));
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();

        i = System.currentTimeMillis();

        one.setPower((i / 1000) % 2 == 0 ? 0 : 1);
        two.setPower(Math.sin(i / 500d));
        three.setPower((i / 1000) % 2 == 0 ? -0.3 : 0.3);
        pwm.setPower(1);
    }
}
