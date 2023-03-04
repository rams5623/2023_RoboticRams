// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.columnConst;

public class Column extends SubsystemBase {
  /** Creates a new Column. */
  private final WPI_TalonSRX m_talonColumn = new WPI_TalonSRX(columnConst.ktalon_column);

  private final DigitalInput s_columnSwitchRev = new DigitalInput(columnConst.kswitchRev_column);
  private final DigitalInput s_columnSwitchFwd = new DigitalInput(columnConst.kswitchFwd_column);

  public Column() {
    // Reset the talon settings
    m_talonColumn.configFactoryDefault();

    // Motor controller settings
    m_talonColumn.setInverted(true);
    m_talonColumn.setNeutralMode(NeutralMode.Brake);
    m_talonColumn.configNeutralDeadband(columnConst.kDeadbandColumn);
    m_talonColumn.configOpenloopRamp(0.2);

    // Encoder Stuff
    m_talonColumn.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonColumn.setSensorPhase(false);
    m_talonColumn.configNominalOutputForward(0.0);
    m_talonColumn.configNominalOutputReverse(0.0);
    m_talonColumn.configPeakOutputForward(0.9);
    m_talonColumn.configPeakOutputReverse(-0.9);
    m_talonColumn.selectProfileSlot(columnConst.kSlotidx, columnConst.kPIDidx);
    m_talonColumn.config_kF(columnConst.kSlotidx, 0.0);
    m_talonColumn.config_kP(columnConst.kSlotidx, 0.0);
    m_talonColumn.config_kI(columnConst.kSlotidx, 0.0);
    m_talonColumn.config_kD(columnConst.kSlotidx, 0.0);
    m_talonColumn.setSelectedSensorPosition(0.0);
    //m_talonColumn.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
  }

  public void gotoPosition(double position) {
    m_talonColumn.set(ControlMode.Position, position);
  }

  public void move(double speed) {
    m_talonColumn.set(ControlMode.PercentOutput, speed); 
  }
  
  public void forward() {
    m_talonColumn.set(ControlMode.PercentOutput, columnConst.SPEED_FORWARD);
  }

  public void reverse() {
    m_talonColumn.set(ControlMode.PercentOutput, columnConst.SPEED_BACKWARD);
  }

  public void stop() {
    m_talonColumn.set(ControlMode.PercentOutput, 0);
  }

  public void resetEncoder() {
    m_talonColumn.setSelectedSensorPosition(0);
  }

  public double getPosition() {
    return m_talonColumn.getSelectedSensorPosition();
  }

  public boolean getRevSwitch() {
    return !s_columnSwitchRev.get();
  }
  
  public boolean getFwdSwitch() {
    return !s_columnSwitchFwd.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Column Reverse Switch", getRevSwitch());
    SmartDashboard.putBoolean("Column Forward Switch", getFwdSwitch());
    SmartDashboard.putNumber("Raw Column Encoder", getPosition());
  }
}
