import { Component, OnInit } from '@angular/core';
import { Address, AddressService } from '../../../service/address.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-my-address',
    standalone: false,
    templateUrl: './my-address.component.html',
    styleUrls: ['./my-address.component.scss']
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

    constructor(private addressService: AddressService) { }

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

    toggleCreateForm() {
        this.showCreateForm = !this.showCreateForm;
    }

    createAddress() {
        if (!this.newAddress.fullName || !this.newAddress.phone || !this.newAddress.address) {
            Swal.fire('Thiếu thông tin', 'Vui lòng điền đầy đủ các trường.', 'warning');
            return;
        }

        this.isCreating = true;
        this.addressService.createAddress(this.newAddress).subscribe({
            next: () => {
                this.newAddress = { fullName: '', phone: '', address: '' };
                this.isCreating = false;
                this.showCreateForm = false;
                this.loadAddresses();
                Swal.fire('Thành công', 'Đã thêm địa chỉ mới', 'success');
            },
            error: () => {
                this.isCreating = false;
                Swal.fire('Lỗi', 'Không thể thêm địa chỉ', 'error');
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

    startEdit(address: Address) {
        this.editingAddressId = address.id;
        this.editedAddress = { ...address };
    }

    cancelEdit() {
        this.editingAddressId = null;
        this.editedAddress = {};
    }

    updateAddress() {
        if (!this.editingAddressId) return;

        this.addressService.updateAddress(this.editingAddressId, this.editedAddress).subscribe({
            next: () => {
                this.loadAddresses();
                this.cancelEdit();
                Swal.fire('Thành công', 'Cập nhật địa chỉ thành công', 'success');
            },
            error: () => {
                Swal.fire('Lỗi', 'Không thể cập nhật địa chỉ', 'error');
            }
        });
    }
}
