package Commands.Arm;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.ElevatorSubsystem;

public class ElevatorCmd extends CommandBase {
    private ElevatorSubsystem elevator;
    private int m_setpoint;
    private boolean isFinished = false;

    public ElevatorCmd(ElevatorSubsystem elevator, int setpoint){
        this.m_setpoint=setpoint;
        this.elevator=elevator;
        addRequirements(elevator);
    }

    @Override
    public void execute(){
        elevator.setArmPower(1);
        elevator.setArmPosition(m_setpoint);
    }


    @Override
    public void end(boolean interrupted) {
        elevator.setArmPower(0);
        super.end(interrupted);
    }
}