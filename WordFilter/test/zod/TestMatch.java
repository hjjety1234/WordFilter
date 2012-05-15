package zod;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;
import org.junit.Test;

public class TestMatch {
	@Test
	public void testMatch() throws IOException {
		String source = FileUtils.readFileToString(new File(
				"D:/zod/workspace-j2ee/WordFilter/test/source/1.html"));
		List<String> keywords = FileUtils.readLines(new File(
				"D:/zod/workspace-j2ee/WordFilter/test/keyword.txt"));

		AhoCorasick tree = new AhoCorasick();
		for (String keyword : keywords) {
			if (!keyword.isEmpty()) {
				tree.add(keyword.getBytes(), keyword);
			}
		}
		// tree.add("中国".getBytes(), "中国");
		// tree.add("中东".getBytes(), "中东");
		tree.prepare();

		Set<String> matchedWords = new HashSet<String>();
		Iterator<SearchResult> searcher = tree.search(source.getBytes());
		while (searcher.hasNext()) {
			SearchResult result = searcher.next();
			Set<?> r = result.getOutputs();
			for (Object o : r) {
				matchedWords.add((String) o);
			}
			System.out.println(result.getOutputs());
			System.out.println("Found at index: " + result.getLastIndex());
		}

		for (String matchedWord : matchedWords) {
			source = source.replaceAll(matchedWord, "<span class='keyword'>"
					+ matchedWord + "</span>");
		}
		FileUtils.writeStringToFile(new File(
				"D:/zod/workspace-j2ee/WordFilter/test/result/1.html"), source);
	}
}
