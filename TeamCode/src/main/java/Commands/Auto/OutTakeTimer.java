package Commands.Auto;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;
import com.pedropathing.util.Timer;

import java.sql.Time;

import Subsystems.LinkageSubsystem;

public class OutTakeTimer extends CommandBase {
    private LinkageSubsystem link;
    private Timing.Timer timer;
    public OutTakeTimer(LinkageSubsystem linkage){
        this.link = linkage;
        timer = new Timing.Timer(1);
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.outtake();
    }

    @Override
    public boolean isFinished() {
        timer.start();
        return timer.done();
    }

    @Override
    public void end(boolean interrupted) {
        link.ready();
    }
}
