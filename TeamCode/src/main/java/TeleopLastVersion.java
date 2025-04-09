import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Commands.Elevator.ElevatorHookCmd;
import Commands.Drive.DriveCommand;
import Commands.Elevator.BucketScoreCmd;
import Commands.Elevator.ElevatorControlDownCmd;
import Commands.Elevator.ElevatorControlUpCmd;
import Commands.Intake.IntakeCloseCmd;
import Commands.Intake.IntakeOpenCmd;
import Commands.Intake.WristIntakeCmd;
import Commands.Intake.WristMotorIn;
import Commands.Intake.WristMotorOut;
import Commands.Intake.WristMotorReady;
import Commands.Intake.WristRaiseCmd;
import Subsystems.BucketSubsystem;
import Subsystems.DriveSubsystem;
import Subsystems.ElevatorSubsystem;
import Subsystems.LinkageSubsystem;
@TeleOp(name = "Op_Modes.Teleop.Tele")
public class TeleopLastVersion extends CommandOpMode {
    private ElevatorSubsystem elevatorSubsystem;
    private DriveSubsystem driveSubsystem;
    private GamepadEx DriverPad;
    private GamepadEx OperatorPad;
    private BucketSubsystem bucketSubsystem;
    private LinkageSubsystem linkageSubsystem;

    @Override
    public void initialize() {
        DriverPad = new GamepadEx(gamepad1);
        OperatorPad = new GamepadEx(gamepad2);

        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        bucketSubsystem = new BucketSubsystem(hardwareMap, telemetry);
        linkageSubsystem = new LinkageSubsystem(hardwareMap, telemetry);
        elevatorSubsystem = new ElevatorSubsystem(hardwareMap, telemetry);

        linkageSubsystem.outtakeTele();

        OperatorPad.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new WristRaiseCmd(linkageSubsystem));
        OperatorPad.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new WristIntakeCmd(linkageSubsystem));
        OperatorPad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new IntakeOpenCmd(linkageSubsystem));
        OperatorPad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new IntakeCloseCmd(linkageSubsystem));

        OperatorPad.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new ElevatorControlUpCmd(elevatorSubsystem, 69));
        OperatorPad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new ElevatorControlDownCmd(elevatorSubsystem, 1));

        OperatorPad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new BucketScoreCmd(bucketSubsystem));

        OperatorPad.getGamepadButton(GamepadKeys.Button.Y)
                        .whenHeld(new WristMotorIn(linkageSubsystem))
                                .whenReleased(new WristMotorReady(linkageSubsystem));
        OperatorPad.getGamepadButton(GamepadKeys.Button.A)
                        .whenHeld(new WristMotorOut(linkageSubsystem))
                                .whenReleased(new WristMotorReady(linkageSubsystem));
        OperatorPad.getGamepadButton(GamepadKeys.Button.START)
                        .whenHeld(new ElevatorHookCmd(elevatorSubsystem, -1));

        register(
                driveSubsystem,
                bucketSubsystem,
                linkageSubsystem,
                elevatorSubsystem
        );
        driveSubsystem.setDefaultCommand(
                new DriveCommand(
                        ()->DriverPad.getLeftX(),
                        ()->DriverPad.getLeftY(),
                        ()->DriverPad.getRightX(),
                        driveSubsystem
                )
        );


    }
}
