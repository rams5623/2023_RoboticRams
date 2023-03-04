// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.Autos;
import frc.robot.commands.MoveBoom;
import frc.robot.commands.MoveColumn;
import frc.robot.subsystems.Boom;
import frc.robot.subsystems.Clamp;
import frc.robot.subsystems.Column;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
//import frc.robot.subsystems.Cameras;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 * 
 * m = SUBSYSTEM
 * s = SENSOR/JOYSTICK
 * c = COMMAND
 **/
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Boom m_boom = new Boom();
  private final Clamp m_clamp = new Clamp();
  private final Intake m_intake = new Intake();
  private final Column m_column = new Column();
  private final Drivetrain m_drivetrain = new Drivetrain();

  // Controller creation
  private final Joystick s_Jdriver = new Joystick(0);
  //private final Joystick s_Jop = new Joystick(1);
  private final XboxController s_Jop = new XboxController(1);

  //Limit Switches
  //private final DigitalInput s_columnSwitch = new DigitalInput(columnConst.kswitch_column);
  //private final DigitalInput s_boomSwitch = new DigitalInput(boomConst.kswitch_boom);

  // The sendable chooser will show up on the shuffle board for the driver to select the automode they would like to perform for the match.
  SendableChooser<Command> m_chooser = new SendableChooser<>();



  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   **/
  public RobotContainer() {
    // Smartdashboard subsystem data
    m_chooser.setDefaultOption("Simple Drive Straight", Autos.driveStraightAuto(m_drivetrain, m_boom));
    
    // Set the default command of the drivetrain to be the ArcadeDrive command so the dirvers can just drive automatically
    m_drivetrain.setDefaultCommand(
      new ArcadeDrive(s_Jdriver::getY, s_Jdriver::getZ, m_drivetrain)
    );

    m_column.setDefaultCommand(
      new MoveColumn(s_Jop::getRightY, m_column)
    );

    m_boom.setDefaultCommand(
      new MoveBoom(s_Jop::getLeftY, m_boom)
    );
    
    // Configure button bindings for controllers and such
    configureBindings();
  }



  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    Trigger columnResetTrigger = new Trigger(m_column::getRevSwitch);
    columnResetTrigger.whileTrue(new InstantCommand(m_column::resetEncoder, m_column));

    Trigger boomResetTrigger = new Trigger(m_boom::getSwitch);
    boomResetTrigger.whileTrue(new InstantCommand(m_boom::resetEncoder, m_boom));
    /*
     * If the driver joystick (USB 0) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    if (s_Jdriver.isConnected()) {
      /* Create all button objects */
      /* Use the button objects for driver controller */
      // Enable charging station drive up with braking
      
      // Enable slow mode for driving to provide finer driving movments
      //Jdriver_2.onTrue(new ArcadeDrive(() -> s_Jdriver.getY() * 0.8, () -> s_Jdriver.getZ() * 0.6, m_drivetrain));
      new JoystickButton(s_Jdriver, 2).whileTrue(new ArcadeDrive(() -> s_Jdriver.getY() * 0.5, () -> s_Jdriver.getZ() * 0.6, m_drivetrain));
    }

    /*
     * If the operator joystick (USB 1) is connected then run this section, otherwise move on.
     * This prevents the buttons from being created on joystick that isnt present and causing a list of errors to appear on connection to the rio.
     */
    if (s_Jop.isConnected()) {
      /* Create all button objects */

      // Intake motor intake
      new JoystickButton(s_Jop, Button.kA.value).whileTrue(new StartEndCommand(m_intake::intake, m_intake::stop, m_intake));
      //Jop_1.onTrue(new StartEndCommand(m_intake::intake, m_intake::stop, m_intake)); // onTrue does not perform the End portion of this command

      // Intake motor outake
      new JoystickButton(s_Jop, Button.kB.value).whileTrue(new StartEndCommand(m_intake::outake, m_intake::stop, m_intake));
      //Jop_2.onTrue(new StartEndCommand(m_intake::outake, m_intake::stop, m_intake)); // onTrue does not perform the End portion of this command

      // Clamp motor clamp
      new JoystickButton(s_Jop, Button.kX.value).whileTrue(new StartEndCommand(m_clamp::clamp, m_clamp::stop, m_clamp));
      //Jop_7.onTrue(new StartEndCommand(m_clamp::clamp, m_clamp::stop, m_clamp)); // onTrue does not perform the End portion of this command

      // Clamp motor unclamp
      new JoystickButton(s_Jop, Button.kY.value).whileTrue(new StartEndCommand(m_clamp::unclamp, m_clamp::stop, m_clamp));
      //Jop_8.onTrue(new StartEndCommand(m_clamp::unclamp, m_clamp::stop, m_clamp)); // onTrue does not perform the End portion of this command
    }
  }



  /**
   * This is the command that will run during autonomous mode:
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // Various auto modes will go into the Autos file similar to how constants all go in the constants file
    return Autos.exampleAuto(m_intake);
  }
}


/***
 * EXAMPLE STUFF:
 * 
 *  // Schedule `ExampleCommand` when `exampleCondition` changes to `true` THIS WOULD BE GOOD FOR RESETING THE ENCODERS WITH THE LIMIT SWITCHES
 *  //new Trigger(m_exampleSubsystem::exampleCondition)
 *  //    .onTrue(new ExampleCommand(m_exampleSubsystem));
 *
 *  // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
 *  // cancelling on release.
 *  //m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
 * 
 *  Replace with CommandPS4Controller or CommandJoystick if needed
 *   private final CommandXboxController m_driverController =
 *     new CommandXboxController(OperatorConstants.kDriverControllerPort);
 * 
 ***/