package util;

public class ColorParser {
    public static int getChannelFromRGBA8888(int rgba8888, String channel) {
        String colStr = Integer.toHexString(rgba8888);
        if(colStr.length() < 8) {
            int numToFill = 8 - colStr.length();
            String newStr = "";
            do {
                newStr += "0";
                numToFill--;
            } while(numToFill >= 0);
        }
        if(channel.equals("red")) colStr = colStr.substring(0, 2);
        else if(channel.equals("green")) colStr = colStr.substring(2, 4);
        else if(channel.equals("blue")) colStr = colStr.substring(4, 6);
        else if(channel.equals("alpha")) colStr = colStr.substring(6);

        return Integer.parseInt(colStr, 16);
    }
}
