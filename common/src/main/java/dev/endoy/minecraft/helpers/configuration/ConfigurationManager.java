package dev.endoy.minecraft.helpers.configuration;

import be.dieterblancke.configuration.api.FileStorageType;
import be.dieterblancke.configuration.api.IConfiguration;
import be.dieterblancke.configuration.yaml.YamlConfiguration;
import be.dieterblancke.configuration.yaml.comments.CommentType;
import dev.endoy.minecraft.helpers.EndoyApplication;
import dev.endoy.minecraft.helpers.injector.Comment;
import dev.endoy.minecraft.helpers.injector.Configuration;
import dev.endoy.minecraft.helpers.injector.Value;
import dev.endoy.minecraft.helpers.utils.ReflectionUtils;
import dev.endoy.minecraft.helpers.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ConfigurationManager
{

    private final EndoyApplication endoyApplication;
    private final Map<String, IConfiguration> configurations = new ConcurrentHashMap<>();

    public void save( Class<?> configurationClass )
    {
        if ( !configurationClass.isAnnotationPresent( Configuration.class ) )
        {
            throw new InvalidConfigurationException( "Class " + configurationClass.getName() + " is not annotated with @Configuration" );
        }

        Configuration configurationAnnotation = configurationClass.getAnnotation( Configuration.class );
        IConfiguration configuration = this.getOrLoadConfig( configurationAnnotation.fileType(), configurationAnnotation.filePath() );

        for ( Field declaredField : configurationAnnotation.getClass().getDeclaredFields() )
        {
            if ( declaredField.isAnnotationPresent( Value.class ) )
            {
                Value value = declaredField.getAnnotation( Value.class );
                String path = value.path().isEmpty() ? Utils.convertCamelCaseToDotNotation( declaredField.getName() ) : value.path();
                Object fieldValue;

                try
                {
                    fieldValue = ReflectionUtils.getFieldValue( declaredField, configurationClass );
                }
                catch ( IllegalAccessException e )
                {
                    continue;
                }

                if ( configuration instanceof YamlConfiguration yamlConfiguration && declaredField.isAnnotationPresent( Comment.class ) )
                {
                    Comment comment = declaredField.getAnnotation( Comment.class );

                    yamlConfiguration.setComment( path, String.join( "\n", comment.value() ), CommentType.BLOCK );
                }

                configuration.set( path, fieldValue );
            }
        }
    }

    public IConfiguration getOrLoadConfig( FileStorageType fileStorageType, String filePath )
    {
        return configurations.computeIfAbsent( filePath, key -> IConfiguration.loadConfiguration(
            fileStorageType,
            new File( endoyApplication.getDataFolder(), filePath )
        ) );
    }
}
