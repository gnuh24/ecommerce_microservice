import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

@Component({
    selector: 'app-home',
    standalone: false,
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss'
})

export class HomeComponent implements OnInit {
    inputModel: any;

    constructor() {
    }

    ngOnInit() {
    }
}
