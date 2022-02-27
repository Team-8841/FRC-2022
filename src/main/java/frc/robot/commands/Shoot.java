package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CargoHandler;

public class Shoot extends CommandBase {
    private CargoHandler m_cargoHandler;

    public Shoot(CargoHandler cargoHandler) {
        m_cargoHandler = cargoHandler;

        addRequirements(cargoHandler);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_cargoHandler.setIntake(.4);
        m_cargoHandler.setQueue1(.35);
        m_cargoHandler.setQueue2(.5);
    }

    @Override
    public void end(boolean interrupted) {
        m_cargoHandler.setQueue1(0);
        m_cargoHandler.setQueue2(0);
        m_cargoHandler.setIntake(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
