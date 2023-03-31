// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.unfoldBoom;
import frc.robot.commands.unfoldColumn;
import frc.robot.commands.autos.AutoConstants.unfoldConst;
import frc.robot.subsystems.Boom;
import frc.robot.subsystems.Column;

public class unfold extends SequentialCommandGroup {
  public unfold(Column column, Boom boom) {
    /*
     * 1) (INIT) Reset Boom Encoder and Column Encoder.
     * 1a) Timeout of 0.4 seconds since resets happen slowly.
     * 2) Start Moving Boom until it reaches the limit switch.
     * 2a) After a little delay start moving the column in parallel back to its limit switch.
     * 3) (END) End when both have reached their limit switches and have reset.
     */

    // Add your commands in the addCommands() call
    addCommands(
      new ParallelCommandGroup(
        // Unfold Boom Arm. Downwards direction will flip it out of starting config
        new unfoldBoom(boom).withTimeout(unfoldConst.kBoomTimeout),
        // After a short delay, begin moving column backwards to home position
        new unfoldColumn(column)
        .beforeStarting(new WaitCommand(unfoldConst.kColumnStartDelay))
        .withTimeout(unfoldConst.kColumnTimeout)
      ) // END PARALLEL COMMAND GROUP
    ); // END ADD COMMANDS
  }
}
