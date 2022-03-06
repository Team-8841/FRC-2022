package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Shooter;

public class Auto2Ball extends SequentialCommandGroup {

        public Auto2Ball(DriveSubsystem drive, CargoHandler cargoHandler, Shooter shooter) {
                super(//
                                new ResetDriveSensors(drive), //

                                new SequentialCommandGroup(new DriveToDistance(4, .35, drive)) //
                // new ParallelRaceGroup(new IntakeInAndOut(true, true, cargoHandler))


                ); //


                /*
                 * new ParallelRaceGroup(new SetShooterSpeed(3800, .55, shooter), new
                 * SequentialCommandGroup(new WaitCommand(2), new Shoot(cargoHandler)))
                 */


        }

}
