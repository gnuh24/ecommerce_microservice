import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../enviro/environment';

@Injectable({ providedIn: 'root' })
export class AddressService {

    private baseUrl = environment.apiUserService;

    constructor(private http: HttpClient) { }

    getMyAddresses(): Observable<{ data: Address[] }> {
        return this.http.get<{ data: Address[] }>(`${this.baseUrl}/addresses/me`);
    }

    createAddress(data: Partial<Address>) {
        return this.http.post(`${this.baseUrl}/addresses/me`, data);
    }

    updateAddress(id: string, data: Partial<Address>) {
        return this.http.patch(`${this.baseUrl}/addresses/${id}`, data);
    }

    setDefault(id: string) {
        return this.http.patch(`${this.baseUrl}/addresses/${id}/set-default`, {});
    }

    deleteAddress(id: string) {
        return this.http.delete(`${this.baseUrl}/addresses/${id}`);
    }
}

export interface Address {
    id: string;
    address: string;
    phone: string;
    fullName: string;
    isDefault: boolean;
}
