package Commands.Elevator;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.ElevatorSubsystem;

public class ElevatorControlUpCmd extends CommandBase {
    private ElevatorSubsystem elevator;
    private int m_setpoint;
    private boolean isFinished = false;
    double kP = 5, kI = 0, kD = 0, kF=0;

    public ElevatorControlUpCmd(ElevatorSubsystem elevator, int setpoint){
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
}