package usr.eusth.xml;


import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://eusthenopteron/school", name="school")
public class School {
	private String name;

	@XmlElementWrapper(name = "students")
	@XmlElement(name = "student")
	private ArrayList<Student> students = new ArrayList<Student>();

	public School(String name, ArrayList<Student> students) {
		this.name = name;
		this.students = students;
	}

	@SuppressWarnings("unused")
	private School() {}

	@XmlRootElement(namespace = "http://eusthenopteron/school", name="school")
	public static class SchoolNameFirst  {
		public String name;
	}

	@Override
	public String toString() {
		return "School{" +
				"name='" + name + '\'' +
				", students=" + students +
				'}';
	}



	public static void main(String[] args) {
		ArrayList<Student> students = new ArrayList<Student>(1000);
		for(int i = 0; i<  100; i++) {
			students.add(new Student("Student #" + i));
		}

		try(
			FileOutputStream output = new FileOutputStream("school.xml");
		) {
			Factory.saveInstance(output, new School("BFH", students));
		} catch (JAXBException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		try (
			FileInputStream input = new FileInputStream("school.xml");
		) {
			SchoolNameFirst school = (SchoolNameFirst)Factory.loadInstance(input, SchoolNameFirst.class);
			SchoolList schools = new SchoolList();
			schools.schools.add(school);
			Factory.saveInstance(System.out, schools);
		} catch (FileNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (JAXBException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
