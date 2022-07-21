package util;

import java.io.DataInputStream;

/**
 * Class to read a string from a {@link DataInputStream}
 */
public class ReadChars {

    public static String readStringIn(DataInputStream in){
        try{
            StringBuilder builder = new StringBuilder();
            int inputLength = in.readInt();
            for(int i=0; i<inputLength; i++){
                builder.append(in.readChar());
            }
            return builder.toString();
        }
        catch(Exception e) {
            return "";
        }
    }
}
