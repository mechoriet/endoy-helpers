package dev.endoy.helpers.common.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface TransformValue
{

    Class<? extends ValueTransformer> value() default NoOpValueTransformer.class;

}
