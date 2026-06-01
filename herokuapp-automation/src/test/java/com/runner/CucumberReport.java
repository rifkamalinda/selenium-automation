package com.runner;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import com.github.mkolisnyk.cucumber.reporting.CucumberUsageReporting;

public class CucumberReport {

	public void generateCucumberReport() throws Exception {
		CucumberResultsOverview results = new CucumberResultsOverview();

		// results.setOutputDirectory("target");
		// results.setOutputName("cucumber-results");
		results.setSourceFile("./target/cucumber-com.horizon.report.json");
		results.setOutputDirectory("target");
		results.setOutputName("cucumber-results");

		CucumberUsageReporting report = new CucumberUsageReporting();
		report.setJsonUsageFile("./target/cucumber-usage.json");
		report.setOutputDirectory("target");
		report.executeReport();
		results.execute();
		try {

			// results.executeFeaturesOverviewReport();
			report.executeReport();
			// results.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			CucumberReport CR = new CucumberReport();

			System.out.println("Report is getting generated " + CR.toString());
			CR.generateCucumberReport();
		} catch (Throwable t) {

			t.getStackTrace();
		} finally {

			System.out.println("check report in target finally");
		}
	}
}
