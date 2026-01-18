package com.example.demo.shared.pix;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PixPayloadGenerator {

    public static String generate(
            String pixKey,
            String merchantName,
            String merchantCity,
            String txid,
            BigDecimal amount) {

        String payload = "000201" +
                "26" + format("br.gov.bcb.pix") + format(pixKey) +
                "52040000" +
                "5303986" +
                formatField("54", formatAmount(amount)) +
                "5802BR" +
                formatField("59", merchantName) +
                formatField("60", merchantCity) +
                formatField("62", "05" + txid);

        return payload + calculateCRC(payload + "6304");
    }

    private static String format(String value) {
        return String.format("%02d%s", value.length(), value);
    }

    private static String formatField(String id, String value) {
        return id + format(value);
    }

    private static String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String calculateCRC(String payload) {
        int crc = 0xFFFF;

        for (char c : payload.toCharArray()) {
            crc ^= (c << 8);
            for (int i = 0; i < 8; i++) {
                crc = (crc & 0x8000) != 0
                        ? (crc << 1) ^ 0x1021
                        : crc << 1;
            }
        }
        return String.format("%04X", crc & 0xFFFF);
    }
}
