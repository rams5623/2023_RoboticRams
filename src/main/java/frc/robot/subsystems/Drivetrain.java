// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
// import com.kauailabs.navx.frc.AHRS;

// import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.driveConst;


public class Drivetrain extends SubsystemBase {
  /** Creates a new subsystem tilted Drivetrain. 
   * Will be used for controlling the motors and sensors related to the drive train.
   * These include the four drive motor controllers, the pigeon gyro, and the two drive encoders.
   * Robot Orientation:
   * ======================================================
   *                 Front
   *                +++++++
   *                   =
   *                   =
   *            ++++++++++++++++
   *            +              +
   * Left (/\+) +              + Right (\/+)
   *            +    Battery   +
   *            ++++++++++++++++
   * 
   *                  Rear
  */

  // Create motor controller objects for the drive motors
  private final WPI_TalonSRX m_talonFR = new WPI_TalonSRX(driveConst.ktalon_FR);
  private final WPI_TalonSRX m_talonFL = new WPI_TalonSRX(driveConst.ktalon_FL);
  private final WPI_TalonSRX m_talonRR = new WPI_TalonSRX(driveConst.ktalon_RR);
  private final WPI_TalonSRX m_talonRL = new WPI_TalonSRX(driveConst.ktalon_RL);

  private final PigeonIMU s_pidgey = new PigeonIMU(m_talonRL); // ###Change to the motor controller that this is plugged into
  // private final AHRS m_gyro = new AHRS(SPI.Port.kMXP); // NavX Gyro and Accelerometer in MXP port in center of Rio
  private double [] ypr_rot = new double [3]; // Gyro container to hold values for three axis angles
  private double [] xyz_rot = new double [3]; // Gyro container to hold values for three axis rotation rates
  private PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();

  public Drivetrain() {
    // Reset the talons to factory default to remove any configurations that may cause issues this time around
    m_talonFR.configFactoryDefault();
    m_talonFL.configFactoryDefault();
    m_talonRR.configFactoryDefault();
    m_talonRL.configFactoryDefault();

    // Set rear motor controllers to follow the front motor controller commands
    m_talonRR.follow(m_talonFR);
    m_talonRL.follow(m_talonFL);

    // Set one side of the robot to be inverse, followers will follow master.
    m_talonFL.setInverted(false);
    m_talonRL.setInverted(InvertType.FollowMaster);
    m_talonFR.setInverted(true); // Inverse right side because + command should produce forward movement
    m_talonRR.setInverted(InvertType.FollowMaster);

    // Set initial drive mode to Coast. Later operations in the match may switch this back and forth between brake and coast.
    m_talonFR.setNeutralMode(NeutralMode.Coast);
    m_talonFL.setNeutralMode(NeutralMode.Coast);
    m_talonRR.setNeutralMode(NeutralMode.Coast);
    m_talonRL.setNeutralMode(NeutralMode.Coast);

    // Set a deadband for motor control command. This will eliminate minor jostick drift from sending commands to the motor at null position
    m_talonFR.configNeutralDeadband(driveConst.kDeadbandLeft);
    m_talonFL.configNeutralDeadband(driveConst.kDeadbandLeft);
    m_talonRR.configNeutralDeadband(driveConst.kDeadbandRight);
    m_talonRL.configNeutralDeadband(driveConst.kDeadbandRight);

    // Nominal output percentage for forward and reverse when nothing is applied
    m_talonFL.configNominalOutputForward(0);
    m_talonFR.configNominalOutputForward(0);
    m_talonFL.configNominalOutputReverse(0);
    m_talonFR.configNominalOutputReverse(0);

    // Peak percentage output of controller for forward and reverse directions
    m_talonFL.configPeakOutputForward(0.9);
    m_talonFR.configPeakOutputForward(0.9);
    m_talonFL.configPeakOutputReverse(-0.9); // Peak Reverse NEED TO BE NEGATIVE!!!
    m_talonFR.configPeakOutputReverse(-0.9); // Peak Reverse NEED TO BE NEGATIVE!!!

    // Peak current limit trigger. When current goes above this for the defined time the current will be limited to the continous current limit.
    m_talonFL.configPeakCurrentLimit(16);
    m_talonFR.configPeakCurrentLimit(16);
    m_talonRL.configPeakCurrentLimit(16);
    m_talonRR.configPeakCurrentLimit(16);

    m_talonFL.configPeakCurrentDuration(1000);
    m_talonFR.configPeakCurrentDuration(1000);
    m_talonRL.configPeakCurrentDuration(1000);
    m_talonRR.configPeakCurrentDuration(1000);

    m_talonFL.configContinuousCurrentLimit(14); // What current is limited to after peak has been met
    m_talonFR.configContinuousCurrentLimit(14);
    m_talonRL.configContinuousCurrentLimit(14);
    m_talonRR.configContinuousCurrentLimit(14);

    // Enable/Disable current limiting feature with the above parameters
    m_talonFL.enableCurrentLimit(false);
    m_talonFR.enableCurrentLimit(false);
    m_talonRL.enableCurrentLimit(false);
    m_talonRR.enableCurrentLimit(false);

    // ENCODER SETUP
    // Attach encoder to master motor controllers (front)
    m_talonFL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonFR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

    m_talonFL.setSelectedSensorPosition(0.0);
    m_talonFR.setSelectedSensorPosition(0.0);

    // GYRO SETUP
    s_pidgey.configFactoryDefault();
    s_pidgey.setFusedHeading(0.0);
    s_pidgey.setTemperatureCompensationDisable(false);
    
    // 
  }

  /*
   * This will be the function used to drive the robot in both Percent and Position modes. No need to have the talon controllers
   * have the PID functions because we are not going to use any fancy MotionMagic driving this year with normal tank drive setup.
   * Encoder position will be used to determine the distance driven for things like auto mode.
   */
  public void drive(double forward, double rotate) {
    m_talonFL.set(ControlMode.PercentOutput, -forward, DemandType.ArbitraryFeedForward, rotate);
    m_talonFR.set(ControlMode.PercentOutput, -forward, DemandType.ArbitraryFeedForward, -rotate);
  }

  /* Completely stop the motors */
  public void stop() {
    m_talonFL.set(ControlMode.PercentOutput, 0.0);
    m_talonFR.set(ControlMode.PercentOutput, 0.0);
  }

  /* Set the drive motors to Coast. This is normal drive operating condition */
  public void setDriveCoast() {
    m_talonFL.setNeutralMode(NeutralMode.Coast);
    m_talonFR.setNeutralMode(NeutralMode.Coast);
    m_talonRL.setNeutralMode(NeutralMode.Coast);
    m_talonRR.setNeutralMode(NeutralMode.Coast);
  }

  /* Set the drive motors to Brake. This is for Auto mode and driving onto the charging platform */
  public void setDriveBrake() {
    m_talonFL.setNeutralMode(NeutralMode.Brake);
    m_talonFR.setNeutralMode(NeutralMode.Brake);
    m_talonRL.setNeutralMode(NeutralMode.Brake);
    m_talonRR.setNeutralMode(NeutralMode.Brake);
  }

  /*
   * ENCODER FUNCTIONS
   */
  public void resetEncoder() {
    m_talonFL.setSelectedSensorPosition(0.0);
    m_talonFR.setSelectedSensorPosition(0.0);
  }

  public double getLeftEncoder() {
    return m_talonFL.getSelectedSensorPosition() * driveConst.kEncDistancePerPulse;
  }

  public double getRightEncoder() {
    return m_talonFR.getSelectedSensorPosition() * driveConst.kEncDistancePerPulse;
  }

  public double getAvgEncoder() {
    return (getRightEncoder() + getLeftEncoder())/2;
  }
  
  /*
   * PIDGEON FUNCTIONS
   */
  public double getYaw() {
    /* This value tends to drift A LOT! Try not to use it. */
    return ypr_rot[0];
  }

  public double getYawRate() {
    /* Rate of change in Yaw. using this with the D in PID control will give a smoother response */
    return xyz_rot[0];
  }

  public double getPitch() {
    /* Pitch is the rotation around the X axis and will be used to determine if the charging platform is level */
    return ypr_rot[1];
  }

  public double getPitchRate() {
    /* Rate of change in pitch. Using this as a D in PID control will give a smoother response. */
    return xyz_rot[1];
  }
  
  public double getRoll() {
    /* Roll is the rotation around the Y axis */
    return ypr_rot[2];
  }

  public double getRollRate() {
    /* Rate of change in roll. Using this as a D in PID control will give a smoother response. */
    return xyz_rot[2];
  }
  
  public double getHeading() {
    /* Use this to get the "Yaw" rather than the getYaw. The getYaw has a lot of drift */
    s_pidgey.getFusedHeading(fusionStatus);
    return fusionStatus.heading;
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    s_pidgey.getYawPitchRoll(ypr_rot); // Gyro angle of rotation
    s_pidgey.getRawGyro(xyz_rot); // Gyro rate of rotation

    //Smartdahsboard Stuff
    SmartDashboard.putNumber("Raw Left Drive Encoder", getLeftEncoder());
    SmartDashboard.putNumber("Raw Right Drive Encoder", getRightEncoder());
    SmartDashboard.putNumber("Yaw", getHeading());
    SmartDashboard.putNumber("Pitch",getPitch());
    SmartDashboard.putNumber("Roll",getRoll());
    // When a NavX is installed uncomment below
    // SmartDashboard.putNumber("Gyro Yaw", m_gyro.getYaw());
    // SmartDashboard.putNumber("Gyro Roll", m_gyro.getRoll());
    // SmartDashboard.putNumber("Gyro Pitch", m_gyro.getPitch());
  }
}
