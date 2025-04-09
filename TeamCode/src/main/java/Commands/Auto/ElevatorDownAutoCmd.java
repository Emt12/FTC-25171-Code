package Commands.Auto;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import java.util.concurrent.TimeUnit;

import Subsystems.DriveSubsystem;
import Subsystems.ElevatorSubsystem;

public class ElevatorDownAutoCmd extends CommandBase {
    private ElevatorSubsystem elevator;
    private int m_setpoint;
    private boolean isFinished = false;
    Timing.Timer timer;
    DriveSubsystem drive;
    double kP = 1, kI = 0, kD = 0.2, kF=0;
    public ElevatorDownAutoCmd(ElevatorSubsystem elevator, DriveSubsystem drive, int setpoint){
        this.m_setpoint=setpoint;
        this.elevator=elevator;
        timer = new Timing.Timer(2000, TimeUnit.MILLISECONDS);
        timer.start();
        this.drive = drive;
        //elevator.setCoefs(kP, kI, kD, kF);
        addRequirements(elevator);
    }

    @Override
    public void execute(){
        elevator.setArmPower(1);
        elevator.setArmPosition(m_setpoint);
    }

    @Override
    public boolean isFinished() {
        return elevator.atTarget();
    }

    @Override
    public void end(boolean interrupted) {
        elevator.setArmPower(0);
        drive.changeFlag(1);

        super.end(interrupted);
    }
}