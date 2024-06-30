package dev.endoy.helpers.injector.withcirculardeps.deep;

import dev.endoy.helpers.EndoyApplicationTest;
import dev.endoy.helpers.injector.CircularDependencyException;
import dev.endoy.helpers.injector.Component;
import dev.endoy.helpers.injector.Injector;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InjectableWithDeepCircularDependencyTest extends EndoyApplicationTest
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

        TestComponentWithCircularDependency2 testComponent;

    }

    @Value
    @Component
    public static class TestComponentWithCircularDependency2
    {

        TestComponentWithCircularDependency3 testComponent;

    }

    @Value
    @Component
    public static class TestComponentWithCircularDependency3
    {

        TestComponentWithCircularDependency testComponent;

    }
}
