package njtai.ui;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;

import njtai.NJTAI;
import njtai.StringUtil;
import njtai.models.MangaObjs;

public final class MMenu extends List implements CommandListener {

	public MMenu() {
		super("NJTAI", List.IMPLICIT, new String[] { "Enter number", "Popular list", "Recently uploaded",
				"Search by title", "Settings", "Keyboard controls help", "About" }, null);
		this.addCommand(exitCmd);
		this.setCommandListener(this);
	}

	private Command exitCmd = new Command("Exit", Command.EXIT, 2);
	private Command backCmd = new Command("Back", Command.BACK, 2);
	private Command openCmd = new Command("Go", Command.OK, 1);
	private Command searchCmd = new Command("Search", Command.OK, 1);

	static final String POPULAR_DIV = "<div class=\"container index-container index-popular\">";
	static final String NEW_DIV = "<div class=\"container index-container\">";
	static final String PAGIN_SEC = "<section class=\"pagination\">";

	static final String SEARCH_Q = "/search/?q=";

	public void commandAction(Command c, Displayable d) {
		try {
			if (c == backCmd) {
				NJTAI.setScr(this);
				return;
			}
			if (c == searchCmd) {
				try {
					String st = ((TextBox) d).getString();
					st = st.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ').replace('\0', ' ');
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < st.length(); i++) {
						switch (st.charAt(i)) {
						case '!':
							sb.append("%21");
							break;
						case '#':
							sb.append("%23");
							break;
						case '$':
							sb.append("%24");
							break;
						case '%':
							sb.append("%25");
							break;
						case '&':
							sb.append("%26");
							break;
						case '\'':
							sb.append("%27");
							break;
						case '(':
							sb.append("%28");
							break;
						case ')':
							sb.append("%29");
							break;
						case '*':
							sb.append("%2A");
							break;
						case '+':
							sb.append("%2B");
							break;
						case ',':
							sb.append("%2C");
							break;
						case '/':
							sb.append("%2F");
							break;
						case ':':
							sb.append("%3A");
							break;
						case ';':
							sb.append("%3B");
							break;
						case '=':
							sb.append("%3D");
							break;
						case '?':
							sb.append("%3F");
							break;
						case '@':
							sb.append("%40");
							break;
						case '[':
							sb.append("%5B");
							break;
						case ']':
							sb.append("%5D");
							break;
						case '{':
							sb.append("%7B");
							break;
						case '|':
							sb.append("%7C");
							break;
						case '}':
							sb.append("%7D");
							break;
						case '\\':
							sb.append("%5C");
							break;
						case '~':
							sb.append("%7E");
							break;
						case '-':
							sb.append("%2D");
							break;
						case '_':
							sb.append("%5F");
							break;
						case '"':
							sb.append("%22");
							break;
						case '.':
							sb.append("%2E");
							break;
						case ' ':
							sb.append("%20");
							break;
						default:
							sb.append(st.charAt(i));
							break;
						}
					}
					String q = NJTAI.proxy + NJTAI.baseUrl + SEARCH_Q + sb.toString();
					String data = NJTAI.httpUtf(q);
					String section1 = StringUtil.range(data, NEW_DIV, PAGIN_SEC, false);
					NJTAI.setScr(new MangaList("Search results", this, new MangaObjs(section1)));
				} catch (Exception e) {
					e.printStackTrace();
					NJTAI.setScr(this);
					NJTAI.pause(100);
					NJTAI.setScr(new Alert("Failed to open", "Have you entered something URL-breaking?", null,
							AlertType.ERROR));
				}
				return;
			}
			if (c == openCmd) {
				try {
					NJTAI.setScr(new MangaPage(Integer.parseInt(((TextBox) d).getString()), this));
				} catch (Exception e) {
					NJTAI.setScr(this);
					NJTAI.pause(100);
					NJTAI.setScr(new Alert("Failed to go to page", "Have you entered correct number?", null,
							AlertType.ERROR));
				}
			}
			if (c == exitCmd) {
				NJTAI.close();
			}
			if (c == List.SELECT_COMMAND) {
				mainMenuLinks();
			}
		} catch (Throwable t) {
			System.gc();
			t.printStackTrace();
			NJTAI.setScr(this);
			NJTAI.pause(100);
			String info;
			if (t instanceof OutOfMemoryError) {
				info = "Not enough memory!";
			} else if (t instanceof IOException) {
				info = "Failed to connect.";
			} else {
				info = t.toString();
			}
			NJTAI.setScr(new Alert("Error", info, null, AlertType.ERROR));
		}
	}

	private void mainMenuLinks() throws IOException {
		switch (getSelectedIndex()) {
		case 0:
			// number;
			final TextBox tb = new TextBox("Enter ID:", "", 7, 2);
			tb.addCommand(openCmd);
			tb.addCommand(backCmd);
			tb.setCommandListener(this);
			NJTAI.setScr(tb);
			return;
		case 4:
			// sets
			NJTAI.setScr(new Prefs(this));
			return;
		case 1:
			// popular
			String section = StringUtil.range(NJTAI.getHP(), POPULAR_DIV, NEW_DIV, false);
			NJTAI.setScr(new MangaList("Popular list", this, new MangaObjs(section)));
			return;
		case 2:
			// new
			String section1 = StringUtil.range(NJTAI.getHP(), NEW_DIV, PAGIN_SEC, false);
			NJTAI.setScr(new MangaList("Recently uploaded", this, new MangaObjs(section1)));
			return;
		case 3:
			// search
			search();
			return;
		case 5:
			Alert a = new Alert("Controls", "OK - change zoom; D-PAD - move page / turn page; RSK - return.", null,
					AlertType.INFO);
			a.setTimeout(Alert.FOREVER);
			NJTAI.setScr(a);
			return;
		case 6:
			Alert a1 = new Alert("About", "NJTAI v" + NJTAI.ver() + "\n Developer: Feodor0090", null, AlertType.INFO);
			a1.setTimeout(Alert.FOREVER);
			NJTAI.setScr(a1);
			return;
		}
	}

	private void search() {
		final TextBox tb = new TextBox("Enter query:", "", 80, 0);
		tb.addCommand(searchCmd);
		tb.addCommand(backCmd);
		tb.setCommandListener(this);
		NJTAI.setScr(tb);
	}

}
