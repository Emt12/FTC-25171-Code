package FinalTeleop.Intake;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.LinkageSubsystem;

public class WristMotorReady extends CommandBase {
    LinkageSubsystem link;
    public WristMotorReady(LinkageSubsystem link){
        this.link = link;
    }

    @Override
    public void execute() {
        link.wristMotorStop();
        super.execute();
    }
}
