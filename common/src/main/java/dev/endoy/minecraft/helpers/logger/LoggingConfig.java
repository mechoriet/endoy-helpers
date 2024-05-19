package dev.endoy.minecraft.helpers.logger;

import org.slf4j.event.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggingConfig
{

    private static final Map<String, Level> LOGGING_LEVELS = new ConcurrentHashMap<>();

    public static void setLoggingLevel( String packageName, Level level )
    {
        LOGGING_LEVELS.put( packageName, level );
    }

    public static void setLoggingLevel( Class<?> clazz, Level level )
    {
        LOGGING_LEVELS.put( clazz.getName(), level );
    }

    public static Level getLoggingLevel( Class<?> clazz )
    {
        return LOGGING_LEVELS.entrySet()
            .stream()
            .filter( it -> it.getKey().equals( clazz.getName() ) || clazz.getPackageName().startsWith( it.getKey() ) )
            .findFirst()
            .map( Map.Entry::getValue )
            .orElse( Level.INFO );
    }
}
