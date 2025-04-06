package Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinkageSubsystem extends SubsystemBase {
    private Servo rlservo;
    private Servo llservo;
    private Servo rwservo;
    private Servo lwservo;

    private Motor m_intake;

    private ColorSensor m_colorSensor;

    private Telemetry telemetry;

    int r=0, g=0, b=0;
    public LinkageSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        rlservo = hardwareMap.get(Servo.class, "rlservo");
        llservo = hardwareMap.get(Servo.class, "llservo");
        rwservo = hardwareMap.get(Servo.class, "rwservo");
        lwservo = hardwareMap.get(Servo.class, "lwservo");
        m_intake = new Motor(hardwareMap, "intake");
        m_intake.setRunMode(Motor.RunMode.RawPower);
        this.telemetry = telemetry;
        m_colorSensor = hardwareMap.get(ColorSensor.class, "color");


    }
    public int findColor(int r, int g, int b){
        int total = r+g+b;
        if(total<800)
            return 0;

        if (r>b && r>g){
            return 3;
        } else if (g>r && g>b) {
            return 1;
        } else if (b>r &&b>g) {
            return 2;
        }

        return 5;
    }

    @Override
    public void periodic() {

        r = m_colorSensor.red();
        g = m_colorSensor.green();
        b = m_colorSensor.blue();
        telemetry.addData("R", r);
        telemetry.addData("G", g);
        telemetry.addData("B", b);

        telemetry.addData("color code", findColor(r, g, b));


        super.periodic();
    }
    public int getColor(){
        return findColor(r ,g ,b);
    }

    public void outtake(){
        rlservo.setPosition(0);
        llservo.setPosition(0.7);
        rwservo.setPosition(0);
        lwservo.setPosition(1);
        m_intake.set(1);
    }

    public void ready(){
        rlservo.setPosition(0);
        llservo.setPosition(0.7);
        rwservo.setPosition(0.06);
        lwservo.setPosition(0.94);
        m_intake.set(0);
    }

    public void intake(){
        rlservo.setPosition(0.7);
        llservo.setPosition(0);
        rwservo.setPosition(1);
        lwservo.setPosition(0);
        m_intake.set(-1);
    }

    public void intakeTele(){
        rlservo.setPosition(0.7);
        llservo.setPosition(0);
    }
    public void outtakeTele(){
        rlservo.setPosition(0);
        llservo.setPosition(0.7);
    }
    public void wristIntake(){
        rwservo.setPosition(1);
        lwservo.setPosition(0);
        m_intake.set(-1);
    }
    public void wristOuttake(){
        rwservo.setPosition(0);
        lwservo.setPosition(1);
        m_intake.set(0);
    }
}
