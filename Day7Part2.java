import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Day7Part2 {
    public static void main(String[] args) throws Exception {

        Map<String,List<Bag>> rules = Files.lines(Paths.get("input-day7.txt"))
            .map(Day7Part2::parseRule)
            .collect(Collectors.toMap(Bag::getColor, Bag::getContainedBags));

        System.out.println(numberOfContainedBags("shiny gold", 0, rules));
    }

    static int numberOfContainedBags(String color, int count, Map<String,List<Bag>> rules) {
        List<Bag> containedBags = rules.get(color);
        int size = containedBags.size();
        return rules.isEmpty() 
            ? count 
            : containedBags.stream()
                .mapToInt(bag -> bag.count + bag.count * numberOfContainedBags(bag.color, bag.count, rules))
                .reduce(0, (a, b) -> a + b);
    }

    static class Bag {
        int count;
        String color;
        List<Bag> containedBags;

        Bag(String color) {
            this(color, 1, Collections.emptyList());
        }

        Bag(String color, int count) {
            this(color, count, Collections.emptyList());
        }

        Bag(String color, int count, List<Bag> containedBags) {
            this.count = count;
            this.color = color;
            this.containedBags = containedBags;
        }

        String getColor() { return color; }
        List<Bag> getContainedBags() { return containedBags; }

        public String toString() {
            return String.format("Color: %s, containedBags: %s", color, containedBags);
        }
    }

    static Bag parseRule(String s) {
        String[] tokens = s.split(" ");
        String color = tokens[0] + " " + tokens[1];
        if ("no".equals(tokens[4])) return new Bag(color);
        int index = 4;
        List<Bag> containedBags = new ArrayList<>();
        while (index < tokens.length) {
            int number = Integer.parseInt(tokens[index]);
            String nextColor = tokens[index + 1] + " " + tokens[index + 2];
            containedBags.add(new Bag(nextColor, number));
            index += 4;
        }
        return new Bag(color, 1, containedBags);
    }
}
