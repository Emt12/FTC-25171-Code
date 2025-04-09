package Commands.Elevator;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import java.util.concurrent.TimeUnit;

import Subsystems.ElevatorSubsystem;

public class ElevatorControlDownCmd extends CommandBase {
    private ElevatorSubsystem elevator;
    private int m_setpoint;
    private boolean isFinished = false;
    double kP = 1, kI = 0, kD = 0.2, kF=0;
    public ElevatorControlDownCmd(ElevatorSubsystem elevator, int setpoint){
        this.m_setpoint=setpoint;
        this.elevator=elevator;
        //elevator.setCoefs(kP, kI, kD, kF);
        addRequirements(elevator);
    }

    @Override
    public void execute(){
        elevator.setArmPower(1);
        elevator.setArmPosition(m_setpoint);
    }

    //@Override
    public boolean isFinished() {
        return elevator.atTarget();
    }

    @Override
    public void end(boolean interrupted) {
        elevator.setArmPower(0);

        super.end(interrupted);
    }
}