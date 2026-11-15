import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class QuizGame {

    static Scanner input = new Scanner(System.in);

    // Question class
    static class Question {
        String question;
        String[] options;
        int correctAnswer;
        String fact;

        Question(String question, String[] options, int correctAnswer, String fact) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.fact = fact;
        }
    }

    // Mistake class
    static class Mistake {
        Question question;
        int userAnswer;

        Mistake(Question question, int userAnswer) {
            this.question = question;
            this.userAnswer = userAnswer;
        }
    }

    // Streak information
    static class Streak {
        LocalDate lastQuizDate = null;
        int currentStreak = 0;
        int longestStreak = 0;
    }

    // Create all 10 questions with their facts
    public static ArrayList<Question> createQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        questions.add(new Question(
            "Which keyword is used to create an object in Java?",
            new String[]{"class", "new", "this", "static"},
            2,
            "The new keyword allocates memory and creates objects in Java."
        ));

        questions.add(new Question(
            "Which method is the starting point of a Java program?",
            new String[]{"start()", "run()", "main()", "execute()"},
            3,
            "The main() method is where Java program execution begins."
        ));

        questions.add(new Question(
            "Who originally developed Java?",
            new String[]{"Microsoft", "Sun Microsystems", "Google", "Apple"},
            2,
            "Java was originally developed by James Gosling at Sun Microsystems."
        ));

        questions.add(new Question(
            "Which symbol is used to end a statement in Java?",
            new String[]{":", ".", ";", ","},
            3,
            "A semicolon is used to terminate most Java statements."
        ));

        questions.add(new Question(
            "Which of the following is a non-primitive data type?",
            new String[]{"int", "float", "char", "String"},
            4,
            "String is a non-primitive type because it is a class in Java."
        ));

        questions.add(new Question(
            "Which keyword is used for inheritance in Java?",
            new String[]{"implements", "extends", "inherits", "super"},
            2,
            "The extends keyword allows a class to inherit another class."
        ));

        questions.add(new Question(
            "What is it called when methods have the same name but different parameters?",
            new String[]{"Overloading", "Overriding", "Encapsulation", "Abstraction"},
            1,
            "Method overloading allows multiple methods with the same name but different parameters."
        ));

        questions.add(new Question(
            "Which loop executes at least once?",
            new String[]{"for", "while", "do-while", "nested loop"},
            3,
            "A do-while loop checks its condition after executing the loop body."
        ));

        questions.add(new Question(
            "Which keyword prevents a class from being inherited?",
            new String[]{"final", "static", "private", "abstract"},
            1,
            "A final class cannot be extended by another class."
        ));

        questions.add(new Question(
            "Which operator is used to compare two values for equality?",
            new String[]{"=", "==", "!=", "++"},
            2,
            "The == operator checks whether two values are equal."
        ));

        return questions;
    }

    // Get valid user choice with error handling
    public static int getChoice(int min, int max) {
        while (true) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            try {
                int choice = Integer.parseInt(input.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    // Welcome screen
    public static void welcomeScreen() {
        System.out.println("\n==================================");
        System.out.println("           QUIZVERSE              ");
        System.out.println("     PLAY, LEARN & IMPROVE        ");
        System.out.println("==================================");
        System.out.println("     EXPLORE, THINK, CONQUER      ");
        System.out.println("==================================");
    }

    // Load streak from file
    public static Streak loadStreak() {
        Streak streak = new Streak();
        try (BufferedReader reader = new BufferedReader(new FileReader("streak.txt"))) {
            String date = reader.readLine();
            String current = reader.readLine();
            String longest = reader.readLine();

            if (date != null && !date.equals("null")) {
                streak.lastQuizDate = LocalDate.parse(date);
            }
            if (current != null) {
                streak.currentStreak = Integer.parseInt(current);
            }
            if (longest != null) {
                streak.longestStreak = Integer.parseInt(longest);
            }
        } catch (IOException | NumberFormatException e) {
            // First run or file doesn't exist yet: streak starts at zero.
        }
        return streak;
    }

    // Save streak information to file
    public static void saveStreak(Streak streak) {
        try (FileWriter writer = new FileWriter("streak.txt")) {
            writer.write(String.valueOf(streak.lastQuizDate) + "\n");
            writer.write(streak.currentStreak + "\n");
            writer.write(streak.longestStreak + "\n");
        } catch (IOException e) {
            System.out.println("Unable to save streak information.");
        }
    }

    // Update daily streak after completing a quiz
    public static void updateStreak(Streak streak) {
        LocalDate today = LocalDate.now();

        if (streak.lastQuizDate == null) {
            streak.currentStreak = 1;
        } else if (streak.lastQuizDate.equals(today)) {
            // Already completed a quiz today. Do not increase streak again.
        } else if (streak.lastQuizDate.plusDays(1).equals(today)) {
            // Played on consecutive days.
            streak.currentStreak++;
        } else {
            // Missed one or more days.
            streak.currentStreak = 1;
        }

        streak.lastQuizDate = today;

        if (streak.currentStreak > streak.longestStreak) {
            streak.longestStreak = streak.currentStreak;
        }

        saveStreak(streak);

        System.out.println("\n==================================");
        System.out.println("       DAILY STREAK UPDATE        ");
        System.out.println("==================================");
        System.out.println("Current Streak: " + streak.currentStreak + " day(s)");
        System.out.println("Longest Streak: " + streak.longestStreak + " day(s)");
        System.out.println("==================================");
    }

    // Display streak information
    public static void showStreak(Streak streak) {
        System.out.println("\n==================================");
        System.out.println("       YOUR LEARNING STREAK       ");
        System.out.println("==================================");
        System.out.println("Current Streak: " + streak.currentStreak + " day(s)");
        System.out.println("Longest Streak: " + streak.longestStreak + " day(s)");

        if (streak.lastQuizDate != null) {
            System.out.println("Last Quiz Date: " + streak.lastQuizDate);
        } else {
            System.out.println("You haven't completed a quiz yet!");
        }
        System.out.println("==================================");
    }

    // Start quiz
    public static void startQuiz(Streak streak) {
        ArrayList<Question> questions = createQuestions();
        ArrayList<Mistake> mistakes = new ArrayList<>();

        // Randomize question order
        Collections.shuffle(questions);

        int score = 0;

        System.out.println("\n==================================");
        System.out.println("           QUIZ STARTED           ");
        System.out.println("==================================");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);

            System.out.println("\nQuestion " + (i + 1) + " of " + questions.size());
            System.out.println(q.question);

            for (int j = 0; j < q.options.length; j++) {
                System.out.println((j + 1) + ". " + q.options[j]);
            }

            int answer = getChoice(1, 4);

            if (answer == q.correctAnswer) {
                System.out.println("\nCorrect Answer! 🎉");
                score++;
            } else {
                System.out.println("\nWrong Answer! ❌");
                System.out.println("Your Answer: " + q.options[answer - 1]);
                System.out.println("Correct Answer: " + q.options[q.correctAnswer - 1]);
                mistakes.add(new Mistake(q, answer));
            }

            // Interesting fact
            System.out.println("\n💡 Interesting Fact: " + q.fact);
            System.out.println("----------------------------------");
        }

        showResult(score, questions.size());
        updateStreak(streak);
        showMistakeNotebook(mistakes);
    }

    // Show final result
    public static void showResult(int score, int total) {
        double percentage = (score * 100.0) / total;

        System.out.println("\n==================================");
        System.out.println("           QUIZ RESULT            ");
        System.out.println("==================================");
        System.out.println("Total Questions: " + total);
        System.out.println("Correct Answers: " + score);
        System.out.println("Wrong Answers:   " + (total - score));
        System.out.printf("Percentage:      %.2f%%\n", percentage);

        if (percentage >= 80) {
            System.out.println("Excellent! Amazing performance!");
        } else if (percentage >= 50) {
            System.out.println("Good job! Keep learning!");
        } else {
            System.out.println("Keep practicing! You can improve!");
        }
        System.out.println("==================================");
    }

    // Mistake Notebook
    public static void showMistakeNotebook(ArrayList<Mistake> mistakes) {
        System.out.println("\n==================================");
        System.out.println("         MISTAKE NOTEBOOK         ");
        System.out.println("==================================");

        if (mistakes.isEmpty()) {
            System.out.println("Perfect! You made no mistakes! 🌟");
        } else {
            for (int i = 0; i < mistakes.size(); i++) {
                Mistake m = mistakes.get(i);
                System.out.println("\nMistake " + (i + 1));
                System.out.println("Question: " + m.question.question);
                System.out.println("Your Answer: " + m.question.options[m.userAnswer - 1]);
                System.out.println("Correct Answer: " + m.question.options[m.question.correctAnswer - 1]);
                System.out.println("Fact: " + m.question.fact);
            }
        }
        System.out.println("\n==================================");
    }

    // Main method
    public static void main(String[] args) {
        Streak streak = loadStreak();

        while (true) {
            welcomeScreen();

            System.out.println("\n1. Play Quiz");
            System.out.println("2. View Learning Streak");
            System.out.println("3. Exit");

            int choice = getChoice(1, 3);

            if (choice == 1) {
                startQuiz(streak);
            } else if (choice == 2) {
                showStreak(streak);
            } else {
                System.out.println("\nThank you for playing QuizVerse!");
                System.out.println("Keep learning. Keep growing!");
                break;
            }
        }

        input.close();
    }
}
