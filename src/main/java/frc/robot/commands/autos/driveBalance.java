
package frc.robot.commands.autos;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.commands.autos.AutoConstants.balanceConst;
import frc.robot.subsystems.Drivetrain;

public class driveBalance extends PIDCommand {

  public driveBalance(Double initYaw, Drivetrain drivetrain) {
    super(
        // The controller that the command will use
        new PIDController(balanceConst.kP, balanceConst.kI, balanceConst.kD),
        // This should return the measurement
        drivetrain::getPitch,
        // This should return the setpoint (can also be a constant)
        initYaw,
        // This uses the output
        output -> drivetrain.drive(output, 0.0),
          // Requires the drivetrain
          drivetrain
        );

    getController().enableContinuousInput(balanceConst.kminInput, balanceConst.kmaxInput);
    getController().setTolerance(balanceConst.kpitchTol, balanceConst.krateTol);
    getController().setSetpoint(initYaw);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
