package njtai.d;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import njtai.models.WebAPIA;

public class WebAPIAImpl extends WebAPIA {
	
	private static final String USER_AGENT = null;

	WebAPIAImpl() {
		inst = this;
	}

	@Override
	public byte[] get(String uri) throws IOException {
		System.out.println("GET " + uri);
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestMethod("GET");
		conn.connect();
		int len = conn.getContentLength();
		InputStream is;
		try {
			is = conn.getInputStream();
		} catch (Exception e) {
			is = conn.getErrorStream();
		}
		if(len <= 0) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        final byte[] buf = new byte[4096];
	        int read;
	        while ((read = is.read(buf)) != -1) {
	            baos.write(buf, 0, read);
	        }
			is.close();
			conn.disconnect();
			final byte[] b = baos.toByteArray();
			System.out.println(len + " " + b.length);
			baos.close();
			return b;
		} else {
	        final byte[] b = new byte[len];
	        /*
	        int i = 0;
	        int read = 0;
	        while (true) {
	        	read = is.read();
	        	if(read == -1 || i >= len) {
	        		break;
	        	}
	        }
	        */
	        new DataInputStream(is).readFully(b);
	        return b;
		}
	}

	public byte[] getOrNull(String url) {
		try {
			return get(url);
		} catch (Exception e) {
			return null;
		}
	}

	public String getUtf(String url) throws IOException {
		return new String(get(url), "UTF-8");
	}

	public String getUtfOrNull(String url) {
		try {
			return getUtf(url);
		} catch (Exception e) {
			return null;
		}
	}
}