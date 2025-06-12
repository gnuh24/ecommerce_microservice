import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    private readonly accessTokenKey = 'token';
    private readonly refreshTokenKey = 'refreshToken';

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

    clearTokens(): void {
        if (typeof window !== 'undefined') {
            sessionStorage.removeItem(this.accessTokenKey);
            sessionStorage.removeItem(this.refreshTokenKey);
        }
    }
}
