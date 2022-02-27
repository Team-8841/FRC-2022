package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;

public class AutoLeaveTarmac extends SequentialCommandGroup {

    public AutoLeaveTarmac(DriveSubsystem drive) {
        super(new ResetDriveSensors(drive),

                new SequentialCommandGroup(new WaitCommand(.5), new DriveToDistance(20, drive),
                        new TurnToAngle(90, drive), new DriveToDistance(10, drive))

        );
    }

}
