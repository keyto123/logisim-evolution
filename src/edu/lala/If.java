package edu.lala;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;

public class If extends InstanceFactory {

	private int address = 0;
	private int oldClockState = 0;

	public If() {
		super("IF stage");
		setAttributes(new Attribute[] { StdAttr.WIDTH }, new Object[] { BitWidth.ONE });
		Bounds bounds = Bounds.create(-50, -40, 100, 80);
		setOffsetBounds(bounds);
		
		int distances[] = new int[]{bounds.getWidth() / 2, bounds.getHeight() / 2};
		
		Port ps[] = new Port[] {
			new Port(-distances[0], 0, Port.INPUT, 32), // Offset
			new Port(10, distances[1], Port.INPUT, 1), // Reset
			new Port(-10, distances[1], Port.INPUT, 1), // Clock
			new Port(distances[0], 0, Port.OUTPUT, 32), // Output
		};
		
		setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int offset = state.getPortValue(0).toIntValue();
		int reset = state.getPortValue(1).toIntValue();
		int clk = state.getPortValue(2).toIntValue();

		if (clk != oldClockState) {
			if(clk == 1) {
				int offsetMod = offset >= 0 ? offset : -offset;
				if ((offsetMod % 4) == 0) {
					address += offset;
				} else {
					JOptionPane.showMessageDialog(null, "Invalid argument for offset: " + offset);
				}				
			}
			oldClockState = clk;
		}

		if (reset == 1) {
			address = 0;
		}

		Value out = Value.createKnown(BitWidth.create(32), address);
		state.setPort(3, out, 32);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Bounds bounds = painter.getBounds();
		GraphicsUtil.switchToWidth(painter.getGraphics(), 2);

		int posx = bounds.getX(), posy = bounds.getY();
		int width = bounds.getWidth(), height = bounds.getHeight();
		
		Graphics g = painter.getGraphics();
		
		// figure title
		g.fillRect(posx, posy - 20, width, 20);
		
		g.setColor(Color.WHITE);
		//g.drawString(super.getName(), posx + 2, posy - 5);
		GraphicsUtil.drawCenteredText(g, super.getName(), posx + 15, posy - 15);
		g.setColor(Color.BLACK);
		
		// main figure
		g.drawRect(posx, posy - 20, width, height + 20);

		// each point
		painter.drawPort(0, "offset", Direction.EAST);
		painter.drawPort(1, "rst", Direction.SOUTH);
		painter.drawPort(2, "clk", Direction.SOUTH);
		painter.drawPort(3, "output", Direction.WEST);
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(7, 7, 10, 10);
	}
}
