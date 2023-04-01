package frc.robot.commands.autos;

public final class AutoConstants {
    /* UNFOLD AUTO COMMAND CONSTANTS */
    public static class unfoldConst {
        public static final double kColumnTimeout = 5.0; // [seconds] Timeout for moving the column to unfold position
        public static final double kColumnStartDelay = 2.4; // [seconds] Delay before moving column in order to prevent boom-column interaction
        public static final double kBoomTimeout = 6.5; // [seconds] Timeout for moving the boom to unfold position
    }

    public static class driveConst {
        public static final double kMaxSpeed = 0.50; // [Percentage]
        public static final double kDrivePos = 85; // [Inches]
        public static final double kDrivePosDiff = 1.0; // [Inches]
        public static final double kDrivePgain = .05;
        public static final double kDriveDgain = 0.0;
        public static final double kYawPgain = 0.1;
        public static final double kYawDgain = 0.0001;
    }

    public static class balanceConst {
        public static final double kdriveP = 0.0527; // Proportional Gain Constant for drive speed (Derived based on 20% speed @ an error of 3.0 deg)
        public static final double kdriveI = 0.000;
        public static final double kdriveD = 0.0; // Derivative Gain Constant for drive speed
        public static final double kyawP = 0.0667; // Proportional Gain Constant for driving straight
        public static final double kyawI = 0.0;
        public static final double kyawD = 0.0; // Derivative Gain Constant for driving straight
        
        public static final double kunfoldTImeout = 4.0;

        public static final double kbalanceTimeLimit = 6.0; // [Seconds] Time limit to stop command while within tolerance
        public static final double kbalanceTolerance = 1.0; // [Degrees] ± angular tolerance for balancing
        public static final double kMAX_SPEED = 0.365; // [Percent] Max allowable drive speed
        public static final double kMAX_SPEED_TURN = 0.3; // [Percent] Max allowable turn speed
    }

    public static class cubeFloorConst {
        public static final double kunfoldTimeout = 4.5;
        public static final double kunclampTimeout = 2.0;
        public static final double kdriveDist = -140.0;
        public static final double kdriveTimeout = 7.0;
        
    }
}
