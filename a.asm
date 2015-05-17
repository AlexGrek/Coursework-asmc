.386
Data1 Segment
val1     dw 0ah
omega    dd 312 
alpha    db 'Prodan A.'
beta    equ 5+(10-8)*2+3
delta equ -2
ulta equ 8
sex equ eax
gamma    db 0101b
Data1 ends

Code1 Segment 
	assume cs : code1, ds : data1
	
	beg:
	
    mov eax, 26h
	mov al, delta
	mov ah, 5+(31-1)*4
	moves:mov dh, 123
	mov edi, 3
	mov esp, beta
	mov ebp, -378
	
	clis:
    cli
	jb clis
	jb incs
	
    inc al 
	inc ah
	inc bl
	inc bh
	inc dh
	inc cl
	inc eax
	inc esi
	incs:inc ebx
	inc ebp
	inc esp
	
	jb incs
	jb step
	step:
	jb step
	jb clis
	jb moves
	jb beg
		
	dec gamma[eax+edx]
	dec fs:omega[ecx+edi]
	dec alpha[esp+esi]
	dec gamma[ebp+ebx]
	dec ss:gamma[ebp+eax]
	
	jb ending
	
	divs:
	div al
	div ah
	div bl
	div bh
	div ch
	div cl
	div eax
	div esi
	div ebx
	div ebp
	div esp
	
	jb beg
	jb divs
	
	adc al, alpha[ecx+esi]
	adc ah, gs:alpha[ebx+esp]
	adc ch, gamma[ebp+edi]
	adc eax, omega[esp+esi]
	adc ecx, omega[ebx+ebx]
	jb divs
	adc esp, omega[ebp+ecx]
    adc edx, cs:omega[ebx+edi]
	
	jb divs
	
	cmp al, ah
	cmp al, al
	cmp ch, dl
	cmp bl, al
	cmp eax, ebx
	cmp ecx, edi
	cmp ebp, esp
	cmp edi, ecx
	
	jb ending
	
	and alpha[ecx+esp], al
	and alpha[ebp+edx], ch
	and gs: gamma[esi+ebx], dl
	and omega[edx+esi], eax
	and omega[esp+ecx], esi
	and gs: omega[edi+ebx], ebp
	
	xor alpha[ecx+esp], beta
	xor alpha[ebp+edx], delta
	xor gs: gamma[esi+ebx], 1
	xor omega[edx+esi], 127h
	xor omega[esp+ecx], -101b
	xor gs: omega[edi+ebx], 345-91-(63-84)/2
	ending:
Code1 ends
end





