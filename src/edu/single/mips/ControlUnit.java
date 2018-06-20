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

public class ControlUnit extends InstanceFactory {
	
	
	// ports index
	public static final int INSTRUCTION = 	0;
	public static final int REGDST = 	1;
	public static final int ALUSRC = 	2;
	public static final int MEMTOREG = 	3;
	public static final int REGWRITER = 4;
	public static final int MEMREAD = 	5;
	public static final int MEMWRITE = 	6;
	public static final int BRANCH = 	7;
	public static final int ALUOP = 	8;
	
	private final static int PORTINDEX[] = {
			INSTRUCTION, REGDST, ALUSRC, MEMTOREG, REGWRITER,
			MEMREAD, MEMWRITE, BRANCH, ALUOP
	};
	
	// ports
	private int regDst = 0;
	private int aluSrc = 0;
	private int memToReg = 0;
	private int regWriter = 0;
	private int memRead = 0;
	private int memWrite = 0;
	private int branch = 0;
	private int aluOp = 0;
	
	// helpful
	private int opcode = 0;
	private int aluCodeType = 0;
	private int funct = 0;
	
	public ControlUnit() {
		super("Control Unit");

		// 95% useless (5% = no (other)bugs with attributes
		setAttributes(new Attribute[] { StdAttr.FACING }, new Object[] { Direction.WEST });
		
		Bounds bounds = Bounds.create(-40, -100, 80, 180);
		
		setOffsetBounds(bounds);
		
		int size[] = LalaFunctions.getDistanceFromMiddle(bounds);

		// Configure each port
		Port ps[] = new Port[] {
			new Port(-size[0], 0, Port.INPUT, 32), // Opcode
			new Port(size[0], -70, Port.OUTPUT, 1), // RegDst
			new Port(size[0], -50, Port.OUTPUT, 1), // AluSrcm
			new Port(size[0], -30, Port.OUTPUT, 1), // MemToReg
			new Port(size[0], -10, Port.OUTPUT, 1), // RegWriter
			new Port(size[0], 10, Port.OUTPUT, 1), // MemRead
			new Port(size[0], 30, Port.OUTPUT, 1), // MemWrite
			new Port(size[0], 50, Port.OUTPUT, 1), // Branch
			new Port(size[0], 70, Port.OUTPUT, 4) // Aluop				
		};

		ps[INSTRUCTION].setToolTip(Strings.getter("Opcode: Receive bits 31-26 from opcode"));
		ps[REGDST].setToolTip(Strings.getter("Register Destination"));
		ps[ALUSRC].setToolTip(Strings.getter("Alu Source"));
		ps[MEMTOREG].setToolTip(Strings.getter("Memory to Register"));
		ps[REGWRITER].setToolTip(Strings.getter("Register Writer"));
		ps[MEMREAD].setToolTip(Strings.getter("Memory Read"));
		ps[MEMWRITE].setToolTip(Strings.getter("Memory Write"));
		ps[BRANCH].setToolTip(Strings.getter("Branch"));
		ps[ALUOP].setToolTip(Strings.getter("Alu Op"));

		setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int instruction = state.getPortValue(INSTRUCTION).toIntValue();
		regDst = 0;
		aluSrc = 0;
		memToReg = 0;
		regWriter = 0;
		memRead = 0;
		memWrite = 0;
		branch = 0;
		aluOp = 0;

		aluCodeType = 0x0;
		
		this.opcode = LalaFunctions.getBits(instruction, 26, 6);
		this.funct = LalaFunctions.getBits(instruction, 0, 6);
		
		this.SetControlUnitOutput(opcode);
		aluOp = ControlUnit.computeAluOp(aluCodeType, funct);

		// "convert" the values to the used Value class
		Value values[] = new Value[] {
				null,
				Value.createKnown(BitWidth.create(1), regDst),
				Value.createKnown(BitWidth.create(1), aluSrc),
				Value.createKnown(BitWidth.create(1), memToReg),
				Value.createKnown(BitWidth.create(1), regWriter),
				Value.createKnown(BitWidth.create(1), memRead),
				Value.createKnown(BitWidth.create(1), memWrite),
				Value.createKnown(BitWidth.create(1), branch),
				Value.createKnown(BitWidth.create(4), aluOp),
		};

		int delay = 6;
		
		for(int index : PORTINDEX) {
			if(index != 0) {
				state.setPort(index, values[index], delay);
			}
		}
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
		LalaFunctions.setTitle(painter, this.getName());
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(7, 3, 8, 12);
	}
	
	public static int computeAluOp(int codeType, int funct) {
		int alu_op = 0;
		switch (codeType) {
		case 0x0: // lw | sw
			alu_op = 0x2;
			break;

		case 0x1: // beq
			alu_op = 0x6;
			break;

		case 0x2: // R
		case 0x3:
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
		switch (opcode) {
		case 0x0: // R
			regDst = 1;
			regWriter = 1;
			aluCodeType = 0x2;
			break;

		// case 0x5: // bne
		case 0x4: // beq
			aluCodeType = 1;
		case 0x2: // jump
			branch = 1;
			break;

		case 0x8: // addi
		case 0x9: // addiu
			// case 0xC: // andi
			// case 0xD: // ori
			aluSrc = 1;
			regWriter = 1;
			// Aluop = 0x2; aluop 0 works for addi and addiu
			break;

		case 0x23: // lw
			regWriter = 1;
			aluSrc = 1;
			memToReg = 1;
			memRead = 1;
			break;

		case 0x2B: // sw
			aluSrc = 1;
			memWrite = 1;
			break;

		default:
			break;
		}
	}
}
