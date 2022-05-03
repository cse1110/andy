package delft;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Property;

public class UseCombinator1 {
    @Property
    void t1() {
        Arbitrary<Integer> a = Arbitraries.integers();
        Arbitrary<Integer> b = Arbitraries.integers();

        Combinators.combine(a, b).as((x, y) -> new AB(x,y));
    }

    private class AB {
        public AB(Integer x, Integer y) {
        }
    }
}
