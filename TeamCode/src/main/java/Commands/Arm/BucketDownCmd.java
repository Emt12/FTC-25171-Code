package Commands.Arm;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.BucketSubsystem;

public class BucketDownCmd extends CommandBase {
    private BucketSubsystem bucket;
    boolean finish = false;
    public BucketDownCmd(BucketSubsystem bucket){
        this.bucket = bucket;
        addRequirements(bucket);
    }

    @Override
    public void execute() {
        finish = bucket.runBucketDown();
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return finish;
    }

    @Override
    public void end(boolean interrupted) {
        bucket.stop();
    }
}
