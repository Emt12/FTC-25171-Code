package Commands.Linkage;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class OutTakeCmd extends CommandBase {
    private LinkageSubsystem link;
    public OutTakeCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.outtake();
    }

    @Override
    public boolean isFinished() {
        return link.getColor()==0;
    }

    @Override
    public void end(boolean interrupted) {
        link.ready();
    }
}
