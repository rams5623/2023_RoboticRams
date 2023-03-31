
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
  
  // ADD THESE TO AUTO CONSTANTS (Then add "balanceConst." in front of all the uses in this file)
  private static final double balanceTimeLimit = 5.0; // [Seconds] Time limit to stop command while within tolerance
  private static final double balanceTolerance = 2.5; // [Degrees] ± angular tolerance for balancing
  private static final double MAX_SPEED = 0.7; // [Percent] Max allowable drive speed
  private static final double MAX_SPEED_TURN = 0.3; // [Percent] Max allowable turn speed
  // \/ Lower this value if it is too fast when near balanced \/
  private static final double kdriveP = 0.0667; // Proportional Gain Constant for drive speed (Derived based on 20% speed @ an error of 3.0 deg)
  private static final double kdriveD = 0.0; // Derivative Gain Constant for drive speed
  private static final double kyawP = 0.0667; // Proportional Gain Constant for driving straight
  private static final double kyawD = 0.0; // Derivative Gain Constant for driving straight
  
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
    
    if (Math.abs(currentAngle) < balanceTolerance) { // Check to see if the robot is within balance tolerance
      // Dont move the robot if the pitch angle is within tolerance
      driveSpeed = 0.0;
      turnSpeed = 0.0;
      
    } else { // Robot is not within balance tolerance
      // Adjust the straight speed based on the magnitude of the difference in pitch angle
      driveSpeed = errorAngle * kdriveP + ((0.0) * kdriveD);
      
      // Limit the max allowable speed for the drive motors
      if (Math.abs(driveSpeed) > MAX_SPEED) {
        driveSpeed = Math.copySign(MAX_SPEED, driveSpeed); // Set the drive speed to the max speed with the ± sign of the driveSpeed
      }
      
      // TODO: Add yaw angle control to keep the robot going straight
      // Adjust the turn speed based on the magnitude of the heading angle
      turnSpeed = errorYaw * kyawP + ((0.0) * kyawD);
      // Limit the maximum turn speed allowed to be supplied to the drive motors
      if (Math.abs(turnSpeed) > MAX_SPEED_TURN) {
        turnSpeed = Math.copySign(MAX_SPEED_TURN, turnSpeed); // Set the drive speed to the max speed with the ± sign of the driveSpeed
      }
      // Sikes! this isnt tested yet so make turn speed zero
      turnSpeed = 0.0; // Remove after testing
      
      // Reset the timer since the robot is not balancing yet
      m_timer.reset();
    }
    
    // Apply calculated straight and turn drive values to the drive motors
    drivetrain.drive(driveSpeed, turnSpeed);
  }
  
  // When the timer has surpassed the balance time allowed, end the command.
  @Override
  public boolean isFinished() { return (m_timer.getFPGATimestamp() > balanceTimeLimit); }
  
  // End the command and stop applying power to the drive motors
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
  }
}
