import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { ProfileRoutingModule } from './profile-routing.module';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { MyAccountComponent } from './my-account/my-account.component';
import { MyAddressComponent } from './my-address/my-address.component';
import { ProfileMenuComponent } from './profile-menu/profile-menu.component';

import { UserLayoutModule } from '../../layouts/user-layout/user-layout.module';

@NgModule({
    declarations: [
        MyProfileComponent,
        MyAccountComponent,
        MyAddressComponent,
        ProfileMenuComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ProfileRoutingModule,
        UserLayoutModule,

        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatButtonModule,
        MatCardModule,
        MatProgressSpinnerModule,
        MatListModule,
        MatRadioModule,
        MatDatepickerModule,
        MatNativeDateModule
    ]
})
export class ProfileModule { }
