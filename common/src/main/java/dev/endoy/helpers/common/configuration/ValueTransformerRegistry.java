package dev.endoy.helpers.common.configuration;

import dev.endoy.helpers.common.transform.ValueTransformer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValueTransformerRegistry
{

    private static final Map<Class<? extends ValueTransformer>, ValueTransformer> VALUE_TRANSFORMERS = new ConcurrentHashMap<>();

    public static <T> ValueTransformer<T> getOrCreateValueTransformer( Class<? extends ValueTransformer> transformerClass )
    {
        return VALUE_TRANSFORMERS.computeIfAbsent( transformerClass, ( clazz ) ->
        {
            ValueTransformer transformer;

            try
            {
                transformer = clazz.getDeclaredConstructor().newInstance();
            }
            catch ( Exception e )
            {
                throw new ValueTransformerCreationException( e );
            }

            return transformer;
        } );
    }

}
