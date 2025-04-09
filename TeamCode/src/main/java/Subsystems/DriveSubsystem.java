package Subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

public class DriveSubsystem extends SubsystemBase {
    int flag=0;
    private Follower follower;
    private Telemetry telemetry;
    private final Pose startPose = new Pose(0,0,0);
    boolean fieldRelative = false;

    DcMotor fl, fr, bl, br;
    public DriveSubsystem(HardwareMap hwMap, Telemetry telemetry){
        follower = new Follower(hwMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        follower.startTeleopDrive();
        this.telemetry = telemetry;

    }

    public void drive(double lx, double ly, double rx){
        if(fieldRelative){
            follower.setTeleOpMovementVectors(ly, -lx, -rx, false);
            follower.update();
            updateTelemetry();
        }
        else{
            follower.setTeleOpMovementVectors(ly, -lx, -rx, true);
            follower.update();
            updateTelemetry();
        }
    }
    void updateTelemetry(){
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }
    private void changeRelative(){
        fieldRelative = !fieldRelative;
    }
    public Command changeRelativity() {
        return new RunCommand(this::changeRelative);
    }
    public void autoUpdate(){
        follower.update();}

    public Pose getCurrentPose(){
        return follower.getPose();
    }

    public Follower getFollower(){
        return follower;
    }

    public void changeFlag(int step){
        flag = step;
    }
    public int getFlag(){
        return flag;
    }
}
