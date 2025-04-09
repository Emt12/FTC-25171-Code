import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.PrintCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.StartEndCommand;
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

import Commands.Auto.AutoDrive;
import Commands.Auto.AutoDriveX;
import Commands.Auto.ElevatorControlAutoCmd;
import Commands.Auto.ElevatorDownAutoCmd;
import Commands.Elevator.BucketScoreCmd;
import Commands.Elevator.ElevatorControlDownCmd;
import Commands.Linkage.IntakeCmd;
import Commands.Linkage.OutTakeCmd;
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

@Autonomous(name = "Auto21", group = "Autos")
public class Auto1 extends CommandOpMode {
    private  ElevatorSubsystem elevatorSubsystem;
    private  BucketSubsystem bucketSubsystem;
    private DriveSubsystem driveSubsystem;
    private LinkageSubsystem linkageSubsystem;

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));

    // Skor yapılacak konum
    private final Pose scorePose = new Pose(6.3, 14.5, Math.toRadians(332.62));

    // İlk örnek alınacak konum
    private final Pose pickup1Pose = new Pose(6.35, 14.55, Math.toRadians(355.62));

    // İkinci örnek alınacak konum
    private final Pose pickup2Pose = new Pose(14.7, 20.4, Math.toRadians(340));

    // Üçüncü örnek alınacak konum
    private final Pose pickup3Pose = new Pose(20.7, 24.8, Math.toRadians(24));

    // Park pozisyonu
    private final Pose parkPose = new Pose(0, 0, Math.toRadians(0));

    /* These are our Paths and PathChains that we will define in buildPaths() */
    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;



    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        /* Here is an example for Constant Interpolation
        scorePreload.setConstantInterpolation(startPose.getHeading()); */

        /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();

        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();

        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
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
                        new InstantCommand(()->linkageSubsystem.ready()).withTimeout(500),
                        new AutoDrive(driveSubsystem, scorePreload ,telemetry, 0.5),
                        new ElevatorControlAutoCmd(elevatorSubsystem, 69),
                        new BucketScoreCmd(bucketSubsystem),
                        new ElevatorDownAutoCmd(elevatorSubsystem, driveSubsystem, 0),
                        //new AutoDrive(driveSubsystem, grabPickup1, telemetry, 0.5,false),


                        new IntakeCmd(linkageSubsystem),
                        new WaitCommand(500),
                        new OutTakeCmd(linkageSubsystem),
                        new WaitCommand(500),
                        new AutoDrive(driveSubsystem, grabPickup1, telemetry, 0.5,false),
                        //new AutoDrive(driveSubsystem,telemetry, 350),

                        new ElevatorControlAutoCmd(elevatorSubsystem, 69),
                        new BucketScoreCmd(bucketSubsystem),
                        new ElevatorControlDownCmd(elevatorSubsystem, 0)

                        //new AutoDrive(driveSubsystem, grabPickup1, telemetry, 0.5, true)
                        //new WaitCommand(1000),
                        //new AutoDrive(driveSubsystem, scorePickup1, telemetry, 0.5, false),
                        //new WaitCommand(1000),
                        //new ElevatorControlAutoCmd(elevatorSubsystem, 69),
                        //new WaitCommand(1000),
                        //new BucketScoreCmd(bucketSubsystem),
                        //new ElevatorControlDownCmd(elevatorSubsystem, 0),
                        //new WaitCommand(500000000)

                )

        );

    }
}

