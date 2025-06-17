import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../enviro/environment';

import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { TokenService } from './token.service'; // Đảm bảo import đúng path
import { throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AddressService {

    private baseUrl = environment.apiUserService;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService,
        private router: Router
    ) { }

    getMyAddresses(): Observable<{ data: Address[] }> {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.get<{ data: Address[] }>(`${this.baseUrl}/addresses/me`, { headers });
    }

    createAddress(data: Partial<Address>) {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.post(`${this.baseUrl}/addresses/me`, data, { headers });
    }

    updateAddress(id: string, data: Partial<Address>) {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.patch(`${this.baseUrl}/addresses/${id}`, data, { headers });
    }

    setDefault(id: string) {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.patch(`${this.baseUrl}/addresses/${id}/set-default`, {}, { headers });
    }

    deleteAddress(id: string) {
        const headers = this.getAuthHeaders();
        if (!headers) {
            this.handleExpiredSession();
            return throwError(() => new Error('Session expired'));
        }

        return this.http.delete(`${this.baseUrl}/addresses/${id}`, { headers });
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
            this.tokenService.clearTokens();
            this.router.navigate(['/auth/login']);
        });
    }

}

export interface Address {
    id: string;
    address: string;
    phone: string;
    fullName: string;
    isDefault: boolean;
}
