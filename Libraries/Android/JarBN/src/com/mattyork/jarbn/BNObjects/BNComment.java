package com.mattyork.jarbn.BNObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.mattyork.jarbn.BNUtilities;
import com.mattyork.jarbn.OMScanner;
import com.mattyork.jarbn.BNObjects.BNPost.PostType;

public class BNComment implements Parcelable{
	public enum CommentType {
		CommentTypeDefault, CommentTypeAskHN, CommentTypeJobs
	}

	public CommentType Type;
	public String Text;
	public String Username;
	public String CommentId;
	public String ParentID;
	public String TimeCreatedString;
	public String ReplyURLString;
	public int Level;
	public ArrayList<BNCommentLink> Links;
	
	public BNComment() {
	}
	
	 protected BNComment(Parcel in) {
	        Type = (CommentType) in.readValue(CommentType.class.getClassLoader());
	        Text = in.readString();
	        Username = in.readString();
	        CommentId = in.readString();
	        ParentID = in.readString();
	        TimeCreatedString = in.readString();
	        ReplyURLString = in.readString();
	        Level = in.readInt();
	        if (in.readByte() == 0x01) {
	            Links = new ArrayList<BNCommentLink>();
	            in.readList(Links, BNCommentLink.class.getClassLoader());
	        } else {
	            Links = null;
	        }
	    }

	    @Override
	    public int describeContents() {
	        return 0;
	    }

	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeValue(Type);
	        dest.writeString(Text);
	        dest.writeString(Username);
	        dest.writeString(CommentId);
	        dest.writeString(ParentID);
	        dest.writeString(TimeCreatedString);
	        dest.writeString(ReplyURLString);
	        dest.writeInt(Level);
	        if (Links == null) {
	            dest.writeByte((byte) (0x00));
	        } else {
	            dest.writeByte((byte) (0x01));
	            dest.writeList(Links);
	        }
	    }

	    @SuppressWarnings("unused")
	    public static final Parcelable.Creator<BNComment> CREATOR = new Parcelable.Creator<BNComment>() {
	        @Override
	        public BNComment createFromParcel(Parcel in) {
	            return new BNComment(in);
	        }

	        @Override
	        public BNComment[] newArray(int size) {
	            return new BNComment[size];
	        }
	    };
	
	public static ArrayList<BNComment> parsedCommentsFromHTML(String htmlString, BNPost post) {
		ArrayList<BNComment> comments = new ArrayList<BNComment>();
		List<String> htmlComponents = Arrays.asList(htmlString.split("\\s*<tr><td><table border=0><tr><td><img src=\"s.gif\"\\s*"));
		
		if (post.Type == PostType.PostTypeAskHN) {
			//Grab AskHN Post
			OMScanner scanner = new OMScanner(htmlComponents.get(0));
			String text, user, timeAgo;
			
			//Scan User
			scanner.skipToString("<a href=\"user?id=");
			user = scanner.scanToString("\">");
			
			//Scan Time Ago
			scanner.skipToString("</a> ");
			timeAgo = scanner.scanToString("ago");
			
			//Scan Ask text
			scanner.skipToString("</tr><tr><td></td><td>");
			text = scanner.scanToString("</td>");
			
			//Create special comment for it
			BNComment newComment = new BNComment();
			newComment.Level = 0;
			newComment.Username = user;
			newComment.TimeCreatedString = timeAgo;
			newComment.Text = BNUtilities.stringByReplacingHTMLEntitiesInText(text);
			newComment.Links = BNCommentLink.linksFromCommentText(newComment.Text);
			newComment.Type = CommentType.CommentTypeAskHN;
			comments.add(newComment);
		}
		
		if (post.Type == PostType.PostTypeJobs) {
			//Grave Jobs Post
			OMScanner scanner = new OMScanner(htmlComponents.get(0));
			scanner.skipToString("</tr><tr><td></td><td>");
			String text = scanner.scanToString("</td>");
			
			//Create special comment for it
			BNComment newComment = new BNComment();
			newComment.Level = 0;
			newComment.Text = BNUtilities.stringByReplacingHTMLEntitiesInText(text);
			newComment.Links = BNCommentLink.linksFromCommentText(newComment.Text);
			newComment.Type = CommentType.CommentTypeJobs;
			comments.add(newComment);
		}
		
		for (int xx = 1; xx < htmlComponents.size(); xx++) {
			
			//Parse comment
			BNComment newComment = BNComment.commentFromHTML(htmlComponents.get(xx));
			
	        //Add comment to array
	        comments.add(newComment);
		}
		
		return comments;
	}
	
	public static BNComment commentFromHTML(String htmlString){
		//Create master scanner
		OMScanner scanner = new OMScanner(htmlString);
		
		//Create empty comment
		BNComment newComment = new BNComment();
		newComment.Type = CommentType.CommentTypeDefault;
        String level = "";
        String user = "";
        String text = "";
        String reply = "";
        String commentId = "";
		
        int contentIndex = -1;
        
        //Get Comment Level
        contentIndex = htmlString.indexOf("height=1 width=");
        if (contentIndex != -1) {
        	scanner.setScanIndex(contentIndex + "height=1 width=".length());
            newComment.Level = Integer.parseInt(scanner.scanToString(">"))/40;
            scanner.setScanIndex(0);
		}
        
        
        //Get Username
        contentIndex = htmlString.indexOf("<a href=\"user?id=");
        if (contentIndex != -1) {
        	scanner.setScanIndex(contentIndex + "<a href=\"user?id=".length());
        	user = scanner.scanToString("\">");
            if (user.length() == 0) {
    			newComment.Username = "[deleted]";
    		}
            else {
            	newComment.Username = user;
            }
            scanner.setScanIndex(0);
		}
        
        //Get Date/Time
        contentIndex = htmlString.indexOf(newComment.Username+"</a>");
        if (contentIndex != -1) {
        	 scanner.setScanIndex(contentIndex +newComment.Username.length()+"</a> ".length());
             newComment.TimeCreatedString = scanner.scanToString("  |");
             scanner.setScanIndex(0);
		}
        else if (htmlString.indexOf(newComment.Username+"</font></a>") != -1) {
        	scanner.setScanIndex(htmlString.indexOf(newComment.Username+"</font></a>") +newComment.Username.length()+"</font></a> ".length());
            newComment.TimeCreatedString = scanner.scanToString("  |");
            scanner.setScanIndex(0);
		}
        
        //Get Comment Text
        newComment.Text = "";
        ArrayList<Integer> commentIndexes = BNComment.findIndexes(htmlString, "<span class=\"comment\"><font color=", scanner);
        for (Integer index : commentIndexes) {
        	if (index != -1) {
            	scanner.setScanIndex(index + "<span class=\"comment\"><font color=#000000>".length());
            	text =  scanner.scanToString("</font>");
            	
            	if (newComment.Text.equals("")) {
            		newComment.Text = BNUtilities.stringByReplacingHTMLEntitiesInText(text);
				}
            	else {
            		newComment.Text = newComment.Text +"\n\n"+BNUtilities.stringByReplacingHTMLEntitiesInText(text);
            	}
                scanner.setScanIndex(0);
    		}
		}
        
        //Get Comment Id
        contentIndex = htmlString.indexOf("<a href=\"reply?id=");
        if (contentIndex != -1) {
			scanner.setScanIndex(contentIndex + "<a href=\"reply?id=".length());
			newComment.CommentId = scanner.scanToString("&");
	        
			//Get Reply Url String
			scanner.setScanIndex(contentIndex + "<a href=\"".length());
	        newComment.ReplyURLString = scanner.scanToString("\">");
		}
        
        //Get Links
        newComment.Links = BNCommentLink.linksFromCommentText(newComment.Text);
        
        newComment.ParentID = "";
        
        return newComment;
	}
	
	private static ArrayList<Integer> findIndexes(String baseString, String matchString, OMScanner scanner){
		scanner.setScanIndex(0);
		ArrayList<Integer> matches = new ArrayList<Integer>();
		
		Integer contentIndex = 0;
		while (contentIndex != -1) {
			contentIndex = baseString.substring(scanner.getScanIndex(), baseString.length()-1).indexOf(matchString);
			if (contentIndex != -1) {
				matches.add(contentIndex);
				scanner.setScanIndex(contentIndex + matchString.length());
			}
		}
		
	    return matches;
	}
}
