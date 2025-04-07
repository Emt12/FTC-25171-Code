package FinalTeleop.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class IntakeCloseCmd extends CommandBase {
    private LinkageSubsystem link;
    public IntakeCloseCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.outtakeTele();
        super.execute();
    }
}
