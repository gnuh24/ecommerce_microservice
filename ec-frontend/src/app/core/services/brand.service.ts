import { Injectable } from '@angular/core';
import { environment } from '../../../enviro/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { BrandNoPagination } from '../../models/brand.model';

interface ApiResponse<T> {
  status: number;
  message: string;
  data: T;
}
@Injectable({
  providedIn: 'root'
})

export class BrandService {
  private baseUrl = environment.apiUrl; // Re-add baseUrl

  constructor(private http: HttpClient) { }

  getBrandsNoPagination(): Observable<BrandNoPagination[]> {
    return this.http.get<ApiResponse<BrandNoPagination[]>>(`${this.baseUrl}/catalog/brands/no-paging`)
      .pipe(
        map(response => response.data)
      );
  }
}
