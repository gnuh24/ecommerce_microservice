import { Component, OnInit } from '@angular/core';
import { Address, AddressService } from '../../../service/address.service';
import Swal from 'sweetalert2';
import { MatDialog } from '@angular/material/dialog';
import { AddressFormDialogComponent } from '../address-form-dialog/address-form-dialog.component';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Address } from '../../../models/address.model'; // Updated path to model
import { AddressService } from '../../../core/services/address.service'; // Updated path to service
import Swal from 'sweetalert2';
import { MatDialog, MatDialogModule } from '@angular/material/dialog'; // Import MatDialogModule
import { AddressFormDialogComponent } from '../address-form-dialog/address-form-dialog.component';

@Component({
    selector: 'app-my-address',
    standalone: true,
    imports: [CommonModule, RouterModule, MatDialogModule],
    templateUrl: './my-address.component.html',
    styleUrls: [
        './my-address.component.scss',
        '../profile.scss'
    ]
})
export class MyAddressComponent implements OnInit {
    addresses: Address[] = [];
    newAddress: Partial<Address> = {
        fullName: '',
        phone: '',
        address: ''
    };
    isCreating = false;
    showCreateForm = false;
    editingAddressId: string | null = null;
    editedAddress: Partial<Address> = {};

    constructor(
        private addressService: AddressService,
        private dialog: MatDialog
    ) { }

    ngOnInit() {
        this.loadAddresses();
    }

    loadAddresses() {
        this.addressService.getMyAddresses().subscribe({
            next: res => {
                this.addresses = res.data;
            },
            error: () => {
                Swal.fire('Lỗi', 'Không thể tải danh sách địa chỉ', 'error');
            }
        });
    }

    openDialog(isEdit: boolean, address?: Address) {
        const dialogRef = this.dialog.open(AddressFormDialogComponent, {
            width: '400px',
            data: { address }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                if (isEdit && address) {
                    this.addressService.updateAddress(address.id, result).subscribe({
                        next: () => {
                            this.loadAddresses();
                            Swal.fire('Thành công', 'Cập nhật địa chỉ thành công', 'success');
                        },
                        error: () => {
                            Swal.fire('Lỗi', 'Không thể cập nhật địa chỉ', 'error');
                        }
                    });
                } else {
                    this.addressService.createAddress(result).subscribe({
                        next: () => {
                            this.loadAddresses();
                            Swal.fire('Thành công', 'Đã thêm địa chỉ mới', 'success');
                        },
                        error: () => {
                            Swal.fire('Lỗi', 'Không thể thêm địa chỉ', 'error');
                        }
                    });
                }
            }
        });
    }


    setDefaultAddress(address: Address) {
        this.addressService.setDefault(address.id).subscribe({
            next: () => {
                this.loadAddresses();
                Swal.fire('Thành công', 'Đã đặt làm địa chỉ mặc định', 'success');
            },
            error: () => {
                Swal.fire('Lỗi', 'Không thể đặt làm mặc định', 'error');
            }
        });
    }

    deleteAddress(address: Address) {
        Swal.fire({
            title: 'Bạn có chắc?',
            text: 'Xóa địa chỉ này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then(result => {
            if (result.isConfirmed) {
                this.addressService.deleteAddress(address.id).subscribe({
                    next: () => {
                        this.loadAddresses();
                        Swal.fire('Đã xóa', 'Địa chỉ đã được xóa.', 'success');
                    },
                    error: () => {
                        Swal.fire('Lỗi', 'Không thể xóa địa chỉ', 'error');
                    }
                });
            }
        });
    }

}
