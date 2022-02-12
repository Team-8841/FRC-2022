package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TurretConstants;

public class Turret extends SubsystemBase {

    private final VictorSPX m_turretMotor = new VictorSPX(TurretConstants.kTurretMotorPort);
    private final DigitalInput m_leftLimit = new DigitalInput(TurretConstants.kLeftSensorPort);
    private final DigitalInput m_rightLimit = new DigitalInput(TurretConstants.kRightSensorPort);

    public Turret() {
        m_turretMotor.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {
        updateStatus();
    }

    public void setSpeed(final double speed) {
        if ((speed < 0 && m_leftLimit.get()) || (speed > 0 && m_rightLimit.get())) {
            m_turretMotor.set(ControlMode.PercentOutput, speed);
        } else {
            m_turretMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void updateStatus() {
        SmartDashboard.putBoolean("[Turret] Left Limit", !m_leftLimit.get());
        SmartDashboard.putBoolean("[Turret] right Limit", !m_rightLimit.get());
    }
}
