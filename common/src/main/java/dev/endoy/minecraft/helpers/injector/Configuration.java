package dev.endoy.minecraft.helpers.injector;

import be.dieterblancke.configuration.api.FileStorageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface Configuration
{

    String filePath();

    FileStorageType fileType() default FileStorageType.YAML;

}
