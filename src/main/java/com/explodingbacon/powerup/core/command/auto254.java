package com.explodingbacon.powerup.core.command;

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

    Path p;

    public auto254() {
        List<Path.Waypoint> path = new ArrayList<>();
        path.add(new Path.Waypoint(new Translation2d(0, 0), 70));
        path.add(new Path.Waypoint(new Translation2d(100, 0), 60));
        path.add(new Path.Waypoint(new Translation2d(100, 100), 35));
        path.add(new Path.Waypoint(new Translation2d(0, 0), 0));
        p = new Path(path);
    }

    @Override
    public void onInit() {
        PursuitControl control = new PursuitControl();
        control.followPath(p, false);


        while (!control.isDone() && Robot.isAutonomous() && Robot.isEnabled()) {
            DriveSignal o = control.tick();
            Robot.drive.tankDrive(o.leftMotor, o.rightMotor);
            if (control.isDone()) {
                Log.d("Kinda done?");
            }
           // robot.setMotors(o.left, o.right);
            try {
             Thread.sleep(10);
            } catch (Exception e) {}
        }
        Log.d("Done!");
        Robot.drive.tankDrive(0,0);
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
