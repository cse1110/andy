package delft;
//Random student solution

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ZagZigTest {

    String result;
    ZagZig zag;
    @BeforeEach
    void setup(){
        zag = new ZagZig();
    }

    //length = 0
    @Test
    void lengthIs0(){

        assertThatThrownBy( () ->
        {
            zag.zagzig("", 3);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    //length > 1000
    @Test
    void lengthIs1005(){
        result = new String(new char[1005]);
        assertThatThrownBy( () ->
        {
            zag.zagzig(result, 3);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    //numrows = 0
    @Test
    void numRowsIs0(){
        assertThatThrownBy( () ->
        {
            zag.zagzig("sadasqqwe", 0);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    //numrows > 1000
    @Test
    void numRowsIs1001(){

        System.out.println("RandomString1");
        assertThatThrownBy( () ->
        {
            zag.zagzig("sadasqqwe", 1001);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    //numrows = 1
    @Test
    void numRowsIs1(){
        result = "TESTTHIS";
        assertEquals(zag.zagzig("TESTTHIS", 1), result);
    }

    //random basic case
    @Test
    void regularTest(){
        result = "P  I\nYA HR\nA LS IG\nP  I  N";
        assertEquals(zag.zagzig("PAYPALISHIRING", 4), result);
    }

    //no of rows equal to length
    //random
    @Test
    void rowsEqualToLength(){

        System.out.println("HelloWorld2");

        result = "D\nC\nB\nA";
        assertEquals(zag.zagzig("ABCD", 4), result);
    }

    //no of rows bigger than length
    @Test
    void rowsBiggerThanLength(){
        result = "C\nB\nA";
        assertEquals(zag.zagzig("ABC", 4), result);
    }

    //no of rows smaller than length
    @Test
    void rowsSmallerThanLength(){
        result = "D\nCE\nB\nA";
        assertEquals(zag.zagzig("ABCDE", 4), result);
    }

    //rows is only 2
    @Test
    void rows2(){
        result = "BDFH\nACEG";
        assertEquals(zag.zagzig("ABCDEFGH", 2), result);
    }

    //only 2 zigs
    @Test
    void zigs2(){

        System.out.println("I love SQT3");

        result = "P\nYA\nA L\nP  I\nP   I";
        assertEquals(zag.zagzig("PPAYPALII", 5), result);
    }
}

