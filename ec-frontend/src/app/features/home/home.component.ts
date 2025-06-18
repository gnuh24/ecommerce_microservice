import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'app-home',
    standalone: false,
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss'
})
export class HomeComponent {
    responseData: any = null;
    loading = false;
    error: string | null = null;

    constructor(private http: HttpClient) { }

    callRefreshTokenApi(): void {
        this.loading = true;
        this.error = null;
        this.responseData = null;

        this.http.post<any>('http://localhost:8080/api/user/auth/refresh-token', null, { withCredentials: true }).subscribe({
            next: (res) => {
                this.responseData = res;
                this.loading = false;
                console.log('Response from refresh token API:', res);
            },
            error: (err) => {
                this.error = '❌ Lỗi khi gọi API refresh token';
                this.loading = false;
            }
        });
    }
}
