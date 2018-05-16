package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.powerup.core.Robot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class AutoMotion extends Command {

    static final double max_velocity = 2.1336 * 0.5;
    static final double max_accel = 2 * .5;
    static final double max_jerk = 30;

    static final double wheelbase_width = 0.8382;
    static final double wheel_diameter = 0.1524;

    TankModifier modifier;

    EncoderFollower left, right;

    static final double kP = 2;

    public AutoMotion() {
        super();

        /*Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };*/

        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(1, 1 , Pathfinder.d2r(-90)),
                new Waypoint(0.5, 2, Pathfinder.d2r(180)),
                new Waypoint(0.5, 0, Pathfinder.d2r(90))
        };

        Log.i("Made waypoints");

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, max_velocity, max_accel, max_jerk);
        Log.i("config");

        Trajectory trajectory = Pathfinder.generate(points, config);

        Log.i("MADE TRAJ");

        modifier = new TankModifier(trajectory).modify(wheelbase_width); //wheelbase width

        Log.i("modded");

        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());

        Log.i("Followers done, auto init done");
    }

    @Override
    public void onInit() {
        Robot.drive.gyro.rezero();

        left.configureEncoder(Robot.drive.leftDriveEncoder.get(), 360, wheel_diameter);
        right.configureEncoder(Robot.drive.rightDriveEncoder.get(), 360, wheel_diameter);

        left.configurePIDVA(kP, 0.0, 0.0, 1 / max_velocity, 0);
        right.configurePIDVA(kP, 0.0, 0.0, 1 / max_velocity, 0);
    }

    @Override
    public void onLoop() {
        double l = left.calculate(Robot.drive.leftDriveEncoder.get());
        double r = right.calculate(Robot.drive.rightDriveEncoder.get());

        double gyro_heading = Robot.drive.gyro.getHeading();    // Assuming the gyro is giving a value in degrees
        double desired_heading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.0016 * (-1.0/80.0) * angleDifference;

        //double error = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        //double turn = 0.025 * (error);

        Robot.drive.tankDrive(l+turn,r-turn);
        //setLeftMotors(l + turn);
        //setRightMotors(r - turn);
    }

    @Override
    public void onStop() {
        Robot.drive.tankDrive(0,0);
    }

    @Override
    public boolean isFinished() {
        /*if (left.isFinished() && right.isFinished()) {
            return true;
        }*/
        if (!Robot.isAutonomous() || !Robot.isEnabled()) {
            return true;
        }
        return false;
    }
}
