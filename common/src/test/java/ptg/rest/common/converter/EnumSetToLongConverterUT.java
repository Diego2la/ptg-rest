package ptg.rest.common.converter;

import org.junit.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class EnumSetToLongConverterUT {

    @Test
    public void testLongToEnumSetConverter() {

        Set<TestEnum> result = EnumSetToLongConverter.convertToEntityAttribute(2L, TestEnum.class);

        assertEquals(result, EnumSet.of(TestEnum.SECOND));
    }

    @Test
    public void testEnumSetToConverter() {
        long result = EnumSetToLongConverter.convertToDatabaseColumn(EnumSet.of(TestEnum.FIRST));

        assertEquals(result, 1L);
    }

    enum TestEnum {
        FIRST, SECOND;
    }
}
