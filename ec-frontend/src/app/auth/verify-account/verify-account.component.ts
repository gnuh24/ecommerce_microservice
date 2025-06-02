import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../service/auth.service'; // Cập nhật đường dẫn nếu khác


@Component({
    selector: 'app-verify-account',
    standalone: false,
    templateUrl: './verify-account.component.html',
    styleUrls: ['./verify-account.component.scss']
})
export class VerifyAccountComponent implements OnInit {
    message: string = '';
    isLoading: boolean = true;
    isSuccess: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            const otp = params['otp'];
            if (otp) {
                this.authService.activeAccount(otp).subscribe({
                    next: () => {
                        this.isSuccess = true;
                        this.message = 'Tài khoản của bạn đã được kích hoạt thành công!';
                        this.isLoading = false;
                    },
                    error: (err) => {
                        this.isSuccess = false;
                        this.message = 'OTP không hợp lệ hoặc đã hết hạn.';
                        this.isLoading = false;
                    }
                });
            } else {
                this.message = 'Thiếu mã xác thực OTP!';
                this.isLoading = false;
            }
        });
    }
}
