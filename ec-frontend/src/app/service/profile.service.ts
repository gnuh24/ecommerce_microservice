import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../enviro/environment';
import { TokenService } from './token.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

export interface Response<T> {
    status: number;
    message: string;
    data: T;
}

export interface ProfileDetail {
    id: string;
    fullName: string;
    email: string;
    phone: string;
    gender: 'MALE' | 'FEMALE' | 'OTHER';
    birthday: string;
}

export interface ProfileUpdateForm {
    fullName: string;
    phone: string;
    gender: 'MALE' | 'FEMALE' | 'OTHER';
    birthday: string;
}

@Injectable({
    providedIn: 'root',
})
export class ProfileService {
    private baseUrl = environment.apiUserService;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) { }

    private getAuthHeaders(): HttpHeaders | null {
        const token = this.tokenService.getAccessToken();
        if (!token) return null;

        return new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
    }

    getMyProfile(): Observable<Response<ProfileDetail>> {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.get<Response<ProfileDetail>>(
            `${this.baseUrl}/profiles/me`,
            { headers }
        );
    }

    updateMyProfile(form: ProfileUpdateForm): Observable<Response<ProfileDetail>> {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.patch<Response<ProfileDetail>>(
            `${this.baseUrl}/profiles/me`,
            form,
            { headers }
        );
    }



    private handleExpiredSession() {
        Swal.fire({
            icon: 'warning',
            title: 'Thông báo',
            text: 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.',
            confirmButtonText: 'Đăng nhập'
        }).then(() => {
            this.tokenService.clearTokens();
            this.router.navigate(['/auth/login']);
        });
    }
}
3