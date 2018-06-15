package edu.single.mips;

import java.awt.Color;
import java.awt.Graphics;

import com.bfh.logisim.fpgaboardeditor.Strings;
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

import edu.single.funcoes.LalaFunctions;

public class ControlUnit extends InstanceFactory {
	
	public int RegDst = 0;
	public int AluSrc = 0;
	public int MemToReg = 0;
	public int RegWriter = 0;
	public int MemRead = 0;
	public int MemWrite = 0;
	public int Branch = 0;
	public int Aluop = 0;
	
	public ControlUnit() {
		super("Control Unit");

		// 95% useless (5% = no (other)bugs with attributes
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });
		
		Bounds bounds = Bounds.create(-40, -100, 80, 180);
		
		setOffsetBounds(bounds);
		
		int size[] = LalaFunctions.getDistanceFromMiddle(bounds);

		// Configure each port
		Port ps[] = new Port[9];
		ps[0] = new Port(-size[0], 0, Port.INPUT, 6); // Opcode
		ps[1] = new Port(size[0], -70, Port.OUTPUT, 1); // RegDst
		ps[2] = new Port(size[0], -50, Port.OUTPUT, 1); // AluSrc
		ps[3] = new Port(size[0], -30, Port.OUTPUT, 1); // MemToReg
		ps[4] = new Port(size[0], -10, Port.OUTPUT, 1); // RegWriter
		ps[5] = new Port(size[0], 10, Port.OUTPUT, 1); // MemRead
		ps[6] = new Port(size[0], 30, Port.OUTPUT, 1); // MemWrite
		ps[7] = new Port(size[0], 50, Port.OUTPUT, 1); // Branch
		ps[8] = new Port(size[0], 70, Port.OUTPUT, 2); // Aluop

		ps[0].setToolTip(Strings.getter("Opcode: Receive bits 31-26 from opcode"));
		ps[1].setToolTip(Strings.getter("Register Destination"));
		ps[2].setToolTip(Strings.getter("Alu Source"));
		ps[3].setToolTip(Strings.getter("Memory to Register"));
		ps[4].setToolTip(Strings.getter("Register Writer"));
		ps[5].setToolTip(Strings.getter("Memory Read"));
		ps[6].setToolTip(Strings.getter("Memory Write"));
		ps[7].setToolTip(Strings.getter("Branch"));
		ps[8].setToolTip(Strings.getter("Alu Op"));

		setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int opcode = state.getPortValue(0).toIntValue();
		RegDst = 0;
		AluSrc = 0;
		MemToReg = 0;
		RegWriter = 0;
		MemRead = 0;
		MemWrite = 0;
		Branch = 0;
		Aluop = 0;

		LalaFunctions.SetControlUnitOutput(this, opcode);

		// "convert" the values to the used Value class
		Value regdst = Value.createKnown(BitWidth.create(1), RegDst);
		Value alusrc = Value.createKnown(BitWidth.create(1), AluSrc);
		Value memtoreg = Value.createKnown(BitWidth.create(1), MemToReg);
		Value regwriter = Value.createKnown(BitWidth.create(1), RegWriter);
		Value memread = Value.createKnown(BitWidth.create(1), MemRead);
		Value memwrite = Value.createKnown(BitWidth.create(1), MemWrite);
		Value branch = Value.createKnown(BitWidth.create(1), Branch);
		Value aluop = Value.createKnown(BitWidth.create(2), Aluop);

		// ??
		int delay = 6;

		// Set values to the ports
		state.setPort(1, regdst, delay);
		state.setPort(2, alusrc, delay);
		state.setPort(3, memtoreg, delay);
		state.setPort(4, regwriter, delay);
		state.setPort(5, memread, delay);
		state.setPort(6, memwrite, delay);
		state.setPort(7, branch, delay);
		state.setPort(8, aluop, delay);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		// Make the line a little larger
		GraphicsUtil.switchToWidth(painter.getGraphics(), 2);

		// Draw a rect around the unit
		painter.drawBounds();

		// Draw each port
		painter.drawPort(0, "OP", Direction.EAST);
		painter.drawPort(1, "RegDst", Direction.WEST);
		painter.drawPort(2, "ALUsrc", Direction.WEST);
		painter.drawPort(3, "MtoR", Direction.WEST);
		painter.drawPort(4, "RWriter", Direction.WEST);
		painter.drawPort(5, "MRead", Direction.WEST);
		painter.drawPort(6, "MWrite", Direction.WEST);
		painter.drawPort(7, "Branch", Direction.WEST);
		painter.drawPort(8, "Aluop", Direction.WEST);

		// Give it a Title!
		LalaFunctions.setTitle(painter, this);
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(7, 3, 8, 12);
	}
}
