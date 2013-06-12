package usr.eusth.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(namespace = "http://eusthenopteron/school", name="student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {

	private String name;
	@XmlJavaTypeAdapter(MapAdapterAsArray.class)
	private HashMap<String, Integer> grades = new HashMap<>();

	public Student(String name, HashMap<String, Integer> grades) {
		this.name = name;
		this.grades = grades;
	}
	public Student(String name) {
		this.name = name;
	}



	@SuppressWarnings("unused")
	private Student() {}
}


class MapAdapterAsArray extends
		XmlAdapter<GradeMapElement[], Map<String, Integer>> {

	@Override
	public GradeMapElement[] marshal(Map<String, Integer> map)
			throws Exception {
		GradeMapElement[] mapElements = new GradeMapElement[map.size()];
		int i = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet())
			mapElements[i++] = new GradeMapElement(entry.getKey(),
					entry.getValue());
		return mapElements;
	}

	@Override
	public Map<String, Integer> unmarshal(
			GradeMapElement[] timeUnitMapTuples) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (GradeMapElement mapelement : timeUnitMapTuples)
			map.put(mapelement.subject, mapelement.grade);
		return map;
	}
}


class GradeMapElement {
	@XmlElement(name = "subject", required = true)
	public String subject;
	@XmlElement(name = "grade")
	public Integer grade;

	@SuppressWarnings(" unused ")
	private GradeMapElement() {
		// Required by JAXB
	}

	GradeMapElement(String subject, Integer grade) {
		this.subject = subject;
		this.grade = grade;
	}
}
