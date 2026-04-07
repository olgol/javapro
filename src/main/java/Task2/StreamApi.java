package Task2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamApi {

    // 1. Найти 3-е наибольшее число (с учетом повторов).
    public static int thirdLargest(List<Integer> numbers) {
        return numbers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Недостаточно чисел для поиска 3-го наибольшего"));
    }

    // 2. Найти 3-е наибольшее уникальное число.
    public static int thirdLargestUnique(List<Integer> numbers) {
        return numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Недостаточно уникальных чисел для поиска 3-го наибольшего"));
    }

    // 3. Получить имена 3 самых старших инженеров (по убыванию возраста).
    public static List<String> top3OldestEngineers(List<Employee> employees) {
        return employees.stream()
                .filter(employee -> "Инженер".equals(employee.getPosition()))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    // 4. Посчитать средний возраст сотрудников с должностью "Инженер".
    public static double averageAgeOfEngineers(List<Employee> employees) {
        return employees.stream()
                .filter(employee -> "Инженер".equals(employee.getPosition()))
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0.0);
    }

    // 5. Найти самое длинное слово в списке.
    public static String longestWord(List<String> words) {
        return words.stream()
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> new IllegalArgumentException("Список слов пуст"));
    }

    // 6. Построить хеш-мапу "слово -> количество вхождений" по строке.
    public static Map<String, Long> wordFrequency(String sentence) {
        return Arrays.stream(sentence.split("\\s+"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    // 7. Отсортировать строки по длине, а при равной длине - по алфавиту.
    public static List<String> sortByLengthThenAlphabet(List<String> words) {
        return words.stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }

    // 8. Найти самое длинное слово среди всех слов массива строк.
    public static Optional<String> longestWordFromLines(String[] lines) {
        return Arrays.stream(lines)
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .filter(word -> !word.isBlank())
                .max(Comparator.comparingInt(String::length));
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);
        System.out.println("3-е наибольшее: " + thirdLargest(numbers));
        System.out.println("3-е наибольшее уникальное: " + thirdLargestUnique(numbers));

        List<Employee> employees = List.of(
                new Employee("Анна", 41, "Инженер"),
                new Employee("Игорь", 35, "Инженер"),
                new Employee("Мария", 29, "Аналитик"),
                new Employee("Олег", 45, "Инженер"),
                new Employee("Петр", 38, "Инженер")
        );
        System.out.println("Топ-3 старших инженера: " + top3OldestEngineers(employees));
        System.out.println("Средний возраст инженеров: " + averageAgeOfEngineers(employees));

        List<String> words = List.of("дом", "программирование", "кот", "алгоритм");
        System.out.println("Самое длинное слово: " + longestWord(words));
        System.out.println("Сортировка по длине и алфавиту: " + sortByLengthThenAlphabet(words));

        String sentence = "java stream api java code stream";
        System.out.println("Частота слов: " + wordFrequency(sentence));

        String[] lines = {
                "one two three four five",
                "stream api makes code readable",
                "extraordinary word is long enough"
        };
        System.out.println("Самое длинное из массива строк: " + longestWordFromLines(lines).orElse("не найдено"));
    }
}
