import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../enviro/environment';
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
    tokenExpirationTime: string;      // vd: "30 phút"
    refreshToken: string;
    refreshTokenExpirationTime: string; // vd: "7 ngày"
}


interface RegisterRequest {
    username: string;
    password: string;
}

interface ResetPasswordForm {
    otp: string;
    newPassword: string;
}

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private baseUrl = environment.apiUserService;

    constructor(private http: HttpClient) { }

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




    // logout(): void {
    //     // Có thể gọi API logout backend hoặc xóa token client-side
    //     localStorage.removeItem('authToken');
    // }

    // Thêm các method khác nếu cần
}
