import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SeekerTest {

	private RandomAccessFile offsetFile;
	private RandomAccessFile hashFile;
	private RandomAccessFile wordFile;
	private RandomAccessFile korpusFile;

    /**
     * Sets up the test fixture. 
     * (Called before every test case method.)
     * @throws FileNotFoundException 
     */
    @Before
    public void setUp() throws FileNotFoundException {
		offsetFile = new RandomAccessFile("offsetIndex", "r");
		hashFile = new RandomAccessFile("hashKey", "r");
		wordFile = new RandomAccessFile("wordIndex", "r");
    }
    
    @Test
    public void testSBWI() {
        assertEquals("Empty list should have 0 elements", 8, Seeker.SBWI("destruction", 19));
    }

}
