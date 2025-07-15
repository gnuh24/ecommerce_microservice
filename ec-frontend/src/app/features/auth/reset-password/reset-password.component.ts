import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { getMessageByCode } from '../../../core/system-error-code';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';

@Component({
    selector: 'app-reset-password',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule, SharedModule, FormsModule],
    templateUrl: './reset-password.component.html',
    styleUrls: [
        './reset-password.component.scss',
        '../auth.scss'
    ]
})
export class ResetPasswordComponent implements OnInit {
    resetForm!: FormGroup;
    hide1 = true;
    hide2 = true;
    username = '';
    countdown = 0;
    timer: any;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.username = this.route.snapshot.queryParamMap.get('email') || '';

        if (!this.username) {
            Swal.fire('Lỗi', 'Không tìm thấy email để đặt lại mật khẩu', 'error');
            this.router.navigate(['/auth/forgot-password']);
            return;
        }

        this.startCountdown()

        this.resetForm = this.fb.group(
            {
                otp: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
                newPassword: ['', [Validators.required, Validators.minLength(6)]],
                repeatNewPassword: ['', [Validators.required]]
            },
            {
                validators: this.passwordsMatchValidator
            }
        );

    }

    passwordsMatchValidator(form: FormGroup) {
        const pass = form.get('newPassword')?.value;
        const repeat = form.get('repeatNewPassword')?.value;
        return pass === repeat ? null : { mismatch: true };
    }

    onSubmit(): void {
        if (this.resetForm.valid) {
            const { otp, newPassword } = this.resetForm.value;

            this.authService.resetPassword(this.username, {
                otp,
                newPassword
            }).subscribe({
                next: () => {
                    Swal.fire('Thành công', 'Mật khẩu của bạn đã được đặt lại', 'success');
                    this.router.navigate(['/auth/login']);
                },
                error: (err) => {
                    const code = err?.error?.code;
                    const errorMessage = getMessageByCode(code);

                    Swal.fire({
                        icon: 'error',
                        title: 'Không thể đặt lại mật khẩu',
                        text: errorMessage,
                    });
                }
            });
        }
    }

    onResendOtp(): void {
        this.authService.resendForgotPasswordOtp(this.username).subscribe({
            next: () => {
                Swal.fire('Đã gửi lại', 'Mã xác thực đã được gửi tới email của bạn', 'success');
                this.startCountdown();
            },
            error: (err) => {
                const code = err?.error?.code;
                const errorMessage = getMessageByCode(code);

                Swal.fire({
                    icon: 'error',
                    title: 'Không thể gửi lại OTP',
                    text: errorMessage,
                });
            }
        });
    }

    startCountdown(): void {
        this.countdown = 60;
        this.timer = setInterval(() => {
            this.countdown--;
            if (this.countdown <= 0) {
                clearInterval(this.timer);
            }
        }, 1000);
    }
}
