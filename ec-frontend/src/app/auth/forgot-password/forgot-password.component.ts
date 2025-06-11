import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
    selector: 'app-forgot-password',
    standalone: false,
    templateUrl: './forgot-password.component.html',
    styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
    forgotForm!: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.forgotForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]]
        });
    }

    onSubmit(): void {
        if (this.forgotForm.valid) {
            const email = this.forgotForm.value.email;

            this.authService.checkUsernameExists(email).subscribe({
                next: () => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Mã xác thực đã được gửi tới email của bạn.'
                    });
                    // Chuyển sang trang verify OTP (tuỳ bạn đặt route)
                    this.router.navigate(['/auth/verify-otp'], { queryParams: { email } });
                },
                error: (err) => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại',
                        text: err.error.message || 'Không thể gửi email khôi phục.'
                    });
                }
            });
        }
    }
}
