// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.AutoTemplate1;
import frc.robot.commands.AutoTemplate2;
import frc.robot.commands.AutoTemplate3;
import frc.robot.commands.AutoTemplate4;
import frc.robot.commands.AutoTemplate5;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_drive = new DriveSubsystem();
  private final Compressor m_compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

  //Chooser for auto commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  private final Joystick m_leftJoystick = new Joystick(OIConstants.kLeftjoystickPort);
  private final Joystick m_rightJoystick = new Joystick(OIConstants.kRightjoystickPort);
  private final Joystick m_copilotDS = new Joystick(OIConstants.kCopilotDsPort);

  private static double m_driveStrightSetpoint = 0;

  private final PIDCommand strightDriveCommand = new PIDCommand(
    new PIDController (
      DriveConstants.kStrightDriveP,
      DriveConstants.kStrightDriveI, 
      DriveConstants.kStrightDriveD
    ),

    m_drive::getHeading,

    () -> m_driveStrightSetpoint,

    output -> m_drive.arcadeDrive(-m_rightJoystick.getY(), output),
    m_drive
  );

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    m_compressor.disable();


    m_drive.setDefaultCommand(
      new RunCommand( () -> m_drive.tankDrive(-m_leftJoystick.getY(), -m_rightJoystick.getY()), m_drive)
    );

    m_chooser.setDefaultOption("Default Auto", new AutoTemplate1(m_drive));
    m_chooser.addOption("Auto2", new AutoTemplate2(m_drive));
    m_chooser.addOption("Auto3", new AutoTemplate3(m_drive));
    m_chooser.addOption("Auto4", new AutoTemplate4(m_drive));
    m_chooser.addOption("Auto5", new AutoTemplate5(m_drive));
    SmartDashboard.putData("Auto Modes", m_chooser);

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Drive Straight
    new JoystickButton(m_rightJoystick, OIConstants.kstraightDrivePort).whenHeld(strightDriveCommand.beforeStarting( () -> m_driveStrightSetpoint = m_drive.getHeading(), m_drive));

      // Compressor
    new JoystickButton(m_copilotDS, OIConstants.kCompressorSwitchPort).whenHeld( new RunCommand ( () -> {m_compressor.disable(); SmartDashboard.putBoolean("Compressor status", m_compressor.getPressureSwitchValue());}));
    new JoystickButton(m_copilotDS, OIConstants.kCompressorSwitchPort).whenReleased( new RunCommand ( () -> {m_compressor.enableDigital(); SmartDashboard.putBoolean("Compressor status", m_compressor.getPressureSwitchValue());}));
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  }
}
