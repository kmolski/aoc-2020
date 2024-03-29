import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BinaryBoarding {
    private static List<String> readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.collect(Collectors.toList());
        }
    }

    private static int calculateIndex(String line, int max, char lowerMarker, char upperMarker) {
        int min = 0;

        for (int i = 0; i < line.length() && min != max; ++i) {
            int middle = (max - min) / 2 + min;

            if (line.charAt(i) == lowerMarker) {
                max = middle;
            } else if (line.charAt(i) == upperMarker) {
                min = middle + 1;
            }
        }

        return min;
    }

    private static List<Integer> calculateSeatIDs(List<String> lines) {
        return lines.stream().map(l -> calculateIndex(l, 127, 'F', 'B') * 8
                                     + calculateIndex(l, 7, 'L', 'R'))
                             .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Integer> seatIDs = calculateSeatIDs(readLines(filename));

            IntSummaryStatistics stats = seatIDs.stream().mapToInt(Integer::intValue).summaryStatistics();
            System.out.println("Part 1: " + stats.getMax());
            System.out.println("Part 2: " + IntStream.rangeClosed(stats.getMin(), stats.getMax())
                                                     .filter(i -> !seatIDs.contains(i))
                                                     .findAny().orElse(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
