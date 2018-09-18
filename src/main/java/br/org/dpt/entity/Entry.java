package br.org.dpt.entity;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.org.dpt.changer.Changer;

public class Entry {

	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static final SimpleDateFormat sdfNoYear = new SimpleDateFormat("dd/MM HH:mm");
	
	private static final int maxChars = Changer.cardWidth/9;
	
	private String header;
	private ArrayList<String> data;
	private Date deadline;
	private Color[] color;

	public Entry(String data) throws Exception {
		String[] fields = data.split(";");
		setColor(getColorFromCriticity(fields[0]));
		setDeadline(sdf.parse(fields[1]));
		setHeader(fields[2]);
		setData(fields[3]);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

	public void setData(String data) {
		// Split using (int)(width/9) char max lines
		String[] words = data.split(" ");
		setData(new ArrayList<String>());
		getData().add(new String());
		int lineSize = 0;
		for (String word : words) {
			if (lineSize + word.length() > maxChars || "|".equals(word)) {
				getData().add(new String());
				lineSize = 0;
			}

			if (!"|".equals(word)) {
				if (getData().get(getData().size() - 1).isEmpty()) {
					getData().set(getData().size() - 1, getData().get(getData().size() - 1) + word);
					lineSize += word.length();
				} else {
					getData().set(getData().size() - 1, getData().get(getData().size() - 1) + " " + word);
					lineSize += word.length() + 1;
				}
			}
		}
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Color[] getColor() {
		return color;
	}

	public void setColor(Color[] color) {
		this.color = color;
	}

	private Color[] getColorFromCriticity(String criticity) {
		switch (criticity) {
		case "CRITICAL":
			return new Color[] { new Color(217, 1, 169), new Color(255, 195, 244) };
		case "MAJOR":
			return new Color[] { new Color(255, 185, 1), new Color(255, 242, 181) };
		case "MINOR":
			return new Color[] { new Color(11, 137, 5), new Color(199, 239, 196) };
		case "INFO":
			return new Color[] { new Color(119, 119, 119), new Color(249, 249, 249) };
		default:
			return new Color[] { new Color(0, 0, 0), new Color(0, 0, 0) };
		}
	}

	public String getStringDeadline() {
		return sdfNoYear.format(getDeadline());
	}
}
