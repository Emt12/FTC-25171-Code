package FinalTeleop.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class WristMotorOut extends CommandBase {
    LinkageSubsystem link;
    public WristMotorOut(LinkageSubsystem link){
        this.link = link;
    }

    @Override
    public void execute() {
        link.wristMotorOut();
        super.execute();
    }
}
