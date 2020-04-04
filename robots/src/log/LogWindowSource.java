package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    /**
     * переменная обозначающая длину очерди сообщений
     */
    private int m_iQueueLength;
    /**
     * Переменная хранящая все сообщения
     */
    private ConcurrentLinkedQueue<LogEntry> m_messages;
    /**
     * Переменая хранящая все логи
     */
    private final ArrayList<LogChangeListener> m_listeners;
    /**
     * переменная хранящая все активные логи
     */
    private volatile LogChangeListener[] m_activeListeners;

    /**
     * Конструктор класса, создаюший массив логов и массив изменений
     * @param iQueueLength переданная длина сообщени
     * @see Logger
     */
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new ConcurrentLinkedQueue<LogEntry>();
        m_listeners = new ArrayList<LogChangeListener>();
    }

    /**
     * Метод обновления активных логов
     * @param listener прослушиватель
     */
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    /**
     * Метод добавления сообщения логирования
     * @param logLevel уровень логирования
     * @param strMessage сообщение логирования
     */
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (m_messages){
            addNewMessage(entry);
        }
        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        if (activeListeners != null) {
            for (LogChangeListener listener : activeListeners) {
                listener.onLogChanged();
            }
        }
    }

    private void addNewMessage(LogEntry message) {
        if (m_messages.size() == m_iQueueLength){
            m_messages.poll();
        }
        m_messages.add(message);
    }

    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        CopyOnWriteArrayList<LogEntry> messages = new CopyOnWriteArrayList<>();
        int i = 0;
        for (LogEntry entry : m_messages) {
            if (i >= startFrom && i <= indexTo) {
                messages.add(entry);
                i++;
            }
        }
        return messages;
    }

    /**
     * Метод прохода по коллекции сообщений
     * @return сообщение из колекции
     */
    public Iterable<LogEntry> all()
    {
        return m_messages;
    }
}
