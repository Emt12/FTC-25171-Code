package Op_Modes.Teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import Commands.Arm.BucketDownCmd;
import Commands.Arm.BucketUpCmd;
import Commands.Arm.ElevatorCmd;
import Commands.Arm.ElevatorHookCmd;
import Commands.Auto.IntakeAutoCmd;
import Commands.Auto.ScoreCmd;
import Commands.Drive.DriveCommand;
import Commands.Linkage.IntakeCmd;
import Commands.Linkage.OutTakeCmd;
import Commands.Linkage.ReadyCmd;
import Commands.Teleop.IntakeTeleCmd;
import Commands.Teleop.OutTakeTeleCmd;
import Subsystems.BucketSubsystem;
import Subsystems.DriveSubsystem;
import Subsystems.ElevatorSubsystem;
import Subsystems.LinkageSubsystem;

@TeleOp(name = "Op_Modes.Teleop.ElevatorEMT")
public class ElevatorEMT extends CommandOpMode {
        private ElevatorSubsystem elevator;
        private DriveSubsystem driveSubsystem;
        private GamepadEx gamepad;
        private GamepadEx gamepadU;
        private BucketSubsystem bucketSubsystem;
        private LinkageSubsystem link;



        @Override
        public void initialize() {
                gamepad = new GamepadEx(gamepad1);
                gamepadU = new GamepadEx(gamepad2);

                driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
                bucketSubsystem =  new BucketSubsystem(hardwareMap, telemetry);
                link = new LinkageSubsystem(hardwareMap, telemetry);


                elevator = new ElevatorSubsystem(
                        hardwareMap,
                        telemetry
                );

                gamepadU.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                        .whenPressed(driveSubsystem.changeRelativity());

                gamepadU.getGamepadButton(GamepadKeys.Button.BACK)
                        .whenHeld(new ElevatorHookCmd(elevator, -1));
                gamepadU.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                        .whenPressed(new ElevatorCmd(elevator, 69));
                gamepadU.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenPressed(new ElevatorCmd(elevator, 0));

                gamepadU.getGamepadButton(GamepadKeys.Button.A)
                        .whenPressed(new BucketUpCmd(bucketSubsystem))
                        .whenReleased(new BucketDownCmd(bucketSubsystem));

                gamepadU.getGamepadButton(GamepadKeys.Button.Y)
                        .whenPressed(new IntakeCmd(link))
                        .whenReleased(new ReadyCmd(link));
                gamepadU.getGamepadButton(GamepadKeys.Button.B)
                        .whenPressed(new OutTakeCmd(link))
                        .whenReleased(new ReadyCmd(link));

                gamepadU.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                        .whenHeld(new IntakeTeleCmd(link))
                        .whenReleased(new ReadyCmd(link));

                gamepadU.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                        .whenHeld(new OutTakeTeleCmd(link));

                /*
                gamepadU.getGamepadButton(GamepadKeys.Button.A)
                        .whenPressed(new IntakeAutoCmd(link));
                gamepadU.getGamepadButton(GamepadKeys.Button.Y)
                        .whenPressed(new ScoreCmd(link, elevator, bucketSubsystem));
                 */


                register(driveSubsystem);
                driveSubsystem.setDefaultCommand(
                        new DriveCommand(
                                ()->gamepad.getLeftX(),
                                ()->gamepad.getLeftY(),
                                ()->gamepad.getRightX(),
                                driveSubsystem
                        )
                );
        }

}