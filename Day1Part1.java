import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;

public class Day1Part1 {
    public static void main(String[] args) throws Exception {
        List<Integer> input = Files.lines(Paths.get("input-day1.txt")).map(Integer::parseInt).collect(Collectors.toList());
        input.forEach( x -> input.forEach( y ->  {
            if (x + y == 2020) {
                System.out.println(x * y); 
                System.exit(0);
            }
        }));
    }
}
