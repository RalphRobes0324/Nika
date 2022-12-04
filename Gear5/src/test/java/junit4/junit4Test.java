package junit4;



import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class junit4Test {

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void isEmptyTest() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty(" "));
        assertFalse(isEmpty("nika"));
        assertFalse(isEmpty("  nika  "));
    }

    @Test
    public void isBlankTest() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank(" "));
        assertFalse(isBlank("nika"));
        assertFalse(isBlank("  nika  "));
    }

    public static boolean containsWhitespace(final CharSequence seq) {
        if (seq == null || seq.length() == 0) {
            return false;
        }
        final int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testContainsWhitespace() {
        assertFalse(containsWhitespace(null));
        assertFalse(containsWhitespace(""));
        assertFalse(containsWhitespace("n"));
        assertFalse(containsWhitespace("nika"));
        assertTrue(containsWhitespace(" "));
        assertTrue(containsWhitespace(" n"));
        assertTrue(containsWhitespace("nika "));
        assertTrue(containsWhitespace("n a"));
        assertTrue(containsWhitespace("n  a"));
    }


    public static String[] concatenateStringArrays(final String[] array1, final String[] array2) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }
        if (array2 == null || array2.length == 0) {
            return array1;
        }

        final String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }


    @Test
    public void testConcatenateStringArrays() {

        final String[] input1 = new String[]{"nikaString2"};
        final String[] input2 = new String[]{"nikaString1", "nikaString2"};
        final String[] result = concatenateStringArrays(input1, input2);

        assertNull(concatenateStringArrays(null, null));

        assertEquals(3, result.length);

        assertEquals("nikaString2", result[0]);
        assertEquals("nikaString1", result[1]);
        assertEquals("nikaString2", result[2]);

        assertArrayEquals(input1, concatenateStringArrays(input1, null));
        assertArrayEquals(input2, concatenateStringArrays(null, input2));
    }

    public int largestNumber(final int[] list) {
        int index, max = Integer.MAX_VALUE;
        for (index = 0; index < list.length - 1; index++) {
            if (list[index] > max) {
                max = list[index];
            }
        }
        return max;
    }
    @Test
    public void testFail() {


    }

}







