package com.explodingbacon.powerup.core.positioning;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Timer;
import team254.utils.AdaptivePurePursuitController;
import team254.utils.DriveSignal;
import team254.utils.Path;
import team254.utils.RigidTransform2d;

public class PursuitControl {

    FakeMotor leftOutput, rightOutput;
    public AdaptivePurePursuitController pathFollower;
    PIDController left, right;

    final double kP = 0.01, kI = 0, kD = 0;

    public PursuitControl() {
        leftOutput = new FakeMotor();
        rightOutput = new FakeMotor();
        left = new PIDController(leftOutput, Robot.drive.leftDriveEncoder, kP, kI, kD);
        right = new PIDController(rightOutput, Robot.drive.rightDriveEncoder, kP, kI, kD);
    }

    public void followPath(Path path, boolean reversed) {
        pathFollower = new AdaptivePurePursuitController(Constants.pathFollowLookahead,
                Constants.pathFollowMaxAccel, Constants.kLooperDt, path, reversed, 0.1); //.25
    }

    public boolean isDone() {
        return pathFollower.isDone();
    }

    public DriveSignal tick() {
        if (pathFollower != null && !pathFollower.isDone()) {
            if (!left.isEnabled()) left.enable();
            if (!right.isEnabled()) right.enable();
            RigidTransform2d robot_pose = RobotState.getInstance().getLatestFieldToVehicle().getValue();
            RigidTransform2d.Delta command = pathFollower.update(robot_pose, Timer.getFPGATimestamp());
            Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);

            // Scale the command to respect the max velocity limits
            double max_vel = 0;
            max_vel = Math.max(max_vel, Math.abs(setpoint.left));
            max_vel = Math.max(max_vel, Math.abs(setpoint.right));
            if (max_vel > Constants.pathFollowMaxVel) {
                double scaling = Constants.pathFollowMaxVel / max_vel;
                setpoint = new Kinematics.DriveVelocity(setpoint.left * scaling, setpoint.right * scaling);
            }

            //Utils.log(setpoint.left + ", " + setpoint.right);
            //Utils.log(Utils.roundToPlace((float)setpoint.left, 5) + " / " + Utils.roundToPlace((float)setpoint.right, 2));

            //TODO: velocity PID for left and right wheels receive the setpoint.left and setpoint.right values

            left.setTarget(left.getCurrentSourceValue() + setpoint.left);
            right.setTarget(right.getCurrentSourceValue() + setpoint.right);
        } else {
            left.disable();
            right.disable();
            leftOutput.setPower(0);
            rightOutput.setPower(0);
        }
        return new DriveSignal(leftOutput.getPower(), rightOutput.getPower());
    }
}