package edu.single.mips;

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
import com.cburch.logisim.tools.Strings;

import edu.single.funcoes.LalaFunctions;

public class InstructionFetch extends InstanceFactory {

	private int address = 0;
	private boolean oldClockState = false;
	
	public InstructionFetch() {
		super("IF", Strings.getter("Instruction Fetch"));
		
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });
		
		Bounds bounds = Bounds.create(-50, -20, 100, 40);
		int size[] = new int[] { bounds.getWidth() / 2, bounds.getHeight() / 2 };
		
		setOffsetBounds(bounds);
		
		Port ps[] = new Port[] {
				new Port(-size[0], 0, Port.INPUT, 32), // offset
				new Port(20, size[1], Port.INPUT, 1), // reset
				new Port(-20, size[1], Port.INPUT, 1), // clock
				new Port(size[0], 0, Port.OUTPUT, 32), // address
		};
		ps[0].setToolTip(Strings.getter("Offset to be added with default 4 jump"));
		ps[1].setToolTip(Strings.getter("Reset if enabled"));
		ps[2].setToolTip(Strings.getter("Clock input"));
		ps[3].setToolTip(Strings.getter("Output address"));
		this.setPorts(ps);
	}

	public void propagate(InstanceState state) {
		
		int offset = state.getPortValue(0).toIntValue();
		boolean reset = state.getPortValue(1).toIntValue() > 0;
		boolean clockState = state.getPortValue(2).toIntValue() > 0;
		
		
		if(clockState != oldClockState) {
			if(clockState) {
				address += 4;
				if(offset % 4 == 0) {
					address += offset;
				} else {
					JOptionPane.showMessageDialog(null, "offset isn't a valid value: " + offset);
				}
			}
			oldClockState = clockState;
		}
		if(reset) {
			address = 0;
		}
		
		
		Value Address = Value.createKnown(BitWidth.create(32), address);
		
		state.setPort(3, Address, 16);
	}

	public void paintInstance(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.black);
		painter.drawBounds();
		
		painter.drawPort(0, "offset", Direction.EAST);
		painter.drawPort(1, "reset", Direction.SOUTH);
		painter.drawPort(2, "clock", Direction.SOUTH);
		painter.drawPort(3, "address", Direction.WEST);
		LalaFunctions.setTitle(painter, this);
	}
	
	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.black);
		g.drawRect(5, 7, 12, 8);
	}
}
