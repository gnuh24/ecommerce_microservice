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

@Injectable({ providedIn: 'root' })
export class ProfileService {
    private baseUrl = environment.apiUserService;

    constructor(private http: HttpClient) { }

    getMyProfile(): Observable<Response<ProfileDetail>> {
        return this.http.get<Response<ProfileDetail>>(`${this.baseUrl}/profiles/me`);
    }

    updateMyProfile(form: ProfileUpdateForm): Observable<Response<ProfileDetail>> {
        return this.http.patch<Response<ProfileDetail>>(`${this.baseUrl}/profiles/me`, form);
    }
}
