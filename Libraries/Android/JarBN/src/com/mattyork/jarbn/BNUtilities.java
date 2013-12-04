package com.mattyork.jarbn;

import java.util.ArrayList;

import com.mattyork.jarbn.BNObjects.BNIndexSet;

public class BNUtilities {

	public static String stringByReplacingHTMLEntitiesInText(String text) {
		text = text.replace("<p>", "\n\n");
		text = text.replace("</p>", "");
		text = text.replace("<i>", "");
		text = text.replace("</i>", "");
		text = text.replace("&#38;", "&");
		text = text.replace("&#62;", ">");
		text = text.replace("&#x27;", "'");
		text = text.replace("&#x2F;", "/");
		text = text.replace("&quot;", "\"");
		text = text.replace("&#60;", "<");
		text = text.replace("&lt;", "<");
		text = text.replace("&gt;", ">");
		text = text.replace("&amp;", "&");
		text = text.replace("<pre><code>", "");
		text = text.replace("</code></pre>", "");

		// Replace <a> tags
		ArrayList<BNIndexSet> linkIndexes = linkIndexesArrayList(text);
		int replacementOffset = 0;
		
		for (int ii = 0; ii < linkIndexes.size(); ii++) {
			int beginningContentIndex = linkIndexes.get(ii).start - replacementOffset;
			int endContentIndex = linkIndexes.get(ii).end - replacementOffset;

			if (beginningContentIndex != -1 && endContentIndex != -1) {
				String linkSubString = text.substring(beginningContentIndex,
						endContentIndex);
				String formattedLinkSubString = linkSubString.replace(" rel=\"nofollow\">", "");
				formattedLinkSubString = formattedLinkSubString.replace("<a href=", "");
				formattedLinkSubString = formattedLinkSubString.replace("</a>", "");
				formattedLinkSubString = formattedLinkSubString.replace("</font>", "");

				int endQuoteIndex = formattedLinkSubString.lastIndexOf("\"");
				formattedLinkSubString = formattedLinkSubString.substring(1, endQuoteIndex);

				text = text.replace(linkSubString, formattedLinkSubString);
				replacementOffset += (linkSubString.length() - formattedLinkSubString.length());
			}
		}
		
		text = text.replace(" rel=\"nofollow\">", "");
		text = text.replace("<a href=", "");
		text = text.replace("</a>", "");
		text = text.replace("</font>", "");
		
		return text;
	}

	private static ArrayList<BNIndexSet> linkIndexesArrayList(String text) {
		ArrayList<BNIndexSet> indexes = new ArrayList<BNIndexSet>();
		
		if (text.contains("<a href=")) {
			OMScanner scanner = new OMScanner(text);
			
			while (text.substring(scanner.getScanIndex()).contains("<a href=")) {
				scanner.skipToString("<a href=");
				int startIndex = scanner.getScanIndex();
				scanner.scanToString("</a>");
				int endIndex = scanner.getScanIndex();

				indexes.add(new BNIndexSet(startIndex, endIndex));
				//scanner.setScanIndex(scanner.getScanIndex()+ "<a href=".length());
				
				if (scanner.getScanIndex() >= text.length()) {
					break;
				}
			}
		}
		
		return indexes;
	}
	
	
}


