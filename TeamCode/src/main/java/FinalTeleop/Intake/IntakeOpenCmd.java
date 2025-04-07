package FinalTeleop.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class IntakeOpenCmd extends CommandBase {
    private LinkageSubsystem link;
    public IntakeOpenCmd(LinkageSubsystem linkage){
        this.link = linkage;
        addRequirements(link);
    }

    @Override
    public void execute() {
        link.intakeTele();
        super.execute();
    }
}
