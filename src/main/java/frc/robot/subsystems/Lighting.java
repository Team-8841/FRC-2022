package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MiscConstants;

// Guide: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/misc/addressable-leds.html
public class Lighting extends SubsystemBase {

    public enum LightingState {
        Idle, Active, Shooting
    }

    private LightingState m_rState = LightingState.Idle;

    private AddressableLED m_led = new AddressableLED(MiscConstants.kLEDPort);

    private AddressableLEDBuffer m_ledBuffer =
            new AddressableLEDBuffer(MiscConstants.kLEDBufferLength);

    private int m_rainbowFirstPixelHue;

    public void Lighting() {
        updateStatus();

        m_led.setLength(m_ledBuffer.getLength());
    }

    public void setLightingState(LightingState state) {
        m_rState = state;
    }

    public LightingState getLightingState() {
        return m_rState;
    }

    public void rainbow() {
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;

            m_ledBuffer.setHSV(i, hue, 255, 128);
        }

        m_rainbowFirstPixelHue += 360;
        m_rainbowFirstPixelHue %= 180;

        m_led.setData(m_ledBuffer);
    }

    public void setLEDColor(int h, int s, int v) {
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            m_ledBuffer.setHSV(i, h, s, v);
        }

        m_led.setData(m_ledBuffer);
    }

    @Override
    public void periodic() {
        updateStatus();

        /*
         * if (m_rState == LightingState.Idle) { rainbow(); m_led.setData(m_ledBuffer); }
         */

    }

    private void updateStatus() {
        SmartDashboard.putString("[Lighting]: Robot State", getLightingState().toString());
    }

}
