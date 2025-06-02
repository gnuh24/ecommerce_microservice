import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';


export const routes: Routes = [

    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },

    {
        path: 'auth',
        loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
    },

    // {
    //     path: 'admin',
    //     loadChildren: () =>
    //         import('./features/admin/router/admin.modules').then(
    //             (m) => m.AdminModule
    //         ),
    // },
    // { path: 'home', component: HomeComponent },
    // { path: 'lich-trinh', component: TripSearchComponent },
    // { path: 'tra-cuu-ve', component: TicketLookupComponent },
    // { path: 'profile', component: ProfileComponent },
    // { path: 'payment', component: PaymentComponent },
    // { path: 'trip-detail/:id', component: TripDetailComponent },
    // { path: 'login', component: LoginComponent },
    // { path: 'signup', component: SignupComponent },
    // { path: 'forget-password', component: ForgetPasswordComponent },
    // { path: 'verify-password', component: VerifyPasswordComponent },

    // {
    //     path: 'order-history',
    //     component: OrderHistoryComponent,
    //     canActivate: [() => !!sessionStorage.getItem('user_data')],
    // },
    // { path: 'order-history/invoice/:id', component: InvoiceDetailFormComponent },

    { path: '**', redirectTo: '/home' },

];

