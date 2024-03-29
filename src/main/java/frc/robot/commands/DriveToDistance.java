package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToDistance extends CommandBase {
    private DriveSubsystem m_drive;

    private PIDController distancePID = new PIDController(0.085, 0, 0); // TODO: Tune
    private PIDController rotationPID = new PIDController(0.1, 0, 0.005); // TODO: Tune

    private double m_maxSpeed;


    public DriveToDistance(double targetDistance, double speed, DriveSubsystem drive) {
        m_maxSpeed = speed;
        m_drive = drive;
        addRequirements(drive);
        distancePID.setSetpoint(targetDistance);
        distancePID.setTolerance(5, 2); // TODO: tune
    }

    @Override
    public void initialize() {
        m_drive.resetEncoder();
        rotationPID.setSetpoint(m_drive.getHeading());
    }

    @Override
    public void execute() {
        double speed = distancePID.calculate(m_drive.getDistance());

        if (speed > m_maxSpeed)
            speed = m_maxSpeed;
        else if (speed < -m_maxSpeed)
            speed = -m_maxSpeed;

        double rotSpeed = rotationPID.calculate(m_drive.getHeading());
        double maxRotSpeed = .8; // TODO: Tune

        if (rotSpeed > maxRotSpeed)
            rotSpeed = maxRotSpeed;
        else if (rotSpeed < -maxRotSpeed)
            rotSpeed = -maxRotSpeed;

        m_drive.arcadeDrive(speed, rotSpeed);
    }


    @Override
    public void end(boolean interrupted) {
        m_drive.arcadeDrive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return distancePID.atSetpoint();
    }

}
