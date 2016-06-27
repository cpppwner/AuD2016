package ab3;

import ab3.impl.Eberl.AuDHashMapImpl;

public class HashTabelleSample {
	public static void main(String[] args)
	{
		//Erzeugt eine Hashtabelle, welche Integer-Schlüssel verwendet um Strings abzuspeichern. Es ist aber möglich, auch weitere Typen anzugeben.
		//Achtung: Die Implementierung ist leer und funktioniert nicht!
		AuDHashMap<Integer, String> hashMap = new AuDHashMapImpl<Integer,String>();
		
		hashMap.put(2, "Das ist ein String");
		hashMap.put(1, "Das ist ein anderer String");
		hashMap.put(102334, "Und ein dritter noch");
	}
}
