package usr.eusth.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://ch.fbi.xml.beispielEins", name = "time_unit")
public class TimeUnit {
	private String unitLabel = "s";
	private double factorToSIUnit = 1.0;

	@XmlElement(name = " time_unit_map ")
	private HashMap<TimeUnit, Boolean> timeUnitMap;

	public String getUnitLabel() {
		return unitLabel;
	}

	public void setUnitLabel(String unitLabel) {
		this.unitLabel = unitLabel;
	}

	public double getFactorToSIUnit() {
		return factorToSIUnit;
	}

	public void setFactorToSIUnit(double factorToSIUnit) {
		this.factorToSIUnit = factorToSIUnit;
	}

	public TimeUnit() {
		timeUnitMap =new HashMap<>();
		timeUnitMap.put(new TimeUnit(), true);
	}
}


