class TestUnit:

    outputs = ['IsImmediate', 'IsRegister', 'IsTableB', 'IsJump', 'IsBranch', 'IsMem', 'Immediate', 'Offset', 'JumpTarget', 'ALUOpCode', 'ImmediateSelect', 'ImmSignExt', 'CompSign', 'Ra', 'Rb', 'Rd',
                'PCSelect', 'BranchSelect', 'MemLoad', 'MemStore', 'MemWord', 'MemSignExt', 'FuncField', 'SaControl', 'ASelect', 'ExecOut']
    output_lens = ['1', '1', '1', '1', '1', '1', '16', '16', '26', '4', '1', '1', '1', '5', '5', '5', '2', '3', '1', '1', '1', '1', '1', '2', '2', '2']

    def __init__(self, inst, non_zero_outs):
        self.inst = inst
        self.non_zero_outs = non_zero_outs

    def __str__(self):
        res = str(self.inst) + " "
        for out in TestUnit.outputs:
            if out in self.non_zero_outs:
                res += str(self.non_zero_outs[out]) + " "
            else:
                res += '0' + " "
        return res



tests = [
    # ADDIU
    TestUnit("00100101010010010000010100110010", {
        "IsImmediate" : "1",
        "Immediate" : "0000010100110010",
        "ALUOpCode" : "0010",
        "ImmediateSelect" : "1",
        "ImmSignExt" : "1",
        "Ra" : "01010",
        "Rd" : "01001",
    }),
    # ANDI
    TestUnit("00110010010011000000000100100011", {
        "IsImmediate" : "1",
        "Immediate" : "0000000100100011",
        "ALUOpCode" : "1000",
        "ImmediateSelect" : "1",
        "Ra" : "10010",
        "Rd" : "01100",
    }),
    # ORI
    TestUnit("00110101101101100000010001101010", {
        "IsImmediate" : "1",
        "Immediate" : "0000010001101010",
        "ALUOpCode" : "1010",
        "ImmediateSelect" : "1",
        "Ra" : "01101",
        "Rd" : "10110"
    }),
    # XORI
    TestUnit("00111001101101100000010001101010", {
        "IsImmediate" : "1",
        "Immediate" : "0000010001101010",
        "ALUOpCode" : "1100",
        "ImmediateSelect" : "1",
        "Ra" : "01101",
        "Rd" : "10110"
    }),
    # SLTI
    TestUnit("00101001101101100000010001101010", {
        "IsImmediate" : "1",
        "Immediate" : "0000010001101010",
        "ImmediateSelect" : "1",
        "ImmSignExt" : "1",
        "CompSign" : "1",
        "Ra" : "01101",
        "Rd" : "10110",
        "ExecOut" : "10"
    }),
    # SLTIU
    TestUnit("00101101101101100000010001101010", {
        "IsImmediate" : "1",
        "Immediate" : "0000010001101010",
        "ImmediateSelect" : "1",
        "ImmSignExt" : "1",
        "Ra" : "01101",
        "Rd" : "10110",
        "ExecOut" : "10"
    }),



    # ADDU
    TestUnit("00000001101010011011000000100001", {
        "IsRegister" : "1",
        "ALUOpCode" : "0010",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # SUBU
    TestUnit("00000001101010011011000000100011", {
        "IsRegister" : "1",
        "ALUOpCode" : "0111",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # AND
    TestUnit("00000001101010011011000000100100", {
        "IsRegister" : "1",
        "ALUOpCode" : "1000",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # OR
    TestUnit("00000001101010011011000000100101", {
        "IsRegister" : "1",
        "ALUOpCode" : "1010",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # XOR
    TestUnit("00000001101010011011000000100110", {
        "IsRegister" : "1",
        "ALUOpCode" : "1100",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # NOR
    TestUnit("00000001101010011011000000100111", {
        "IsRegister" : "1",
        "ALUOpCode" : "1110",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1"
    }),
    # SLT
    TestUnit("00000001101010011011000000101010", {
        "IsRegister" : "1",
        "CompSign" : "1",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1",
        "ExecOut" : "10"
    }),
    # SLTU
    TestUnit("00000001101010011011000000101011", {
        "IsRegister" : "1",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1",
        "ExecOut" : "10"
    }),



    # MOVN
    TestUnit("00000001101010011011000000001011", {
        "IsRegister" : "1",
        "ALUOpCode" : "1011",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1",
        "ASelect" : "01",
        "ExecOut" : "01"
    }),
    # MOVZ
    TestUnit("00000001101010011011000000001010", {
        "IsRegister" : "1",
        "ALUOpCode" : "1001",
        "Ra" : "01101",
        "Rb" : "01001",
        "Rd" : "10110",
        "FuncField" : "1",
        "ASelect" : "01",
        "ExecOut" : "01"
    }),
]

_ = ['IsImmediate', 'IsRegister', 'IsTableB', 'IsJump', 'IsBranch', 'IsMem', 'Immediate', 
            'Offset', 'JumpTarget', 'ALUOpCode', 'ImmediateSelect', 'ImmSignExt', 'CompSign', 
            'Ra', 'Rb', 'Rd', 'PCSelect', 'BranchSelect', 'MemLoad', 'MemStore', 'MemWord', 
            'MemSignExt', 'FuncField', 'SaControl', 'ASelect', 'ExecOut']

print "Instruction[32] ",
for out, l in zip(TestUnit.outputs, TestUnit.output_lens):
    print out + "[" + str(l) + "] ",
print ""
for test in tests:
    print test