import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileLayoutComponent } from './profile-layout/profile-layout.component';
import { MyProfileComponent } from './my-profile/my-profile.component'
import { MyAccountComponent } from './my-account/my-account.component';

const routes: Routes = [
    {
        path: '',
        component: ProfileLayoutComponent,
        children: [
            // { path: '', redirectTo: '', pathMatch: 'full' },
            { path: '', component: MyProfileComponent },
            { path: 'my-account', component: MyAccountComponent },
            { path: '**', redirectTo: '' }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProfileRoutingModule { }
