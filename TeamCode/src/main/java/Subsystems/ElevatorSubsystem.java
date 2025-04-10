package Subsystems;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import Utils.Utilities;

@Config
public class ElevatorSubsystem extends SubsystemBase {
    private DcMotorEx elevatorRight;
    private DcMotorEx elevatorLeft;

    private int target = 1;
    private Telemetry telemetry;

    public static double elevatorPerimeter=12.25221;

    public static double encoderResolution = 384.5;

    private TouchSensor touchSensor;

    PIDFCoefficients newPidL, newPidR;


    public ElevatorSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        elevatorLeft = hardwareMap.get(DcMotorEx.class, "rightElevator");
        elevatorRight = hardwareMap.get(DcMotorEx.class, "leftElevator");
        touchSensor = hardwareMap.get(TouchSensor.class, "touch");

        this.telemetry = telemetry;

        Utilities.resetMotors(elevatorLeft);
        Utilities.resetMotors(elevatorRight);

        elevatorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        elevatorRight.setTargetPosition(target);
        elevatorLeft.setTargetPosition(target);

        elevatorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elevatorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Utilities.setMotors(elevatorLeft,true);
        Utilities.setMotors(elevatorRight,true);


        this.telemetry = telemetry;
    }

    @Override
    public void periodic(){
        telemetry.addData("target", target);
        double dummy = elevatorToCM(elevatorLeft.getCurrentPosition());
        telemetry.addData("elevator Pose", dummy);
        target = CMtoTick(target);
        elevatorRight.setTargetPosition(target);
        elevatorLeft.setTargetPosition(target);
        telemetry.addData("touch", touchSensor.isPressed());

    }

    public boolean isReached(){
        return !elevatorLeft.isBusy() || !elevatorRight.isBusy();
    }

    public void setArmPower(double power){
        elevatorLeft.setPower(power);
        elevatorRight.setPower(power);
    }
    public boolean atTarget(){
        return touchSensor.isPressed();
    }
    public void setArmPosition(int distance){
        target=distance;
    }
    public static double elevatorToCM(int tick){
        return (tick/encoderResolution)*elevatorPerimeter;
    }
    public static int CMtoTick(double CM){
        return (int)(((CM*encoderResolution))/elevatorPerimeter);
    }
}