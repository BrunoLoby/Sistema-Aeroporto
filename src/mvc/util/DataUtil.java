package mvc.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtil {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatar(LocalDate data) {
        return data.format(dateFormatter);
    }
}