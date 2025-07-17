import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { getMessageByCode } from '../../../core/system-error-code';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule, SharedModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss', '../auth.scss'],
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;
    hide = true;

    // Inject AuthService qua constructor DI (không new trực tiếp)
    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            const loginData = this.loginForm.value;

            this.authService.login(loginData).subscribe({
                next: (res) => {
                    // Giả sử response có status, message hoặc token
                    Swal.fire({
                        icon: 'success',
                        title: 'Đăng nhập thành công',
                        text: `Chào mừng ${res.data.fullName}!`
                    });

                    // Chuyển hướng đến trang chính hoặc trang người dùng
                    this.router.navigate(['/home']);
                },
                error: (err) => {
                    const code = err?.error?.code;
                    const errorMessage = getMessageByCode(code);

                    Swal.fire({
                        icon: 'error',
                        title: 'Đăng nhập thất bại',
                        text: errorMessage,
                    });
                }

            });
        }


    }
}
