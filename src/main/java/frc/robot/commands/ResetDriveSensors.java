package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;

public class ResetDriveSensors extends InstantCommand {

    private DriveSubsystem m_drive;

    public ResetDriveSensors(DriveSubsystem drive) {
        m_drive = drive;
    }
    

    @Override
    public void initialize() {
        m_drive.resetEncoder();
        m_drive.resetHeading();
    }

}
