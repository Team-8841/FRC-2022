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
                public static final double kWheelGearRatio = 7.6; // Drivetrain to wheel gear ratio

                public static final double kEncoderDistancePerPulse =
                                // Assumes the encoders are directly attached to the wheel shafts
                                ((kWheelDiameterInches * Math.PI) / (double) kEncoderCPR)
                                                * kWheelGearRatio;

                public static final double kStrightDriveP = 0.05;
                public static final double kStrightDriveI = 0;
                public static final double kStrightDriveD = 0.007;

        }

        public static final class ShooterConstants {
                public static final int kShooterMotorPort = 5; // Spark max (can bus)
                public static final int kShooterSlavePort = 6; // Spark max (can bus)

                public static final int kHoodServo1Port = 8; // Hood servo 1 (PWM)
                public static final int kHoodServo2Port = 9; // Hood servo 2 (PWM)

                public static final int kCurrentLimit = 80; // May need to tune

                public static final int kEncoderCPR = 42; // NEO encoder 42 CPR
                public static final double kP = 0.000240; // 0.000240; old - 0.000230
                public static final double kI = 0.0000001; // 0.0000001; old - 0.000001
                public static final double kD = 0;
                public static final double kIZone = 330; // 330;
                public static final double kFF = 0.000165; // 0.000165;
                public static final double kMaxOutput = 1;
                public static final double kMinOutput = 0;
                public static final double kAllowedError = 200; // TODO: Tune


                // Fender Shot - 80% 3500RPM
                // Tarmac Shot - 55% 3600RPM
                // Line Shot - 40% 3700RPM
                // Safe Shot 25% 3900RPM

                public static final double kShooterSpeed1 = 3500; // Fender 3500
                public static final double kShooterSpeed2 = 3550; // Tarmac 3600
                public static final double kShooterSpeed3 = 3700; // Line 3700
                public static final double kShooterSpeed4 = 3900; // Safe 3900
                public static final double kShooterSpeed5 = 2500; // haha funni

                public static final double kShooterHoodAngle1 = .77; // Fender .79
                public static final double kShooterHoodAngle2 = .55; // Tarmac .55
                public static final double kShooterHoodAngle3 = .41; // Line .41
                public static final double kShooterHoodAngle4 = .25; // Safe .25
                public static final double kShooterHoodAngle5 = .30; // haha funni
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

                public static final int kIntakeSolenoidPort = 3;

                public static final int kQueue1SensorPort = 0;
                public static final int kQueue2SensorPort = 1;

        }

        public static final class ClimberConstants {
                public static final int kMasterLiftMotor = 11;
                public static final int kSlaveLiftMotor = 12;

                public static final int kHangerSolenoidPort = 7;

                public static final int kTopLimitSensorPort = 9;
                public static final int kBottomLimitSensorPort = 8;
        }

        public static final class MiscConstants {
                public static final int kLEDPort = 6;
                public static final int kLEDBufferLength = 104; // Default is 60 leds 104 on robot
        }

        public static final class OIConstants {

                public static final String kControlMode = "gamepad"; // Options"gmaepad", "joystcks"

                public static final int kLeftjoystickPort = 0; // DS port 0
                public static final int kRightjoystickPort = 1; // DS port 1
                public static final int kCopilotDsPort = 2; // DS port 2
                public static final int kControllerPort = 3; // Gamepad

                // right joystick inputs
                public static final int kMechDrivePort = 2; // Thumb button
                public static final int kshootPort = 1; // Trigger

                // copilot inputs
                // TODO: Figureout what switch is what
                public static final int kVisionSwitchPort = 14;
                public static final int kCompressorSwitchPort = 3;
                public static final int kClimbModeSwitchPort = 2;
                public static final int kIntakeInPort = 4;
                public static final int kIntakeOutPort = 5;
                public static final int kClimberSwitchPort = 9;
                public static final int kIntakeSolenoidPort = 13;
                public static final int kAutoMode1Port = 8;
                public static final int kAutoMode2Port = 6;

                public static final int kMiniJoystick1XPort = 2;
                public static final int kMiniJoystick1YPort = 3;

                public static final int kMiniJoystick2XPort = 1;
                public static final int kMiniJoystick2YPort = 0;

                public static final int kMiniJoystick3XPort = 7;
                public static final int kMiniJoystick3YPort = 8;


        }

}
