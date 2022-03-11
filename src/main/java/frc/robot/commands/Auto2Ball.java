package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Shooter;

public class Auto2Ball extends SequentialCommandGroup {

        public Auto2Ball(DriveSubsystem drive, CargoHandler cargoHandler, Shooter shooter) {
                super(
                        // To save this file without it auto formatting use ctrl + k then ctrl + shift + s
                        new ResetDriveSensors(drive),
                        new ParallelRaceGroup(
                                new IntakeInAndOut(true, true, cargoHandler),
                                new SequentialCommandGroup(
                                        new WaitCommand(1),
                                        new DriveToDistance(20, .4, drive)
                                )
                        ),
                        new ParallelRaceGroup(
                                new SetShooterSpeed(3700, .39, shooter),
                                new SequentialCommandGroup(
                                        new WaitCommand(2),
                                        new Shoot(cargoHandler)
                                )
                        ),
                        new SetShooterSpeed(0, .55, shooter)
                );

        }
}
