import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { AuthService } from '../../../service/auth.service'; // Đường dẫn tùy vào project của bạn

interface UpdatePasswordForm {
    oldPassword: string;
    newPassword: string;
}

@Component({
    selector: 'app-my-account',
    standalone: false,
    templateUrl: './my-account.component.html',
    styleUrls: ['./my-account.component.scss']
})
export class MyAccountComponent {
    profile = {
        email: 'example@example.com' // load từ API thực tế
    };

    newEmail = '';
    currentPassword = '';
    newPassword = '';
    confirmPassword = '';

    constructor(private authService: AuthService) { }

    onUpdateEmail() {
        if (!this.newEmail) return;

        // Gợi ý: xử lý cập nhật email nếu cần
        console.log('Gửi email mới:', this.newEmail);
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
