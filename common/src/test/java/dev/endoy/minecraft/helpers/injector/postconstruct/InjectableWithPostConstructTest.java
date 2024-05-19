package dev.endoy.minecraft.helpers.injector.postconstruct;

import dev.endoy.minecraft.helpers.EndoyApplicationTest;
import dev.endoy.minecraft.helpers.injector.Component;
import dev.endoy.minecraft.helpers.injector.Injector;
import dev.endoy.minecraft.helpers.injector.PostConstruct;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InjectableWithPostConstructTest extends EndoyApplicationTest
{

    private static boolean postConstruct1Called = false;
    private static boolean postConstruct2Called = false;

    @Test
    @DisplayName( "Test PostConstruct to be functional" )
    void testInject()
    {
        Injector injector = Injector.forProject( this.getClass() );
        injector.inject();

        assertTrue( postConstruct1Called );
        assertTrue( postConstruct2Called );
    }

    @Value
    @Component
    public static class TestComponent
    {

        TestComponent2 testComponent2;

        @PostConstruct
        void postConstruct()
        {
            postConstruct1Called = true;
        }
    }

    @Component
    public static class TestComponent2
    {

        @PostConstruct
        void postConstruct()
        {
            postConstruct2Called = true;
        }
    }
}