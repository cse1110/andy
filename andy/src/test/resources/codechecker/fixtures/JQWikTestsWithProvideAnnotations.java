package delft;

import net.jqwik.api.*;
import net.jqwik.api.constraints.Negative;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Triangle {

    public static boolean isTriangle(int a, int b, int c) {
        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
            return false;
        return true;
    }
}

public class JQWikTestsWithProvideAnnotations {



    // use the ABC class below.
    class ABC {

        int a;

        int b;

        int c;

        public ABC(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    private final Triangle triangle = new Triangle();

    //tests triangles where all sides are either 2 or 3, which should always be valid
    @Property
    void validTriangle(@ForAll("inValidRange") int a, @ForAll("inValidRange") int b, @ForAll("inValidRange") int c){
        assertEquals(triangle.isTriangle(a,b,c), true);
    }

    //When side a is negative, this should be an invalid triangle
    @Property
    void invalidNegativeSideA(@ForAll @Negative int a, @ForAll("inValidRange") int b, @ForAll("inValidRange") int c){
        assertEquals(triangle.isTriangle(a,b,c), false);
    }


    // when A is between 100-200 and B and C are both small, this should always be an invalid triangle
    @Property
    void invalidTriangleA(@ForAll("invalid") int a, @ForAll("invalid1") int b, @ForAll("invalid2") int c){
        assertEquals(triangle.isTriangle(a,b,c), false);
    }

    // when B is between 100-200 and B and C are both small, this should always be an invalid triangle
    @Property
    void invalidTriangleB(@ForAll("invalid1") int a, @ForAll("invalid2") int b, @ForAll("invalid") int c){
        assertEquals(triangle.isTriangle(a,b,c), false);
    }

    // when C is between 100-200 and B and C are both small, this should always be an invalid triangle
    @Property
    void invalidTriangleC(@ForAll("invalid2") int a, @ForAll("invalid") int b, @ForAll("invalid1") int c){
        assertEquals(triangle.isTriangle(a,b,c), false);
    }

    @Provide
    Arbitrary<Integer> invalid() {
        return Arbitraries.integers().greaterOrEqual(100).lessOrEqual(200);
    }

    @Provide
    Arbitrary<Integer> invalid1() {
        return  Arbitraries.integers().greaterOrEqual(10).lessOrEqual(20);
    }

    @Provide
    Arbitrary<Integer> invalid2() {
        return Arbitraries.integers().greaterOrEqual(1).lessOrEqual(10);
    }

    @Provide
    Arbitrary<Integer> inValidRange() {
        return Arbitraries.of(2, 3);
    }
}
