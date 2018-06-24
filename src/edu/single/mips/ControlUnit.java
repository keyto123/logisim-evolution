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

import edu.single.funcoes.LsdFunctions;

public class ControlUnit extends InstanceFactory {

	// ports index
	public static final byte OPCODEINDEX = 0;
	public static final byte FUNCTINDEX = 1;
	public static final byte REGWRITERINDEX = 2;
	public static final byte MEMTOREGINDEX = 3;
	public static final byte MEMWRITEINDEX = 4;
	public static final byte MEMREADINDEX = 5;
	public static final byte ALUOPINDEX = 6;
	public static final byte ALUSRCINDEX = 7;
	public static final byte REGDSTINDEX = 8;
	public static final byte BRANCHINDEX = 9;

	private static final int IMMEDIATE = -1;

	// ports
	private byte regWriter = 0;
	private byte memToReg = 0;
	private byte memWrite = 0;
	private byte memRead = 0;
	private byte aluOp = 0;
	private byte aluSrc = 0;
	private byte regDst = 0;
	private byte branch = 0;

	// helpful
	private int opcode = 0;
	private int aluCodeType = 0;
	private int funct = 0;

	public ControlUnit() {
		super("Control Unit");

		// 95% useless (5% = no (other)bugs with attributes
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });

		Bounds bounds = Bounds.create(-60, -100, 120, 180);

		setOffsetBounds(bounds);

		int size[] = LsdFunctions.getDistanceFromMiddle(bounds);

		// Configure each port
		Port ps[] = new Port[] { 
				new Port(-size[0], -20, Port.INPUT, 6), // Opcode
				new Port(-size[0], 0, Port.INPUT, 6), // Funct
				new Port(size[0], -70, Port.OUTPUT, 1), // RegWriter
				new Port(size[0], -50, Port.OUTPUT, 1), // MemToReg
				new Port(size[0], -30, Port.OUTPUT, 1), // MemWrite
				new Port(size[0], -10, Port.OUTPUT, 1), // MemRead
				new Port(size[0], 10, Port.OUTPUT, 4), // Aluop
				new Port(size[0], 30, Port.OUTPUT, 1), // AluSrc
				new Port(size[0], 50, Port.OUTPUT, 1), // RegDst
				new Port(size[0], 70, Port.OUTPUT, 1), // Branch
		};

		ps[OPCODEINDEX].setToolTip(Strings.getter("Opcode: Receive bits 31-26 from opcode"));
		ps[REGDSTINDEX].setToolTip(Strings.getter("Register Destination"));
		ps[ALUSRCINDEX].setToolTip(Strings.getter("Alu Source"));
		ps[MEMTOREGINDEX].setToolTip(Strings.getter("Memory to Register"));
		ps[REGWRITERINDEX].setToolTip(Strings.getter("Register Writer"));
		ps[MEMREADINDEX].setToolTip(Strings.getter("Memory Read"));
		ps[MEMWRITEINDEX].setToolTip(Strings.getter("Memory Write"));
		ps[BRANCHINDEX].setToolTip(Strings.getter("Branch"));
		ps[ALUOPINDEX].setToolTip(Strings.getter("Alu Op"));

		setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		this.opcode = state.getPortValue(OPCODEINDEX).toIntValue();
		this.funct = state.getPortValue(FUNCTINDEX).toIntValue();
		regDst = 0;
		aluSrc = 0;
		memToReg = 0;
		regWriter = 0;
		memRead = 0;
		memWrite = 0;
		aluOp = -1;
		branch = 0;

		aluCodeType = 0x0;

		this.SetControlUnitOutput(opcode);
		if (aluOp == -1)
			aluOp = ControlUnit.computeAluOp(aluCodeType, funct);

		int delay = 6;

		state.setPort(REGWRITERINDEX, Value.createKnown(BitWidth.create(1), regWriter), delay);
		state.setPort(MEMTOREGINDEX, Value.createKnown(BitWidth.create(1), memToReg), delay);
		state.setPort(MEMWRITEINDEX, Value.createKnown(BitWidth.create(1), memWrite), delay);
		state.setPort(MEMREADINDEX, Value.createKnown(BitWidth.create(1), memRead), delay);
		state.setPort(ALUOPINDEX, Value.createKnown(BitWidth.create(4), aluOp), delay);
		state.setPort(ALUSRCINDEX, Value.createKnown(BitWidth.create(1), aluSrc), delay);
		state.setPort(REGDSTINDEX, Value.createKnown(BitWidth.create(1), regDst), delay);
		state.setPort(BRANCHINDEX, Value.createKnown(BitWidth.create(1), branch), delay);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		// Make the line a little larger
		GraphicsUtil.switchToWidth(painter.getGraphics(), 2);

		// Draw a rect around the unit
		painter.drawBounds();

		// Draw each port
		painter.drawPort(OPCODEINDEX, "opcode", Direction.EAST);
		painter.drawPort(FUNCTINDEX, "funct", Direction.EAST);
		painter.drawPort(REGWRITERINDEX, "RegWriter", Direction.WEST);
		painter.drawPort(MEMTOREGINDEX, "MemtoReg", Direction.WEST);
		painter.drawPort(MEMWRITEINDEX, "MemWrite", Direction.WEST);
		painter.drawPort(MEMREADINDEX, "MemRead", Direction.WEST);
		painter.drawPort(ALUOPINDEX, "AluOp", Direction.WEST);
		painter.drawPort(ALUSRCINDEX, "AluSrc", Direction.WEST);
		painter.drawPort(BRANCHINDEX, "Branch", Direction.WEST);
		painter.drawPort(REGDSTINDEX, "RegDst", Direction.WEST);

		// Give it a Title!
		LsdFunctions.setTitle(painter, this.getName());
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(7, 3, 8, 12);
	}

	public static byte computeAluOp(int codeType, int funct) {
		byte alu_op = 0;
		switch (codeType) {
		case 0x0: // lw | sw
			alu_op = 0x2;
			break;

		case 0x1: // beq
			alu_op = 0x6;
			break;

		case 0x2: // R
		case 0x3: // ?
			switch (funct) {
			case 0x0: // sll(shift left logical)
				alu_op = 0x0;
				break;

			case 0x2: // srl(shift right logical)
				alu_op = 0x4;
				break;

			case 0x3: // sra(shift right arithmetic)
				alu_op = 0x5;
				break;

			case 0x20: // add
				alu_op = 0x2;
				break;

			case 0x21: // addu
				alu_op = 0x3;
				break;

			case 0x22: // sub
				alu_op = 0x6;
				break;

			case 0x23: // subu
				alu_op = 0x7;
				break;

			case 0x24: // and
				alu_op = 0x8;
				break;

			case 0x25: // or
				alu_op = 0xA;
				break;

			case 0x26: // xor
				alu_op = 0xC;
				break;

			case 0x27: // nor
				alu_op = 0xE;
				break;
			}
		}
		return alu_op;
	}

	private void SetControlUnitOutput(int opcode) {
		if (opcode >= 0x08 && opcode <= 0x0F) {
			SetControlUnitOutput(IMMEDIATE);
		}
		switch (opcode) {
		case 0x00: // R
			regDst = 1;
			regWriter = 1;
			aluCodeType = 0x2;
			break;

		case 0x5: // bne
		case 0x04: // beq
			aluCodeType = 1;
		case 0x02: // jump
			branch = 1;
			break;

		case 0x08: // addi
			aluOp = 0x02;
			break;

		case 0x09: // addiu
			aluOp = 0x3;
			break;

		case 0x0A: // slti
			aluOp = 0x02;
			break;

		case 0x0B: // sltiu
			aluOp = 0x02;
			break;

		case 0x0C: // andi
			aluOp = 0x08;
			break;

		case 0x0D: // ori
			aluOp = 0x0A;
			break;

		case 0x0F: // lui
			aluOp = 0x02;
			break;

		case IMMEDIATE:
			aluSrc = 1;
			regWriter = 1;
			break;

		case 0x23: // lw
			regWriter = 1;
			aluSrc = 1;
			memToReg = 1;
			memRead = 1;
			aluCodeType = 0;
			break;

		case 0x2B: // sw
			aluCodeType = 0;
			aluSrc = 1;
			memWrite = 1;
			break;

		default:
			break;
		}
	}
}
