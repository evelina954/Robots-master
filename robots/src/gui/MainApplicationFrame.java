package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
/**
 * Класс, отвечающий за создание главного окна и обеспечивающий взаимодействие всех окон
 * @see GameVisualizer
 * @see GameWindow
 */

public class MainApplicationFrame extends JFrame
{
    /** Главное окно */
    private final JDesktopPane desktopPane = new JDesktopPane();
    /**
     * Конструктор - создает объект главного окна, закрепляет остальные окна на главное
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setLocation(300,10);
        gameWindow.setSize(500,  500);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(MainApplicationFrame.this,
                        "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Метод создает окно для логгирования
     * @return окно логгирования
     * @see MainApplicationFrame#MainApplicationFrame()
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Метод добавляет окно на {@link MainApplicationFrame#desktopPane}
     * @param frame окно, которое следует закрепить на главном окне
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private void addToMenu(JMenu menu, String txt) {
        JMenuItem currentItem = new JMenuItem(txt, KeyEvent.VK_S);
        currentItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        menu.add(currentItem);
    }

    private JMenu addReturnTab(String name, int key, String description) {
        JMenu tab = new JMenu(name);
        tab.setMnemonic(key);
        tab.getAccessibleContext().setAccessibleDescription(description);
        return tab;
    }

    /**
     * Метод создает Menu Bar с режимами отображения окон, с тестами и с настройками логгирования
     * * @return Menu Bar в {@link MainApplicationFrame#MainApplicationFrame()}
     */
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = addReturnTab("Режим отображения",
                KeyEvent.VK_V, "Управление режимом отображения");
        addToMenu(lookAndFeelMenu, "Системная схема");
        addToMenu(lookAndFeelMenu, "Универсальная схема");
        JMenu testMenu = addReturnTab("Тесты", KeyEvent.VK_T,
                "Тестовые команды.");

        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_ESCAPE);
        exitItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(MainApplicationFrame.this,
                    "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
            testMenu.add(addLogMessageItem);
        }

        testMenu.add(exitItem);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    /**
     * Устанавливает режим отображения окон при нажатии пользователем соответсвующей кнопки
     * @param className режим отображения, передается из {@link MainApplicationFrame#generateMenuBar()}
     */
    private void setLookAndFeel(String className)
    {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }
}
