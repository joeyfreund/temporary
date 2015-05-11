package com.xnlogic.pacer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Pipe;
import java.nio.channels.SocketChannel;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Channels {

	
	
	
	
	public static class Looper {

		//		Pipe pipe;

		ByteBuffer buffer;
		byte[] b;
		SocketChannel c;

		//		public Looper(Pipe pipe, String start) throws IOException {
		//			this.pipe = pipe;
		//			this.buffer = ByteBuffer.allocate(48);
		//			this.b = new byte[48];
		//			this.write(start);
		//		}

		public Looper(String start) throws IOException {
			this.buffer = ByteBuffer.allocate(48);
			this.b = new byte[48];
			this.c = SocketChannel.open();
			this.write(start);
		}



		public void loop(int n) throws IOException{
			String s = next();
			int i = 0;

			while(s != null && i < n){
				System.out.println(s);
				write(i + s);
			}
		}

		private String next() throws IOException{
			int bytesReads = c.read(buffer); 
			if(bytesReads < 0){
				return null;
			} else {
				buffer.get(b, 0, bytesReads);
				return new String(b);
			}
		}

		private void write(String item) throws IOException{
			buffer.clear();
			buffer.put(item.getBytes());
			buffer.flip();

			while(buffer.hasRemaining()) {
				c.write(buffer);
			}
		}


		//		public void loop(int n) throws IOException{
		//			int bytesRead = pipe.source().read(buffer);
		//			int i = 0;
		//			while(bytesRead > -1 && i < n){
		//				buffer.get(b, 0, bytesRead);
		//				String msg = new String(b);
		//				System.out.println("(" + bytesRead + " bytes long): '" + msg + "'");
		//				this.write(i + "");
		//				
		//				i++;
		////				buffer.clear();
		//				bytesRead = pipe.source().read(buffer);
		//			}
		//			pipe.source().close();
		//			pipe.sink().close();
		//		}


		//		private void write(String item) throws IOException{
		////			buffer.clear();
		//			buffer.put(item.getBytes());
		//			buffer.flip();
		//	
		//			while(buffer.hasRemaining()) {
		//				pipe.sink().write(buffer);
		//			}
		//		}
	}

	public static void main(String[] args) throws IOException {

		Channel inChannel = SocketChannel.open();

		//		RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
		//	    FileChannel inChannel = aFile.getChannel();

		//	    ByteBuffer buf = ByteBuffer.allocate(48);
		//
		//	    int bytesRead = inChannel.read(buf);
		//	    while (bytesRead != -1) {
		//
		//	      System.out.println("Read " + bytesRead);
		//	      buf.flip();
		//
		//	      while(buf.hasRemaining()){
		//	          System.out.print((char) buf.get());
		//	      }
		//
		//	      buf.clear();
		//	      bytesRead = inChannel.read(buf);
		//	    }
		//	    aFile.close();


		Pipe pipe = Pipe.open();

		Looper looper = new Looper("A");
		looper.loop(5);







		//		SinkChannel sinkChannel = pipe.sink();
		//		String newData = "New String to write to file..." + System.currentTimeMillis();
		//
		//		ByteBuffer buf = ByteBuffer.allocate(48);
		//		buf.clear();
		//		buf.put(newData.getBytes());
		//
		//		buf.flip();
		//
		//		while(buf.hasRemaining()) {
		//			sinkChannel.write(buf);
		//		}
		//	    
		//	    
		//		SourceChannel pipeOut = pipe.source();
		//		ByteBuffer buf2 = ByteBuffer.allocate(48);
		//		int bytesRead = pipeOut.read(buf2);
		//		
		//		System.out.println("Read " + bytesRead + " bytes.");
		//		System.out.println(new String(buf2.array()));

	}

}
