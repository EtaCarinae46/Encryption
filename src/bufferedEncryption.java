import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;


class bufferedEncryption implements Runnable {


    private ProgressBar pBar;
    private List<File> fileList;
    private File keyF;
    private byte[] key;
    private TextArea Log;
    private int buffer = 153600;

    bufferedEncryption(List<File> f, String k, File k2, ProgressBar pB, TextArea l) {
        pBar = pB;
        fileList = f;
        key = k.getBytes();
        keyF = k2;
        Log = l;
    }


    @Override
    public void run() {
        try {
            for (File f: fileList) {
                if (!f.exists()) continue;
                if (f.length() == 0) continue;

                Log.setText(Log.getText() + "[ Encryption started ]\n");
                long Start = System.currentTimeMillis();
                
                if (keyF != null && keyF.exists()) {
                    keyAsFile(f);
                } else if (key != "".getBytes()) {
                    keyAsString(f);
                }

                Log.setText(Log.getText() + "[ Done in " + (System.currentTimeMillis()-Start)/1000.0 + "s ] \"" + f.getPath() + "\"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void keyAsString(File file) throws Exception {
        RandomAccessFile inFile = new RandomAccessFile(file, "rw");

        int neededLength = (int)Math.min(file.length(),buffer);

        byte[] text = new byte[neededLength];
        int readedBytes;
        int keyPos = 0;
        long fp;
        int to = (int)Math.ceil(file.length()/(double)buffer);
        pBar.setVisible(true);
        for (int i = 0; i < to; i++) {
            fp = inFile.getFilePointer();
            readedBytes = inFile.read(text, 0, neededLength);
            keyPos = encrypt(text, key, key.length, keyPos);
            inFile.seek(fp);
            inFile.write(text, 0, readedBytes);
            pBar.setProgress(i/(double)to);
        }
        inFile.close();
        pBar.setVisible(false);
        pBar.setProgress(0.0);
    }

    private void keyAsFile(File file) throws Exception {
        RandomAccessFile inFile = new RandomAccessFile(file, "rw");
        RandomAccessFile inKey = new RandomAccessFile(keyF, "r");

        int rTextSize = (int)Math.min(file.length(),buffer);
        int rKeySize = (int)Math.min(keyF.length(),buffer);

        byte[] text = new byte[rTextSize];
        byte[] tempKey = new byte[rKeySize];
        int readedBytes;
        int readedKey;
        int keyPos = 0;
        long fileFP;
        int to = (int)Math.ceil(file.length()/(double)buffer);
        pBar.setVisible(true);
        for (int i = 0; i < to; i++) {

            fileFP = inFile.getFilePointer();
            inKey.seek(inKey.getFilePointer()%keyF.length());
            readedBytes = inFile.read(text, 0, rTextSize);
            readedKey = inKey.read(tempKey, 0, rKeySize);
            keyPos = encrypt(text, tempKey, readedKey, keyPos);
            inFile.seek(fileFP);
            inFile.write(text, 0, readedBytes);
            pBar.setProgress(i/(double)to);
        }
        inFile.close();
        pBar.setVisible(false);
        pBar.setProgress(0.0);
    }

    private int encrypt(byte[] text, byte[] key, int keySize, int off) {
        int keyPos = off;
        for (int i = 0; i < text.length; i++) {
            text[i] = (byte) (text[i] ^ key[keyPos]);
            keyPos = (keyPos+1) % keySize;
        }
        return keyPos;
    }
}
