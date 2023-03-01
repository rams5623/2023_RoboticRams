// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ArcadeDrive extends CommandBase {
  private final Drivetrain m_drive;
  private final DoubleSupplier m_straight;
  private final DoubleSupplier m_turn;

  public ArcadeDrive(DoubleSupplier straight, DoubleSupplier turn, Drivetrain drive) {
    m_drive = drive;
    m_straight = straight;
    m_turn = turn;
    addRequirements(m_drive);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //m_drive.drive(m_straight.getAsDouble(), m_turn.getAsDouble());

    // Straight speed adjustment
    if (m_straight.getAsDouble() < 0.1 && m_straight.getAsDouble() > -0.1) {
      m_drive.drive(0.0, m_turn.getAsDouble());
    } else if (m_straight.getAsDouble() > 0.1) {
      m_drive.drive(m_straight.getAsDouble(), m_turn.getAsDouble()+0.15);
    } else if (m_straight.getAsDouble() < -0.1) {
      m_drive.drive(m_straight.getAsDouble(), m_turn.getAsDouble()-0.05);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
