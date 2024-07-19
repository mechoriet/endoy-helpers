package dev.endoy.helpers.common.utils;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import dev.endoy.helpers.common.logger.EndoyLogger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReflectionUtils
{

    private static final Logger ENDOY_LOGGER = EndoyLogger.forClass( ReflectionUtils.class );

    private ReflectionUtils()
    {
    }

    public static void invokeMethod( Method method, Object instance ) throws ReflectiveOperationException
    {
        method.setAccessible( true );
        method.invoke( instance );
        method.setAccessible( false );
    }

    public static Object getFieldValue( Field field, Object instance ) throws IllegalAccessException
    {
        field.setAccessible( true );
        Object value = field.get( instance );
        field.setAccessible( false );
        return value;
    }

    public static void setFieldValue( Field field, Object instance, Object value ) throws IllegalAccessException
    {
        field.setAccessible( true );
        field.set( instance, value );
        field.setAccessible( false );
    }

    public static List<Class<?>> getClassesInPackageAnnotatedWith( Class<?> starterClass, Class<? extends Annotation> annotation )
    {
        return getClassesInPackage( starterClass )
            .stream()
            .filter( clazz -> clazz.isAnnotationPresent( annotation ) )
            .collect( Collectors.toList() );
    }

    public static Collection<Class<?>> getClassesInPackageImplementing( Class<?> baseClass, Class<?> interfaceClass )
    {
        return getClassesInPackage( baseClass )
            .stream()
            .filter( interfaceClass::isAssignableFrom )
            .filter( clazz -> !clazz.equals( interfaceClass ) )
            .collect( Collectors.toList() );
    }

    public static List<Class<?>> getClassesInPackage( Class<?> starterClass )
    {
        List<Class<?>> classes;

        try
        {
            classes = ClassPath.from( starterClass.getClassLoader() )
                .getTopLevelClassesRecursive( starterClass.getPackageName() )
                .stream()
                .map( ClassPath.ClassInfo::load )
                .flatMap( clazz -> getClassAndInnerClasses( clazz ).stream() )
                .collect( Collectors.toList() );
        }
        catch ( IOException e )
        {
            org.slf4j.Logger logger = LoggerFactory.getLogger( ReflectionUtils.class );
            logger.error( "Failed to get classes in package: " + starterClass.getPackageName(), e );
            ENDOY_LOGGER.log( Level.SEVERE, "Failed to get classes in package: " + starterClass.getPackageName(), e );
            classes = new ArrayList<>();
        }

        if ( classes.isEmpty() )
        {
            CodeSource src = starterClass.getProtectionDomain().getCodeSource();

            if ( src != null )
            {
                try
                {
                    URL jar = src.getLocation();
                    ZipInputStream zip = new ZipInputStream( jar.openStream() );

                    while ( true )
                    {
                        ZipEntry e = zip.getNextEntry();
                        if ( e == null )
                        {
                            break;
                        }
                        String name = e.getName().replace( "/", "." );

                        if ( name.startsWith( starterClass.getPackageName() ) )
                        {
                            if ( name.endsWith( ".class" ) )
                            {
                                classes.addAll( getClassAndInnerClasses(
                                    Class.forName( name.replace( ".class", "" ) )
                                ) );
                            }
                        }
                    }
                }
                catch ( IOException | ClassNotFoundException e )
                {
                    ENDOY_LOGGER.log( Level.SEVERE, "Failed to get classes in package: " + starterClass.getPackageName(), e );
                }
            }
        }

        return classes
            .stream()
            .sorted( Comparator.comparing( Class::getSimpleName ) )
            .toList();
    }

    private static Collection<Class<?>> getClassAndInnerClasses( Class<?> currentClass )
    {
        List<Class<?>> classes = Lists.newArrayList( currentClass );

        for ( Class<?> declaredClass : currentClass.getDeclaredClasses() )
        {
            classes.addAll( getClassAndInnerClasses( declaredClass ) );
        }

        return classes;
    }

    public static Object createInstance( Class<?> clazz )
    {
        try
        {
            return clazz.getDeclaredConstructor().newInstance();
        }
        catch ( ReflectiveOperationException e )
        {
            ENDOY_LOGGER.log( Level.SEVERE, "Failed to create instance of class: " + clazz.getName(), e );
            return null;
        }
    }
}
