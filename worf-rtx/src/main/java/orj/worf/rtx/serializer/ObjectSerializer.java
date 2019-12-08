package orj.worf.rtx.serializer;

public interface ObjectSerializer<T> {

	/**
	 * 序列化
	 * @param t
	 * @return
	 */
	byte[] serialize(T t);
	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	T deserialize(byte[] bytes);
}
