import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.




public class Main {
    private static final String MENU_TEXT = """
            Используйте русскую раскладку
            
            1.Начать раунд [у]
            
            2.Выйти [н]
            
            """;

    private static final String ROUND_START_TEXT = """
            ИГРОВОЙ РАУНД НАЧАЛСЯ
            
            У ВАС 6 ЖИЗНЕЙ
            """;
    public static void main(String[] args) {
        startMenu();
    }
    public static void startMenu(){
        while (true){
            System.out.println(MENU_TEXT);
            switch (userInput("cp866")){
                case 'у' -> startGameRound();
                case 'н' -> System.exit(0);
            }
        }
    }
    public static void clearConsole(){
                System.out.flush();
    }
    public static char userInput(String charsetName){
        Scanner scanner = new Scanner(System.in, charsetName);
        String input = scanner.next().toLowerCase();
        return input.charAt(0);
    }

    public static void startGameRound(){
        clearConsole();
        System.out.println(ROUND_START_TEXT);

        List<Character> hiddenWord = selectWord();

        List<Integer> guessedIndices = new ArrayList<>();

        Set<Character> checkedCharacters = new HashSet<>();

        int lives = 6;

        if (hiddenWord.isEmpty()) {
            return;
        }

        System.out.println(getMaskedString(hiddenWord,guessedIndices));

        while (!TestForLose(lives) && !TestForVictory(hiddenWord,guessedIndices)){

            Character entered = userInput("cp866");

            Set<Integer> indices = attempt(entered,hiddenWord);

            if(!checkedCharacters.contains(entered)){

                if(!indices.isEmpty()){
                    guessedIndices.addAll(indices);
                }
                else {
                    lives--;
                }
                System.out.println(getMaskedString(hiddenWord,guessedIndices));
                System.out.println("ОСТАВШЕЕСЯ КОЛИЧЕСТВО ЖИЗНЕЙ " + lives);
            }
            checkedCharacters.add(entered);

        }
    }

    public static List<Character> selectWord() {
        String filePath = "words.txt";

        try {
            List<String> allWords = Files.readAllLines(Paths.get(filePath));

            if (allWords.isEmpty()) {
                System.out.println("Файл words.txt пуст");
                return new ArrayList<>();
            }

            Random random = new Random();
            int randomIndex = random.nextInt(allWords.size());

            String randomWord = allWords.get(randomIndex).toLowerCase();
            List<Character> hiddenWord = new ArrayList<>();
            for (int i = 0; i < randomWord.length(); i++) {
                hiddenWord.add(randomWord.charAt(i));
            }
            return hiddenWord;
        } catch (IOException ex) {
            clearConsole();
            System.err.println("Ошибка в при выборе слова из файла" + ex.getMessage());
            return new ArrayList<>();
        }
    }


    public static Set<Integer> attempt(Character userInput, List<Character> hiddenWord){
        Set<Integer> indices = new HashSet<>();

        for (int i = 0; i < hiddenWord.size(); i++) {
            if (hiddenWord.get(i).equals(userInput)) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static String getMaskedString(List<Character> hiddenWord, List<Integer> guessedIndices) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < hiddenWord.size(); i++) {
            if (guessedIndices.contains(i)) {
                result.append(hiddenWord.get(i));
            } else {
                result.append('#');
            }
        }

        return result.toString();
    }

    public static boolean TestForVictory(List<Character> hiddenWord, List<Integer> guessedIndices){
        if (guessedIndices.size() == hiddenWord.size()){
            System.out.println("Вы победили");
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean TestForLose(Integer lives){
        if (lives > 0){
            return false;
        }
        else {
            System.out.println("Вы проиграли");
            return true;
        }
    }
}