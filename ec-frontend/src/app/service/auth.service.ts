import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { TokenService } from './token.service';
import { environment } from '../../enviro/environment';
import { HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';


@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private baseUrl = environment.apiUserService;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) { }

    login(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/login`, data);
    }

    loginForStaff(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/staff-login`, data);
    }

    checkUsernameExists(username: string): Observable<any> {
        return this.http.get(`${this.baseUrl}/auth/check-username?username=${username}`, {});
    }

    activeAccount(otp: string): Observable<any> {

        return this.http.post(`${this.baseUrl}/auth/active-account?otp=${otp}`, {});
    }

    register(data: RegisterRequest): Observable<any> {
        return this.http.post(`${this.baseUrl}/auth/register`, data);
    }

    resetPassword(username: string, data: ResetPasswordForm): Observable<any> {
        return this.http.patch(`${this.baseUrl}/auth/reset-password/${username}`, data);
    }

    resendForgotPasswordOtp(username: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/auth/send-reset-password-otp/${username}`, {});
    }

    updatePassword(data: UpdatePasswordForm): Observable<any> {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.patch(`${this.baseUrl}/auth/update-password`, data, { headers });
    }

    private getAuthHeaders(): HttpHeaders | null {
        const token = this.tokenService.getAccessToken();
        if (!token) return null;

        return new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
    }


    private handleExpiredSession() {
        Swal.fire({
            icon: 'warning',
            title: 'Thông báo',
            text: 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.',
            confirmButtonText: 'Đăng nhập',
            allowOutsideClick: false,
            allowEscapeKey: false
        }).then(() => {
            this.tokenService.clearTokens(); // Xóa access + refresh token nếu có
            this.router.navigate(['/auth/login']);
        });
    }


    // logout(): void {
    //     // Có thể gọi API logout backend hoặc xóa token client-side
    //     localStorage.removeItem('authToken');
    // }

    // Thêm các method khác nếu cần
}

interface LoginRequest {
    username: string;
    password: string;
}

export interface Response<T> {
    status: number,
    message: string;
    data: T;
}


export interface LoginResponse {
    id: string;
    username: string;
    fullName: string;
    role: string;
    token: string;
    tokenExpirationTime: string;
    refreshToken: string;
    refreshTokenExpirationTime: string;
}


interface RegisterRequest {
    username: string;
    password: string;
}

interface ResetPasswordForm {
    otp: string;
    newPassword: string;
}

interface UpdatePasswordForm {
    oldPassword: string;
    newPassword: string;
}
