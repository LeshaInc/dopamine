package ru.sigsegv.dopamine.query;

public class Tester {
    public static void main(String[] args) {
        var input = "Скворцова (Дарья ИЛИ дашенька) курс:(2 ИЛИ 3) НЕ факультет:БИТ";
        var parser = new Parser(input);
        System.out.println(parser.expr());
    }
}
