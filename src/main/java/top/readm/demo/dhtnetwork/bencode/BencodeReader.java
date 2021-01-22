package top.readm.demo.dhtnetwork.bencode;

import java.io.IOException;
import java.io.InputStream;

/**
 * Just like char reader
 */
public class BencodeReader {

    /**
     * Read one bencode item at a time. returns null when finished.
     * Can read from a file reader, buffered reader, even socket..
     * @return
     */
    public BencodeType read(InputStream in) throws IOException {
        /**
         * 1. Read one char, determine the type
         */
        in.mark(0);
        int c = in.read();
        if (c == -1) return null;

        char ch = (char) c;

        /**
         * 2. Push back the char to stream.
         */
        in.reset();
        /**
         * 3. For each type, call different handlers
         */
        return buildBencodeByType(in, ch);
    }

    private BencodeType buildBencodeByType(InputStream in, char ch) throws IOException{
        switch (ch) {
            case 'i':
                return readBInt(in);
            case 'l':
                return readBList(in);
            case 'd':
                return readBMap(in);
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
                return readBString(in);
            default:
                throw new IOException("Invalid data " + ch);
        }
    }

    private BInt readBInt(InputStream in) throws IOException{
        BInt bInt = new BInt();
        bInt.decode(in);
        return bInt;
    }

    private BBytes readBString(InputStream in) throws IOException{
        BBytes bString = new BBytes();
        bString.decode(in);
        return bString;
    }

    private BList readBList(InputStream in) throws IOException{
        BList bList = new BList();
        bList.decode(in);
        return bList;
    }

    private BDictionary readBMap(InputStream in) throws IOException{
        BDictionary bDictionary = new BDictionary();
        bDictionary.decode(in);
        return bDictionary;
    }


}
