// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Boom;

public class MoveBoom extends CommandBase {
  private final Boom m_boom;
  private final DoubleSupplier m_speed;
  private final Boolean m_bypass;
  /** Creates a new MoveBoom. */
  public MoveBoom(DoubleSupplier speed, Boolean bypass, Boom boom) {
    m_boom = boom;
    m_speed = speed;
    m_bypass = bypass;
    addRequirements(m_boom);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_speed.getAsDouble() < 0.15 && m_speed.getAsDouble() > -0.15) {
      m_boom.move(0.0, m_bypass);
    } else if (m_speed.getAsDouble() > 0.1 || m_speed.getAsDouble() < -0.05) {
      m_boom.move(m_speed.getAsDouble(), m_bypass);
    }

    //m_boom.move(m_speed.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
