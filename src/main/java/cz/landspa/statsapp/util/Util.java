package cz.landspa.statsapp.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;

public class Util {

    public static LocalDate getSeasonStart() {
        LocalDate now = LocalDate.now();
        // Pokud je aktuální datum mezi 1.1. a 31.5., sezóna začala 1.6. předchozího roku
        if (now.getMonthValue() >= 1 && now.getMonthValue() <= 5) {
            return LocalDate.of(now.getYear() - 1, 6, 1);
        }
        // Jinak začala 1.6. aktuálního roku
        return LocalDate.of(now.getYear(), 6, 1);
    }

    public static String getUrl(HttpServletRequest request){
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        return scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "");
    }

}
