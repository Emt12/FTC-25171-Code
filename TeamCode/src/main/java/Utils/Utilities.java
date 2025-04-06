package Utils;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Utilities {
    public static void resetMotorEncoders(DcMotor motor, boolean encoder){
        if (encoder){
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public static void resetMotors(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public static void setMotors(DcMotor motor, boolean encoder){
        if (encoder){
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        } else {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
}
