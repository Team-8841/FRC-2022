package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class SetShooterSpeed extends CommandBase {
    private Shooter m_shooter;

    private double m_speed, m_angle;

    public SetShooterSpeed(double speed, double angle, Shooter shooter) {
        m_shooter = shooter;
        m_speed = speed;
        m_angle = angle;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_shooter.setSetpoint(m_speed);
        m_shooter.setHoodAngle(m_angle);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

