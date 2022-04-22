package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class Climber extends SubsystemBase {

    private final TalonSRX m_masterLiftMotor = new TalonSRX(ClimberConstants.kMasterLiftMotor);
    private final TalonSRX m_slaveLiftMotor = new TalonSRX(ClimberConstants.kSlaveLiftMotor);

    private final Solenoid m_hangSolenoid =
            new Solenoid(PneumaticsModuleType.CTREPCM, ClimberConstants.kHangerSolenoidPort);

    private final DigitalInput m_topLimit = new DigitalInput(ClimberConstants.kTopLimitSensorPort);
    private final DigitalInput m_bottomLimit =
            new DigitalInput(ClimberConstants.kBottomLimitSensorPort);

    public Climber() {
        configureTalon(m_masterLiftMotor);
        configureTalon(m_slaveLiftMotor);


        m_masterLiftMotor.setInverted(true);
        m_slaveLiftMotor.setInverted(false);

        m_slaveLiftMotor.follow(m_masterLiftMotor);
    }

    private void configureTalon(TalonSRX talon) {
        talon.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {
        updateStatus();
    }

    public void setLiftSpeed(double speed) {
        m_masterLiftMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setHangSoleniod(boolean extended) {
        m_hangSolenoid.set(extended);
    }

    public boolean getTopLimit() {
        return !m_topLimit.get();
    }

    public boolean getBottomLimit() {
        return !m_bottomLimit.get();
    }

    public boolean getArmExtended() {
        return m_hangSolenoid.get();
    }

    private void updateStatus() {
        SmartDashboard.putBoolean("[Climber]: Top limit", getTopLimit());
        SmartDashboard.putBoolean("[Climber]: Bottom limit", getBottomLimit());
        SmartDashboard.putBoolean("[climber]: Arms Extened", getArmExtended());

    }

}
