package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import java.math.BigInteger;

final class DecodedBitStreamParser {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode = null;
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final BigInteger[] EXP900 = new BigInteger[16];
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final char[] MIXED_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', 13, 9, ',', ':', '#', '-', '.', '$', '/', '+', '%', '*', '=', '^'};
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final char[] PUNCT_CHARS = new char[]{';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', 13, 9, ',', ':', 10, '-', '.', '$', '/', '\"', '|', '*', '(', ')', '?', '{', '}', '\''};
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode() {
        int[] iArr = $SWITCH_TABLE$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode;
        if (iArr == null) {
            iArr = new int[Mode.values().length];
            try {
                iArr[Mode.ALPHA.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Mode.ALPHA_SHIFT.ordinal()] = 5;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Mode.LOWER.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Mode.MIXED.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Mode.PUNCT.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Mode.PUNCT_SHIFT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode = iArr;
        }
        return iArr;
    }

    static {
        EXP900[0] = BigInteger.ONE;
        BigInteger nineHundred = BigInteger.valueOf(900);
        EXP900[1] = nineHundred;
        for (int i = 2; i < EXP900.length; i++) {
            EXP900[i] = EXP900[i - 1].multiply(nineHundred);
        }
    }

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(int[] codewords) throws FormatException {
        StringBuilder result = new StringBuilder(100);
        int codeIndex = 1 + 1;
        int code = codewords[1];
        while (true) {
            int codeIndex2 = codeIndex;
            if (codeIndex2 >= codewords[0]) {
                return new DecoderResult(null, result.toString(), null, null);
            }
            switch (code) {
                case TEXT_COMPACTION_MODE_LATCH /*900*/:
                    codeIndex2 = textCompaction(codewords, codeIndex2, result);
                    break;
                case BYTE_COMPACTION_MODE_LATCH /*901*/:
                    codeIndex2 = byteCompaction(code, codewords, codeIndex2, result);
                    break;
                case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                    codeIndex2 = numericCompaction(codewords, codeIndex2, result);
                    break;
                case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                    codeIndex2 = byteCompaction(code, codewords, codeIndex2, result);
                    break;
                case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                    codeIndex2 = byteCompaction(code, codewords, codeIndex2, result);
                    break;
                default:
                    codeIndex2 = textCompaction(codewords, codeIndex2 - 1, result);
                    break;
            }
            if (codeIndex2 < codewords.length) {
                codeIndex = codeIndex2 + 1;
                code = codewords[codeIndex2];
            } else {
                throw FormatException.getFormatInstance();
            }
        }
    }

    private static int textCompaction(int[] codewords, int codeIndex, StringBuilder result) {
        int[] textCompactionData = new int[(codewords[0] << 1)];
        int[] byteCompactionData = new int[(codewords[0] << 1)];
        int index = 0;
        boolean end = false;
        while (codeIndex < codewords[0] && !end) {
            int codeIndex2 = codeIndex + 1;
            int code = codewords[codeIndex];
            if (code >= TEXT_COMPACTION_MODE_LATCH) {
                switch (code) {
                    case TEXT_COMPACTION_MODE_LATCH /*900*/:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        break;
                    case BYTE_COMPACTION_MODE_LATCH /*901*/:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        break;
                    case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        break;
                    case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                        textCompactionData[index] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                        codeIndex = codeIndex2 + 1;
                        byteCompactionData[index] = codewords[codeIndex2];
                        index++;
                        break;
                    case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        break;
                    default:
                        codeIndex = codeIndex2;
                        break;
                }
            }
            textCompactionData[index] = code / 30;
            textCompactionData[index + 1] = code % 30;
            index += 2;
            codeIndex = codeIndex2;
        }
        decodeTextCompaction(textCompactionData, byteCompactionData, index, result);
        return codeIndex;
    }

    private static void decodeTextCompaction(int[] textCompactionData, int[] byteCompactionData, int length, StringBuilder result) {
        Mode subMode = Mode.ALPHA;
        Mode priorToShiftMode = Mode.ALPHA;
        for (int i = 0; i < length; i++) {
            int subModeCh = textCompactionData[i];
            char ch = 0;
            switch ($SWITCH_TABLE$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode()[subMode.ordinal()]) {
                case 1:
                    if (subModeCh >= 26) {
                        if (subModeCh != 26) {
                            if (subModeCh != 27) {
                                if (subModeCh != 28) {
                                    if (subModeCh != 29) {
                                        if (subModeCh == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                            result.append((char) byteCompactionData[i]);
                                            break;
                                        }
                                    }
                                    priorToShiftMode = subMode;
                                    subMode = Mode.PUNCT_SHIFT;
                                    break;
                                }
                                subMode = Mode.MIXED;
                                break;
                            }
                            subMode = Mode.LOWER;
                            break;
                        }
                        ch = ' ';
                        break;
                    }
                    ch = (char) (subModeCh + 65);
                    break;
                    break;
                case 2:
                    if (subModeCh >= 26) {
                        if (subModeCh != 26) {
                            if (subModeCh != 27) {
                                if (subModeCh != 28) {
                                    if (subModeCh != 29) {
                                        if (subModeCh == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                            result.append((char) byteCompactionData[i]);
                                            break;
                                        }
                                    }
                                    priorToShiftMode = subMode;
                                    subMode = Mode.PUNCT_SHIFT;
                                    break;
                                }
                                subMode = Mode.MIXED;
                                break;
                            }
                            priorToShiftMode = subMode;
                            subMode = Mode.ALPHA_SHIFT;
                            break;
                        }
                        ch = ' ';
                        break;
                    }
                    ch = (char) (subModeCh + 97);
                    break;
                    break;
                case 3:
                    if (subModeCh >= PL) {
                        if (subModeCh != PL) {
                            if (subModeCh != 26) {
                                if (subModeCh != 27) {
                                    if (subModeCh != 28) {
                                        if (subModeCh != 29) {
                                            if (subModeCh == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                                result.append((char) byteCompactionData[i]);
                                                break;
                                            }
                                        }
                                        priorToShiftMode = subMode;
                                        subMode = Mode.PUNCT_SHIFT;
                                        break;
                                    }
                                    subMode = Mode.ALPHA;
                                    break;
                                }
                                subMode = Mode.LOWER;
                                break;
                            }
                            ch = ' ';
                            break;
                        }
                        subMode = Mode.PUNCT;
                        break;
                    }
                    ch = MIXED_CHARS[subModeCh];
                    break;
                    break;
                case 4:
                    if (subModeCh >= 29) {
                        if (subModeCh != 29) {
                            if (subModeCh == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                result.append((char) byteCompactionData[i]);
                                break;
                            }
                        }
                        subMode = Mode.ALPHA;
                        break;
                    }
                    ch = PUNCT_CHARS[subModeCh];
                    break;
                    break;
                case 5:
                    subMode = priorToShiftMode;
                    if (subModeCh >= 26) {
                        if (subModeCh == 26) {
                            ch = ' ';
                            break;
                        }
                    }
                    ch = (char) (subModeCh + 65);
                    break;
                    break;
                case 6:
                    subMode = priorToShiftMode;
                    if (subModeCh >= 29) {
                        if (subModeCh == 29) {
                            subMode = Mode.ALPHA;
                            break;
                        }
                    }
                    ch = PUNCT_CHARS[subModeCh];
                    break;
                    break;
            }
            if (ch != 0) {
                result.append(ch);
            }
        }
    }

    private static int byteCompaction(int mode, int[] codewords, int codeIndex, StringBuilder result) {
        int count;
        long value;
        char[] decodedData;
        boolean end;
        int codeIndex2;
        int code;
        int j;
        if (mode == BYTE_COMPACTION_MODE_LATCH) {
            count = 0;
            value = 0;
            decodedData = new char[6];
            int[] byteCompactedCodewords = new int[6];
            end = false;
            while (codeIndex < codewords[0] && !end) {
                codeIndex2 = codeIndex + 1;
                code = codewords[codeIndex];
                if (code < TEXT_COMPACTION_MODE_LATCH) {
                    byteCompactedCodewords[count] = code;
                    count++;
                    value = (900 * value) + ((long) code);
                    codeIndex = codeIndex2;
                } else if (code == TEXT_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH || code == NUMERIC_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH_6 || code == BEGIN_MACRO_PDF417_CONTROL_BLOCK || code == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || code == MACRO_PDF417_TERMINATOR) {
                    codeIndex = codeIndex2 - 1;
                    end = true;
                } else {
                    codeIndex = codeIndex2;
                }
                if (count % 5 == 0 && count > 0) {
                    for (j = 0; j < 6; j++) {
                        decodedData[5 - j] = (char) ((int) (value % 256));
                        value >>= 8;
                    }
                    result.append(decodedData);
                    count = 0;
                }
            }
            for (int i = (count / 5) * 5; i < count; i++) {
                result.append((char) byteCompactedCodewords[i]);
            }
        } else if (mode == BYTE_COMPACTION_MODE_LATCH_6) {
            count = 0;
            value = 0;
            end = false;
            while (codeIndex < codewords[0] && !end) {
                codeIndex2 = codeIndex + 1;
                code = codewords[codeIndex];
                if (code < TEXT_COMPACTION_MODE_LATCH) {
                    count++;
                    value = (900 * value) + ((long) code);
                    codeIndex = codeIndex2;
                } else if (code == TEXT_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH || code == NUMERIC_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH_6 || code == BEGIN_MACRO_PDF417_CONTROL_BLOCK || code == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || code == MACRO_PDF417_TERMINATOR) {
                    codeIndex = codeIndex2 - 1;
                    end = true;
                } else {
                    codeIndex = codeIndex2;
                }
                if (count % 5 == 0 && count > 0) {
                    decodedData = new char[6];
                    for (j = 0; j < 6; j++) {
                        decodedData[5 - j] = (char) ((int) (255 & value));
                        value >>= 8;
                    }
                    result.append(decodedData);
                }
            }
        }
        return codeIndex;
    }

    private static int numericCompaction(int[] codewords, int codeIndex, StringBuilder result) throws FormatException {
        int count = 0;
        boolean end = false;
        int[] numericCodewords = new int[15];
        while (codeIndex < codewords[0] && !end) {
            int codeIndex2 = codeIndex + 1;
            int code = codewords[codeIndex];
            if (codeIndex2 == codewords[0]) {
                end = true;
            }
            if (code < TEXT_COMPACTION_MODE_LATCH) {
                numericCodewords[count] = code;
                count++;
                codeIndex = codeIndex2;
            } else if (code == TEXT_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH || code == BYTE_COMPACTION_MODE_LATCH_6 || code == BEGIN_MACRO_PDF417_CONTROL_BLOCK || code == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || code == MACRO_PDF417_TERMINATOR) {
                codeIndex = codeIndex2 - 1;
                end = true;
            } else {
                codeIndex = codeIndex2;
            }
            if (count % 15 == 0 || code == NUMERIC_COMPACTION_MODE_LATCH || end) {
                result.append(decodeBase900toBase10(numericCodewords, count));
                count = 0;
            }
        }
        return codeIndex;
    }

    private static String decodeBase900toBase10(int[] codewords, int count) throws FormatException {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < count; i++) {
            result = result.add(EXP900[(count - i) - 1].multiply(BigInteger.valueOf((long) codewords[i])));
        }
        String resultString = result.toString();
        if (resultString.charAt(0) == '1') {
            return resultString.substring(1);
        }
        throw FormatException.getFormatInstance();
    }
}
