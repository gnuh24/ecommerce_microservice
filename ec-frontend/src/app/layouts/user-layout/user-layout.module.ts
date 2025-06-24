import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // ✅ ngModel
import { RouterModule, RouterOutlet } from '@angular/router';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip'; // (optional)

import { UserHeaderComponent } from './user-header/user-header.component';
import { UserFooterComponent } from './user-footer/user-footer.component';
import { UserLayoutComponent } from './user-layout.component';
import { UserMenuComponent } from './user-menu/user-menu.component';

@NgModule({
  declarations: [
    UserHeaderComponent,
    UserFooterComponent,
    UserLayoutComponent,
    UserMenuComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatToolbarModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule,
    RouterOutlet,
  ],
  exports: [UserLayoutComponent],
})
export class UserLayoutModule {}
