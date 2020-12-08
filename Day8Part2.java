import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Day8Part2 {

    public static void main(String[] args) throws Exception {
        List<String> program = Files.lines(Paths.get("input-day8.txt")).collect(Collectors.toList());
        boolean done = false;
        int index = -1;
        //System.out.println("  " + program);
        while (!done) {
            List<String> copy = new ArrayList<>(program);
            index = replaceNextNopOrJmp(copy, index + 1);
            //System.out.println(index + " " + copy);
            Result result = execute(copy);
            done = result.finished;
            if (done) {
                System.out.println(result);
            }
        }
    }

    static int replaceNextNopOrJmp(List<String> program, int start) {
        int index = start;
        while (true) {
            String[] tokens = program.get(index).split(" ");
            if ("jmp".equals(tokens[0])) {
                program.set(index, "nop " + tokens[1]);
                return index;
            }
            if ("nop".equals(tokens[0])) {
                program.set(index, "jmp " + tokens[1]);
                return index;
            }
            index++;
        }
    }

    static class Result {
        int accumulator;
        int index;
        boolean finished;
        List<String> statements;

        Result(int acc, int index, boolean finished, List<String> statements) {
            this.accumulator = acc;
            this.index = index;
            this.finished = finished;
            this.statements = statements;
        }

        public String toString() {
            return String.format("acc=%d, index=%d, finished=%s, statements=%s", accumulator, index, finished, statements);
        }
    }

    static Result execute(List<String> program) {
        Set<Integer> indexes = new HashSet<>();
        List<String> statements = new ArrayList<>();
        int accumulator = 0;
        int index = 0;
        while (index < program.size()) {
            //System.out.println("index=" + index + " exec=" + executed);
            if (indexes.contains(index)) return new Result(accumulator, index, false, statements);
            indexes.add(index);
            statements.add(program.get(index));
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
        return new Result(accumulator, index, true, statements);
    }
}
