package ptg.rest.common.converter;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;

public class EnumSetToLongConverter {

    public static Long convertToDatabaseColumn(Set<? extends Enum> attribute) {
        if (attribute == null) {
            return null;
        }

        BitSet result = new BitSet(64);
        attribute.forEach(e -> result.set(e.ordinal()));
        if (result.isEmpty()) {
            return 0L;
        }
        return result.toLongArray()[0];
    }

    public static <E extends Enum<E>> Set<E> convertToEntityAttribute(Long dbData, Class<E> clazz) {
        if (dbData == null) {
            return null;
        }

        BitSet bitSet = BitSet.valueOf(new long[]{dbData});
        Set<E> result = EnumSet.allOf(clazz);
        result.removeIf(flag -> !bitSet.get(flag.ordinal()));
        return result;
    }
}
