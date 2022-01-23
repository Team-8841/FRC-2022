package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CargoHandler extends SubsystemBase {
    //Create motor controller instances
    //Create a solenoid instance
    //Create digital input instances
    //Add to robot controller


    public CargoHandler() {
        //Set motor controller neutral mode
    }

    @Override
    public void periodic(){
        updateStatus();
    }

    public void setInakeSolenoid(boolean state) {
        //toggle the solenoid
    }

    public void setIntake(double speed) {
        //set the motor speed
    }

    public void setConveyor(double speed) {
        //set the motor speed
    }

    //Create functions for seonsor logic

    public void updateStatus() {
        //Put sensor information on dashboard
    }

}
