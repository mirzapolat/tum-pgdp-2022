package pgdp.pingutrip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final public class PinguTrip {

    // To hide constructor in utility class.
    private PinguTrip() {}

    public static Stream<WayPoint> readWayPoints(String pathToWayPoints) {
        try {
            return new BufferedReader(new FileReader(pathToWayPoints))
                    .lines()                                            // Alle Zeilen nehmen
                    .takeWhile(a -> !a.equals("---"))                   // Nur bis kurz vor --- gehen
                    .filter(a -> !a.startsWith("//"))                   // alle Kommentare rausfiltern
                    .map(WayPoint::ofString);                           // mit der ofString Funktion in WayPoints umwandeln
        }
        catch (IOException e) {
            return Stream.empty();
        }
    }

    public static Stream<OneWay> transformToWays(List<WayPoint> wayPoints) {
        IntStream st = IntStream.range(0, wayPoints.size()-1); // Die Indizes der Liste als Int Stream. -1 damit der letzte auch genommen werden kann.
        return st.mapToObj(a -> new OneWay(wayPoints.get(a), wayPoints.get(a + 1))); // Die Indizes ersetzen durch die Paare an Waypoints für einen OneWay.
    }

    public static double pathLength(Stream<OneWay> oneWays) {
        return oneWays
                .mapToDouble(OneWay::getLength) // Alle OneWays durch ihre Längen ersetzen
                .sum(); // Summe davon
    }

    public static List<OneWay> kidFriendlyTrip(List<OneWay> oneWays) {
        double average = pathLength(oneWays.stream()) / oneWays.size();
        return IntStream.range(0, oneWays.size())
                .takeWhile(a -> oneWays.get(a).getLength() <= average)  // alle indizes nehmen, auf die es zutrifft
                .mapToObj(oneWays::get) // Auf OneWays mappen
                .toList();  // liste
        // hätte man vielleicht auch leichter machen können.
    }

    public static WayPoint furthestAwayFromHome(Stream<WayPoint> wayPoints, WayPoint home) {
        return wayPoints
                .max(Comparator.comparingDouble(p -> p.distanceTo(home)))    // Am weitestens von zu hause entfernt nehmen
                .orElse(home);  // ansonsten home nehmen
    }

    public static boolean onTheWay(Stream<OneWay> oneWays, WayPoint visit) {
        return oneWays.anyMatch(way -> way.isOnPath(visit));
    }

    public static String prettyDirections(Stream<OneWay> oneWays) {
        return oneWays
                .map(OneWay::prettyPrint)   // in pretty konvertieren
                .collect(Collectors.joining("\n")); // mit \n kombinieren
    }

    public static void main(String[] args) {
        List<WayPoint> wayPoints = readWayPoints("test_paths/path.txt").toList();
        // List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));

        List<OneWay> oneWays = transformToWays(wayPoints).toList();
        // List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        double length = pathLength(oneWays.stream());
        // 17.230 ...

        List<OneWay> kidFriendly = kidFriendlyTrip(oneWays);
        // List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        WayPoint furthest = furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0));
        // new WayPoint(19.1, 3.2);

        boolean onTheWay = onTheWay(oneWays.stream(), new WayPoint(0.0, 0.0));
        // false

        onTheWay = onTheWay(oneWays.stream(), new WayPoint(19.1, 3.2));
        // true

        String directions = prettyDirections(oneWays.stream());
        // "25 Schritte Richtung 331 Grad."
    }

}
