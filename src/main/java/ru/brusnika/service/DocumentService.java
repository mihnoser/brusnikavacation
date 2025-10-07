package ru.brusnika.service;

import ru.brusnika.model.User;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentService {

    public File generateVacationNotice(User user, LocalDate vacationDate, Integer vacationDays,
                                       int vacationNumber, int daysBefore) throws IOException {

        Map<String, String> templateData = new HashMap<>();
        templateData.put("employeeName", user.getFullName());
        templateData.put("shortName", user.getFirstNameAndPatronymic() != null ?
                user.getFirstNameAndPatronymic() : user.getFullName());
        templateData.put("startDate", vacationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        templateData.put("vacationDays", String.valueOf(vacationDays));
        templateData.put("notificationDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        String htmlContent = generateHtmlContent(templateData);

        Path tempFile = Files.createTempFile("vacation_notice_" + user.getTelegramId() + "_", ".html");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write(htmlContent);
        }

        return tempFile.toFile();
    }

    private String generateHtmlContent(Map<String, String> data) {
        // Используем StringBuilder чтобы избежать проблем с символами %
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>")
                .append("    <meta name=\"author\" content=\"vk.com/mihnosergey\"/>")
                .append("    <title>Уведомление об предстоящем отпуске</title>")
                .append("    <style type=\"text/css\">")
                .append("        #maincontainer")
                .append("        {")
                .append("        top:0px;")
                .append("        padding-top:0;")
                .append("        margin:0; position:relative;")
                .append("        width:800px;")
                .append("        height:100%;")
                .append("        }")
                .append("        #header")
                .append("        {")
                .append("        float:right;")
                .append("        text-align:left;")
                .append("        }")
                .append("    </style>")
                .append("</head>")
                .append("")
                .append("<body>")
                .append("<div id=\"maincontainer\" align=\"center\">")
                .append("    <table align=\"center\";>")
                .append("        <tr><td><div class=\"logo\"><img src=\"https://i.ibb.co/spKDWN6n/brusnikalogo.png\" width=\"500\"></div></td></tr>")
                .append("        <tr><td><div></div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div class=\"notification\" align=\"center\">УВЕДОМЛЕНИЕ</div></td></tr>")
                .append("        <tr><td><div class=\"headnotification\" align=\"center\">о начале ежегодного отпуска</div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div class=\"datenumber\">").append(data.get("notificationDate")).append("</div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div class=\"dear IO\" align=\"center\">Уважаемый (ая) ").append(data.get("employeeName")).append("!</div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div class=\"text\" align=\"justify\">&emsp;&emsp;&emsp;Извещаем Вас, что в соответствии с графиком отпусков филиала ООО \"Брусника. Организатор строительства\" в Новосибирске на 2025г., утвержденным приказом №БОС.НСК/04-03-01/24-96 от 16.12.24 НСК Вам будет предоставлен c ").append(data.get("startDate")).append(" г. ежегодный основной оплачиваемый отпуск продолжительностью ").append(data.get("vacationDays")).append(" календарных дней.</div></td></tr>")
                .append("        <tr><td><div><br><br></div></td></tr>")
                .append("        <tr><td><div class=\"boss\" align=\"center\">Директор филиала&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;А.В.Михно</div></td></tr>")
                .append("        <tr><td><div><br><br></div></td></tr>")
                .append("        <tr><td><div class=\"notified\">С уведомлением ознакомлен:<br><br></div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div>______________________________&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;__________________________</div></td></tr>")
                .append("        <tr><td><div>&emsp;&emsp;&emsp;(должность, ФИО)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;(подпись)</div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div>______________________</div></td></tr>")
                .append("        <tr><td><div>&emsp;&emsp;&emsp;&emsp;(дата)</div></td></tr>")
                .append("        <tr><td><div><br><br></div></td></tr>")
                .append("        <tr><td><div class=\"logo\"><img src=\"https://i.ibb.co/spKDWN6n/brusnikalogo.png\" width=\"500\"></div></td></tr>")
                .append("        <tr><td align=\"right\"><div align=\"right\" width=\"500\"><div id=\"header\">Директору филиала ООО \"Брусника.<br>Организатор строительства\"<br>в Новосибирске Михно А.В.<br>от __________________________<br>_______________________________<br>_______________________________</div></div></td></tr>")
                .append("        <tr><td><div><br><br></div></td></tr>")
                .append("        <tr><td><div class=\"notification\" align=\"center\">Заявление</div></td></tr>")
                .append("        <tr><td><div><br></div></td></tr>")
                .append("        <tr><td><div class=\"text\" align=\"justify\">&emsp;&emsp;&emsp;Прошу внести изменения в график отпусков на 2025 год и предоставить мне ежегодный основной оплачиваемый отпуск продолжительностью _____ календарных дней с \"___\"______________2025г. в связи с _____________________________________________________________________________________________</div></td></tr>")
                .append("        <tr><td><div><br><br><br></div></td></tr>")
                .append("        <tr><td><div class=\"boss\" align=\"center\">\"____\"_______________ 20___г.&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;_______________/____________________________/</div></td></tr>")
                .append("        <tr><td><div class=\"boss\" align=\"center\">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;(подпись)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;(Ф.И.О.)</div></td></tr>")
                .append("    </table>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}