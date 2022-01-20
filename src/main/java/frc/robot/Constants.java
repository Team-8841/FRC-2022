// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class DriveConstants {
        public static final int k_leftFrontMotorPort = 1; //Spark max
        public static final int k_leftBackMotorPort = 2; //Spark max
        public static final int k_rightFrontMotorPort = 3; //Spark max
        public static final int k_rightBackMotorPort = 4; //Spark max

        // default for neo is 80
        public static final int kCurrentLimit = 60;

        public static final int kEncoderCPR = 42; //Neo encoder CPR

        public static final double kWheelDiameterInches = 6.0; // Wheel diameter in inches
        public static final double kWheelGearRatio = 8.0; // Drivetrain to wheel gear ratio
        public static final double kEncoderDistancePerPulse = 
            //Assumes the encoders are directly attached to the wheel shafts
            ((kWheelDiameterInches * Math.PI) / (double) kEncoderCPR) * kWheelGearRatio;

        public static final double kStrightDriveP = 0.1;
        public static final double kStrightDriveI = 0;
        public static final double kStrightDriveD = 0.005;

    }


    public static final class OIConstants {
        public static final int kLeftjoystickPort = 0;
        public static final int kRightjoystickPort = 1;
        public static final int kCopilotDsPort = 2;

        //right joystick inputs
        public static final int kMechDrivePort = 2;
        public static final int kshootPort = 1;


        //copilot inputs
        public static final int kVisionSwitchPort = 1;//old climber switch
        public static final int kCompressorSwitchPort = 2;//old Compressor switch
        public static final int kIntakeInPort = 4;
        public static final int kIntakeOutPort = 5;
        public static final int kClimberSolenoidPort = 6;//old Arm Up
        public static final int kClimberSwitchPort = 9;//old elevator overide switch
        public static final int kIntakeSolenoidPort = 12;

        public static final int kTurretJoystickXPort = 4;
        public static final int kClimberJoystickYPort = 2;
    }



}
