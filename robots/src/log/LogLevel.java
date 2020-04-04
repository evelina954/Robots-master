package log;

/**
 * Уровни для дебага
 * @see LogWindowSource
 */

public enum LogLevel
{
    /**
     * Мелкие сообщения при отладке
     */
    Trace(0),
    /**
     * Подробные сообщения, используемые во время отладки приложения
     */
    Debug(1),
    /**
     * Информационные сообщения о том, что происходит в приложении
     */
    Info(2),
    /**
     * Предупреждения о возникновении нежелательной ситуации
     */
    Warning(3),
    /**
     * Ошибки, при которых приложние способно продолжать работать
     */
    Error(4),
    /**
     * Фатальные ошибки, обычно приводящие к заввершению работы приложения
     */
    Fatal(5);

    /**
     *Поле, определяющее режим дебага
     */
    private int m_iLevel;

    /**
     * Конструктор - по числу устанавливает уровень дебага
     * @param iLevel уровень дебага (в виде числа)
     */
    private LogLevel(int iLevel)
    {
        m_iLevel = iLevel;
    }

    /**
     * Возвращает текущий уровень дебага
     * @return уровень дебага (в виде числа)
     */
    public int level()
    {
        return m_iLevel;
    }
}

