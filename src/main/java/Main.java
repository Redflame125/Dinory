import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bitte geben Sie die Wartezeit ein, wie lange die Karten aufgedeckt bleiben: ");
        int waitPeriod = scanner.nextInt() * 1000;

        System.out.println("Bitte geben Sie die Anzahl der Spieler ein: ");
        int playerCount = scanner.nextInt();

        AtomicReferenceArray<String> playerNames = new AtomicReferenceArray<>(new String[playerCount]);


        for (int i = 0; i < playerCount; i++) {
            System.out.println("Bitte geben Sie den Namen des " + (i + 1) + ". Spielers ein: ");
            playerNames.set(i, scanner.next());
        }

        new GameUI(waitPeriod, playerCount, playerNames);
    }
}
