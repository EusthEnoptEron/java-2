package usr.eusth.xml;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 06.06.13
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class Example2 {
	public static void main(String[] args) {
		try {
			Factory.saveSchema(new File("."), School.class);
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (JAXBException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
