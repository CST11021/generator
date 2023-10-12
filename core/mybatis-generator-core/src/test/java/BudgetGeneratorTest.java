import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 盖伦
 * @Date 2023/10/12
 */
public class BudgetGeneratorTest {

    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();

        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration config = parser.parseConfiguration(new File("budgetGeneratorConfig.xml"));

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), warnings);
        myBatisGenerator.generate(new VerboseProgressCallback());
        System.out.println("告警信息");
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }

}
