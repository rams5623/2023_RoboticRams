
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.boomConst;
import frc.robot.Constants.posConst;

public class Boom extends SubsystemBase {
  /** Creates the objects that will reside only in the Boom Subsystem */
  private final WPI_TalonSRX m_talonBoom = new WPI_TalonSRX(boomConst.ktalon_boom);
  private final DigitalInput s_boomSwitch = new DigitalInput(boomConst.kswitch_boom);

  public Boom() {
    // Reset the talon settings
    m_talonBoom.configFactoryDefault();

    // Motor controller settings
    m_talonBoom.setInverted(false); // Positive command to the motor controller should be a positive direction (Green blinking lights)
    m_talonBoom.setNeutralMode(NeutralMode.Brake); // Brake mode to prevent the boom from coasting downwards too much under its own weight
    m_talonBoom.configNeutralDeadband(boomConst.kDeadbandBoom);
    m_talonBoom.configNominalOutputForward(0.0);
    m_talonBoom.configNominalOutputReverse(0.0);
    m_talonBoom.configOpenloopRamp(0.2); // Smoothes out motor commands during normal operation
    m_talonBoom.configClosedloopRamp(0.3); // Smoothes out motor commands in PID Control Modes

    // Limit Max Command given to motor to prevent mechanism damage from going to fast
    m_talonBoom.configPeakOutputForward(0.75); // Max Forward Command Allowed
    m_talonBoom.configPeakOutputReverse(-0.75); // Max Reverse Command Allowed

    // Encoder Stuff
    m_talonBoom.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonBoom.setSensorPhase(boomConst.kSensorPhase);
    m_talonBoom.setSelectedSensorPosition(0.0); // On Robot start in starting config we want the sensor to read zero and not the home angle
    
    // PID for talon controller position
    m_talonBoom.selectProfileSlot(boomConst.kSlotidx, boomConst.kPIDidx);
    m_talonBoom.configAllowableClosedloopError(boomConst.kSlotidx, 0.0);
    m_talonBoom.config_kF(boomConst.kSlotidx, boomConst.kF);
    m_talonBoom.config_kP(boomConst.kSlotidx, boomConst.kP);
    m_talonBoom.config_kI(boomConst.kSlotidx, boomConst.kI);
    m_talonBoom.config_kD(boomConst.kSlotidx, boomConst.kD);

    // SoftLimit on Max Boom Height to prevent over extending
    m_talonBoom.configForwardSoftLimitThreshold(posConst.kMaxBoom * posConst.kBoomCountPerDegree); // [counts] = [degree] * [counts/degree]
    m_talonBoom.configForwardSoftLimitEnable(true);
  }

  /*
   * Use PID Control of Motor Controller to Move the Arm to the Given Angle
   * Since the operator has no control over this except to specify what angle to go to,
   * there is no bypass exception allowed once the switch has activated. This is done
   * in case a situation would occur where the switch is triggered but the PID is still
   * commanding the boom arm to lower further into the ground.
   */
  public void gotoPosition(double angle) {
    // if (getSwitch()) {
    //   stop()
    // } else {
    //   m_talonBoom.set(ControlMode.Position, angle * posConst.kBoomCountPerDegree);
    // }
    m_talonBoom.set(ControlMode.Position, angle * posConst.kBoomCountPerDegree); // [counts] = [degrees] * [counts / degrees]
  }
  
  /* 
   * Move the Boom Arm at a Variable Speed Depending on What is Provided to the Function.
   * Arbitrary Feed Forward is used to counteract the force of gravity pulling the arm down.
   * The feed forward value should be an equation that accounts for the arm position so that
   * 90° from the floor is the maximum amount of counteracting force given since that is the
   * arm position with the highest amount of moment from gravity. The additional weight of a
   * field element (Cone being the heaviest) should be accounted for as well.
   * A 'bypass' parameter is built in to allow for situations where the switch is triggered but
   * the boom arm must be lowered more. This is the function used fro operator control of the
   * arm so the they shold have the option to bypass the limits if necessary.
   */
  public void move(Double speed) {
    m_talonBoom.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, boomConst.karbitraryBoom); // Remove this line after testing the above section
  }
  /*public void move(Double speed, Boolean bypassSwitch) {
    if ((getSwitch() && (speed < 0.0)) && !bypassSwitch) {
       stop()
    } else {
       m_talonBoom.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, boomConst.karbitraryBoom);
    }
  }*/
  
  /*
   * Move Boom Arm in an Upwards Direction at a Constant Speed
   * Logic to prevent going too far up is not required for this since there isnt really a limit.
   */
  public void up() {
    m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_UP);  
  }
  
  /* 
   * Move Boom Arm in a Downwards Direction at a Constant Speed
   * Motor should stop when the lower limit switch is triggered to prevent under extending the arm
   * and pressing on the floor too much with the intake.
   */
  public void down() {
    // if (getSwitch()) {
    //   stop()
    // } else {
    //   m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_DOWN);
    // }
    m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_DOWN); // Remove this line after testing the above section
  }
  
  /*
   * Apply 0% Command to Boom Arm Motor to "Stop" It from Moving
   * Some drift downwards will occur as a result of gravity since no arbitrary feed forward is
   * being applied in this command. The desired affect is to completely stop commanding the
   * motor, rather than to just stop and hold it at a positon, so there should not be an arbitrary
   * feedforward providing additional output.
   */
  public void stop() {
    m_talonBoom.set(ControlMode.PercentOutput, 0);
  }
  
  /*
   * Reset the Encoder Value to the Home Position Angle
   * The encoder on the boom is converted to and from CCW degree angle from the negative z axis.
   * The boom arm is never fully zero in that nomenclature due to the interaction with the floor
   * so the home position angle is measured relative to the z axis and applied upon reset
   */
  public void resetEncoder() {
    m_talonBoom.setSelectedSensorPosition(posConst.kMinBoom * posConst.kBoomCountPerDegree); // [counts] = [degrees] * [counts/degree]
  }
  
  /*
   * Get the Current Encoder Count as an Angle
   * The sensor positon of the boom arm encoder is converted to an angle based on the derived
   * constant conversion. The function will output the angle with respect to the negative Z-axis
   * and not the total number of encoder counts.
   */
  public double getPosition() { // Change to getAngle() to better describe what the function does
    return (m_talonBoom.getSelectedSensorPosition() / posConst.kBoomCountPerDegree); // [degrees] = [counts] / [counts/degree]
  }
  
  /*
   * Get Current Boolean State of the Boom Switch
   * The switch is in a normally closed state and will read False when the arm is in the home
   * position. The desired response when calling the getSwitch function is for the home position
   * to read as 'True' and all other conditions to be 'False'.
   */
  public boolean getSwitch() {
    return !s_boomSwitch.get(); // Not(!) operator gets desired response of True at Home position
  }
  
  /*
   * Periodic Function
   * Called once per scheduler run. Great place for putting smartdashboard puts/gets so that every
   * desired value to be read on the dashboard can be updated regularly within the subsystem it resides.
   */
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Boom Zero Switch", getSwitch()); // [True/False]
    SmartDashboard.putNumber("Boom Angle", getPosition()); // [Degrees]
    SmartDashboard.putNumber("Boom Raw Encoder", getPosition() * posConst.kBoomCountPerDegree); // [Counts] = [Degrees] * [Counts/Degree]
    SmartDashboard.putNumber("Boom Current", m_talonBoom.getSupplyCurrent());// [Amps]
  }
}
