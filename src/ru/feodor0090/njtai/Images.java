package ru.feodor0090.njtai;

import java.util.Hashtable;

import javax.microedition.lcdui.Image;

import tube42.ImgUtil;

public class Images {
	private static Hashtable cache = new Hashtable();

	public static void reset() {
		cache = new Hashtable();
		System.gc();
	}

	public static Image get(String url, boolean mini) {
		// cache lookup
		if (cache.containsKey(url))
			return (Image) cache.get(url);
		String url1 = url;
		// url proc
		if (url.startsWith("https://"))
			url = url.substring(8);
		if (url.startsWith("http://"))
			url = url.substring(7);
		url = NJTAI.proxy + url;
		System.out.println("Loading " + url);

		Image i = Network.loadImage(url);
		if (mini) {
			int h = NJTAI.getHeight() * 2 / 3;
			int w = (int) (((float) h / i.getHeight()) * i.getWidth());
			i = ImgUtil.resize(i, w, h, true);
		} else {
			if (NJTAI.cache)
				cache.put(url1, i);
		}
		return i;
	}

	public static Image get(String url, int targetH) {
		if(url==null)return null;
		// cache lookup
		if (cache.containsKey(url))
			return (Image) cache.get(url);
		String url1 = url;
		// url proc
		if (url.startsWith("https://"))
			url = url.substring(8);
		if (url.startsWith("http://"))
			url = url.substring(7);
		url = NJTAI.proxy + url;
		System.out.println("Loading " + url);

		Image i = Network.loadImage(url);
		int h = targetH;
		int w = (int) (((float) h / i.getHeight()) * i.getWidth());
		i = ImgUtil.resize(i, w, h, true);
		if (NJTAI.cache)
			cache.put(url1, i);
		return i;
	}
}
