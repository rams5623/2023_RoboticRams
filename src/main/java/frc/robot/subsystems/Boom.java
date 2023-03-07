
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
    m_talonBoom.setInverted(false);
    m_talonBoom.setNeutralMode(NeutralMode.Brake);
    m_talonBoom.configNeutralDeadband(boomConst.kDeadbandBoom);
    m_talonBoom.configOpenloopRamp(0.2);
    m_talonBoom.configClosedloopRamp(0.3);
    m_talonBoom.configNominalOutputForward(0.0);
    m_talonBoom.configNominalOutputReverse(0.0);
    m_talonBoom.configPeakOutputForward(0.75);
    m_talonBoom.configPeakOutputReverse(-0.75);

    // Encoder Stuff
    m_talonBoom.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonBoom.setSensorPhase(boomConst.kSensorPhase);
    m_talonBoom.setSelectedSensorPosition(0.0);
    
    // PID for talon controller position
    m_talonBoom.selectProfileSlot(boomConst.kSlotidx, boomConst.kPIDidx);
    m_talonBoom.config_kF(boomConst.kSlotidx, boomConst.kF);
    m_talonBoom.config_kP(boomConst.kSlotidx, boomConst.kP);
    m_talonBoom.config_kI(boomConst.kSlotidx, boomConst.kI);
    m_talonBoom.config_kD(boomConst.kSlotidx, boomConst.kD);

    m_talonBoom.configForwardSoftLimitThreshold(posConst.kMaxBoom * kBoomCountPerDegree); // [counts] = [degree] * [counts/degree]
    m_talonBoom.configForwardSoftLimitEnable(true);
  }

  
  public void gotoPosition(double position) {
    // if (getSwitch()) {
    //   stop()
    // } else {
    //   m_talonBoom.set(ControlMode.Position, position);
    // }
    m_talonBoom.set(ControlMode.Position, position);
  }

  public void move(Double speed) {
    // if (getSwitch() && (speed < 0.0)) {
    //   stop()
    // } else {
    //   m_talonBoom.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, boomConst.karbitraryBoom);
    // }
    m_talonBoom.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, boomConst.karbitraryBoom);
  }
  
  public void up() {
    m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_UP);  
  }

  public void down() {
    // if (getSwitch()) {
    //   stop()
    // } else {
    //   m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_DOWN);
    // }
    m_talonBoom.set(ControlMode.PercentOutput, boomConst.SPEED_DOWN);
  }

  public void stop() {
    // Stop the motor input
    m_talonBoom.set(ControlMode.PercentOutput, 0);
  }

  public void resetEncoder() {
    m_talonBoom.setSelectedSensorPosition(kMinBoom * kBoomCountPerDegree); // [counts] = [degrees] * [counts/degree]
  }

  public double getPosition() {
    return (m_talonBoom.getSelectedSensorPosition() / kBoomCountPerDegree); // [degrees] = [counts] / [counts/degree]
  }

  public boolean getSwitch() {
    return !s_boomSwitch.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Boom Zero Switch", getSwitch());
    SmartDashboard.putNumber("Raw Boom Encoder", getPosition());
  }
}
