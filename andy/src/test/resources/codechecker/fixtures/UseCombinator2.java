package delft;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Property;

import static net.jqwik.api.Combinators.combine;


public class UseCombinator2 {
    @Property
    void t1() {
        Arbitrary<Integer> a = Arbitraries.integers();
        Arbitrary<Integer> b = Arbitraries.integers();

        combine(a, b).as((x, y) -> new AB(x,y));
    }

    private class AB {
        public AB(Integer x, Integer y) {
        }
    }
}
