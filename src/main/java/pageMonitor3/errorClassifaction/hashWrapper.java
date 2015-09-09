package pageMonitor3.errorClassifaction;

import java.nio.ByteBuffer;

public class hashWrapper {

	public final byte[] hash;
	
	public hashWrapper(byte[] hash_){
		hash = hash_;
	}
	
	@Override
	public boolean equals(Object obj){
		return (this.hashCode() == ((hashWrapper)obj).hashCode());
	}
	
	@Override
	public int hashCode(){
		ByteBuffer buffer = ByteBuffer.wrap(hash);
		int returnVal = buffer.getInt();
		return returnVal;
	}
	
	
}
