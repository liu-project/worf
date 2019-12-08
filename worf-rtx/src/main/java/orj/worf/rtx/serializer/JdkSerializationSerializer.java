package orj.worf.rtx.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JdkSerializationSerializer<T> implements ObjectSerializer<T> {

	@Override
	public byte[] serialize(T object) {
        if (object == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream ex = null;
            try {
                ex = new ObjectOutputStream(baos);
                ex.writeObject(object);
                ex.flush();
                return baos.toByteArray();
            } catch (IOException var3) {
                throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), var3);
            } finally {
            	if(baos != null) {
            		try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            		baos = null;
            	}
            	
            	if(ex != null) {
            		try {
						ex.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            		ex = null;
            	}
            }
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) {

        if (bytes == null) {
            return null;
        } else {
        	ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        	ObjectInputStream ex = null;
            try {
                ex = new ObjectInputStream(inputStream);
                return (T) ex.readObject();
            } catch (IOException var2) {
                throw new IllegalArgumentException("Failed to deserialize object", var2);
            } catch (ClassNotFoundException var3) {
                throw new IllegalStateException("Failed to deserialize object type", var3);
            } finally {
            	if(inputStream != null) {
            		try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            	inputStream = null;
            	
            	if(ex != null) {
            		try {
						ex.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            	ex = null;
            }
        }
    
	}

}
