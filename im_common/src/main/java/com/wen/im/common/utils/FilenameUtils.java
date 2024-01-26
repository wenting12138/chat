//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.wen.im.common.utils;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameUtils {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String EMPTY_STRING = "";
    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR;
    private static final char OTHER_SEPARATOR;
    private static final Pattern IPV4_PATTERN;
    private static final int IPV4_MAX_OCTET_VALUE = 255;
    private static final int IPV6_MAX_HEX_GROUPS = 8;
    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;
    private static final int MAX_UNSIGNED_SHORT = 65535;
    private static final int BASE_16 = 16;
    private static final Pattern REG_NAME_PART_PATTERN;

    public FilenameUtils() {
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    private static boolean isSeparator(char ch) {
        return ch == '/' || ch == '\\';
    }



    public static String separatorsToUnix(String path) {
        return path != null && path.indexOf(92) != -1 ? path.replace('\\', '/') : path;
    }

    public static String separatorsToWindows(String path) {
        return path != null && path.indexOf(47) != -1 ? path.replace('/', '\\') : path;
    }

    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        } else {
            return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
        }
    }

    public static int indexOfLastSeparator(String fileName) {
        if (fileName == null) {
            return -1;
        } else {
            int lastUnixPos = fileName.lastIndexOf(47);
            int lastWindowsPos = fileName.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    public static int indexOfExtension(String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return -1;
        } else {
            int offset;
            if (isSystemWindows()) {
                offset = fileName.indexOf(58, getAdsCriticalOffset(fileName));
                if (offset != -1) {
                    throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
                }
            }

            offset = fileName.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(fileName);
            return lastSeparator > offset ? -1 : offset;
        }
    }

    public static String getName(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            requireNonNullChars(fileName);
            int index = indexOfLastSeparator(fileName);
            return fileName.substring(index + 1);
        }
    }

    private static void requireNonNullChars(String path) {
        if (path.indexOf(0) >= 0) {
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    public static String getBaseName(String fileName) {
        return removeExtension(getName(fileName));
    }

    public static String getExtension(String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return null;
        } else {
            int index = indexOfExtension(fileName);
            return index == -1 ? "" : fileName.substring(index + 1);
        }
    }

    private static int getAdsCriticalOffset(String fileName) {
        int offset1 = fileName.lastIndexOf(SYSTEM_SEPARATOR);
        int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            return offset2 == -1 ? 0 : offset2 + 1;
        } else {
            return offset2 == -1 ? offset1 + 1 : Math.max(offset1, offset2) + 1;
        }
    }

    public static String removeExtension(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            requireNonNullChars(fileName);
            int index = indexOfExtension(fileName);
            return index == -1 ? fileName : fileName.substring(0, index);
        }
    }

    public static boolean isExtension(String fileName, String extension) {
        if (fileName == null) {
            return false;
        } else {
            requireNonNullChars(fileName);
            if (extension != null && !extension.isEmpty()) {
                String fileExt = getExtension(fileName);
                return fileExt.equals(extension);
            } else {
                return indexOfExtension(fileName) == -1;
            }
        }
    }

    public static boolean isExtension(String fileName, String... extensions) {
        if (fileName == null) {
            return false;
        } else {
            requireNonNullChars(fileName);
            if (extensions != null && extensions.length != 0) {
                String fileExt = getExtension(fileName);
                String[] var3 = extensions;
                int var4 = extensions.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String extension = var3[var5];
                    if (fileExt.equals(extension)) {
                        return true;
                    }
                }

                return false;
            } else {
                return indexOfExtension(fileName) == -1;
            }
        }
    }

    public static boolean isExtension(String fileName, Collection<String> extensions) {
        if (fileName == null) {
            return false;
        } else {
            requireNonNullChars(fileName);
            if (extensions != null && !extensions.isEmpty()) {
                String fileExt = getExtension(fileName);
                Iterator var3 = extensions.iterator();

                String extension;
                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    extension = (String)var3.next();
                } while(!fileExt.equals(extension));

                return true;
            } else {
                return indexOfExtension(fileName) == -1;
            }
        }
    }

    static String[] splitOnTokens(String text) {
        if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
            return new String[]{text};
        } else {
            char[] array = text.toCharArray();
            ArrayList<String> list = new ArrayList();
            StringBuilder buffer = new StringBuilder();
            char prevChar = 0;
            char[] var5 = array;
            int var6 = array.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                char ch = var5[var7];
                if (ch != '?' && ch != '*') {
                    buffer.append(ch);
                } else {
                    if (buffer.length() != 0) {
                        list.add(buffer.toString());
                        buffer.setLength(0);
                    }

                    if (ch == '?') {
                        list.add("?");
                    } else if (prevChar != '*') {
                        list.add("*");
                    }
                }

                prevChar = ch;
            }

            if (buffer.length() != 0) {
                list.add(buffer.toString());
            }

            return (String[])list.toArray(EMPTY_STRING_ARRAY);
        }
    }

    private static boolean isValidHostName(String name) {
        return isIPv6Address(name) || isRFC3986HostName(name);
    }

    private static boolean isIPv4Address(String name) {
        Matcher m = IPV4_PATTERN.matcher(name);
        if (m.matches() && m.groupCount() == 4) {
            for(int i = 1; i <= 4; ++i) {
                String ipSegment = m.group(i);
                int iIpSegment = Integer.parseInt(ipSegment);
                if (iIpSegment > 255) {
                    return false;
                }

                if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private static boolean isIPv6Address(String inet6Address) {
        boolean containsCompressedZeroes = inet6Address.contains("::");
        if (containsCompressedZeroes && inet6Address.indexOf("::") != inet6Address.lastIndexOf("::")) {
            return false;
        } else if ((!inet6Address.startsWith(":") || inet6Address.startsWith("::")) && (!inet6Address.endsWith(":") || inet6Address.endsWith("::"))) {
            String[] octets = inet6Address.split(":");
            if (containsCompressedZeroes) {
                List<String> octetList = new ArrayList(Arrays.asList(octets));
                if (inet6Address.endsWith("::")) {
                    octetList.add("");
                } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
                    octetList.remove(0);
                }

                octets = (String[])octetList.toArray(EMPTY_STRING_ARRAY);
            }

            if (octets.length > 8) {
                return false;
            } else {
                int validOctets = 0;
                int emptyOctets = 0;

                for(int index = 0; index < octets.length; ++index) {
                    String octet = octets[index];
                    if (octet.isEmpty()) {
                        ++emptyOctets;
                        if (emptyOctets > 1) {
                            return false;
                        }
                    } else {
                        emptyOctets = 0;
                        if (index == octets.length - 1 && octet.contains(".")) {
                            if (!isIPv4Address(octet)) {
                                return false;
                            }

                            validOctets += 2;
                            continue;
                        }

                        if (octet.length() > 4) {
                            return false;
                        }

                        int octetInt;
                        try {
                            octetInt = Integer.parseInt(octet, 16);
                        } catch (NumberFormatException var9) {
                            return false;
                        }

                        if (octetInt < 0 || octetInt > 65535) {
                            return false;
                        }
                    }

                    ++validOctets;
                }

                return validOctets <= 8 && (validOctets >= 8 || containsCompressedZeroes);
            }
        } else {
            return false;
        }
    }

    private static boolean isRFC3986HostName(String name) {
        String[] parts = name.split("\\.", -1);

        for(int i = 0; i < parts.length; ++i) {
            if (parts[i].isEmpty()) {
                return i == parts.length - 1;
            }

            if (!REG_NAME_PART_PATTERN.matcher(parts[i]).matches()) {
                return false;
            }
        }

        return true;
    }

    static {
        SYSTEM_SEPARATOR = File.separatorChar;
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }

        IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");
    }
}
