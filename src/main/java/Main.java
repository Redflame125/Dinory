import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bitte geben Sie die Wartezeit ein, wie lange die Karten aufgedeckt bleiben: ");
        int waitPeriod = scanner.nextInt() * 1000;

        if (waitPeriod < 1000) {
            waitPeriod = 1000;
            System.out.println("Wartezeit muss größer als 1 Sekunde sein! \nDie Wartezeit wird auf 1 Sekunde gesetzt.\n\n");
        } else if (waitPeriod > 10000) {
            waitPeriod = 10000;
            System.out.println("Wartezeit muss kleiner als 10 Sekunden sein! \nDie Wartezeit wird auf 10 Sekunden gesetzt.\n\n");
        }

        System.out.println("Bitte geben Sie die Anzahl der Spieler ein: ");
        int playerCount = scanner.nextInt();

        if (playerCount <= 0) {
            System.out.println("Es muss mindestens ein Spieler vorhanden sein! \nDie Anzahl der Spieler wird auf 1 gesetzt.\n\n");
            playerCount = 1;
        }

        AtomicReferenceArray<String> playerNames = new AtomicReferenceArray<>(new String[playerCount]);


        for (int i = 0; i < playerCount; i++) {
            System.out.println("Bitte geben Sie den Namen des " + (i + 1) + ". Spielers ein: ");
            playerNames.set(i, scanner.next());
        }

        new GameUI(waitPeriod, playerCount, playerNames);
    }
}
