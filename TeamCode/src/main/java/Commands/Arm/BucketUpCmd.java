package Commands.Arm;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.BucketSubsystem;

public class BucketUpCmd extends CommandBase {
    private BucketSubsystem bucket;
    boolean finish=false;

    public BucketUpCmd(BucketSubsystem bucket){
        this.bucket = bucket;
        addRequirements(bucket);
    }

    @Override
    public void execute() {
        finish=bucket.runBucketUp();
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return finish;
    }
}
