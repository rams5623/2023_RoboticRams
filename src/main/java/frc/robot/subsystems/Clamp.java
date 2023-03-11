// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.clampConst;

public class Clamp extends SubsystemBase {
   /** Creates the objects that will reside only in the Clamp Subsystem */
  private final WPI_TalonSRX m_talonClamp = new WPI_TalonSRX(clampConst.ktalon_clamp);

  public Clamp() {
    m_talonClamp.configFactoryDefault();

    m_talonClamp.setInverted(false);
    m_talonClamp.setNeutralMode(NeutralMode.Brake);
    m_talonClamp.configNeutralDeadband(clampConst.kDeadbandClamp);
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
  }

  public void clamp() {
    m_talonClamp.set(ControlMode.PercentOutput, clampConst.SPEED_CLAMP);
  }

  public void hold() {
    m_talonClamp.set(ControlMode.PercentOutput, .5);
  }

  public void unclamp() {
    m_talonClamp.set(ControlMode.PercentOutput, -clampConst.SPEED_RELEASE);
  }

  public void stop() {
    m_talonClamp.set(ControlMode.PercentOutput, 0.0);
  }

  public double getMotorCurrent() {
    return m_talonClamp.getSupplyCurrent();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Clamp Current", m_talonClamp.getStatorCurrent());
  }
}
