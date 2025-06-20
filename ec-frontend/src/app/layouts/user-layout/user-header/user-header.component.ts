import { Component } from '@angular/core';
import { ElementRef } from '@angular/core';
@Component({
    selector: 'app-user-header',
    standalone: false,
    templateUrl: './user-header.component.html',
    styleUrl: './user-header.component.scss'
})
export class UserHeaderComponent {
    inputModel: any;

    displayMenu = false;

    menuAnchor: any;

    constructor(public el: ElementRef) {
    }

    ngOnInit() {
        this.menuAnchor = this.el.nativeElement;
    }
}
