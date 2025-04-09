package Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.concurrent.TimeUnit;

public class BucketSubsystem extends SubsystemBase {
    private Motor bucketMotor;
    public BucketSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        bucketMotor = new Motor(hardwareMap, "bucket");
        bucketMotor.setRunMode(Motor.RunMode.RawPower);
    }

    public boolean runBucketUp(){
        Timing.Timer timer = new Timing.Timer(500, TimeUnit.MILLISECONDS);
        timer.start();
        while(!timer.done())
            bucketMotor.set(1);
        return true;
    }
    public boolean runBucketDown(){
        Timing.Timer timer = new Timing.Timer(500, TimeUnit.MILLISECONDS);
        timer.start();
        while(!timer.done())
            bucketMotor.set(-1);
        bucketMotor.set(0);
        return true;
    }
    public void stop(){
        bucketMotor.set(0);
    }


}
