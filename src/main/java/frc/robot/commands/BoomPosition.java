// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Boom;

public class BoomPosition extends CommandBase {
  /** Creates a new BoomPosition. */
  private final Boom m_boom;
  private final Double m_angle;
  
  
  public BoomPosition(Double angle, Boom boom) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_boom = boom;
    m_angle = angle;
    addRequirements(m_boom);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_boom.gotoPosition(m_angle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_boom.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_boom.getPosition() > (m_angle + 2) && m_boom.getPosition() < (m_angle - 2)) {
      return true;
    } else {
      return false;
    }
  }
}
