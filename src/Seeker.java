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
	
	public void wordTest() throws IOException{ //
		/* test to seek on the spot "des" points at to search for "destruction"
		 * meaning we skip the word desire
		*/
		wordFile.seek(19);
		String hash = "des";
		String lookingFor = "destruction";
		String word = "";
		String number = "";
		while(!(word.equals(lookingFor))){ 
			System.out.println("1) Word is :" + word + ":" + lookingFor + ":");
			word = getWord(wordFile);
			System.out.println("2) Word is :" + word + ":" + lookingFor + ":");
			String wordSub = word.substring(0, 3);
			if(!(hash.equals(wordSub))){
				System.out.println("error");
				System.out.println("Word is " + word);
				System.out.println("hash is \"" + hash + "\"");
				System.out.println("wordSub is \"" + wordSub + "\"");
			}
			// eat up number:
			number = getWord(wordFile);
		}
		System.out.println("Word is now " + word);
		System.out.println("Position is " + number);
		
		
	}
	
	private int getPos(RandomAccessFile file) throws IOException {
		return (int) file.getFilePointer();
	}
	
	private String getWord(RandomAccessFile file) throws IOException {
		StringBuilder stb = new StringBuilder();
		String word;
		char c;
		c = (char)file.read();
		do {
			stb.append(c);
			c = (char)file.read();
		} while (c != ' ');
		
		word = stb.toString();
		word.trim();
		return word;
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
