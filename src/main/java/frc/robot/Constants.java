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
  public static class controllerConst {
    // Deadband for controller axis inputs
    public static final double kDriveAxisZDeadband = 0.08;
    public static final double kDriveAxisYDeadband = 0.10;
    public static final double kOpAxisLeftYDeadband = 0.10;
    public static final double kOpAxisRightYDeadband = 0.10;
  }

  /*
   * Constants used for various positions of the boom and column
   */
  public static class posConst {
    // Pickup Position
    public static final int kPickupBoom = 0;
    public static final int kPickupColm = 1000; // Need actual value

    // Middle Poll Position
    public static final int kMidBoom = 1500;
    public static final int kMidColm = 400; // Need actual value

    // Top Poll Position
    public static final int kTopBoom = 3800;
    public static final int kTopColm = 500; // Need actual value

    // Floor Position
    public static final int kBotBoom = 100;

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

    // motor speed limiters (MOSTLY USED IN AUTO TO UNFOLD)
    public static final double SPEED_FORWARD = 0.5;
    public static final double SPEED_BACKWARD = 0.5;

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
    public static final int kEncoderCPR = 4096; //counts per rev
    public static final double kGearRatio = 1 / 200;
    public static final double kFloorAngle = 10; //degrees
    public static final double kMaxAngle = 150; //degrees
    public static final double kAnglePerCount = 0.0; //counts per angle
    
    // Speed reducer commands (MOSTLY USED IN AUTO TO UNFOLD)
    public static final double SPEED_UP = 0.5;
    public static final double SPEED_DOWN = 0.5;

    // Boom rear limit switch for reseting encoder
    public static final int kswitch_boom = 2;
  }

  public static class intakeConst {
    // Talon CAN ID
    public static final int ktalon_intake = 17;
    // Talon SRX command deadband zone
    public static final double kDeadbandIntake = 0.08;
    // Speed reducer commands
    public static final double SPEED_IN = 0.8;
    public static final double SPEED_OUT = 0.5;
  }

  public static class clampConst {
    // Talon CAN ID
    public static final int ktalon_clamp = 18;
    // Talon SRX command deadband zone
    public static final double kDeadbandClamp = 0.08;
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
    public static final int kpidgey = ktalon_RL; // Not on CAN so this doesnt apply. Only assign an INT ID number when wired directly to CAN

    // Talon SRX command deadband zone
    public static final double kDeadbandLeft = 0.15;
    public static final double kDeadbandRight = 0.10;
    // Encoder PID Stuff
    public static final int kSlotidx = 0;
    public static final int kPIDidx = 0;
    public static final double kLeftP = 2.0;
    public static final double kLeftI = 0.0;
    public static final double kLeftD = 0.0;
    // Position Math
    public static final int kEncoderCPR = 4096; // Stadard pulse/rev for ctre encoder
    public static final double kWheelDiameter = 6.01; // Inches
    public static final double kEncDistancePerPulse = 
        // Assumes encoders are mounted on wheel shafts
        (kWheelDiameter * Math.PI) / (double) kEncoderCPR; // inch per encoder count
        // For 6 inch wheels:
        // 1 Encoder Revolution = 4096 Encoder Counts = 1 Wheel Revolution = 18.84956 Travel Distance (Inches)
        // To use:
        // Have Counts but Need Inches - RawEncoder * kEncDistancePerPulse
        // Have Inches but Need Counts - Inches / kEncDistancePerPulse

    // Speed reducer commands
    public static final double SPEED_TURN = 0.65; // Max straight speed percentage (positive constant)
    public static final double SPEED_STRT = 0.85; // Max straight speed percentage (positive constant)
  }


  public static class a_UnfoldConst {
    public static final int kAutoUnfoldColumnPos = -500; // Convert to inches with equation in constants
    public static final double kAutoUnfoldColumnTime = 2.0; // seconds
    public static final double kAutoUnfoldColumnDelay = 2.0; // seconds
    public static final int kAutoUnfoldBoomPos = -7000; // Convert to inches with equation in constants
    public static final double kAutoUnfoldBoomTime = 5; // seconds
  }

  public static class a_ChargeConst {
    public static final double kAutoChargeDriveSpeed1 = 0.4; // percent
    public static final double kAutoChargeDrivePos1 = 8.0; // inches (Move closer to charging pad)
    public static final double kAutoChargeDriveTime1 = 2.0; // seconds (timeout to keep things moving)
    public static final double kAutoChargeUnfoldTime = 5.0; // seconds
    public static final double kAutoChargeUnfoldPos = -7500; // counts
    public static final double kAutoChargeDrivePos2 = 8.0; // inches (Move onto lowered charging pad)
    public static final double kAutoChargeDriveTime2 = 1.5; // seconds (timeout to keep things moving)
    public static final double kAutoChargePitch = 0.0; // desired pitch
    public static final double kAutoChargePitchPgain = 0.022;
    public static final double kAutoChargePitchDgain = 0.0005; // Zero is unused. Start with 10X less then P
    public static final double kAutoChargeYawPgain = 0.07;
    public static final double kAutoChargeYawDgain = 0.001;
  }

  public static class a_StrtConst {

  }
}
