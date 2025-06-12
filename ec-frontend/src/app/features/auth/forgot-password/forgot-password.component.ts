import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../service/auth.service';
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

            // Hiển thị loading
            Swal.fire({
                title: 'Đang xử lý...',
                text: 'Vui lòng đợi trong giây lát',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });

            this.authService.checkUsernameExists(email).subscribe({
                next: (res) => {
                    if (res.data === true) {
                        this.authService.resendForgotPasswordOtp(email).subscribe({
                            next: () => {
                                Swal.close(); // Đóng loading
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Thành công',
                                    text: 'Mã xác thực đã được gửi tới email của bạn.'
                                }).then(() => {
                                    this.router.navigate(['/auth/reset-password'], { queryParams: { email } });
                                });
                            },
                            error: (err) => {
                                Swal.close(); // Đóng loading
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Thất bại',
                                    text: err.error.message || 'Không thể gửi OTP.'
                                });
                            }
                        });
                    } else {
                        Swal.close(); // Đóng loading
                        Swal.fire({
                            icon: 'error',
                            title: 'Thất bại',
                            text: 'Email không tồn tại trong hệ thống !'
                        });
                    }
                },
                error: (err) => {
                    Swal.close(); // Đóng loading
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại',
                        text: err.error.message || 'Email không tồn tại trong hệ thống.'
                    });
                }
            });
        }
    }


}
