import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class IndexKonstruktör {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		konstruera();
	}

	private static void konstruera() {
		
		
		// Creating and initializing path-variables
		String rawIndexPath = System.getProperty("user.dir") + "/tokenizing/ut";	// our rawIndex /tokenizing/ut
		String wordIndexPath = "wordIndex";		//
		String offsetIndexPath = "offsetIndex"; //
		String hashKeyPath = "hashKey";
		
		
		// Creating necessary streams and writers  
		// Input from rawIndex
		FileInputStream fis;				// Will read from rawIndex 
		//BufferedInputStream bis;			// Will wrap fis
		//Kattio rawIndex;					// will wrap bis
		InputStreamReader isr;				//TODO: replace kattio
		BufferedReader rawIndex; 			//TODO: replace kattio
		
		// Output to wordIndex
		FileWriter fwOne;					// Will write to wordIndex
		BufferedWriter wordIndex;			// Will wrap fwOne
		// ska vi wrap wordIndex med kattio?
		
		// Output to offsetIndex
		FileOutputStream fos;				// Will stream to offsetIndex
		BufferedOutputStream bos;			// Will wrap fos
		DataOutputStream offsetIndex;		// Will wrap bos
		// ska vi wrap offsetIndex med kattio?
		
		// Output to hashKey
		FileWriter fwTwo;					// Will write to hashKey
		BufferedWriter hashKey;				// Will wrap fwTwo
		// Ska vi wrap hashKey med kattio?
		
		// Creating and initializing necessary variables
		
		String oldWord = "";				// word checked during last loop
		String newWord = "";				// word checked during current loop
		
		// Each word will point to a position in offsetIndex
		// where we will find their positions in the corpus
		int offsetPosition = 0;	

		// Each 3-letter hash will point to a position in wordIndex
		// where we will find the words beginning with the hash
		int hashPosition = 0;
		
		// The current 3-letter substring, changes when we come to a new one.
		String oldSubstring = "";
		// We will need a control number for the special cases when the word length is less than 3
		String newSubstring = "";
		
		// A list that will hold the positions the current word has been 
		// encountered on throughout the corpus
		ArrayList<Integer> textPositions = new ArrayList<>(); 
		
		// Will construct the strings sent to wordIndex
		StringBuilder wordBuilder = new StringBuilder();
		// Will construct the strings sent to hashKey
		StringBuilder hashBuilder = new StringBuilder();
		
		try{
			
			// Initialize the streams and writers 
			fis = new FileInputStream(rawIndexPath);
			isr = new InputStreamReader(fis, "ISO-8859-1");
			rawIndex = new BufferedReader(isr);
			
			fwOne = new FileWriter(wordIndexPath);
			wordIndex = new BufferedWriter(fwOne);
			
			fos = new FileOutputStream(offsetIndexPath);
			bos = new BufferedOutputStream(fos);
			offsetIndex = new DataOutputStream(bos);
			
			fwTwo = new FileWriter(hashKeyPath);
			hashKey = new BufferedWriter(fwTwo);
			
			String line = rawIndex.readLine();
			StringTokenizer st; 
			// Start loop-through of rawIndex
			while(line!=null){
				st= new StringTokenizer(line);
				//Start loop-through line
				while(st.hasMoreTokens()){
				// Load next word into newWord 
				// assuming the text will alternate between word and respective place
				newWord = st.nextToken();
				
				// Check if we have reached a new word
				if(!(oldWord.equals(newWord)) && !(oldWord.equals(""))){ //optimize
					
					// append the last word and it's offset position to the list of words
					wordBuilder.append(oldWord + " " + offsetPosition + " ");
					
					// write the number of positions we have of last word to the offsetIndex
					offsetIndex.writeInt(textPositions.size());
					
					// write all of the positions to the offsetIndex
					for(int offset : textPositions){
						offsetIndex.writeInt(offset);
					}
					
					// increase the offset position (measured in bytes)
					offsetPosition += (textPositions.size() + 1);
					
					// clear the textPositions (make room for new word)
					textPositions.clear();
				}
				
				// Control for StringIndexOutOfBoundsException:
				switch(newWord.length()){
					case 1 : newSubstring = newWord.substring(0, 1);
						break;
					case 2 : newSubstring = newWord.substring(0, 2);
						break;
					default : newSubstring = newWord.substring(0, 3);
				}
					
				// Check if the new word also has a new first-3-letter substring
				if(!(oldSubstring.equals(newSubstring)) && !(oldWord.equals(""))){ //optimize
					
					// Add to the hash
					hashBuilder.append(oldSubstring + " " + hashPosition + " ");
					oldSubstring = newSubstring;
					
					// Write to the hashKey file
					hashKey.write(hashBuilder.toString());
					
					// Empty the hashBuilder
					hashBuilder.setLength(0);
					
					// Update current HashPosition
					hashPosition += wordBuilder.length();
					
					// Write all words on last substring to file
					wordIndex.write(wordBuilder.toString());
					
					// Empty the wordBuilder
					wordBuilder.setLength(0);
				}

				// Do this for all words:
				
				// Add the associated position to our list of text positions
				textPositions.add(Integer.parseInt(st.nextToken()));
				// We are now done with the current word
				oldWord = newWord;
				oldSubstring = newSubstring;
			}
				line = rawIndex.readLine();
			}
			
			// The loop is done when the rawIndex is empty
			
			// Take care of the last word and then close the streams
			
			// append it's offset position to the list of words
			wordBuilder.append(oldWord + " " + offsetPosition + " ");
			// Write all words on last substring to file
			wordIndex.write(wordBuilder.toString());
			
			// append to hashKey file
			hashBuilder.append(oldSubstring + " " + hashPosition + " ");
			// Write to the hashKey file
			hashKey.write(hashBuilder.toString());
			
			// write the number of positions we have of it to the offsetIndex
			offsetIndex.writeInt(textPositions.size());
			
			// write all of it's positions to the offsetIndex
			for(int offset : textPositions){
				offsetIndex.writeInt(offset);
			}
			
			// Done and dusted.
			//rawIndex.close();
			wordIndex.close();
			offsetIndex.close();
			hashKey.close();

		} catch( Exception e ){
			e.printStackTrace();
		}		
	}

}
