package Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class WristIntakeCmd extends CommandBase {
    private LinkageSubsystem link;
    boolean finish;
    public WristIntakeCmd(LinkageSubsystem linkage){
        this.link = linkage;
    }

    @Override
    public void execute() {
        finish = link.wristIntake();
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return finish;
    }

    @Override
    public void end(boolean interrupted) {
        link.wristIntake();
    }
}
