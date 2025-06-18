import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../../service/profile.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-my-profile',
    standalone: false,
    templateUrl: './my-profile.component.html',
    styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit {
    profile: any = {
        email: '',
        fullName: '',
        phone: '',
        gender: 'MALE',
        birthday: ''
    };

    genders: string[] = ['MALE', 'FEMALE', 'OTHER'];

    constructor(private profileService: ProfileService) { }

    ngOnInit(): void {
        Swal.fire({
            title: 'Đang tải thông tin...',
            allowOutsideClick: false,
            didOpen: () => Swal.showLoading()
        });

        this.profileService.getMyProfile().subscribe({
            next: (res) => {
                this.profile = res.data;
                Swal.close();
            },
            error: (err) => {
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi tải thông tin',
                    text: err.error?.message || 'Không thể tải thông tin cá nhân.'
                });
            }
        });
    }

    onSubmit(): void {
        // Kiểm tra trường bắt buộc
        if (!this.profile.fullName || !this.profile.birthday || !this.profile.gender) {
            Swal.fire({
                icon: 'warning',
                title: 'Thiếu thông tin',
                text: 'Vui lòng điền đầy đủ họ tên, ngày sinh và giới tính.'
            });
            return;
        }

        Swal.fire({
            title: 'Đang cập nhật...',
            text: 'Vui lòng chờ trong giây lát',
            allowOutsideClick: false,
            didOpen: () => Swal.showLoading()
        });

        this.profileService.updateMyProfile(this.profile).subscribe({
            next: () => {
                Swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Cập nhật thành công',
                    text: 'Thông tin cá nhân đã được cập nhật.'
                });
            },
            error: (err) => {
                Swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi cập nhật',
                    text: err.error?.message || 'Không thể cập nhật thông tin.'
                });
            }
        });
    }
}
