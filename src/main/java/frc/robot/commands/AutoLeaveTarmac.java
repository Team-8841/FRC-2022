package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

public class AutoLeaveTarmac extends SequentialCommandGroup {

    public AutoLeaveTarmac(DriveSubsystem drive) {
        super(
            // To save this file without it auto formatting use ctrl + k then ctrl + shift + s
            new ResetDriveSensors(drive), 
            new DriveToDistance(20, .5, drive)
        );
    }

}
