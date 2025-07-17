import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

@Component({
    selector: 'app-profile-menu',
    standalone: true,
    imports: [CommonModule, RouterModule, MatIconModule,MatListModule],
    templateUrl: './profile-menu.component.html',
    styleUrl: './profile-menu.component.scss'
})
export class ProfileMenuComponent {

}
