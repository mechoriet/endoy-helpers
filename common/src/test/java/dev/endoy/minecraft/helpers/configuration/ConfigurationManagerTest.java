package dev.endoy.minecraft.helpers.configuration;

import com.google.common.io.Files;
import dev.endoy.minecraft.helpers.EndoyApplicationTest;
import dev.endoy.minecraft.helpers.injector.*;
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
}