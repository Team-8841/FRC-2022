package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;

public class Auto4Ball extends SequentialCommandGroup {

    public Auto4Ball(DriveSubsystem drive) {
        /*
         * new WaitCommand(.5), new DriveToDistance(20, drive), new TurnToAngle(90, drive),
         */
        super(//
                new ResetDriveSensors(drive), //
                new SequentialCommandGroup(//
                        new ParallelCommandGroup(//
                        // deploy intake
                        // turn on intake Queue1 and Queue2
                        ), //
                        new DriveToDistance(40, drive), // 50% speed
                        new WaitCommand(0.25), //
                        new DriveToDistance(-68, drive), // 50% speed
                        new WaitCommand(0.5), //
                        // shoot *2
                        new TurnToAngle(77, drive), //
                        new WaitCommand(.25), //
                        new DriveToDistance(240, drive), // 50% speed
                        new WaitCommand(0.5), //
                        new DriveToDistance(-240, drive), // 50% speed
                        new WaitCommand(0.5) //
                // shoot *2

                ));
    }

}
