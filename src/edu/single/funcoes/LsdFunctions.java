package edu.single.funcoes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.util.GraphicsUtil;

/**
 * 
 * @author lsd
 *
 */
public final class LsdFunctions {

	private LsdFunctions() {
	}

	// Created by lsd
	public static int getBits(int value, int start, int quantity) {
		if (quantity > 32 || (quantity + start) > 32 || quantity < 0 || start > 31 || start < 0) {
			throw new NumberFormatException("Value size is invalid");
		}

		if (quantity == 32) {
			return value;
		}

		int end = (start + quantity) - 1;
		value <<= (31 - end);
		value >>>= ((31 - quantity) + 1);
		return value;
	}

	// Created by lsd
	public static List<String> getComponentValue(Project proj, String componentName, String label, int portIndex) {
		List<String> result = new ArrayList<String>();

		Iterator<Component> components = ProjectUtils.getComponentsIterator(proj);
		while (components.hasNext()) {
			Component comp = components.next();
			if (comp.getFactory().getName().equals(componentName)) {
				if (label == null || comp.getAttributeSet().getValue(StdAttr.LABEL).equals(label)) {
					result.add(proj.getCircuitState().getValue(comp.getEnd(portIndex).getLocation())
							.toDecimalString(true));
				}
			}
		}

		return result;
	}

	// Created by lsd
	public static <T> void printArray(T[][] obj) {
		StringBuilder bs = new StringBuilder();
		for (T[] ob : obj) {
			for (T o : ob) {
				bs.append(o);
				bs.append(" ");
			}
			bs.append("\n");
		}
		System.out.println(bs.toString());
	}

	public static int[] getDistanceFromMiddle(Bounds bounds) {
		return new int[] { bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight() };
	}
	
	// Created by lsd
	public static void setTitle(InstancePainter painter, String title) {
		setTitle(painter, title, true);
	}

	// Created by lsd
	public static void setTitle(InstancePainter painter, String title, boolean up) {
		// to maintain color
		Graphics g = painter.getGraphics();
		Color old = g.getColor();
		Bounds bounds = painter.getBounds();

		int x = bounds.getX(), y = 0, offset = 8;
		int width = bounds.getWidth();
		
		if(up) {
			y = bounds.getY() - 20;
		} else {
			y = bounds.getY() + bounds.getHeight();
		}
		g.setColor(Color.BLACK);
		GraphicsUtil.switchToWidth(g, 2);
		g.drawRect(x, y, width, 20);
		g.fillRect(x, y, width, 20);
		
		g.setColor(Color.WHITE);
		GraphicsUtil.drawCenteredText(g, title, x + (width / 2), y + offset);

		g.setColor(old);
	}

}
