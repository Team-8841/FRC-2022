package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
    private final CANSparkMax m_shooter =
            new CANSparkMax(ShooterConstants.kShooterMotorPort, MotorType.kBrushless);
    private final CANSparkMax m_slave =
            new CANSparkMax(ShooterConstants.kShooterSlavePort, MotorType.kBrushless);
    private final SparkMaxPIDController m_pidController;
    private final RelativeEncoder m_encoder;
    private double m_kP, m_kI, m_kD, m_kIZone, m_kFF, m_setPoint, m_setHoodAngle;

    private Servo m_hoodServo1 = new Servo(ShooterConstants.kHoodServo1Port);
    private Servo m_hoodServo2 = new Servo(ShooterConstants.kHoodServo2Port);


    public Shooter() {
        configureSpark(m_shooter);
        configureSpark(m_slave);

        m_shooter.setInverted(false);
        m_slave.setInverted(false);

        m_slave.follow(m_shooter, true);

        m_hoodServo1.setBounds(2, 0, 0, 0, 1);
        m_hoodServo2.setBounds(2, 0, 0, 0, 1);

        m_pidController = m_shooter.getPIDController();
        m_encoder = m_shooter.getEncoder();

        m_pidController.setP(ShooterConstants.kP);
        m_pidController.setI(ShooterConstants.kI);
        m_pidController.setD(ShooterConstants.kD);
        m_pidController.setFF(ShooterConstants.kFF);
        m_pidController.setOutputRange(ShooterConstants.kMinOutput, ShooterConstants.kMaxOutput);

        // display PID coefficients on SmartDashboard
        // SmartDashboard.putNumber("[Shooter] P Gain", ShooterConstants.kP);
        // SmartDashboard.putNumber("[Shooter] I Gain", ShooterConstants.kI);
        // SmartDashboard.putNumber("[Shooter] D Gain", ShooterConstants.kD);
        // SmartDashboard.putNumber("[Shooter] I Zone", ShooterConstants.kIZone);
        // SmartDashboard.putNumber("[Shooter] Feed Forward", ShooterConstants.kFF);
        // SmartDashboard.putNumber("[Shooter] Setpoint", 0);
    }


    private void configureSpark(CANSparkMax sparkMax) {
        sparkMax.restoreFactoryDefaults();
        sparkMax.setSmartCurrentLimit(ShooterConstants.kCurrentLimit);
        sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }

    @Override
    public void periodic() {
        updateStatus();
    }

    public void setSetpoint(double setpoint) {
        m_pidController.setReference(setpoint, ControlType.kVelocity);
        m_setPoint = setpoint;
    }

    public void setHoodAngle(double angle) {
        m_hoodServo1.set(angle);
        m_hoodServo2.set(angle);
    }

    public double getHoodAngle() {
        return m_hoodServo1.get();
    }

    public boolean upToSpeed() {
        double curVel = m_encoder.getVelocity();
        return curVel > m_setPoint - ShooterConstants.kAllowedError;
    }

    public void tune() {
        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("[Shooter] P Gain", ShooterConstants.kP);
        double i = SmartDashboard.getNumber("[Shooter] I Gain", ShooterConstants.kI);
        double d = SmartDashboard.getNumber("[Shooter] D Gain", ShooterConstants.kD);
        double iZone = SmartDashboard.getNumber("[Shooter] I Zone", ShooterConstants.kIZone);
        double ff = SmartDashboard.getNumber("[Shooter] Feed Forward", ShooterConstants.kFF);
        double setpoint = SmartDashboard.getNumber("[Shooter] Setpoint", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if ((p != m_kP)) {
            m_pidController.setP(p);
            m_kP = p;
        }
        if ((i != m_kI)) {
            m_pidController.setI(i);
            m_kI = i;
        }
        if ((d != m_kD)) {
            m_pidController.setD(d);
            m_kD = d;
        }
        if ((i != m_kIZone)) {
            m_pidController.setIZone(iZone);
            m_kIZone = iZone;
        }
        if ((ff != m_kFF)) {
            m_pidController.setFF(ff);
            m_kFF = ff;
        }
        if ((setpoint != m_setPoint)) {
            m_pidController.setReference(setpoint, ControlType.kVelocity);
            m_setPoint = setpoint;
        }

    }

    public void updateStatus() {
        SmartDashboard.putNumber("[Shooter] Velocity", m_encoder.getVelocity());
        // SmartDashboard.putNumber("[Shooter] Hood Angle", m_hoodServo1.get());
        SmartDashboard.putBoolean("[Shooter] At Speed", upToSpeed());
    }
}
