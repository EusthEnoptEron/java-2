package usr.eusth.xml;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 10.06.13
 * Time: 08:21
 * To change this template use File | Settings | File Templates.
 */
public class Example3 {
	public static void main(String[] args) {
		ArrayList<Student> students = new ArrayList<Student>(1000);
		for(int i = 0; i<  100; i++) {
			HashMap<String, Integer> map = new HashMap<>();
			map.put("English", Math.round((float)Math.random() * 7) );
			students.add(new Student("Student #" + i, map));
		}

		try(
			FileOutputStream output = new FileOutputStream("school2.xml");
		) {
			Factory.saveInstance(output, new School("BFH", students));
			Factory.saveSchema(new File("."), School.class);

		} catch (JAXBException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		try (
				FileInputStream input = new FileInputStream("school2.xml");
		) {
			School school = (School)Factory.loadInstance(input, School.class);
			System.out.println(school.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (JAXBException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
