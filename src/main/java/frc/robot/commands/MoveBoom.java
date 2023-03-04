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
  /** Creates a new MoveBoom. */
  public MoveBoom(DoubleSupplier speed, Boom boom) {
    m_boom = boom;
    m_speed = speed;
    addRequirements(m_boom);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    m_boom.move(m_speed.getAsDouble());
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
