package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Shooter;

public class Auto2Ball extends SequentialCommandGroup {

    public Auto2Ball(DriveSubsystem drive, CargoHandler cargoHandler, Shooter shooter) {
        super(//
                new ResetDriveSensors(drive), //
                new SequentialCommandGroup(//
                        new IntakeInAndOut(true, true, cargoHandler), //
                        new WaitCommand(2), //
                        new DriveToDistance(40, .5, drive), // 50% speed
                        new WaitCommand(2), //
                        new IntakeInAndOut(false, true, cargoHandler), //
                        new WaitCommand(2), //
                        new DriveToDistance(-68, .5, drive), // 50% speed
                        new WaitCommand(2), //
                        new SetShooterSpeed(ShooterConstants.kShooterSpeed2,
                                ShooterConstants.kShooterHoodAngle3, shooter),
                        new WaitCommand(2), //
                        new Shoot(cargoHandler)//
                ));
    }

}
