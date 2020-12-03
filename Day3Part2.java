import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day3Part2 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.lines(Paths.get("input-day3.txt")).collect(Collectors.toList());
        long treeCount = LongStream.of(
            countTrees(lines, 1, 1),
            countTrees(lines, 3, 1),
            countTrees(lines, 5, 1),
            countTrees(lines, 7, 1),
            countTrees(lines, 1, 2))
            .reduce(1, (a, b) -> a * b);

        System.out.println(treeCount);
        // 2983070376
    }

    static int countTrees(List<String> lines, int right, int down) {
        int numLines = lines.size();
        int row = 0, col = 0, treeCount = 0;
        while (true) {
            row += down;
            if (row >= numLines) return treeCount;
            String line = lines.get(row);
            col = (col + right) % line.length();
            if (line.charAt(col) == '#') treeCount++;
        }
    }
}

