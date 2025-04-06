package Commands.Auto;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.pathgen.Path;

import Subsystems.DriveSubsystem;

public class UpdateCmd extends CommandBase {
    DriveSubsystem drive;


    public UpdateCmd(DriveSubsystem drive) {
        this.drive = drive;
        addRequirements(drive);
    }

    @Override
    public void execute() {
        drive.autoUpdate();
        super.execute();
    }
}
