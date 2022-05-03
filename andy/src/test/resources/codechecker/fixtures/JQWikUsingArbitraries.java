package delft;


import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;


class LeapYear {

    public static boolean isLeapYear(int year) {
        if (year % 400 == 0)
            return true;
        if (year % 100 == 0)
            return false;
        return year % 4 == 0;
    }
}

public class JQWikUsingArbitraries {

    private final LeapYear leapyear = new LeapYear();

    @Property
    void divisibleBy400(@ForAll("div400") int year){
        assertThat(leapyear.isLeapYear(year)).isTrue();
    }

    @Property
    void divisibleBy4notBy100(@ForAll("div4not100") int year){
        assertThat(leapyear.isLeapYear(year)).isTrue();
    }


    @Property
    void divisibleBy100(@ForAll("div100") int year){
        assertThat(leapyear.isLeapYear(year)).isFalse();
    }

    @Property
    void nonDivisbleBy4(@ForAll("notdiv4") int year){
        assertThat(leapyear.isLeapYear(year)).isFalse();
    }

    @Provide
    Arbitrary<Double> div400() {
        return Arbitraries.doubles()
                .filter(i -> i % 400 == 0);
    }

    @Provide
    Arbitrary<Integer> div4not100() {
        return Arbitraries.integers()
                .filter(i -> i % 4 == 0)
                .filter(i -> i % 100 != 0);
    }

    @Provide
    Arbitrary<Integer> div100() {
        return Arbitraries.integers()
                .filter(i -> i % 100 == 0)
                .filter(i -> i % 400 != 0);
    }

    @Provide
    Arbitrary<Integer> notdiv4() {
        return Arbitraries.integers()
                .filter(i -> i % 4 != 0);
    }



}
