import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
    selector: 'app-staff-login',
    standalone: false,
    templateUrl: './staff-login.component.html',
    styleUrl: './staff-login.component.scss'
})
export class StaffLoginComponent implements OnInit {
    loginForm!: FormGroup;
    hide = true;

    // Inject AuthService qua constructor DI (không new trực tiếp)
    constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

    ngOnInit(): void {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            const loginData = this.loginForm.value;

            this.authService.loginForStaff(loginData).subscribe({
                next: (res) => {
                    // Giả sử response có status, message hoặc token
                    Swal.fire({
                        icon: 'success',
                        title: 'Đăng nhập thành công',
                        text: `Chào mừng ${res.data.fullName}!`
                    });

                    // Lưu token vào localStorage hoặc sessionStorage
                    sessionStorage.setItem('id', res.data.id);
                    sessionStorage.setItem('token', res.data.token);
                    sessionStorage.setItem('refreshToken', res.data.refreshToken);
                    sessionStorage.setItem('fullName', res.data.fullName);
                    sessionStorage.setItem('username', res.data.username);
                    sessionStorage.setItem('role', res.data.role);
                    sessionStorage.setItem('avatar', res.data.avatar || ''); // Nếu không có avatar thì để rỗng
                    // Chuyển hướng đến trang chính hoặc trang người dùng
                    this.router.navigate(['/admin']);
                },
                error: (err) => {
                    // Hiển thị lỗi từ backend trả về
                    Swal.fire({
                        icon: 'error',
                        title: 'Đăng nhập thất bại',
                        text: err.error.message || 'Lỗi không xác định'
                    });
                }
            });
        }
    }
}
