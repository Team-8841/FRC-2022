// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.AutoTemplate1;
import frc.robot.commands.AutoTemplate2;
import frc.robot.commands.AutoTemplate3;
import frc.robot.commands.AutoTemplate4;
import frc.robot.commands.AutoTemplate5;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.DriveState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_drive = new DriveSubsystem();
  private final Shooter m_shooter = new Shooter();
  // private final CargoHandler m_cargoHandler = new CargoHandler();
  private final Vision m_vision = new Vision();
  // private final Turret m_turret = new Turret();
  // private final Compressor m_compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

  // Chooser for auto commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  private final Joystick m_leftJoystick = new Joystick(OIConstants.kLeftjoystickPort);
  private final Joystick m_rightJoystick = new Joystick(OIConstants.kRightjoystickPort);
  private final Joystick m_copilotDS = new Joystick(OIConstants.kCopilotDsPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // m_compressor.disable();

    // Default Drive command
    m_drive.setDefaultCommand(new RunCommand(() -> m_drive.RobotDrive(m_leftJoystick.getY(),
        -m_rightJoystick.getY(), m_rightJoystick.getX()), m_drive));

    // Default Vision command

    m_vision.setDefaultCommand(new RunCommand(() -> {
      m_vision.updateStatus();

      if (m_copilotDS.getRawButton(OIConstants.kVisionSwitchPort)) { // Vision mode
        m_vision.setDriveMode(false);
      } else {
        m_vision.setDriveMode(true);
      }
    }, m_vision));



    // Default Shooter command
    m_shooter.setDefaultCommand(new RunCommand(() -> {
      // m_shooter.tune(); // TODO: comment this out after tuning shooter
      m_shooter.setSetpoint(getDesiredShooterSpeed());
      m_shooter.setHoodAngle(getDesiredShooterHoodAngle());
    }, m_shooter));

    // Default Turret command
    /*
     * m_turret.setDefaultCommand(new RunCommand(() -> { if
     * (m_copilotDS.getRawButton(OIConstants.kVisionSwitchPort)) { // Auto targeting goodness double
     * headingError = m_vision.getTargetHorizontalOffset(); double turretSpeed = TurretConstants.kP
     * * headingError; double minSpeed = 0.09; double threshold = 0.25;
     * 
     * if (Math.abs(turretSpeed) < minSpeed && Math.abs(headingError) > threshold) { if (turretSpeed
     * < 0) { turretSpeed = -minSpeed; } else { turretSpeed = minSpeed; } }
     * m_turret.setSpeed(turretSpeed); } else { // Manual control
     * m_turret.setSpeed(getDesiredTurretSpeed()); } }, m_turret));
     */

    // Default CargoHandler command
    /*
     * m_cargoHandler.setDefaultCommand(new RunCommand(() -> {
     * m_cargoHandler.setIntakeSolenoid(m_copilotDS.getRawButton(OIConstants.kIntakeSolenoidPort));
     * m_cargoHandler.sensorControl(m_copilotDS.getRawButton(OIConstants.kIntakeInPort),
     * m_copilotDS.getRawButton(OIConstants.kIntakeOutPort)); }, m_cargoHandler));
     */

    // Auto mode selector
    m_chooser.setDefaultOption("Default Auto", new AutoTemplate1(m_drive));
    m_chooser.addOption("Auto2", new AutoTemplate2(m_drive));
    m_chooser.addOption("Auto3", new AutoTemplate3(m_drive));
    m_chooser.addOption("Auto4", new AutoTemplate4(m_drive));
    m_chooser.addOption("Auto5", new AutoTemplate5(m_drive));
    SmartDashboard.putData("Auto Modes", m_chooser);

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Drive state changers
    new JoystickButton(m_rightJoystick, OIConstants.kMechDrivePort).whenHeld(new RunCommand(() -> {
      m_drive.driveState(DriveState.MECANUM_DRIVE);
      SmartDashboard.putString("[DT]Drive State", "MECANUM_DRIVE");
    }));
    new JoystickButton(m_rightJoystick, OIConstants.kMechDrivePort)
        .whenReleased(new RunCommand(() -> {
          m_drive.driveState(DriveState.TANK_DRIVE);
          SmartDashboard.putString("[DT]Drive State", "TANK_DRIVE");
        }));

    /*
     * new JoystickButton(m_rightJoystick, OIConstants.kshootPort).whenHeld(new RunCommand(() -> {
     * if (m_copilotDS.getRawButton(OIConstants.kIntakeOutPort)) { m_cargoHandler.setIntake(-.4); }
     * else if (m_copilotDS.getRawButton(OIConstants.kIntakeInPort)) { m_cargoHandler.setIntake(.4);
     * } else { m_cargoHandler.setIntake(0); }
     * 
     * if (getDesiredShooterSpeed() > 100) { m_cargoHandler.setQueue2(.6);
     * m_cargoHandler.setQueue1(.4); } else { m_cargoHandler.setQueue2(0);
     * m_cargoHandler.setQueue1(0); } }, m_cargoHandler));
     */
  }

  public double getDesiredTurretSpeed() {
    double turretStickX = m_copilotDS.getRawAxis(OIConstants.kTurretJoystickXPort);
    if (turretStickX < 0.03) {
      return -0.5;
    } else if (turretStickX > 0.07) {
      return 0.5;
    } else {
      return 0;
    }
  }

  public double getDesiredShooterHoodAngle() {
    double angle;
    double knobValue = m_copilotDS.getRawAxis(1);
    double threshold = 0.010;

    // If Shooter Knob is at 1
    if (knobValue < 0.024 - threshold) {
      angle = ShooterConstants.kShooterHoodAngle2;
    }
    // If Shooter Knob is at 2
    else if (knobValue >= 0.024 - threshold && knobValue < 0.024 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle1;
    }
    // If Shooter Knob is at 3
    else if (knobValue >= 0.024 + threshold && knobValue < 0.055 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle2;
    }
    // If Shooter Knob is at 4
    else if (knobValue >= 0.055 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle3;
    } else {
      angle = ShooterConstants.kShooterHoodAngle2;
    }

    return angle;
  }

  public double getDesiredShooterSpeed() {
    double speed;
    double knobValue = m_copilotDS.getRawAxis(1);
    double threshold = 0.010;

    // If Shooter Knob is at 1
    if (knobValue < 0.024 - threshold) {
      speed = 0;
    }
    // If Shooter Knob is at 2
    else if (knobValue >= 0.024 - threshold && knobValue < 0.024 + threshold) {
      speed = ShooterConstants.kShooterSpeed1;
    }
    // If Shooter Knob is at 3
    else if (knobValue >= 0.024 + threshold && knobValue < 0.055 + threshold) {
      speed = ShooterConstants.kShooterSpeed2;
    } // If Shooter Knob is at 4
    else if (knobValue >= 0.055 + threshold) {
      speed = ShooterConstants.kShooterSpeed3;
    } else {
      speed = 0;
    }

    return speed;
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
