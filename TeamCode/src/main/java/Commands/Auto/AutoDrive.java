package Commands.Auto;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import Subsystems.DriveSubsystem;

public class AutoDrive extends CommandBase {
    DriveSubsystem drive;
    Path path;
    PathChain pathC;
    double xSpd=0, ySpd=0, hSpd=0;
    Telemetry telemetry;
    Follower follower;
    public AutoDrive(DriveSubsystem drive, Path path, Telemetry telemetry) {
        this.drive = drive;
        this.path = path;
        this.telemetry = telemetry;
        addRequirements(drive);
        follower = drive.getFollower();
        follower.setMaxPower(0.8);
        follower.followPath(path);
    }

    public AutoDrive(DriveSubsystem drive, PathChain path, Telemetry telemetry) {
        this.drive = drive;
        this.pathC = path;
        this.telemetry = telemetry;
        addRequirements(drive);
        follower = drive.getFollower();
        follower.setMaxPower(0.8);
        follower.followPath(pathC);
    }

    @Override
    public void execute() {
        /*double erX = pose.getX() - drive.getCurrentPose().getX();
        double erY = pose.getY() - drive.getCurrentPose().getY();
        double erH = pose.getHeading() - drive.getCurrentPose().getHeading();
        telemetry.addData("des x", erX);
        telemetry.addData("des y", erY);
        telemetry.addData("des hed", erH);
        drive.drive(erX*0.1, erY*0.01, 0);*/

        follower.update();


        super.execute();
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy();

    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
}
