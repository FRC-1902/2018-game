package com.explodingbacon.powerup.core.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.positioning.PursuitControl;
import team254.utils.DriveSignal;
import team254.utils.Path;
import team254.utils.Translation2d;

import java.util.ArrayList;
import java.util.List;

public class auto254 extends Command {

    Path firstCube;

    Path toCube;
    Path toScore;
    PursuitControl control;

    public auto254() {
        control = new PursuitControl();
        List<Path.Waypoint> path = new ArrayList<>();

        path.add(new Path.Waypoint(new Translation2d(0, 0), 90));
        path.add(new Path.Waypoint(new Translation2d(135, 0), 90));
        path.add(new Path.Waypoint(new Translation2d(138, -2), 90));
        firstCube = new Path(path);

        path.clear();
        //path.add(new Path.Waypoint(new Translation2d(0, 0), 90));
        path.add(new Path.Waypoint(new Translation2d(138, -2), 90));
        path.add(new Path.Waypoint(new Translation2d(113, -15), 40));
        path.add(new Path.Waypoint(new Translation2d(103, -40), 40));
        toCube = new Path(path);

        path.clear();
        path.add(new Path.Waypoint(new Translation2d(90, -40), 90));
        path.add(new Path.Waypoint(new Translation2d(138, -25), 90));
        toScore = new Path(path);
    }

    public void followPath(Path p, boolean reversed) {
        control.followPath(p, reversed);
        while (!control.isDone() && Robot.isAutonomous() && Robot.isEnabled()) {
            DriveSignal o = control.tick();
            Robot.drive.tankDrive(o.leftMotor, o.rightMotor);
            if (control.isDone()) {
                Log.d("Kinda done?");
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {}
        }
        Robot.drive.tankDrive(0,0);
    }

    @Override
    public void onInit() {
        Robot.arm.armPID.enable();
        Robot.arm.setState(true, false);
        followPath(firstCube, false);
        try {
            Thread.sleep(150);
        } catch (Exception e) {}
        outtake();
        Robot.arm.setState(false, true);
        Robot.intake.setIntake(0.7, 1, true);
        followPath(toCube, true);
        Robot.intake.setIntake(0, false);
        Robot.arm.setState(true, false);
        followPath(toScore, false);
        try {
            Thread.sleep(150);
        } catch (Exception e) {}
        outtake();
    }

    public void outtake() {
        Robot.intake.setIntake(0.5, false); //0.4
        try {
            Thread.sleep(350);
        } catch (Exception e) {}
        Robot.intake.setIntake(0, false);
    }

    @Override
    public void onLoop() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
