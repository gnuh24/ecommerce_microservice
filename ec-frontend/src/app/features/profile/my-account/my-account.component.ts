import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { AuthService } from '../../../service/auth.service'; // Đường dẫn tùy vào project của bạn
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
    standalone: false,
    templateUrl: './my-account.component.html',
    styleUrls: [
        './my-account.component.scss',
        '../profile.scss'
    ]
})
export class MyAccountComponent {
    profile = {
        email: 'example@example.com' // load từ API thực tế
    };

    newEmail = '';
    currentPassword = '';
    newPassword = '';
    confirmPassword = '';

    otpCode = '';
    emailLoading = false;
    otpSent = false;

    countdown = 0;
    private countdownSub?: Subscription;

    constructor(private authService: AuthService) {
        const storedEmail = sessionStorage.getItem('username');
        if (storedEmail) {
            this.profile.email = storedEmail;
        }
    }

    onUpdateEmail() {
        if (!this.newEmail) return;

        this.emailLoading = true;

        // Bước 1: Kiểm tra email có tồn tại chưa
        this.authService.checkUsernameExists(this.newEmail).subscribe({
            next: (exists) => {

                if (exists.data) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Email đã tồn tại',
                        text: 'Vui lòng chọn email khác.'
                    });
                    this.emailLoading = false;
                    return;
                }

                // Bước 2: Gửi OTP
                this.authService.resendUpdateEmailOtp(this.newEmail).subscribe({
                    next: () => {
                        this.emailLoading = false;
                        this.otpSent = true;
                        this.startCountdown();
                        Swal.fire({
                            icon: 'success',
                            title: 'Đã gửi mã OTP',
                            text: `Vui lòng kiểm tra email: ${this.newEmail}.`
                        });
                    },
                    error: () => {
                        this.emailLoading = false;
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
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi kiểm tra email',
                    text: 'Vui lòng thử lại sau.'
                });
            }
        });
    }

    resendOTP() {
        this.authService.resendUpdateEmailOtp(this.newEmail).subscribe({
            next: () => {
                this.startCountdown();
                Swal.fire({
                    icon: 'success',
                    title: 'Đã gửi lại mã OTP',
                    text: 'Vui lòng kiểm tra email của bạn.'
                });
            },
            error: () => {
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: 'Không thể gửi lại mã OTP.'
                });
            }
        });
    }

    onVerifyOTP() {
        const form: UpdateEmailForm = {
            otp: this.otpCode,
            newEmail: this.newEmail
        };

        this.authService.updateEmail(form).subscribe({
            next: () => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Email đã được cập nhật! Vui lòng đăng nhập lại.'
                }).then(() => {
                    this.profile.email = this.newEmail;
                    this.otpSent = false;
                    this.newEmail = '';
                    this.otpCode = '';
                    this.stopCountdown();

                    this.authService.logout();
                });
            },
            error: (err) => {
                Swal.fire({
                    icon: 'error',
                    title: 'OTP không hợp lệ',
                    text: err.error?.message || 'Vui lòng kiểm tra lại.'
                });
            }
        });
    }

    startCountdown() {
        this.countdown = 180; // 180s = 3 phút
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
        if (this.newPassword !== this.confirmPassword) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Mật khẩu mới và xác nhận không khớp!'
            });
            return;
        }

        const form: UpdatePasswordForm = {
            oldPassword: this.currentPassword,
            newPassword: this.newPassword
        };

        this.authService.updatePassword(form).subscribe({
            next: () => {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Đổi mật khẩu thành công!'
                });

                // Reset form
                this.currentPassword = '';
                this.newPassword = '';
                this.confirmPassword = '';
            },
            error: (err) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: err?.error?.message || 'Có lỗi xảy ra khi đổi mật khẩu.'
                });
            }
        });
    }
}
