package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CargoHandlerConstants;

public class CargoHandler extends SubsystemBase {
    private final VictorSPX m_intakeMotor = new VictorSPX(CargoHandlerConstants.kIntakeMotorPort);
    private final VictorSPX m_queueMotor1 = new VictorSPX(CargoHandlerConstants.kQueue1MotorPort);
    private final VictorSPX m_queueMotor2 = new VictorSPX(CargoHandlerConstants.kQueue2MotorPort);

    private final Solenoid m_intakeArm =
            new Solenoid(PneumaticsModuleType.CTREPCM, CargoHandlerConstants.kIntakeSolenoidPort);

    private final DigitalInput m_queueSensor1 =
            new DigitalInput(CargoHandlerConstants.kQueue1SensorPort);
    private final DigitalInput m_queueSensor2 =
            new DigitalInput(CargoHandlerConstants.kQueue2SensorPort);

    public CargoHandler() {
        configureVictor(m_intakeMotor);
        configureVictor(m_queueMotor1);
        configureVictor(m_queueMotor2);

        m_intakeMotor.setInverted(false);
        m_queueMotor1.setInverted(true);
        m_queueMotor2.setInverted(false);
    }

    @Override
    public void periodic() {
        updateStatus();
    }

    private void configureVictor(VictorSPX victor) {
        victor.setNeutralMode(NeutralMode.Brake);
    }


    public void setIntakeSolenoid(boolean state) {
        m_intakeArm.set(state);
    }

    public boolean getIntakeSolenoidDown() {
        return m_intakeArm.get();
    }

    public void setIntake(double speed) {
        m_intakeMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setQueue1(double speed) {
        m_queueMotor1.set(ControlMode.PercentOutput, speed);
    }

    public void setQueue2(double speed) {
        m_queueMotor2.set(ControlMode.PercentOutput, speed);
    }

    public boolean getQueue1Sensor() {
        return !m_queueSensor1.get();
    }

    public boolean getQueue2Sensor() {
        return !m_queueSensor2.get();
    }

    public void updateStatus() {
        SmartDashboard.putBoolean("[CH] Queue 1 Sensor", getQueue1Sensor());
        SmartDashboard.putBoolean("[CH] Queue 2 Sensor", getQueue2Sensor());
    }


    public void sensorControl(boolean intakeIn, boolean intakeOut) {
        double intakeSpeed = .5;
        double queue1Speed = .4;
        double queue2Speed = .3;

        // if (getIntakeSolenoidDown()) {
        if (intakeOut) {
            setIntake(-intakeSpeed);
            setQueue1(-queue1Speed);
            setQueue2(-queue2Speed);
        } else if (intakeIn) {
            // No cargo
            if (!getQueue1Sensor() & !getQueue2Sensor()) {
                setIntake(intakeSpeed);
                setQueue1(queue1Speed);
                setQueue2(queue2Speed);
            }
            // cargo in queue 1
            else if (getQueue1Sensor() & !getQueue2Sensor()) {
                setIntake(intakeSpeed);
                setQueue1(queue1Speed);
                setQueue2(queue2Speed);
            }
            // cargo in queue 2
            else if (!getQueue1Sensor() & getQueue2Sensor()) {
                setIntake(intakeSpeed);
                setQueue1(queue1Speed);
                setQueue2(0);
            }
            // cargo in queue 1 and 2
            else if (getQueue1Sensor() & getQueue2Sensor()) {
                setIntake(-intakeSpeed);
                setQueue1(0);
                setQueue2(0);
            }
        } else {
            setIntake(0);
            setQueue1(0);
            setQueue2(0);
        }
        // }
    }
}
