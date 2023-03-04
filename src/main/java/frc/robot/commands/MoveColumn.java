// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Column;

public class MoveColumn extends CommandBase {
  private final Column m_column;
  private final DoubleSupplier m_speed;

  public MoveColumn(DoubleSupplier speed, Column column) {
    m_column = column;
    m_speed = speed;
    addRequirements(m_column);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Positive Xbox Axis is reverse
    if (m_column.getRevSwitch() && m_speed.getAsDouble() > 0.1) {
      m_column.stop();
    } else if (m_column.getFwdSwitch() && m_speed.getAsDouble() < -0.1) {
      m_column.stop();
    } else {
      m_column.move(m_speed.getAsDouble());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_column.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
