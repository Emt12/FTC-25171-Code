package Commands.Auto;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import Subsystems.LinkageSubsystem;

public class IntakeTimer extends CommandBase {
    private LinkageSubsystem link;
    private Timing.Timer timer;
    public IntakeTimer(LinkageSubsystem linkage){
        this.link = linkage;
        timer = new Timing.Timer(2);
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.intake();
    }

    @Override
    public boolean isFinished() {
        timer.start();
        return timer.done();
    }
}
