import java.util.InputMismatchException; // Для обработки ошибки с вводом
import java.util.Scanner; // Для ввода с клавиатуры, но также можно для чтения файлов
import java.util.Random; // Для бота

public class Main {
    public static void main(String[] args) {
        // Вывод приветствия и поля
        System.out.println("Игра: КреСТИкИ-НоЛИки");
        System.out.println();

        System.out.println("Играть с ботом? (y/n)");

        // Здесь мы видим "new Scanner.." -> ожидаем тип Scanner. Поэтому можем не писать "Scanner (имя)", а пишем "var (имя)", оно короче и понятней
        var random = new Random(); // Для бота, вместо var можно Random
        var scanner = new Scanner(System.in); // System.in означает, что мы ожидаем ввод с клавиатуры. Также можно вместо этого вписать файл и читать его
        int xWins = 0;
        int oWins = 0;
        int draws = 0;

        while (true) {
            char result = playOneGame(scanner);

            switch (result) {
                case 'x' -> xWins++;
                case 'o' -> oWins++;
                case 'd' -> draws++;
            }

            System.out.println();
            System.out.println("Счет -> x: " + xWins + " | o: " + oWins + " | ничья: " + draws);
            System.out.println();

            // Хочет ли пользователь продолжить? Если нет, то выходим из цикла
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

            try {
                System.out.print("Введите номер строки: (0-2)");
                row = scanner.nextInt(); // Здесь нельзя писать "var row", читаемость упадет. Всегда, где цифры лучше не исп. "var"
                if (row < 0 || row > 2) {
                    throw new IllegalArgumentException();
                }

                System.out.print("Введите номер столбца: (0-2)");
                col = scanner.nextInt();
                if (col < 0 || col > 2) {
                    throw new IllegalArgumentException(); // Если индекса такой ячейки нет (выход за пределы массива) - вызываем ошибку
                }

            } catch (InputMismatchException |
                     IllegalArgumentException e) { // InputMismatchException - ошибка, получил тип данных не который ожидал
                // Т.к. ранее мы выбросили ошибку с аргументом, то мы должны ее ловить в catch, а также нужно поймать ошибку с типами данных
                // Для этого используется multi-catch - это |.
                // e - нужен, чтобы хранить внутри себя данные об ошибке, потом можно будет вывести ее и посмотреть + это обязательный синтаксис Java
                // Важно! Из-за multi-catch 2 разные ошибки сохраняются в 1 e
                System.out.println("Ошибка ввода! Введите число от 0 до 2 (включительно)");
                // Важно! Если user введет например "Пока", а контейнер ожидает Int, он заберет "Пока" и сохранит его и когда после
                // обработки ошибки он вернется к этому этапу он уже будет содержать "Пока" и опять будет ошибка. Поэтому Важно его очистить.
                scanner.nextLine(); // Очистка контейнера
                continue;
            }

            // Проверка на занятость ячейки

            if (board[row][col] == '.') {
                moveCount++; // Увеличиваем ход на 1
                board[row][col] = turn; // Заполняем ячейку

                if (isWin(turn, board)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    System.out.println("Победил игрок: " + turn + ". Поздравляем!");
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
                System.out.println("Поле уже занято, выберите другое");
            }
        }
    }

    private static boolean askYesNo(Scanner scanner) {
        while (true) {
            System.out.println("Сыграть еще раз? (y/n)");
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
                        System.out.println("Неверный ввод. Введите y, чтобы продолжить или n, чтобы завершить"); // Вызов метода (выражение) - разрешен
            }
        }
    }

    private static int ChoiceOR(Scanner scanner, Random random) {
        while (true) {
            System.out.println("Выберите: орел(0) или решка(1). Напишите, что вы выбрали: 0/1");
            char answer = scanner.next().trim().toLowerCase().charAt(0); //trim убрать невидимые символы,
            // toLowerCase переводит в нижний регистр,
            // а charAt(0) берет первый символ(индекс который в скобках), пример: yes -> y

            switch (answer) {
                case '0' -> {
                    int coinFlip = random.nextInt(2); // 2 в скобках означает что от 0 до 2(не включительно)
                    return (coinFlip == 0) ? 0 : 1; // здесь интересный трюк описанный после метода будет
                } //  Без {} выдает ошибку. Разрешены только выражения, блоки(фигурные скобки {}) и выброс ошибок (throw ...)
                case '1' -> {
                    int coinFlip = random.nextInt(2);
                    return (coinFlip == 1) ? 2 : 3;
                } // Поэтому прячем return в фигурные скобки, который разрешен синтаксисом языка
                default ->
                        System.out.println("Неверный ввод. Введите 0, если вы за 'орел' или 1, если вы за 'решка'"); // Вызов метода (выражение) - разрешен
            }
        }
    }
    // Почему возвращает числа 0-4, сделано, чтобы собрать всю инфу, возвращая так мы понимаем, что выбрал пользователь и выиграл он или нет
    // Удобно, что если возвратное значение кратно 2, то выиграл user

    // Для игры с ботом
    private static char playOneGameWithBot(Scanner scanner) { // Извне берется только сканер
        // Орел или решка
        System.out.println("Кто ходит первым определяется жребием");
        System.out.println("Выберите: орел(0) или решка(1). Напишите за кого вы: 0/1");
        int choise = ChoiceOR();
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

            try {
                System.out.print("Введите номер строки: (0-2)");
                row = scanner.nextInt(); // Здесь нельзя писать "var row", читаемость упадет. Всегда, где цифры лучше не исп. "var"
                if (row < 0 || row > 2) {
                    throw new IllegalArgumentException();
                }

                System.out.print("Введите номер столбца: (0-2)");
                col = scanner.nextInt();
                if (col < 0 || col > 2) {
                    throw new IllegalArgumentException(); // Если индекса такой ячейки нет (выход за пределы массива) - вызываем ошибку
                }

            } catch (InputMismatchException |
                     IllegalArgumentException e) { // InputMismatchException - ошибка, получил тип данных не который ожидал
                // Т.к. ранее мы выбросили ошибку с аргументом, то мы должны ее ловить в catch, а также нужно поймать ошибку с типами данных
                // Для этого используется multi-catch - это |.
                // e - нужен, чтобы хранить внутри себя данные об ошибке, потом можно будет вывести ее и посмотреть + это обязательный синтаксис Java
                // Важно! Из-за multi-catch 2 разные ошибки сохраняются в 1 e
                System.out.println("Ошибка ввода! Введите число от 0 до 2 (включительно)");
                // Важно! Если user введет например "Пока", а контейнер ожидает Int, он заберет "Пока" и сохранит его и когда после
                // обработки ошибки он вернется к этому этапу он уже будет содержать "Пока" и опять будет ошибка. Поэтому Важно его очистить.
                scanner.nextLine(); // Очистка контейнера
                continue;
            }

            // Проверка на занятость ячейки

            if (board[row][col] == '.') {
                moveCount++; // Увеличиваем ход на 1
                board[row][col] = turn; // Заполняем ячейку

                if (isWin(turn, board)) {
                    printBoard(board); // Печать поля для красоты
                    System.out.println();
                    System.out.println("Победил игрок: " + turn + ". Поздравляем!");
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
                System.out.println("Поле уже занято, выберите другое");
            }
        }
    }
}