/*
 * THIS SUBSYSTEM IS CURRENTLY NOT BEING USED AS OF KENTWOOD COMPETITION
 * LEAVE ALL INTAKE ITEMS IN PLACE BECAUSE THE TALON IS STILL ON THE ROBOT
 * THE GAME PIECE PICKUP MECHANISM CAN BE SWAPPED
 */

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeConst;

/*
 * INTAKE NOMENCLATURE
 * 
 * (-) Negative Command Given to the TalonSRX (Red Blinking Lights):
 * DROP = SPIT OUT = OUTAKE
 * 
 * (+) Positive Command Given to the TalonSRX: (Green Blinking Lights):
 * PICKUP = SUCK IN = INTAKE
 * 
 */

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private final WPI_TalonSRX m_talonIntake = new WPI_TalonSRX(intakeConst.ktalon_intake);

  public Intake() {
    m_talonIntake.configFactoryDefault();

    m_talonIntake.setInverted(false);
    m_talonIntake.setNeutralMode(NeutralMode.Brake);
    m_talonIntake.configNeutralDeadband(intakeConst.kDeadbandIntake);
    // m_talonClamp.enableDeadbandElimination(true); // DOES THIS WORK? IS THIS WHY THE DEADBAND NEVER SEEMS TO WORK BECAUSE IT WAS NEVER ENABLED?!?!?!?!? WHY ISNT IT ENABLED BY DEFAULT!?!?!?
    m_talonIntake.configOpenloopRamp(0.0);
    m_talonIntake.configClosedloopRamp(0.0);
  }

  /* 
   * Drive The Rollers Up and Inwards at a Constant Speed
   * Intaking is considered a positive power direction for the motor controller (Green blinking lights).
   * The spining rollers will drive a game piece up and into the clamp mechanism. From the perspective
   * of looking back to front on the robot, the rollers are spining towards the robot as if the intake
   * mechanism could drive itself by rolling on the floor.
   */
  public void intake() {
    m_talonIntake.set(ControlMode.PercentOutput, intakeConst.SPEED_IN);
    SmartDashboard.putString("Intake Direction", "Intake");
  }
  
  /* 
   * Drive The Rollers Down and Outwards at a Constant Speed
   * Outaking is considered a negative power direction for the motor controller (Red blinking lights).
   * The spining rollers will drive a game piece out of the clamp mechanism. From the perspective
   * of looking back to front on the robot, the rollers are spining away from the robot as if the intake
   * mechanism could drive itself by rolling on the floor.
   */
  public void outake() {
    m_talonIntake.set(ControlMode.PercentOutput, -intakeConst.SPEED_OUT);
    SmartDashboard.putString("Intake Direction", "Outake");
  }
  
  /* 
   * Apply 0% Command to Intake Motor to "Stop" it from Moving
   * The desired affect of this function is to stop sending a command to the motor. As a result there
   * should not be any feedforward compensation or percent of power output applied at all when this
   * is called.
   */
  public void stop() {
    m_talonIntake.set(ControlMode.PercentOutput, 0);
    SmartDashboard.putString("Intake Direction", "Stopped");
  }
  
  /* 
   * RUNS ONCE EVERY PROCESS LOOP OF THE SCHEDULER
   */
  @Override
  public void periodic() {
    //SmartDashboard.putNumber("Intake Current", m_talonIntake.getStatorCurrent());
  }
}
