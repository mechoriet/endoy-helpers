package dev.endoy.minecraft.helpers.injector.withcirculardeps.fields;

import dev.endoy.minecraft.helpers.EndoyApplicationTest;
import dev.endoy.minecraft.helpers.injector.Component;
import dev.endoy.minecraft.helpers.injector.Inject;
import dev.endoy.minecraft.helpers.injector.Injector;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectableWithFieldCircularDependencyTest extends EndoyApplicationTest
{

    @Test
    @DisplayName( "Test circular dependency detection with self-reference from it's own constructor" )
    void testInject()
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        injector.inject();

        TestComponent testComponent = injector.getInjectableInstance( TestComponent.class );
        TestComponent2 testComponent2 = injector.getInjectableInstance( TestComponent2.class );
        TestComponent3 testComponent3 = injector.getInjectableInstance( TestComponent3.class );

        assertEquals( testComponent.getTestComponent2(), testComponent2 );
        assertEquals( testComponent2.getTestComponent3(), testComponent3 );
        assertEquals( testComponent.getTestComponent2().getTestComponent3().getTestComponent(), testComponent );
    }

    @Getter
    @Component
    public static class TestComponent
    {

        @Inject
        TestComponent2 testComponent2;

    }

    @Getter
    @Component
    public static class TestComponent2
    {

        @Inject
        TestComponent3 testComponent3;

    }

    @Getter
    @Component
    public static class TestComponent3
    {

        @Inject
        TestComponent testComponent;

    }
}
