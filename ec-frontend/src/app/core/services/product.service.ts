import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../enviro/environment'; // Re-import environment
import { ProductListResponse, ProductPageData, ProductListItem } from '../../models/product-page.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = environment.apiUrl; // Re-add baseUrl

  constructor(private http: HttpClient) { } // Inject HttpClient directly

  getProducts(page: number, size: number, search: string): Observable<ProductPageData> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', search);

    // Sử dụng http.get trực tiếp
    return this.http.get<ProductListResponse>(`${this.baseUrl}/products`, { params }).pipe(
      map(response => response.data)
    );
  }

  getProductsContent(page: number, size: number, search: string): Observable<ProductListItem[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', search);

    // Sử dụng http.get trực tiếp
    return this.http.get<ProductListResponse>(`${this.baseUrl}/products`, { params }).pipe(
      map(response => response.data.content)
    );
  }
}
