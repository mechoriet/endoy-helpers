package dev.endoy.minecraft.helpers.injector;

import be.dieterblancke.configuration.api.FileStorageType;
import be.dieterblancke.configuration.api.IConfiguration;
import dev.endoy.minecraft.helpers.EndoyApplication;
import dev.endoy.minecraft.helpers.utils.ReflectionUtils;
import dev.endoy.minecraft.helpers.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;
import java.util.Arrays;

@RequiredArgsConstructor
public class ConfigurationInjector
{

    private final Injector injector;
    private final EndoyApplication endoyApplication;

    public static ConfigurationInjector forInjector( Injector injector, EndoyApplication endoyApplication )
    {
        return new ConfigurationInjector( injector, endoyApplication );
    }

    void inject()
    {
        this.injector.initializeInjectablesOfType(
            Configuration.class,
            configurations -> configurations.forEach( configuration -> this.injectConfigurationFields(
                configuration.instance().getClass(),
                configuration.instance(),
                configuration.annotation()
            ) )
        );
    }

    void injectConfigurationFields( Class<?> clazz, Object instance, Configuration configuration )
    {
        this.injectConfigurationFields(
            clazz,
            instance,
            endoyApplication.getConfigurationManager().getOrLoadConfig( configuration.fileType(), configuration.filePath() )
        );
    }

    void injectConfigurationFields( Class<?> clazz, Object instance )
    {
        this.injectConfigurationFields(
            clazz,
            instance,
            endoyApplication.getConfigurationManager().getOrLoadConfig( FileStorageType.YAML, "config.yml" )
        );
    }

    private void injectConfigurationFields( Class<?> clazz, Object instance, IConfiguration configuration )
    {
        Arrays.stream( clazz.getDeclaredFields() )
            .filter( field -> field.isAnnotationPresent( Value.class ) )
            .forEach( field ->
            {
                Value value = field.getAnnotation( Value.class );
                Object configValue = getConfigurationValue( configuration, field.getName(), value );

                try
                {
                    ReflectionUtils.setFieldValue( field, instance, configValue );
                }
                catch ( IllegalAccessException e )
                {
                    throw new FailedInjectionException( e );
                }
            } );
    }

    public Object getConfigurationValue( Parameter parameter, Value value )
    {
        return getConfigurationValue(
            endoyApplication.getConfigurationManager().getOrLoadConfig( FileStorageType.YAML, "config.yml" ),
            parameter.getName(),
            value
        );
    }

    public Object getConfigurationValue( IConfiguration configuration, String name, Value value )
    {
        return configuration.get( value.path().isEmpty() ? Utils.convertCamelCaseToDotNotation( name ) : value.path() );
    }
}
