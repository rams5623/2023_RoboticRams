// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Boom;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(Intake intake) {
    return Commands.sequence(new InstantCommand(intake::stop, intake));
  }

  // Fill in autos here I guess instead of in the RobotContainer
  public static CommandBase driveStraightAuto(Drivetrain drive, Boom boom) {
    return Commands.parallel(
    new StartEndCommand(() -> drive.drive(.4, 0), drive::stop, drive).withTimeout(2.0),
    new StartEndCommand(boom::down,boom::stop,boom).withTimeout(2)
  );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
