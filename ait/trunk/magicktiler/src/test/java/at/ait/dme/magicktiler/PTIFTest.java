package at.ait.dme.magicktiler;

import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

public class PTIFTest extends TestCase {

	@Test 
	public void testTMSTiling() throws TilingException {		
		// Generate a Pyramid TIF from the test imag
		File result = new File("target/pyramid-tif.ptif");
		
		PTIFConverter ptiffer = new PTIFConverter();
		TilesetInfo info = ptiffer.convert(
				new File("src/test/resources/Hong_Kong_Night_Skyline.jpg"),
				result
		);
		
		// Check if image metadata was read correctly
		assertEquals("Wrong width calculated for the Zoomify tileset!", 4670, info.getWidth());
		assertEquals("Wrong height calculated for the Zoomify tileset!", 2000, info.getHeight());
		
		// Check if tileset properties were computed correctly
		assertEquals("Wrong number of zoom levels calculated for the Zoomify tileset!", 6, info.getZoomLevels());
		
		// Check if the file was created
		assertTrue(result.exists());
	}

}
