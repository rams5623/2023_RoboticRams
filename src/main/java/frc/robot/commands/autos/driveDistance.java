// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.autos.AutoConstants.driveConst;
import frc.robot.subsystems.Drivetrain;

public class driveDistance extends CommandBase {
  private final Drivetrain drivetrain;
  private final Boolean driveStraight;
  private final Double dist;
  private Double PIDError;
  private Double PIDOut;

  public driveDistance(Double p_dist, Boolean p_driveStraight, Drivetrain p_drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    drivetrain = p_drivetrain;
    driveStraight = p_driveStraight;
    dist = p_dist;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // PIDError = drivetrain.getAvgEncoder(); //[Inches]
    // PIDOut = (dist - PIDError) * driveConst.kDriveP;
    // 
    // if (PIDOut > driveConst.kDriveMaxSpeed) {
    //   PIDOut = driveConst.kDriveMaxSpeed;
    // }
    // 
    // drivetrain.drive(
    //   PIDOut,
    //   0.0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // drivetra
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return false;
    return true; //return false;
  }
}
