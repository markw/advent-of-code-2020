import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Day6Part2 {
    public static void main(String[] args) throws Exception {

        long answeredByAll = new GroupingParser()
            .parse("input-day6.txt")
            .stream()
            .mapToLong(Day6Part2::answeredByAll)
            .reduce(0, (a,b) -> a + b);

        System.out.println(answeredByAll);
    }

    static Set<Integer> toSet(String s) {
        return s.chars().boxed().collect(Collectors.toSet());
    }

    static long answeredByAll(List<String> group) {
        Set<Integer> answers = toSet(group.get(0));
        group.forEach(g -> answers.retainAll(toSet(g)));
        return answers.size();
    }
}
