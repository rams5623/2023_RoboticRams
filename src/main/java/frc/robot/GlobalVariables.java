package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.boomConst.boomPosition;
import frc.robot.Constants.columnConst.columnPosition;

public class GlobalVariables extends SubsystemBase {
  private boomPosition BoomPosition = boomPosition.MANUAL;
  private columnPosition ColumnPosition = columnPosition.MANUAL;
  private Boolean SwitchBypass = false;
  
  public GlobalVariables() {}
  
  // pull the current boom position setpoint
  public boomPosition getBoomPosition() {
    return BoomPosition;
  }
  
  // set the current boom posiiton setpoint
  public void setBoomPosition(boomPosition p_BoomPosition) {
    BoomPosition = p_BoomPosition;
  }
  
  // pull the current column position setpoint
  public columnPosition getColumnPosition() {
    return ColumnPosition;
  }
  
  // set the current column position setpoint
  public void setColumnPosition(columnPosition p_ColumnPosition) {
    ColumnPosition = p_ColumnPosition;
  }

  // pull the current limit switch bypass state
  public Boolean getSwitchBypass() {
    return SwitchBypass;
  }

  // set the current limit switch bypass state
  public void setSwitchBypass(Boolean p_bypassSwitch) {
    SwitchBypass = p_bypassSwitch;
  }
}
