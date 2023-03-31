
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.clampConst;

/*
 * CLAMP NOMENCLATURE
 * 
 * (-) Negative Command Given to the TalonSRX (Red Blinking Lights):
 * DROP = RELEASED = OPEN = UPWARDS = UNCLAMPED
 * 
 * (+) Positive Command Given to the TalonSRX: (Green Blinking Lights):
 * PICKUP = GRABBED = CLOSED = DOWNWARDS = CLAMPED
 * 
 */
public class Clamp extends SubsystemBase {
   /** Creates the objects that will reside only in the Clamp Subsystem */
  private final WPI_TalonSRX m_talonClamp = new WPI_TalonSRX(clampConst.ktalon_clamp);
   
  public Clamp() {
    m_talonClamp.configFactoryDefault();

    m_talonClamp.setInverted(false);
    m_talonClamp.setNeutralMode(NeutralMode.Brake);
    m_talonClamp.configNeutralDeadband(clampConst.kDeadbandClamp);
     //m_talonClamp.enableDeadbandElimination(true); // DOES THIS WORK? IS THIS WHY THE DEADBAND NEVER SEEMS TO WORK BECAUSE IT WAS NEVER ENABLED?!?!?!?!? WHY ISNT IT ENABLED BY DEFAULT!?!?!?
    m_talonClamp.configOpenloopRamp(0.0);
    m_talonClamp.configClosedloopRamp(0.0);

    // Current limiting on the clamp to avoid too much draw
    //m_talonClamp.configPeakCurrentLimit(7);
    //m_talonClamp.configPeakCurrentDuration(500);
    //m_talonClamp.configContinuousCurrentLimit(6);
    //m_talonClamp.enableCurrentLimit(false);

    // ADD CURRENT CLOSED LOOP CONTROL HERE
    // PID for talon controller position
    // m_talonClamp.selectProfileSlot(clampConst.kSlotidx, clampConst.kPIDidx);
    // m_talonClamp.configAllowableClosedloopError(clampConst.kSlotidx, 0.5);
    // m_talonClamp.config_kF(clampConst.kSlotidx, clampConst.kF);
    // m_talonClamp.config_kP(clampConst.kSlotidx, clampConst.kP);
    // m_talonClamp.config_kI(clampConst.kSlotidx, clampConst.kI);
    // m_talonClamp.config_kD(clampConst.kSlotidx, clampConst.kD);

    m_talonClamp.configNominalOutputForward(0.0);
    m_talonClamp.configNominalOutputReverse(0.0);
    m_talonClamp.configPeakOutputForward(clampConst.SPEED_CLAMP);
    m_talonClamp.configPeakOutputReverse(-clampConst.SPEED_CLAMP);

    hold(true);
  }

  // TODO: WHAT DIRECTION IS ACTUALLY CLAMP AND UNCLAMP!!!!!!
   
  /* 
   * Move Clamp in a Downwards Direction at a Constant Speed
   * This is considered the positive power direction for the motor controller (Green blinking lights).
   * A limit of sorts (positional or current) should be applied to restrict the absolute movement in
   * software so that the bag motor does not burn out or destroy any attached mechanisms.
   */
  public void clamp() {
    m_talonClamp.set(ControlMode.PercentOutput, clampConst.SPEED_CLAMP);
  }
   
  /* 
   * Hold the Clamp Opened or Closed
   * Using a constant percent output or by current PID, hold the clamp open or closed based on the given
   * input parameter.
   * TODO: MORE FINE TUNING IS REQUIRED TO IMPLEMENT A CURRENT RESTRICTED PID AS THE WAY TO HOLD THE CLAMP
   */
  public void hold(boolean isClamped) {
    if (isClamped) {
       // Hold in the clamped direction (Positive input)
       m_talonClamp.set(ControlMode.PercentOutput, clampConst.SPEED_HOLD_CLAMP);
    } else {
       // Hold in the unclamped direction (Negative input)
       m_talonClamp.set(ControlMode.PercentOutput, -clampConst.SPEED_HOLD_OPEN);
    }
  }

  /* 
   * Move Clamp in an Upwards Direction at a Constant Speed
   * This is considered the negative power direction for the motor controller (Red blinking lights).
   * A limit of sorts (positional or current) should be applied to restrict the absolute movement in
   * software so that the bag motor does not burn out or destroy any attached mechanisms.
   */
  public void unclamp() {
    m_talonClamp.set(ControlMode.PercentOutput, -clampConst.SPEED_RELEASE);
  }

  /* 
   * Apply 0% Command to Clamp Motor to "Stop" it from Moving
   * The desired affect of this function is to stop sending a command to the motor. As a result there
   * should not be any feedforward compensation or percent of power output applied at all when this
   * is called.
   */
  public void stop() {
    m_talonClamp.set(ControlMode.PercentOutput, 0.0);
  }

  /* 
   * Get The Amount of Current Supplied to the Motor
   * The getStatorCurrent will provide a double value of the current supplied downstream of the TalonSRX.
   * On the flip side, the getSupplyCurrent will provide a double value of the current supplied to the
   * TalonSRX from the PDP.
   */
  public double getMotorCurrent() {
    return m_talonClamp.getStatorCurrent();
  }

  /* 
   * RUNS ONCE EVERY PROCESS LOOP OF THE SCHEDULER
   */
  @Override
  public void periodic() {
   SmartDashboard.putNumber("Clamp Current", m_talonClamp.getStatorCurrent());
  }
}
