package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CargoHandler;

public class IntakeInAndOut extends CommandBase {

    private CargoHandler m_cargoHandler;

    private boolean m_in, m_down;


    public IntakeInAndOut(boolean in, boolean down, CargoHandler cargoHandler) {
        m_cargoHandler = cargoHandler;
        m_in = in;
        m_down = down;

        addRequirements(cargoHandler);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_cargoHandler.setIntakeSolenoid(m_down);
        m_cargoHandler.sensorControl(m_in, false);
    }


    @Override
    public void end(boolean interrupted) {
        m_cargoHandler.setIntake(0);
        m_cargoHandler.setQueue1(0);
        m_cargoHandler.setQueue2(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
