package top.readm.demo.dhtnetwork.bencode;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * Just like char reader
 */
public class BencodeReader {

    /**
     * Read one bencode item at a time. returns null when finished.
     * Can read from a file reader, buffered reader, even socket..
     * @return
     */
    public BencodeType read(PushbackReader reader) throws IOException {
        /**
         * 1. Read one char, determine the type
         */
        int c = reader.read();
        if (c == -1) return null;

        char ch = (char) c;

        /**
         * 2. Push back the char to stream.
         */
        reader.unread(ch);
        /**
         * 3. For each type, call different handlers
         */
        return buildBencodeByType(reader, ch);
    }

    private BencodeType buildBencodeByType(Reader reader, char ch) throws IOException{
        switch (ch) {
            case 'i':
                return readBInt(reader);
            case 'l':
                return readBList(reader);
            case 'd':
                return readBMap(reader);
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readBString(reader);
            default:
                throw new IOException("Invalid data " + ch);
        }
    }

    private BInt readBInt(Reader reader) throws IOException{
        BInt bInt = new BInt();
        bInt.decode(reader);
        return bInt;
    }

    private BString readBString(Reader reader) throws IOException{
        BString bString = new BString();
        bString.decode(reader);
        return bString;
    }

    private BList readBList(Reader reader) throws IOException{
        BList bList = new BList();
        bList.decode(reader);
        return bList;
    }

    private BMap readBMap(Reader reader) throws IOException{
        BMap bMap = new BMap();
        bMap.decode(reader);
        return bMap;
    }


}
