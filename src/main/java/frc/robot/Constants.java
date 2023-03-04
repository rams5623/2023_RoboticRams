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

  /*
   * Constants used for various positions of the boom and column
   */
  public static class posConst {
    // Pickup Position
    public static final int kPickupBoom = 0;
    public static final int kPickupColm = 1000;

    // Middle Poll Position
    public static final int kMidBoom = 1500;
    public static final int kMidColm = 400;

    // Top Poll Position
    public static final int kTopBoom = 3800;
    public static final int kTopColm = 500;

    // Floor Position
    public static final int kBotBoom = 200;

    // MAX BOOM HEIGHT AT FULL COLUMN FORWARD
    public static final int kMaxBoom = 4300;
  }


  public static class columnConst {
    // Talon CAN ID
    public static final int ktalon_column = 15;
    // Talon SRX command deadband zone
    public static final double kDeadbandColumn = 0.10;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // Position Math
    public static final int kEncoderCPR = 4096;
    public static final double kTravelDist = 11.5625; // Full travel from rev switch to fwd switch
    public static final int kTravelCount = 100;
    public static final double kEncDistancePerPulse = 0.0;

    // motor speed limiters
    public static final double SPEED_FORWARD = 0.6;
    public static final double SPEED_BACKWARD = 0.6;

    // Column rear limit switch for reseting encoder
    public static final int kswitchRev_column = 0;
    public static final int kswitchFwd_column = 1;
  }

  public static class boomConst {
    // Talon CAN ID
    public static final int ktalon_boom = 16;
    // Talon SRX command deadband zone
    public static final double kDeadbandBoom = 0.20;
    // Talon SRX Arbitrarty Feedback for Gravity
    public static final double karbitraryBoom = -0.08;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // Position Math
    public static final int kEncoderCPR = 4096;
    public static final double kEncDistancePerPulse = 0.0;
    
    // Speed reducer commands
    public static final double SPEED_UP = 0.5;
    public static final double SPEED_DOWN = 0.3;
    // Boom rear limit switch for reseting encoder
    public static final int kswitch_boom = 2;
  }

  public static class intakeConst {
    // Talon CAN ID
    public static final int ktalon_intake = 17;
    // Talon SRX command deadband zone
    public static final double kDeadbandIntake = 0.04;
    // Speed reducer commands
    public static final double SPEED_IN = 0.8;
    public static final double SPEED_OUT = 0.5;
  }

  public static class clampConst {
    // Talon CAN ID
    public static final int ktalon_clamp = 18;
    // Talon SRX command deadband zone
    public static final double kDeadbandClamp = 0.04;
    // Speed reducer commands
    public static final double SPEED_CLAMP = 0.7;
    public static final double SPEED_RELEASE = 0.7;
  }

  public static class driveConst {
    // Talon CAN IDs
    public static final int ktalon_FR = 14;
    public static final int ktalon_FL = 13;
    public static final int ktalon_RR = 12;
    public static final int ktalon_RL = 11;
    
    // Pidgey 1.0 CAN ID
    public static final int kpidgey = ktalon_RL;
    //public static final boolean kisPigOnCAN = false;

    // Talon SRX command deadband zone
    public static final double kDeadbandLeft = 0.15;
    public static final double kDeadbandRight = 0.10;

    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    // Position Math
    public static final int kEncoderCPR = 4096; // Stadard pulse/rev for ctre encoder
    public static final double kWheelDiameter = 6.01; // Inches
    public static final double kEncDistancePerPulse = 
        // Assumes encoders are mounted on wheel shafts
        (kWheelDiameter * Math.PI) / (double) kEncoderCPR; // inch per encoder count
        // For 6 inch wheels:
        // 1 Encoder Revolution = 4096 Encoder Counts = 1 Wheel Revolution = 18.84956 Travel Distance (Inches)

    // Speed reducer commands
    public static final double SPEED_TURN = 0.65;
    public static final double SPEED_STRT = -0.85;
  }
}
