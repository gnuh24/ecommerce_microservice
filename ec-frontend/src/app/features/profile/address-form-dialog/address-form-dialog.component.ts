import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { Address } from '../../../models/address.model';

@Component({
    selector: 'app-address-form-dialog',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDialogModule],
    templateUrl: './address-form-dialog.component.html',
    styleUrls: ['./address-form-dialog.component.scss']
})
export class AddressFormDialogComponent {
    addressForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private dialogRef: MatDialogRef<AddressFormDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { address?: Address }
    ) {
        this.addressForm = this.fb.group({
            fullName: [data.address?.fullName || '', Validators.required],
            phone: [data.address?.phone || '', [Validators.required, Validators.pattern('^[0-9]{9,11}$')]],
            address: [data.address?.address || '', Validators.required]
        });
    }

    onSubmit() {
        if (this.addressForm.valid) {
            this.dialogRef.close(this.addressForm.value);
        }
    }

    onCancel() {
        this.dialogRef.close();
    }
}
