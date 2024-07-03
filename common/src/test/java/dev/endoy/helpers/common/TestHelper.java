package dev.endoy.helpers.common;

import dev.endoy.helpers.common.utils.ReflectionUtils;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.any;

public class TestHelper
{

    public static void callRealMethods( MockedStatic<ReflectionUtils> reflectionUtils )
    {
        reflectionUtils.when( () -> ReflectionUtils.getClassesInPackageAnnotatedWith( any(), any() ) )
            .thenCallRealMethod();
        reflectionUtils.when( () -> ReflectionUtils.getClassesInPackageImplementing( any(), any() ) )
            .thenCallRealMethod();
        reflectionUtils.when( () -> ReflectionUtils.setFieldValue( any(), any(), any() ) )
            .thenCallRealMethod();
        reflectionUtils.when( () -> ReflectionUtils.getFieldValue( any(), any() ) )
            .thenCallRealMethod();
        reflectionUtils.when( () -> ReflectionUtils.invokeMethod( any(), any() ) )
            .thenCallRealMethod();
    }

    public static File createTempDirectory() throws IOException
    {
        return Files.createTempDirectory( "endoy-helpers" ).toFile();
    }
}
