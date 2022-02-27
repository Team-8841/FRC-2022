package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

public class AutoLeaveTarmac extends SequentialCommandGroup {

    public AutoLeaveTarmac(DriveSubsystem drive) {
        super(//
                new ResetDriveSensors(drive), //
                new SequentialCommandGroup(//
                        new DriveToDistance(40, .5, drive) // 50% speed
                ));
    }

}
