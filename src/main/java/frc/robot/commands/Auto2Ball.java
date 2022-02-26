package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;

public class Auto2Ball extends SequentialCommandGroup {

    public Auto2Ball(DriveSubsystem drive) {
        super(//
                new ResetDriveSensors(drive), //
                new SequentialCommandGroup(//
                        new ParallelCommandGroup(//
                        // deploy intake
                        // turn on intake
                        ), //
                        new DriveToDistance(40, drive), // 50% speed
                        new WaitCommand(0.25), //
                        new DriveToDistance(-68, drive), // 50% speed
                        // spin shooter to 3000
                        new WaitCommand(0.5) //
                // feed
                // shoot *2
                ));
    }

}
