
package frc.robot;

import frc.robot.Constants.controllerConst;
import frc.robot.Constants.driveConst;
import frc.robot.Constants.posConst;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.Autos;
import frc.robot.commands.BoomPosition;
import frc.robot.commands.ColumnPosition;
import frc.robot.commands.MoveBoom;
import frc.robot.commands.MoveColumn;
import frc.robot.subsystems.Boom;
import frc.robot.subsystems.Clamp;
import frc.robot.subsystems.Column;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 **/
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Boom m_boom = new Boom();
  private final Clamp m_clamp = new Clamp();
  private final Intake m_intake = new Intake();
  private final Column m_column = new Column();
  private final Drivetrain m_drivetrain = new Drivetrain();

  // Controller definition
  public static final Joystick s_Jdriver = new Joystick(0);
  public static final XboxController s_Jop = new XboxController(1);

  // The sendable chooser will show up on the shuffle board for the driver to select the automode they would like to perform for the match.
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   **/
  public RobotContainer() {
    // Create and place auto selector on the SmartDashboard
    m_chooser.setDefaultOption("Drive Straight", Autos.driveStraightAuto(m_drivetrain, m_boom)); // 
    m_chooser.addOption("Drive Straight + Unfold", Autos.driveStraightUnfoldAuto(m_drivetrain,m_boom,m_column));
    m_chooser.addOption("Drive Onto Charging Station", Autos.driveChargingStation(m_drivetrain, m_boom, m_column)); // 
    m_chooser.addOption("Just Unfold/No Drive", Autos.unfoldAuto(m_boom, m_column)); // Just unfolds the boom and column and doesnt drive at all. More of a concept auto to gather the correct parameters before incorporating driving
    //m_chooser.addOption("Cube Floor", Autos.cubeFloorAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Middle", Autos.cubeMidAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Top", Autos.cubeTopAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Floor & Pad", Autos.cubeFloorPadAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Middle & Pad", Autos.cubeMidPadAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    SmartDashboard.putData(m_chooser); // Place the Auto selector on the dashboard with all the options
    
    // Put all the subsystems on the dashboard
    /* TODO: INVESTIGATE & FIX WHY THESE DON'T SHOW UP ON DASHBOARD LIKE THEY SHOULD */
    SmartDashboard.putData("Clamp", m_clamp);
    SmartDashboard.putData("Intake", m_intake);
    SmartDashboard.putData("Boom", m_boom);
    SmartDashboard.putData("Column", m_column);
    SmartDashboard.putData("Drivetrain", m_drivetrain);
    

    // Set the default command of the drivetrain to be driven by driver joystick
    m_drivetrain.setDefaultCommand(
      new RunCommand(()->
        // Command to run without suppliers
        m_drivetrain.drive(
          // double value for straight driving
          getDriveStickY() * driveConst.SPEED_SLOWSTRT,
          // double value for turn driving
          getDriveStickZ() * driveConst.SPEED_SLOWTURN
        ),
        // Requirements for command
        m_drivetrain
    ));

    // Set the default command of the column to be controlled by operator controller right stick
    m_column.setDefaultCommand(
      new RunCommand(() ->
        m_column.move(-getOpRightStickY(),false),
        m_column
    ));

    // Set the default command of the boom arm angle to be controlled by the operator controller left stick
    m_boom.setDefaultCommand(
      new RunCommand(() ->
        m_boom.move(getOpLeftStickY(), false),
        m_boom
    ));

    // Configure button bindings for controllers and such
    configureBindings();
  }
  
  
  
  /**
   * Use this method to define your trigger->command mappings.
   */
  private void configureBindings() {
    /* 
     * onTrue vs. whileTrue:
     * - onTrue should be used when only a single press is needed to start a command that will continue on its own.
     * - whileTrue should be used for commands that need to start when a button is pressed, continue while it is being held down, and stop when the button is released
     *
     * Trigger vs. JoystickButton
     * - Triggers canb ecreated from joystick button objects. There seems to be some difference in using a Trigger of a JoystickButton versus just using
     * a JoystickButton. This was ever present in the issue with the Intake and Clamp systems not responding to button presses during the Week 2 compeition,
     * which for some reason only occured on the field. The firewall was disabled per FTA recommendation and the code was changed during troubleshooting.
     * the results of these two changes resolved the button response issue (some how!?!?!?!?!?).
     */
    
    /* Creates a trigger in response to the limit switch activation for zeroing encoders */
    Trigger columnResetTrigger = new Trigger(m_column::getRevSwitch);
    columnResetTrigger.onTrue(new InstantCommand(m_column::resetEncoder, m_column));

    Trigger boomResetTrigger = new Trigger(m_boom::getSwitch);
    boomResetTrigger.onTrue(new InstantCommand(m_boom::resetEncoder, m_boom));
    
    /*
     * (DISABLED DURING TROUBLESHOOTING) If the driver joystick (USB 0) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    /* Create the button objects/triggers for driver controller */
      
    // Enable slow mode for driving to provide finer driving movments
    new JoystickButton(s_Jdriver, 2).whileTrue(
      new ArcadeDrive(
        () -> s_Jdriver.getY() * driveConst.SPEED_STRT,
        () -> s_Jdriver.getZ() * driveConst.SPEED_TURN,
        m_drivetrain
      )
    );

    // Manually toggle between braking and coasting the drive train
    new JoystickButton(s_Jdriver, 7).toggleOnTrue(new StartEndCommand(m_drivetrain::setDriveBrake, m_drivetrain::setDriveCoast, m_drivetrain));

    
    /*
     * (DISABLED DURING TROUBLESHOOTING) If the operator joystick (USB 1) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    /* Create the button objects/triggers for operator controller */
    
    // (A BUTTON) Outtake motor intake
    Trigger xbox_A = new CommandXboxController(1).a();
    xbox_A.whileTrue(new ParallelCommandGroup(
      new StartEndCommand(m_intake::intake, m_intake::stop, m_intake), // Run intake as soon as button is pressed
      new StartEndCommand(m_clamp::clamp, m_clamp::hold, m_clamp).beforeStarting(new WaitCommand(1.0)) // Wait 1 second after button is pressed to clamp and hold
    ));

    // (B BUTTON) Intake motor outake
    Trigger xbox_B = new CommandXboxController(1).b();
    xbox_B.whileTrue(new ParallelCommandGroup(
      new StartEndCommand(m_clamp:unclamp, m_clamp::stop, m_clamp), // Unclamp as soon as the button is pressed
      new StartEndCommand(m_intake::intake, m_intake::stop, m_intake).beforeStarting(new WaitCommand(0.6)) // Wait 6/10 of a second after the button is pressed to run the intake outwards
    ));

    // (Y BUTTON) Clamp motor clamp and hold part
    Trigger xbox_Y = new CommandXboxController(1).x();
    xbox_Y.whileTrue(new StartEndCommand(m_clamp::unclamp, m_clamp::stop, m_clamp));

    // (X BUTTON) Clamp motor unclamp
    Trigger xbox_X = new CommandXboxController(1).y();
    xbox_X.whileTrue(new StartEndCommand(m_clamp::clamp, m_clamp::stop, m_clamp));

    // (BACK BUTTON) Reset encoders to zero on the boom and column
    new JoystickButton(s_Jop, Button.kBack.value).whileTrue(new ParallelCommandGroup(
      new InstantCommand(m_boom::resetEncoder,m_boom), // Set boom encoder to home position value
      new InstantCommand(m_column::resetEncoder, m_column) // Set column encoder to home position value
    ));

    // (START BUTTON) Allow for Operator to Bypass Column and Boom Limit Switches
    new JoystickButton(s_Jop, Button.kStart.value).whileTrue(new ParallelCommandGroup(
      new MoveColumn(() -> getOpRightStickY(), true, m_column), // Override fwd and rev limit swtiched on column path
      new MoveBoom(() -> getOpLeftStickY(), true, m_boom) // override bottom limit switch on boom path
    ));
  
    /* (POV BUTTONS) Set Column and Boom Positions to reach various positions on field */
    // Top Grid Position
    new POVButton(s_Jop, 0).onTrue(
      new ParallelCommandGroup(
        new BoomPosition((double) posConst.kTopBoom, m_boom),
        new ColumnPosition((double) posConst.kTopColm, m_column).beforeStarting(new WaitCommand(.5))
      ).withTimeout(5.0)
    );
    // Middle Grid Position
    new POVButton(s_Jop, 90).onTrue(
      new ParallelCommandGroup(
        new BoomPosition((double) posConst.kMidBoom, m_boom),
        new ColumnPosition((double) posConst.kMidColm, m_column).beforeStarting(new WaitCommand(.5))
      ).withTimeout(4.0)
    );
    // Home Floor Position
    new POVButton(s_Jop, 180).onTrue(
      new ParallelCommandGroup(
        new BoomPosition((double) posConst.kMinBoom, m_boom),
        new ColumnPosition((double) posConst.kMinColm, m_column).beforeStarting(new WaitCommand(.5))
      ).withTimeout(4.0)
    );
    // Game Piece Safe Carry Position
    new POVButton(s_Jop, 270).onTrue(
      new ParallelCommandGroup(
        new BoomPosition((double) 47.0, m_boom),
        new ColumnPosition((double) 0.0, m_column).beforeStarting(new WaitCommand(.5))
      ).withTimeout(4.0)
    );
   } // END configureBindings()

  
  /*
   * Gets the axis value from the driver joytick roation (z-axis). Applies
   * a deadband to the input to prevent minor joystick drift sending commands
   * to the motors.
   */
  public static double getDriveStickZ() {
    double axisValue = s_Jdriver.getZ();
    if (Math.abs(axisValue) < controllerConst.kDriveAxisZDeadband) {
      axisValue = 0.0;
    }
    return axisValue;
  } // END getDriveStickZ()

  /*
   * Gets the axis value from the driver joytick forward (y-axis). Applies
   * a deadband to the input to prevent minor joystick drift sending commands
   * to the motors.
   */
  public static double getDriveStickY() {
    double axisValue = s_Jdriver.getY();
    if (Math.abs(axisValue) < controllerConst.kDriveAxisYDeadband) {
      axisValue = 0.0;
    }
    return axisValue;
  } // END getDriveStickY()

  /*
   * Gets the axis value from the operator left joystick (y-axis). Applies a
   * deadband to the input to prevent minor joystick drift sending commands
   * to the motors and causing jitteryness and unwanted movements.
   */
  public static double getOpLeftStickY() {
    double axisValue = s_Jop.getLeftY();
    if (Math.abs(axisValue) < controllerConst.kOpAxisLeftYDeadband) {
      axisValue = 0.0;
    }
    return axisValue;
  } // END getOpLeftStickY()

  /*
   * Gets the axis value from the operator right joystick (y-axis). Applies a
   * deadband to the input to prevent minor joystick drift sending commands
   * to the motors and causing jitteryness and unwanted movements.
   */
  public static double getOpRightStickY() {
    double axisValue = s_Jop.getRightY();
    if (Math.abs(axisValue) < controllerConst.kOpAxisRightYDeadband) {
      axisValue = 0.0;
    }
    return axisValue;
  } // END getOpRightStickY()

  /**
   * This is the command that will run during autonomous mode:
   */
  public Command getAutonomousCommand() {
    // Various auto modes will go into the Autos file similar to how constants all go in the constants file
    // They are all selected from the auto chooser on the smart dashboard.
    return m_chooser.getSelected();
  } // END getAutonomousCommand()
} // END RobotContainer Class
