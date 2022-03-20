package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class Climber extends SubsystemBase {

    private final TalonSRX m_frontLiftMotor = new TalonSRX(ClimberConstants.kFrontLiftMotorPort);
    private final TalonSRX m_rearLiftMotor = new TalonSRX(ClimberConstants.kRearLiftMotorPort);
    private final VictorSPX m_rearPivorMotor = new VictorSPX(ClimberConstants.kRearPivotMotorPort);

    private final DigitalInput m_frontTopLimit =
            new DigitalInput(ClimberConstants.kFrontTopLimitSensorPort);
    private final DigitalInput m_frontBottomLimit =
            new DigitalInput(ClimberConstants.kFrontBottomLimitSensorPort);

    private final DigitalInput m_rearForwardLimit =
            new DigitalInput(ClimberConstants.kRearForwardLimitSensorPort);
    private final DigitalInput m_rearBackLimit =
            new DigitalInput(ClimberConstants.kRearBackLimitSensorPort);
    private final DigitalInput m_rearTopLimit =
            new DigitalInput(ClimberConstants.kRearTopLimitSensorPort);
    private final DigitalInput m_rearBottomLimit =
            new DigitalInput(ClimberConstants.kRearBottomLimitSensorPort);



    public Climber() {
        configureTalon(m_frontLiftMotor);
        configureTalon(m_rearLiftMotor);
        configureVictor(m_rearPivorMotor);

        m_frontLiftMotor.setInverted(false);
        m_rearLiftMotor.setInverted(false);
        m_rearPivorMotor.setInverted(true);
    }


    private void configureVictor(VictorSPX victor) {
        victor.setNeutralMode(NeutralMode.Brake);
    }

    private void configureTalon(TalonSRX talon) {
        talon.setNeutralMode(NeutralMode.Brake);
    }


    public boolean getFrontTopSensor() {
        return !m_frontTopLimit.get();
    }

    public boolean getFrontBottomSensor() {
        return !m_frontBottomLimit.get();
    }

    public boolean getRearForwardSensor() {
        return !m_rearForwardLimit.get();
    }

    public boolean getRearBackSensor() {
        return !m_rearBackLimit.get();
    }

    public boolean getRearTopSensor() {
        return !m_rearTopLimit.get();
    }

    public boolean getRearBottomSensor() {
        return !m_rearBottomLimit.get();
    }

    public void setFrontLiftMotorSpeed(double speed) {
        m_frontLiftMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setRearLiftMotorSpeed(double speed) {
        m_rearLiftMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setRearPivotMotorSpeed(double speed) {
        m_rearPivorMotor.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public void periodic() {
        updateStatus();
    }


    private void updateStatus() {
        SmartDashboard.putBoolean("[Climber]: Front Top Limit", getFrontTopSensor());
        SmartDashboard.putBoolean("[Climber]: Front Bottom Limit", getFrontBottomSensor());
        SmartDashboard.putBoolean("[Climber]: Rear Forward Limit", getRearForwardSensor());
        SmartDashboard.putBoolean("[Climber]: Rear Back Limit", getRearBackSensor());
        SmartDashboard.putBoolean("[Climber]: Rear Top Limit", getRearTopSensor());
        SmartDashboard.putBoolean("[Climber]: Rear Bottom Limit", getRearBottomSensor());


    }

}
