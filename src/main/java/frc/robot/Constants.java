// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

        public static final class DriveConstants {

                // Motor controllers
                public static final int k_leftFrontMotorPort = 1; // Spark max (can bus)
                public static final int k_leftBackMotorPort = 2; // Spark max (can bus)
                public static final int k_rightFrontMotorPort = 3; // Spark max (can bus)
                public static final int k_rightBackMotorPort = 4; // Spark max (can bus)

                public static final int kCurrentLimit = 60; // default for neo is 80
                public static final double kDTRampRate = 0.5; // Drivetrain open loop ramp rate |
                                                              // tune to slow
                                                              // acceleration (higher = slower)

                public static final int kEncoderCPR = 42; // Neo encoder CPR

                public static final double kWheelDiameterInches = 6.0; // Wheel diameter in inches
                public static final double kWheelGearRatio = 8.0; // Drivetrain to wheel gear ratio

                public static final double kEncoderDistancePerPulse =
                                // Assumes the encoders are directly attached to the wheel shafts
                                ((kWheelDiameterInches * Math.PI) / (double) kEncoderCPR)
                                                * kWheelGearRatio;

                public static final double kStrightDriveP = 0.1; // These PID values may not be
                                                                 // needed
                public static final double kStrightDriveI = 0;
                public static final double kStrightDriveD = 0.005;

        }

        public static final class ShooterConstants {
                public static final int kShooterMotorPort = 5; // Spark max (can bus)
                public static final int kShooterSlavePort = 6; // Spark max (can bus)

                public static final int kHoodServo1Port = 1; // Hood servo 1
                public static final int kHoodServo2Port = 2; // Hood servo 2

                public static final int kCurrentLimit = 80; // May need to tune

                public static final int kEncoderCPR = 42; // NEO encoder 42 CPR
                public static final double kP = 0; // 0.000230;
                public static final double kI = 0; // 0.000001;
                public static final double kD = 0;
                public static final double kIZone = 0; // 330;
                public static final double kFF = 1; // 0.000165;
                public static final double kMaxOutput = 0.5;
                public static final double kMinOutput = 0;
                public static final double kAllowedError = 200;

                public static final double kShooterSpeed1 = 1000;
                public static final double kShooterSpeed2 = 2500;
                public static final double kShooterSpeed3 = 5000;

                public static final double kShooterHoodAngle1 = 0;// TODO: Tune
                public static final double kShooterHoodAngle2 = 0.6;// TODO: Tune
                public static final double kShooterHoodAngle3 = 0.68;// TODO: Tune

                public static final double kDefaultHoodAngle = 0.6;// TODO: Tune
        }

        public static final class TurretConstants {
                public static final int kTurretMotorPort = 7;

                public static final int kLeftSensorPort = 2;
                public static final int kRightSensorPort = 3;

                public static final double kP = 0.085;// TODO: Tune
        }


        public static final class CargoHandlerConstants {
                public static final int kIntakeMotorPort = 8;

                public static final int kQueue1MotorPort = 9;
                public static final int kQueue2MotorPort = 10;

                public static final int kIntakeSolenoidPort = 1;

                public static final int kQueue1SensorPort = 0;
                public static final int kQueue2SensorPort = 1;

        }

        public static final class OIConstants {

                public static final int kLeftjoystickPort = 0; // DS port 0
                public static final int kRightjoystickPort = 1; // DS port 1
                public static final int kCopilotDsPort = 2; // DS port 2

                // right joystick inputs
                public static final int kMechDrivePort = 2; // Thumb button
                public static final int kshootPort = 1; // Trigger

                // copilot inputs
                // TODO: Figureout what switch is what
                public static final int kVisionSwitchPort = 1;
                public static final int kCompressorSwitchPort = 2;
                public static final int kIntakeInPort = 4;
                public static final int kIntakeOutPort = 5;
                public static final int kClimberSolenoidPort = 6;
                public static final int kClimberSwitchPort = 9;
                public static final int kIntakeSolenoidPort = 12;

                public static final int kTurretJoystickXPort = 4;
                public static final int kClimberJoystickYPort = 2;
        }

}
