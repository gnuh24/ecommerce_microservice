import { Component } from '@angular/core';

@Component({
    selector: 'app-my-account',
    standalone: false,
    templateUrl: './my-account.component.html',
    styleUrl: './my-account.component.scss'
})
export class MyAccountComponent {
    profile = {
        email: 'example@example.com' // load từ API
    };

    newEmail = '';
    currentPassword = '';
    newPassword = '';
    confirmPassword = '';

    onUpdateEmail() {
        // Call API update email
        if (!this.newEmail) return;
        console.log('Gửi email mới:', this.newEmail);
    }

    onUpdatePassword() {
        if (this.newPassword !== this.confirmPassword) {
            alert('Mật khẩu mới và xác nhận không khớp!');
            return;
        }
        console.log('Cập nhật mật khẩu', {
            old: this.currentPassword,
            new: this.newPassword,
        });
    }
}
