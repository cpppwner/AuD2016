package ab6.test;

import org.junit.Assert;
import org.junit.Test;

import ab6.LZW;
import ab6.impl.Eberl.LZWImpl;

public class LZWTest {

	private LZW lzw = new LZWImpl();

	@Test
	public void checkBasicCallsOK() {
		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);
		lzw.setCodetableSize(16);
		lzw.setMode(true);
		lzw.setMode(true);
		lzw.setCodetableSize(16);

		Assert.assertArrayEquals(new byte[] {}, lzw.finish());

		lzw.setCodetableSize(16);
		lzw.setMode(true);
		lzw.setCodetableSize(16);
		lzw.setMode(true);
		lzw.setMode(true);
		lzw.setCodetableSize(16);

		Assert.assertArrayEquals(new byte[] {}, lzw.finish());
	}

	@Test(expected = IllegalStateException.class)
	public void checkBasicCallsNOK() {
		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);

		lzw.data("servus".getBytes());

		lzw.setCodetableSize(16);
	}

	@Test
	public void checkBasicCodingAndDecodingMultipleData() {
		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);

		byte[] input1 = "ser".getBytes();
		byte[] input2 = "vus".getBytes();
		byte[] input3 = "servus".getBytes();
		lzw.data(input3);
		byte[] coded = lzw.finish();

		lzw.data(input1);
		lzw.data(input2);
		byte[] coded2 = lzw.finish();

		//"servus" entspricht "ser" + "vus"
		Assert.assertArrayEquals(coded, coded2);
	}

	@Test
	public void checkBasicCodingAndDecoding16Bit() {
		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);

		byte[] input = "servus".getBytes();
		lzw.data(input);
		byte[] coded = lzw.finish();

		lzw.setMode(false);
		lzw.data(coded);
		byte[] decoded = lzw.finish();

		//Codieren + Decodieren liefert die Ursprungsdaten
		Assert.assertArrayEquals(input, decoded);
	}

	@Test
	public void checkBasicCodingAndDecoding10Bit() {
		lzw.clear();
		lzw.setCodetableSize(10);
		lzw.setMode(true);

		byte[] input = "servus".getBytes();
		lzw.data(input);
		byte[] coded = lzw.finish();

		lzw.setMode(false);
		lzw.data(coded);
		byte[] decoded = lzw.finish();

		//Codieren + Decodieren liefert die Ursprungsdaten
		Assert.assertArrayEquals(input, decoded);
	}

	@Test
	public void checkBasicCodingSizeReduction() {
		byte[] input = "servus miteinand".getBytes();

		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);

		lzw.data(input);
		byte[] coded16 = lzw.finish();

		lzw.setCodetableSize(10);
		lzw.data(input);
		byte[] coded10 = lzw.finish();

		//Die Codierung mit 10 Bit Codebuch liefert kürzere codierte Daten
		Assert.assertEquals(true, coded10.length < coded16.length);
	}

	@Test
	public void checkBasicCodingSizeExact() {

		//Liefert 10 Zeichen in der Ausgabe (97 98 257 99 258 261 97 263 263 100).
		byte[] input = "ababcbababaaaaad".getBytes();

		lzw.clear();
		lzw.setCodetableSize(16);
		lzw.setMode(true);

		lzw.data(input);
		byte[] coded16 = lzw.finish();

		//160 Bit = 20 Bytes (10 Codes zu je 2 Byte = 16 Bit)
		Assert.assertEquals(coded16.length, 10*2);

		lzw.setCodetableSize(10);
		lzw.data(input);
		byte[] coded10 = lzw.finish();

		//100 Bit = 13 Bytes aufgerundet (10 Codes zu je 10 Bit sind 100 Bit -> 13 Byte)
		Assert.assertEquals(coded10.length, 13);
	}


	@Test
	public void checkBasicResetCode() {
		lzw.clear();
		lzw.setCodetableSize(9);
		lzw.setMode(true);


		StringBuilder sb = new StringBuilder();
		String input = "ababcbababaaaaad";

		//Wiederholt zusammenfügen, damit die Inputdaten so lang sind, dass das Reset-Kommando 256 nötig wird
		for(int i = 0; i < 2000; i++)
			sb.append(input);

		String inputString = sb.toString();

		lzw.data(inputString.getBytes());

		byte[] coded = lzw.finish();

		lzw.setMode(false);
		lzw.data(coded);
		byte[] decoded = lzw.finish();

		String decodedString = new String(decoded);

		//Teste den resultierenden String
		Assert.assertEquals(inputString, decodedString);
	}
}