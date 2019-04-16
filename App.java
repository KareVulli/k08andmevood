import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

    public static String separateThousands(String s) {
        String[] splits = s.split(",");

        DecimalFormat formatter = (DecimalFormat)  NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return splits[0] + ": " + formatter.format(Integer.parseInt(splits[1]));
    }

    public static Integer getPopulation(String s) {
        return Integer.parseInt(s.split(",")[1]);
    }

    public static void main(String[] args) throws Exception {

        // List only 
        List<String> countries = Files.readAllLines(Paths.get("data.txt"))
            .stream()
            .filter(s -> getPopulation(s) > 100000000)
            .sorted((s1, s2) -> getPopulation(s2) - getPopulation(s1))
            .map(App::separateThousands)
            .collect(Collectors.toList());

        Files.write(Paths.get("output.txt"), countries);


        // Get count grouped by millions of population

        Map<Integer, Long> countriesGroups = Files.readAllLines(Paths.get("data.txt"))
            .stream()
            .sorted((s1, s2) -> getPopulation(s2) - getPopulation(s1))
            .collect(Collectors.groupingBy(s -> getPopulation(s) / 1000000, Collectors.counting()));

        List<String> populationCounts = countriesGroups.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> "> " + entry.getKey() + " milj: " + entry.getValue())
            .collect(Collectors.toList());

        Files.write(Paths.get("outputGroups.txt"), populationCounts);

        System.out.println("Done!");
        
    }
}