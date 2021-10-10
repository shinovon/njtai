package njtai.m.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import njtai.NJTAI;
import njtai.m.NJTAIM;

/**
 * Njtai preferences screen.
 * 
 * @author Feodor0090
 *
 */
final class Prefs extends Form implements ItemCommandListener, CommandListener {

	private MMenu menu;
	private final Command bkC = new Command(NJTAI.rus ? "Применить" : "Apply", 2, 2);
	private final Command cnclC = new Command(NJTAI.rus ? "Отмена" : "Revert", Command.SCREEN, 2);
	private final Command prC = new Command("Proxy setup", 8, 1);

	private final String[] yn = new String[] { NJTAI.rus ? "Нет" : "No", NJTAI.rus ? "Да" : "Yes" };
	private final String[] ynr = new String[] { NJTAI.rus ? "Нет (экономит память)" : "No (saves RAM)",
			NJTAI.rus ? "Да" : "Yes" };

	private final StringItem ramWarn = new StringItem(NJTAI.rus ? "Предупреждение" : "Warning",
			NJTAI.rus ? "Включение предзагрузки или кэширования приведёт к ошибкам на устройствах с небольшой памятью."
					: "Enabling preloading/caching will cause crash on low memory devices.");
	private final StringItem s40Warn = new StringItem(NJTAI.rus ? "Работа на S40" : "Working on S40", NJTAI.rus
			? ("Памяти S40 не хватит для просмотра, но хватит для скачивания. Отключите загрузку обложек в списках и на "
					+ "странице, сохранение списков и кэш для работы. Рекомендовано использовать поиск по ID, а не по названию.")
			: ("S40's memory is enough for downloading, but not for viewing. "
					+ "Disable covers and lists keeping to make it work. Using ID input (not search) is recommended."));

	private final ChoiceGroup cache = new ChoiceGroup(NJTAI.rus ? "Поведение кэширования" : "Caching behaviour", 4,
			new String[] { NJTAI.rus ? "Отключено" : "Disabled",
					NJTAI.rus ? "Сохранять уже загруженное" : "Keep already loaded",
					NJTAI.rus ? "Предзагружать" : "Preload" },
			null);
	private final ChoiceGroup pageCover = new ChoiceGroup(
			NJTAI.rus ? "Загружать обложку на странице" : "Load cover in manga page", 4, ynr, null);
	private final ChoiceGroup covers = new ChoiceGroup(
			NJTAI.rus ? "Загружать обложку в списках" : "Load covers in lists", 4, ynr, null);
	private final ChoiceGroup lists = new ChoiceGroup(
			NJTAI.rus ? "Запоминать списки при открытии страницы" : "Keep lists when opening pages", 4, ynr, null);
	private final ChoiceGroup bitmaps = new ChoiceGroup(NJTAI.rus ? "Декодировать JPEG единожды (повысит плавность)"
			: "Decode JPEG only once (improves perfomance)", 4, ynr, null);
	private final ChoiceGroup urls = new ChoiceGroup(NJTAI.rus ? "Предзагружать URL страниц" : "Preload image urls", 4,
			yn, null);
	protected final TextField proxy = new TextField(NJTAI.rus ? "Префикс прокси" : "Proxy prefix", NJTAI.proxy, 100, 0);
	private final StringItem aboutProxy = new StringItem(null,
			NJTAI.rus ? "Настройка вашего прокси" : "Setting your own proxy", StringItem.BUTTON);

	private final ChoiceGroup view = new ChoiceGroup("View type", 4, new String[] { "Auto", "SWR", "HWA" }, null);
	private final ChoiceGroup files = new ChoiceGroup(NJTAI.rus ? "Кэшировать на карту памяти" : "Cache to memory card",
			4, yn, null);

	public Prefs(MMenu menu) {
		super("NJTAI settings");
		this.menu = menu;

		setCommandListener(this);
		addCommand(bkC);
		addCommand(cnclC);

		String vendor = System.getProperty("java.vendor");
		if (vendor != null && vendor.toLowerCase().indexOf("ndroid") != -1) {
			append(new StringItem("J2MEL-specific bugs",
					"On j2me loader 1.7.4 and lower switches on this screen won't reflect actual settings states due "
							+ "to emulator's bug. Don't forget to set them all as you need before returning!"));
		}

		cache.setSelectedIndex(NJTAI.cachingPolicy, true);
		pageCover.setSelectedIndex(NJTAI.loadCoverAtPage ? 1 : 0, true);
		lists.setSelectedIndex(NJTAI.keepLists ? 1 : 0, true);
		covers.setSelectedIndex(NJTAI.loadCovers ? 1 : 0, true);
		bitmaps.setSelectedIndex(NJTAI.keepBitmap ? 1 : 0, true);
		urls.setSelectedIndex(NJTAI.preloadUrl ? 1 : 0, true);
		files.setSelectedIndex(NJTAI.files ? 1 : 0, true);
		view.setSelectedIndex(NJTAI.view, true);
		aboutProxy.setDefaultCommand(prC);
		aboutProxy.setItemCommandListener(this);

		append(ramWarn);
		if (Runtime.getRuntime().totalMemory() == 2048 * 1024)
			append(s40Warn);
		append(cache);
		append(pageCover);
		append(covers);
		append(lists);
		append(files);
		append(bitmaps);
		append(urls);
		append(proxy);
		append(aboutProxy);
		append(new StringItem("Beta features", "Better not to touch them."));
		append(view);
		ChoiceGroup c = new ChoiceGroup("Setting", 4, new String[] { "1", "2", "3" }, null);
		c.setSelectedIndex(1, true);
		// append(c);
	}

	public final void commandAction(Command c, Displayable arg1) {
		cmd(c);
	}

	public final void commandAction(Command c, Item arg1) {
		cmd(c);
	}

	private final void cmd(Command c) {
		if (c == bkC) {
			NJTAI.cachingPolicy = cache.getSelectedIndex();
			NJTAI.loadCoverAtPage = pageCover.getSelectedIndex() == 1;
			NJTAI.keepLists = lists.getSelectedIndex() == 1;
			NJTAI.loadCovers = covers.getSelectedIndex() == 1;
			NJTAI.preloadUrl = urls.getSelectedIndex() == 1;
			NJTAI.keepBitmap = bitmaps.getSelectedIndex() == 1;
			NJTAI.view = view.getSelectedIndex();
			NJTAI.files = files.getSelectedIndex() == 1;
			NJTAI.proxy = proxy.getString();
			if (NJTAI.proxy.length() == 0) {
				NJTAI.proxy = "https://";
			} else if (NJTAI.proxy.startsWith("http") && NJTAI.proxy.indexOf("://") != 0
					&& NJTAI.proxy.indexOf('.') != 0) {

				NJTAIM.setScr(menu);
				if (!NJTAI.pl.savePrefs()) {
					Alert a = new Alert("Settings", "Failed to write settings. They will reset after exit.", null,
							AlertType.ERROR);
					a.setTimeout(Alert.FOREVER);
					NJTAIM.setScr(a);
				}
			} else {
				Alert a = new Alert("Settings",
						"Incorrect proxy URL. Leave the field empty if you don't want to use it.", null,
						AlertType.ERROR);
				a.setTimeout(Alert.FOREVER);
				NJTAIM.setScr(a);
			}
		} else if (c == prC) {
			Alert a = new Alert("Proxy", "Proxy is necessary due to bad TLS support on java and domain blocks. "
					+ "To setup your own server, just create a PHP script that will take URL from query params, "
					+ "request it via CURL and return content. Read more info on github. To disable proxy, write \"https://\".",
					null, AlertType.INFO);
			a.setTimeout(Alert.FOREVER);
			NJTAIM.setScr(a);
		} else if (c == cnclC) {
			NJTAIM.setScr(menu);
		}
	}

}