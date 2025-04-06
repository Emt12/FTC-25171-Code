package Subsystems;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import Utils.Utilities;

@Config
public class ElevatorSubsystem extends SubsystemBase {
    private DcMotor elevatorRight;
    private DcMotor elevatorLeft;

    //private Motor elevatorRight;
    //private Motor elevatorLeft;
    private int target = 1;
    private Telemetry telemetry;
    public static double kP = 0.005, kI = 0, kD = 0;
    public static double maxSpeed = 1;
    private PIDController armPID;
    public static double basketHeight = 100;
    public static double zeroHeight = 0;
    public static double elevatorPerimeter=12.25221;
    public static double elevatorGearRatio = (((1+(double)(46/17))) * (1+(double)(46/17)));
    public static double encoderResolution = 384.5;
    private MotorGroup motorGroup;

    private boolean auto = true;
    private TouchSensor touchSensor;

    public ElevatorSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        elevatorLeft = hardwareMap.get(DcMotor.class, "rightElevator");
        elevatorRight = hardwareMap.get(DcMotor.class, "leftElevator");
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
        target = CMtoTick(target);
        elevatorRight.setTargetPosition(target);
        elevatorLeft.setTargetPosition(target);
        telemetry.addData("touch", touchSensor.isPressed());
        encoderResetSwitch();
    }

    public void setArmPower(double power){
        elevatorLeft.setPower(power);
        elevatorRight.setPower(power);
    }

    public void setArmPosition(int distance){
        target=distance;
    }



    //
    public void stop(){
        elevatorLeft.setPower(0);
        elevatorRight.setPower(0);
    }
    public void encoderResetSwitch(){
        if(touchSensor.isPressed() && target==0) {
            Utilities.resetMotors(elevatorLeft);
            Utilities.resetMotors(elevatorRight);
            target = 3;
        }
    }

    public static double elevatorToCM(int tick){
        return (tick/encoderResolution)*elevatorGearRatio*elevatorPerimeter;
    }
    public static int CMtoTick(double CM){
        return (int)(((CM*encoderResolution))/elevatorPerimeter);
    }
}