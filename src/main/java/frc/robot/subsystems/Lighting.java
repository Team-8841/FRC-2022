package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MiscConstants;

public class Lighting extends SubsystemBase {

    public enum RobotState {
        Idle, Active
    }

    private RobotState m_rState;

    private final AddressableLED m_led = new AddressableLED(MiscConstants.kLEDPort);

    private final AddressableLEDBuffer m_ledBuffer =
            new AddressableLEDBuffer(MiscConstants.kLEDBufferLength);

    private int animationTimer = 0;
    private int changeTimer = 0;

    private int m_rainbowFirstPixelHue;

    public void setRobotState(RobotState state) {
        m_rState = state;
    }

    public RobotState getRobotState() {
        return m_rState;
    }

    private void rainbow() {
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;

            m_ledBuffer.setHSV(i, hue, 255, 128);
        }

        m_rainbowFirstPixelHue += 360;
        m_rainbowFirstPixelHue %= 180;
    }


    @Override
    public void periodic() {
        updateStatus();

        rainbow();

        m_led.setData(m_ledBuffer);
    }

    private void updateStatus() {
        SmartDashboard.putString("[Lighting]: Robot State", getRobotState().toString());
    }

}
