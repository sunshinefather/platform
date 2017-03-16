package com.platform.common.crypto;

public class Base64 {
	private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static byte[] decode(String input) {
        if (input.length() % 4 != 0) {
           return null;
        }
        byte decoded[] = new byte[((input.length() * 3) / 4)-(input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }
        return decoded;
    }

    public static String encode(byte[] in) {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3) {
            b = (in[i] & 0xFC) >> 2;//取高6位(11111100),向右移2位==1
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;//取低2位(11)向左移4位获得第二位的最高2位
            if (i + 1 < in.length) {
                b |= (in[i + 1] & 0xF0) >> 4;//取高4位(11110000),向右移4位，取或集==2
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;//取低4位(1111)向左移2位获得第二位的最高4位,下面依次内推
                if (i + 2 < in.length) {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }
        return out.toString();
    }
}