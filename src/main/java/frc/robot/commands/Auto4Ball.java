package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Shooter;

public class Auto4Ball extends SequentialCommandGroup {

        public Auto4Ball(DriveSubsystem drive, CargoHandler cargoHandler, Shooter shooter) {
                // To save this file without it auto formatting use ctrl + k then ctrl + shift + s
                super(// TODO: reprogram this
                      // To save this file without it auto formatting use ctrl + k then ctrl + shift + s
                                new ResetDriveSensors(drive),
                                new ParallelRaceGroup(
                                        new IntakeInAndOut(true, true, cargoHandler),
                                        new SetShooterSpeed(3650, .35, shooter),
                                        new SequentialCommandGroup(
                                                new WaitCommand(0.5),
                                                        new DriveToDistance(28, .5,drive)
                                                )
                                ),
                                new ParallelRaceGroup(
                                        new SetShooterSpeed(3700, .35, shooter),
                                        new SequentialCommandGroup(
                                                new WaitCommand(0.5),
                                                new ParallelRaceGroup(
                                                        new WaitCommand(1.5),
                                                        new Shoot(cargoHandler))
                                                )
                                ),
                                new ParallelRaceGroup(
                                        new SetShooterSpeed(0, .53, shooter),
                                        new IntakeInAndOut(true, true, cargoHandler),
                                        new SequentialCommandGroup(
                                                new DriveToDistance(57, .7, drive),
                                                new WaitCommand(2),
                                                new DriveToDistance(-74, .7, drive)
                                        )
                                ),
                                new ParallelRaceGroup(
                                        new SetShooterSpeed(3500, .45, shooter),
                                        new SequentialCommandGroup(
                                                new WaitCommand(0.75),
                                                new Shoot(cargoHandler)
                                        )
                                )


                );
        }

}
