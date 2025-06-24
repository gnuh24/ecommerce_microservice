import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MyProfileComponent } from './my-profile/my-profile.component'
import { MyAccountComponent } from './my-account/my-account.component';
import { MyAddressComponent } from './my-address/my-address.component';

const routes: Routes = [
    {
        path: '',
        children: [
            // { path: '', redirectTo: '', pathMatch: 'full' },
            { path: '', component: MyProfileComponent },
            { path: 'my-account', component: MyAccountComponent },
            { path: 'my-address', component: MyAddressComponent },

            { path: '**', redirectTo: '' }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProfileRoutingModule { }
