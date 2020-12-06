import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class GroupingParser {

    private List<List<String>> groups;
    private List<String> current;

    GroupingParser() {}

    List<List<String>> parse(String fileName) {
        groups = new ArrayList<>();
        current = new ArrayList<>();
        try {
            Files.lines(Paths.get(fileName)).forEach(this::handleLine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (current.size() > 0) groups.add(current);
        return groups;
    }
    
    Stream<List<String>> stream(String fileName) {
        return parse(fileName).stream();
    }

    private void handleLine(String s) {
        if (s.isEmpty()) {
            groups.add(current);
            current = new ArrayList<>();
            return;
        }
        current.add(s);
    }
}
