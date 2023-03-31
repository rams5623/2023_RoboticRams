
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.columnConst;
import frc.robot.Constants.posConst;

public class Column extends SubsystemBase {
  /** Creates the objects that will reside only inthe Column Subsystem */
  private final WPI_TalonSRX m_talonColumn = new WPI_TalonSRX(columnConst.ktalon_column);
  private final DigitalInput s_columnSwitchRev = new DigitalInput(columnConst.kswitchRev_column);
  private final DigitalInput s_columnSwitchFwd = new DigitalInput(columnConst.kswitchFwd_column);

  public Column() {
    // Reset the talon settings
    m_talonColumn.configFactoryDefault();

    // Motor controller settings
    m_talonColumn.setInverted(false); // Positive command to the motor controller should be a positive direction (green lights blinking)
    m_talonColumn.setNeutralMode(NeutralMode.Brake); // Brake mode to prevent the column from coasting in either direction under load from the boom
    m_talonColumn.configNeutralDeadband(columnConst.kDeadbandColumn);
    m_talonColumn.configNominalOutputForward(0.0);
    m_talonColumn.configNominalOutputReverse(0.0);
    m_talonColumn.configOpenloopRamp(0.4); // Smoothes out motor commands during normal operation
    m_talonColumn.configClosedloopRamp(0.2); // Smoothes out motor commands in PID Control Modes

    // Limit Max Command given to motor to prevent mechanism damage from going to fast
    m_talonColumn.configPeakOutputForward(0.50);
    m_talonColumn.configPeakOutputReverse(-0.50);
    
    // Encoder Stuff
    m_talonColumn.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonColumn.setSensorPhase(true); // Add as constant (columnConst.kSensorPhase);
    m_talonColumn.selectProfileSlot(columnConst.kSlotidx, columnConst.kPIDidx);
    m_talonColumn.configAllowableClosedloopError(columnConst.kSlotidx, 0.0);
    m_talonColumn.config_kF(columnConst.kSlotidx, columnConst.kF);
    m_talonColumn.config_kP(columnConst.kSlotidx, columnConst.kP);
    m_talonColumn.config_kI(columnConst.kSlotidx, columnConst.kI);
    m_talonColumn.config_kD(columnConst.kSlotidx, columnConst.kD);
<<<<<<< HEAD
    m_talonColumn.configClosedLoopPeakOutput(columnConst.kPIDidx, 0.65);
    m_talonColumn.setSelectedSensorPosition(-posConst.kFoldColm * posConst.kColmCountPerInch);
  }

  /*
   * Adjust Position or Set Speed on The fly to Control the Arm
   * Combines the functions of gotoPosition and move into one function that may provide
   * less janky movements from the ControlBoom command. This function is to only be
   * used with the ControlBoom command.
   */
  public void motorControl(ControlMode mode, double output, boolean bypassSwitch) {
    if ((mode == ControlMode.PercentOutput && (((getFwdSwitch() && (output > 0.0)) || (getRevSwitch() && (output < 0.0))) && !bypassSwitch))
      || (mode == ControlMode.Position && (getFwdSwitch() && (output > 7.5)))
    ) { // OR gotoPosition() conditions are true
      // Don't move below the limit switch
      stop();
    } else if (mode == ControlMode.PercentOutput) {
      // Otherwise move desired speed or to position
      m_talonColumn.set(mode, output);
    } else if (mode == ControlMode.Position) {
      m_talonColumn.set(mode, output * posConst.kColmCountPerInch); // [counts] = [inches] * [Count / inch]
    }
    // SmartDashboard.putNumber("Column Control Output", output);
  }


  /*
=======
    m_talonColumn.setSelectedSensorPosition(0.0);
  }

  /*
>>>>>>> parent of 7e0b667 (Merge pull request #11 from Robotic-Rams-5623/mid-season-dev-boom)
   * Use PID Control of Motor Controller to Move the column to the Given Position
   * Since the operator has no control over this except to specify what position to go to,
   * there is no bypass exception allowed once the switch has activated. This is done
   * in case a situation would occur where the switch is triggered but the PID is still
   * commanding the column leg to move further into the gears.
   * 
   * TODO: ADD & TEST THE LIMIT SWITCH RESTRICTION FOR AUTO POSITIONING
   */
  public void gotoPosition(double position) {
    // if (getRevSwitch() || getFwdSwitch()) {
    //   stop();
    // } else {
    //   m_talonColumn.set(ControlMode.Position, position);
    // };
    m_talonColumn.set(ControlMode.Position, position * posConst.kColmCountPerInch); // [Counts] = [Inches] * [Counts/Inch]
  }

  /* 
   * Move the Column Arm at a Variable Speed Depending on What is Provided to the Function.
   * A 'bypass' parameter is built in to allow for situations where the switches are triggered
   * but the column arm must be moved forward or reverse more. This is the function used for operator
   * control of the arm so the they shold have the option to bypass the limits if necessary.
   */
  public void move(Double speed, Boolean bypassSwitch) {
    if (((getFwdSwitch() && (speed > 0.0)) || (getRevSwitch() && (speed < 0.0))) && !bypassSwitch) {
      stop();
    } else {
      m_talonColumn.set(ControlMode.PercentOutput, speed); 
    }
  }
  
  /*
   * Move Column in a Forward Direction at a Constant Speed
   * Logic to prevent this from going to far and brekaing the slide mechanism is required
   * since there isnt really a programmable limit.
   */
  public void forward() {
    m_talonColumn.set(ControlMode.PercentOutput, columnConst.SPEED_FORWARD);
  }
  
  /* 
   * Move Column in a Backwards Direction at a Constant Speed
   * Logic to prevent this from going to far and breaking the slide mechanism is required
   * since there isnt really a programmable limit.
   */
  public void reverse() {
    m_talonColumn.set(ControlMode.PercentOutput, -columnConst.SPEED_BACKWARD);
  }
  
  /*
   * Apply 0% Command to Column Arm Motor to "Stop" It from Moving
   * Since brake mode is on and there isnt a gravity aspect to this part of the mechanism
   * there won't be much drift at all occuring naturally. The desired affect of this function
   * is to stop sending a command to the motor so there shoudnt be any compensation on it.
   */
  public void stop() {
    m_talonColumn.set(ControlMode.PercentOutput, 0);
  }
  
  /*
   * Reset the Encoder Value to the Home Position Distane
   * The encoder on the column is converted to and from inches of positon from the home point.
   */
  public void resetEncoder() {
    m_talonColumn.setSelectedSensorPosition(0);
  }

  /*
   * Get the Current Encoder Count as a Position in Inches
   * The sensor positon of the column arm encoder is converted to position based on the derived
   * constant conversion. The function will output the position in inches with respect to translation on the Y-axis
   * and not the total number of encoder counts.
   */
  public double getPosition() {
    return (m_talonColumn.getSelectedSensorPosition() / posConst.kColmCountPerInch); // [Inches] = [Counts] / [Counts/Inch]
  }

  /*
   * Get Current Boolean State of the Reverse Column Switch
   * The switch is in a normally closed state and will read False when the arm is in the home
   * position. The desired response when calling the getRevSwitch function is for the home position
   * to read as 'True' and all other conditions to be 'False'.
   */
  public boolean getRevSwitch() {
    return !s_columnSwitchRev.get();
  }
  
  /*
   * Get Current Boolean State of the Forward Column Switch
   * The switch is in a normally closed state and will read False when the arm is in the home
   * position. The desired response when calling the getFwdSwitch function is for the home position
   * to read as 'True' and all other conditions to be 'False'.
   */
  public boolean getFwdSwitch() {
    return !s_columnSwitchFwd.get();
  }

  /*
   * Periodic Function
   * Called once per scheduler run. Great place for putting smartdashboard puts/gets so that every
   * desired value to be read on the dashboard can be updated regularly within the subsystem it resides.
   */
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Column Reverse Switch?", getRevSwitch()); // [True/False]
    SmartDashboard.putBoolean("Column Forward Switch?", getFwdSwitch()); // [True/False]
    SmartDashboard.putNumber("Column Position", getPosition()); // [Inches]
    // SmartDashboard.putNumber("Column Raw Encoder", getPosition() * posConst.kColmCountPerInch); // [Counts] = [Inches] * [Counts/Inch]
    // SmartDashboard.putNumber("Column Current", m_talonColumn.getSupplyCurrent()); // [Amps]
  }
}
