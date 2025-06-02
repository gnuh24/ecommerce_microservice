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
    avatar: string | null; // Có thể là null nếu không có avatar
    role: string;
    token: string;
    tokenExpirationTime: string;      // vd: "30 phút"
    refreshToken: string;
    refreshTokenExpirationTime: string; // vd: "7 ngày"
}


interface RegisterRequest {
    email: string;
    password: string;
    fullName?: string;
    // thêm các trường cần thiết
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


    // register(data: RegisterRequest): Observable<any> {
    //     return this.http.post(`${this.baseUrl}/auth/register`, data);
    // }

    // logout(): void {
    //     // Có thể gọi API logout backend hoặc xóa token client-side
    //     localStorage.removeItem('authToken');
    // }

    // Thêm các method khác nếu cần
}
