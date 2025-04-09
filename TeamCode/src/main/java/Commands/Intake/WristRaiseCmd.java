package Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class WristRaiseCmd extends CommandBase {
    private LinkageSubsystem link;
    boolean finish;

    public WristRaiseCmd(LinkageSubsystem linkage){
        this.link = linkage;
    }

    @Override
    public void execute() {
        finish = link.wristOuttake();
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return finish;
    }

    @Override
    public void end(boolean interrupted) {
        link.wristOuttake();
    }
}
