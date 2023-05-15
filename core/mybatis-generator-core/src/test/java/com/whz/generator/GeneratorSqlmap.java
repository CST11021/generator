package com.whz.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorSqlmap {

	public static void main(String[] args) throws Exception {
		try {
			GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
			generatorSqlmap.generator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generator() throws Exception{

		List<String> warnings = new ArrayList<String>();

		Configuration config = new ConfigurationParser(warnings).parseConfiguration(new File("whzTestGeneratorConfig.xml"));
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), warnings);
		myBatisGenerator.generate(null);
		System.out.println("告警信息");
		for (String warning : warnings) {
			System.out.println(warning);
		}

	}

}
