import javafx.scene.control.TextArea;

import java.io.*;
import java.io.RandomAccessFile;
import java.util.List;

class normalEncryption {

    static int buffer;

    static void start(List<File> files, String k, TextArea logs) throws Exception {
        logs.setText(logs.getText() + "[ Encryption started ]\n");

        byte[] text = new byte[buffer];
        byte[] key = k.getBytes();

        for (File source : files) {
            if (!source.exists()) continue;
            if (source.length() == 0) continue;

            long start = System.currentTimeMillis();
            RandomAccessFile in = new RandomAccessFile(source, "rw");
            int realSize = in.read(text, 0, buffer);
            in.seek(0);
            encrypt(text, key, key.length);
            in.write(text, 0, realSize);
            in.close();

            double done = (System.currentTimeMillis() - start) / 1000.0;
            logs.setText(logs.getText() + "[ Done in " + done + "s ] \"" + source.getPath() + "\"\n");
        }
    }

    static void start(List<File> files, File k, TextArea logs) throws Exception {
        logs.setText(logs.getText() + "[ Encryption started ]\n");

        byte[] text = new byte[buffer];
        byte[] key = new byte[buffer];

        for (File source : files) {
            if (!source.exists()) continue;
            if (source.length() == 0) continue;

            long start = System.currentTimeMillis();
            RandomAccessFile inFile = new RandomAccessFile(source, "rw");
            RandomAccessFile inKey = new RandomAccessFile(k, "r");

            int textSize = inFile.read(text, 0, buffer);
            int keySize = inKey.read(key, 0, buffer);

            encrypt(text, key, keySize);
            inFile.seek(0);
            inFile.write(text, 0, textSize);

            inFile.close();
            inKey.close();
            double done = (System.currentTimeMillis() - start) / 1000.0;
            logs.setText(logs.getText() + "[ Done in " + done + "s ] \"" + source.getPath() + "\"\n");
        }
    }

    private static void encrypt(byte[] text, byte[] key, int keySize) {
        for (int i = 0; i < text.length; i++) {
            text[i] = (byte) (text[i] ^ key[i % keySize]);
        }
    }
}