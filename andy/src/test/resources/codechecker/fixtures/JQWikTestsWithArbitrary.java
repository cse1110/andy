package delft;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;
import net.jqwik.api.arbitraries.IntegerArbitrary;

public class JQWikTestsWithArbitrary {
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

    @Provide
    private Arbitrary<ABC> invalidTriangles(){
        IntegerArbitrary A = Arbitraries.integers().greaterOrEqual(0);
        IntegerArbitrary B = Arbitraries.integers().greaterOrEqual(0);
        IntegerArbitrary C = Arbitraries.integers().greaterOrEqual(0);
        return Combinators.combine(A, B, C).as(ABC::new).filter(T -> ((T.a >= (T.b + T.c) || T.c >= (T.b + T.a) || T.b >= (T.a + T.c))));
    }

}
