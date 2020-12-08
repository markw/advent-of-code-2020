import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Day8Part1 {

    public static void main(String[] args) throws Exception {
        List<String> program = Files.lines(Paths.get("input-day8.txt")).collect(Collectors.toList());
        System.out.println(execute(program));
    }

    static int execute(List<String> program) {
        Set<Integer> executed = new HashSet<>();
        int accumulator = 0;
        int index = 0;
        while (true) {
            //System.out.println("index=" + index + " exec=" + executed);
            if (executed.contains(index)) return accumulator;
            executed.add(index);
            String[] tokens = program.get(index).split(" ");
            switch (tokens[0]) {
                case "acc":
                    accumulator += Integer.parseInt(tokens[1]);
                    index++;
                    break;
                case "jmp":
                    index += Integer.parseInt(tokens[1]);
                    break;
                case "nop":
                    index++;
                    break;
            }
        }
    }
}
