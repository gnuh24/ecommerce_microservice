import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../enviro/environment';
import { ProductListResponse, ProductPageData, ProductListItem } from '../../models/product-page.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = environment.apiUrl + '/catalog'; // Base URL for catalog service

  constructor(private http: HttpClient) { }

  getPublicProducts(page: number, size: number, search: string = '', brandId?: string, categoryId?: string): Observable<ProductListResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }
    if (brandId) {
      params = params.set('brandId', brandId);
    }
    if (categoryId) {
      params = params.set('categoryId', categoryId);
    }

    return this.http.get<ProductListResponse>(`${this.baseUrl}/products/public`, { params });
  }

  getProducts(page: number, size: number, search: string): Observable<ProductPageData> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', search);

    return this.http.get<ProductListResponse>(`${this.baseUrl}/products`, { params }).pipe(
      map(response => response.data)
    );
  }

  getProductsContent(page: number, size: number, search: string): Observable<ProductListItem[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', search);

    return this.http.get<ProductListResponse>(`${this.baseUrl}/products`, { params }).pipe(
      map(response => response.data.content)
    );
  }

  getProductDetailPublic(slug: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/products/public/${slug}`);
  }
}
