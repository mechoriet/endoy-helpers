package dev.endoy.helpers.common.configuration;

import com.google.common.io.Files;
import dev.endoy.helpers.common.EndoyApplicationTest;
import dev.endoy.helpers.common.injector.*;
import dev.endoy.helpers.common.transform.TransformValue;
import dev.endoy.helpers.common.transform.ValueTransformer;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationManagerTest extends EndoyApplicationTest
{

    @Test
    void testCreateDefault() throws IOException
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.registerInjectable( TestConfiguration.class, new TestConfiguration() );

        this.getConfigurationManager().createDefault( TestConfiguration.class );

        assertEquals(
            String.join( "\n", Files.readLines( new File( getDataFolder(), "config.yml" ), StandardCharsets.UTF_8 ) ),
            """
                test: test
                test-section:
                  test: test
                  test-nr: 123"""
        );
    }

    @Test
    void testCreateDefaultWithComments() throws IOException
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.registerInjectable( TestConfigurationWithComments.class, new TestConfigurationWithComments() );

        this.getConfigurationManager().createDefault( TestConfigurationWithComments.class );

        assertEquals(
            String.join( "\n", Files.readLines( new File( getDataFolder(), "config-with-comments.yml" ), StandardCharsets.UTF_8 ) ),
            """
                # This is a test
                test: test
                # This is a test section
                # With a second line
                # A third line
                # And even a fourth line :O
                test-section:
                  # This is a test section with test comment
                  test: test
                  # This is a test section with test number comment
                  # Even with multi line comments!!!1!!
                  test-nr: 123"""
        );
    }

    @Test
    void testWithEnum() throws IOException
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.registerInjectable( TestConfigurationWithEnum.class, new TestConfigurationWithEnum() );

        this.getConfigurationManager().createDefault( TestConfigurationWithEnum.class );

        assertEquals(
            String.join( "\n", Files.readLines( new File( getDataFolder(), "config-with-enum.yml" ), StandardCharsets.UTF_8 ) ),
            """
                test: TEST"""
        );
    }

    @Test
    void testWithTransform() throws IOException
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.registerInjectable( TestConfigurationWithTransform.class, new TestConfigurationWithTransform() );

        this.getConfigurationManager().createDefault( TestConfigurationWithTransform.class );

        assertEquals(
            String.join( "\n", Files.readLines( new File( getDataFolder(), "config-with-transform.yml" ), StandardCharsets.UTF_8 ) ),
            """
                test: testing"""
        );
    }

    @Configuration( filePath = "config.yml" )
    public static class TestConfiguration
    {

        @Value
        private String test = "test";

        @Value( path = "test-section" )
        private TestConfigurationSection testSection = new TestConfigurationSection();

        @ConfigurationSection
        public static class TestConfigurationSection
        {

            @Value
            private String test = "test";

            @Value( path = "test-nr" )
            private int testNr = 123;

        }
    }

    @Configuration( filePath = "config-with-comments.yml" )
    public static class TestConfigurationWithComments
    {

        @Value
        @Comment( "This is a test" )
        private String test = "test";

        @Value( path = "test-section" )
        @Comment( { "This is a test section", "With a second line", "A third line", "And even a fourth line :O" } )
        private TestConfigurationSectionWithComments testSection = new TestConfigurationSectionWithComments();

        @ConfigurationSection
        public static class TestConfigurationSectionWithComments
        {

            @Value
            @Comment( "This is a test section with test comment" )
            private String test = "test";

            @Value( path = "test-nr" )
            @Comment( { "This is a test section with test number comment", "Even with multi line comments!!!1!!" } )
            private int testNr = 123;

        }
    }

    @Configuration( filePath = "config-with-enum.yml" )
    public static class TestConfigurationWithEnum
    {

        @Value
        private TestEnum test = TestEnum.TEST;

        public enum TestEnum
        {
            TEST, TESTING
        }
    }

    @Configuration( filePath = "config-with-transform.yml" )
    public static class TestConfigurationWithTransform
    {

        @Value
        @TransformValue( value = TestTransform.class )
        private TransformedTest test = new TransformedTest( "testing" );


        public static class TestTransform implements ValueTransformer<TransformedTest>
        {

            @Override
            public TransformedTest transformFromConfigValue( Object value )
            {
                return new TransformedTest( (String) value );
            }

            @Override
            public Object transformToConfigValue( TransformedTest value )
            {
                return value.getValue();
            }
        }

        public static class TransformedTest
        {
            @Getter
            private final String value;

            public TransformedTest( String value )
            {
                this.value = value;
            }

            @Override
            public String toString()
            {
                return this.value;
            }
        }
    }
}