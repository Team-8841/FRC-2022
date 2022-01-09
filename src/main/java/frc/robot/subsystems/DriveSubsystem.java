// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
  private final CANSparkMax m_leftFrontMotor = new CANSparkMax(DriveConstants.k_leftFrontMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_leftBackMotor = new CANSparkMax(DriveConstants.k_leftBackMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_rightFrontMotor = new CANSparkMax(DriveConstants.k_rightFrontMotorPort, MotorType.kBrushless);
  private final CANSparkMax m_rightBackMotor = new CANSparkMax(DriveConstants.k_rightBackMotorPort, MotorType.kBrushless);

  private final RelativeEncoder m_encoder;

  private AHRS m_gyro;

  //Left side drivetrain
  private final MotorControllerGroup m_leftMotors = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);

  //Right side drivetain
  private final MotorControllerGroup m_rightMotors = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);

  //The drivetain
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);  


  /** Creates a new ExampleSubsystem. */
  public DriveSubsystem() {
    configureSpark(m_leftFrontMotor);
    configureSpark(m_leftBackMotor);
    configureSpark(m_rightFrontMotor);
    configureSpark(m_rightBackMotor);
    m_drive.setMaxOutput(0.7);

    m_encoder = m_leftFrontMotor.getEncoder();

    try { 
      m_gyro = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException e) {
      DriverStation.reportError("Error instantiating navX MXP " + e.getMessage(), true);
    }

  }

  private void configureSpark(CANSparkMax sparkMax) {
    sparkMax.restoreFactoryDefaults();
    sparkMax.setOpenLoopRampRate(0.5);//TODO: tune to slow acceleration (higher = slower)
    sparkMax.setSmartCurrentLimit(DriveConstants.kCurrentLimit);
    sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
  }

  @Override
  public void periodic() {
    updateStatus();
  }

  public void tankDrive(double left, double right) {
    m_drive.tankDrive(left, right);
  }


  public void arcadeDrive(double forward, double rotate) {
    m_drive.arcadeDrive(forward, rotate);
  }

  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
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