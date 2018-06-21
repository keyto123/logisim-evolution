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
 * @author lala
 *
 */
public class LalaFunctions {

	// Created by lala
	public static int getBits(int value, int start, int quantity) {
		if(quantity > 32 || (quantity + start) > 32 || quantity < 0 || start > 31 || start < 0) {
			throw new NumberFormatException("Value size is invalid");
		}
		
		if(quantity == 32) {
			return value;
		}
		
		int end = (start + quantity) - 1;
		value <<= (31 - end);
		value >>>= ((31 - quantity) + 1);
		return value;
	}
	
	// Created by lala
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

	// Created by lala
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

	// Created by lala
	public static void setTitle(InstancePainter painter, String title) {
		// to maintain color
		Graphics g = painter.getGraphics();
		Color old = g.getColor();
		Bounds bounds = painter.getBounds();

		int x = bounds.getX(), y = bounds.getY();
		int width = bounds.getWidth();

		g.setColor(Color.BLACK);
		GraphicsUtil.switchToWidth(g, 2);
		g.drawRect(x, y - 20, width, 19);
		g.fillRect(x, y - 19, width, 19);

		g.setColor(Color.WHITE);
		GraphicsUtil.drawCenteredText(g, title, x + (width / 2), y - 10);

		g.setColor(old);
	}

}
