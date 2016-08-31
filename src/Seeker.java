import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Seeker {

	/*// declare streams from offsetIndex
	private FileInputStream o_fis;
	private BufferedInputStream o_bos;
	private DataInputStream offsetStream;
	
	// declare stream from hashIndex
	private FileInputStream h_fis;				// Will read from hashIndex 
	private BufferedInputStream h_bis;			// Will wrap h_fis
	private Kattio hashReader;					// will wrap h_bis
	
	// declare stream from wordIndex
	private FileInputStream w_fis;				// Will read from wordIndex 
	private BufferedInputStream w_bis;			// Will wrap w_fis
	private Kattio wordReader;					// will wrap w_bis
	
	// declare stream from korpus
	private FileInputStream k_fis;				// Will read from korpus 
	private BufferedInputStream k_bis;			// Will wrap k_fis
	private Kattio korpusReader;				// will wrap k_bis
	*/
	
	private RandomAccessFile offsetFile;
	private RandomAccessFile hashFile;
	private RandomAccessFile wordFile;
	private RandomAccessFile korpusFile;
	
	public Seeker(String hashKey, String wordIndex, String offsetIndex, String korpus) throws IOException {
		
		offsetFile = new RandomAccessFile(offsetIndex, "r");
		hashFile = new RandomAccessFile(hashKey, "r");
		wordFile = new RandomAccessFile(wordIndex, "r");
		//korpusFile = new RandomAccessFile(korpus, "r");
		
	}
	
	public void offsetTest() throws IOException{
		for(int i = 0; i < 10; i++){
			int wahabi = offsetFile.readInt();
			System.out.println(wahabi);			
		}
	}
	
	public void wordTest() throws IOException{
		char wahabi = (char)wordFile.read();
		System.out.print(wahabi);
		while(wahabi != ' '){
			wahabi = (char)wordFile.read();
			System.out.print(wahabi +"i");	
		}
		System.out.println();
		String wahabi2 = wordFile.readUTF();
		System.out.println(wahabi2+"i");			
	}
	
	public void hashTest() throws IOException{
		for(int i = 0; i < 10; i++){
			int wahabi = hashFile.readInt();
			System.out.println(wahabi);			
		}
	}
	
	/*public void korpusTest() throws IOException{
		for(int i = 0; i < 10; i++){
			int wahabi = korpusFile.readInt();
			System.out.println(wahabi);			
		}
	}*/

	
}
