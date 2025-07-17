import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginResponse } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    private readonly accessTokenKey = 'token';
    private readonly refreshTokenKey = 'refreshToken';
    private readonly userKey = 'user';

    constructor(private router: Router) { }

    getAccessToken(): string | null {
        if (typeof window !== 'undefined') {
            return sessionStorage.getItem(this.accessTokenKey);
        }
        return null;
    }

    setAccessToken(token: string): void {
        if (typeof window !== 'undefined') {
            sessionStorage.setItem(this.accessTokenKey, token);
        }
    }

    removeAccessToken(): void {
        if (typeof window !== 'undefined') {
            sessionStorage.removeItem(this.accessTokenKey);
        }
    }

    getRefreshToken(): string | null {
        if (typeof window !== 'undefined') {
            return sessionStorage.getItem(this.refreshTokenKey);
        }
        return null;
    }

    setRefreshToken(refreshToken: string): void {
        if (typeof window !== 'undefined') {
            sessionStorage.setItem(this.refreshTokenKey, refreshToken);
        }
    }

    removeRefreshToken(): void {
        if (typeof window !== 'undefined') {
            sessionStorage.removeItem(this.refreshTokenKey);
        }
    }

    setUserInfo(user: LoginResponse): void {
        if (typeof window !== 'undefined') {
            sessionStorage.setItem(this.userKey, JSON.stringify(user));
        }
    }

    getUserInfo(): LoginResponse | null {
        if (typeof window !== 'undefined') {
            const user = sessionStorage.getItem(this.userKey);
            return user ? JSON.parse(user) : null;
        }
        return null;
    }

    clearTokens(): void {
        if (typeof window !== 'undefined') {
            sessionStorage.removeItem(this.accessTokenKey);
            sessionStorage.removeItem(this.refreshTokenKey);
            sessionStorage.removeItem(this.userKey);
        }
    }
}
