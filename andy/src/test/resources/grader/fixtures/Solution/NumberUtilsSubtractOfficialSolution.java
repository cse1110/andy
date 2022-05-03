package delft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NumberUtilsTests {

	@ParameterizedTest
	@MethodSource("testCases")
	void shouldReturnCorrectResult(List<Integer> left, List<Integer> right, List<Integer> expected) {
		assertThat(subtract(left, right)).isEqualTo(expected);
	}

	static Stream<Arguments> testCases() {
		// inspired by https://leetcode.com/problems/add-two-numbers/

		/*
		 * left:
		 * - empty
		 * - null
		 * - single digit
		 * - multiple digits
		 * - zeroes on the left
		 *
		 * right:
		 * - empty
		 * - null
		 * - single digit
		 * - multiple digits
		 * - zeroes on the left
		 *
		 * (left, right):
		 * - len(left) > len(right)
		 * - len(right) > len(left)
		 * - len(left) = len(right)
		 * - same number
		 *
		 * carry:
		 * - subtraction without carry
		 * - subtraction with carry
		 *   - one carry at the beginning
		 *   - one carry in the middle
		 *   - many carries
		 *   - many carries, not in a row
		 *   - carry in the last digit
		 *
		 * Test cases:
		 *
		 * T1 = left null
		 * T2 = left empty
		 * T3 = right null
		 * T4 = right empty
		 *
		 * single digit:
		 * T5 = single digit, no carry
		 * T6 = single digit, carry
		 *
		 * multiple digits:
		 * T7 = no carry
		 * T8 = carry in the least significant digit
		 * T9 = carry in the middle
		 * T10 = many carries
		 * T11 = many carries, not in a row
		 * T12 = carry in the most significant digit
		 *
		 * multiple digits, different length:
		 * T13 = no carry
		 * T14 = carry in the middle
		 * T15 = many carries
		 * T16 = many carries, not in a row
		 * T17 = carry in the most significant digit
		 *
		 * zeroes on the left:
		 * T18 = no carry
		 * T19 = carry
		 * (do not see reason to combine with all the carries again)
		 *
		 * same number:
		 * T20 = single digit
		 * T21 = multiple digits
		 *
		 * equal digits at the same index:
		 * T22 = with carry
		 */
		return Stream.of(
				// nulls and empties should return null
				Arguments.of(null, numbers(7,2), null), // T1
				Arguments.of(numbers(), numbers(), numbers(0)), // T2
				Arguments.of(numbers(9,8), null, null), // T3
				Arguments.of(numbers(9,8), numbers(), numbers(9,8)), // T4

				// single digits
				Arguments.of(numbers(3), numbers(1), numbers(2)), // T5
				Arguments.of(numbers(1,1), numbers(9), numbers(2)), // T6

				// multiple digits
				Arguments.of(numbers(4,2), numbers(1,1), numbers(3,1)), // T7
				Arguments.of(numbers(5,5), numbers(2,9), numbers(2,6)), // T8
				Arguments.of(numbers(7,2,5), numbers(6,9,3), numbers(3,2)), // T9
				Arguments.of(numbers(2,1,1,1), numbers(1,9,9,9), numbers(1,1,2)), // T10
				Arguments.of(numbers(8,1,8,1), numbers(2,9,2,9), numbers(5,2,5,2)), // T11
				Arguments.of(numbers(2,1), numbers(1,9), numbers(2)), // T12

				// multiple digits, different length, with and without carry
				Arguments.of(numbers(2,2), numbers(1), numbers(2,1)), // T13
				Arguments.of(numbers(1,3,7,3), numbers(1,9,2), numbers(1,1,8,1)), // T14
				Arguments.of(numbers(4,2,3,7,3), numbers(8,4,9,2), numbers(3,3,8,8,1)), // T15
				Arguments.of(numbers(4,2,3,7,3), numbers(8,1,9,2), numbers(3,4,1,8,1)), // T16
				Arguments.of(numbers(3,0,1), numbers(2,9,1), numbers(1,0)), // T17

				// zeroes on the left
				Arguments.of(numbers(0,0,0,2,3), numbers(0,1,2), numbers(1,1)), // T18
				Arguments.of(numbers(0,0,0,2,2), numbers(0,1,9), numbers(3)), // T19

				// same number
				Arguments.of(numbers(1), numbers(1), numbers(0)), // T20
				Arguments.of(numbers(1,1), numbers(1,1), numbers(0)), // T21

				// equal digits at the same index
				Arguments.of(numbers(2,1,0), numbers(1,1,1), numbers(9,9)) // T22
		);
	}

	@ParameterizedTest
	@MethodSource("digitsOutOfRange")
	void shouldThrowExceptionWhenDigitsAreOutOfRange(List<Integer> left, List<Integer> right) {
		assertThatThrownBy(() -> subtract(left, right)).isInstanceOf(IllegalArgumentException.class);
	}

	static Stream<Arguments> digitsOutOfRange() {
		return Stream.of(Arguments.of(numbers(2, -1, 1), numbers(1, 1, 1)),
				Arguments.of(numbers(1, 1, 1), numbers(1, -1, 1)), Arguments.of(numbers(1, 10, 1), numbers(1, 1, 1)),
				Arguments.of(numbers(2, 1, 1), numbers(1, 11, 1)));
	}

	@ParameterizedTest
	@MethodSource("leftSmallerThanRight")
	void shouldThrowExceptionWhenLeftNumberIsSmallerThanRightNumber(List<Integer> left, List<Integer> right) {
		assertThatThrownBy(() -> subtract(left, right)).isInstanceOf(IllegalArgumentException.class);
	}

	static Stream<Arguments> leftSmallerThanRight() {
		return Stream.of(Arguments.of(numbers(1), numbers(2)), Arguments.of(numbers(1, 0), numbers(1, 1)),
				Arguments.of(numbers(), numbers(1)));
	}

	// returns a mutable list of integers
	private static List<Integer> numbers(int... nums) {
		List<Integer> list = new ArrayList<>();
		for (int n : nums)
			list.add(n);
		return list;
	}

	private static List<Integer> subtract(List<Integer> left, List<Integer> right) {
		return NumberUtils.subtract(left, right);
	}
}
