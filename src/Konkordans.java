import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Konkordans {

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
	
	private static RandomAccessFile offsetFile;
	private static RandomAccessFile hashFile;
	private static RandomAccessFile wordFile;
	private static RandomAccessFile korpusFile;
	
	public static void main(String[] args) throws IOException {
		
		String word = args[0];
		
		String korpusPath = System.getProperty("user.dir") + "/tokenizing/testfil";
			
		offsetFile = new RandomAccessFile("offsetIndex", "r");
		hashFile = new RandomAccessFile("hashKey", "r");
		wordFile = new RandomAccessFile("wordIndex", "r");
		korpusFile = new RandomAccessFile(korpusPath, "r");

		int offsetIndex = -1;
		int[] wordList = {};

		int wordIndex = SBHI(word);

		if(wordIndex != -1)
			 offsetIndex = SBWI(word, wordIndex);
		
		if(offsetIndex!=-1)
			 wordList = SBOS(offsetIndex);		
		
		System.out.println("Det finns " + wordList.length + " förekommelser av ordet");
		if(wordList.length>25){
			Scanner sc = new Scanner(System.in);
			System.out.print("Vill du ha förekomsterna utskrivna? (y/n):\t");
			String alternativ = sc.nextLine();
			if(alternativ.equals("n")){
				System.exit(1);	
			}
		}
		
		if(wordList.length>0)	
			PBK(wordList, word);
	}
	
	public static int[] SBOS(int offset) throws IOException{
		offsetFile.seek(offset*4);
		int size = offsetFile.readInt();
		int[] retlist = new int[size];
		for(int i = 0; i<size; i++) {
			retlist[i] = offsetFile.readInt();
		}
		return retlist;
	}
	
	public static void PBK(int [] offsets, String word) throws IOException {
 	StringBuilder sb;
 	for(int i : offsets) {
 		sb = new StringBuilder();
 		char c;
 		int end;
 		if(!(i<30))
 			i -=30;
 		korpusFile.seek(i);
 		for(int j = 0; j<60+word.length(); j++) {
 				end = korpusFile.read();
 				if (end == -1)
 					break;
 				c = (char) end;
 				sb.append( c=='\n' ? ' ' : c );
 		}
 		System.out.println(sb.toString());
 	}
 }
	
	public static int SBHI(String word) throws IOException {
		hashFile.seek(0);
		String subWord = "";
		String readWord = "";
		int retInt = 0;
		for (int i = 0; i<word.length(); i++) {
			subWord += word.charAt(i);
			if(i == 2)
				break;
		}
		while (true) {
		
				readWord = getWord(hashFile);
				if(	readWord.equals("-1"))
					return Integer.parseInt(readWord);
			
			if(readWord.equals(subWord)) {
				retInt = Integer.parseInt(getWord(hashFile));
				return retInt;
			}
			readWord =getWord(hashFile);
		}
	}
	
	private static String getSubString(String word){
		// Control for StringIndexOutOfBoundsException:
		switch(word.length()){
			case 1 : return word.substring(0, 1);
			case 2 : return word.substring(0, 2);
			default : return word.substring(0, 3);
		}
	}
	
	public static int SBWI(String lookingFor, int position) throws IOException{ //
		
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

	
	private static String getWord(RandomAccessFile file) throws IOException {
		StringBuilder stb = new StringBuilder();
		String word;
		char c;
		c = (char)file.read();
		int end;
		do {
			stb.append(c);
			//System.out.print(c);
			end = file.read();
			if(end==-1)
				return "-1";
			c = (char)end;
			
		} while (c != ' ');
		//System.out.println();
		
		word = stb.toString();
		word.trim();
		return word;
	}

	
}
