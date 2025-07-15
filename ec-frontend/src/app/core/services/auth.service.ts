import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { TokenService } from './token.service';
import { environment } from '../../../enviro/environment';


@Injectable({ providedIn: 'root' })
export class AuthService {
    private baseUrl = environment.apiUserService;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) { }

    login(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/login`, data, {
            withCredentials: true
        });
    }

    loginForStaff(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/staff-login`, data, {
            withCredentials: true
        });
    }

    refreshToken(): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/refresh-token`, null, {
            withCredentials: true
        });
    }

    checkUsernameExists(username: string): Observable<any> {
        return this.http.get(`${this.baseUrl}/auth/check-username?username=${username}`);
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

    resendUpdateEmailOtp(username: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/auth/send-update-email-otp/${username}`, {});
    }

    updatePassword(data: UpdatePasswordForm): Observable<any> {
        return this.http.patch(`${this.baseUrl}/auth/update-password`, data);
    }

    updateEmail(data: UpdateEmailForm): Observable<any> {
        return this.http.patch(`${this.baseUrl}/auth/update-email`, data);
    }

    logout(): void {
        sessionStorage.clear();
        localStorage.clear();
        this.router.navigate(['/auth/login']);
    }
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

interface UpdateEmailForm {
    otp: string;
    newEmail: string;
}
