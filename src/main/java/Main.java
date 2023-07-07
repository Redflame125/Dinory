import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);

            // Get WaitPeriod
            System.out.println("Bitte geben Sie die Wartezeit ein, wie lange die Karten aufgedeckt bleiben: ");
            int waitPeriod = scanner.nextInt() * 1000;

            if (waitPeriod < 1000) { // Min WaitPeriod
                waitPeriod = 1000;
                System.out.println("Wartezeit muss größer als 1 Sekunde sein! \nDie Wartezeit wird auf 1 Sekunde gesetzt.\n\n");
            } else if (waitPeriod > 10000) { // Max WaitPeriod
                waitPeriod = 10000;
                System.out.println("Wartezeit muss kleiner als 10 Sekunden sein! \nDie Wartezeit wird auf 10 Sekunden gesetzt.\n\n");
            }

            // Get Layout
            System.out.println("Bitte geben Sie die Größe des Spielfelds ein: ");
            int layout = scanner.nextInt();
            if (layout < 4) { // Min Layout
                layout = 2;
                System.out.println("Das Spielfeld muss mindestens 2x2 groß sein! \nDie Größe des Spielfelds wird auf 2x2 gesetzt.\n\n");
            } else if (layout > 6) { // Max Layout
                layout = 6;
                System.out.println("Das Spielfeld darf maximal 6x6 groß sein! \nDie Größe des Spielfelds wird auf 6x6 gesetzt.\n\n");
            } else if (layout % 2 != 0) { // Valid Layout
                layout++;
                System.out.println("Das Spielfeld muss eine gerade Anzahl an Karten haben! \nDie Größe des Spielfelds wird auf " + layout + "x" + layout + " gesetzt.\n\n");
            }

            // Get PlayerCount
            System.out.println("Bitte geben Sie die Anzahl der Spieler ein: ");
            int playerCount = scanner.nextInt();

            if (playerCount <= 0) { // Min PlayerCount
                System.out.println("Es muss mindestens ein Spieler vorhanden sein! \nDie Anzahl der Spieler wird auf 1 gesetzt.\n\n");
                playerCount = 1;
            }

            AtomicReferenceArray<String> playerNames = new AtomicReferenceArray<>(new String[playerCount]);

            // Get PlayerNames
            for (int i = 0; i < playerCount; i++) {
                System.out.println("Bitte geben Sie den Namen des " + (i + 1) + ". Spielers ein: ");
                playerNames.set(i, scanner.next());
            }

            // Start Game
            new GameUI(waitPeriod, playerCount, playerNames, layout);
        } else {
            new GameUI(1000, 1, new AtomicReferenceArray<>(new String[]{"Player1"}), 6); // Debug
        }
    }
}
