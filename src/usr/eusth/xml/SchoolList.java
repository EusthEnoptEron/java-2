package usr.eusth.xml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(namespace = "http://eusthenopteron/school", name="schools")
public class SchoolList {
	public ArrayList<School.SchoolNameFirst> schools = new ArrayList<>();
}
