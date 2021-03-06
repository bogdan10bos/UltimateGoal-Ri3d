package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Shooter {
    public DcMotor motor;
    public Servo lift, push, angleChanger;

    private double endTime;
    private boolean on, pushing, tower;
    private static final double MAX_POWER = 1.0;
    private static final double MAX_TIME = 788;

    private static final double POS_LIFT_DOWN = 0.73;
    private static final double POS_LIFT_UP = 0.40;

    private static final double POS_ANGLE_TOWER = 0.35;
    private static final double POS_ANGLE_POWERSHOT = 0.9;

    private static final double POS_PUSH_RING = 0.7;
    private static final double POS_PUSH_RESET = 0.45;

    public ElapsedTime runtime;

    public Shooter(DcMotor _left, Servo _push, Servo _lift, Servo _angleChanger) {
        motor = _left;
        push = _push;
        lift = _lift;
        angleChanger = _angleChanger;

        on = false;
        pushing = false;
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setPosition(POS_LIFT_DOWN);

        push.setPosition(POS_PUSH_RESET);

        tower = true;
        angleChanger.setPosition(POS_ANGLE_TOWER);

        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void changeState(boolean change) {
        if(change) {
            on = !on;
            if(on)
                motor.setPower(MAX_POWER);
            else
                motor.setPower(0.0);
        }
    }

    public void setState(double power) {
        motor.setPower(MAX_POWER * power);
        if(power < 0.001)   on = false;
        else                on = true;
    }

    public void pushRing(boolean change) {
        if( Math.abs(lift.getPosition() - POS_LIFT_DOWN) < 0.001 ) return;
        if(!pushing && change) {
            push.setPosition(POS_PUSH_RING);
            pushing = true;
            endTime = runtime.milliseconds() + MAX_TIME;
        }

        if(runtime.milliseconds() > endTime) {
            pushing = false;
            push.setPosition(POS_PUSH_RESET);
        }
    }

    public void Up (boolean change){
        if(change)  lift.setPosition(POS_LIFT_UP);
    }

    public void Down (boolean change){
        if(change)  lift.setPosition(POS_LIFT_DOWN);
    }
    
    public void anglePowershot() {
        angleChanger.setPosition(POS_ANGLE_POWERSHOT);
    }

    public void angleTower() {
        angleChanger.setPosition(POS_ANGLE_TOWER);
    }

    public void changeAngleState(boolean change) {
        if(change) {
            tower = !tower;
            if(tower)   angleChanger.setPosition(POS_ANGLE_TOWER);
            else        angleChanger.setPosition(POS_ANGLE_POWERSHOT);
        }
    }
}
