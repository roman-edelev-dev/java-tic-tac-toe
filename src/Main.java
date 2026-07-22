import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Вывод приветствия и поля
        System.out.println("Игра: КреСТИкИ-НоЛИки");
        System.out.println();


        // Поле
        char[][] board = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };

        // Здесь мы видим "new Scanner.." -> ожидаем тип Scanner. Поэтому можем не писать "Scanner (имя)", а пишем "var (имя)", оно короче и понятней
        var scanner = new Scanner(System.in); // System.in означает, что мы ожидаем ввод с клавиатуры. Также можно вместо этого вписать файл и читать его

        // Текущий ход. Начало с "х"
        char turn = 'х';

        // Счетчик ходов
        int moveCount = 0;

        while (true) {

            // Вывод текущего поля
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Убрали лишние пробелы в конце + сделали расстояние между "."
                    if (j != 2) {
                        System.out.print(board[i][j] + "  ");
                    }
                    else {
                        System.out.print(board[i][j]);
                    }
                }
                System.out.println();
            }

            // Запрос у пользователя ячейки поля

            System.out.println("Сейчас ходит: " + turn);

            // Блок try/catch сохраняет переменные инициализированные внутри него оставляем внутри себя
            // Поэтому объявляем заранее
            int row = 0;
            int col = 0;

            try {
                System.out.print("Введите номер строки(0-2): ");
                row = scanner.nextInt(); // Здесь нельзя писать "var row", читаемость упадет. Всегда, где цифры лучше не исп. "var"

                System.out.print("Введите номер столбца(0-2): ");
                col = scanner.nextInt();

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    throw new Exception(); // Если индекса такой ячейки нет - специально вызываем ошибку и отправляем в блок catch
                }
            }
            catch (Exception e) { // Exception e - мы обрабатываем все возможные ошибки и отправляем в начало цикла
                System.out.println("Ошибка ввода!");
                // Важно! Если user введет например "Пока", а контейнер ожидает Int, он заберет "Пока" и сохранит его и когда после
                // обработки ошибки он вернется к этому этапу он уже будет содержать "Пока" и опять будет ошибка. Поэтому Важно его очистить.
                scanner.nextLine(); // Очистка контейнера
                continue;
            }

            // Проверка на занятость ячейки

            if (board[row][col] == '.') {
                moveCount++; // Увеличиваем ход на 1
                board[row][col] = turn; // Заполняем ячейку

                // Проверка на победу
                if (moveCount >= 5) { // Выиграть можно только, если >= 5 ходов, поэтому раньше не проверяем
                    boolean hasWon = false; // Флажок на победу

                    for (int i = 0; i < 3; i++) {
                        // Проверка строк и столбцов
                        if ((board[i][0] == turn && board[i][1] == turn && board[i][2] == turn)) hasWon = true;
                        if ((board[0][i] == turn && board[1][i] == turn && board[2][i] == turn)) hasWon = true;
                    }
                    // Проверка главной и побочной диагонали
                    if ((board[0][0] == turn && board[1][1] == turn && board[2][2] == turn)) hasWon = true;
                    if ((board[0][2] == turn && board[1][1] == turn && board[2][0] == turn)) hasWon = true;

                    if (hasWon) {
                        System.out.println();
                        System.out.println("Победил игрок: " + turn + ". Поздравляем!");
                        break;
                    }
                }

                // Проверка на ничью
                if (moveCount == 9) {
                    System.out.println();
                    System.out.println("Ничья!");
                    break;
                }

                // Смена хода
                if (turn == 'х') {
                    turn = 'о';
                }
                else {
                    turn = 'х';
                }
            }
            else {
                System.out.println("Ячейка уже занята, выберите другую");
            }
        }
    }
}