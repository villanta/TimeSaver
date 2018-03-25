package com.timesaver.app.file;

import java.util.Properties;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Configuration {

	private static final String FONT_PROPERTY_KEY = "FONT";
	private static final String COLOR_PROPERTY_KEY = "COLOR";
	private static final double DEFAULT_SIZE = 12;

	private Font font;
	private Color color;

	public Configuration() {
		font = new Font(DEFAULT_SIZE);
		color = Color.WHITE;
	}
	
	public void writeTo(Properties props) {
		String fontName = font.getName();
		String colorRgba = String.format("%f:%f:%f:%f", color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
		
		props.put(FONT_PROPERTY_KEY, fontName);
		props.put(COLOR_PROPERTY_KEY, colorRgba);
	}

	public void readFrom(Properties props) {
		String fontName = props.getProperty(FONT_PROPERTY_KEY);
		String colorRgba = props.getProperty(COLOR_PROPERTY_KEY);

		setFont(new Font(fontName, DEFAULT_SIZE));

		String[] rgba = colorRgba.split(":");
		double red = Double.parseDouble(rgba[0]);
		double green = Double.parseDouble(rgba[1]);
		double blue = Double.parseDouble(rgba[2]);
		double opacity = Double.parseDouble(rgba[3]);
		setColor(new Color(red, green, blue, opacity));
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
