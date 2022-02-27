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
import frc.robot.Constants.TurretConstants;
import frc.robot.commands.Auto2Ball;
import frc.robot.commands.Auto4Ball;
import frc.robot.commands.AutoLeaveTarmac;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.DriveState;
import frc.robot.subsystems.Lighting;
import frc.robot.subsystems.Lighting.LightingState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
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
  private final CargoHandler m_cargoHandler = new CargoHandler();
  private final Vision m_vision = new Vision();
  private final Turret m_turret = new Turret();
  private final Climber m_climber = new Climber();
  private final Lighting m_lighting = new Lighting();
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
    m_turret.setDefaultCommand(new RunCommand(() -> {
      if (m_copilotDS.getRawButton(OIConstants.kVisionSwitchPort)) {
        // vision Control
        double headingError = m_vision.getTargetHorizontalOffset();
        double turretSpeed = TurretConstants.kP * headingError;
        double minSpeed = 0.09;
        double treshold = 0.25;

        // if we are not within the treshold and the set speed is too low set it to min speed
        if (Math.abs(turretSpeed) < minSpeed && Math.abs(headingError) > treshold) {
          if (turretSpeed < 0) {
            turretSpeed = -minSpeed;
          } else {
            turretSpeed = minSpeed;
          }
        }
        m_turret.setSpeed(turretSpeed);
      } else if (!m_copilotDS.getRawButton(OIConstants.kVisionSwitchPort)
          && !m_copilotDS.getRawButton(OIConstants.kClimbModeSwitchPort)) {
        // Manual Control
        m_turret.setSpeed(getDesiredTurretSpeed());
      }
    }, m_turret));


    // Default CargoHandler command

    m_cargoHandler.setDefaultCommand(new RunCommand(() -> {
      // m_cargoHandler.setIntakeSolenoid(m_copilotDS.getRawButton(OIConstants.kIntakeSolenoidPort));
      m_cargoHandler.sensorControl(m_copilotDS.getRawButton(OIConstants.kIntakeInPort),
          m_copilotDS.getRawButton(OIConstants.kIntakeOutPort));
    }, m_cargoHandler));


    // Default climber command
    m_climber.setDefaultCommand(new RunCommand(() -> {
      if (m_copilotDS.getRawButton(OIConstants.kClimbModeSwitchPort)) {

        double commandedFrontLiftSpeed = getDesiredFrontLiftSpeed();
        double commandedRearLiftSpeed = getDesiredRearLiftSpeed();
        double commandedRearPivotSpeed = getDesiredRearPivotSpeed();

        if (m_climber.getFrontTopSensor() && commandedFrontLiftSpeed > 0) {
          m_climber.setFrontLiftMotorSpeed(0);
        } else if (m_climber.getFrontBottomSensor() && commandedFrontLiftSpeed < 0) {
          m_climber.setFrontLiftMotorSpeed(0);
        } else {
          m_climber.setFrontLiftMotorSpeed(commandedFrontLiftSpeed);
        }

        if (m_climber.getRearTopSensor() && commandedRearLiftSpeed > 0) {
          m_climber.setRearLiftMotorSpeed(0);
        } else if (m_climber.getRearBottomSensor() && commandedRearLiftSpeed < 0) {
          m_climber.setRearLiftMotorSpeed(0);
        } else {
          m_climber.setRearLiftMotorSpeed(commandedRearLiftSpeed);
        }

        if (m_climber.getRearForwardSensor() && commandedRearPivotSpeed > 0) {
          m_climber.setRearPivotMotorSpeed(0);
        } else if (m_climber.getRearBackSensor() && commandedRearPivotSpeed < 0) {
          m_climber.setRearPivotMotorSpeed(0);
        } else {
          m_climber.setRearPivotMotorSpeed(commandedRearPivotSpeed);
        }
      } else {
        m_climber.setFrontLiftMotorSpeed(0);
        m_climber.setRearLiftMotorSpeed(0);
        m_climber.setRearPivotMotorSpeed(0);
      }
    }));

    // Default lighting command
    m_lighting.setDefaultCommand(new RunCommand(() -> {
      if (getDesiredShooterSpeed() > 0) {
        m_lighting.setLightingState(LightingState.Shooting);
      } else {
        m_lighting.setLightingState(LightingState.Active);
      }

      if (m_lighting.getLightingState() == LightingState.Shooting) {
        if (m_shooter.upToSpeed()) {
          m_lighting.setLEDColor(133, 100, 100);
        } else if (m_lighting.getLightingState() == LightingState.Active) {
          m_lighting.setLEDColor(120, 100, 100);
        }
      }
    }));

    // Auto mode selector
    m_chooser.setDefaultOption("Default Leave Tarmac", new AutoLeaveTarmac(m_drive));
    m_chooser.addOption("2 Ball Auto", new Auto2Ball(m_drive, m_cargoHandler, m_shooter));
    m_chooser.addOption("4 Ball Auto", new Auto4Ball(m_drive, m_cargoHandler, m_shooter));
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


    new JoystickButton(m_rightJoystick, OIConstants.kshootPort).whenHeld(new RunCommand(() -> {
      if (m_copilotDS.getRawButton(OIConstants.kIntakeInPort)) {
        m_cargoHandler.setQueue2(.4);
      }

      if (m_copilotDS.getRawButton(OIConstants.kIntakeOutPort)) {
        m_cargoHandler.setIntake(-.4);
      } else if (m_copilotDS.getRawButton(OIConstants.kIntakeInPort)) {
        m_cargoHandler.setIntake(.4);
      } else {
        m_cargoHandler.setIntake(0);
      }

      if (getDesiredShooterSpeed() > 100) {
        m_cargoHandler.setQueue2(.6);
        m_cargoHandler.setQueue1(.4);
      } else {
        m_cargoHandler.setQueue2(0);
        m_cargoHandler.setQueue1(0);
      }
    }, m_cargoHandler));

  }

  public double getDesiredTurretSpeed() {
    double turretStickX = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick1XPort);
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
    double knobValue = m_copilotDS.getRawAxis(5);
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
    double knobValue = m_copilotDS.getRawAxis(4);
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
    else if (knobValue >= 0.024 + threshold && knobValue < 0.035 + threshold) {
      speed = ShooterConstants.kShooterSpeed2;
    } // If Shooter Knob is at 4
    else if (knobValue >= 0.035 + threshold && knobValue < 0.055 + threshold) {
      speed = ShooterConstants.kShooterSpeed3;
    } else if (knobValue >= 0.055 + threshold) {
      speed = ShooterConstants.kShooterSpeed4;
    } else {
      speed = 0;
    }

    return speed;
  }

  public double getDesiredFrontLiftSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick2YPort);
    SmartDashboard.putNumber("[Climber] Front Lift Y", ClimberStickY);
    if (ClimberStickY < 0.035) {
      return -0.4;
    } else if (ClimberStickY > 0.075) {
      return 0.4;
    } else {
      return 0;
    }
  }


  public double getDesiredRearLiftSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick3YPort);
    SmartDashboard.putNumber("[Climber] Rear Lift Y", ClimberStickY);
    if (ClimberStickY < 0.035) {
      return -0.4;
    } else if (ClimberStickY > 0.075) {
      return 0.4;
    } else {
      return 0;
    }
  }

  public double getDesiredRearPivotSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick1YPort);
    SmartDashboard.putNumber("[Climber] Front pivot Y", ClimberStickY);
    if (ClimberStickY < 0.035) {
      return -0.4;
    } else if (ClimberStickY > 0.075) {
      return 0.4;
    } else {
      return 0;
    }
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
