import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardGame {

    // Card list
    static String[] cards = {
        "2@", "2#", "2^", "2*", "3@", "3#", "3^", "3*", "4@", "4#", "4^", "4*",
        "5@", "5#", "5^", "5*", "6@", "6#", "6^", "6*", "7@", "7#", "7^", "7*",
        "8@", "8#", "8^", "8*", "9@", "9#", "9^", "9*", "10@", "10#", "10^", "10*",
        "J@", "J#", "J^", "J*", "Q@", "Q#", "Q^", "Q*", "K@", "K#", "K^", "K*",
        "A@", "A#", "A^", "A*"
    };

    // Players' hands
    static List<List<String>> players = new ArrayList<>();

    public static void main(String[] args) {
        // Initialize the 4 players
        for (int i = 0; i < 4; i++) {
            players.add(new ArrayList<>());
        }

        // Shuffle cards
        List<String> shuffledDeck = new ArrayList<>();
        Collections.addAll(shuffledDeck, cards);
        Collections.shuffle(shuffledDeck);

        // Distribute cards to 4 players
        distributeCards(shuffledDeck);

        // Display hands of players
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + (i + 1) + "'s cards: " + players.get(i));
        }

        // Determine winner
        int winner = determineWinner();
        System.out.println("Player " + (winner + 1) + " is the winner!");
    }

    // Distribute cards evenly to 4 players
    public static void distributeCards(List<String> deck) {
        int playerIndex = 0;
        for (String card : deck) {
            players.get(playerIndex).add(card);
            playerIndex = (playerIndex + 1) % 4;
        }
    }

    // Determine the winner based on rules
    public static int determineWinner() {
        int[] maxAlphanumericCounts = new int[4];
        String[] maxAlphanumericCards = new String[4];

        // Iterate through each player's cards
        for (int i = 0; i < players.size(); i++) {
            Map<String, Integer> cardCount = new HashMap<>();
            for (String card : players.get(i)) {
                String alphanumericPart = card.substring(0, card.length() - 1); // Extract alphanumeric part
                cardCount.put(alphanumericPart, cardCount.getOrDefault(alphanumericPart, 0) + 1);
            }

            // Find the most frequent alphanumeric part and its count
            String maxAlphanumeric = "";
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : cardCount.entrySet()) {
                if (entry.getValue() > maxCount || (entry.getValue() == maxCount && compareCards(entry.getKey(), maxAlphanumeric) > 0)) {
                    maxCount = entry.getValue();
                    maxAlphanumeric = entry.getKey();
                }
            }

            maxAlphanumericCounts[i] = maxCount;
            maxAlphanumericCards[i] = maxAlphanumeric;
        }

        // Determine the winner
        int winner = 0;
        for (int i = 1; i < 4; i++) {
            if (maxAlphanumericCounts[i] > maxAlphanumericCounts[winner]) {
                winner = i;
            } else if (maxAlphanumericCounts[i] == maxAlphanumericCounts[winner]) {
                if (compareCards(maxAlphanumericCards[i], maxAlphanumericCards[winner]) > 0) {
                    winner = i;
                } else if (compareCards(maxAlphanumericCards[i], maxAlphanumericCards[winner]) == 0) {
                    // If alphanumeric parts are the same, compare symbols
                    String symbol1 = getMaxSymbol(players.get(i), maxAlphanumericCards[i]);
                    String symbol2 = getMaxSymbol(players.get(winner), maxAlphanumericCards[winner]);
                    if (compareSymbols(symbol1, symbol2) > 0) {
                        winner = i;
                    }
                }
            }
        }
        return winner;
    }

    // Compare two card alphanumeric parts (returns 1 if card1 > card2, -1 if card1 < card2, 0 if equal)
    public static int compareCards(String card1, String card2) {
        String[] order = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"}; // Descending order
        List<String> rankOrder = Arrays.asList(order);

        return Integer.compare(rankOrder.indexOf(card1), rankOrder.indexOf(card2));
    }

    // Compare two symbols (returns 1 if symbol1 > symbol2, -1 if symbol1 < symbol2, 0 if equal)
    public static int compareSymbols(String symbol1, String symbol2) {
        String[] symbolOrder = {"@", "#", "^", "*"}; // Symbol ranking
        List<String> symbolRankOrder = Arrays.asList(symbolOrder);

        return Integer.compare(symbolRankOrder.indexOf(symbol1), symbolRankOrder.indexOf(symbol2));
    }

    // Get the highest-ranked symbol for a given alphanumeric part in the player's hand
    public static String getMaxSymbol(List<String> playerCards, String alphanumericPart) {
        String maxSymbol = "*"; // Start with the lowest-ranked symbol
        for (String card : playerCards) {
            if (card.startsWith(alphanumericPart)) {
                String symbol = card.substring(card.length() - 1);
                if (compareSymbols(symbol, maxSymbol) > 0) {
                    maxSymbol = symbol;
                }
            }
        }
        return maxSymbol;
    }
}
