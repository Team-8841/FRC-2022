package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Shooter;

public class Auto4Ball extends SequentialCommandGroup {

    public Auto4Ball(DriveSubsystem drive, CargoHandler cargoHandler, Shooter shooter) {
// To save this file without it auto formatting use ctrl + k then ctrl + shift + s
        super(// TODO: reprogram this
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
                        new Shoot(cargoHandler), //
                        new WaitCommand(2), //
                        new SetShooterSpeed(0, ShooterConstants.kShooterHoodAngle3, shooter),
                        new WaitCommand(2), //
                        new TurnToAngle(77, drive), //
                        new WaitCommand(2), //
                        new IntakeInAndOut(true, true, cargoHandler), //
                        new DriveToDistance(240, .5, drive), // 50% speed
                        new WaitCommand(2), //
                        new IntakeInAndOut(false, true, cargoHandler), //
                        new WaitCommand(2), //
                        new DriveToDistance(-240, .5, drive), // 50% speed
                        new WaitCommand(2), //
                        new SetShooterSpeed(ShooterConstants.kShooterSpeed2,
                                ShooterConstants.kShooterHoodAngle3, shooter),
                        new WaitCommand(2), //
                        new Shoot(cargoHandler), //
                        new WaitCommand(2), //
                        new SetShooterSpeed(0, ShooterConstants.kShooterHoodAngle3, shooter)//

                ));
    }

}
