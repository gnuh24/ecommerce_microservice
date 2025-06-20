import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Address } from '../../../service/address.service';

@Component({
    selector: 'app-address-form-dialog',
    standalone: false,
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
