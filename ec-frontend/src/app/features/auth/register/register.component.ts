import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from '../../../service/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { getMessageByCode } from '../../../core/system-error-code';

@Component({
    selector: 'app-register',
    standalone: false,
    templateUrl: './register.component.html',
    styleUrls: [
        './register.component.scss',
        '../auth.scss'
    ]
})
export class RegisterComponent implements OnInit {
    registerForm!: FormGroup;
    hidePassword = true;
    hideConfirm = true;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.registerForm = this.fb.group(
            {
                email: ['', [Validators.required, Validators.email]],
                password: ['', [Validators.required, Validators.minLength(6)]],
                confirmPassword: ['', Validators.required]
            },
            {
                validators: this.passwordMatchValidator
            }
        );
    }

    passwordMatchValidator(form: AbstractControl) {
        const password = form.get('password')?.value;
        const confirmPassword = form.get('confirmPassword')?.value;
        return password === confirmPassword ? null : { passwordMismatch: true };
    }

    onSubmit(): void {
        if (this.registerForm.valid) {
            const formData = {
                username: this.registerForm.value.email,
                password: this.registerForm.value.password
            };

            // Hiển thị loading
            Swal.fire({
                title: 'Đang kiểm tra...',
                text: 'Vui lòng chờ trong giây lát',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });

            // Bước 1: Kiểm tra username đã tồn tại chưa
            this.authService.checkUsernameExists(formData.username).subscribe({
                next: (res) => {
                    const isExisted = res?.data === true;

                    if (isExisted) {
                        Swal.close();
                        Swal.fire({
                            icon: 'warning',
                            title: 'Email đã tồn tại',
                            text: 'Vui lòng sử dụng email khác.'
                        });
                    } else {
                        // Bước 2: Gọi API đăng ký
                        this.authService.register(formData).subscribe({
                            next: () => {
                                Swal.close();
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Đăng ký thành công',
                                    text: 'Vui lòng kiểm tra email để kích hoạt tài khoản.'
                                }).then(() => {
                                    this.router.navigate(['/auth/login']);
                                });
                            },
                            error: (err) => {
                                Swal.close();

                                const code = err?.error?.code;
                                const errorMessage = getMessageByCode(code);
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi đăng ký',
                                    text: errorMessage,
                                });
                            }
                        });
                    }
                },
                error: (err) => {
                    Swal.close();
                    const code = err?.error?.code;
                    const errorMessage = getMessageByCode(code);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi kiểm tra email',
                        text: errorMessage,
                    });
                }
            });
        }
    }

}
