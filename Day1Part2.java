import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

public class Day1Part2 {
    public static void main(String[] args) throws Exception {
        List<Integer> input = Files.lines(Paths.get("input-day1.txt")).map(Integer::parseInt).collect(Collectors.toList());
        input.forEach(x -> input.forEach(y -> input.forEach(z -> {
            if (x + y + z == 2020) {
                System.out.println(x * y * z); 
                System.exit(0);
            }
        })));

        /*
         * another way
         */
        /*
        input.stream().flatMap(x -> 
                input.stream().flatMap(y -> 
                    input.stream().map(z -> Arrays.asList(x + y + z, x * y * z))))
        .filter(x -> x.get(0) == 2020)
        .findFirst()
        .ifPresent(x -> System.out.println(x.get(1)));
        */
    }
}
