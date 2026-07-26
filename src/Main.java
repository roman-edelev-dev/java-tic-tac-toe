import java.util.InputMismatchException; // Для обработки ошибки с вводом
import java.util.Scanner; // Для ввода с клавиатуры, но также можно для чтения файлов
import java.util.Random; // Для бота
import java.util.List; // Для динамического списка(для переменной хранящей пустые поля)
import java.util.ArrayList; // Аналогично выше

public class Main {
    public static void main(String[] args) {
        // Вывод приветствия и поля
        System.out.println("++++++++======КреСТИкИ-НоЛИки======++++++++");

        // Здесь мы видим "new Scanner.." -> ожидаем тип Scanner. Поэтому можем не писать "Scanner (имя)", а пишем "var (имя)", оно короче и понятней
        var scanner = new Scanner(System.in); // System.in означает, что мы ожидаем ввод с клавиатуры. Также можно вместо этого вписать файл и читать его
        int xWins = 0;
        int oWins = 0;
        int draws = 0;
        int uWins = 0;
        int bWins = 0;

        System.out.print("Играть с ботом (" + GREEN + "y" + RESET + "/" + RED + "n" + RESET + ")? ");
        boolean withBotOrNo = askYesNo(scanner);

        while (true) {
            char result = withBotOrNo ? playOneGameWithBot(scanner) : playOneGame(scanner);

            switch (result) {
                case 'x' -> xWins++; // o
                case 'o' -> oWins++; // x
                case 'd' -> draws++; // Ничья
                case 'u' -> uWins++; // Выиграл user
                case 'b' -> bWins++; // Выиграл bot
            }

            System.out.println();
            System.out.println("-------------------------------------------");
            System.out.println("Счет для одного -> " + GREEN + "x" + RESET + ": " + xWins + " | " + RED + "o" + RESET + ": " + oWins + " | ничья: " + draws);
            System.out.println("Счет с ботом -> " + GREEN + "user" + RESET + ": " + uWins + " | " + RED + "bot" + RESET + ": " + bWins + " | ничья: " + draws);
            System.out.println("-------------------------------------------");
            System.out.println();

            // Хочет ли пользователь продолжить? Если нет, то выходим из цикла
            System.out.print("Сыграть еще раз (" + GREEN + "y" + RESET + "/" + RED + "n" + RESET + ")? ");
            if (!askYesNo(scanner))
                break; //askYesNo вернет false(пользователь хочет закончить), тогда '!' изменит его на true и сработает break
        }
    }

    private static void printBoard(char[][] board) {
        // char[][] board внутри скобок, это тип данных и имя (может быть любое,
        // необязательно такое же как в блоке ниже) которе будет использоваться
        // Вывод текущего поля
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Убрали лишние пробелы в конце + сделали расстояние между "."
                if (j != 2) {
                    System.out.print(board[i][j] + "  ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static boolean isWin(char turn, char[][] board) { // Важно! ЛЮБОЙ путь выполнения должен вести к return
        for (int i = 0; i < 3; i++) {
            // Проверка строк и столбцов
            // Если победа - возвращаем true (return true), до этого я хотел инициализировать переменную
            // если она подходит - меняю на true и только в конце return. Мало того, что вызывало ошибки, код проходил лишние строки (мог завершмться раньше)
            if ((board[i][0] == turn && board[i][1] == turn && board[i][2] == turn)) return true;
            if ((board[0][i] == turn && board[1][i] == turn && board[2][i] == turn)) return true;
        }
        // Проверка главной и побочной диагонали
        if ((board[0][0] == turn && board[1][1] == turn && board[2][2] == turn)) return true;
        if ((board[0][2] == turn && board[1][1] == turn && board[2][0] == turn)) return true;

        return false; // Код пройдет все проверки для победы, если ни 1 не будет пройдена - вернет false
    }

    private static boolean isDraw(int moveCount) {
            /*
            Было вот так (много и некрасиво):
            if (moveCount == 9) {
                return true;
            }
            else {
                return false;
            }
             */
        // Стало:
        return moveCount == 9; // Это выражение уже вернет булевое значение
    }

    private static char nextTurn(char turn) {
        // Еще можно заменить чуть старшим способом (через тернарный оператор(? и :)):
        // return (turn == 'x' ? 'o' : 'x');
        return switch (turn) {
            case 'x' -> 'o';
            case 'o' -> 'x';
            default -> turn;
        }; // Важно! В switch ; после } Примечание(от 24.07): только в return switch ставится
    }

    private static char playOneGame(Scanner scanner) { // Извне берется только сканер
        // Поле
        char[][] board = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };

        // Текущий ход. Начало с "х"
        char turn = 'x';

        // Счетчик ходов
        int moveCount = 0;

        while (true) {
            System.out.println();
            printBoard(board); // Вывод поля
            System.out.println();

            System.out.println("Сейчас ходит: " + turn);

            // Запрос у пользователя ячейки поля
            // Блок try/catch сохраняет переменные инициализированные внутри него оставляем внутри себя
            // Поэтому объявляем заранее
            int row = 0;
            int col = 0;

            // Ввод и обработка ошибок
            int move[] = readMove(scanner, board); // Надо внутрь переменную, не надо их объявлять
            row = move[0];
            col = move[1];
            // Проверка на занятость ячейки

            if (board[row][col] == '.') {
                moveCount++; // Увеличиваем ход на 1
                board[row][col] = turn; // Заполняем ячейку

                if (isWin(turn, board)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    System.out.println(GREEN + "Победил игрок: " + turn + ". Поздравляем!" + RESET);
                    return turn;
                }

                // Проверка на ничью
                if (isDraw(moveCount)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    System.out.println("Ничья!");
                    return 'd';
                }

                // Смена хода
                turn = nextTurn(turn);

            } else {
                System.out.println(RED + "!!! Поле занято !!!" + RESET);
            }
        }
    }

    private static boolean askYesNo(Scanner scanner) {
        while (true) {
            char answer = scanner.next().trim().toLowerCase().charAt(0); //trim убрать невидимые символы,
            // toLowerCase переводит в нижний регистр,
            // а charAt(0) берет первый символ(индекс который в скобках), пример: yes -> y
            switch (answer) {
                case 'y' -> {
                    return true;
                } //  Без {} выдает ошибку. Разрешены только выражения, блоки(фигурные скобки {}) и выброс ошибок (throw ...)
                case 'n' -> {
                    return false;
                } // Поэтому прячем return в фигурные скобки, который разрешен синтаксисом языка
                default ->
                        System.out.println(RED + "Неверный ввод. Введите y, чтобы продолжить или n, чтобы завершить" + RESET); // Вызов метода (выражение) - разрешен
            }
        }
    }

    // Для игры с ботом
    private static char playOneGameWithBot(Scanner scanner) { // Извне берется только сканер
        Random random = new Random();

        // Поле
        char[][] board = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };

        // Орел или решка
        System.out.println("===Кто ходит первым определяется жребием===");

        boolean humanFirst = random.nextBoolean(); // Сначала было Boolean,

        char humanSymbol = humanFirst ? 'x' : 'o';
        char botSymbol = humanFirst ? 'o' : 'x';

        char turn = 'x';
        // Счетчик ходов
        int moveCount = 0;

        while (true) {

            System.out.println();
            printBoard(board); // Вывод поля
            System.out.println();

            // Запрос у пользователя ячейки поля
            // Блок try/catch сохраняет переменные инициализированные внутри него оставляем внутри себя
            // Поэтому объявляем заранее
            int row = 0;
            int col = 0;

            // Сохраняем в переменную, иначе если просто вызовем то оно сохранится в локальной переменной и не будет меняться
            int[] move = (turn == humanSymbol) ? readMove(scanner, board) : moveBot(random, board);// Надо внутрь переменную, не надо их объявлять
            row = move[0];
            col = move[1];
            if (turn != humanSymbol) System.out.println("Бот ходит в поле " + (row * 3 + col + 1));

            // Проверка на занятость ячейки
            if (board[row][col] == '.') {
                moveCount++; // Увеличиваем ход на 1
                board[row][col] = turn; // Заполняем ячейку

                if (isWin(turn, board)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    if (turn == humanSymbol) {
                        turn = 'u'; // Что выиграл user
                        System.out.println(GREEN + "Вы выиграли! Поздравляем!" + RESET);
                    } else {
                        turn = 'b';
                        System.out.println(RED + "Вы проиграли :(" + RESET);
                    }
                    return turn;
                }

                // Проверка на ничью
                if (isDraw(moveCount)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    System.out.println("Ничья.");
                    return 'd';
                }

                // Смена хода
                turn = nextTurn(turn);

            } else {
                System.out.println(RED + "!!! Поле занято !!!" + RESET);
            }
        }
    }

    private static int[] moveBot(Random random, char[][] board) {
        List<Integer> empty = new ArrayList<>(); // Инициализация динамического списка с именем empty
        for (int i = 0; i < 9; i++) { // Чтобы не делать вложенный for, есть зависимость, если например: 5/3 = 1(строка), 5%3(столб) = 2, то есть board[1][2] - это верно
            if (board[i / 3][i % 3] == '.') { // Если поле пустое(.) добавляем в список
                empty.add(i); // Метод названиеСписка.add(переменная или значение или еще что) добавляет "это" в список
            }
        }
        int pick = empty.get(random.nextInt(empty.size())); // Например: empty[6,4,8] и empty.size() вернет 3, но из-за random.nextInt()
        // он выберет не из 0 1 2 3, а до 3(не включительно) -> 0 1 2 - а это индексы empty

        return new int[]{pick / 3, pick % 3}; // чтобы не делать отдельно переменную и присваивать ей, мы просто напишем new int[] и вернем
    }

    private static int[] readMove(Scanner scanner, char[][] board) {
        while (true) {
            try {
                System.out.print("Введите номер поля от 1 до 9: ");
                System.out.println();
                int n = scanner.nextInt();
                if (n < 1 || n > 9) throw new IllegalArgumentException();
                int row = (n - 1) / 3;
                int col = (n - 1) % 3;
                if (board[row][col] != '.') {
                    System.out.println(RED + "!!! Поле занято !!!" + RESET);
                    continue;
                }
                return new int[]{row, col};
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println(RED + "Ошибка ввода!" + RESET);
                scanner.nextLine();
            }
        }
    }

    // Для изменения цвета шрифта
    // На примере красного: \u001b[31m | (27)char + "[31m" | "\033[31m"
    // Еще есть: \n перенос на новую строку, \t добавит таб
    private static final String RESET = "\u001b[0m";
    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String GRAY = "\u001b[90m";
}