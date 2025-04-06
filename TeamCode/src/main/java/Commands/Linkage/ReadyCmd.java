package Commands.Linkage;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class ReadyCmd extends CommandBase {
    private LinkageSubsystem link;
    public ReadyCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.ready();
    }
}
