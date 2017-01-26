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
]

print "Instruction[32] ",
for out, l in zip(TestUnit.outputs, TestUnit.output_lens):
    print out + "[" + str(l) + "] ",
print ""
for test in tests:
    print test