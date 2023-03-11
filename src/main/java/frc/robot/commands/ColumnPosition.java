// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Column;

public class ColumnPosition extends CommandBase {
  /** Creates a new ColumnPosition. */
  private final Column m_column;
  private final Double m_position;

  public ColumnPosition(Double position, Column column) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_column = column;
    m_position = position;
    addRequirements(m_column);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_column.gotoPosition(m_position);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_column.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((m_column.getPosition() > (m_position + 0.3)) && (m_column.getPosition()) < (m_position - 0.3)) {
      return true;
    } else {
      return false;
    }
  }
}
