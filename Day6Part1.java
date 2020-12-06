import java.util.List;

public class Day6Part1 {
    public static void main(String[] args) throws Exception {
        List<List<String>> groups = new GroupingParser().parse("input-day6.txt");
        System.out.println(groups.stream().mapToLong(Day6Part1::answers).reduce(0, (a,b) -> a + b));
    }

    static long answers(List<String> group) {
        return String.join("", group).chars().distinct().count();
    }
}
