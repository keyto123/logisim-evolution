addiu $8, $0, 25
addu $8, $9, $10
addu $9, $11, $12
subu $10, $9, $8
sll $5, $5, 2
j 12
sw $9, 0x0000($0)
bne $8, $9, 12
lw $10, 0x0000($0)
lw $11, 0x0001($0)

