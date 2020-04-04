package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

/**
 * Класс логгирования программы
 */
public class LogWindow extends JInternalFrame implements LogChangeListener
{
    /**
     * окно логирования
     * @see LogWindowSource
     */
    private LogWindowSource m_logSource;
    /**
     * переменная значения логирования
     */
    private TextArea m_logContent;

    /**
     * Конструктор - создание нового объекта с определенными значениями, наследуеммые от JInternalFrame
     * @param logSource - окошко
     * Тут же создается панель <b>panel</b>, на которой будут видный логи
     * @see LogWindowSource
     */
    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                int result = JOptionPane.showConfirmDialog(LogWindow.this,
                        "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    LogWindow.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    e.getInternalFrame().dispose();
                }
                else if (result == JOptionPane.NO_OPTION) {
                    LogWindow.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    /**
     * Метод обновления данных логирования, он проходит по всем сообщениям и добавляет их в <b>content</b>,
     * После записи всех сообщение он выводить их на экран
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    /**
     * Переопределение метода изменения логирования
     * Пробегается по элементам <b>updateLogContent</b>, и ставит их в очередь выполнения
     */
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
