import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { take } from 'rxjs/operators';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';

@Component({
    selector: 'app-verify-account',
    standalone: true,
    imports: [CommonModule, RouterModule, SharedModule],
    templateUrl: './verify-account.component.html',
    styleUrls: [
        './verify-account.component.scss',
        '../auth.scss'
    ]
})
export class VerifyAccountComponent implements OnInit {
    message: string = '';
    isLoading: boolean = true;
    isSuccess: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private authService: AuthService,
    ) { }

    ngOnInit(): void {
        this.route.queryParams
            .pipe(take(1))
            .subscribe(params => {
                const otp = params['otp'];
                if (otp) {
                    this.authService.activeAccount(otp).pipe(take(1)).subscribe({
                        next: () => {
                            this.isSuccess = true;
                            this.message = 'Tài khoản của bạn đã được kích hoạt thành công!';
                            this.isLoading = false;
                        },
                        error: () => {
                            this.isSuccess = false;
                            this.message = 'OTP không hợp lệ hoặc đã hết hạn.';
                            this.isLoading = false;
                        }
                    });
                } else {
                    this.isSuccess = false;
                    this.message = 'Thiếu mã xác thực OTP!';
                    this.isLoading = false;
                }
            });
    }
}
