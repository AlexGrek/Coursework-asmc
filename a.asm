.386
Data1 Segment
val1     dw 056ah
omega    dd 312
alpha    db 'Prodan A.'
beta    equ 5+10
gamma    db 01001101b
Data1 ends

Code1 Segment 
	assume cs : code1, ds : data1
    mov eax, 26h
    cli
    inc al 
    mov dh, 123
@label:
    xor gamma, 0110b
    dec gamma
    cmp eax, edx
    jb @label
    adc eax, omega
    div edx
    mov edi, 3
    and alpha[eax+edi], dl
Code1 ends
end





