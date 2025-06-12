import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileLayoutComponent } from './profile-layout/profile-layout.component';
import { MyProfileComponent } from './my-profile/my-profile.component'

const routes: Routes = [
    {
        path: '',
        component: ProfileLayoutComponent,
        children: [
            // { path: '', redirectTo: '', pathMatch: 'full' },
            { path: '', component: MyProfileComponent },
            { path: '**', redirectTo: '' }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProfileRoutingModule { }
