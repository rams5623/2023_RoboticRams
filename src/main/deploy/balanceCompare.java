/**
 * ==========================
 *   VARIOUS BALANCE AUTOS
 * ==========================
**/
/*

// TEAM 6002 (Zoobotix) - JAVA:
public class CMD_AdjustBalance extends CommandBase {
  SUB_Drivetrain m_drivetrain;
  Timer m_timer = new Timer();
  double m_timeLimit;
  public CMD_AdjustBalance(SUB_Drivetrain p_drivetrain) {
    m_drivetrain = p_drivetrain;
  }
  @Override
  public void initialize() {
    m_timer.start();
    m_timer.reset();
    if (Math.abs(m_drivetrain.getRoll()) < 5){
      m_timeLimit = Math.abs(m_drivetrain.getRoll() * 0.04);  
    } else{ 
    m_timeLimit = Math.abs(m_drivetrain.getRoll() * 0.07);  
    }
  }
  @Override
  public void execute() {
      m_drivetrain.drive(Math.copySign(0.17, m_drivetrain.getRoll()), 0, 0, true, false);
    // drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean rateLimit)
  }
  @Override
  public boolean isFinished() {
    return (m_timer.get() > m_timeLimit);
  }
}


// TEAM 6153 (Blue Crew Robotics) - CPP:
Initialize() {
  m_timer.reset();
  m_levelPitchValue = m_drivetrain->getPitchLevelValue();
  m_finished = false;
}
Execute() {
  double currentPitch = m_drivetrain->getPitch();
  double pitchTolerance = 1.5;
  double speed = 0.0;
  speed = 0.0;
  m_drivetrain->SetRamp(0.5)
  if (currentPitch > m_levelPitchValue + pitchTolerance) { speed = 0.14; }
  else if (currentPitch < m_levelPitchValue - pitchTolerance) { speed = -0.14; }
  else { m_timer.Start(); }
  
  if (m_timer.HasElapsed(2.0)) { m_finsihed = true; } // Timer has elapsed 2 seconds
  
  m_drivetrain ->driveStraight(speed);
}
End() {}
IsFinished() { return m_finsihed }


// TEAM #### ()
private final Drivetrain drivetrain;
private final PIDController controller = new PIDController(0.5,0,0);
private final double tolerance = 4;
controller.enableContinuousInput(-90,90);
controller.setTolerance(tolerance);
controller.setSetpoint(0);

initialize() {
  drivetrain.lockWheels();
}
execute() {
  double yInput;
  yInput = controller.calculate(drivetrain.getPitch, 0);
  drive(yInput);
}
end() {
  drive(0);
  drivetrain.unlockWheels();
}


// TEAM 2877 (Ligerbots)
private static final double BALANCED_ERROR_DEGREES = 2.5; // max error for what counts as balanced
private static final double BALANCED_DEGREES = 0;
private static final double BALANCE_KP = 0.035; // WPI 2023-03-15
private static final double MAX_MPS = 1.0;
private static final double BALANCE_SECONDS = 1; // how many seconds the robot has to be balanced before stopping
private static final double ANGLE_KP = Units.degreesToRadians(1); // = 0.017453 radians
private static final double MAX_ANGLE_SPEED = Units.degreesToRadians(10); // = 0.174533 radians
private Drivetrain m_drivetrain;
private final Timer m_timer = new Timer();
initialize() {
  m_timer.reset();
  m_timer.start();
}
execute() {
  double tiltAngle = m_driveTrain.getTiltDegrees();
  double driveMPS, angleSpeed;
  double errorDegrees = tiltAngle - BALANCED_DEGREES;
  Rotation2d driveAngle = m_driveTrain.getTiltDirection();
  if (Math.abs(tiltAngle) < 7.5) { driveMPS = 0.0; angleSpeed = 0.0; }
  else {
    driveMPS = errorDegrees * BALANCE_KP; // Use angle to set speed
    if (Math.abs(driveMPS) > MAX_MPS) { driveMPS = Math.copySign(MAX_MPS, driveMPS); } // Cap Max Speed
    m_drivetrain.drive(driveMPS); // Set speed
    if (Math.abs(errorDegrees) >= BALANCED_ERROR_DEGREES) { m_timer.reset(); }
  }
}
end() { m_timer.stop(); }
isFinished() { return m_timer.hasElapsed(BALANCE_SECONDS); }

*/
