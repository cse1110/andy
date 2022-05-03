package delft;

import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class ManyJQWikTests {
    @Property
    void t1() {}
    @Property void t2() {}
    @Property void t3() {}

    @Provide("a")
    void p1() {}
    @Provide void p2() {}
    @Provide void p3() {}
}
