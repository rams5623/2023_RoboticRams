
package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.autos.AutoConstants.driveConst;
import frc.robot.subsystems.Drivetrain;

public class driveDistance extends CommandBase {
  private final Drivetrain drivetrain;
  private final Boolean driveGyro;
  private final Double dist;
  private Double PIDError;
  private Double PIDOut;
  private Double rotate;

  public driveDistance(Double p_dist, Boolean p_driveGyro, Drivetrain p_drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    drivetrain = p_drivetrain;
    driveGyro = p_driveGyro;
    dist = p_dist;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetEncoder();
    drivetrain.setDriveBrake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    PIDError = drivetrain.getAvgEncoder(); //[Inches]
    PIDOut = (dist - PIDError) * driveConst.kDrivePgain;
    
    if (Math.abs(PIDOut) > driveConst.kMaxSpeed) {
      PIDOut = Math.copySign(driveConst.kMaxSpeed, PIDOut);
    }
    
    if (driveGyro) {
      rotate = ((drivetrain.getHeading() - 0.0) * driveConst.kYawPgain) - (drivetrain.getYawRate() * driveConst.kYawDgain);
    } else {
      rotate = 0.0;
    }

    drivetrain.drive(PIDOut, rotate);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(dist - PIDError) < driveConst.kDrivePosDiff) {
      return true;
    } else {
      return false;
    }
  }
}
