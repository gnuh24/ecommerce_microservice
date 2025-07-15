import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthService } from '../../../service/auth.service';
import { interval, Subscription } from 'rxjs';

interface UpdatePasswordForm {
    oldPassword: string;
    newPassword: string;
}

interface UpdateEmailForm {
    otp: string;
    newEmail: string;
}

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthService } from '../../../core/services/auth.service'; // Updated path
import { interval, Subscription } from 'rxjs';

interface UpdatePasswordForm {
    oldPassword: string;
    newPassword: string;
}

interface UpdateEmailForm {
    otp: string;
    newEmail: string;
}

@Component({
    selector: 'app-my-account',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule],
    templateUrl: './my-account.component.html',
    styleUrls: [
        './my-account.component.scss',
        '../profile.scss'
    ]
})
export class MyAccountComponent implements OnInit {
    profile = {
        email: 'example@example.com'
    };

    emailForm!: FormGroup;
    otpForm!: FormGroup;
    passwordForm!: FormGroup;

    emailLoading = false;
    otpSent = false;

    countdown = 0;
    private countdownSub?: Subscription;

    constructor(private fb: FormBuilder, private authService: AuthService) {
        const storedEmail = sessionStorage.getItem('username');
        if (storedEmail) {
            this.profile.email = storedEmail;
        }
    }

    ngOnInit(): void {
        this.emailForm = this.fb.group({
            newEmail: ['', [Validators.required, Validators.email]]
        });

        this.otpForm = this.fb.group({
            otp: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
            currentPassword: ['', [Validators.required, Validators.minLength(6)]]
        });

        this.passwordForm = this.fb.group({
            currentPassword: ['', [Validators.required, Validators.minLength(6)]],
            newPassword: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', Validators.required]
        });
    }

    onUpdateEmail() {
        if (this.emailForm.invalid) return;

        const newEmail = this.emailForm.value.newEmail;
        this.emailLoading = true;

        Swal.fire({
            title: 'Đang kiểm tra email...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        this.authService.checkUsernameExists(newEmail).subscribe({
            next: (exists) => {
                if (exists.data) {
                    Swal.close();
                    Swal.fire({
                        icon: 'error',
                        title: 'Email đã tồn tại',
                        text: 'Vui lòng chọn email khác.'
                    });
                    this.emailLoading = false;
                    return;
                }

                this.authService.resendUpdateEmailOtp(newEmail).subscribe({
                    next: () => {
                        this.emailLoading = false;
                        this.otpSent = true;
                        this.startCountdown();
                        Swal.close();
                        Swal.fire({
                            icon: 'success',
                            title: 'Đã gửi mã OTP',
                            text: `Vui lòng kiểm tra email: ${newEmail}.`
                        });
                    },
                    error: () => {
                        this.emailLoading = false;
                        Swal.close();
                        Swal.fire({
                            icon: 'error',
                            title: 'Thất bại',
                            text: 'Không thể gửi mã OTP. Vui lòng thử lại.'
                        });
                    }
                });
            },
            error: () => {
                this.emailLoading = false;
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi kiểm tra email',
                    text: 'Vui lòng thử lại sau.'
                });
            }
        });
    }

    resendOTP() {
        const newEmail = this.emailForm.value.newEmail;

        Swal.fire({
            title: 'Đang gửi lại OTP...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        this.authService.resendUpdateEmailOtp(newEmail).subscribe({
            next: () => {
                this.startCountdown();
                Swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Đã gửi lại mã OTP',
                    text: 'Vui lòng kiểm tra email của bạn.'
                });
            },
            error: () => {
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: 'Không thể gửi lại mã OTP.'
                });
            }
        });
    }

    onVerifyOTP() {
        if (this.otpForm.invalid) return;

        Swal.fire({
            title: 'Đang xác thực...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        const form: UpdateEmailForm & { currentPassword: string } = {
            otp: this.otpForm.value.otp,
            newEmail: this.emailForm.value.newEmail,
            currentPassword: this.otpForm.value.currentPassword
        };

        this.authService.updateEmail(form).subscribe({
            next: () => {
                Swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Email đã được cập nhật! Vui lòng đăng nhập lại.'
                }).then(() => {
                    this.profile.email = this.emailForm.value.newEmail;
                    this.otpSent = false;
                    this.emailForm.reset();
                    this.otpForm.reset();
                    this.stopCountdown();
                    this.authService.logout();
                });
            },
            error: (err) => {
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: err.error?.message || 'OTP hoặc mật khẩu không hợp lệ.'
                });
            }
        });
    }

    startCountdown() {
        this.countdown = 180;
        this.stopCountdown();
        this.countdownSub = interval(1000).subscribe(() => {
            this.countdown--;
            if (this.countdown <= 0) this.stopCountdown();
        });
    }

    stopCountdown() {
        if (this.countdownSub) {
            this.countdownSub.unsubscribe();
        }
    }

    onUpdatePassword() {
        if (this.passwordForm.invalid) return;

        const { currentPassword, newPassword, confirmPassword } = this.passwordForm.value;

        if (newPassword !== confirmPassword) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Mật khẩu mới và xác nhận không khớp!'
            });
            return;
        }

        Swal.fire({
            title: 'Đang đổi mật khẩu...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        const form: UpdatePasswordForm = {
            oldPassword: currentPassword,
            newPassword: newPassword
        };

        this.authService.updatePassword(form).subscribe({
            next: () => {
                Swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Đổi mật khẩu thành công!'
                });
                this.passwordForm.reset();
            },
            error: (err) => {
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: err?.error?.message || 'Có lỗi xảy ra khi đổi mật khẩu.'
                });
            }
        });
    }
}