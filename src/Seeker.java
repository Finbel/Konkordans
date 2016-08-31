import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

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
	
	public void konkord(String word) throws IOException{

		int offsetIndex = 0;
		int[] wordList;

		int wordIndex = SBHI(word);

		if(wordIndex != -1)
			 offsetIndex = SBWI(word, wordIndex);
		
		if(offsetIndex!=-1)
			 wordList = SBOS(offsetIndex);		
		
		//if(wordList.length>0)	
			//PBK(wordList);
	}
	
	private int[] SBOS(int offset) throws IOException{
		offsetFile.seek(offset);
		int size = offsetFile.readInt();
		int[] retlist = new int[size];
		for(int i = 0; i<size; i++) {
			retlist[i] = offsetFile.readInt();
		}
		return retlist;
	}
	
	private void PBK(int [] offsets) throws IOException {
 	StringBuilder sb;
 	for(int i : offsets) {
 		sb = new StringBuilder();
 		if(!(i<30))
 			i -=30;
 		korpusFile.seek(i);
 		for(int j = 0; j<30; j++) {
 			try {
 				sb.append((char)korpusFile.read());
 			}
 			catch(EOFException e) {break;}
 		}
 		System.out.println(sb.toString());
 	}
 }
	
	private int SBHI(String word) throws IOException {
		String subWord = "";
		String readWord;
		int retInt;
		for (int i = 0; i<word.length(); i++) {
			subWord += word.charAt(i);
			if(i == 2)
				break;
		}
		while (true) {
			try {
				readWord = getWord(hashFile);
			} catch(EOFException ex) {
				retInt = -1;
				return retInt;
			}
			if(readWord.equals(subWord)) {
				retInt = Integer.parseInt(getWord(hashFile));
				return retInt;
			}
			readWord =getWord(hashFile);
		}
	}
	
	private String getSubString(String word){
		// Control for StringIndexOutOfBoundsException:
		switch(word.length()){
			case 1 : return word.substring(0, 1);
			case 2 : return word.substring(0, 2);
			default : return word.substring(0, 3);
		}
	}
	
	public int SBWI(String lookingFor, int position) throws IOException{ //
		
		wordFile.seek(position);
		String hash = getSubString(lookingFor);
		
		String word = "";
		String number = "";
		String wordSub;
		
		while(!(word.equals(lookingFor))){ 
	
			word = getWord(wordFile);
			wordSub = getSubString(word);
	
			if(!(hash.equals(wordSub))){
				System.out.println("error, word does not exsist");
				return -1;
			}
			
			// eat up number:
			number = getWord(wordFile);

		}
		
		return Integer.parseInt(number);
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
