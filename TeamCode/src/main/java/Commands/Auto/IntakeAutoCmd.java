package Commands.Auto;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import Subsystems.LinkageSubsystem;

public class IntakeAutoCmd extends SequentialCommandGroup {
    private LinkageSubsystem link;
    public IntakeAutoCmd(LinkageSubsystem linkage){
        this.link = linkage;

        addCommands(
                new IntakeTimer(link),
                new OutTakeTimer(link)
                );
        addRequirements(linkage);
    }
}
