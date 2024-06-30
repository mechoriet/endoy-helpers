package dev.endoy.helpers.spring.subdomain;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.HashSet;

public class SubdomainRequestMappingHandlerMapping extends RequestMappingHandlerMapping
{

    @Override
    protected RequestCondition<?> getCustomTypeCondition( Class<?> handlerType )
    {
        SubdomainController subdomainController = AnnotationUtils.findAnnotation( handlerType, SubdomainController.class );

        if ( subdomainController != null )
        {
            return new SubdomainRequestCondition( new HashSet<>( Arrays.asList( subdomainController.value() ) ) );
        }

        return null;
    }
}
