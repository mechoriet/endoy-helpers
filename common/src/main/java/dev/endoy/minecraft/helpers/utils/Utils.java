package dev.endoy.minecraft.helpers.utils;

public class Utils
{

    private Utils()
    {
    }

    public static String convertCamelCaseToDashNotation( String camelCase )
    {
        return camelCase.replaceAll( "([a-z])([A-Z]+)", "$1-$2" ).toLowerCase();
    }

    public static String randomString( int length )
    {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < length; i++ )
        {
            int index = (int) ( Math.random() * characters.length() );
            stringBuilder.append( characters.charAt( index ) );
        }
        return stringBuilder.toString();
    }
}
