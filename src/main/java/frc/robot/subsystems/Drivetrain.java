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
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;

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

  private final PigeonIMU s_pidgey = new PigeonIMU(m_talonRL); // ###Change to the motor controller that this is plugged into###
  private double [] ypr_rot = new double [3];
  private PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();

  public Drivetrain() {
    //Reset the talons to factory default to remove any configurations that may cause issues this time around
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

    // ENCODER SETUP
    // Attach encoder to master motor controllers (front)
    m_talonFL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    m_talonFR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

    m_talonFL.configNominalOutputForward(0);
    m_talonFR.configNominalOutputForward(0);
    m_talonFL.configNominalOutputReverse(0);
    m_talonFR.configNominalOutputReverse(0);

    m_talonFL.configPeakOutputForward(0.9);
    m_talonFR.configPeakOutputForward(0.9);
    m_talonFL.configPeakOutputReverse(-0.9); // Peak Reverse NEED TO BE NEGATIVE!!!
    m_talonFR.configPeakOutputReverse(-0.9);

    m_talonFL.selectProfileSlot(driveConst.kSlotidx, driveConst.kPIDidx);
    m_talonFR.selectProfileSlot(driveConst.kSlotidx, driveConst.kPIDidx);

    m_talonFL.config_kF(driveConst.kSlotidx, 0.9);
    m_talonFL.config_kP(driveConst.kSlotidx, 0.0);
    m_talonFL.config_kI(driveConst.kSlotidx, 0.0);
    m_talonFL.config_kD(driveConst.kSlotidx, 0.0);

    m_talonFR.config_kF(driveConst.kSlotidx, 0.9);
    m_talonFR.config_kP(driveConst.kSlotidx, 0.0);
    m_talonFR.config_kI(driveConst.kSlotidx, 0.0);
    m_talonFR.config_kD(driveConst.kSlotidx, 0.0);

    m_talonFL.setSelectedSensorPosition(0.0);
    m_talonFR.setSelectedSensorPosition(0.0);

    // Pigeon IMU Sensor Settings
    s_pidgey.configFactoryDefault();
    s_pidgey.setFusedHeading(0.0);
    s_pidgey.setTemperatureCompensationDisable(false);
    //s_pidgey.enterCalibrationMode(CalibrationMode.Temperature);
  }


  public void drive(double forward, double rotate) {
    m_talonFL.set(ControlMode.PercentOutput, (forward * driveConst.SPEED_STRT), DemandType.ArbitraryFeedForward, rotate * driveConst.SPEED_TURN);
    m_talonFR.set(ControlMode.PercentOutput, (forward * driveConst.SPEED_STRT), DemandType.ArbitraryFeedForward, -rotate * driveConst.SPEED_TURN);
  }

  public void stop() {
    m_talonFL.set(ControlMode.PercentOutput, 0.0);
    m_talonFR.set(ControlMode.PercentOutput, 0.0);
  }

  public void setDriveCoast() {
    m_talonFL.setNeutralMode(NeutralMode.Coast);
    m_talonFR.setNeutralMode(NeutralMode.Coast);
    m_talonRL.setNeutralMode(NeutralMode.Coast);
    m_talonRR.setNeutralMode(NeutralMode.Coast);
  }

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
    return m_talonFL.getSelectedSensorPosition();
  }

  public double getRightEncoder() {
    return m_talonFR.getSelectedSensorPosition();
  }

  public double getAvgEncoder() {
    return (getRightEncoder() + getLeftEncoder())/2;
  }
  
  /*
   * PIDGEON FUNCTIONS
   */
  public double getYaw() {
    return ypr_rot[0];
  }

  public double getPitch() {
    return ypr_rot[1];
  }
  
  public double getHeading() {
    s_pidgey.getFusedHeading(fusionStatus);
    return fusionStatus.heading;
  }


  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    s_pidgey.getYawPitchRoll(ypr_rot);

    //Smartdahsboard Stuff
    SmartDashboard.putNumber("Raw Left Drive Encoder", getLeftEncoder());
    SmartDashboard.putNumber("Raw Right Drive Encoder", getRightEncoder());
    SmartDashboard.putNumber("Yaw", getHeading());
    SmartDashboard.putNumber("Pitch",getPitch());
  }
}
