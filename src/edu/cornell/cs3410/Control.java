package edu.cornell.cs3410;

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
import com.cburch.logisim.util.StringGetter;

import java.util.List;
import java.util.Arrays;

public class Control extends InstanceFactory {
	
	static final int NUM_OUTPUTS = 27;
	static final int CONTROL_WIDTH = 200;
	static final int SPACING = 30;
	static final int CONTROL_HEIGHT = (NUM_OUTPUTS + 1) * SPACING;
    // Need to align the input pin on a multiple of 10
    static final int INPUT_HEIGHT = CONTROL_HEIGHT % 20 == 0 ? CONTROL_HEIGHT/2 : CONTROL_HEIGHT / 2 + 5;
	private List<Port> out_ports;
	private List<String> out_port_names;
    private Port in_port;
    private int ports_made = 0;
	
	Control() {
        super("Control");
        //setAttributes(new Attribute[] { 32 },
        //new Object[] { BitWidth.create(8) });
        setOffsetBounds(Bounds.create(0, 0, CONTROL_WIDTH, CONTROL_HEIGHT));
        out_port_names = Arrays.asList(
            "IsImmediate",
            "IsRegister",
            "IsTableB",
            "IsJump",
            "IsBranch",
            "IsMem",
            "Immediate",
            "Offset",
            "JumpTarget",
            "ShiftAmount",
            "ALUOpCode",
            "ImmediateSelect",
            "ImmSignExt",
            "CompSign",
            "Ra",
            "Rb",
            "Rd",
            "PC Select",
            "Branch Select",
            "MemLoad",
            "MemStore",
            "MemWord",
            "MemSignExt",
            "FuncField",
            "SaControl",
            "ASelect",
            "ExecOut"
        );
        out_ports = Arrays.asList(
            get_out_port(1), //IsImmediate
            get_out_port(1), //IsRegister
            get_out_port(1), //IsTableB
            get_out_port(1),
            get_out_port(1),
            get_out_port(1),
            get_out_port(16), //Immediate
            get_out_port(16), //Offset
            get_out_port(26), //JumpTarget
            get_out_port(5), //ShiftAmount
            get_out_port(4), //ALUOpCode
            get_out_port(1),
            get_out_port(1),
            get_out_port(1),
            get_out_port(5), //Ra
            get_out_port(5),
            get_out_port(5),
            get_out_port(2), //PC Select
            get_out_port(3),
            get_out_port(1), //MemLoad
            get_out_port(1),
            get_out_port(1),
            get_out_port(1),
            get_out_port(1), //FuncField
            get_out_port(2),
            get_out_port(2),
            get_out_port(2)  //ExecOut
        );

        in_port = new Port(0, INPUT_HEIGHT, Port.INPUT, 32);
        
        Port[] all_ports = new Port[NUM_OUTPUTS + 1];
        out_ports.toArray(all_ports);
        
        assert(all_ports[NUM_OUTPUTS] == null);
        // Add the input port to the end of the list
        all_ports[NUM_OUTPUTS] = in_port;
        
        setPorts(all_ports);
    }

    private Port get_out_port(int bit_width) {
        return new Port(CONTROL_WIDTH, get_height(), Port.OUTPUT, bit_width);
    }
	
	private int get_height() {
        ports_made++;
		return SPACING * this.ports_made;
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawRectangle(painter.getBounds(), "Control");
		for(int i = 0; i < out_ports.size(); i++) {
			painter.drawPort(i, out_port_names.get(i), Direction.WEST);
		}
        // Input port is the last port
        painter.drawPort(NUM_OUTPUTS, "Instruction", Direction.EAST);
	}

    private Value getIsImmediate(int instruction) {
        // Get first 6 bits of instruction
        int opcode = instruction >>> 26;
        if (opcode >= 0b001001 && opcode <= 0b001111) {
            return getValue(1, 1);
        } else {
            return getValue(0, 1);
        }
    }

    private Value getIsRegister(int instruction) {
        // Get first 6 bits of instruction
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        if (opcode == 0b0 && lastSixBits != 0b001000 && lastSixBits != 0b001001) {
            return getValue(1, 1);
        } else {
            return getValue(0, 1);
        }
    }

    private Value getIsTableB(int instruction) {
        int opcode = instruction >>> 26;
        if (getIsJump(instruction).toIntValue() == 1) {
            return getValue(1,1);
        }
        if (getIsBranch(instruction).toIntValue() == 1) {
            return getValue(1,1);
        }
        if (getIsMem(instruction).toIntValue() == 1) {
            return getValue(1,1);
        }
        return getValue(0,1);
    }

    private Value getIsJump(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode == 0b000010 || opcode == 0b000011) {
            return getValue(1,1);
        }
        int lastSixBits = instruction & 0b111111;
        if ((lastSixBits == 0b001000 || lastSixBits == 0b001001) && opcode == 0b0) {
            return getValue(1,1);
        }
        return getValue(0,1);
    }

    private Value getIsBranch(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode >= 0b000100 && opcode <= 0b000111) {
            return getValue(1,1);
        }
        if (opcode == 0b000001) {
            return getValue(1,1);
        }
        return getValue(0,1);
    }

    private Value getIsMem(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode >= 0b100000 && opcode <= 0b101011) {
            return getValue(1,1);
        }
        return getValue(0,1);
    }

    private Value getImmediate(int instruction) {
        if (getIsImmediate(instruction).toIntValue() == 1) {
            // First 16 bits are the immediate for an I type instruction
            return getValue(instruction & 0b1111111111111111, 16);
        } else {
            return getValue(0,16);
        }
    }

    private Value getOffset(int instruction) {
        if (getIsBranch(instruction).toIntValue() == 1 || getIsMem(instruction).toIntValue() == 1) {
            return getValue(instruction & 0b1111111111111111, 16);
        } else {
            return getValue(0,16);
        }
    }

    private Value getJumpTarget(int instruction) {
        int opcode = instruction >>> 26;
        // J and JAL
        if (opcode == 0b000010 || opcode == 0b000011) {
            return getValue(instruction & 0b11111111111111111111111111, 26);
        } else {
            return getValue(0, 26);
        }
    }

    private Value getShiftAmount(int instruction) {
        if (getIsRegister(instruction).toIntValue() == 1) {
            //Shamt is bits 6-10 for Shift instruction, but guaranteed to be 0 for non-shift R type instructions
            return getValue(instruction >>> 6 & 0b11111, 5);
        } else {
            return getValue(0,5);
        }
    }

    private Value getALUOpCode(int instruction) {
        int opcode = instruction >>> 26;
        switch (opcode) {
            case 0b001001: // ADDIU
                return getValue(0b0010, 4);
            case 0b001100: // ANDI
                return getValue(0b1000, 4);
            case 0b001101: // ORI
                return getValue(0b1010, 4);
            case 0b001110: // XORI
                return getValue(0b1100, 4);
            default:
                break;
        }
        if(opcode == 0b0) {
            switch (instruction & 0b111111) {
                case 0b000000: // SLL
                case 0b000100:
                    return getValue(0b0001, 4);
                case 0b000010: // SRL
                case 0b000110:
                    return getValue(0b0100, 4);
                case 0b000011: // SRA
                case 0b000111:
                    return getValue(0b0101, 4);
                case 0b100001: // ADDU
                    return getValue(0b0010, 4);
                case 0b100011: // SUBU
                    return getValue(0b0111, 4);
                case 0b100100: // AND
                    return getValue(0b1000, 4);
                case 0b100101: // OR
                    return getValue(0b1010, 4);
                case 0b100110: // XOR
                    return getValue(0b1100, 4);
                case 0b100111: //NOR
                    return getValue(0b1110, 4);
                case 0b001011: // MOVN
                    return getValue(0b1011, 4);
                case 0b001010: // MOVZ
                    return getValue(0b1001, 4);
                default:
                    break;
            }
        }
        if(getIsBranch(instruction).toIntValue() == 1 || getIsMem(instruction).toIntValue() == 1) {
            // Return 001x on branch instruction
            return getValue(0b0011, 4);
        }
        // We don't need an opcode from the ALU
        return getValue(0b0000, 4);
    }

    private Value getImmediateSelect(int instruction) {
        if(getIsImmediate(instruction).toIntValue() == 1 || 
           getIsMem(instruction).toIntValue() == 1 || 
           getIsBranch(instruction).toIntValue() == 1) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getImmSignExt(int instruction) {
        int opcode = instruction >>> 26;
        if((opcode >= 0b001001 && opcode <= 0b001011) || 
           getIsMem(instruction).toIntValue() == 1 || 
           getIsBranch(instruction).toIntValue() == 1 ) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getCompSign(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        if (opcode == 0b001010 || (opcode == 0b0000000 && lastSixBits == 0b101010) || getIsBranch(instruction).toIntValue() == 1) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getRa(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        if(opcode == 0b000010 || opcode == 0b000011 || opcode == 0b001111) {
            return getValue(0,5);
        }
        if(opcode == 0b0 && (lastSixBits >= 0b0 && lastSixBits <= 0b000011)) {
            return getValue(0,5);
        }
        else {
            // Get bits 21-25
            int ra = (instruction >>> 21) & 0b11111;
            return getValue(ra, 5);
        }
    }

    private Value getRb(int instruction) {
        int opcode = instruction >>> 26;
        if(opcode == 0b001111 || getIsJump(instruction).toIntValue() == 1 || getIsImmediate(instruction).toIntValue() == 1 ||
            opcode == 0b000001) {
            return getValue(0,5);
        }
        else {
            // Get bits 16-20
            int ra = (instruction >>> 16) & 0b11111;
            return getValue(ra, 5);
        }
    }

    private Value getRd(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        if(getIsImmediate(instruction).toIntValue() == 1 || getIsMem(instruction).toIntValue() == 1) {
            // Get bits 16-20
            int rd = (instruction >>> 16) & 0b11111;
            return getValue(rd, 5);
        }
        else if (getIsBranch(instruction).toIntValue() == 1 || (opcode == 0 && lastSixBits == 0b001000) || opcode == 0b000010) {
            return getValue(0,5);
        }
        else if (opcode == 0b000011) {
            return getValue(0b11111, 5);
        }
        else {
            // Get bits 11-15
            int rd = (instruction >>> 11) & 0b11111;
            return getValue(rd, 5);
        }
    }

    private Value getPCSelect(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        // JR and JALR
        if(opcode == 0 && (lastSixBits == 0b001000 || lastSixBits == 0b001001)) {
            return getValue(0b01, 2);
        }
        // J and JAL
        else if (opcode == 0b000010 || opcode == 0b000011) {
            return getValue(0b10, 2);
        }
        else if (getIsBranch(instruction).toIntValue() == 1) {
            return getValue(0b11, 2);
        }
        else {
            return getValue(0, 2);
        }
    }

    private Value getBranchSelect(int instruction) {
        int opcode = instruction >>> 26;
        switch (opcode) {
            case 0b000100: // BEQ
                return getValue(0b101, 3);
            case 0b000101: // BNE
                return getValue(0b110, 3);
            case 0b000110: // BLEZ
                return getValue(0b100, 3);
            case 0b000111: // BGTZ
                return getValue(0b001, 3);
            case 0b000001: // BLTZ and BGEZ
                int op = (instruction >>> 16) & 0b11111;
                if (op == 0) {
                    return getValue(0b010, 3);
                } else if (op == 1) {
                    return getValue(0b011, 3);
                }
            default:
                return getValue(0,3);
        }
    }

    private Value getMemLoad(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode == 0b100000 || opcode == 0b100011 || opcode == 0b100100) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getMemStore(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode == 0b101000 || opcode == 0b101011) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getMemWord(int instruction) {
        int opcode = instruction >>> 26;
        if (opcode == 0b101011 || opcode == 0b100011) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getMemSignExt(int instruction) {
        int opcode = instruction >>> 26;
        if(opcode == 0b100000) {
            return getValue(1,1);
        }
        else {
            return getValue(0,1);
        }
    }

    private Value getFuncField(int instruction) {
        int opcode = instruction >>> 26;
        if(opcode == 0b0) {
            // switch (instruction & 0b111111) {
            //     case 0b000000: // SLL
            //     case 0b000010: // SRL
            //     case 0b000011: // SRA
            //     case 0b000100: // SLLV
            //     case 0b
            //     case 0b100001: // ADDU
            //     case 0b100011: // SUBU
            //     case 0b100100: // AND
            //     case 0b100101: // OR
            //     case 0b100110: // XOR
            //     case 0b100111: // NOR
            //     case 0b101010: // SLT
            //     case 0b101011: // SLTU
            //     case 0b001011: // MOVN
            //     case 0b001010: // MOVZ
            //     case 0b001000: // JR
            //     case 0b001001: // JALR
            //         return getValue(1,1);
            //     default:
            //         return getValue(0,1);
            // }
            return getValue(1,1);
        }
        return getValue(0,1);
    }

    private Value getSaControl(int instruction) {
        int opcode = instruction >>> 26;
        if(opcode == 0b001111) {
            return getValue(0b01, 2);
        }
        if(opcode == 0b0) {
            switch (instruction & 0b111111) {
                case 0b000000: // SLL
                case 0b000010: // SRL
                case 0b000011: // SRA
                    return getValue(0b10, 2);
                case 0b000100: // SLLV
                case 0b000110: // SRLV
                case 0b000111: // SRAV
                    return getValue(0b11, 2);
                default:
                    break;
            }
        }
        return getValue(0,2);
    }

    private Value getASelect(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        // MOVN and MOVZ
        if(opcode == 0b0 && (lastSixBits == 0b001011 || lastSixBits == 0b001010)) {
            return getValue(0b01, 2);
        }
        else if(getIsBranch(instruction).toIntValue() == 1) {
            return getValue(0b10, 2);
        }
        else {
            return getValue(0,2);
        }
    }

    private Value getExecOut(int instruction) {
        int opcode = instruction >>> 26;
        int lastSixBits = instruction & 0b111111;
        // SLTI and SLTIU
        if(opcode == 0b001010 || opcode == 0b001011) {
            return getValue(0b10, 2);
        }
        // SLT and SLTU
        if(opcode == 0b0 && (lastSixBits == 0b101011 || lastSixBits == 0b101010)) {
            return getValue(0b10, 2);
        }
        // MOVN and MOVZ
        if(opcode == 0b0 && (lastSixBits == 0b001011 || lastSixBits == 0b001010)) {
            return getValue(0b01, 2);
        }
        // JAL and JALR
        if(opcode == 0b000011 || (opcode == 0 && lastSixBits == 0b001001)) {
            return getValue(0b11, 2);
        }
        return getValue(0,2);
    }


    private Value getValue(int n, int width){
        return Value.createKnown(BitWidth.create(width), n);
    }

	@Override
	public void propagate(InstanceState state) {
		Value instruction_value = state.getPortValue(NUM_OUTPUTS);

        // Check for valid input, set outputs to errors or unknowns if input isn't valid
        if(instruction_value.isErrorValue()) {
            for(int i = 0; i < out_ports.size(); i++) {
                Value out = Value.createError(out_ports.get(i).getFixedBitWidth());
                state.setPort(i, out, out.getWidth());
            }
            return;
        }
        else if (!instruction_value.isFullyDefined()) {
            for(int i = 0; i < out_ports.size(); i++) {
                Value out = Value.createUnknown(out_ports.get(i).getFixedBitWidth());
                state.setPort(i, out, out.getWidth());
            }
            return;
        }
        int instruction = instruction_value.toIntValue();
        for(int i = 0; i < out_ports.size(); i++) {
            Value out;
            switch(out_port_names.get(i)) {
                case "IsImmediate":
                    out = getIsImmediate(instruction);
                    break;
                case "IsRegister":
                    out = getIsRegister(instruction);
                    break;
                case "IsTableB":
                    out = getIsTableB(instruction);
                    break;
                case "IsJump":
                    out = getIsJump(instruction);
                    break;
                case "IsBranch":
                    out = getIsBranch(instruction);
                    break;
                case "IsMem":
                    out = getIsMem(instruction);
                    break;
                case "Immediate":
                    out = getImmediate(instruction);
                    break;
                case "Offset":
                    out = getOffset(instruction);
                    break;
                case "JumpTarget":
                    out = getJumpTarget(instruction);
                    break;
                case "ShiftAmount":
                    out = getShiftAmount(instruction);
                    break;
                case "ALUOpCode":
                    out = getALUOpCode(instruction);
                    break;
                case "ImmediateSelect":
                    out = getImmediateSelect(instruction);
                    break;
                case "ImmSignExt":
                    out = getImmSignExt(instruction);
                    break;
                case "CompSign":
                    out = getCompSign(instruction);
                    break;
                case "Ra":
                    out = getRa(instruction);
                    break;
                case "Rb":
                    out = getRb(instruction);
                    break;
                case "Rd":
                    out = getRd(instruction);
                    break;
                case "PC Select":
                    out = getPCSelect(instruction);
                    break;
                case "Branch Select":
                    out = getBranchSelect(instruction);
                    break;
                case "MemLoad":
                    out = getMemLoad(instruction);
                    break;
                case "MemStore":
                    out = getMemStore(instruction);
                    break;
                case "MemWord":
                    out = getMemWord(instruction);
                    break;
                case "MemSignExt":
                    out = getMemSignExt(instruction);
                    break;
                case "FuncField":
                    out = getFuncField(instruction);
                    break;
                case "SaControl":
                    out = getSaControl(instruction);
                    break;
                case "ASelect":
                    out = getASelect(instruction);
                    break;
                case "ExecOut":
                    out = getExecOut(instruction);
                    break;
                default:
                    out = Value.createError(out_ports.get(i).getFixedBitWidth());
            }
            state.setPort(i,out, out.getWidth());
        }
	}

}
