package com.prospecta.demo.services;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CSVService {
    public void updateCSV(final MultipartFile file, final CSVWriter writer) throws CsvException, IOException {
        final CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

        final CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).withSkipLines(0).withCSVParser(parser).build();

        final List<CsvBean> beans = new CsvToBeanBuilder<CsvBean>(csvReader).withType(CsvBean.class).build().parse();
        final StatefulBeanToCsv<CsvBean> sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer).withQuotechar('\'').withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
        updateFieldsWithFormulaExec(beans);
        sbc.write(beans);
    }

    @Data
    @NoArgsConstructor
    public static class CsvBean {
        @CsvBindByName(column = "A")
        private String A;
        @CsvBindByName(column = "B")
        private String B;
        @CsvBindByName(column = "C")
        private String C;
    }

    public static void updateFieldsWithFormulaExec(List<CsvBean> beans) {
        for (int i = 0; i < beans.size(); i++) {
            CsvBean currentBean = beans.get(i);
            if (isFormula(currentBean.getA())) {
                String expr = replaceReferences(currentBean.getA(), beans);
                currentBean.setA(evaluateExpression(expr));
            }
            if (isFormula(currentBean.getB())) {
                String expr = replaceReferences(currentBean.getB(), beans);
                currentBean.setB(evaluateExpression(expr));
            }
            if (isFormula(currentBean.getC())) {
                String expr = replaceReferences(currentBean.getC(), beans);
                currentBean.setC(evaluateExpression(expr));
            }
        }
    }

    public static boolean isFormula(String value) {
        Pattern formulaPattern = Pattern.compile("^[^=]*[A-Z]\\d+|[-+*/]");
        Matcher matcher = formulaPattern.matcher(value);
        return matcher.find();
    }

    public static String replaceReferences(String expr, List<CsvBean> beans) {
        Pattern pattern = Pattern.compile("([ABC])(\\d+)");
        Matcher matcher = pattern.matcher(expr);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String column = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2)) - 1;
            String replacement = "";
            if (column.equals("A")) {
                replacement = String.valueOf(beans.get(index).getA());
            } else if (column.equals("B")) {
                replacement = String.valueOf(beans.get(index).getB());
            } else if (column.equals("C")) {
                replacement = String.valueOf(beans.get(index).getC());
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static String evaluateExpression(final String expr) {
        if ('=' != expr.charAt(0)) {
            log.error("Evaluation mark is missing, skipping the expression : {}", expr);
            return expr;
        }
        Pattern pattern = Pattern.compile("(\\d+)|([+\\-*/])");
        Matcher matcher = pattern.matcher(expr);
        int result = 0;
        String operator = "";
        while (matcher.find()) {
            String match = matcher.group();
            if (match.matches("\\d+")) {
                if (result == 0) result += Integer.parseInt(match);
                else {
                    result = evaluate(result, Integer.parseInt(match), operator);
                    operator = "";
                }
            } else operator = match;
        }
        return String.valueOf(result);
    }

    private static int evaluate(int result, int i, String operator) {
        return switch (operator) {
            case "+" -> result + i;
            case "-" -> result - i;
            case "*" -> result * i;
            case "/" -> result / i;
            default -> 0;
        };

    }
}
