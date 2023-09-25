import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WhzGeneratorTest {

	public static void main(String[] args) throws Exception {
		List<String> warnings = new ArrayList<String>();

		ConfigurationParser parser = new ConfigurationParser(warnings);
		// Configuration config = parser.parseConfiguration(new File("budgetGeneratorConfig.xml"));
		Configuration config = parser.parseConfiguration(new File("whzTestGeneratorConfig.xml"));

		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), warnings);
		myBatisGenerator.generate(new ProgressCallbackImpl());
		System.out.println("告警信息");
		for (String warning : warnings) {
			System.out.println(warning);
		}
	}

	public static class ProgressCallbackImpl implements ProgressCallback {

		@Override
		public void introspectionStarted(int totalTasks) {
			System.out.println("1 introspectionStarted: " + totalTasks);
		}

		@Override
		public void generationStarted(int totalTasks) {
			System.out.println("2 generationStarted: " + totalTasks);
		}

		@Override
		public void saveStarted(int totalTasks) {
			System.out.println("3 saveStarted: " + totalTasks);
		}

		@Override
		public void startTask(String taskName) {
			System.out.println("4 startTask: " + taskName);
		}

		@Override
		public void done() {
			System.out.println("5 done ");
		}

		@Override
		public void checkCancel() throws InterruptedException {

		}
	}

}
