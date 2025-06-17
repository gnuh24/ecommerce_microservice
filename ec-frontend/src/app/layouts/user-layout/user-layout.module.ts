import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { UserHeaderComponent } from './user-header/user-header.component';
import { UserFooterComponent } from './user-footer/user-footer.component';
import { UserLayoutComponent } from './user-layout.component';
import { RouterModule } from '@angular/router'; // ✅ Thêm dòng này

@NgModule({
    declarations: [UserHeaderComponent, UserFooterComponent, UserLayoutComponent],
    imports: [
        CommonModule,
        MatToolbarModule,
        RouterModule // ✅ Thêm vào đây để dùng <router-outlet>
    ],
    exports: [UserLayoutComponent]
})
export class UserLayoutModule { }
