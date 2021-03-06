package njtai.models;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import njtai.NJTAI;
import njtai.StringUtil;

/**
 * List of {@link MangaObj}ects. Parses form page blocks and intended to be
 * passed into {@link njtai.ui.MangaList} constructor.
 * 
 * @author Feodor0090
 *
 */
public class MangaObjs implements Enumeration {

	/**
	 * Parses this object from html fragment.
	 * 
	 * @param html HTML content of container with a few gallery blocks.
	 */
	public MangaObjs(String html) {
		String[] items = StringUtil.split(html, "<div class=\"gallery\"");
		if (items.length == 1 && items[0].length() < 3) {
			list = new MangaObj[0];
			return;
		}
		Vector v = new Vector();
		for (int i = 0; i < items.length; i++) {
			try {
				if (!items[i].startsWith("<h"))
					v.addElement(new MangaObj(items[i]));
			} catch (RuntimeException e) {
				System.out.println("Failed on " + i);
				e.printStackTrace();
			}
		}
		list = new MangaObj[v.size()];
		v.copyInto(list);
		v.removeAllElements();
		v = null;
	}

	MangaObj[] list;

	int next = 0;

	public boolean hasMoreElements() {
		return next < list.length;
	}

	public Object nextElement() {
		if (NJTAI.loadCovers)
			list[next].loadCover();
		next++;
		return list[next - 1];
	}

	public static final String POPULAR_DIV = "<div class=\"container index-container index-popular\">";
	public static final String NEW_DIV = "<div class=\"container index-container\">";
	public static final String PAGIN_SEC = "<section class=\"pagination\">";
	public static final String SEARCH_Q = "/search/?q=";

	/**
	 * Gets list of popular.
	 * 
	 * @return Loaded list.
	 * @throws IOException            Connection error.
	 * @throws IllegalAccessException Proxy is broken.
	 */
	public static MangaObjs getPopularList() throws IOException, IllegalAccessException {
		String sec = StringUtil.range(NJTAI.getHP(), POPULAR_DIV, NEW_DIV, false);
		return new MangaObjs(sec);
	}

	/**
	 * Gets list of new uploads.
	 * 
	 * @return Loaded list.
	 * @throws IOException            Connection error.
	 * @throws IllegalAccessException Proxy is broken.
	 */
	public static MangaObjs getNewList() throws IOException, IllegalAccessException {
		String sec = StringUtil.range(NJTAI.getHP(), NEW_DIV, PAGIN_SEC, false);
		return new MangaObjs(sec);
	}

	/**
	 * Searches for the title and gets list of results.
	 * 
	 * @param query  Text to search.
	 * @param caller Screen to work after.
	 * @return Loaded list.
	 */
	public static MangaObjs getSearchList(String query, Object caller) {
		String q = NJTAI.proxy + NJTAI.baseUrl + SEARCH_Q + query;
		String r = WebAPIA.inst.getUtfOrNull(q);
		if (r == null) {
			NJTAI.pl.showNotification("Network error", "Check proxy and connection.", 3, caller);
			return null;
		}
		String sec = StringUtil.range(r, NEW_DIV, PAGIN_SEC, false);
		return new MangaObjs(sec);
	}
}
