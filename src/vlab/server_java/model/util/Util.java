package vlab.server_java.model.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Класс, реализующий экранирование/разэкранирование специальных символов
 * при передаче состояния ВЛР на сторону сервера и обратно.
 */
public class Util {


    private static Random random = new Random(System.nanoTime());

	/**
	 * метод для экранирования строки
	 * @param param исходная строка
	 * @return экранированная строка
	 */
    public static String escapeParam( String param ){
        String res = param.replaceAll( "&", "&amp;" );

        res = res.replace( "<", "&#060;" );
        res = res.replace(">", "&#062;");

        res = res.replace( "\r\n", "<br/>" );
	    res = res.replace( "\r", "<br/>" );
	    res = res.replace( "\n", "<br/>" );

        res = res.replace( "<", "&lt;" );
        res = res.replace(">", "&gt;");

        res = res.replace("-", "$");
        res = res.replace( "\"", "!" );

//        res = res.replaceAll("-", "&#0045;");
//        res = res.replaceAll( "\"", "&quot;" );

        res = res.replaceAll( "\'", "&apos;" );

        res = res.replace( "\\", "&#92;" );

        String xml10pattern = "[^"
                + "\u0009\r\n"
                + "\u0020-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]";

        res = res.replace( xml10pattern, "" );

        return res;
    }

	/**
	 * метод для разэкранирования строки
	 * @param param исходная экранированная строка
	 * @return разэкранированная строка
	 */
    public static String unescapeParam( String param ){
        String res = param.replace( "&amp;", "&" );
        res = res.replace( "&quot;", "\"" );
        res = res.replace( "&lt;br/&gt;", "\r\n");
        res = res.replace( "&lt;", "<" );
        res = res.replace( "&gt;", ">" );
        res = res.replace( "&#0045;", "-" );

        res = res.replace("$", "-");
        res = res.replace("!", "\"" );


        res = res.replace( "&apos;", "\'" );

        res = res.replace("&minus;", "-");
        res = res.replace( "&#92;", "\\" );

        res = res.replaceAll( "<br/>", "\r\n");

        return res;
    }

    public static String prepareInputJsonString(String input) {
        if( input == null ) input = "";
        else input = unescapeParam(input.trim()).trim();
        return input;
    }

    public static void main(String[] args) {
        System.out.println(escapeParam("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Program langType=\"msvc\"><!--#include <iostream>\n\nusing namespace std;\n\nint main(){\n\\tcout<<\"Hello world!\"<<endl;\n\treturn 0;\n}--></Program>\n"));
        System.out.println(escapeParam("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Program langType=\"msvc\"><!--" + "program simple;\n\nbegin\n\tWriteLn('Hello world!');\nEnd.\n" + "--></Program>\n"));
        System.out.println(escapeParam("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Program langType=\"msvc\"><!--#include <iostream>\n" +
                "#include <stdio.h>\n" +
                "#define E (1e-4)\n" +
                "#define E2 (1e-1)\n" +
                "using namespace std;\n" +
                "int main(){\n" +
                "    double l, T, m;\n" +
                "    cin >>  l >> T >> m;\n" +
                "    double x = m - T / 30;\n" +
                "     if (x <= E)\n" +
                "        cout << \"0.00\\n\";\n" +
                "    else {\n" +
                "    int x1 = x * 100;\n" +
                "    if ((x * 100) - (int)(x * 100) >= E2)\n" +
                "        x1++;\n" +
                "    printf(\"%.2lf\\n\", x1 / 100.0);    }\n" +
                "    return 0;\n" +
                "}--></Program>\n"));
    }

    public static BigDecimal shrink(BigDecimal v){
        return v.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal[] shrink(BigDecimal[] v){
        v = Arrays.copyOf(v, v.length);
        for (int i = 0; i < v.length; i++) {
            v[i] = shrink(v[i]);
        }
        return v;
    }


    public static List<BigDecimal[]> shrink(List<BigDecimal[]> v){
        List<BigDecimal[]> newV = new ArrayList<>(v.size());
        for (BigDecimal[] arr : v) {
            newV.add(shrink(arr));
        }
        return newV;
    }


    public static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    public static BigDecimal bd(double d) {
        return BigDecimal.valueOf(d);
    }


    public static int getRandomIntegerBetween(int a, int b) {
        return (a + random.nextInt(b - a + 1));
    }

    public static double getRandomDoubleBetween(int a, int b) {
        return (a + random.nextDouble() * (b-a));
    }

    public static boolean getRandomBoolean(){
        return random.nextBoolean();
    }

}
