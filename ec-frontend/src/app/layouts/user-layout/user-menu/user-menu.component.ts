import { Component } from '@angular/core';

@Component({
    selector: 'app-user-menu',
    standalone: false,
    templateUrl: './user-menu.component.html',
    styleUrls: ['./user-menu.component.scss']
})
export class UserMenuComponent {
    menuTabs = [
        { id: 1, label: 'Women', content: 'Content for Women' },
        { id: 2, label: 'Men', content: 'Content for Men' },
        { id: 3, label: 'Kids', content: 'Content for Kids' },
        { id: 4, label: 'Sale', content: 'SALE info' }
    ];
}
