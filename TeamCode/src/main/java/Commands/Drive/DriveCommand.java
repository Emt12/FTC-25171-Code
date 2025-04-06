package Commands.Drive;

import com.arcrobotics.ftclib.command.CommandBase;

import java.util.function.DoubleSupplier;

import Subsystems.DriveSubsystem;

public class DriveCommand extends CommandBase {
    DriveSubsystem drive;
    DoubleSupplier lx, ly, rx;
    boolean fieldRelative;
    public DriveCommand(DoubleSupplier lx, DoubleSupplier ly, DoubleSupplier rx, DriveSubsystem drive){
        this.drive = drive;
        this.lx = lx;
        this.ly = ly;
        this.rx = rx;
        addRequirements(drive);
    }

    @Override
    public void execute() {
        drive.drive(lx.getAsDouble(), ly.getAsDouble(), rx.getAsDouble());
        super.execute();
    }

}
