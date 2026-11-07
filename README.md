# Java Reaction Timer

A console game that measures how fast you react. Wait for **GO**, then press **Enter** as quickly as you can.

- Random delay before GO, so you can't time it from a countdown
- Pressing Enter early counts as a false start
- Several rounds, with your best and average time at the end

## Run it

Requires Java 9 or newer.

```bash
javac -d out src/module-info.java src/reactiontimer/ReactionTimer.java
java -p out -m reactiontimer/reactiontimer.ReactionTimer
```

Pick the number of rounds (1-20, default 5):

```bash
java -p out -m reactiontimer/reactiontimer.ReactionTimer 3
```

Or open the folder in Eclipse (project files included) and run `ReactionTimer`.

## Example

```
Round 1: get ready...
GO! IMA FIRIN MAH LAZOR!

243 ms - Great

=== Results ===
Valid rounds: 3
Best:    221 ms
Average: 248 ms - Great
False starts: 0
```

Times include a little terminal and keyboard latency, so compare runs on the same machine.
