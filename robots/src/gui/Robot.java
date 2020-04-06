package gui;

import javax.swing.*;
import java.awt.Point;


public class Robot extends JPanel
{
    public volatile double robotPositionX;
    public volatile double robotPositionY;
    public volatile double robotDirection;
    public volatile int targetPositionX;
    public volatile int targetPositionY;
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.003;

    protected void setStartPosition() {

        robotPositionX = 100;
        robotPositionY = 100;
        robotDirection = 0;
        targetPositionX = 150;
        targetPositionY = 100;
    }

    protected void setTargetPosition(Point p)
    {
        targetPositionX = p.x;
        targetPositionY = p.y;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }
    public int getTargetPositionY() {
        return targetPositionY;
    }

    public double getRobotPositionX(){
        return robotPositionX;
    }
    public double getRobotPositionY(){
        return robotPositionY;
    }

    public double getRobotDirection(){
        return robotDirection;
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent()
    {
        double distance = distance(targetPositionX, targetPositionY,
                robotPositionX, robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(robotPositionX, robotPositionY, targetPositionX, targetPositionY);
        double angularVelocity = 0;
        double angleBetweenTargetRobot = asNormalizedRadians(angleToTarget - robotDirection);
        if (angleBetweenTargetRobot < Math.PI) {
            angularVelocity = maxAngularVelocity;
        } else {
            angularVelocity = -maxAngularVelocity;
        }

        if (Math.abs(angleToTarget - robotDirection) < 0.05) {

            moveRobot(velocity, angularVelocity, 10);
        } else {
            if (distance < 15) {
                moveRobot(0, angularVelocity, 10);
            } else {
                moveRobot(velocity / 2, angularVelocity, 10);
            }
        }
//        if (angleToTarget > robotDirection)
//        {
//            angularVelocity = maxAngularVelocity;
//        }
//        if (angleToTarget < robotDirection)
//        {
//            angularVelocity = -maxAngularVelocity;
//        }
//
//        moveRobot(velocity, angularVelocity, 10);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        double newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        double newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        robotPositionX = newX;
        robotPositionY = newY;
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
        robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0) {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI) {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

}
