package log;

/**
 * Интерфейс, предоставляющие базовые методы для работы с прослушивателем логов
 * @see LogWindowSource
 */

public interface LogChangeListener
{
    /**
     * Метод опредеяет то, что журнал логгирования изменился
     * @see LogWindowSource
     */
    public void onLogChanged();
}
