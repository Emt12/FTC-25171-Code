package Commands.Teleop;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class IntakeTeleCmd extends CommandBase {
    private LinkageSubsystem link;
    public IntakeTeleCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.intakeTele();
    }

}
