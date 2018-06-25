addu $8, $9, $10
addiu $8, $9, -10
ori $8, $9, 0
andi $8, $9, 0
addu $9, $11, $12
subu $10, $9, $8
sw $9, 0x0000($0)
label: lw $11, 0x0001($0)
j 0
bne $0, $0, 0
beq $0, $0, 0
