package usr.eusth.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 03.06.13
 * Time: 09:18
 * To change this template use File | Settings | File Templates.
 */
public class Example1 {
	public static void main(String[] args) {
		byte[] array;
//		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			FileOutputStream os = new FileOutputStream("test.xml");
			TimeUnit unit = new TimeUnit();
			Factory.saveInstance(os, unit);
			os.close();
//
//			array = os.toByteArray();
//
//			Scanner s = new Scanner(new ByteArrayInputStream(array));
//			while(s.hasNextLine()) {
//				System.out.println(s.nextLine());
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
