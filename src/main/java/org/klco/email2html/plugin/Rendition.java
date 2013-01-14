package org.klco.email2html.plugin;

/**
 * Represents an image renditon to be created.
 */
public class Rendition {

	/** The fill. */
	private int fill;

	/** The height. */
	private int height;

	/** The name. */
	private String name;

	/** The width. */
	private int width;

	/**
	 * Parses the source string.
	 * 
	 * @param source
	 *            the source
	 * @return the rendition
	 */
	public static Rendition parse(String source) {
		Rendition r = new Rendition();
		String[] sourceStrs = source.split(" ");
		if (sourceStrs.length < 3) {
			throw new java.lang.IllegalArgumentException(
					"Rendition string must contain name, height and width");
		}
		r.name = sourceStrs[0];
		r.height = Integer.parseInt(sourceStrs[1], 10);
		r.width = Integer.parseInt(sourceStrs[2], 10);
		if (sourceStrs.length == 4) {
			r.fill = Integer.parseInt(sourceStrs[3], 10);
		}
		return r;
	}

	/**
	 * Gets the fill.
	 * 
	 * @return the fill
	 */
	public int getFill() {
		return fill;
	}

	/**
	 * Sets the fill.
	 * 
	 * @param fill
	 *            the new fill
	 */
	public void setFill(int fill) {
		this.fill = fill;
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
