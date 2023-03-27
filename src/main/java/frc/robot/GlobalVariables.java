package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.boomConst.boomPosition;
import frc.robot.Constants.columnConst.columnPosition;

public class GlobalVariables extends SubsystemBase {
  private boomPosition BoomPosition;
  private columnPosition ColumnPosition;
  
  public GlobalVariables() {}
  
  // pull the current boom position setpoint
  public boomPosition getBoomPosition() {
    return BoomPosition;
  }
  
  // set the boom posiiton setpoint
  public setBoomPosition(boomPosition p_BoomPosition) {
    BoomPosition = p_BoomPosition;
  }
  
  // pull the current column position setpoint
  public columnPosition getColumnPosition() {
    return ColumnPosition;
  }
  
  // set the current column position setpoint
  public setColumnPosition(p_ColumnPosition) {
    ColumnPosition = p_ColumnPosition;
  }
}
