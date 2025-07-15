import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path: '',
        children: [
            { path: '', loadComponent: () => import('./my-profile/my-profile.component').then(m => m.MyProfileComponent) },
            { path: 'my-account', loadComponent: () => import('./my-account/my-account.component').then(m => m.MyAccountComponent) },
            { path: 'my-address', loadComponent: () => import('./my-address/my-address.component').then(m => m.MyAddressComponent) },
            { path: '**', redirectTo: '' }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProfileRoutingModule { }