import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../service/auth.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { getMessageByCode } from '../../../core/system-error-code';

@Component({
    selector: 'app-staff-login',
    templateUrl: './staff-login.component.html',
    standalone: false,
    styleUrls: [
        './staff-login.component.scss',
        '../auth.scss',
    ]
})
export class StaffLoginComponent implements OnInit {
    loginForm!: FormGroup;
    hide = true;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            const loginData = this.loginForm.value;

            this.authService.loginForStaff(loginData).subscribe({
                next: (res) => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Đăng nhập thành công',
                        text: `Chào mừng ${res.data.fullName}!`
                    });

                    sessionStorage.setItem('id', res.data.id);
                    sessionStorage.setItem('token', res.data.token);
                    sessionStorage.setItem('refreshToken', res.data.refreshToken);
                    sessionStorage.setItem('fullName', res.data.fullName);
                    sessionStorage.setItem('username', res.data.username);
                    sessionStorage.setItem('role', res.data.role);

                    this.router.navigate(['/admin']);
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
