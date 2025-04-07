package FinalTeleop.Elevator;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.ElevatorSubsystem;

public class ElevatorHookTeleopCmd extends CommandBase {
    private ElevatorSubsystem elevator;
    private int m_setpoint;
    private boolean isFinished = false;

    public ElevatorHookTeleopCmd(ElevatorSubsystem elevator, int setpoint){
        this.m_setpoint=setpoint;
        this.elevator=elevator;

        addRequirements(elevator);
    }

    @Override
    public void execute(){
        elevator.setArmPower(-1);
        elevator.setArmPosition(m_setpoint);
    }

    @Override
    public boolean isFinished(){
        return isFinished;
    }
}