import java.util.List;

public class Day6Part1 {
    public static void main(String[] args) {

        long count = new GroupingParser()
            .stream("input-day6.txt")
            .mapToLong(Day6Part1::answers)
            .reduce(0, (a,b) -> a + b);

        System.out.println(count);
    }

    static long answers(List<String> group) {
        return String.join("", group).chars().distinct().count();
    }
}
