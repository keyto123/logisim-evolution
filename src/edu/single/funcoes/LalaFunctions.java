package edu.single.funcoes;

import java.awt.Color;
import java.awt.Graphics;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.util.GraphicsUtil;

import edu.single.mips.ControlUnit;

public class LalaFunctions {
	
	public static void setTitle(InstancePainter painter, InstanceFactory factory) {
		// to maintain color
		Graphics g = painter.getGraphics();
		Color old = g.getColor();
		Bounds bounds = painter.getBounds();
		
		int x = bounds.getX(), y = bounds.getY();
		int width = bounds.getWidth();
		
		g.setColor(Color.BLACK);
		g.drawRect(x - 1, y - 20, width + 1, 19);
		g.fillRect(x, y - 19, width, 18);
		
		g.setColor(Color.GRAY);
		GraphicsUtil.drawCenteredText(g, factory.getName(), x + (width / 2), y - 10);
		
		g.setColor(old);
	}
	
	public static int getAluOp(int op, int funct) {
		int alu_op = 0;
		switch (op) {
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

	public static void SetControlUnitOutput(ControlUnit controlUnit, int opcode) {
		switch (opcode) {
		case 0x0: // R
			controlUnit.RegDst = 1;
			controlUnit.RegWriter = 1;
			controlUnit.Aluop = 0x2;
			break;

		// case 0x5: // bne
		case 0x4: // beq
			controlUnit.Aluop = 1;
		case 0x2: // jump
			controlUnit.Branch = 1;
			break;

		case 0x8: // addi
		case 0x9: // addiu
		// case 0xC: // andi
		// case 0xD: // ori
			controlUnit.AluSrc = 1;
			controlUnit.RegWriter = 1;
			// Aluop = 0x2; aluop 0 works for addi and addiu
			break;

		case 0x23: // lw
			controlUnit.RegWriter = 1;
			controlUnit.AluSrc = 1;
			controlUnit.MemToReg = 1;
			controlUnit.MemRead = 1;
			break;

		case 0x2B: // sw
			controlUnit.AluSrc = 1;
			controlUnit.MemWrite = 1;
			break;
		
		default:
			break;
		}
	}
}
