package server;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NameGenerator {

    private static Set<String> takenNames = new HashSet<>();
    public static String checkNameAvailability(String name) {
        if (name.isBlank()) {
            name = generateRandomName();
        }

        while (takenNames.contains(name)) {
            name = generateRandomName();
        }

        System.out.println("Player was assigned name: " + name);
        takenNames.add(name);
        return name;
    }

    private static String generateRandomName() {
        String[] words = {
                "Cactus", "Pickle", "Dragon", "Unicorn", "Ninja", "Pirate", "Robot", "Wizard", "Samurai", "Vampire", "Ghost", "Zombie"
        };

        Random random = new Random();
        String word1, word2;
        do {
            word1 = words[random.nextInt(words.length)];
            word2 = words[random.nextInt(words.length)];
        } while (word1.equals(word2));

        return word1 + word2;
    }
}
