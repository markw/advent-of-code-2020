import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Day4Part1 {

    static final List<Map<String,String>> passports = new ArrayList<>();
    static Map<String,String> current = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Files.lines(Paths.get("input-day4.txt")).forEach(Day4Part1::handleLine);
        if (!current.isEmpty()) passports.add(current);

        //System.out.println(passports.size());
        System.out.println(passports.stream().filter(Day4Part1::isValidPassport).count());
    }

    static boolean isValidPassport(Map<String,String> passport) {
        List<String> missing = Stream.of("byr","iyr","eyr","hgt","hcl","ecl","pid","cid").filter(k -> !passport.containsKey(k)).collect(Collectors.toList());
        return missing.isEmpty() || missing.equals(Collections.singletonList("cid"));
    }

    static void handleLine(String s) {
        if (s.trim().isEmpty()) {
            if (!current.isEmpty()) passports.add(current);
            current = new HashMap<>();
            return;
        }
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreElements()) {
            String[] tokens = st.nextToken().split(":");
            current.put(tokens[0], tokens[1]);
        }
    }
}

