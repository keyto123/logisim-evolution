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

	public static int OFFSETINDEX = 0;
	public static int BRANCHINDEX = 1;
	public static int RESETINDEX = 2;
	public static int CLOCKINDEX = 3;
	public static int ADDRESSINDEX = 4;
	
	private int address = 0;
	private boolean oldClockState = false;
	
	public InstructionFetch() {
		super("IF", Strings.getter("Instruction Fetch"));
		
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });
		
		Bounds bounds = Bounds.create(-50, -40, 100, 80);
		int size[] = LalaFunctions.getDistanceFromMiddle(bounds);
		
		setOffsetBounds(bounds);
		
		Port ps[] = new Port[] {
				new Port(-size[0], -10, Port.INPUT, 32), // offset
				new Port(-size[0], 10, Port.INPUT, 1), // branch
				new Port(20, size[1], Port.INPUT, 1), // reset
				new Port(-20, size[1], Port.INPUT, 1), // clock
				new Port(size[0], 0, Port.OUTPUT, 32), // address
		};
		ps[OFFSETINDEX].setToolTip(Strings.getter("Offset to be added with default 4 jump"));
		ps[BRANCHINDEX].setToolTip(Strings.getter("If branch is set, use offset value to compute address"));
		ps[RESETINDEX].setToolTip(Strings.getter("Reset if enabled"));
		ps[CLOCKINDEX].setToolTip(Strings.getter("Clock input"));
		ps[ADDRESSINDEX].setToolTip(Strings.getter("Output address"));
		this.setPorts(ps);
	}

	public void propagate(InstanceState state) {
		
		int offset = state.getPortValue(OFFSETINDEX).toIntValue();
		boolean reset = state.getPortValue(RESETINDEX).toIntValue() > 0;
		boolean clockState = state.getPortValue(CLOCKINDEX).toIntValue() > 0;
		boolean branch = state.getPortValue(BRANCHINDEX).toIntValue() > 0;
		
		if(clockState != oldClockState) {
			if(clockState) {
				address += 4;
				if(branch) {
					if(offset % 4 == 0) {
						address += offset;						
					} else {
						JOptionPane.showMessageDialog(null, "offset isn't a valid value: " + offset);						
					}
				}
			}
			oldClockState = clockState;
		}
		if(reset) {
			address = 0;
		}
		
		
		Value Address = Value.createKnown(BitWidth.create(32), address);
		
		state.setPort(ADDRESSINDEX, Address, 16);
	}

	public void paintInstance(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.black);
		painter.drawBounds();
		
		painter.drawPort(OFFSETINDEX, "offset", Direction.EAST);
		painter.drawPort(BRANCHINDEX, "branch", Direction.EAST);
		painter.drawPort(RESETINDEX, "reset", Direction.SOUTH);
		painter.drawPort(CLOCKINDEX, "clock", Direction.SOUTH);
		painter.drawPort(ADDRESSINDEX, "address", Direction.WEST);
		LalaFunctions.setTitle(painter, this.getName());
	}
	
	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.black);
		g.drawRect(5, 7, 12, 8);
	}
}
