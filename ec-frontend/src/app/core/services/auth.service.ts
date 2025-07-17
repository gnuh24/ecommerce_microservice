import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { TokenService } from './token.service';
import { environment } from '../../../enviro/environment';


@Injectable({ providedIn: 'root' })
export class AuthService {
    private baseUrl = environment.apiUserService;
    private userSubject: BehaviorSubject<LoginResponse | null>;
    public user$: Observable<LoginResponse | null>;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) {
        this.userSubject = new BehaviorSubject<LoginResponse | null>(this.tokenService.getUserInfo());
        this.user$ = this.userSubject.asObservable();
    }

    login(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/login`, data, {
            withCredentials: true
        }).pipe(
            tap(res => {
                this.tokenService.setAccessToken(res.data.token);
                this.tokenService.setRefreshToken(res.data.refreshToken);
                this.tokenService.setUserInfo(res.data);
                this.userSubject.next(res.data);
            })
        );
    }

    loginForStaff(data: LoginRequest): Observable<Response<LoginResponse>> {
        return this.http.post<Response<LoginResponse>>(`${this.baseUrl}/auth/staff-login`, data, {
            withCredentials: true
        }).pipe(
            tap(res => {
                this.tokenService.setAccessToken(res.data.token);
                this.tokenService.setRefreshToken(res.data.refreshToken);
                this.tokenService.setUserInfo(res.data);
                this.userSubject.next(res.data);
            })
        );
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
        this.tokenService.clearTokens();
        this.userSubject.next(null);
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
