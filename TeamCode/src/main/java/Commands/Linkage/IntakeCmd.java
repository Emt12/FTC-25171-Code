package Commands.Linkage;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class IntakeCmd extends CommandBase {
    private LinkageSubsystem link;
    public IntakeCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.intake();
    }

    @Override
    public boolean isFinished() {
        return link.getColor()!=0;
    }

    @Override
    public void end(boolean interrupted) {
        link.ready();
    }
}
