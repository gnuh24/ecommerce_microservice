import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
    { path: 'register', loadComponent: () => import('./register/register.component').then(m => m.RegisterComponent) },
    { path: 'forgot-password', loadComponent: () => import('./forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent) },
    { path: 'reset-password', loadComponent: () => import('./reset-password/reset-password.component').then(m => m.ResetPasswordComponent) },
    { path: 'staff-login', loadComponent: () => import('./staff-login/staff-login.component').then(m => m.StaffLoginComponent) },
    { path: 'verify-account', loadComponent: () => import('./verify-account/verify-account.component').then(m => m.VerifyAccountComponent) },
    { path: '**', redirectTo: 'login' }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthRoutingModule { }