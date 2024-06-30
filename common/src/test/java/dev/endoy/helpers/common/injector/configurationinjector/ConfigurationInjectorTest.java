package dev.endoy.helpers.common.injector.configurationinjector;

import dev.endoy.helpers.common.EndoyApplicationTest;
import dev.endoy.helpers.common.injector.*;
import dev.endoy.helpers.common.transform.TransformValue;
import dev.endoy.helpers.common.transform.ValueTransformer;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationInjectorTest extends EndoyApplicationTest
{

    @BeforeAll
    public static void setup() throws IOException
    {
        dataFolder = Files.createTempDirectory( "endoy-helpers" ).toFile();

        Files.writeString(
            new File( dataFolder, "config.yml" ).toPath(),
            """
                test: test
                test-section:
                  test: test
                  test-nr: 123
                """
        );

        Files.writeString(
            new File( dataFolder, "config-with-comments.yml" ).toPath(),
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
        Files.writeString(
            new File( dataFolder, "config-with-enum.yml" ).toPath(),
            """
                test: TEST
                """
        );
        Files.writeString(
            new File( dataFolder, "config-with-transform.yml" ).toPath(),
            """
                test: testing
                """
        );
    }

    @Test
    void testCreateDefault()
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.inject();

        TestConfiguration testConfiguration = injector.getInjectableInstance( TestConfiguration.class );
        assertEquals( "test", testConfiguration.getTest() );
        assertEquals( "test", testConfiguration.getTestSection().getTest() );
        assertEquals( 123, testConfiguration.getTestSection().getTestNr() );
    }

    @Test
    void testCreateDefaultWithComments()
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.inject();

        TestConfigurationWithComments testConfiguration = injector.getInjectableInstance( TestConfigurationWithComments.class );
        assertEquals( "test", testConfiguration.getTest() );
        assertEquals( "test", testConfiguration.getTestSection().getTest() );
        assertEquals( 123, testConfiguration.getTestSection().getTestNr() );
    }

    @Test
    void testWithEnum()
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.inject();

        TestConfigurationWithEnum testConfiguration = injector.getInjectableInstance( TestConfigurationWithEnum.class );
        assertEquals( TestEnum.TEST, testConfiguration.getTest() );
    }

    @Test
    void testWithTransform()
    {
        Injector injector = Injector.forProject( this.getClass(), this );
        this.setInjector( injector );
        injector.inject();

        TestConfigurationWithTransform testConfiguration = injector.getInjectableInstance( TestConfigurationWithTransform.class );
        assertEquals( "testing", testConfiguration.getTest().getValue() );
    }

    public enum TestEnum
    {
        TEST, TESTING
    }

    @Getter
    @Configuration( filePath = "config.yml" )
    public static class TestConfiguration
    {

        @Value
        private String test = "test";

        @Value( path = "test-section" )
        private TestConfigurationSection testSection = new TestConfigurationSection();

        @Getter
        @ConfigurationSection
        public static class TestConfigurationSection
        {

            @Value
            private String test = "test";

            @Value( path = "test-nr" )
            private int testNr = 123;

        }
    }

    @Getter
    @Configuration( filePath = "config-with-comments.yml" )
    public static class TestConfigurationWithComments
    {

        @Value
        @Comment( "This is a test" )
        private String test = "test";

        @Value( path = "test-section" )
        @Comment( { "This is a test section", "With a second line", "A third line", "And even a fourth line :O" } )
        private TestConfigurationSectionWithComments testSection = new TestConfigurationSectionWithComments();

        @Getter
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

    @Getter
    @Configuration( filePath = "config-with-enum.yml" )
    public static class TestConfigurationWithEnum
    {

        @Value
        private TestEnum test = TestEnum.TEST;

    }

    @Getter
    @Configuration( filePath = "config-with-transform.yml" )
    public static class TestConfigurationWithTransform
    {

        @Value
        @TransformValue( value = TestTransform.class )
        private TransformedTest test = new TransformedTest( "testing" );

    }

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