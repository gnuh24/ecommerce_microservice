import { Injectable } from '@angular/core';
import { environment } from '../../../enviro/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { CategoryNoPagination } from '../../models/category.model';

interface ApiResponse<T> {
  status: number;
  message: string;
  data: T;
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private baseUrl = environment.apiUrl; // Re-add baseUrl

  constructor(private http: HttpClient) { }

  getCategoriesNoPagination(): Observable<CategoryNoPagination[]> {
    return this.http.get<ApiResponse<CategoryNoPagination[]>>(`${this.baseUrl}/catalog/categories/no-paging`)
      .pipe(
        map(response => response.data)
      );
  }
}
