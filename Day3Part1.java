import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day3Part1 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.lines(Paths.get("input-day3.txt")).collect(Collectors.toList());
        System.out.println(countTrees(Arrays.asList(
                        ".....",
                        "...#.",
                        ".#...",
                        "....#"
                        ), 3, 1));
        System.out.println(countTrees(lines, 3, 1));
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

