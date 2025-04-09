package Commands.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class WristMotorIn extends CommandBase {
    LinkageSubsystem link;
    public WristMotorIn(LinkageSubsystem link){
        this.link = link;
    }

    @Override
    public void execute() {
        link.wristMotorIn();
        super.execute();
    }
}
