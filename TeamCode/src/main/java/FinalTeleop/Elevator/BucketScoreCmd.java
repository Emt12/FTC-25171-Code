package FinalTeleop.Elevator;

import com.arcrobotics.ftclib.command.CommandBase;

import Subsystems.BucketSubsystem;

public class BucketScoreCmd extends CommandBase {
    private BucketSubsystem bucket;
    boolean finish=false;

    public BucketScoreCmd(BucketSubsystem bucket){
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

    @Override
    public void end(boolean interrupted) {
        bucket.runBucketDown();
        super.end(interrupted);
    }
}
