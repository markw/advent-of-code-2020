import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Day4Part2 {

    static final List<Map<String,String>> passports = new ArrayList<>();
    static Map<String,String> current = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //System.out.println(hairColor.matcher("#123456").matches());
        Files.lines(Paths.get("input-day4.txt")).forEach(Day4Part2::handleLine);
        if (!current.isEmpty()) passports.add(current);

        System.out.println(passports.stream().filter(Day4Part2::isValidPassport).count());
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

