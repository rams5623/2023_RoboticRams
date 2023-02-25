// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeConst;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private final WPI_TalonSRX m_talonIntake = new WPI_TalonSRX(intakeConst.ktalon_intake);

  public Intake() {
    m_talonIntake.configFactoryDefault();

    m_talonIntake.setInverted(false);
    m_talonIntake.setNeutralMode(NeutralMode.Brake);
    m_talonIntake.configNeutralDeadband(intakeConst.kDeadbandIntake);
  }

  public void intake() {
    m_talonIntake.set(ControlMode.PercentOutput, intakeConst.SPEED_IN);
  }

  public void outake() {
    m_talonIntake.set(ControlMode.PercentOutput, intakeConst.SPEED_OUT);
  }

  public void stop() {
    m_talonIntake.set(ControlMode.PercentOutput, 0);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
