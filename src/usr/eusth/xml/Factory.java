package usr.eusth.xml;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Factory for XML/Java conversion
 */
public class Factory {
	public static void saveInstance(OutputStream out, Object instance)
			throws JAXBException, IOException {
		Marshaller marshaller = JAXBContext.newInstance(instance.getClass())
							.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(instance, out);
		out.flush();
	}

	public static Object loadInstance(InputStream inputStream , Class instanceClass)
			throws JAXBException {
		Unmarshaller unmarshaller = JAXBContext.newInstance(instanceClass)
								.createUnmarshaller();
		return unmarshaller.unmarshal(inputStream);
	}

	static class LocalFileSchemaResolver extends SchemaOutputResolver {
		private  File baseDir;

		LocalFileSchemaResolver(File baseDir) {
			this.baseDir = baseDir;
		}

		@Override
		public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
			return new StreamResult(new File(baseDir, suggestedFileName));
		}
	}

	public static void saveSchema(File baseDir, Class... classes) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance(classes);
		context.generateSchema(new LocalFileSchemaResolver(baseDir));
	}
}
