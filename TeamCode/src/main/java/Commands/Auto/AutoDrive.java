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
    public AutoDrive(DriveSubsystem drive, Path path, Telemetry telemetry, double power) {
        this.drive = drive;
        this.path = path;
        this.telemetry = telemetry;
        addRequirements(drive);
        follower = drive.getFollower();
        follower.setMaxPower(power);
        follower.followPath(path);
    }

    public AutoDrive(DriveSubsystem drive, PathChain path, Telemetry telemetry, double power, boolean holdEnd) {
        this.drive = drive;
        this.pathC = path;
        this.telemetry = telemetry;
        addRequirements(drive);
        follower = drive.getFollower();
        follower.setMaxPower(power);
        follower.followPath(pathC, holdEnd);
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

        telemetry.addData("X", drive.getCurrentPose().getX());
        telemetry.addData("Y", drive.getCurrentPose().getY());
        telemetry.addData("Heading", Math.toDegrees(drive.getCurrentPose().getHeading()));

        follower.update();
        telemetry.update();


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
