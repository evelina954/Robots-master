package gui;

import javax.swing.*;
import java.awt.Point;


public class Robot extends JPanel
{
    /**
     * переменная показывающая позицию жука по координате Х
     */
    public volatile double robotPositionX;
    /**
     * переменная показывающая позицию жука по координате У
     */
    public volatile double robotPositionY;
    /**
     * переменная показывающая напавление жука
     */
    public volatile double robotDirection;

    /**
     * переменная показывающая позицию еды по координате Х
     */
    public volatile int targetPositionX;
    /**
     * переменная показывающая позицию еды по координате У
     */
    public volatile int targetPositionY;

    /**
     * переменная максимальной скорости жука
     */
    private static final double maxVelocity = 0.1;
    /**
     * переменная максимальной скорости жука
     */
    private static final double maxAngularVelocity = 0.001;

    /**
     * Метод задающий начальную позицию игры, то есть где находится еда и жук, и напрвавление жука
     */
    protected void setStartPosition() {

        robotPositionX = 100;
        robotPositionY = 100;
        robotDirection = 0;
        targetPositionX = 150;
        targetPositionY = 100;
    }

    /**
     * Метод меняющий положение еды
     * @param p приходящая точка, когда мы нажали на кнопку мыши
     */
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

    /**
     * Метод вычисляющий направление жука, через тангенс угла между жуком и едой
     * @param fromX позиция жука
     * @param fromY позиция жука
     * @param toX позиция еды
     * @param toY позиция жука
     * @return возращает угол в радианах
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Метод обновления событий, движения объектов
     */
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

    /**
     * Метод изменяющий координаты жука, вызывается в {@link Robot#onModelUpdateEvent()}
     * @param velocity скорость жука
     * @param angularVelocity угловая скорость жука
     * @param duration направление жука
     */
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        double newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        double newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        robotPositionX = newX;
        robotPositionY = newY;
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
        robotDirection = newDirection;
    }

    /**
     * Статический метод преобразования угла в радианы от 0 до 2P
     * @param angle угол между жуком и едой {@link Robot#angleTo(double, double, double, double)}
     * @return нормализованный угол между жуком и едой
     */
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

    /**
     * Метод вычисляющий расстояние от жука до еды
     * @param x1 координата жука
     * @param y1 координата жука
     * @param x2 координата еды
     * @param y2 координата еды
     * @return расстояние от жука до еды
     */
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

}
