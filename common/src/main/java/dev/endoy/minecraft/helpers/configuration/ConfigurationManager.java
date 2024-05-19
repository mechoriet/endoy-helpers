package dev.endoy.minecraft.helpers.configuration;

import be.dieterblancke.configuration.api.FileStorageType;
import be.dieterblancke.configuration.api.IConfiguration;
import be.dieterblancke.configuration.api.ISection;
import be.dieterblancke.configuration.yaml.YamlConfiguration;
import be.dieterblancke.configuration.yaml.comments.CommentType;
import dev.endoy.minecraft.helpers.EndoyApplication;
import dev.endoy.minecraft.helpers.injector.Comment;
import dev.endoy.minecraft.helpers.injector.Configuration;
import dev.endoy.minecraft.helpers.injector.ConfigurationSection;
import dev.endoy.minecraft.helpers.injector.Value;
import dev.endoy.minecraft.helpers.utils.ReflectionUtils;
import dev.endoy.minecraft.helpers.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ConfigurationManager
{

    private final EndoyApplication endoyApplication;
    private final Map<String, IConfiguration> configurations = new ConcurrentHashMap<>();

    public void createDefault( Class<?> configClass )
    {
        this.validateConfig( configClass );

        Configuration configuration = configClass.getAnnotation( Configuration.class );
        File file = new File( endoyApplication.getDataFolder(), configuration.filePath() );

        if ( !file.exists() )
        {
            try
            {
                if ( !file.getParentFile().exists() )
                {
                    file.getParentFile().mkdirs();
                }

                file.createNewFile();
                this.save( configClass.getDeclaredConstructors()[0].newInstance() );
            }
            catch ( IOException | InstantiationException | IllegalAccessException | InvocationTargetException e )
            {
                throw new ConfigurationException( "Failed to create default configuration file", e );
            }
        }
    }

    public void save( Object config )
    {
        this.validateConfig( config.getClass() );

        Configuration configurationAnnotation = config.getClass().getAnnotation( Configuration.class );
        IConfiguration configuration = this.getOrLoadConfig( configurationAnnotation.fileType(), configurationAnnotation.filePath() );

        this.writeFieldsToConfiguration( configuration, config.getClass(), config, "" );

        try
        {
            configuration.save();
        }
        catch ( IOException e )
        {
            throw new ConfigurationException( "Failed to save configuration file", e );
        }
    }

    public IConfiguration getOrLoadConfig( FileStorageType fileStorageType, String filePath )
    {
        return configurations.computeIfAbsent( filePath, key -> IConfiguration.loadConfiguration(
            fileStorageType,
            new File( endoyApplication.getDataFolder(), filePath )
        ) );
    }

    private void writeFieldsToConfiguration( IConfiguration configuration, Class<?> clazz, Object instance, String prefix )
    {
        for ( Field declaredField : clazz.getDeclaredFields() )
        {
            if ( !declaredField.isAnnotationPresent( Value.class ) )
            {
                continue;
            }

            Value value = declaredField.getAnnotation( Value.class );
            String path = value.path().isEmpty() ? Utils.convertCamelCaseToDotNotation( declaredField.getName() ) : value.path();

            Object fieldValue;
            try
            {
                fieldValue = ReflectionUtils.getFieldValue( declaredField, instance );
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

            if ( declaredField.getType().isAnnotationPresent( ConfigurationSection.class ) )
            {
                ISection section = configuration.exists( path ) ? configuration.getSection( path ) : configuration.createSection( path );
                writeFieldsToConfiguration( configuration, declaredField.getType(), fieldValue, prefix + path + "." );
                configuration.set( path, section );
            }
            else
            {
                configuration.set( path, fieldValue );
            }
        }
    }

    private void validateConfig( Class<?> configClass )
    {
        if ( !configClass.isAnnotationPresent( Configuration.class ) )
        {
            throw new ConfigurationException( "Class " + configClass.getName() + " is not annotated with @Configuration" );
        }
    }
}
