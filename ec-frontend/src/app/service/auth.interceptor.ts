import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
    HttpErrorResponse
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { TokenService } from './token.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private readonly PUBLIC_PATHS: string[] = [
        '/api/user/auth/login',
        '/api/user/auth/staff-login',
        '/api/user/auth/register',
        '/api/user/auth/check-username',
        '/api/user/auth/active-account',
        '/api/user/auth/send-reset-password-otp',
        '/api/user/auth/reset-password',
        '/api/user/auth/refresh-token',
        '/api/user/swagger',
        '/api/user/v3/api-docs'
    ];

    constructor(
        private authService: AuthService,
        private tokenService: TokenService,
        private router: Router
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (this.isPublicPath(req.url)) {
            return next.handle(req);
        }

        const token = this.tokenService.getAccessToken();
        let authReq = req;
        if (token) {
            authReq = this.addTokenHeader(req, token);
        }

        return next.handle(authReq).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    return this.handle401Error(authReq, next);
                }
                return throwError(() => error);
            })
        );
    }

    private isPublicPath(url: string): boolean {
        return this.PUBLIC_PATHS.some(path => url.startsWith(path));
    }

    private addTokenHeader(req: HttpRequest<any>, token: string): HttpRequest<any> {
        return req.clone({
            headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
    }

    private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return this.authService.refreshToken().pipe(
            switchMap((response) => {
                const newToken = response.data.token;
                this.tokenService.setAccessToken(newToken);

                const cloned = this.addTokenHeader(req, newToken);
                return next.handle(cloned);
            }),
            catchError((err) => {
                this.tokenService.clearTokens();
                Swal.fire({
                    icon: 'warning',
                    title: 'Thông báo',
                    text: 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.',
                    confirmButtonText: 'Đăng nhập',
                    allowOutsideClick: false,
                    allowEscapeKey: false
                }).then(() => {
                    this.router.navigate(['/auth/login']);
                });

                return throwError(() => err);
            })
        );
    }
}
