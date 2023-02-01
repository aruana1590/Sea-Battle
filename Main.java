package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static final int FIELD_LENGTH = 5;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Игрок № 1, введите Ваше имя");
        String player1Name = sc.nextLine();
        System.out.println("Hello, " + player1Name + "!");
        System.out.println();
        System.out.println("Игрок № 2, введите Ваше имя");
        String player2Name = sc.nextLine();
        System.out.println("Hello, " + player2Name + "!");

        char[][] playerField1 = new char[FIELD_LENGTH][FIELD_LENGTH];//массив с пустым полем первого игрока
        char[][] playerField2 = new char[FIELD_LENGTH][FIELD_LENGTH];//массив с пустым полем второго игрока

        fillPlayerField(player1Name, playerField1);
        fillPlayerField(player2Name, playerField2);

        game(player1Name, player2Name, playerField1, playerField2);

    }


    //метод по заполнению поля игрока кораблями
    public static void fillPlayerField(String playerName, char[][] playerField) {
        //запол newPlayerнение палубами, от четырехпалубных до однопалубных
        for (int i = 4; i >= 1; i--) {
            System.out.println(playerName + ", расставляем " + i + "-палубный корабль.");
           boolean validationResult = true;
            while (validationResult != false) {
                System.out.println(playerName + ", введите х координату: ");
                int x = sc.nextInt();
                System.out.println("Введите y координату: ");
                int y = sc.nextInt();
                System.out.println("Выберите направление: 1 - по горизонтали, 2 - по вертикали");
                int direction = sc.nextInt();
                validationResult = validateCoordForShip(playerField, x, y, direction, i);

                if (validationResult == true) {
                    if (direction == 1) {
                        for (int k = 0; k < i; k++) {
                            playerField[y][x + k] = '1';
                        }
                    }
                    if (direction == 2) {
                        for (int l = 0; l < i; l++) {
                            playerField[y + l][x] = '1';
                        }
                    }
                    drawField(playerField);
                } else {
                    System.out.println("Введите координаты снова");
                }
            }
        }
    }
    static void drawField(char[][] fieldView) {

        for (char[] cells : fieldView) {
            for (char cell : cells) {
//если значение равно 0, то ячейка пустая, т.к. корабля в ней нет
                if (cell == 0) {
                    System.out.print(" |");
                }
//если клетка непустая, отличная от 0, то отрисовывается содержимое cell.
                else {
                    System.out.print(cell + "|");

                }
            }
            System.out.println("");
            System.out.println("----------");
        }
    }

    //метод по реализации самой игры
    public static void game(String player1Name, String player2Name, char[][] player1Field, char[][] player2Field) {
        char [][] battleField1 = new char[FIELD_LENGTH][FIELD_LENGTH];// поле со всеми выстрелами игрока 1
        char [][] battleField2 = new char[FIELD_LENGTH][FIELD_LENGTH];// поле со всеми встрелами игрока 2

        String currentName = player1Name;// сейчас стреляет 1 игрок
        char[][] currentField = player2Field;// по полю 2 ирока
        char[][] currentBattleField = battleField1; //все выстрелы 1 игрока сохраняются здесь

        //игра продолжается до тех пор пока живы оба игрока
        while (isPlayerAlive(player1Field) && isPlayerAlive(player2Field)) {
            // drawField(currentBattleField);
            System.out.println(currentName + ", введите координаты для выстрела. Введите x координату");
            int x = sc.nextInt();
            System.out.println("Введите y координату");
            int y = sc.nextInt();
            int shotResult = shootCheck(currentBattleField, currentField, x, y);
            if (shotResult == 0) {
                if (currentName == player1Name) {
                    currentName = player2Name; //здесь не происходит переключение на другого игрока, если текущий игрок промахнулся
                    currentField = player1Field;
                    currentBattleField = battleField2;
                } else if (currentName == player2Name) {
                    currentName = player1Name; //здесь не происходит переключение на другого игрока, если текущий игрок промахнулся
                    currentField = player2Field;
                    currentBattleField = battleField1;
                }
            }
        }
    }
    //метод проверки результата выстрела, если выстрел попадает в ячейку со значением 1, то значит стрелок попал и 1 заменяется на символ # и возвращает 1 (true)
    //если выстрел мимо, то в ячейку записывается символ * и метод возвращает значение 0 (false)
//   Возвращаемые значения нам нужны для того, чтобы в методе, внутри которого вызывается метод handleShot,
//   мы могли понимать, успешно или неуспешно прошёл выстрел. На основе этого мы принимаем решение,
//	переходит ход к другому игроку или нет.
    private static int shootCheck(char[][] battleField, char[][] playerField, int x, int y) {

        if ('1'==playerField[y][x]) {

            playerField[y][x] = '#'; //записываем символ попадания в поле соперника
            battleField[y][x] = '#'; //записываем символ попадания в поле стреляющего также
            System.out.println("Хороший выстрел!");
            return 1;
        }
        else {
            battleField[y][x] = '*';
            System.out.println("Мимо");
            return 0;
        }
    }
    //проверка
    public static boolean isPlayerAlive(char[][] playerField) {
        for (char[] cells : playerField) {
            for (char cell : cells) {
                if ('1'==cell) {
                    return true;
                }
            }

        }
        return false;
    }
    //функция, проверяющая чтобы корабли были расположены минимум через 1 клетку друг от друга
    private static boolean validateCoordForShip(char[][] field, int x, int y, int position, int shipType) {

        while (shipType != 0) {
            for (int i = 0; i < shipType; i++) {
                int xi = 0;
                int yi = 0;
                if (position == 1) {
                    yi = i;
                } else {
                    xi = i;
                }
                if (y + 1 + yi < field.length && y + 1 + yi >= 0) {
                    if (field[y + 1 + yi][x + xi] != '0') {
                        return false;
                    }
                }
                if (y - 1 + yi < field.length && y - 1 + yi >= 0) {
                    if (field[y - 1 + yi][x + xi] != '0') {
                        return false;
                    }
                }
                if (x + 1 + xi < field.length && x + 1 + xi >= 0) {
                    if (field[y + yi][x + 1 + xi] != '0') {
                        return false;
                    }
                }
                if (x - 1 + xi < field.length && x - 1 + xi >= 0) {
                    if (field[y + yi][x - 1 + xi] != '0') {
                        return false;

                    }
                }
            }
            shipType--;
        }
        return true;
    }
}