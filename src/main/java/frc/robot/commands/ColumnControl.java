
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.GlobalVariables;
import frc.robot.RobotContainer;
import frc.robot.Constants.posConst;
import frc.robot.Constants.columnConst.columnPosition;
import frc.robot.subsystems.Column;

public class ColumnControl extends CommandBase {
  private final Column m_column;
  private final RobotContainer m_controls;
  private final GlobalVariables m_variables;

  private Boolean isManual;

  public ColumnControl(Column p_column, RobotContainer p_controls, GlobalVariables p_variables) {
    // Assign variables to objects
    m_column = p_column;
    m_controls = p_controls;
    m_variables = p_variables;
    addRequirements(m_column);
  }

  /*
   * Will execute once every scheduler loop. Execute loop controls how
   * the column arm is controled whether manually or with the position
   * control. Manual control will always override the automatic
   * positioning.
   */
  @Override
  public void execute() {
    // Get the current desired postion or control mode of the column arm from the globals
    columnPosition m_columnPosition = m_variables.getColumnPosition();

    // Assign the left stick value to a variable so the function isnt called multiple times
    Double axisValue = m_controls.getOpRightStickY();

    // If there is any value from the left stick y axis then set override mode
    if (Math.abs(axisValue) > 0.0) { isManual = true; }
    else { isManual = false; }

    // If manual overide is true or mode is manual then left op stick
    if (isManual || (m_columnPosition == columnPosition.MANUAL)) { 
      m_column.move(axisValue, m_variables.getSwitchBypass());
    }
    // Home position is the limit switch positions
    else if (m_columnPosition == columnPosition.HOME) {
      m_column.gotoPosition(posConst.kMinColm);
    }
    // Stow position is a safe spot for the arm to be in during travel
    else if (m_columnPosition == columnPosition.STOW) {
      m_column.gotoPosition(posConst.kStowColm);
    }
    // Drop the piece off on the floor
    else if (m_columnPosition == columnPosition.FLOOR) {
      m_column.gotoPosition(posConst.kBotColm);
    }
    // Drop the piece off on the middle pole or shelf
    else if (m_columnPosition == columnPosition.MIDDLE) {
      m_column.gotoPosition(posConst.kMidColm);
    }
    // Drop the piece off on the Top pole or shelf
    else if (m_columnPosition == columnPosition.TOP) {
      m_column.gotoPosition(posConst.kTopColm);
    }
    // Maybe will never use this but the position is a valid point
    else if (m_columnPosition == columnPosition.FOLD) {
      m_column.gotoPosition(posConst.kFoldColm);
    }
    // Not a valid mode for the Column arm to be in
    else {
      m_column.stop();
    }
  }

  /*
   * Nothing happens when the command starts.
   */
  @Override
  public void initialize() {}

  /* 
   * Called once the command ends or is interrupted.
   * If the command ends just stop the Column motor. It will stop at the end
   * of the match when the robot gets disabled so stop it.
   */ 
  @Override
  public void end(boolean interrupted) { m_column.stop(); }

  /*
   * isFinished will always be false for this command because it will run once
   * every scheduler loop to control the Column arm.
   */
  @Override
  public boolean isFinished() { return false; }
}
