package Commands.Teleop;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class OutTakeTeleCmd extends CommandBase {
    private LinkageSubsystem link;
    public OutTakeTeleCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.outtakeTele();
    }

}
