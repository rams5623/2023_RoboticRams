
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.GlobalVariables;
import frc.robot.RobotContainer;
import frc.robot.Constants.posConst;
import frc.robot.Constants.boomConst.boomPosition;
import frc.robot.subsystems.Boom;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class BoomControl extends CommandBase {
  private final Boom m_boom;
  private final RobotContainer m_controls;
  private final GlobalVariables m_variables;
  
  private ControlMode talonMode;
  private double motorOut;

  public BoomControl(Boom p_boom, RobotContainer p_controls, GlobalVariables p_variables) {
    // Assign variables to objects
    m_boom = p_boom;
    m_controls = p_controls;
    m_variables = p_variables;
    addRequirements(m_boom);
  }

  /*
   * Will execute once every scheduler loop. Execute loop controls how
   * the boom arm is controled whether manually or with the position
   * control. Manual control will always override the automatic
   * positioning.
   */
  @Override
  public void execute() {
    // Get the current desired postion or control mode of the boom arm from the globals
    boomPosition m_boomPosition = m_variables.getBoomPosition();

    // If there is any value from the left stick y axis then set override mode
    if (Math.abs(m_controls.getOpLeftStickY()) > 0.0) { m_boomPosition = boomPosition.MANUAL; }    
    
    // If manual overide is true or mode is manual then left op stick
    if (m_boomPosition == boomPosition.MANUAL) { 
      talonMode = ControlMode.PercentOutput;
      motorOut = m_controls.getOpLeftStickY();
      SmartDashboard.putString("Boom Position Setpoint", "MANUAL");
      m_variables.setBoomPosition(boomPosition.MANUAL); 
    }
    // Home position is the limit switch positions
    else if (m_boomPosition == boomPosition.HOME) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kMinBoom;
      SmartDashboard.putString("Boom Position Setpoint", "HOME");
    }
    // Stow position is a safe spot for the arm to be in during travel
    else if (m_boomPosition == boomPosition.STOW) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kStowBoom;
      SmartDashboard.putString("Boom Position Setpoint", "STOW");
    }
    // Drop the piece off on the floor
    else if (m_boomPosition == boomPosition.FLOOR) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kBotBoom;
      SmartDashboard.putString("Boom Position Setpoint", "FLOOR");
    }
    // Drop the piece off on the middle pole or shelf
    else if (m_boomPosition == boomPosition.MIDDLE) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kMidBoom;
      SmartDashboard.putString("Boom Position Setpoint", "MIDDLE");
    }
    // Drop the piece off on the Top pole or shelf
    else if (m_boomPosition == boomPosition.TOP) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kTopBoom;
      SmartDashboard.putString("Boom Position Setpoint", "TOP");
    }
    // Maybe will never use this but the position is a valid point
    else if (m_boomPosition == boomPosition.FOLD) {
      talonMode = ControlMode.Position;
      motorOut = posConst.kFoldBoom;
      SmartDashboard.putString("Boom Position Setpoint", "FOLD");
    }
    // Not a valid mode for the boom arm to be in
    else {
      m_boom.stop();
    }
    m_boom.motorControl(talonMode, motorOut, m_variables.getSwitchBypass());
  }

  /*
   * Nothing happens when the command starts.
   */
  @Override
  public void initialize() {
    m_variables.setBoomPosition(boomPosition.MANUAL);
  }

  /* 
   * Called once the command ends or is interrupted.
   * If the command ends just stop the boom motor. It will stop at the end
   * of the match when the robot gets disabled so stop it.
   */ 
  @Override
  public void end(boolean interrupted) { m_boom.stop(); }

  /*
   * isFinished will always be false for this command because it will run once
   * every scheduler loop to control the boom arm.
   */
  @Override
  public boolean isFinished() { return false; }
}
