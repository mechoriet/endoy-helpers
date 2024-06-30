package dev.endoy.helpers.common.injector.withcirculardeps.self;

import dev.endoy.helpers.common.EndoyApplicationTest;
import dev.endoy.helpers.common.injector.CircularDependencyException;
import dev.endoy.helpers.common.injector.Component;
import dev.endoy.helpers.common.injector.Injector;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InjectableWithSelfCircularDependencyTest extends EndoyApplicationTest
{

    @Test
    @DisplayName( "Test circular dependency detection with self-reference from it's own constructor" )
    void testInject()
    {
        assertThrows( CircularDependencyException.class, () ->
        {
            Injector injector = Injector.forProject( this.getClass(), this );
            injector.inject();
        } );
    }

    @Value
    @Component
    public static class TestComponentWithCircularDependency
    {

        TestComponentWithCircularDependency testComponent;

    }
}