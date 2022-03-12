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
                      // To save this file without it auto formatting use ctrl + k then ctrl + shift
                      // + s
                                new ResetDriveSensors(drive),
                                new ParallelRaceGroup(new IntakeInAndOut(true, true, cargoHandler),
                                                new SetShooterSpeed(3800, .39, shooter),
                                                new SequentialCommandGroup(new WaitCommand(0.5),
                                                                new DriveToDistance(28, .6,
                                                                                drive))),
                                new ParallelRaceGroup(new SetShooterSpeed(3800, .39, shooter),
                                                new SequentialCommandGroup(new WaitCommand(0.5),
                                                                new ParallelRaceGroup(
                                                                                new WaitCommand(1.5),
                                                                                new Shoot(cargoHandler))

                                                )),
                                new ParallelRaceGroup(new SetShooterSpeed(0, .39, shooter),
                                                new IntakeInAndOut(true, true, cargoHandler),
                                                new SequentialCommandGroup(
                                                                new DriveToDistance(62, .7, drive),
                                                                new WaitCommand(1),
                                                                new DriveToDistance(-62, .7,
                                                                                drive))),
                                new ParallelRaceGroup(new SetShooterSpeed(3800, .39, shooter),
                                                new SequentialCommandGroup(new WaitCommand(0.5),
                                                                new Shoot(cargoHandler)))


                );
        }

}
