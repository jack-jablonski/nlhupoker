package se.hupoker.lut;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author Alexander Nyberg
 */
public abstract class LutTable {
    private final boolean DEBUG = false;
    protected static final int BADENTRY = -1;

    /*
     * The actual Look-Up-Table
     */
    private final float lut[];

    protected LutTable(int tableSize) {
        lut = new float[tableSize];
        Arrays.fill(lut, BADENTRY);
    }

    public float lookupOne(LutKey lutKey) {
        int index = getIndex(lutKey);
        return lut[index];
    }

    public void setManually(LutKey lutKey, float value) {
        int index = getIndex(lutKey);
        lut[index] = value;
    }

    /**
     * @return Internal index into LUT.
     */
    abstract protected int getIndex(LutKey in);

    private int getTableSize() {
        return lut.length * 4;
    }

    public void save(String path) {
        try {
            FileChannel fc = new FileOutputStream(path).getChannel();
            ByteBuffer buff = ByteBuffer.allocate(getTableSize());
            buff.asFloatBuffer().put(lut);
            fc.write(buff);
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load(String path) {
        try {
            FileChannel fc = new FileInputStream(path).getChannel();
            ByteBuffer buff = ByteBuffer.allocate(getTableSize());
            fc.read(buff);
            buff.flip();
            for (int i = 0; i < lut.length; i++) {
                lut[i] = buff.getFloat();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Successfully loaded " + path);

        return true;
    }

    protected void debug(String str) {
        if (DEBUG) {
            System.out.println(str);
        }
    }
}