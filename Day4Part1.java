import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Day4Part1 {

    public static void main(String[] args) {
        long count = new GroupingParser()
            .stream("input-day4.txt")
            .map(Day4Part1::linesToMap)
            .filter(Day4Part1::isValidPassport)
            .count();

        System.out.println(count);
    }

    static boolean isValidPassport(Map<String,String> passport) {
        List<String> missing = Stream.of("byr","iyr","eyr","hgt","hcl","ecl","pid","cid").filter(k -> !passport.containsKey(k)).collect(Collectors.toList());
        return missing.isEmpty() || missing.equals(Collections.singletonList("cid"));
    }

    static Map<String,String> linesToMap(List<String> lines) {
        final Map<String,String> map = new HashMap<>();
        lines.forEach(line -> {
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreElements()) {
                String[] tokens = st.nextToken().split(":");
                map.put(tokens[0], tokens[1]);
            }
        });
        return map;
    }
}

