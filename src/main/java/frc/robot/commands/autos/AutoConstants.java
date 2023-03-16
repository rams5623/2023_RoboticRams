package frc.robot.commands.autos;

public final class AutoConstants {
    /* UNFOLD AUTO COMMAND CONSTANTS */
    public static class unfoldConst {
        public static final double kColumnTimeout = 5.0; // [seconds] Timeout for moving the column to unfold position
        public static final double kColumnStartDelay = 4.5; // [seconds] Delay before moving column in order to prevent boom-column interaction
        public static final double kBoomTimeout = 14.0; // [seconds] Timeout for moving the boom to unfold position
    }

    public static class driveConst {
        public static final double kMaxSpeed = -0.5; // [Percentage]
        public static final double kDrivePos = -52; // [Inches]
        public static final double kDrivePosDiff = 1.0; // [Inches]
        public static final double kDrivePgain = 0.1;
        public static final double kDriveDgain = 0.0;
        public static final double kYawPgain = 0.1;
        public static final double kYawDgain = 0.0001;
    }

    public static class balanceConst {
        public static final double kP = 0.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kminInput = -15.0;
        public static final double kmaxInput = 15.0;
        public static final double kpitchTol = 0.5;
        public static final double krateTol = 0.01;
    }
}
