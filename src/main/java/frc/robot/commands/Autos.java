// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.Constants.driveConst;
import frc.robot.subsystems.Boom;
import frc.robot.subsystems.Column;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup; 
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
  /** Example static factory for an autonomous command. */

  public static CommandBase exampleAuto(Intake intake) {
    return Commands.sequence(new InstantCommand(intake::stop, intake));
  }

  public static CommandBase unfoldAuto(Boom boom, Column column) {
    final int kAutoUnfoldColumnPos = -500; // Convert to inches with equation in constants
    final double kAutoUnfoldColumnTime = 2.0; // seconds
    final double kAutoUnfoldColumnDelay = 2.0; // seconds
    final int kAutoUnfoldBoomPos = -7000; // Convert to inches with equation in constants
    final double kAutoUnfoldBoomTime = 5; // seconds

    return Commands.sequence(
      new ParallelCommandGroup(
        /* Column Movement */
        new FunctionalCommand(
          // Reset column encoder on command start
          column::resetEncoder,
          // Move column reverse back to the zero position at start of command
          () -> column.reverse(),
          // When command is interrupted on its end stop the column motor
          interrupted -> column.stop(),
          // Iterrupt this command when the column has reached the desired start position or hit the rev switch
          () -> (column.getPosition() <= kAutoUnfoldColumnPos) || (column.getRevSwitch()),
          // Requires the coumn subsystem to run this command
          column
        ).withTimeout(kAutoUnfoldColumnTime).beforeStarting(new WaitCommand(kAutoUnfoldColumnDelay)),

        /* Boom Movement */
        new FunctionalCommand(
          // Reset column encoder on command start
          boom::resetEncoder,
          // Move column forward at start of command
          () -> boom.down(),
          // When command is interrupted on its end stop the column motor
          interrupted -> boom.stop(),
          // Iterrupt this command when the column has reached the desired start position
          () -> (boom.getPosition() <= kAutoUnfoldBoomPos) || (boom.getSwitch()),
          // Requires the coumn subsystem to run this command
          boom
        ).withTimeout(kAutoUnfoldBoomTime)
      )
    );
  }

  public static CommandBase driveChargingStation(Drivetrain drive, Boom boom, Column column) {
    final double kAutoChargeDriveSpeed1 = 0.4; // percent
    final double kAutoChargeDrivePos1 = 8.0; // inches (Move closer to charging pad)
    final double kAutoChargeDriveTime1 = 2.0; // seconds (timeout to keep things moving)
    final double kAutoChargeUnfoldTime = 5.0; // seconds
    final double kAutoChargeUnfoldPos = -7500; // counts
    final double kAutoChargeDrivePos2 = 8.0; // inches (Move onto lowered charging pad)
    final double kAutoChargeDriveTime2 = 1.5; // seconds (timeout to keep things moving)
    final double kAutoChargePitch = 0.0; // desired pitch
    final double kAutoChargePitchPgain = 0.022;
    final double kAutoChargePitchDgain = 0.0005; // Zero is unused. Start with 10X less then P
    final double kAutoChargeYawPgain = 0.07;
    final double kAutoChargeYawDgain = 0.001;
    /* 
     * 1) Drive the robot up to the charging station from auto start position.
     * 2a) Unfold the boom (rotate down) and use it to press down on the charging station.
     * 2b) Boom should stop when extra current is used or when the boom switch is triggered.
     * 3) Drive forward (straight) onto the charging station while still pressing down with boom.
     * 4) Use PID on drive using pitch of gyro to level out the charging pad.
     */
    return Commands.sequence(
      new SequentialCommandGroup(
        new InstantCommand(drive::setDriveBrake,drive).withTimeout(0.2),
        /* (1)-(2) Drive a little closer to the pad while lowering boom down*/
        new FunctionalCommand(
          // Reset drive encoders on command start
          drive::resetEncoder,
          // Move drive forward a little at start of command
          () -> drive.drive(kAutoChargeDriveSpeed1, 0.0),
          // When command is interrupted on its end stop the drive motors
          interrupted -> drive.stop(),
          // Iterrupt this command when the drive reaches the desired position
          () -> (drive.getAvgEncoder()*driveConst.kEncDistancePerPulse >= kAutoChargeDrivePos1), // Inches
          // Requires the coumn subsystem to run this command
          drive
        ).withTimeout(kAutoChargeDriveTime1).alongWith(
          /* (2) Unfold Boom */
          new FunctionalCommand(
            // Reset column encoder on command start
            boom::resetEncoder,
            // Move column forward at start of command
            () -> boom.down(),
            // When command is interrupted on its end stop the column motor
            interrupted -> boom.stop(),
            // Iterrupt this command when the column has reached the desired start position
            () -> (boom.getPosition() <= kAutoChargeUnfoldPos) || (boom.getSwitch()), // Counts
            // Requires the coumn subsystem to run this command
            boom
          ).withTimeout(kAutoChargeUnfoldTime)
        ),
        /* (3) Drive up on the pad a little bit */
        new FunctionalCommand(
          // Reset drive encoders on command start
          drive::resetEncoder,
          // Move drive forward a little at start of command
          () -> drive.drive(kAutoChargeDriveSpeed1, 0.0),
          // When command is interrupted on its end stop the drive motors
          interrupted -> drive.stop(),
          // Iterrupt this command when the drive reaches the desired position
          () -> ((drive.getAvgEncoder()*driveConst.kEncDistancePerPulse) >= kAutoChargeDrivePos2), // Inches
          // Requires the coumn subsystem to run this command
          drive
        ).withTimeout(kAutoChargeDriveTime2),
        /* (4) Drive straight and level off the pad */
        new FunctionalCommand(
          // Reset drive encoders on command start
          drive::resetEncoder,
          // Move drive forward a little at start of command
          () -> drive.drive(
            // Straight speed based on pitch (P & D Control Loop)
            (drive.getPitch() - kAutoChargePitch) * kAutoChargePitchPgain - (drive.getPitchRate()) * kAutoChargePitchDgain,
            // Turn adjustment to account for robot drift and keep robot driving straight (P & D Control Loop)
            (drive.getHeading() - 0.0) * kAutoChargeYawPgain - (drive.getYawRate()) * kAutoChargeYawDgain
          ),
          // When command is interrupted on its end stop the drive motors
          interrupted -> drive.stop(),
          // Iterrupt this command when the drive reaches the desired position
          () -> (drive.getPitch() <= 2.0) && (drive.getPitch() >= -2.0),
          // Requires the coumn subsystem to run this command
          drive
        )
      )
    );
  }

  // Fill in autos here I guess instead of in the RobotContainer
  public static CommandBase driveStraightAuto(Drivetrain drive, Boom boom) {
    final double kAutoStrtSpeed = 0.5; // Percentage
    final double kAutoStrtPos = 36; // Inches
    final double kAutoStrtYawPgain = 0.0;
    final double kAutoStrtYawDgain = 0.0;

    return Commands.sequence(
      /*  */
      new FunctionalCommand(
        // Reset drive encoders on command start
        drive::resetEncoder,
        // Move drive forward a little at start of command
        () -> drive.drive(
          // Straight speed based on pitch (P & D Control Loop)
          kAutoStrtSpeed,
          // Turn adjustment to account for robot drift and keep robot driving straight (P & D Control Loop)
          (drive.getHeading() - 0.0) * kAutoStrtYawPgain - (drive.getYawRate()) * kAutoStrtYawDgain
        ),
        // When command is interrupted on its end stop the drive motors
        interrupted -> drive.stop(),
        // Iterrupt this command when the drive reaches the desired position
        () -> (drive.getAvgEncoder()*driveConst.kEncDistancePerPulse) >= kAutoStrtPos, // Inches
        // Requires the coumn subsystem to run this command
        drive
      )
  );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
