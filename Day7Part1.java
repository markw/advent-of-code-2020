import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Day7Part1 {
    public static void main(String[] args) throws Exception {

        Map<String,List<String>> rules = Files.lines(Paths.get("input-day7.txt"))
            .map(Day7Part1::parseRule)
            .collect(Collectors.toMap(Bag::getColor, Bag::getContainedColors));

        long count = rules.entrySet().stream().map(entry -> containsShinyGold(entry.getKey(), rules))
            .filter(Optional::isPresent)
            .count();

        System.out.println(count);
    }

    static Optional<String> containsShinyGold(String color, Map<String,List<String>> rules) {
        List<String> containedColors = rules.get(color);
        return containedColors.contains("shiny gold") 
            ? Optional.of(color) 
            : containedColors.stream()
                .map(rule -> containsShinyGold(rule, rules))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }


    static class Bag {
        String color;
        List<String> containedColors;

        Bag(String color) {
            this(color, Collections.emptyList());
        }

        Bag(String color, List<String> containedColors) {
            this.color = color;
            this.containedColors = containedColors;
        }

        String getColor() { return color; }
        List<String> getContainedColors() { return containedColors; }

        public String toString() {
            return String.format("Color: %s, containedColors: %s", color, containedColors);
        }
    }

    static Bag parseRule(String s) { 
        Scanner scanner = new Scanner(s);
        scanner.findInLine("(.+) bags contain ((\\d+ .+ bags?(,|\\.))|no other bags)");
        MatchResult mr = scanner.match();
        String color = mr.group(1);
        String rulesStr = mr.group(2);
        if (rulesStr.equals("no other bags")) return new Bag(color);

        List<String> containedColors = Arrays.stream(rulesStr.split(", ")).map(r -> {
            String[] tokens = r.split(" ");
            int count = Integer.parseInt(tokens[0]);
            String containedColor = tokens[1] + " " + tokens[2];
            return containedColor;
        })
        .collect(Collectors.toList());

        return new Bag(color, containedColors);
    }
}
