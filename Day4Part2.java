import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Day4Part2 {

    public static void main(String[] args) throws Exception {

        long count = new GroupingParser()
            .parse("input-day4.txt")
            .stream()
            .map(Day4Part2::linesToMap)
            .filter(Day4Part2::isValidPassport)
            .count();

        System.out.println(count);
    }

    static boolean isIntegerBetween(int min, int max, String s) {
        try {
            int n = Integer.parseInt(s);
            return min <= n && n <= max;
        }
        catch (Exception e) { return false; }
    }

    static Pattern heightPattern = Pattern.compile("^([0-9]{2,3}+)(in|cm)$");
    static Pattern hairColor = Pattern.compile("^#[0-9a-f]{6}+$");
    static Pattern nineDigits = Pattern.compile("\\d{9}+");
    static List<String> eyeColors = Arrays.asList("amb","blu","brn","gry","grn","hzl","oth");

    static boolean isValidPassport(Map<String,String> passport) {
        if (!isIntegerBetween(1920, 2002, passport.get("byr"))) return false;
        if (!isIntegerBetween(2010, 2020, passport.get("iyr"))) return false;
        if (!isIntegerBetween(2020, 2030, passport.get("eyr"))) return false;
        if (!eyeColors.contains(passport.get("ecl"))) return false;
        if (!hairColor.matcher(passport.getOrDefault("hcl","")).matches()) return false;
        if (!nineDigits.matcher(passport.getOrDefault("pid","")).matches()) return false;

        String height = passport.getOrDefault("hgt","");
        Matcher heightMatcher = heightPattern.matcher(height);
        if (!heightMatcher.matches()) return false;
        String units = heightMatcher.group(1);
        String unitOfMeasure = heightMatcher.group(2);
        if ("cm".equals(unitOfMeasure) && !isIntegerBetween(150,193,units)) return false;
        if ("in".equals(unitOfMeasure) && !isIntegerBetween(59,76,units))   return false;

        return true;
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

