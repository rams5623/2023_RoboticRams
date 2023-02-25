// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class columnConst {
    // Talon CAN ID
    public static final int ktalon_column = 15;
    // Talon SRX command deadband zone
    public static final double kDeadbandColumn = 0.04;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // motor speed limiters
    public static final double SPEED_FORWARD = 0.8;
    public static final double SPEED_BACKWARD = 0.8;
    // Column rear limit switch for reseting encoder
    public static final int kswitch_column = 0;
  }

  public static class boomConst {
    // Talon CAN ID
    public static final int ktalon_boom = 16;
    // Talon SRX command deadband zone
    public static final double kDeadbandBoom = 0.04;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // Speed reducer commands
    public static final double SPEED_UP = 0.8;
    public static final double SPEED_DOWN = 0.8;
    // Boom rear limit switch for reseting encoder
    public static final int kswitch_boom = 1;
  }

  public static class intakeConst {
    // Talon CAN ID
    public static final int ktalon_intake = 17;
    // Talon SRX command deadband zone
    public static final double kDeadbandIntake = 0.04;
    // Speed reducer commands
    public static final double SPEED_IN = 0.8;
    public static final double SPEED_OUT = 0.8;
  }

  public static class clampConst {
    // Talon CAN ID
    public static final int ktalon_clamp = 18;
    // Talon SRX command deadband zone
    public static final double kDeadbandClamp = 0.04;
    // Speed reducer commands
    public static final double SPEED_CLAMP = 0.95;
    public static final double SPEED_RELEASE = 0.95;
  }

  public static class driveConst {
    // Talon CAN IDs
    public static final int ktalon_FR = 1;
    public static final int ktalon_FL = 2;
    public static final int ktalon_RR = 3;
    public static final int ktalon_RL = 4;
    // Pidgey 1.0 CAN ID
    public static final int kpidgey = 5;
    // Talon SRX command deadband zone
    public static final double kDeadbandLeft = 0.10;
    public static final double kDeadbandRight = 0.05;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // Speed reducer commands
    public static final double SPEED_TURN = 0.65;
    public static final double SPEED_STRT = -0.85;
  }
}
