package ru.abrarov.javatests.topbuilder.example;

import java.util.Collection;
import java.util.List;

import ru.abrarov.javatests.topbuilder.FrequencyAnalyzer;
import ru.abrarov.javatests.topbuilder.NaiveFrequencyAnalyzer;

/**
 * Sample app for demonstration of FrequencyAnalyzer usage.
 * 
 * @see FrequencyAnalyzer
 * @see NaiveFrequencyAnalyzer
 */
public class UsageExample {

	public static void main(String[] args) {
		// Parse command line args and get user defined parameters of the source
		// data generation and the top list size
		final Parameters parameters = new Parameters(args);
		// Build randomized source data to be analyzed
		final Collection<String> sourceValues = buildRandomValues(parameters);
		if (parameters.showSourceData) {
			printSourceData(sourceValues);
		}
		// Create solver and analyze source data
		final FrequencyAnalyzer frequencyAnalyzer = buildFrequencyAnalyzer(parameters);
		printStartNotification();
		final long startTime = System.currentTimeMillis();
		final List<FrequencyAnalyzer.Item> top = frequencyAnalyzer
				.buildTopFrequentList(sourceValues.iterator(),
						parameters.topListSize);
		final long durationInMillis = System.currentTimeMillis() - startTime;
		printStopNotification();
		printTestDuration(durationInMillis);
		// Output analyzed results - the built top list
		printAnalyzedData(top);
	}

	private static class Parameters {

		/**
		 * Minimum length of the source string to be generated.
		 */
		public final int minValueLength;
		/**
		 * Maximum length of the source string to be generated.
		 */
		public final int maxValueLength;
		/**
		 * Number of unique source strings to be generated.
		 */
		public final int uniqueValueCount;
		/**
		 * Total number of generated source strings
		 */
		public final int totalValueCount;
		/**
		 * Maximum size of the top list to be build.
		 */
		public final int topListSize;
		public final boolean showSourceData;

		/**
		 * Parses commandline parameters and builds app parameters according to
		 * the parsed data.
		 * 
		 * @param args
		 *            Commandline parameters to be parsed and used for building
		 *            app parameters: &lt;unique value number&gt; &lt;total
		 *            value number&gt; &lt;top list size&gt; &lt;min value
		 *            length&gt; &lt;max value length&gt; &lt;show source data
		 *            flag&gt;
		 */
		private Parameters(String[] args) {
			this.uniqueValueCount = parseIntArg(args, 0, 100);
			this.totalValueCount = parseIntArg(args, 1, 10000);
			this.topListSize = parseIntArg(args, 2, 10);
			this.minValueLength = parseIntArg(args, 3, 4);
			this.maxValueLength = parseIntArg(args, 4, 32);
			this.showSourceData = parseIntArg(args, 5, 1) != 0;
		}

		/**
		 * Parses given commandline parameter as integer value.
		 * 
		 * @param args
		 *            Commandline parameters.
		 * @param index
		 *            Index of the commandline parameter to be parsed.
		 * @param defaultValue
		 *            Default value returned when the commandline parameter
		 *            wasn't found.
		 * @return Parsed value or the default one if the commandline parameter
		 *         wasn't found.
		 */
		private static int parseIntArg(String[] args, int index,
				int defaultValue) {
			if (args.length > index) {
				return Integer.parseInt(args[index]);
			}
			return defaultValue;
		}
	}

	/**
	 * Outputs source data.
	 * 
	 * @param values
	 *            Source data to be output.
	 */
	private static void printSourceData(Collection<String> values) {
		System.out.println(String.format("Values to analyze (total: %d)",
				values.size()));
		for (String value : values) {
			System.out.println(value);
		}
	}

	private static void printStartNotification() {
		System.out.print("Starting...");
	}

	private static void printStopNotification() {
		System.out.println("done");
	}

	private static void printTestDuration(long durationInMillis) {
		System.out.println(String.format("Duration: %d.%03d sec",
				durationInMillis / 1000, durationInMillis % 1000));
	}

	/**
	 * Outputs result data.
	 * 
	 * @param topList
	 *            The top list to be output.
	 */
	private static void printAnalyzedData(
			Collection<FrequencyAnalyzer.Item> topList) {
		System.out.println(String.format("Top %d most frequent values",
				topList.size()));
		for (FrequencyAnalyzer.Item item : topList) {
			System.out.println(String.format("Frequency: %d. Value: %s",
					item.frequency(), item.value()));
		}
	}

	/**
	 * Builds randomized collection of (random) strings according to the
	 * parameters of app.
	 * 
	 * @param parameters
	 *            App parameters.
	 * @return Randomized collection of random strings.
	 */
	private static Collection<String> buildRandomValues(Parameters parameters) {
		return new RandomSourceDataProvider().buildRandomValues(
				parameters.minValueLength, parameters.maxValueLength,
				parameters.uniqueValueCount, parameters.totalValueCount);
	}

	/**
	 * Builds implementation of analyzer according to the parameters of app.
	 * 
	 * @param parameters
	 *            App parameters.
	 * @return Built analyzer implementation.
	 */
	private static FrequencyAnalyzer buildFrequencyAnalyzer(
			Parameters parameters) {
		// todo: select implementation of FrequencyAnalyzer according to
		// parameters
		return new NaiveFrequencyAnalyzer();
	}
}
