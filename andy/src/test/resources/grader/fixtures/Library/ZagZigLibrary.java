package delft;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class ZagZig {

    public String zagzig(String s, int numRows) {
        // some pre-condition check
        if(s.length() < 1 || s.length() > 1000)
            throw new IllegalArgumentException("1 <= s.length <= 1000");
        if(numRows < 1 || numRows > 1000)
            throw new IllegalArgumentException("1 <= numRows <= 1000");

        // early return: if the number of rows is 1, then, we return the same string
        if (numRows == 1) return s;

        // We create a list of strings, based on the number of rows we need
        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < numRows; i++)
            rows.add(new StringBuilder());

        int curRow = numRows - 1;
        boolean goingDown = true;

        // We visit character by character, and we put it in the list of strings.
        // We change directions whenever we reach the top or the bottom of the list.
        for (char c : s.toCharArray()) {
            rows.get(curRow).append(c);
            boolean topOrBottom = curRow == 0 || curRow == numRows - 1;

            // add spaces if we are 'zagging'
            if(goingDown && !topOrBottom) {
                for(int i = 0; i < rows.size(); i++) {
                    if(i!=curRow)
                        rows.get(i).append(" ");
                }
            }

            if (topOrBottom) goingDown = !goingDown;
            curRow += goingDown ? 1 : -1;
        }

        // we return the final string by simply combining all
        // the stringbuilders into a single string
        return rows
                .stream()
                .map(x->x.toString().trim())
                .collect(Collectors.joining("\n"))
                .trim();
    }

}




