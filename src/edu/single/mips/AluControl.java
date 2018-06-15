package edu.single.mips;

import java.awt.Color;
import java.awt.Graphics;

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
import com.cburch.logisim.util.GraphicsUtil;

import edu.single.funcoes.LalaFunctions;

public class AluControl extends InstanceFactory {
	public AluControl() {
		super("Alu Control");

		// 95% useless (5% = no (other)bugs with attributes
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });

		Bounds bounds = Bounds.create(-40, -40, 80, 80);
		
		setOffsetBounds(bounds);
		
		int size[] = LalaFunctions.getDistanceFromMiddle(bounds);

		Port ps[] = new Port[] {
				new Port(-size[0], -10, Port.INPUT, 6), // OP
				new Port(-size[0], 30, Port.INPUT, 2), // Funct
				new Port(size[0], 10, Port.OUTPUT, 4), // AluOP
		};
		
		ps[0].setToolTip(Strings.getter("receive bits 0-5 from opcode"));
		ps[1].setToolTip(Strings.getter("Receive AluOp from Control Unit"));
		ps[2].setToolTip(Strings.getter("code output to work with cornell's ALU"));

		setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int FUNCT = state.getPortValue(0).toIntValue();
		int OP = state.getPortValue(1).toIntValue();
		int ALU_OP = 0;

		ALU_OP = LalaFunctions.getAluOp(OP, FUNCT);

		Value alu_op = Value.createKnown(BitWidth.create(4), ALU_OP);

		state.setPort(2, alu_op, 6);
	}

	@Override
	public void paintInstance(InstancePainter painter) {

		// Make the line a little larger
		GraphicsUtil.switchToWidth(painter.getGraphics(), 2);

		// Draw some rect around the unit
		painter.drawBounds();
		
		// Give it a Title!
		LalaFunctions.setTitle(painter, this);

		// Draw each port
		painter.drawPort(0, "funct", Direction.EAST);
		painter.drawPort(1, "op", Direction.EAST);
		painter.drawPort(2, "AluOP", Direction.WEST);
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(7, 3, 8, 12);
	}
}
