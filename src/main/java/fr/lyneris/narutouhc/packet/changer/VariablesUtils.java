package fr.lyneris.narutouhc.packet.changer;

import java.lang.reflect.*;

public class VariablesUtils
{
    public static void updateVariablePrivate(final Object obj, final String variableName, final Object newValue) throws Exception {
        Field field;
        try {
            field = obj.getClass().getDeclaredField(variableName);
        }
        catch (NoSuchFieldException e) {
            field = obj.getClass().getSuperclass().getDeclaredField(variableName);
        }
        field.setAccessible(true);
        field.set(obj, newValue);
        field.setAccessible(false);
    }
    
    public static void updateVariablePrivateFinal(final Object obj, final String variableName, final Object newValue) throws Exception {
        final Field field = obj.getClass().getDeclaredField(variableName);
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        modifiersField.setAccessible(false);
        field.set(obj, newValue);
        field.setAccessible(false);
    }
}
