
package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.autos.AutoConstants.balanceConst;
import frc.robot.subsystems.Drivetrain;

public class driveBalance extends CommandBase {
  private final Drivetrain drivetrain;
  private Timer m_timer = new Timer();
  private final Double leveledPitch;
  private double driveSpeed;
  private double turnSpeed;
  
  Double pitchTolerance;
  
  public driveBalance(Double initPitch, Drivetrain p_drivetrain) {
    drivetrain = p_drivetrain;
    leveledPitch = initPitch;
    addRequirements(drivetrain);
  }
  
  @Override
  public void initialize() {
    m_timer.start();
    m_timer.reset();
    drivetrain.resetEncoder();
    drivetrain.setDriveBrake();
  }

  @Override
  public void execute() {
    double errorYaw = drivetrain.getHeading();
    double currentAngle = drivetrain.getPitch();
    double errorAngle = currentAngle - leveledPitch;
    
    if (Math.abs(currentAngle) < balanceConst.kbalanceTolerance) { // Check to see if the robot is within balance tolerance
      // Dont move the robot if the pitch angle is within tolerance
      driveSpeed = 0.0;
      turnSpeed = 0.0;
      
    } else { // Robot is not within balance tolerance
      // Adjust the straight speed based on the magnitude of the difference in pitch angle
      driveSpeed = errorAngle * balanceConst.kdriveP + ((0.0) * balanceConst.kdriveD);
      
      // Limit the max allowable speed for the drive motors
      if (Math.abs(driveSpeed) > balanceConst.kMAX_SPEED) {
        driveSpeed = Math.copySign(balanceConst.kMAX_SPEED, driveSpeed); // Set the drive speed to the max speed with the ± sign of the driveSpeed
      }
      
      // TODO: Add yaw angle control to keep the robot going straight
      // Adjust the turn speed based on the magnitude of the heading angle
      turnSpeed = errorYaw * balanceConst.kyawP + ((0.0) * balanceConst.kyawD);
      // Limit the maximum turn speed allowed to be supplied to the drive motors
      if (Math.abs(turnSpeed) > balanceConst.kMAX_SPEED_TURN) {
        turnSpeed = Math.copySign(balanceConst.kMAX_SPEED_TURN, turnSpeed); // Set the drive speed to the max speed with the ± sign of the driveSpeed
      }
      // Sike! this isnt tested yet so make turn speed zero
      turnSpeed = 0.0; // Remove after testing
      
      // Reset the timer since the robot is not balancing yet
      m_timer.reset();
    }
    
    // Apply calculated straight and turn drive values to the drive motors
    drivetrain.drive(driveSpeed, turnSpeed);
  }
  
  // When the timer has surpassed the balance time allowed, end the command.
  @Override
  public boolean isFinished() { return false; }
  
  // End the command and stop applying power to the drive motors
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}
