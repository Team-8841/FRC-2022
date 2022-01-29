package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class CargoHandler extends SubsystemBase {
    private final VictorSPX intakeMotor = new VictorSPX(IntakeConstants.intakeMotorPort);
    private final VictorSPX feederStageOne = new VictorSPX(IntakeConstants.feederMotorPortOne);
    private final VictorSPX feederStageTwo = new VictorSPX(IntakeConstants.feederMotorPortTwo);
    private final DigitalInput sensorStageOne =
            new DigitalInput(IntakeConstants.feederSensorStageOne);
    private final DigitalInput sensorStageTwo =
            new DigitalInput(IntakeConstants.feederSensorStageTwo);
    private boolean ballInFeederMiddle = false;
    private double shootingStarted = 0;
    private boolean shooting = false;
    // Create motor controller instances
    // Create a solenoid instance
    // Create digital input instances
    // Add to robot controller

    public enum DriveState {
        MOTOR_OFF(0), MOTOR_HALF(0.5), MOTOR_FULL(1);

        private final double value;

        DriveState(final double newVal) {
            value = newVal;
        }

        public double getValue() {
            return value;
        }
    }

    public CargoHandler() {
        setMotorSpeed(feederStageTwo, false);
    }

    @Override
    public void periodic() {
        if (!shooting) {
            setMotorSpeed(feederStageTwo, false);
            setMotorState();
        } else {
            if (System.currentTimeMillis()
                    - shootingStarted >= IntakeConstants.shootingDurationMilliseconds) {
                shooting = false;
            } else {
                sendBallToShooter();
            }
        }
        updateStatus();
    }

    public void setMotorState() {
        if (sensorStageTwo.get()) {
            ballInFeederMiddle = false;
        }
        boolean ballAtBottom = ballInFeederMiddle || sensorStageOne.get();
        // there are two balls in the system
        if (sensorStageTwo.get() && ballAtBottom) {
            setMotorSpeed(intakeMotor, false);
            setMotorSpeed(feederStageOne, false);
        } else if (!sensorStageTwo.get() && ballAtBottom) { // There is a ball moving from stage 1
                                                            // to stage two
            setMotorSpeed(intakeMotor, true);
            setMotorSpeed(feederStageOne, true);
            ballInFeederMiddle = true;
        } else if (sensorStageTwo.get() && !ballAtBottom) { // There is a ball at stage 2 and not
                                                            // stage 1
            setMotorSpeed(intakeMotor, true);
            setMotorSpeed(feederStageOne, false);
        } else if (!sensorStageTwo.get() && !ballAtBottom) { // there are no balls in the system
            setMotorSpeed(intakeMotor, true);
            setMotorSpeed(feederStageOne, true);
        }
    }

    public void sendBallToShooter() {
        setMotorSpeed(intakeMotor, !ballInFeederMiddle);
        setMotorSpeed(feederStageOne, false);
        setMotorSpeed(feederStageTwo, true);
    }

    public void shoot() {
        shooting = true;
        shootingStarted = System.currentTimeMillis();
    }

    public void setMotorSpeed(VictorSPX motor, boolean on) {
        // set the motor speed
        if (on) {
            motor.set(VictorSPXControlMode.PercentOutput, DriveState.MOTOR_FULL.getValue());
        } else {
            motor.set(VictorSPXControlMode.PercentOutput, DriveState.MOTOR_OFF.getValue());
        }
    }

    // Create functions for seonsor logic

    public void updateStatus() {
        // Put sensor information on dashboard
        SmartDashboard.putBoolean("[CARGO] Stage 1", sensorStageOne.get());
        SmartDashboard.putBoolean("[CARGO] Ball in Middle", ballInFeederMiddle);
        SmartDashboard.putBoolean("[CARGO] Stage 2", sensorStageTwo.get());
        SmartDashboard.putBoolean("[CARGO] Shooting", shooting);
    }

}
