package reactiontimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Console reaction timer: wait for GO, then press Enter as fast as you can.
 *
 * The delay before GO is random so you can't anticipate it, and pressing
 * Enter too early counts as a false start.
 *
 * Usage: java ... reactiontimer.ReactionTimer [rounds]   (1-20, default 5)
 */
public class ReactionTimer {

    static final int DEFAULT_ROUNDS = 5;
    static final int MAX_ROUNDS = 20;
    static final int MIN_DELAY_MS = 1500;
    static final int MAX_DELAY_MS = 4000;

    public static void main(String[] args) throws IOException {
        int rounds = parseRounds(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        System.out.println("Reaction timer: " + rounds + " round(s).");
        System.out.println("Press Enter as soon as you see GO. Pressing early is a false start.");

        List<Long> times = new ArrayList<>();
        int falseStarts = 0;

        for (int round = 1; round <= rounds; round++) {
            System.out.println();
            System.out.println("Round " + round + ": get ready...");

            if (!waitRandomDelay()) {
                return; // interrupted
            }

            if (pressedEarly(in)) {
                falseStarts++;
                System.out.println("Too early! Wait for GO.");
                continue;
            }

            System.out.println("GO! IMA FIRIN MAH LAZOR!");
            long start = System.nanoTime();
            if (in.readLine() == null) {
                System.out.println("No more input. Bye!");
                break;
            }
            long ms = (System.nanoTime() - start) / 1_000_000;
            times.add(ms);
            System.out.println(ms + " ms - " + rating(ms));
        }

        printSummary(times, falseStarts);
    }

    /** Reads the optional round count, falling back to the default on bad input. */
    static int parseRounds(String[] args) {
        if (args.length == 0) {
            return DEFAULT_ROUNDS;
        }
        try {
            int n = Integer.parseInt(args[0].trim());
            if (n >= 1 && n <= MAX_ROUNDS) {
                return n;
            }
        } catch (NumberFormatException ignored) {
            // fall through to the warning below
        }
        System.out.println("Rounds must be a number from 1 to " + MAX_ROUNDS
                + ". Using " + DEFAULT_ROUNDS + ".");
        return DEFAULT_ROUNDS;
    }

    /** Sleeps for a random delay. Returns false if the thread was interrupted. */
    private static boolean waitRandomDelay() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_DELAY_MS, MAX_DELAY_MS + 1));
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** True if input arrived before GO. Discards that early input. */
    static boolean pressedEarly(BufferedReader in) throws IOException {
        boolean early = false;
        while (in.ready()) {
            early = true;
            if (in.readLine() == null) {
                break;
            }
        }
        return early;
    }

    static String rating(long ms) {
        if (ms < 200) return "Lightning fast!";
        if (ms < 250) return "Great";
        if (ms < 300) return "Good";
        if (ms < 400) return "Average";
        return "Keep practicing";
    }

    static void printSummary(List<Long> times, int falseStarts) {
        System.out.println();
        System.out.println("=== Results ===");
        if (times.isEmpty()) {
            System.out.println("No valid rounds.");
        } else {
            long best = times.stream().mapToLong(Long::longValue).min().getAsLong();
            long avg = Math.round(times.stream().mapToLong(Long::longValue).average().getAsDouble());
            System.out.println("Valid rounds: " + times.size());
            System.out.println("Best:    " + best + " ms");
            System.out.println("Average: " + avg + " ms - " + rating(avg));
        }
        System.out.println("False starts: " + falseStarts);
    }
}
