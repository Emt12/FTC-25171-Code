package pedroPathing.examples;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelDeadlineGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.ScheduleCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import Commands.Arm.BucketDownCmd;
import Commands.Arm.BucketUpCmd;
import Commands.Arm.ElevatorCmd;
import Commands.Auto.AutoDrive;
import Commands.Auto.ScoreCmd;
import Commands.Auto.UpdateCmd;
import Commands.Linkage.IntakeCmd;
import Commands.Linkage.ReadyCmd;
import Subsystems.BucketSubsystem;
import Subsystems.DriveSubsystem;
import Subsystems.ElevatorSubsystem;
import Subsystems.LinkageSubsystem;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

/**
 * This is an example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Auto1", group = "Autos")
public class ExampleBucketAutoCmd extends CommandOpMode {
    private  ElevatorSubsystem elevatorSubsystem;
    private  BucketSubsystem bucketSubsystem;
    private DriveSubsystem driveSubsystem;
    private LinkageSubsystem linkageSubsystem;

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    private final Pose scorePose = new Pose(-1.373, 12.656, Math.toRadians(315));

    /** Lowest (First) Sample from the Spike Mark */
    private final Pose pickup1Pose = new Pose(11.681, 13.189, Math.toRadians(348));

    /** Middle (Second) Sample from the Spike Mark */
    private final Pose pickup2Pose = new Pose(11.366, 18.967, Math.toRadians(0));

    /** Highest (Third) Sample from the Spike Mark */
    private final Pose pickup3Pose = new Pose(24.193, 19.862, Math.toRadians(40.3));

    /** Park Pose for our robot, after we do all of the scoring. */
    private final Pose parkPose = new Pose(0, 0, Math.toRadians(0));

    /* These are our Paths and PathChains that we will define in buildPaths() */
    private Path scorePreload, park, pickup1, score1;



    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        pickup1 = new Path(new BezierLine(new Point(scorePose), new Point(pickup1Pose)));
        pickup1.setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading());

        score1 = new Path(new BezierLine(new Point(pickup1Pose), new Point(scorePose)));
        score1.setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading());

        park = new Path(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(parkPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }


    @Override
    public void initialize() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        elevatorSubsystem = new ElevatorSubsystem(hardwareMap, telemetry);
        bucketSubsystem = new BucketSubsystem(hardwareMap, telemetry);
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        linkageSubsystem = new LinkageSubsystem(hardwareMap, telemetry);

        opmodeTimer.resetTimer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();

        register(elevatorSubsystem, bucketSubsystem);


        schedule(
                new SequentialCommandGroup(
                        new AutoDrive(driveSubsystem, scorePreload, telemetry),
                        new ParallelRaceGroup(
                                new ElevatorCmd(elevatorSubsystem, 69),
                                new WaitCommand(1500)
                        ),
                        new BucketUpCmd(bucketSubsystem),
                        new BucketDownCmd(bucketSubsystem),
                        new ParallelRaceGroup(
                                new ElevatorCmd(elevatorSubsystem, 0),
                                new WaitCommand(1000)
                        ),
                        new ParallelRaceGroup(
                                new AutoDrive(driveSubsystem, pickup1, telemetry),
                                new IntakeCmd(linkageSubsystem)
                        ),
                        new AutoDrive(driveSubsystem, score1, telemetry)

                )


        );

    }
}

