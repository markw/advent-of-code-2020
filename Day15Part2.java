import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class Day15Part2 {

    public static void main(String[] args) {

        int target = 30_000_000;
        //int target = 2020;
        Map<Integer,int[]> spoken = new HashMap<>();
        List<Integer> starting = Arrays.asList(0,5,4,1,10,14,7);

        int i = 0;
        for (int n : starting) {
            spoken.put(n, new int[] {++i, i});
        }

        int index = starting.size() + 1;
        int lastSpoken = starting.get(starting.size() - 1);
        while (index <= target) {
            //System.out.println(lastSpoken);
            int[] indices = spoken.get(lastSpoken);
            int nextNum = indices[0] - indices[1];
            int[] nextNumIndices = spoken.get(nextNum);
            int prevIndex = nextNumIndices == null ? index : nextNumIndices[0];
            spoken.put(nextNum, new int[] {index, prevIndex});
            lastSpoken = nextNum;
            index++;
        }
        System.out.println(lastSpoken);
    }
}
