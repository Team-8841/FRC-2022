// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
  private final CANSparkMax m_leftFrontMotor =
      new CANSparkMax(DriveConstants.k_leftFrontMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_leftBackMotor =
      new CANSparkMax(DriveConstants.k_leftBackMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_rightFrontMotor =
      new CANSparkMax(DriveConstants.k_rightFrontMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_rightBackMotor =
      new CANSparkMax(DriveConstants.k_rightBackMotorPort, MotorType.kBrushless);

  private final RelativeEncoder m_encoder;

  private AHRS m_gyro;

  // Left side drivetrain
  private final MotorControllerGroup m_leftMotors =
      new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);

  // Right side drivetain
  private final MotorControllerGroup m_rightMotors =
      new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);

  // The drivetain
  public enum DriveState {
    TANK_DRIVE, MECANUM_DRIVE, MECANUM_DS
  }

  private DriveState state;

  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

  private final MecanumDrive m_mecDrive =
      new MecanumDrive(m_leftFrontMotor, m_leftBackMotor, m_rightFrontMotor, m_rightBackMotor);

  /** Creates a new ExampleSubsystem. */
  public DriveSubsystem() {

    // Configure The motor controllers
    configureSparkCoast(m_leftFrontMotor);
    configureSparkBreak(m_leftBackMotor);
    configureSparkCoast(m_rightFrontMotor);
    configureSparkBreak(m_rightBackMotor);
    m_drive.setMaxOutput(1); // TODO: Tune max speed

    m_encoder = m_leftFrontMotor.getEncoder();

    // Setup gyro
    try {
      m_gyro = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException e) {
      DriverStation.reportError("Error instantiating navX MXP " + e.getMessage(), true);
    }

    // Set inital drive DriveState
    state = DriveState.MECANUM_DRIVE;

  }

  private void configureSparkCoast(CANSparkMax sparkMax) {
    sparkMax.restoreFactoryDefaults();
    sparkMax.setOpenLoopRampRate(DriveConstants.kDTRampRate);
    sparkMax.setSmartCurrentLimit(DriveConstants.kCurrentLimit);
    sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
  }

  private void configureSparkBreak(CANSparkMax sparkMax) {
    sparkMax.restoreFactoryDefaults();
    sparkMax.setOpenLoopRampRate(DriveConstants.kDTRampRate);
    sparkMax.setSmartCurrentLimit(DriveConstants.kCurrentLimit);
    sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
  }

  @Override
  public void periodic() {
    updateStatus();
  }

  public void RobotDrive(double leftx, double lefty, double rightx, double righty) {

    m_drive.feed();
    m_mecDrive.feed();

    switch (state) {
      case TANK_DRIVE:
        m_rightFrontMotor.setInverted(false);
        m_rightBackMotor.setInverted(false);
        m_leftFrontMotor.setInverted(false);
        m_leftBackMotor.setInverted(false);

        m_drive.tankDrive(lefty, righty);
        break;

      case MECANUM_DRIVE:

        m_rightFrontMotor.setInverted(true);
        m_rightBackMotor.setInverted(true);
        m_leftFrontMotor.setInverted(false);
        m_leftBackMotor.setInverted(false);

        m_mecDrive.driveCartesian(leftx, lefty, -rightx, m_gyro.getAngle());
        break;
    }
  }

  public void arcadeDrive(double forward, double rotate) {
    m_rightFrontMotor.setInverted(false);
    m_rightBackMotor.setInverted(false);
    m_leftFrontMotor.setInverted(true);
    m_leftBackMotor.setInverted(true);

    m_drive.arcadeDrive(forward, rotate);
  }

  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }

  public DriveState driveState(DriveState newState) {
    state = newState;

    return state;
  }

  public DriveState driveState() {

    return state;
  }

  public double getHeading() {
    try {
      return m_gyro.getAngle();
    } catch (RuntimeException e) {
      return 0;
    }
  }

  public void resetHeading() {
    try {
      m_gyro.reset();
    } catch (RuntimeException e) {

    }
  }

  public double getDistance() {
    return m_encoder.getPosition();
  }

  public void resetEncoder() {
    m_encoder.setPosition(0);
  }

  public void updateStatus() {
    SmartDashboard.putNumber("[DT] Heading", getHeading());
    SmartDashboard.putNumber("[DT] Distance", getDistance());
  }

}
