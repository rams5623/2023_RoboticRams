
package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  private ControlMode talonMode;
  private double output;

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

    // If there is any value from the left stick y axis then set override mode
    if (Math.abs(m_controls.getOpRightStickY()) > 0.0) { m_columnPosition = columnPosition.MANUAL; }

    // If manual overide is true or mode is manual then left op stick
    if (m_columnPosition == columnPosition.MANUAL) { 

      SmartDashboard.putString("Column Position Setpoint", "MANUAL");
      m_variables.setColumnPosition(columnPosition.MANUAL); 
    }
    // Home position is the limit switch positions
    else if (m_columnPosition == columnPosition.HOME) {
      talonMode = ControlMode.Position;
      output = posConst.kMinColm;
      SmartDashboard.putString("Column Position Setpoint", "HOME");
    }
    // Stow position is a safe spot for the arm to be in during travel
    else if (m_columnPosition == columnPosition.STOW) {
      talonMode = ControlMode.Position;
      output = posConst.kStowColm;
      SmartDashboard.putString("Column Position Setpoint", "STOW");
    }
    // Drop the piece off on the floor
    else if (m_columnPosition == columnPosition.FLOOR) {
      talonMode = ControlMode.Position;
      output = posConst.kBotColm;
      SmartDashboard.putString("Column Position Setpoint", "FLOOR");
    }
    // Drop the piece off on the middle pole or shelf
    else if (m_columnPosition == columnPosition.MIDDLE) {
      talonMode = ControlMode.Position;
      output = posConst.kMidColm;
      SmartDashboard.putString("Column Position Setpoint", "MIDDLE");
    }
    // Drop the piece off on the Top pole or shelf
    else if (m_columnPosition == columnPosition.TOP) {
      talonMode = ControlMode.Position;
      output = posConst.kTopColm;
      SmartDashboard.putString("Column Position Setpoint", "TOP");
    }
    // Maybe will never use this but the position is a valid point
    else if (m_columnPosition == columnPosition.FOLD) {
      talonMode = ControlMode.Position;
      output = posConst.kFoldColm;
      SmartDashboard.putString("Column Position Setpoint", "FOLD");
    }
    // Not a valid mode for the Column arm to be in
    else {
      m_column.stop();
    }
    m_column.motorControl(talonMode, output, m_variables.getSwitchBypass());
  }

  /*
   * Nothing happens when the command starts.
   */
  @Override
  public void initialize() {
    m_variables.setColumnPosition(columnPosition.MANUAL);
  }

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
