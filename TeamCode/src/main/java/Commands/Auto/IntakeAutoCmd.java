package Commands.Auto;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import Commands.Linkage.IntakeCmd;
import Commands.Linkage.OutTakeCmd;
import Subsystems.LinkageSubsystem;

public class IntakeAutoCmd extends SequentialCommandGroup {
    private LinkageSubsystem link;
    public IntakeAutoCmd(LinkageSubsystem linkage){
        this.link = linkage;

        addCommands(
                new IntakeCmd(link),
                new OutTakeCmd(link)
                );
        addRequirements(linkage);
    }
}
