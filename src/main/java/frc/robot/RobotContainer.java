// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.TurretConstants;
import frc.robot.commands.Auto2Ball;
import frc.robot.commands.Auto4Ball;
import frc.robot.subsystems.CargoHandler;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
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
  private final Compressor m_compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

  // Chooser for auto commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  Command m_autoCommand;

  private static double m_driveStraightSetPoint = 0.0;


  // private final Joystick m_leftJoystick = new Joystick(OIConstants.kLeftjoystickPort);
  // private final Joystick m_rightJoystick = new Joystick(OIConstants.kRightjoystickPort);
  private final XboxController m_pad = new XboxController(OIConstants.kControllerPort);
  private final Joystick m_copilotDS = new Joystick(OIConstants.kCopilotDsPort);


  private final PIDCommand straightDriveCommand = new PIDCommand(
      new PIDController(DriveConstants.kStrightDriveP, DriveConstants.kStrightDriveI,
          DriveConstants.kStrightDriveD),
      m_drive::getHeading, () -> m_driveStraightSetPoint,
      output -> m_drive.arcadeDrive(-m_pad.getLeftY(), output), m_drive);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    m_compressor.enableDigital();
    m_compressor.disable();
    // Default Drive command
    // m_drive.setDefaultCommand(new RunCommand(() -> m_drive.RobotDrive(m_leftJoystick.getY(),
    // -m_rightJoystick.getY(), m_rightJoystick.getX()), m_drive));

    m_drive.setDefaultCommand(new RunCommand(() -> m_drive.RobotDrive(m_pad.getLeftX(),
        m_pad.getLeftY(), m_pad.getRightX(), m_pad.getRightY()), m_drive));

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
      m_shooter.setSetpoint(getShooterAndHood()[0]);
      m_shooter.setHoodAngle(getShooterAndHood()[1]);

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
      m_cargoHandler.setIntakeSolenoid(m_copilotDS.getRawButton(OIConstants.kIntakeSolenoidPort));
      m_cargoHandler.sensorControl(m_copilotDS.getRawButton(OIConstants.kIntakeInPort),
          m_copilotDS.getRawButton(OIConstants.kIntakeOutPort));
    }, m_cargoHandler));


    // Default climber command

    m_climber.setDefaultCommand(new RunCommand(() -> {
      if (m_copilotDS.getRawButton(OIConstants.kClimbModeSwitchPort)) {

        double commandedFrontLiftSpeed = getDesiredFrontLiftSpeed();

        if (m_climber.getTopLimit() && commandedFrontLiftSpeed < 0) {
          m_climber.setLiftSpeed(0);
        } else if (m_climber.getBottomLimit() && commandedFrontLiftSpeed > 0) {
          m_climber.setLiftSpeed(0);
        } else {
          m_climber.setLiftSpeed(commandedFrontLiftSpeed);
        }

        if (m_copilotDS.getRawButton(12)) {
          m_climber.setHangSoleniod(true);
        } else {
          m_climber.setHangSoleniod(false);
        }

      }
    }, m_climber));


    // Default lighting command

    m_lighting.setDefaultCommand(new RunCommand(() -> {

      if (RobotState.isDisabled()) {
        m_lighting.setLightingState(LightingState.Idle);
      } else if (RobotState.isEnabled()) {
        if (getShooterAndHood()[0] > 0) {
          m_lighting.setLightingState(LightingState.Shooting);
        } else {
          m_lighting.setLightingState(LightingState.Active);
        }
      } else if (RobotState.isEStopped()) {
        m_lighting.setLightingState(LightingState.ESTOP);
      }

      if (m_lighting.getLightingState() == LightingState.Idle) {
        m_lighting.setLEDColor(255, 0, 255);
      } else if (m_lighting.getLightingState() == LightingState.Active) {
        if (m_cargoHandler.getQueue1Sensor() && !m_cargoHandler.getQueue2Sensor()
            || !m_cargoHandler.getQueue1Sensor() && m_cargoHandler.getQueue2Sensor()) {
          m_lighting.setLEDColor(0, 0, 255);
        } else if (m_cargoHandler.getQueue1Sensor() && m_cargoHandler.getQueue2Sensor()) {
          m_lighting.setLEDColor(0, 255, 0);
        } else if (!m_cargoHandler.getQueue1Sensor() && !m_cargoHandler.getQueue2Sensor()) {
          m_lighting.setLEDColor(0, 0, 0);
        } else {
          m_lighting.setLEDColor(0, 0, 0);
        }
      } else if (m_lighting.getLightingState() == LightingState.Shooting) {

        if (m_shooter.upToSpeed() && m_shooter.getHoodAngle() == getShooterAndHood()[1]) {
          m_lighting.setLEDColor(255, 255, 255);
        } else {
          m_lighting.setLEDColor(0, 0, 0);
        }
      } else if (m_lighting.getLightingState() == LightingState.ESTOP) {
        m_lighting.setLEDColor(255, 0, 0);
      }
    }, m_lighting));


  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Drive state changers
    /*
     * new JoystickButton(m_rightJoystick, OIConstants.kMechDrivePort).whenHeld(new RunCommand(() ->
     * { m_drive.driveState(DriveState.MECANUM_DRIVE); SmartDashboard.putString("[DT]Drive State",
     * "MECANUM_DRIVE"); })); new JoystickButton(m_rightJoystick, OIConstants.kMechDrivePort)
     * .whenReleased(new RunCommand(() -> { m_drive.driveState(DriveState.TANK_DRIVE);
     * SmartDashboard.putString("[DT]Drive State", "TANK_DRIVE"); }));
     */

    new JoystickButton(m_pad, 5).whenHeld(straightDriveCommand
        .beforeStarting(() -> m_driveStraightSetPoint = m_drive.getHeading(), m_drive));

    new JoystickButton(m_copilotDS, OIConstants.kCompressorSwitchPort)
        .whenHeld(new RunCommand(() -> {
          m_compressor.disable();
          SmartDashboard.putBoolean("Compressor pressure switch",
              m_compressor.getPressureSwitchValue());
        }));
    new JoystickButton(m_copilotDS, OIConstants.kCompressorSwitchPort)
        .whenReleased(new RunCommand(() -> {

          // Kill compressor if we are shooting
          if (getShooterAndHood()[0] > 0) {
            m_compressor.disable();
          } else {
            m_compressor.enableDigital();
          }
          SmartDashboard.putBoolean("Compressor pressure switch",
              m_compressor.getPressureSwitchValue());
        }));

    // Rest gyro maybe fix heading
    new JoystickButton(m_pad, 7).whenHeld(new RunCommand(() -> {
      m_drive.resetHeading();
    }));


    // new JoystickButton(m_rightJoystick, OIConstants.kshootPort).whenHeld(new RunCommand(() -> {
    new JoystickButton(m_pad, 6).whenHeld(new RunCommand(() -> {
      if (m_copilotDS.getRawButton(OIConstants.kIntakeInPort) && m_shooter.upToSpeed()) {
        m_cargoHandler.setQueue2(.4);
      }

      if (m_copilotDS.getRawButton(OIConstants.kIntakeOutPort)) {
        m_cargoHandler.setIntake(-.4);
      } else if (m_copilotDS.getRawButton(OIConstants.kIntakeInPort)) {
        m_cargoHandler.setIntake(.4);
      } else {
        m_cargoHandler.setIntake(0);
      }

      if (getShooterAndHood()[0] > 100) {
        m_cargoHandler.setQueue2(.6);
        m_cargoHandler.setQueue1(.4);
      } else {
        m_cargoHandler.setQueue2(0);
        m_cargoHandler.setQueue1(0);
      }
    }, m_cargoHandler));

  }

  // [0] == Speed [1] == hood angle
  public double[] getShooterAndHood() {
    double angle;
    double speed;
    double knobValue = m_copilotDS.getRawAxis(4); // axis #5 for old knob
    double threshold = 0.010;

    // If Shooter Knob is at 1
    if (knobValue < 0.024 - threshold) {
      angle = 0.75;
      speed = 0.0;
    }
    // If Shooter Knob is at 2
    else if (knobValue >= 0.024 - threshold && knobValue < 0.024 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle1;
      speed = ShooterConstants.kShooterSpeed1;
    }
    // If Shooter Knob is at 3
    else if (knobValue >= 0.047 - threshold && knobValue < 0.047 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle2;
      speed = ShooterConstants.kShooterSpeed2;
    } else if (knobValue >= 0.071 - threshold && knobValue < 0.071 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle3;
      speed = ShooterConstants.kShooterSpeed3;
    } else if (knobValue >= 0.094 - threshold && knobValue < 0.094 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle4;
      speed = ShooterConstants.kShooterSpeed4;
    }
    // If Shooter Knob is at 4
    else if (knobValue >= 0.094 + threshold) {
      angle = ShooterConstants.kShooterHoodAngle5;
      speed = ShooterConstants.kShooterSpeed5;
    } else {
      angle = ShooterConstants.kShooterHoodAngle1;
      speed = 0;
    }
    SmartDashboard.putNumber("RPM: ", speed);
    SmartDashboard.putNumber("Angle: ", angle);
    return new double[] {speed, angle};
    // use this to read knob values & not turn on motor- return new double[] {0, .75};
  }

  public double getDesiredTurretSpeed() {
    double turretStickX = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick1XPort);
    if (turretStickX < 0.03) {
      return -0.9;
    } else if (turretStickX > 0.07) {
      return 0.9;
    } else {
      return 0;
    }
  }

  public double getDesiredFrontLiftSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick3XPort);
    SmartDashboard.putNumber("[Climber] Front Lift Y", ClimberStickY);
    if (ClimberStickY < 0.035) {
      return -1;
    } else if (ClimberStickY > 0.075) {
      return 1;
    } else {
      return 0;
    }
  }


  public double getDesiredRearLiftSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick2YPort);
    SmartDashboard.putNumber("[Climber] Rear Lift Y", ClimberStickY);
    if (ClimberStickY < 0.035) {
      return -1;
    } else if (ClimberStickY > 0.075) {
      return 1;
    } else {
      return 0;
    }
  }

  public double getDesiredRearPivotSpeed() {
    double ClimberStickY = m_copilotDS.getRawAxis(OIConstants.kMiniJoystick1XPort);
    SmartDashboard.putNumber("[Climber] pivot Y", ClimberStickY);
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

    if (m_copilotDS.getRawButton(OIConstants.kAutoMode1Port)) {
      m_autoCommand = new Auto2Ball(m_drive, m_cargoHandler, m_shooter);
    } else if (m_copilotDS.getRawButton(OIConstants.kAutoMode2Port)) {
      m_autoCommand = new Auto4Ball(m_drive, m_cargoHandler, m_shooter);
    }

    return m_autoCommand;
  }
}
