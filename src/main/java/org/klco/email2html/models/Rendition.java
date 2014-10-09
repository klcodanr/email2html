package org.klco.email2html.models;

/**
 * Represents an image renditon to be created.
 */
public class Rendition {

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
			r.fill = Boolean.valueOf(sourceStrs[3]);
		}
		return r;
	}

	/** The fill, set to white by default. */
	private boolean fill = false;

	/** The height. */
	private int height;

	/** The name. */
	private String name;

	/** The width. */
	private int width;

	/**
	 * Gets the fill.
	 * 
	 * @return the fill
	 */
	public boolean getFill() {
		return fill;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * Sets the fill.
	 * 
	 * @param fill
	 *            the new fill
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
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
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rendition [fill=" + fill + ", height=" + height + ", name="
				+ name + ", width=" + width + "]";
	}
}
