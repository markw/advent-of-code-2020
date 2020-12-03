import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

public class Day2Part2 {
    public static void main(String[] args) throws Exception {
        Stream<String> input = Files.lines(Paths.get("input-day2.txt"));
        long count = input.filter(Day2Part2::matches).count();
        System.out.println(count);
    }

    static boolean matches(String s) {
        Scanner scan = new Scanner(s);
        scan.findInLine("(\\d+)-(\\d+)\\s(\\w+):\\s(\\w+)");
        MatchResult mr = scan.match();
        int first = Integer.parseInt(mr.group(1));
        int second = Integer.parseInt(mr.group(2));
        char ch = mr.group(3).charAt(0);
        String input = mr.group(4);

        boolean firstMatches = input.charAt(first-1) == ch;
        boolean secondMatches = input.charAt(second-1) == ch;
        return firstMatches ^ secondMatches;
    }
}

