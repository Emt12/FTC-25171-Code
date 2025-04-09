import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
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

import com.qualcomm.robotcore.util.ElapsedTime;


import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Bucket Auto", group = "Houston")
public class BucketAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private Motor elevLeft;
    private Motor elevRight;

    private Motor bucket;

    private Motor intake;

    private ServoEx llServo;
    private ServoEx lrServo;
    private ServoEx wlServo;
    private  ServoEx wrServo;


    public static double elevatorPerimeter = 12.25221;
    public static double encoderResolution = 384.5;

    private int pathState;

    // Robotun başladığı konum
    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));

    // Skor yapılacak konum
    private final Pose scorePose = new Pose(-3.36, 13.06, Math.toRadians(315));

    // İlk örnek alınacak konum
    private final Pose pickup1Pose = new Pose(19, 11, Math.toRadians(10));

    // İkinci örnek alınacak konum
    private final Pose pickup2Pose = new Pose(19.8, 17.4, Math.toRadians(0));

    // Üçüncü örnek alınacak konum
    private final Pose pickup3Pose = new Pose(20.7, 24.8, Math.toRadians(24));

    // Park pozisyonu
    private final Pose parkPose = new Pose(0, 0, Math.toRadians(0));

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    public static int CMtoTick(double CM) {
        return (int)(((CM * encoderResolution)) / elevatorPerimeter);
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        //motor tanımlamaları
        elevLeft = new Motor(hardwareMap, "leftElevator");
        elevRight = new Motor(hardwareMap, "rightElevator");
        bucket = new Motor(hardwareMap, "bucket");
        intake = new Motor(hardwareMap, "intake");

        elevLeft.setRunMode(Motor.RunMode.PositionControl);
        elevRight.setRunMode(Motor.RunMode.PositionControl);
        elevRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        elevLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        elevRight.setPositionCoefficient(0.05);
        elevLeft.setPositionCoefficient(0.05);

        elevRight.setPositionTolerance(13);
        elevLeft.setPositionTolerance(13);

        bucket.setRunMode(Motor.RunMode.RawPower);
        intake.setRunMode(Motor.RunMode.RawPower);

        llServo = new SimpleServo(hardwareMap, "llservo",0,270);
        lrServo = new SimpleServo(hardwareMap, "rlservo",0,270);
        wlServo = new SimpleServo(hardwareMap, "lwservo",0,270);
        wrServo = new SimpleServo(hardwareMap, "rwservo",0,270);


        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void stop() {}

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        park = new Path(new BezierCurve(new Point(scorePose), new Point(parkPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                setElevPosition(70);
                bucketScore(1);
                follower.followPath(scorePreload);
                setPathState(1);
                setElevPosition(0);
                break;
            case 1:
                if (!follower.isBusy()) {
                    intakePosition();
                    follower.followPath(grabPickup1, true);
                    outtakePosition();
                    setPathState(2);
                }
                break;
            case 2:
                if (!follower.isBusy()) {
                    readyPosition();
                    follower.followPath(scorePickup1, true);
                    setElevPosition(70);
                    bucketScore(1);
                    setPathState(3);
                }
                break;
            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(grabPickup3, true);
                    setPathState(6);
                }
                break;
            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(park, true);
                    setPathState(8);
                }
                break;
            case 8:
                if (!follower.isBusy()) {
                    setPathState(9); // Durum dışı bırak
                }
                break;
        }
    }

    public void setElevPosition(int target){
        elevLeft.setTargetPosition(CMtoTick(target));
        elevRight.setTargetPosition(CMtoTick(target));
        int power = 1;
        while (!elevLeft.atTargetPosition()) {
            elevLeft.set(power);
            elevRight.set(-power);
        }
        elevLeft.stopMotor();
        elevRight.stopMotor();
    }

    public void bucketScore(double power) {
        ElapsedTime bucketTimer = new ElapsedTime();
        double _time = 0.4;

        // Kovayı ileri hareket ettir
        bucket.set(power);
        bucketTimer.reset();
        while (bucketTimer.seconds() < _time) {
            // 1 saniye bekle
        }
        // Kovayı geri hareket ettir
        bucket.set(-power);
        bucketTimer.reset();
        while (bucketTimer.seconds() < _time) {
            // 1 saniye daha bekle
        }
        // Kova durdurulur
        bucket.set(0);
    }

    public void intakePosition(){
        intake.set(-0.5);
        llServo.setPosition(0);
        lrServo.setPosition(0.7);
        wlServo.setPosition(0);
        wrServo.setPosition(1);
    }

    public void outtakePosition(){
        intake.set(0.75);
        llServo.setPosition(0.7);
        lrServo.setPosition(0);
        wlServo.setPosition(1);
        wrServo.setPosition(0);
    }

    public void readyPosition(){
        intake.stopMotor();
        llServo.setPosition(0.7);
        lrServo.setPosition(0);
        wlServo.setPosition(1);
        wrServo.setPosition(0);
    }


    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}
