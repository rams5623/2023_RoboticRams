
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.GlobalVariables;
import frc.robot.RobotContainer;
import frc.robot.Constants.posConst;
import frc.robot.Constants.boomConst.boomPosition;
import frc.robot.subsystems.Boom;

public class BoomControl extends CommandBase {
  private final Boom m_boom;
  private final RobotContainer m_controls;
  private final GlobalVariables m_variables;

  private Boolean isManual;

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

    // Assign the left stick value to a variable so the function isnt called multiple times
    Double axisValue = m_controls.getOpLeftStickY();

    // If there is any value from the left stick y axis then set override mode
    if (Math.abs(axisValue) > 0.0) { isManual = true; }
    else { isManual = false; }

    // If manual overide is true or mode is manual then left op stick
    if (isManual || (m_boomPosition == boomPosition.MANUAL)) { 
      m_boom.move(axisValue, false);
    }
    // Home position is the limit switch positions
    else if (m_boomPosition == boomPosition.HOME) {
      m_boom.gotoPosition(posConst.kMinBoom);
    }
    // Stow position is a safe spot for the arm to be in during travel
    else if (m_boomPosition == boomPosition.STOW) {
      m_boom.gotoPosition(posConst.kStowBoom);
    }
    // Drop the piece off on the floor
    else if (m_boomPosition == boomPosition.FLOOR) {
      m_boom.gotoPosition(posConst.kBotBoom);
    }
    // Drop the piece off on the middle pole or shelf
    else if (m_boomPosition == boomPosition.MIDDLE) {
      m_boom.gotoPosition(posConst.kMidBoom);
    }
    // Drop the piece off on the Top pole or shelf
    else if (m_boomPosition == boomPosition.TOP) {
      m_boom.gotoPosition(posConst.kTopBoom);
    }
    // Maybe will never use this but the position is a valid point
    else if (m_boomPosition == boomPosition.FOLD) {
      m_boom.gotoPosition(posConst.kFoldBoom);
    }
    // Not a valid mode for the boom arm to be in
    else {
      m_boom.stop();
    }
  }

  /*
   * Nothing happens when the command starts.
   */
  @Override
  public void initialize() {}

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
