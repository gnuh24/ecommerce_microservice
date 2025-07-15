import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { MAT_DATE_LOCALE, MAT_DATE_FORMATS } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import localeVi from '@angular/common/locales/vi';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/services/auth.interceptor';
registerLocaleData(localeVi);
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';


export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        // ✅ Đây là phần thêm Interceptor đúng cách khi dùng Standalone App
        provideHttpClient(
            withInterceptorsFromDi()
        ),
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },


        // ✅ Cấu hình tiếng Việt cho Datepicker
        { provide: MAT_DATE_LOCALE, useValue: 'vi' },

        // (Tuỳ chọn) Cấu hình định dạng ngày dd/MM/yyyy:
        {
            provide: MAT_DATE_FORMATS,
            useValue: {
                parse: {
                    dateInput: 'DD/MM/YYYY',
                },
                display: {
                    dateInput: 'DD/MM/YYYY',
                    monthYearLabel: 'MMMM YYYY',
                    dateA11yLabel: 'LL',
                    monthYearA11yLabel: 'MMMM YYYY',
                },
            },
        },
    ]
};

