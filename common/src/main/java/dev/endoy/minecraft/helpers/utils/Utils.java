package dev.endoy.minecraft.helpers.utils;

public class Utils
{

    private Utils()
    {
    }

    public static String convertCamelCaseToDotNotation( String camelCase )
    {
        return camelCase.replaceAll( "([a-z])([A-Z]+)", "$1.$2" ).toLowerCase();
    }
}
