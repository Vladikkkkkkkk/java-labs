import java.util.*;

public class UniqueCharsWords {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string with words separated by space:");
        String input = scanner.nextLine();

        scanner.close();
        
        String[] words = input.trim().split("\\s+");
        List<String> resultList = new ArrayList<>();
        
        for (String word : words) {
            if (hasUniqueCharacters(word)) {
                resultList.add(word);
            }
        }
        
        String[] result = resultList.toArray(new String[0]);
        System.out.println("Words with only unique characters: " + Arrays.toString(result));
    }
    
    private static boolean hasUniqueCharacters(String word) {
        if (word.isEmpty()) {
            return false; 
        }
        Set<Character> charSet = new HashSet<>();
        for (char c : word.toCharArray()) {
            if (!charSet.add(c)) {
                return false;
            }
        }
        return true;
    }
}