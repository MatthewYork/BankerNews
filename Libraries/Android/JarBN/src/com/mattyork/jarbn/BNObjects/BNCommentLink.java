package com.mattyork.jarbn.BNObjects;

import java.sql.Struct;
import java.util.ArrayList;

import android.net.Uri;

public class BNCommentLink {
	public enum LinkType {
		LinkTypeDefault, LinkTypeHN
	}

	Uri Uri;
	BNRange UriRange;
	LinkType Type;
	
	/***
	 * Creates an ArrayList of link objects from a given comment string.
	 * @param commentString
	 * @return ArrayList<HNCommentLink>
	 */
	public static ArrayList<BNCommentLink> linksFromCommentText(
			String commentString) {
		ArrayList<BNCommentLink> linkArray = new ArrayList<BNCommentLink>();
		
		return linkArray;
	}
}
