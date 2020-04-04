package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    /**
     * переменная таймер, определяющая время работы
     */
    private Timer m_timer = initTimer();

    /**
     * Статический метод создания таймера
     * @return этот созданый таймер
     */
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Robot robot = new Robot();

    /**
     * Конструктор, который при вызове ставит жука и еду на начальные позиции,
     * включает таймер и начинает прослушивать соббытия нажатия кнопок мыши и обрабатывает их
     */
    public GameVisualizer() 
    {
        robot.setStartPosition();
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                robot.setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    /**
     * Метод сброса тайиера, и задавание нового для перерисовки объектов
     */
    public void stopTimer() {
        m_timer.cancel();
        m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    /**
     * Статический метод, увеличивающий переменную на 0.5
     * @param value значение которое нужно увеличить
     * @return возращает принятое значение + 0.5
     */
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    /**
     * Переопределяем метод рисование объектов
     * @param g графика отрисовки
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(robot.getRobotPositionX()), round(robot.getRobotPositionY()), robot.getRobotDirection());
        drawTarget(g2d, robot.getTargetPositionX(), robot.getTargetPositionY());
    }

    /**
     * Метод рисования закрашенного овала
     * @param g графика отрисовки
     * @param centerX центр тела по Х
     * @param centerY центр тела по У
     * @param diam1 диаметрг овала для Х
     * @param diam2 диаметр овала для У
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Метод рисования овала
     * @param g графика отрисовки
     * @param centerX центр тела по Х
     * @param centerY центр тела по У
     * @param diam1 диаметр овала для Х
     * @param diam2 диаметр овала для У
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Метод отрисовки жука
     * @param g графика отрисовки
     * @param x координата жука Х
     * @param y координата жука У
     * @param direction направление жука, его разворот на плоскости от 0 до 2Р
     */
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(robot.getRobotPositionX());
        int robotCenterY = round(robot.getRobotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    /**
     * Метод отрисовки еды по заданным координатам
     * @param g графика отрисовки
     * @param x координата еды по Х
     * @param y координата еды по У
     */
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
