package br.org.dpt.changer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import br.org.dpt.entity.Entry;
import br.org.dpt.loader.FileLoader;

public class Changer {
	
	private static final int lines = Integer.parseInt(System.getProperty("lines","4")); 
	private static final int columns = Integer.parseInt(System.getProperty("columns","4"));
	public static final int cardWidth = Integer.parseInt(System.getProperty("cardWidth","250"));
	private static final int cardHeight = Integer.parseInt(System.getProperty("cardHeight","125"));
	private static final int xStart = Integer.parseInt(System.getProperty("xStart","220"));
	private static final int yStart = Integer.parseInt(System.getProperty("yStart","174"));
	private static final long sleepTime = Long.parseLong(System.getProperty("sleepTime", "10000"));
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.currentTimeMillis();
	
		if (Boolean.parseBoolean(System.getProperty("singleRun"))) {
			System.out.println("Refreshing tasks file...");
			FileLoader.refresh();
			generate();
			System.out.println("Task file refreshed");
		} else {
			while (true) {
				if (FileLoader.refresh()) {
					System.out.println("Refreshing tasks file...");
					generate();
					System.out.println("Task file refreshed");
				}
				Thread.sleep(sleepTime);
				
				// If we passed 10 minutes without changes.
				if (System.currentTimeMillis() > startTime + 600000L) {
					FileLoader.refresh();
					generate();
					startTime = System.currentTimeMillis();
				}
			}
		}
	}
	
	public static void generate() throws Exception {
		// supply your own path instead of using this one
		String template = System.getProperty("user.dir") + File.separator + "background.png";
		String generated = System.getProperty("user.dir") + File.separator + "background-generated.png";
		
		FileOutputStream foG = new FileOutputStream(new File(generated));
		
		BufferedImage img = ImageIO.read(new File(template));
		
		for (int column=0; column <= ((int)FileLoader.entries.size()/lines) && column < columns; column++) {
			for (int line=0; line < lines && ((column*lines)+line) < FileLoader.entries.size() ; line++) {
				Entry entry = FileLoader.entries.get((column*lines)+line);
				
				// Header
				Graphics2D g2d = img.createGraphics();
				g2d.setColor(entry.getColor()[0]);
				g2d.setStroke(new BasicStroke(1));
				g2d.fillRect((column*(cardWidth+20))+xStart, (line*(cardHeight+15))+yStart, cardWidth, 35);
				g2d.dispose();

				// Due date sign
				InputStream is = Changer.class.getResourceAsStream("/fa-solid-900.ttf");
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                g2d = img.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                g2d.setFont(font.deriveFont(Font.PLAIN, 20));
				long diff = ChronoUnit.DAYS.between(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
						entry.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				if (diff > 0) {
					g2d.setColor(new Color(0,0,0));
					g2d.drawString("\uf164", (column*(cardWidth+20))+xStart+(cardWidth-30), (line*(cardHeight+15))+yStart+5+39/2);
				} else {
					if (entry.getDeadline().after(new Date())) {
						g2d.setColor(new Color(255,255,0));
						g2d.drawString("\uf0a5", (column*(cardWidth+20))+xStart+(cardWidth-30), (line*(cardHeight+15))+yStart+5+39/2);
					} else {
						g2d.setColor(new Color(255,0,0));
						g2d.drawString("\uf071", (column*(cardWidth+20))+xStart+(cardWidth-30), (line*(cardHeight+15))+yStart+5+39/2);
					}
				}
				g2d.dispose();

				// Body
				g2d = img.createGraphics();
				g2d.setColor(entry.getColor()[1]);
				g2d.setStroke(new BasicStroke(1));
				g2d.fillRect((column*(cardWidth+20))+xStart, (line*(cardHeight+15))+yStart+35, cardWidth, (cardHeight-35));
				g2d.dispose();

				// Due date on last line
				g2d = img.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				g2d.setFont(new Font("Calibri Light", Font.PLAIN, 12));
				g2d.setColor(new Color(0,0,0));
				g2d.drawString(entry.getStringDeadline(), (column*(cardWidth+20))+xStart+(cardWidth-10) - g2d.getFontMetrics().stringWidth(entry.getStringDeadline()), (line*(cardHeight+15))+yStart+(cardHeight-10));
				g2d.dispose();

				// Texts
				g2d = img.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				g2d.setFont(new Font("Calibri Light", Font.BOLD, 20));
				g2d.setColor(new Color(0,0,0));
				g2d.drawString(entry.getHeader(), (column*(cardWidth+20))+xStart+5, (line*(cardHeight+15))+yStart+5+39/2);
				g2d.dispose();
				for (int w = 0; w < entry.getData().size(); w++) {
					g2d = img.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
					g2d.setFont(new Font("Calibri Light", Font.PLAIN, 20));
					g2d.setColor(new Color(0,0,0));
					g2d.drawString(entry.getData().get(w), (column*(cardWidth+20))+xStart+5, (line*(cardHeight+15))+yStart+40+15+(w*20));
					g2d.dispose();
				}
			}
		}

		ImageIO.write(img, "PNG", foG);
		foG.flush();
		foG.close();

		if (SPI.INSTANCE.SystemParametersInfo(new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), new UINT_PTR(0), generated,
				new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE))) {
			System.out.println("Background updated.");
		} else {
			System.out.println("Error updating background!");
		}
	}

	public interface SPI extends StdCallLibrary {

		// from MSDN article
		long SPI_SETDESKWALLPAPER = 20;
		long SPIF_UPDATEINIFILE = 0x01;
		long SPIF_SENDWININICHANGE = 0x02;

		@SuppressWarnings("serial")
		SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class, new HashMap<Object, Object>() {
			{
				put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
				put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
			}
		});

		boolean SystemParametersInfo(UINT_PTR uiAction, UINT_PTR uiParam, String pvParam, UINT_PTR fWinIni);
	}
}
