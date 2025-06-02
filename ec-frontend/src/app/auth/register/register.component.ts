import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { tap, finalize } from 'rxjs/operators';

@Component({
    selector: 'app-register',
    standalone: false,
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
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
                password: ['', Validators.required],
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



            this.authService.register(formData)
                .pipe(
                    tap(() => {
                        // Hiển thị loading ngay khi bắt đầu
                        Swal.fire({
                            title: 'Đang xử lý...',
                            text: 'Vui lòng chờ trong giây lát',
                            allowOutsideClick: false,
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        });
                    }),
                    finalize(() => {
                        // Kết thúc loading (dù thành công hay lỗi)
                        Swal.close();
                    })
                )
                .subscribe({
                    next: (res) => {
                        Swal.fire({
                            icon: 'success',
                            title: 'Đăng ký thành công',
                            // text: res.message || 'Vui lòng kiểm tra email để kích hoạt tài khoản.'
                            text: 'Vui lòng kiểm tra email để kích hoạt tài khoản.'

                        });
                        this.router.navigate(['/auth/login']);
                    },
                    error: (err) => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi đăng ký',
                            text: err.error.message || 'Có lỗi xảy ra, vui lòng thử lại.'
                        });
                    }
                });
        }
    }
}
