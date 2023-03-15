package frc.robot.commands.autos;

public final class AutoConstants {
    /* UNFOLD AUTO COMMAND CONSTANTS */
    public static class unfoldConst {
        public static final double kColumnTimeout = 5.0; // [seconds] Timeout for moving the column to unfold position
        public static final double kColumnStartDelay = 4.5; // [seconds] Delay before moving column in order to prevent boom-column interaction
        public static final double kBoomTimeout = 14.0; // [seconds] Timeout for moving the boom to unfold position
    }

    public static class driveConst {
        public static final double kDriveP = 0.1;
        public static final double kDriveMaxSpeed = -0.5; // Percentage
        public static final double kDrivePos = -52; // Inches
        public static final double kDriveYawPgain = 0.1;
        public static final double kDriveYawDgain = 0.0001;
    }
}
