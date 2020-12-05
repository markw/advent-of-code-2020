import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Optional;

public class Day5Part1 {
    public static void main(String[] args) throws Exception {
        //Stream.of("BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL").map(Seat::from).forEach(System.out::println);
        Files.lines(Paths.get("input-day5.txt"))
            .map(Seat::from)
            .map(Seat::id)
            .max(Integer::compare)
            .ifPresent(System.out::println);
    }

    static class Seat {
        int row;
        int col;
        int id() { return row * 8 + col; }

        private static int pow2(int exponent) { return exponent == 0 ? 1 : 2 * pow2(--exponent); }

        private static int parse(String s, int min, int max, int exponent, char upper) {
            if (s.isEmpty()) return min;
            int delta = pow2(exponent--);
            return (s.charAt(0) == upper)
                ? parse(s.substring(1), min + delta, max,         exponent, upper)
                : parse(s.substring(1), min,         max - delta, exponent, upper);
        }

        private static int parseCol(String s) {
            return parse(s, 0, 7, 2, 'R');
        }

        private static int parseRow(String s) {
            return parse(s, 0, 127, 6, 'B');
        }

        Seat(int row, int col) { 
            this.row = row; 
            this.col = col; 
        }

        static Seat from(String s) {
            int row = parseRow(s.substring(0,7));
            int col = parseCol(s.substring(7));
            return new Seat(row, col);
        }

        public String toString() {
            return String.format("row %d, column %d, seat ID %d", row, col, id());
        }
    }
}
