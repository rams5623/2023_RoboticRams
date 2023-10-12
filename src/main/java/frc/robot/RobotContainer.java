
package frc.robot;

import frc.robot.Constants.controllerConst;
import frc.robot.Constants.driveConst;
import frc.robot.Constants.posConst;
import frc.robot.Constants.boomConst.boomPosition;
import frc.robot.Constants.columnConst.columnPosition;
import frc.robot.commands.BoomControl;
import frc.robot.commands.BoomPosition;
import frc.robot.commands.ColumnControl;
import frc.robot.commands.ColumnPosition;
import frc.robot.commands.autos.Autos;
import frc.robot.subsystems.Boom;
import frc.robot.subsystems.Clamp;
import frc.robot.subsystems.Column;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
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
  private final GlobalVariables m_variables = new GlobalVariables();

  // Controller creation
  public static final Joystick s_Jdriver = new Joystick(controllerConst.kDriveJoystickUSB);
  public static final CommandXboxController s_Jop = new CommandXboxController(controllerConst.kOpJoystickUSB);

  // The sendable chooser will show up on the shuffle board for the driver to select the automode they would like to perform for the match.
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   **/
  public RobotContainer() {
    // Create and place auto selector on the SmartDashboard
    m_chooser.setDefaultOption("Just Unfold Arm", Autos.unfoldAuto(m_boom, m_column));
    m_chooser.addOption("Drive Straight", Autos.driveStraightAuto(m_drivetrain));
    m_chooser.addOption("Drive Straight + Unfold", Autos.driveUnfoldAuto(m_drivetrain, m_boom, m_column));
    m_chooser.addOption("Drive Onto Charging Station", Autos.driveBalanceAuto(m_drivetrain, m_boom, m_column)); // [NEEDS TO BE TESTED]
    //m_chooser.addOption("Cube Floor", Autos.cubeFloorAuto(m_drivetrain, m_boom, m_column, m_clamp, m_intake)); // [NEEDS TO BE TESTED]
    //m_chooser.addOption("Cube Middle", Autos.cubeMidAuto(m_drivetrain, m_boom, m_column, m_clamp, m_intake)); // [NEEDS TO BE TESTED]
    //m_chooser.addOption("Cube Top", Autos.cubeTopAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Floor & Pad", Autos.cubeFloorPadAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    //m_chooser.addOption("Cube Middle & Pad", Autos.cubeMidPadAuto(m_drivetrain, m_boom, m_column)); // [NOT CREATED YET]
    SmartDashboard.putData(m_chooser); // Place the Auto selector on the dashboard with all the options
    
    // Put all the subsystems on the dashboard
    SmartDashboard.putData("Clamp", m_clamp); 
    SmartDashboard.putData("Intake", m_intake);
    SmartDashboard.putData("Boom", m_boom);
    SmartDashboard.putData("Column", m_column);
    SmartDashboard.putData("Drivetrain", m_drivetrain);
    

    // Set the default command of the drivetrain to be driven by driver joystick. Doing it this way prevents the need for an
    // ArcadeDrive() command file. Adujstable speeds could be done through a GlobalVar instead a ConstVar.
    m_drivetrain.setDefaultCommand(
      new RunCommand(()->
        // Command to run without suppliers
        m_drivetrain.drive(
          // double value for straight driving
          //getDriveStickY() * driveConst.SPEED_SLOWSTRT,
          getDriveStickY() * m_variables.getDriveSpeed()[0],
          // double value for turn driving
          //getDriveStickZ() * driveConst.SPEED_SLOWTURN
          getDriveStickZ() * m_variables.getDriveSpeed()[1]
        ),
        // Requirements for command
        m_drivetrain
    ));

    // Set the default command of the column to be controlled by operator controller right stick
    m_column.setDefaultCommand(
      new ColumnControl(m_column, this, m_variables)
    );
    // m_column.setDefaultCommand(
    //   new MoveColumn(s_Jop::getRightY, m_column)
    // );

    // Set the default command of the boom arm angle to be controlled by the operator controller left stick
    m_boom.setDefaultCommand(
      new BoomControl(m_boom, this, m_variables)
    );
    // m_boom.setDefaultCommand(
    //   new MoveBoom(s_Jop::getLeftY, m_boom)
    // );

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
    columnResetTrigger.whileTrue(new InstantCommand(m_column::resetEncoder, m_column));

    Trigger boomResetTrigger = new Trigger(m_boom::getSwitch);
    boomResetTrigger.whileTrue(new InstantCommand(m_boom::resetEncoder, m_boom));
    
    
    /* DRIVER JOYSICK SECTION
     * If the driver joystick (USB 0) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    /* 
      * ENABLES TEMPORARY FAST MODE WHILE THE THUMB BUTTON (2) ON THE JOYSTICK IS BEING PRESSED
      * (No longer works with ArcadeDrive() since it's removal from the comp build)
      */
    //new JoystickButton(s_Jdriver, 2).whileTrue( // Button 2 on Driver Joystick
    //  new ArcadeDrive( // Run a new instance of this command
    //    () -> getDriveStickY() * driveConst.SPEED_STRT, // Straight Parameter
    //    () -> getDriveStickZ() * driveConst.SPEED_TURN, // Turn Parameter
    //    m_drivetrain // Command Requirement
    // ));
    // \/ TRY THIS IMPLEMENTATION BUT FOR SETTING DriveSpeed VARIABLE \/
    new JoystickButton(s_Jdriver, 2).whileTrue(
       new InstantCommand(() -> m_variables.setDriveSpeed(driveConst.SPEED_STRT, driveConst.SPEED_TURN), m_variables)
     ).onFalse(
       // Set the switch bypass global variable to false when start is released
       new InstantCommand(() -> m_variables.setDriveSpeed(driveConst.SPEED_SLOWSTRT, driveConst.SPEED_SLOWTURN), m_variables)
    );
    // END FAST BUTTON COMMAND

    /*
      * TOGGLE BRAKE AND COAST MODES OF MOTOR CONTROL ON THE DRIVETRAIN CONTROLLERS WHEN BUTTON 7 IS PRESSED
      */
    new JoystickButton(s_Jdriver, 7).toggleOnTrue( // Button 7 on Driver Joystick
      new StartEndCommand( // Run new instance of StartEnd command
        m_drivetrain::setDriveBrake, // Run this at Command Start
        m_drivetrain::setDriveCoast, // Run this at Command End
        m_drivetrain // Command Requirement
    ));
    // END BRAKE/COAST TOGGLE COMMAND

    /*
      * RESET DRIVE ENCODERS MANUALLY FOR SOME REASON
      */
    
    new JoystickButton(s_Jdriver, 12).toggleOnTrue( // Button 12 on Driver Joystick
      new InstantCommand(m_drivetrain::resetEncoder, m_drivetrain)
    );
    // } /* END DRIVER JOYSTICK SECTION */
    
    
    
    /* OPERATOR CONTROLLER SECTION
     * If the operator joystick (USB 1) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    /*
     * RUN GAME PIECE PICKUP COMMAND
     */
      // Trigger xbox_A = new CommandXboxController(controllerConst.kOpJoystickUSB).a(); // Create new trigger from Xbox controller button A
      // Trigger xbox_A = s_Jop.a(); // [TEST THIS ALTERNATE] Create new trigger from Xbox Controller button A
      // xbox_A.whileTrue(new ParallelCommandGroup( // While the Button A Trigger is True run some commands in parallel
      //   new StartEndCommand(m_intake::intake, m_intake::stop, m_intake), // StartEnd command to run intake inwards and stop on release
      //   new StartEndCommand(m_clamp::clamp, () -> m_clamp.hold(true), m_clamp).beforeStarting(new WaitCommand(1.0)) // StartEnd command to run clamp down and hold on release after a brief delay
      // ));
      /* THIS /\ FOR SURE WORKS IF THE BELOW \/ DOESNT */
      s_Jop.a().whileTrue( // While the Button A is True run some commands in parallel
        new StartEndCommand(m_clamp::clamp, () -> m_clamp.hold(true), m_clamp) // StartEnd command to run clamp down and hold on release
      );
      // END PICKUP PIECE COMMAND

      /*
       * RUN GAME PIECE DROP COMMAND
       */
      // Trigger xbox_B = new CommandXboxController(controllerConst.kOpJoystickUSB).b(); // Create new trigger from Xbox controller button B
      // Trigger xbox_B = s_Jop.b(); // [TEST THIS ALTERNATE] Create new trigger from Xbox Controller button B
      // xbox_B.whileTrue(new ParallelCommandGroup( // While the Button B Trigger is True run some commands in parallel
      //   new StartEndCommand(m_clamp::unclamp, () -> m_clamp.hold(false), m_clamp), // StartEnd command to run Clamp upwards and stop on release
      //   new StartEndCommand(m_intake::outake, m_intake::stop, m_intake).beforeStarting(new WaitCommand(1.0)) // StartEnd command to run intake outwards and stop on release after brief delay
      // ));
      /* THIS /\ FOR SURE WORKS IF THE BELOW \/ DOESNT */
      s_Jop.b().whileTrue( // While the Button B is True run some commands in parallel
        new StartEndCommand(m_clamp::unclamp, () -> m_clamp.hold(false), m_clamp) // StartEnd command to run Clamp upwards and stop on release
      );
      // END DROP PIECE COMMAND

      /*
       * RUN CLAMP DOWNWARDS INDEPENDENTLY OF INTAKE
       */
      // Trigger xbox_X = new CommandXboxController(controllerConst.kOpJoystickUSB).x();
      // xbox_X.whileTrue(new StartEndCommand(m_clamp::clamp, m_clamp::stop, m_clamp));
      /* THIS /\ FOR SURE WORKS IF THE BELOW \/ DOESNT */
      s_Jop.x().whileTrue(new StartEndCommand( // Run new instance of StartEnd command while Button X is true
        m_clamp::clamp, // Run this at Command Start
        m_clamp::stop, // Run this at Command End
        m_clamp // Command Requirement
      ));
      // END INDEPENDENT CLAMP DOWNWARDS COMMAND

      /*
       * RUN CLAMP UPWARDS INDEPENDENTLY OF INTAKE
       */
      // Trigger xbox_Y = new CommandXboxController(controllerConst.kOpJoystickUSB).y();
      // xbox_Y.whileTrue(new StartEndCommand(m_clamp::unclamp, m_clamp::stop, m_clamp));
      /* THIS /\ FOR SURE WORKS IF THE BELOW \/ DOESNT */
      s_Jop.y().whileTrue(new StartEndCommand( // Run new instance of StartEnd command while Button Y is true
        m_clamp::unclamp, // Run this at Command Start
        m_clamp::stop, // Run this at Command End
        m_clamp // Command Requirement
      ));
      // END INDEPENDENT CLAMP UPWARDS COMMAND

      /*
       * RESET ENCODERS TO HOME POSITION VALUES
       */
      s_Jop.back().whileTrue(new ParallelCommandGroup( // Run new instance of parallel command group when back button is true
        new InstantCommand(m_boom::resetEncoder, m_boom), // Reset Boom Encoder
        new InstantCommand(m_column::resetEncoder, m_column) // Reset Column Encoder
      ));
      // END ENCODER RESET COMMAND

      /*
       * BYPASS LIMIT SWITCHES ON BOOM AND COLUMN
       */
      s_Jop.start().onTrue(
        // Set the switch bypass global variable to true when start is pressed
        new InstantCommand(() -> m_variables.setSwitchBypass(true), m_variables)
      ).onFalse(
        // Set the switch bypass global variable to false when start is released
        new InstantCommand(() -> m_variables.setSwitchBypass(false), m_variables)
      );
      // END SWITCH BYPASS COMMAND

      /*
       * POV BUTTON POSITIONAL COMMANDS
       */
      // Top Grid Position
      s_Jop.povUp().onTrue(
        new ParallelCommandGroup(
          new BoomPosition((double) posConst.kTopBoom, m_boom),
          new ColumnPosition((double) posConst.kTopColm, m_column).beforeStarting(new WaitCommand(.3))
        ).withTimeout(5.0));
      // Middle Grid Position
      s_Jop.povRight().onTrue(
        new ParallelCommandGroup(
          new BoomPosition((double) posConst.kMidBoom, m_boom),
          new ColumnPosition((double) posConst.kMidColm, m_column).beforeStarting(new WaitCommand(.2))
        ).withTimeout(5.0));
      // Floor Grid Position
      s_Jop.povDown().onTrue(
        new ParallelCommandGroup(
          new BoomPosition((double) posConst.kBotBoom, m_boom).beforeStarting(new WaitCommand(.5)),
          new ColumnPosition((double) posConst.kBotColm, m_column)
        ).withTimeout(5.0));
      // Store Position
      s_Jop.povLeft().onTrue(
        new ParallelCommandGroup(
          new BoomPosition((double) posConst.kStowBoom, m_boom),
          new ColumnPosition((double) posConst.kStowColm, m_column)
        ).withTimeout(5.0));

      // Top Grid Position
      // s_Jop.povUp().onTrue(
      //   new SequentialCommandGroup(
      //     new InstantCommand(() -> m_variables.setBoomPosition(boomPosition.TOP), m_variables).withTimeout(0.1),
      //     new InstantCommand(() -> m_variables.setColumnPosition(columnPosition.TOP), m_variables).withTimeout(0.1)
      // ));
      // // Middle Grid Position
      // s_Jop.povRight().onTrue(
      //   new SequentialCommandGroup(
      //     new InstantCommand(() -> m_variables.setBoomPosition(boomPosition.MIDDLE), m_variables).withTimeout(0.1),
      //     new InstantCommand(() -> m_variables.setColumnPosition(columnPosition.MIDDLE), m_variables).withTimeout(0.1)
      // ));
      // // Floor Grid Position
      // s_Jop.povDown().onTrue(
      //   new SequentialCommandGroup(
      //     new InstantCommand(() -> m_variables.setBoomPosition(boomPosition.FLOOR), m_variables).withTimeout(0.1),
      //     new InstantCommand(() -> m_variables.setColumnPosition(columnPosition.FLOOR), m_variables).withTimeout(0.1)
      // ));
      // // Stow Travel Position
      // s_Jop.povLeft().onTrue(
      //   new SequentialCommandGroup(
      //     new InstantCommand(() -> m_variables.setBoomPosition(boomPosition.STOW), m_variables).withTimeout(0.1),
      //     new InstantCommand(() -> m_variables.setColumnPosition(columnPosition.STOW), m_variables).withTimeout(0.1)
      // ));
      // END POV POSITION COMMANDS
    // } /* END OPERATOR CONTROLLER SECTION */
  }

  
  /*
   * Gets the axis value from the driver joytick roation (z-axis). Applies
   * a deadband to the input to prevent minor joystick drift sending commands
   * to the motors.
   */
  public double getDriveStickZ() {
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
  public double getDriveStickY() {
    double axisValue = -s_Jdriver.getY();
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
  public double getOpLeftStickY() {
    double axisValue = -s_Jop.getLeftY();
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
  public double getOpRightStickY() {
    double axisValue = -s_Jop.getRightY();
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
