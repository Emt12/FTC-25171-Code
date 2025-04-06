package Commands.Auto;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import Commands.Arm.BucketDownCmd;
import Commands.Arm.BucketUpCmd;
import Commands.Arm.ElevatorCmd;
import Commands.Linkage.OutTakeCmd;
import Subsystems.BucketSubsystem;
import Subsystems.ElevatorSubsystem;
import Subsystems.LinkageSubsystem;

public class ScoreCmd extends SequentialCommandGroup {
    private LinkageSubsystem link;
    private ElevatorSubsystem elevator;
    private BucketSubsystem bucket;
    public ScoreCmd(LinkageSubsystem linkage, ElevatorSubsystem elevator, BucketSubsystem bucket){
        this.link = linkage;
        this.elevator = elevator;
        this.bucket = bucket;

        addCommands(
                new OutTakeCmd(link),
                new ElevatorCmd(elevator, 69),
                new WaitCommand(1500),
                new BucketUpCmd(bucket),
                new BucketDownCmd(bucket)
        );

        addRequirements(linkage, elevator, bucket);
    }
}
