/* 
 *
 * !!! DO NOT USE THIS COMMAND ANYMORE !!!
 * 
 * THE ROBOTCONTAINER WAS UPDATED TO RESTRUCTURE THE ARCADE DRIVE CONTROLS
 * AND REDUCE THE NUMBER OF FILES THAT ARE USED IN THE ROBOT CODE. THIS 
 * WILL BE REMOVED IN THE WEEK 2 COMPETITION BRANCH.
 * 
 */

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.driveConst;
import frc.robot.subsystems.Drivetrain;

public class ArcadeDrive extends CommandBase {
  private final Drivetrain m_drive;
  private final DoubleSupplier m_straight;
  private final DoubleSupplier m_turn;

  public ArcadeDrive(DoubleSupplier straight, DoubleSupplier turn, Drivetrain drive) {
    m_drive = drive;
    m_straight = straight;
    m_turn = turn;
    addRequirements(m_drive);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.drive(
      // Straight Double Parameter (Forward/Reverse)
      m_straight.getAsDouble() * driveConst.SPEED_STRT,
      // Turn Double Parameter (Rotate CW/CCW)
      m_turn.getAsDouble() * driveConst.SPEED_TURN
    );
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  // Decides if command should run during disabled
  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}
