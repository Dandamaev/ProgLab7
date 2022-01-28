package managers;

import database.Credentials;
import lombok.extern.log4j.Log4j2;
import models.City;
import models.Coordinates;
import models.Government;
import models.Human;
import exceptions.ExecutionException;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import utils.NumUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Консольный менеджер
 */
@Slf4j
public class ConsoleManager {

    private Scanner scanner;
    private boolean isScript;
    private Writer writer;
    private Reader reader;

    public ConsoleManager(Reader reader, Writer writer, boolean isScript)
    {
        this.reader = reader;
        this.writer = writer;
        scanner = new Scanner(reader);
        this.isScript = isScript;
    }

    public void writeln(String message) {
        write(message + "\n");
    }

    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            log.error("Input error. {}", e.getMessage());
        }
    }

    public boolean getIsScript(){ return isScript; }

    public String read() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Credentials getCredentials(){
        String username = readWithMessage("Login: ", false);
        String password = readWithMessage("Password: ", false);

        return new Credentials(-1, username, password);
    }

    /**
     * получает введенные данные объектом
     * @return
     */
    public City getCity(){
        boolean capital = false;

        String name = readWithMessage("Enter city name: ", false);
        Coordinates coord = getCoord();

        Double area = readWithParseMinMax("Enter area (Double, от нуля и больше): ", new BigDecimal(0), NumUtil.DOUBLE_MAX, false).doubleValue();

        Number pop = readWithParseMinMax("Enter population (Long, [0; MAX_LONG]): ", new BigDecimal(0), NumUtil.LONG_MAX, true);
        Long population = pop == null ? 0L : pop.longValue();

        Long metersAboveSeaLevel = readWithParse("Enter meters above sea level (Long): ", false).longValue();
        Double timezone = readWithParseMinMax("Enter timezone (Double, [-13;15]): ", new BigDecimal(-13), new BigDecimal(15), false).doubleValue();
        while (true) {
            try {
                capital = parseBoolean(readWithMessage("It is capital? (true/false): ", false));
                break;
            }catch(NumberFormatException ex){
                writeln("True or false?");
                if(isScript) throw new ExecutionException("Cast error");
            }
        }

        Government government = getGoverment();
        Human human = getHuman();

        return new City(-1L, name, coord, LocalDate.now(), area, population, metersAboveSeaLevel, timezone, capital, government, human);
    }


    /**
     * получает координаты
     * @return
     */
    public Coordinates getCoord(){
        float x = readWithParse("Enter X (Float): ", false).floatValue();
        double y = readWithParseMinMax("Enter Y (Double, [-587; DOUBLE_MAX]): ", new BigDecimal(-587), NumUtil.DOUBLE_MAX, false).doubleValue();

        return new Coordinates(x, y);
    }

    /**
     * получает тип правления
     * @return
     */
    public Government getGoverment() {
        Government out = null;
        StringBuilder sb = new StringBuilder();
        for (Government value : Government.values()) {
            sb.append("\n").append(value.getId()).append(" - ").append(value.toString());
        }

        while (true) {
            try {
                Integer inp = readWithParse("Goverment type? Enter number: " + sb.toString(), false).intValue();
                if (inp == null) {
                    return null;
                }
                out = Government.byOrdinal(inp);
                break;
            } catch (ClassCastException | InvalidValueException ex) {
                writeln(ex.getMessage());
                if(isScript) throw new ExecutionException("Cast error");
            }
        }

        return out;
    }


    /**
     * получает возраст человека
     * @return
     */
    public Human getHuman(){
        Integer age = readWithParseMinMax("Enter age (Integer, [0; INTEGER_MAX]): ", new BigDecimal(0), NumUtil.INTEGER_MAX, false).intValue();
        return new Human(age);
    }

    /**
     * парсит boolean значение из сотроки
     * @param s
     * @return
     */
    public static boolean parseBoolean(String s) {
        if ("true".equals(s.toLowerCase())) {
            return true;
        } else if ("false".equals(s.toLowerCase())) {
            return false;
        }else{
            throw new NumberFormatException("Wrong format");
        }
    }


    /**
     * выводит сообщение с вводом от пользователя
     * @param message
     * @param canNull
     * @return
     */
    public String readWithMessage(String message, boolean canNull) {
        String output = "";

        do {
            if (output == null) {
                writeln("Try again");
            }

            if(!isScript) {
                writeln(message);
            }

            output = scanner.nextLine();
            output = output.isEmpty() ? null : output;
        }while (!isScript && !canNull && output == null);
        if(isScript && output == null)
            throw new InvalidValueException("Not-null input");

        return output;
    }

    public Number readWithParse(String msg, boolean canNull){
        Number out = null;

        while (true){
            try{
                String num = readWithMessage(msg, canNull);
                if(num == null && canNull) break;

                NumberFormat format = NumberFormat.getInstance();
                ParsePosition pos = new ParsePosition(0);
                out = format.parse(num, pos);
                if (pos.getIndex() != num.length() || pos.getErrorIndex() != -1) throw new NumberFormatException("Неверный тип данных");

                break;
            }catch (NumberFormatException ex){
                writeln(ex.getMessage());
            }
        }

        return out;
    }

    public Number readWithParseMinMax(String msg, BigDecimal min, BigDecimal max, boolean canNull){
        Number out = null;

        do {
            out = readWithParse(msg, canNull);
            if(out == null && canNull)
                break;
        }while (!NumUtil.isInRange(out, min, max));

        return out;
    }
}
