package se.hupoker.common;

import java.io.*;

/**
 * @author Alexander Nyberg
 */
public class Serializer {
    public void serialize(Serializable object, String fileLocation) {
        try {
            OutputStream file = new FileOutputStream(fileLocation);
            BufferedOutputStream buffer = new BufferedOutputStream(file);

            try (ObjectOutput output = new ObjectOutputStream(buffer)) {
                output.writeObject(object);
            }

            buffer.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read from disk");
        }
    }

    public <T> T deserialize(Class<T> clazz, String fileLocation) {
        try{
            InputStream file = new FileInputStream(fileLocation);
            BufferedInputStream buffer = new BufferedInputStream( file );
            try (ObjectInput input = new ObjectInputStream(buffer)) {
                return clazz.cast(input.readObject());
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException("Could not read from disk");
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Could not read from disk");
        }
    }
}