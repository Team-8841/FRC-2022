package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.subsystems.DriveSubsystem;

public class TurnToAngle extends ProfiledPIDCommand {


    public TurnToAngle(double targetAngleDegrees, DriveSubsystem drive) { 
        super (
            new ProfiledPIDController(
                // PID Gains
                0.05, 0, 0.005, //TODO: tune

                //Motion constants
                new TrapezoidProfile.Constraints(100, 300)),
                //Returns measurement
                drive::getHeading,
                //Returns goal (sometimes constant)
                targetAngleDegrees,

                (output, setpoint) -> {
                    drive.arcadeDrive(0, output);
            });

            addRequirements(drive);

            getController().enableContinuousInput(-180, 180);

            getController().setTolerance(5, 10); //TODO: tune

    }

    
    @Override
    public boolean isFinished() {
        return getController().atGoal();
    }

}
